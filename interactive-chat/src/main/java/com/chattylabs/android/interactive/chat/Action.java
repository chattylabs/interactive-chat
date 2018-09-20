package com.chattylabs.android.interactive.chat;

public interface Action extends InteractiveChatNode, Comparable<Action> {

    interface OnSelected {
        void execute(Action action);
    }

    int getOrder();
}
