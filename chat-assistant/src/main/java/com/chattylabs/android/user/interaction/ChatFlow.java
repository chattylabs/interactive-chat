package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public class ChatFlow implements ChatFlowSource, ChatFlowSourceId {
    private ChatNode from;
    private Edge edge;

    ChatFlow(Edge edge) {
        this.edge = edge;
    }

    @Override
    public ChatFlowTarget from(@NonNull ChatNode node) {
        from = node;
        return target;
    }

    @Override
    public ChatFlowTargetId from(@NonNull String id) {
        from = edge.getNode(id);
        return targetId;
    }

    private ChatFlowTarget target = (node, optNodes) -> {
        edge.addEdge(node, from);
        for (ChatNode n : optNodes) edge.addEdge(n, from);
    };

    private ChatFlowTargetId targetId = (id, optIds) -> {
        edge.addEdge(edge.getNode(id), from);
        for (String s : optIds) edge.addEdge(edge.getNode(s), from);
    };

    abstract static class Edge {
        abstract ChatNode getNode(@NonNull String id);
        abstract void addEdge(@NonNull ChatNode node, @NonNull ChatNode incomingEdge);
    }
}
