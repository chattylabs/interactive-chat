package chattylabs.assistant;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

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

    @Override
    public TargetId from(@StringRes int id) {
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

    private TargetId targetId = new TargetId() {
        @Override
        public void to(@NonNull String id, String... ids) {
            edge.addEdge(edge.getNode(id), from);
            for (String s : ids) edge.addEdge(edge.getNode(s), from);
        }

        @Override
        public void to(@StringRes int id, @StringRes Integer... ids) {
            edge.addEdge(edge.getNode(id), from);
            for (int i : ids) edge.addEdge(edge.getNode(i), from);
        }
    };

    abstract static class Edge {
        abstract Node getNode(@NonNull String id);
        abstract Node getNode(@StringRes int id);
        abstract void addEdge(@NonNull Node node, @NonNull Node incomingEdge);
        abstract void start(@NonNull Node root);
    }
}
