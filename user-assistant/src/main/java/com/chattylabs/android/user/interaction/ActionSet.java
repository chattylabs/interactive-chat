package com.chattylabs.android.user.interaction;

import java.util.ArrayList;

class ActionSet extends ArrayList<Action> implements Node {

    public Action getDefault() {
        for (Action action : this) {
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
