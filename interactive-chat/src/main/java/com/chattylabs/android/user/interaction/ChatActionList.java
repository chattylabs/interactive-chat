package com.chattylabs.android.user.interaction;

import java.util.ArrayList;
import java.util.Set;

class ChatActionList extends ArrayList<ChatAction> implements ChatNode {

    ChatAction getVisited(Set<String> nodes) {
        for (ChatAction action : this) {
            if (nodes.contains(action.getId())) {
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
