package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public interface ChatFlowTargetId {
    void to(@NonNull String id, String... ids);
}
