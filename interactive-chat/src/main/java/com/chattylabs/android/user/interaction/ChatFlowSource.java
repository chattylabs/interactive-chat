package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public interface ChatFlowSource {
    ChatFlowTarget from(@NonNull ChatNode node);
}
