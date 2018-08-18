package com.chattylabs.android.user.interaction;

import android.view.View;
import android.view.ViewGroup;

public interface ChatActionViewBuilder {
    View createView(ViewGroup viewGroup, ChatAction action);
}
