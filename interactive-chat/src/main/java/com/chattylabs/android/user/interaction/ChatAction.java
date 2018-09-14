package com.chattylabs.android.user.interaction;

public interface ChatAction extends ChatNode, Comparable<ChatAction> {

    interface OnSelected {
        void execute(ChatAction action);
    }

    int getOrder();
}
