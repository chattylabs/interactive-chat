package com.chattylabs.android.user.interaction;

import java.util.ArrayList;

class ChatActionSet extends ArrayList<ChatAction> implements ChatNode {

    public ChatAction getDefault() {
        for (ChatAction action : this) {
            if (action.isDefault) {
                return action;
            }
        }
        return get(0);
    }

    @Override
    public String getId() {
        return null;
    }
}
