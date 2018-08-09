package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Pools;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.chattylabs.sdk.android.common.DimensionUtils;
import com.chattylabs.sdk.android.voice.VoiceInteractionComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.chattylabs.sdk.android.voice.VoiceInteractionComponent.*;

final class ChatAssistantComponentImpl extends ChatFlow.Edge implements ChatAssistantComponent {

    private static final long DEFAULT_MESSAGE_DELAY = 500L;

    @VisibleForTesting
    static final String ASSISTANT_LAST_SAVED_NODE = "ASSISTANT_LAST_SAVED_NODE";
    public static final int LOADING_DISPLAY_DELAY = 1000;

    private final Pools.Pool<ArrayList<ChatNode>> mListPool = new Pools.SimplePool<>(10);
    private final SimpleArrayMap<ChatNode, ArrayList<ChatNode>> graph = new SimpleArrayMap<>();
    private final LinearLayoutManager layoutManager;
    private final SharedPreferences sharedPreferences;
    private final VoiceInteractionComponent component;
    private final SpeechSynthesizer speechSynthesizer;
    private final SpeechRecognizer speechRecognizer;
    private final ChatAssistantAdapter adapter;

    private Timer timer = new Timer();
    private TimerTask task;
    private Handler loadingHandler = new Handler(Looper.getMainLooper());
    private Handler voiceInteractionHandler = new Handler(Looper.getMainLooper());
    private Handler scheduleHandler = new Handler(Looper.getMainLooper());
    private boolean paused;
    private ChatNode currentNode;
    private ChatAction lastAction;
    private boolean enableVoiceInteraction;
    private boolean voiceInteractionInitialized;
    private boolean voiceInteractionInitializing;

