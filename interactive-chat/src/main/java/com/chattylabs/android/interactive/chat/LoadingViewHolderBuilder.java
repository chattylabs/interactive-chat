package com.chattylabs.android.interactive.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class LoadingViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new LoadingViewHolderBuilder();
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
        public void onBind(InteractiveChatViewAdapter adapter, int position) {
            // No actions
        }
    }
}
