package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public interface FlowTarget {
    void to(@NonNull Node node, Node... optNodes);
}
