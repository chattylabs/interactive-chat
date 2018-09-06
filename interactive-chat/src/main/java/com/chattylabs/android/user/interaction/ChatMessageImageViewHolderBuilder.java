package com.chattylabs.android.user.interaction;

import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

class ChatMessageImageViewHolderBuilder implements ChatViewHolderBuilder {

    public static ChatViewHolderBuilder build() {
        return new ChatMessageImageViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_chat_message_image,
                viewGroup, false);
        return new ChatMessageImageViewHolderBuilder.ChatImageViewHolder(view);
    }

    static class ChatImageViewHolder extends RecyclerView.ViewHolder implements Binder {
        ImageView imageView;

        ChatImageViewHolder(View v) {
            super(v);
            imageView = (ImageView) ((ViewGroup) v).getChildAt(0);
        }

        @Override
        public void onBind(ChatInteractionViewAdapter adapter, int position) {
            ChatMessageImage message = (ChatMessageImage) adapter.getItems().get(position);
            imageView.setTag(message.id);
            imageView.setImageResource(message.image);
            if (message.tintColor > 0) {
                imageView.setImageTintList(ColorStateList.valueOf(message.tintColor));
            } // TODO reset tintColor
        }
    }
}
