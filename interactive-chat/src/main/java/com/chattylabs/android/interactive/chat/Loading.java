package com.chattylabs.android.interactive.chat;

public class Loading implements InteractiveChatNode, HasViewType {

    @Override
    public int getViewType() {
        return R.id.interactive_chat_loading_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return LoadingViewHolderBuilder.build();
    }
}
