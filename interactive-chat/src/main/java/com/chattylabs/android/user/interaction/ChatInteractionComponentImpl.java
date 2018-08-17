package com.chattylabs.android.user.interaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.v4.util.Pools;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.chattylabs.sdk.android.common.DimensionUtils;
import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;
import com.chattylabs.sdk.android.voice.OnComponentSetup;
import com.chattylabs.sdk.android.voice.RecognizerListener;
import com.chattylabs.sdk.android.voice.SpeechRecognizerComponent;
import com.chattylabs.sdk.android.voice.SpeechSynthesizerComponent;
import com.chattylabs.sdk.android.voice.SynthesizerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


final class ChatInteractionComponentImpl extends ChatFlow.Edge implements ChatInteractionComponent {

    private static final long DEFAULT_MESSAGE_DELAY = 500L;

    @VisibleForTesting
    static final String LAST_SAVED_NODE = BuildConfig.APPLICATION_ID + ".LAST_SAVED_NODE";
    public static final int LOADING_DISPLAY_DELAY = 1000;
    public static final int ITEM_SEPARATOR_SIZE_DIP = 4;

    private final Pools.Pool<ArrayList<ChatNode>> mListPool = new Pools.SimplePool<>(10);
    private final SimpleArrayMap<ChatNode, ArrayList<ChatNode>> graph = new SimpleArrayMap<>();
    private final LinearLayoutManager layoutManager;
    private final SharedPreferences sharedPreferences;
    private ConversationalFlowComponent speechComponent;
    private SpeechSynthesizerComponent speechSynthesizer;
    private SpeechRecognizerComponent speechRecognizer;

    private Timer timer;
    private TimerTask task;
    private Handler uiThreadHandler = new Handler(Looper.getMainLooper());
    private Handler loadingHandler = new Handler(Looper.getMainLooper());
    private Handler speechHandler = new Handler(Looper.getMainLooper());
    private Handler scheduleHandler = new Handler(Looper.getMainLooper());
    private ChatNode currentNode;
    private ChatAction lastAction;
    private boolean paused;
    private boolean enableSynthesizer;
    private boolean enableRecognizer;
    private boolean synthesizerReady;
    private boolean recognizerReady;
    private boolean speechInProgress;

    private final ChatInteractionAdapter adapter = new ChatInteractionAdapter(
            (view, action) -> perform(action), new ArrayList<>());

