package com.chattylabs.android.user.interaction;

import java.util.ArrayList;

class ChatActionList extends ArrayList<ChatAction> implements ChatNode {

    public ChatAction getDefault() {
        for (ChatAction action : this) {
            if (action.isDefault()) {
                return action;
            }
        }
        return get(0);
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_action_list_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatActionListViewHolderBuilder.build();
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
