package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public interface FlowSourceId {
    FlowTargetId from(@NonNull String id);
}
