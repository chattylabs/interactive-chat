package com.chattylabs.android.user.interaction;

public class ChatLoading implements ChatNode, HasViewType {

    @Override
    public int getViewType() {
        return R.id.interactive_chat_loading_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatLoadingViewHolderBuilder.build();
    }
}
