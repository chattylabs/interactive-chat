package com.chattylabs.android.interactive.chat;

import android.support.annotation.NonNull;

public interface InteractiveChatFlowTargetId {
    void to(@NonNull String id, String... ids);
}
