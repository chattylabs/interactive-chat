package com.chattylabs.android.interactive.chat;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

interface ViewHolderBuilder {
    RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType);

    interface Binder {
        void onBind(InteractiveChatViewAdapter adapter, int position);
    }
}
