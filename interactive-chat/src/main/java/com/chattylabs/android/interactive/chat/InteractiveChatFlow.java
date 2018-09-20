package com.chattylabs.android.interactive.chat;

import android.support.annotation.NonNull;

public class InteractiveChatFlow implements InteractiveChatFlowSource, InteractiveChatFlowSourceId {
    private InteractiveChatNode from;
    private Edge edge;

    InteractiveChatFlow(Edge edge) {
        this.edge = edge;
    }

    @Override
    public InteractiveChatFlowTarget from(@NonNull InteractiveChatNode node) {
        from = node;
        return target;
    }

    @Override
    public InteractiveChatFlowTargetId from(@NonNull String id) {
        from = edge.getNode(id);
        return targetId;
    }

    public void start(InteractiveChatNode root) {
        edge.start(root);
    }

    private InteractiveChatFlowTarget target = (node, optNodes) -> {
        edge.addEdge(node, from);
        for (InteractiveChatNode n : optNodes) edge.addEdge(n, from);
    };

    private InteractiveChatFlowTargetId targetId = (id, optIds) -> {
        edge.addEdge(edge.getNode(id), from);
        for (String s : optIds) edge.addEdge(edge.getNode(s), from);
    };

    abstract static class Edge {
        abstract InteractiveChatNode getNode(@NonNull String id);
        abstract void addEdge(@NonNull InteractiveChatNode node, @NonNull InteractiveChatNode incomingEdge);
        abstract void start(@NonNull InteractiveChatNode root);
    }
}
