package com.chattylabs.android.interactive.chat;

import android.support.v7.widget.RecyclerView;

import java.util.List;

abstract class InteractiveChatViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    public abstract List<InteractiveChatNode> getItems();
    public abstract InteractiveChatAdapter.OnActionListener getActionListener();
}
