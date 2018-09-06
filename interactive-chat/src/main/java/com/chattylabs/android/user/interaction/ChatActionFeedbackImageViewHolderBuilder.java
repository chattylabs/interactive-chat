package com.chattylabs.android.user.interaction;

import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


class ChatActionFeedbackImageViewHolderBuilder implements ChatViewHolderBuilder {

    public static ChatViewHolderBuilder build() {
        return new ChatActionFeedbackImageViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_chat_action_image_selected,
                viewGroup, false);
        return new ChatActionFeedbackImageViewHolderBuilder.ChatActionImageSelectedViewHolder(view);
    }

    static class ChatActionImageSelectedViewHolder extends RecyclerView.ViewHolder implements Binder {

        ImageView image;

        ChatActionImageSelectedViewHolder(View v) {
            super(v);
            image = (ImageView) ((ViewGroup) v).getChildAt(0);
        }

        @Override
        public void onBind(ChatInteractionViewAdapter adapter, int position) {
            ChatActionFeedbackImage imageSelected = (ChatActionFeedbackImage) adapter.getItems().get(position);
            image.setImageResource(imageSelected.image);
            if (imageSelected.tintColor > 0) {
                image.setImageTintList(ColorStateList.valueOf(imageSelected.tintColor));
            } // TODO reset tintColor
        }
    }
}
