package chattylabs.assistant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;

import com.chattylabs.android.commons.DimensionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.VisibleForTesting;
import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools;
import chattylabs.conversations.BuildConfig;
import chattylabs.conversations.ConversationalFlow;
import chattylabs.conversations.RecognizerListener;
import chattylabs.conversations.SpeechRecognizer;
import chattylabs.conversations.SpeechSynthesizer;
import chattylabs.conversations.SynthesizerListener;


final class InteractiveAssistantImpl extends Flow.Edge implements InteractiveAssistant {

    private static final long DEFAULT_MESSAGE_DELAY = 500L;

    @VisibleForTesting
    static final String LAST_VISITED_NODE = BuildConfig.APPLICATION_ID + ".LAST_VISITED_NODE";
    @VisibleForTesting
    static final String VISITED_NODES = BuildConfig.APPLICATION_ID + ".VISITED_NODES";

    public static final int LOADING_DISPLAY_DELAY = 1000;
    public static final int ITEM_SEPARATOR_SIZE_DIP = 4;

    private final Pools.Pool<ArrayList<Node>> mListPool = new Pools.SimplePool<>(10);
    private final SimpleArrayMap<Node, ArrayList<Node>> graph = new SimpleArrayMap<>();
    private final LinearLayoutManager layoutManager;
    private final SharedPreferences sharedPreferences;
    private ConversationalFlow speechComponent;
    private SpeechSynthesizer speechSynthesizer;
    private SpeechRecognizer speechRecognizer;

    private Timer timer;
    private TimerTask task;
    private Handler uiThreadHandler = new Handler(Looper.getMainLooper());
    private Handler loadingHandler = new Handler(Looper.getMainLooper());
    private Handler speechHandler = new Handler(Looper.getMainLooper());
    private Handler scheduleHandler = new Handler(Looper.getMainLooper());
    private Node currentNode;
    private Action lastAction;
    private Runnable doneListener;
    private boolean initialized;
    private boolean started;
    private boolean paused;
    private boolean enableSynthesizer;
    private boolean enableRecognizer;
    private boolean synthesizerReady;
    private boolean recognizerReady;
    private boolean speechSetupInProgress;
    private boolean enableLastState;

    private final AssistantAdapter adapter = new AssistantAdapter(
            (view, action) -> perform(action));

    @SuppressLint("MissingPermission") InteractiveAssistantImpl(Builder builder) {
        RecyclerView recyclerView = builder.recyclerView;
        if (recyclerView.getItemDecorationCount() == 0) {
            int s = DimensionUtils.getDimension(recyclerView.getContext(),
                    TypedValue.COMPLEX_UNIT_DIP, ITEM_SEPARATOR_SIZE_DIP);
            SeparatorItemDecoration separatorItemDecoration = new SeparatorItemDecoration(s);
            uiThreadHandler.post(() -> {
                recyclerView.addItemDecoration(separatorItemDecoration);
            });
        }
        timer = new Timer();
        enableLastState = builder.withLastStateEnabled;
        speechComponent = builder.voiceComponent;
        doneListener = builder.doneListener;
        layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        //layoutManager.setSmoothScrollbarEnabled(false);
        sharedPreferences = recyclerView.getContext().getSharedPreferences(
                "interactive_chat", Context.MODE_PRIVATE);
        uiThreadHandler.post(() -> {
            //recyclerView.setItemAnimator(null);
            EmojiCompat.Config config = null;
            recyclerView.setAdapter(adapter);
            if (builder.fontRequest != null) {
                config = new FontRequestEmojiCompatConfig(
                        recyclerView.getContext(), builder.fontRequest
                );
            } else {
                FontRequest fontRequest = new FontRequest(
                        "com.google.android.gms.fonts",
                        "com.google.android.gms",
                        "Source Sans Pro",
                        R.array.com_google_android_gms_fonts_certs
                );
                config = new FontRequestEmojiCompatConfig(
                        recyclerView.getContext(), fontRequest
                );
            }
            config.registerInitCallback(new EmojiCompat.InitCallback() {
                @Override
                public void onInitialized() {
                    initialized = true;
                    if (started) next();
                }

                @Override
                public void onFailed(@Nullable Throwable throwable) {
                    initialized = true;
                    if (started) next();
                }
            });
            try {
                EmojiCompat.get();
                initialized = true;
                if (started) next();
            } catch (Exception ignored) {
                EmojiCompat.init(config);
            }
        });
    }

