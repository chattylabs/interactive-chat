package com.chattylabs.android.user.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class ChatLoadingViewHolderBuilder implements ChatViewHolderBuilder {

    public static ChatViewHolderBuilder build() {
        return new ChatLoadingViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_chat_loading, viewGroup, false);
        return new ChatLoadingViewHolder(view);
    }

    static class ChatLoadingViewHolder extends RecyclerView.ViewHolder implements Binder {
        ChatLoadingViewHolder(View v) {
            super(v);
        }

        @Override
        public void onBind(ChatInteractionViewAdapter adapter, int position) {
            // No actions
        }
    }
}
