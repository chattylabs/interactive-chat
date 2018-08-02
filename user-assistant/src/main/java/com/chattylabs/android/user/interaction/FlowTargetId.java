package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public interface FlowTargetId {
    void to(@NonNull String id, String... ids);
}
