package com.chattylabs.android.user.interaction;

import android.support.v7.widget.RecyclerView;

import java.util.List;

abstract class ChatInteractionViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    public abstract List<ChatNode> getItems();
    public abstract ChatInteractionAdapter.OnActionListener getActionListener();
}
