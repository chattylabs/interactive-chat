package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public class Flow implements FlowSource {
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

    private FlowTarget target = (node, optNodes) -> {
        edge.addEdge(node, from);
        for (Node n : optNodes) edge.addEdge(n, from);
    };

    abstract static class Edge {
        abstract void addEdge(@NonNull Node node, @NonNull Node incomingEdge);
    }
}
