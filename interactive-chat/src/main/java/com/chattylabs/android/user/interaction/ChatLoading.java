package com.chattylabs.android.user.interaction;

public class ChatLoading implements ChatNode {

    @Override
    public int getViewType() {
        return R.id.interactive_chat_loading_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatLoadingViewHolderBuilder.build();
    }

    @Override
    public Runnable onLoaded() {
        return null;
    }

    @Override
    public String getId() {
        return "";
    }
}
