package com.chattylabs.android.user.interaction;

import java.util.ArrayList;
import java.util.Set;

class ChatActionList extends ArrayList<ChatAction> implements ChatNode, HasViewType {

    ChatAction getVisited(Set<String> nodes) {
        for (ChatAction action : this) {
            if (nodes.contains(((HasId) action).getId())) {
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
}
