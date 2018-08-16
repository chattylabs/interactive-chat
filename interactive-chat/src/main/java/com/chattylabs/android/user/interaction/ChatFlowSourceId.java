package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public interface ChatFlowSourceId {
    ChatFlowTargetId from(@NonNull String id);
}
