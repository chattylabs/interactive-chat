package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public interface ChatFlowTarget {
    void to(@NonNull ChatNode node, ChatNode... optNodes);
}
