package com.chattylabs.android.user.interaction;

public abstract class ChatAction implements ChatNode, MustBuildActionFeedback, Comparable<ChatAction> {

    public interface OnSelected {
        void execute(ChatAction action);
    }

    public abstract int getOrder();
}
