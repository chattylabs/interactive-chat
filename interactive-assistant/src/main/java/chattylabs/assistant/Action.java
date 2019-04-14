package chattylabs.assistant;

public interface Action extends Node, Comparable<Action> {

    interface OnSelected {
        void execute(Action action);
    }

    int getOrder();
}
