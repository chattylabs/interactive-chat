package com.chattylabs.android.user.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

class ActionViewHolder extends RecyclerView.ViewHolder {

    ViewGroup actions;

    ActionViewHolder(View v) {
        super(v);
        actions = (ViewGroup) v;
    }
}
