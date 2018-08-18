package com.chattylabs.android.user.interaction;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

class ChatActionImageViewBuilder implements ChatActionViewBuilder {
    private static ChatActionImageViewBuilder instance;

    public static ChatActionViewBuilder build() {
        return instance == null ? instance = new ChatActionImageViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, ChatAction action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ImageButton button = (ImageButton) inflater.inflate(
                R.layout.item_interactive_chat_action_image,
                viewGroup, false);

        ChatActionImage actionImage = (ChatActionImage) action;
        button.setImageResource(actionImage.image);
        if (actionImage.tintColor > 0) {
            button.setImageTintList(ColorStateList.valueOf(actionImage.tintColor));
        }
        button.setTag(R.id.interactive_chat_action_id, actionImage.id);

        return button;
    }
}