    @Override
    synchronized void start(@NonNull Node root) {
        String lastSavedNodeId = getLastVisitedNodeId((HasId) root);
        Node lastSavedNode = getNode(lastSavedNodeId);
        adapter.addItem(root);
        traverse(adapter.getItems(), root, lastSavedNode);
        adapter.checkViewHolders();
        currentNode = lastSavedNode;
        started = true;
        if (initialized) {
            next();
        }
    }

    @Override
    public void enableSpeechSynthesizer(Context context, boolean enable) {
        this.enableSynthesizer = enable;
        if (speechComponent != null && synthesizerReady)
            speechSynthesizer = speechComponent.getSpeechSynthesizer(context);
    }

    @Override
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    public void enableSpeechRecognizer(Context context, boolean enable) {
        this.enableRecognizer = enable;
        if (speechComponent != null && recognizerReady)
            speechRecognizer = speechComponent.getSpeechRecognizer(context);
    }

    @Override
    public void addNode(@NonNull Node node) {
        if (!graph.containsKey(node)) {
            graph.put(node, null);
        }
    }

    @Override
    public boolean isSpeechSynthesizerEnabled() {
        return enableSynthesizer;
    }

    @Override
    public boolean isSpeechRecognizerEnabled() {
        return enableRecognizer;
    }

    @Override
    public Flow prepare() {
        return new Flow(this);
    }

    @Override
    synchronized public void next() {
        boolean canTrack = !(lastAction instanceof CanSkipTracking) ||
                !((CanSkipTracking) lastAction).skipTracking();
        if (lastAction != null && canTrack) trackLastNode();
        show(getNext(), DEFAULT_MESSAGE_DELAY);
    }

    @Override
    public void next(@NonNull Node node) {
        show(node, DEFAULT_MESSAGE_DELAY);
    }

    @Override
    public void setupSpeech(Context context, OnSpeechStatusChecked listener) {
        speechSetupInProgress = true;
        showLoading();
        speechComponent.checkSpeechSynthesizerStatus(context, synthesizerStatus -> {
            speechComponent.checkSpeechRecognizerStatus(context, recognizerStatus -> {
                synthesizerReady = synthesizerStatus == SynthesizerListener.Status.AVAILABLE;
                recognizerReady = recognizerStatus == RecognizerListener.Status.AVAILABLE;
                speechHandler.post(() -> {
                    hideLoading();
                    listener.onStatusChecked(synthesizerStatus, recognizerStatus);
                });
            });
        });

    }

    @Override
    public void setupSpeech(Context context, RecognizerListener.OnStatusChecked listener) {
        speechSetupInProgress = true;
        showLoading();
        speechComponent.checkSpeechRecognizerStatus(context, recognizerStatus -> {
            recognizerReady = recognizerStatus == RecognizerListener.Status.AVAILABLE;
            speechHandler.post(() -> {
                hideLoading();
                listener.execute(recognizerStatus);
            });
        });
    }

    @Override
    public void setupSpeech(Context context, SynthesizerListener.OnStatusChecked listener) {
        speechSetupInProgress = true;
        showLoading();
        speechComponent.checkSpeechSynthesizerStatus(context, synthesizerStatus -> {
            synthesizerReady = synthesizerStatus == SynthesizerListener.Status.AVAILABLE;
            speechHandler.post(() -> {
                hideLoading();
                listener.execute(synthesizerStatus);
            });
            // FIXME: Probably a Dagger issue
            // Second time it exists the App seems like the TTS is still alive, so triedTtsData
            // value is TRUE, causing checkAvailability never to be called.
            // TODO: P2 - to show TTS error screen
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
        sharedPreferences.edit().clear().apply();
        graph.clear();
        currentNode = null;
        lastAction = null;
        doneListener = null;
        paused = false;
        enableSynthesizer = false;
        enableRecognizer = false;
        synthesizerReady = false;
        recognizerReady = false;
        speechSetupInProgress = false;
        enableLastState = false;
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
            if (size == 0 || !(adapter.getItem(size - 1) instanceof LoadingView)) {
                adapter.addItem(new LoadingView());
                layoutManager.scrollToPosition(size);
            }
        }, LOADING_DISPLAY_DELAY);
    }

    @Override
    public void hideLoading() {
        if (loadingHandler != null) loadingHandler.removeCallbacksAndMessages(null);
        int position = adapter.getLastPositionOf(LoadingView.class);
        if (position != -1 && uiThreadHandler != null)
            uiThreadHandler.post(() -> adapter.removeItem(position));
    }

    private void trackLastNode() {
        ArrayList<Node> outgoingEdges = getOutgoingEdges(lastAction);
        if (outgoingEdges != null && !outgoingEdges.isEmpty()) {
            if (outgoingEdges.size() == 1) {
                Node node = outgoingEdges.get(0);
                if (!(node instanceof Action)) {
                    if (enableLastState) setLastVisitedNode((HasId) node);
                } else {
                    throw new IllegalStateException("An Action can only be " +
                            "connected to a Message");
                }
            } else {
                throw new IllegalStateException("An Action cannot have multiple " +
                        "connections in the graph");
            }
        }
    }