    ChatAssistantComponentImpl(Builder builder) {
        RecyclerView recyclerView = builder.recyclerView;
        if (recyclerView.getItemDecorationCount() == 0) {
            int space = DimensionUtils.getDimension(
                    recyclerView.getContext(),
                    TypedValue.COMPLEX_UNIT_DIP, 4);
            VerticalSpaceItemDecoration spaceItemDecoration = new VerticalSpaceItemDecoration(space);
            recyclerView.addItemDecoration(spaceItemDecoration);
        }
        component = builder.voiceInteractionComponent;
        speechSynthesizer = component.getSpeechSynthesizer(recyclerView.getContext());
        speechRecognizer = component.getSpeechRecognizer(recyclerView.getContext());
        layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        layoutManager.setSmoothScrollbarEnabled(false);
        recyclerView.setItemAnimator(null);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(recyclerView.getContext());
        adapter = new ChatAssistantAdapter((view, action) -> {
            setNode(action);
            lastAction = action;
            if (!action.keepAction) {
                resumeLastAction();
            }
            component.stop();
            if (action.onSelected != null) {
                action.onSelected.execute(action);
            }
            if (!action.skipTracking) {
                trackLastAction();
            }
            if (!action.stopFlow) {
                next();
            }
        }, new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initialize(ChatNode rootNode) {
        String lastSavedNodeId = getLastVisitedNode(rootNode);
        ChatNode lastSavedNode = getNode(lastSavedNodeId);
        adapter.addItem(rootNode);
        traverse(adapter.getItems(), rootNode, lastSavedNode);
        currentNode = lastSavedNode;
        next();
    }

    @Override
    public void enableVoiceInteraction(boolean enable) {
        this.enableVoiceInteraction = enable;
    }

    @Override
    public void addNode(@NonNull ChatNode node) {
        if (!graph.containsKey(node)) {
            graph.put(node, null);
        }
    }

    @Override
    public ChatFlow create() {
        return new ChatFlow(this);
    }

    @Override
    public void next() {
        show(getNext(), DEFAULT_MESSAGE_DELAY, true);
    }

    @Override
    public boolean isVoiceInteractionInitialized() {
        return voiceInteractionInitialized;
    }

    @Override
    public void prepareForVoiceInteraction(Context context, OnSuccess onSuccess, OnError onError) {
        voiceInteractionInitializing = true;
        showLoading();
        component.setup(context, status -> {
            boolean isSynthesizerAvailable =
                    status.getSynthesizerStatus() == SYNTHESIZER_AVAILABLE;
            if (status.isAvailable() || isSynthesizerAvailable) {
                voiceInteractionInitialized = true;
                voiceInteractionHandler.post(() -> {
                    hideLoading();
                    onSuccess.execute(status.isAvailable() ?
                            SYNTHESIZER_AVAILABLE +
                                    RECOGNIZER_AVAILABLE :
                            SYNTHESIZER_AVAILABLE);
                });
            }
            else {
                // FIXME: Probably a Dagger issue
                // Second time it exists the App seems like the TTS is still alive, so triedTtsData
                // value is TRUE, causing checkAvailability never to be called.
                // TODO: P2 - to show TTS error screen
                voiceInteractionHandler.post(() -> {
                    hideLoading();
                    onError.execute(status.getSynthesizerStatus() | status.getRecognizerStatus());
                });
            }
        });
    }

    @Override
    public void release() {
        cancel();
        if (loadingHandler != null) {
            loadingHandler.removeCallbacksAndMessages(null);
        }
        if (voiceInteractionHandler != null) {
            voiceInteractionHandler.removeCallbacksAndMessages(null);
        }
        if (scheduleHandler != null) {
            scheduleHandler.removeCallbacksAndMessages(null);
        }
        if (component != null) {
            component.shutdown();
        }
    }

    private void cancel() {
        if (timer != null) {
            if (task != null) {
                task.cancel();
            }
            timer.cancel();
            timer = new Timer();
        }
        if (component != null) {
            component.stop();
        }
    }

    @Override
    public void pause() {
        paused = true;
        cancel();
    }

    @Override
    public void resume() {
        if (paused) {
            paused = false;
            if (task != null) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (task != null) {
                            task.run();
                        }
                    }
                }, task.scheduledExecutionTime());
            }
        }
    }

    @Override
    public void showLoading() {
        loadingHandler.postDelayed(() -> {
            int size = adapter.getItemCount();
            if (size == 0 || !ChatLoading.class.isInstance(adapter.getItem(size - 1))) {
                adapter.addItem(new ChatLoading());
                layoutManager.scrollToPosition(size);
            }
        }, LOADING_DISPLAY_DELAY);
    }

    @Override
    public void hideLoading() {
        if (loadingHandler != null) loadingHandler.removeCallbacksAndMessages(null);
        int position = adapter.getLastPositionOf(ChatLoading.class);
        if (position != -1) adapter.removeItem(position);
    }

    @Override
    public void trackLastAction() {
        ArrayList<ChatNode> outgoingEdges = getOutgoingEdges(lastAction);
        if (outgoingEdges != null && !outgoingEdges.isEmpty()) {
            if (outgoingEdges.size() == 1) {
                ChatNode node = outgoingEdges.get(0);
                if (ChatMessage.class.isInstance(node)) {
                    setLastVisitedNode(node);
                }
                else {
                    throw new IllegalStateException("An Action can only have " +
                                                    "an edge to a Message");
                }
            }
            else {
                throw new IllegalStateException("An Action cannot have multiple " +
                                                "edges in the graph");
            }
        }
    }

    @Override
    public void resumeLastAction() {
        ChatAction action = (ChatAction) getNode();
        removeLast();
        if (action != null)
            addLast(new ChatMessage.Builder().setText(action.text2 == null ? action.text1 : action.text2)
                                     .setImage(action.image).setTintColor(action.tintColor)
                                     .setTextSize(action.textSize).setShowAsAnswer(true).build());
    }

    @Override
    public void addLast(ChatMessage build) {
        adapter.addItem(build);
    }

    @Override
    public void removeLast() {
        if (adapter.getItemCount() > 0) adapter.removeItem(adapter.getItemCount() - 1);
    }

    @Override
    public void setLastVisitedNode(ChatNode node) {
        sharedPreferences.edit().putString(ASSISTANT_LAST_SAVED_NODE, node.getId()).apply();
    }

    @Override
    public String getLastVisitedNode(ChatNode node) {
        return sharedPreferences.getString(ASSISTANT_LAST_SAVED_NODE, node.getId());
    }

    @Override
    public ChatNode getNode() {
        return currentNode;
    }

    @Override
    public void setNode(ChatNode node) {
        this.currentNode = node;
    }

    @Override
    public ChatNode getNext() {
        ArrayList<ChatNode> outgoingEdges = getOutgoingEdges(currentNode);
        if (outgoingEdges == null || outgoingEdges.isEmpty()) {
            return null;
        }

        if (outgoingEdges.size() == 1) {
            ChatNode node = outgoingEdges.get(0);
            if (ChatAction.class.isInstance(node)) {
                ArrayList<ChatNode> nodes = new ArrayList<>(1);
                nodes.add(node);
                return getActionSet(nodes);
            }
            return outgoingEdges.get(0);
        }
        else {
            return getActionSet(outgoingEdges);
        }
    }

    private void finished() {
        sharedPreferences.edit().remove(ASSISTANT_LAST_SAVED_NODE).apply();
    }

    private void traverse(List<ChatNode> items, ChatNode root, ChatNode target) {
        if (root.equals(target)) {
            return;
        }
        final ArrayList<ChatNode> edges = getOutgoingEdges(root);
        if (edges != null && !edges.isEmpty()) {
            if (edges.size() == 1) {
                ChatNode node = edges.get(0);
                if (node.equals(target)) {
                    items.add(node);
                    return;
                }
                if (node instanceof ChatMessage) {
                    items.add(node);
                    traverse(items, node, target);
                }
                else {
                    ChatAction action = (ChatAction) node;
                    items.add(new ChatMessage.Builder().setText(action.text2 == null ? action.text1 : action.text2)
                                                   .setImage(action.image).setTintColor(action.tintColor)
                                                   .setTextSize(action.textSize).setShowAsAnswer(true).build());
                    traverse(items, action, target);
                }
            }
            else {
                ChatActionSet actionSet = getActionSet(edges);
                ChatAction action = actionSet.getDefault();
                if (action != null) {
                    items.add(new ChatMessage.Builder().setText(action.text2 == null ? action.text1 : action.text2)
                                                   .setImage(action.image).setTintColor(action.tintColor)
                                                   .setTextSize(action.textSize).setShowAsAnswer(true).build());
                    traverse(items, action, target);
                }
            }
        }
    }

    private ChatActionSet getActionSet(ArrayList<ChatNode> edges) {
        try {
            ChatActionSet actionSet = new ChatActionSet();
            for (int i = 0, size = edges.size(); i < size; i++) {
                actionSet.add((ChatAction) edges.get(i));
            }
            Collections.sort(actionSet);
            actionSet.getDefault().isDefault = true;
            return actionSet;
        } catch (ClassCastException ignored) {
            throw new IllegalStateException("Only actions can represent several edges in the graph");
        }
    }

    private void show(@Nullable ChatNode node, long delay, boolean showNext) {
        if (node != null) {
            if (node instanceof ChatMessage) {
                schedule(node, delay, showNext);
            }
            else {
                schedule(node, delay, false); // Never show next node automatically for actions
            }
        }
        else {
            hideLoading(); // Otherwise there is no more nodes
            finished();
        }
    }

    private void schedule(ChatNode item, long timeout, boolean showNext) {
        task = new TimerTask() {
            @Override
            public void run() {
                task = null;
                scheduleHandler.post(() -> {
                    currentNode = item;
                    hideLoading();
                    if (item instanceof ChatMessage) {
                        ChatMessage message = (ChatMessage) item;
                        addLast(message);
                        if (message.onLoaded != null) {
                            message.onLoaded.run();
                        }
                        if (enableVoiceInteraction && voiceInteractionInitialized) {
                            showLoading();
                            speechSynthesizer.playText(message.text, (OnSynthesizerDone) s -> {
                                //TODO: perhaps we can remove - speechSynthesizer.resume();
                                voiceInteractionHandler.post(() -> {
                                    if (showNext) {
                                        next();
                                    }
                                    else {
                                        hideLoading();
                                    }
                                });
                            });
                        }
                        else if (enableVoiceInteraction && !voiceInteractionInitializing) {
                            throw new IllegalStateException("You must call #prepareForVoiceInteraction() before");
                        }
                        else if (showNext) {
                            next();
                        }
                    }
                    else {
                        adapter.addItem(item);
                        if (showNext) {
                            next();
                        }
                    }
                    layoutManager.scrollToPosition(adapter.getItemCount() - 1);
                });
            }
        };
        timer.schedule(task, timeout);
    }

    @NonNull
    private ArrayList<ChatNode> getEmptyList() {
        ArrayList<ChatNode> list = mListPool.acquire();
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @Nullable
    private ArrayList<ChatNode> getIncomingEdges(@NonNull ChatNode node) {
        return graph.get(node);
    }

    @Nullable
    private ArrayList<ChatNode> getOutgoingEdges(@NonNull ChatNode node) {
        ArrayList<ChatNode> result = null;
        for (int i = 0, size = graph.size(); i < size; i++) {
            ArrayList<ChatNode> edges = graph.valueAt(i);
            if (edges != null && edges.contains(node)) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(graph.keyAt(i));
            }
        }
        return result;
    }

    @Override
    void addEdge(@NonNull ChatNode node, @NonNull ChatNode incomingEdge) {
        if (!graph.containsKey(node) || !graph.containsKey(incomingEdge)) {
            throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
        }

        ArrayList<ChatNode> edges = graph.get(node);
        if (edges == null) {
            // If edges is null, we should try and get one from the pool and add it to the graph
            edges = getEmptyList();
            graph.put(node, edges);
        }
        // Finally add the edge to the list
        edges.add(incomingEdge);
    }

    @Override
    public ChatNode getNode(@NonNull String id) {
        for (int i = 0, size = graph.size(); i < size; i++) {
            ChatNode node = graph.keyAt(i);
            if (node.getId().equals(id)) {
                return node;
            }
        }
        throw new IllegalArgumentException("Node \"" + id + "\" does not exists in the graph. " +
                "Have you forgotten to add it with addNode(Node)?");
    }
}
