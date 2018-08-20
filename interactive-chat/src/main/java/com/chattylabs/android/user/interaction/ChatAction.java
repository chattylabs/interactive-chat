package com.chattylabs.android.user.interaction;

public abstract class ChatAction implements ChatNode, Comparable<ChatAction> {

    public interface OnSelected {
        void execute(ChatAction action);
    }

    public abstract OnSelected onSelected();

    public abstract boolean skipTracking();

    public abstract boolean stopFlow();

    public abstract int getOrder();

    public abstract String[] getContentDescriptions();

    public abstract ChatActionViewBuilder getActionViewBuilder();

    public abstract ChatNode buildActionFeedback();

    @Override
    final public int getViewType() {
        throw new UnsupportedOperationException();
    }

    @Override
    final public ChatViewHolderBuilder getViewHolderBuilder() {
        throw new UnsupportedOperationException();
    }
}