    private void perform(Action action) {
        currentNode = action;
        lastAction = action;
        if (speechComponent != null) speechComponent.stop();
        if (action instanceof HasOnSelected && ((HasOnSelected) action).onSelected() != null) {
            ((HasOnSelected) action).onSelected().execute(action);
        }
        boolean continueFlow = !(action instanceof CanStopFlow) || !((CanStopFlow) action).stopFlow();
        if (continueFlow) {
            boolean canTrack = !(action instanceof CanSkipTracking) || !((CanSkipTracking) action).skipTracking();
            if (canTrack && enableLastState) saveVisitedNode((HasId) action);
            selectLastVisitedAction();
            next();
        }
    }

    @Override
    public void selectLastVisitedAction() {
        Node node = getNode();
        Action action = null;
        removeLastItem();
        if (node instanceof Action) {
            action = (Action) node;
        } else if (node instanceof ActionList) {
            action = ((ActionList) node).getVisited(getVisitedNodes());
        }
        if (action != null) placeSelectedAction(action);
    }

    private void placeSelectedAction(Action action) {
        lastAction = action;
        if (action instanceof CanHandleState) {
            ((CanHandleState) action).restoreSavedState(sharedPreferences);
        }
        addLast(((MustBuildActionFeedback) action).buildActionFeedback());
    }

    private void addLast(Node node) {
        uiThreadHandler.post(() -> {
            adapter.addItem(node);
            layoutManager.scrollToPosition(adapter.getItemCount() - 1);
        });
    }

    private void removeLastItem() {
        if (adapter.getItemCount() > 0) uiThreadHandler.post(() ->
                adapter.removeItem(adapter.getItemCount() - 1));
    }

    private void setLastVisitedNode(HasId node) {
        sharedPreferences.edit().putString(LAST_VISITED_NODE,
                node.getId()).apply();
    }

    private void saveVisitedNode(HasId node) {
        Set<String> set = getVisitedNodes();
        set.add(node.getId());
        sharedPreferences.edit().putStringSet(VISITED_NODES, set).apply();
        if (node instanceof CanHandleState) {
            ((CanHandleState) node).saveState(sharedPreferences);
        }
    }

    @Override
    public Set<String> getVisitedNodes() {
        return sharedPreferences.getStringSet(VISITED_NODES, new HashSet<>());
    }

    private String getLastVisitedNodeId(HasId node) {
        return sharedPreferences.getString(LAST_VISITED_NODE, node.getId());
    }

    private Node getNode() {
        return currentNode;
    }

    @Nullable
    private Node getNext() {
        ArrayList<Node> outgoingEdges = getOutgoingEdges(currentNode);
        if (outgoingEdges == null || outgoingEdges.isEmpty()) {
            return null;
        }

        if (outgoingEdges.size() == 1) {
            Node node = outgoingEdges.get(0);
            if (node instanceof Action) {
                ArrayList<Node> nodes = new ArrayList<>(1);
                nodes.add(node);
                return getActionList(nodes);
            }
            return outgoingEdges.get(0);
        } else {
            return getActionList(outgoingEdges);
        }
    }

    @Override
    public void removeLastState() {
        sharedPreferences.edit().remove(LAST_VISITED_NODE).apply();
        sharedPreferences.edit().remove(VISITED_NODES).apply();
    }

    private void traverse(List<Node> items, Node root, Node target) {
        if (root.equals(target)) return;
        final ArrayList<Node> edges = getOutgoingEdges(root);
        if (edges != null && !edges.isEmpty()) {
            if (edges.size() == 1) {
                Node node = edges.get(0);
                if (node.equals(target)) {
                    items.add(node);
                    return;
                }
                if (node instanceof Action) {
                    Action action = (Action) node;
                    if (action instanceof CanHandleState) {
                        ((CanHandleState) action).restoreSavedState(sharedPreferences);
                    }
                    if (action instanceof MustBuildActionFeedback) {
                        items.add(((MustBuildActionFeedback) action).buildActionFeedback());
                    }
                    traverse(items, action, target);
                } else {
                    items.add(node);
                    traverse(items, node, target);
                }
            } else {
                ActionList actionList = getActionList(edges);
                Action action = actionList.getVisited(getVisitedNodes());
                if (action != null) {
                    if (action instanceof CanHandleState) {
                        ((CanHandleState) action).restoreSavedState(sharedPreferences);
                    }
                    if (action instanceof MustBuildActionFeedback) {
                        items.add(((MustBuildActionFeedback) action).buildActionFeedback());
                    }
                    traverse(items, action, target);
                }
            }
        }
    }

