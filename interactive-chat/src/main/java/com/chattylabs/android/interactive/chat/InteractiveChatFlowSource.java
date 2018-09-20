package com.chattylabs.android.interactive.chat;

import android.support.annotation.NonNull;

public interface InteractiveChatFlowSource {
    InteractiveChatFlowTarget from(@NonNull InteractiveChatNode node);
}
