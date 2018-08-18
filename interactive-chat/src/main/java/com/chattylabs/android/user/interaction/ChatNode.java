package com.chattylabs.android.user.interaction;

public interface ChatNode extends ChatId {

    /**
     * Use id resources to uniquely identify item view types.
     */
    int getViewType();

    Runnable onLoaded();

    ChatViewHolderBuilder getViewHolderBuilder();
}