    @SuppressLint("MissingPermission")
    ChatInteractionComponentImpl(Builder builder) {
        RecyclerView recyclerView = builder.recyclerView;
        if (recyclerView.getItemDecorationCount() == 0) {
            int space = DimensionUtils.getDimension(
                    recyclerView.getContext(),
                    TypedValue.COMPLEX_UNIT_DIP, ITEM_SEPARATOR_SIZE_DIP);
            VerticalSpaceItemDecoration spaceItemDecoration = new VerticalSpaceItemDecoration(space);
            uiThreadHandler.post(() -> {
                recyclerView.addItemDecoration(spaceItemDecoration);
            });
        }
        timer = new Timer();
        speechComponent = builder.voiceComponent;
        speechSynthesizer = speechComponent.getSpeechSynthesizer(recyclerView.getContext());
        speechRecognizer = speechComponent.getSpeechRecognizer(recyclerView.getContext());
        layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        layoutManager.setSmoothScrollbarEnabled(false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(recyclerView.getContext());
        uiThreadHandler.post(() -> {
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(adapter);
            EmojiCompat.Config config = new BundledEmojiCompatConfig(recyclerView.getContext());
            EmojiCompat.init(config);
        });
    }

    @Override
    public void init(ChatNode root) {
        String lastSavedNodeId = getLastVisitedNodeId(root);
        ChatNode lastSavedNode = getNode(lastSavedNodeId);
        adapter.addItem(root);
        traverse(adapter.getItems(), root, lastSavedNode);
        currentNode = lastSavedNode;
        next();
    }

    @Override
    public void enableSpeechSynthesizer(boolean enable) {
        this.enableSynthesizer = enable;
    }

    @Override
    public void enableSpeechRecognizer(boolean enable) {
        this.enableRecognizer = enable;
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
        if (lastAction != null && !lastAction.skipTracking) trackLastAction();
        show(getNext(), DEFAULT_MESSAGE_DELAY);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void setupSpeech(Context context, OnComponentSetup onSetup) {
        speechInProgress = true;
        showLoading();
        speechComponent.setup(context, status -> {
            synthesizerReady = status.getSynthesizerStatus() ==
                    SynthesizerListener.Status.AVAILABLE;
            recognizerReady = status.getRecognizerStatus() ==
                    RecognizerListener.Status.AVAILABLE;
            // FIXME: Probably a Dagger issue
            // Second time it exists the App seems like the TTS is still alive, so triedTtsData
            // value is TRUE, causing checkAvailability never to be called.
            // TODO: P2 - to show TTS error screen
            speechHandler.post(() -> {
                hideLoading();
                onSetup.execute(status);
            });
        });
    }

    @Override
    public void release() {
        cancel();
        loadingHandler.removeCallbacksAndMessages(null);
        uiThreadHandler.removeCallbacksAndMessages(null);
        speechHandler.removeCallbacksAndMessages(null);
        scheduleHandler.removeCallbacksAndMessages(null);
        if (speechComponent != null) {
            speechComponent.shutdown();
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
        if (speechComponent != null) {
            speechComponent.stop();
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
            if (size == 0 || !ChatLoading.class.isInstance(
                    adapter.getItem(size - 1))) {
                adapter.addItem(new ChatLoading());
                layoutManager.scrollToPosition(size);
            }
        }, LOADING_DISPLAY_DELAY);
    }

    @Override
    public void hideLoading() {
        if (loadingHandler != null) loadingHandler.removeCallbacksAndMessages(null);
        int position = adapter.getLastPositionOf(ChatLoading.class);
        if (position != -1 && uiThreadHandler != null)
            uiThreadHandler.post(() -> adapter.removeItem(position));
    }

    private void trackLastAction() {
        ArrayList<ChatNode> outgoingEdges = getOutgoingEdges(lastAction);
        if (outgoingEdges != null && !outgoingEdges.isEmpty()) {
            if (outgoingEdges.size() == 1) {
                ChatNode node = outgoingEdges.get(0);
                if (ChatMessage.class.isInstance(node)) {
                    setLastVisitedNode(node);
                } else {
                    throw new IllegalStateException("An Action can only have " +
                            "an edge to a Message");
                }
            } else {
                throw new IllegalStateException("An Action cannot have multiple " +
                        "edges in the graph");
            }
        }
    }

    private void perform(ChatAction action) {
        currentNode = action;
        lastAction = action;
        if (!action.keepAction) {
            selectLastAction();
        }
        if (speechComponent != null) speechComponent.stop();
        if (action.onSelected != null) {
            action.onSelected.execute(action);
        }
        if (!action.stopFlow) {
            next();
        }
    }

    @Override
    public void selectLastAction() {
        ChatNode node = getNode();
        ChatAction action = null;
        removeLastItem();
        if (ChatAction.class.isInstance(node)) {
            action = (ChatAction) node;
        } else if (ChatActionSet.class.isInstance(node)) {
            for (ChatAction a : ((ChatActionSet) node))
                if (a.isDefault) {
                    action = a;
                    break;
                }
        }
        if (action != null) assignAction(action);
    }

    private void assignAction(ChatAction action) {
        lastAction = action;
        addLast(new ChatMessage.Builder().setText(
                action.textAfter == null ? action.text : action.textAfter)
                .setImage(action.image).setTintColor(action.tintColor)
                .setTextSize(action.textSize).setShownAsAction(true).build());
    }

    private void addLast(ChatNode node) {
        uiThreadHandler.post(() -> adapter.addItem(node));
    }

    private void removeLastItem() {
        if (adapter.getItemCount() > 0) uiThreadHandler.post(() ->
                adapter.removeItem(adapter.getItemCount() - 1));
    }

    private void setLastVisitedNode(ChatNode node) {
        sharedPreferences.edit().putString(LAST_SAVED_NODE, node.getId()).apply();
    }

    private String getLastVisitedNodeId(ChatNode node) {
        return sharedPreferences.getString(LAST_SAVED_NODE, node.getId());
    }

    private ChatNode getNode() {
        return currentNode;
    }

    @Nullable
    private ChatNode getNext() {
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
        } else {
            return getActionSet(outgoingEdges);
        }
    }

    @Override
    public void reset() {
        sharedPreferences.edit().remove(LAST_SAVED_NODE).apply();
    }

    private void traverse(List<ChatNode> items, ChatNode root, ChatNode target) {
        if (root.equals(target)) return;
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
                } else {
                    ChatAction action = (ChatAction) node;
                    ChatMessage message = new ChatMessage.Builder().setText(
                            action.textAfter == null ? action.text : action.textAfter)
                            .setImage(action.image).setTintColor(action.tintColor)
                            .setTextSize(action.textSize).setShownAsAction(true).build();
                    items.add(message);
                    traverse(items, action, target);
                }
            } else {
                ChatActionSet actionSet = getActionSet(edges);
                ChatAction action = actionSet.getDefault();
                if (action != null) {
                    ChatMessage message =new ChatMessage.Builder().setText(
                            action.textAfter == null ? action.text : action.textAfter)
                            .setImage(action.image).setTintColor(action.tintColor)
                            .setTextSize(action.textSize).setShownAsAction(true).build();
                    items.add(message);
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

    private void show(@Nullable ChatNode node, long delay) {
        if (node != null) {
            schedule(node, delay);
        } else {
            hideLoading(); // Otherwise there is no more nodes
        }
    }

    private void schedule(ChatNode item, long timeout) {
        task = new TimerTask() {
            @Override
            public void run() {
                task = null;
                scheduleHandler.post(() -> {
                    hideLoading();
                    addLast(item);
                    if (item instanceof ChatMessage) {
                        currentNode = item;
                        ChatMessage message = (ChatMessage) item;
                        if (message.onLoaded != null) {
                            message.onLoaded.run();
                        }
                        if (enableSynthesizer && synthesizerReady) {
                            showLoading();
                            speechSynthesizer.playText(message.text,
                                    (SynthesizerListener.OnDone) s ->
                                            speechHandler.post(() -> next()));
                        } else if (enableSynthesizer && !speechInProgress) {
                            throw new IllegalStateException("Have you called #setupSpeech()?");
                        } else next();
                    } else {
                        if (enableRecognizer && recognizerReady) {
                            // show mic icon?
                            speechRecognizer.listen((RecognizerListener.OnMostConfidentResult) result -> {
                                ChatActionSet actionSet = (ChatActionSet) getNext();
                                currentNode = item;
                                if (actionSet != null) for (ChatAction action : actionSet) {
                                    String[] descriptions = action.text != null ?
                                            new String[]{action.text} : null;
                                    if (descriptions == null) descriptions = action.textAfter != null ?
                                            new String[]{action.textAfter} : null;
                                    if (descriptions == null) descriptions = action.contentDescriptions;
                                    if (checkWord(descriptions, result)){
                                        perform(action);
                                        break;
                                    }
                                }
                            });
                        } else if (enableRecognizer && !speechInProgress) {
                            throw new IllegalStateException("Have you called #setupSpeech()?");
                        } else {
                            currentNode = item;
                        }
                        // Never show next node automatically for actions
                    }
                    layoutManager.scrollToPosition(adapter.getItemCount() - 1);
                });
            }
        };
        timer.schedule(task, timeout);
    }

    private boolean checkWord(String[] patterns, String text) {
        for (String pattern : patterns) {
            if (ConversationalFlowComponent.matches(text, pattern)) return true;
        }
        return false;
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
            throw new IllegalArgumentException("All nodes must be present in the graph " +
                    "before being added as an edge");
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
