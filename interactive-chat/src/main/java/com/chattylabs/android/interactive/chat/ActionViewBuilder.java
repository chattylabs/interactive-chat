package com.chattylabs.android.interactive.chat;

import android.view.View;
import android.view.ViewGroup;

public interface ActionViewBuilder {
    View createView(ViewGroup viewGroup, Action action);
}
