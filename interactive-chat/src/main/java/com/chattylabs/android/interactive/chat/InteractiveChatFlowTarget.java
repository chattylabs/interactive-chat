package com.chattylabs.android.interactive.chat;

import android.support.annotation.NonNull;

public interface InteractiveChatFlowTarget {
    void to(@NonNull InteractiveChatNode node, InteractiveChatNode... optNodes);
}
