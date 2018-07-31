package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public interface FlowSource {
    FlowTarget from(@NonNull Node node);
}
