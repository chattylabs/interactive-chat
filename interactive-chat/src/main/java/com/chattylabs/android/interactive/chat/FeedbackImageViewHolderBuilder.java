package com.chattylabs.android.interactive.chat;

import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


class FeedbackImageViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new FeedbackImageViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_chat_action_image_selected,
                viewGroup, false);
        return new FeedbackImageViewHolderBuilder.ChatActionImageSelectedViewHolder(view);
    }

    static class ChatActionImageSelectedViewHolder extends RecyclerView.ViewHolder implements Binder {

        ImageView image;

        ChatActionImageSelectedViewHolder(View v) {
            super(v);
            image = (ImageView) ((ViewGroup) v).getChildAt(0);
        }

        @Override
        public void onBind(InteractiveChatViewAdapter adapter, int position) {
            FeedbackImage imageSelected = (FeedbackImage) adapter.getItems().get(position);
            image.setImageResource(imageSelected.image);
            if (imageSelected.tintColor > 0) {
                image.setImageTintList(ColorStateList.valueOf(imageSelected.tintColor));
            } // TODO reset tintColor
        }
    }
}