    private ActionList getActionList(ArrayList<Node> edges) {
        try {
            ActionList actionList = new ActionList();
            for (int i = 0, size = edges.size(); i < size; i++) {
                actionList.add((Action) edges.get(i));
            }
            Collections.sort(actionList);
            return actionList;
        } catch (ClassCastException ignored) {
            throw new IllegalStateException("Only actions can represent several edges in the graph");
        }
    }

    private void show(@Nullable Node node, long delay) {
        if (node != null) {
            schedule(node, delay);
        } else {
            if (!(currentNode instanceof ActionList)) {
                hideLoading(); // Otherwise there is no more nodes
                if (doneListener != null) doneListener.run();
            }
        }
    }

    private void schedule(Node item, long timeout) {
        task = new TimerTask() {
            @Override
            public void run() {
                task = null;
                scheduleHandler.post(() -> {
                    hideLoading();
                    addLast(item);
                    if (!(item instanceof Action) &&
                        !(item instanceof ActionList)) {
                        currentNode = item;
                        handleNotActionNode(item);
                    } else {
                        handleActionNode(item);
                        // Never show next node automatically for actions
                        // Let the developer to choose when to do next()
                    }
                });
            }
        };
        timer.schedule(task, timeout);
    }

    private void handleActionNode(Node item) {
        if (item instanceof ActionList) {
            for (Action action : (ActionList)  item) {
                if (((HasOnLoaded) action).onLoaded() != null) {
                    ((HasOnLoaded) action).onLoaded().run();
                }
            }
        } else if (item instanceof Action) {
            if (((HasOnLoaded) item).onLoaded() != null) {
                ((HasOnLoaded) item).onLoaded().run();
            }
        }
        if (enableRecognizer && recognizerReady) {
            // TODO: show mic icon?

            ActionList actionList = (ActionList) getNext();
            if (actionList != null) {
                ((CanRecognizeSpeech) actionList)
                        .consumeRecognizer(speechRecognizer, this::perform);
            } else {
                currentNode = item;
            }

        } else if (enableRecognizer && !speechSetupInProgress) {
            throw new IllegalStateException("Have you checked #setupSpeech()?");
        } else {
            currentNode = item;
        }
    }

    private void handleNotActionNode(Node item) {
        if (((HasOnLoaded) item).onLoaded() != null) {
            ((HasOnLoaded) item).onLoaded().run();
        }
        if (enableSynthesizer && synthesizerReady) {
            if (item instanceof HasText) {
                if (item instanceof CanSynthesizeSpeech) {
                    showLoading();
                    ((CanSynthesizeSpeech) item).consumeSynthesizer(speechSynthesizer,
                            () -> speechHandler.post(this::next));
                } else {
                    next();
                }
            } else {
                next();
            }
        } else if (enableSynthesizer && !speechSetupInProgress) {
            throw new IllegalStateException("Have you called #setupSpeech()?");
        } else next();
    }

    @NonNull
    private ArrayList<Node> getEmptyList() {
        ArrayList<Node> list = mListPool.acquire();
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @Nullable
    private ArrayList<Node> getIncomingEdges(@NonNull Node node) {
        return graph.get(node);
    }

    @Nullable
    private ArrayList<Node> getOutgoingEdges(@NonNull Node node) {
        ArrayList<Node> result = null;
        for (int i = 0, size = graph.size(); i < size; i++) {
            ArrayList<Node> edges = graph.valueAt(i);
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
    void addEdge(@NonNull Node node, @NonNull Node incomingEdge) {
        if (!graph.containsKey(node) || !graph.containsKey(incomingEdge)) {
            throw new IllegalArgumentException("All nodes must be present in the graph " +
                    "before being added as an edge");
        }

        ArrayList<Node> edges = graph.get(node);
        if (edges == null) {
            // If edges is null, we should try and get one from the pool and add it to the graph
            edges = getEmptyList();
            graph.put(node, edges);
        }
        // Finally add the edge to the list
        edges.add(incomingEdge);
    }

    @Override
    public Node getNode(@NonNull String id) {
        for (int i = 0, size = graph.size(); i < size; i++) {
            Node node = graph.keyAt(i);
            if (node instanceof HasId && ((HasId) node).getId().equals(id)) {
                return node;
            }
        }
        throw new IllegalArgumentException("Node \"" + id + "\" does not exists in the graph. " +
                "Have you forgotten to add it with addNode(Node)?");
    }

}
