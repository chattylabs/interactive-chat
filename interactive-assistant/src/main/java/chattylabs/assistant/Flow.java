package chattylabs.assistant;

import androidx.annotation.NonNull;

public class Flow implements Source, SourceId {
    private Node from;
    private Edge edge;

    Flow(Edge edge) {
        this.edge = edge;
    }

    @Override
    public Target from(@NonNull Node node) {
        from = node;
        return target;
    }

    @Override
    public TargetId from(@NonNull String id) {
        from = edge.getNode(id);
        return targetId;
    }

    public void start(Node root) {
        edge.start(root);
    }

    private Target target = (node, optNodes) -> {
        edge.addEdge(node, from);
        for (Node n : optNodes) edge.addEdge(n, from);
    };

    private TargetId targetId = (id, optIds) -> {
        edge.addEdge(edge.getNode(id), from);
        for (String s : optIds) edge.addEdge(edge.getNode(s), from);
    };

    abstract static class Edge {
        abstract Node getNode(@NonNull String id);
        abstract void addEdge(@NonNull Node node, @NonNull Node incomingEdge);
        abstract void start(@NonNull Node root);
    }
}
