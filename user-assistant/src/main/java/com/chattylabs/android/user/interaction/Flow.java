package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public class Flow implements FlowSource, FlowSourceId {
    private Node from;
    private Edge edge;

    Flow(Edge edge) {
        this.edge = edge;
    }

    @Override
    public FlowTarget from(@NonNull Node node) {
        from = node;
        return target;
    }

    @Override
    public FlowTargetId from(@NonNull String id) {
        from = edge.getNode(id);
        return targetId;
    }

    private FlowTarget target = (node, optNodes) -> {
        edge.addEdge(node, from);
        for (Node n : optNodes) edge.addEdge(n, from);
    };

    private FlowTargetId targetId = (id, optIds) -> {
        edge.addEdge(edge.getNode(id), from);
        for (String s : optIds) edge.addEdge(edge.getNode(s), from);
    };

    abstract static class Edge {
        abstract Node getNode(@NonNull String id);
        abstract void addEdge(@NonNull Node node, @NonNull Node incomingEdge);
    }
}
