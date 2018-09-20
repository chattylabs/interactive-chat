package com.chattylabs.android.interactive.chat;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

class ActionImageViewBuilder implements ActionViewBuilder {
    private static ActionImageViewBuilder instance;

    public static ActionViewBuilder build() {
        return instance == null ? instance = new ActionImageViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ImageButton button = (ImageButton) inflater.inflate(
                R.layout.item_interactive_chat_action_image,
                viewGroup, false);

        ActionImage actionImage = (ActionImage) action;
        button.setImageResource(actionImage.image);
        if (actionImage.tintColor > 0) {
            button.setImageTintList(ColorStateList.valueOf(actionImage.tintColor));
        }
        button.setTag(R.id.interactive_chat_action_id, actionImage.id);

        return button;
    }
}
