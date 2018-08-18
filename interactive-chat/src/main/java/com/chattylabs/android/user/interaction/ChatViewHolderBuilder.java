package com.chattylabs.android.user.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

interface ChatViewHolderBuilder {
    RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType);

    interface Binder {
        void onBind(ChatInteractionViewAdapter adapter, int position);
    }
}
