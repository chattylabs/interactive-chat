package com.chattylabs.android.interactive.chat;

import android.support.text.emoji.EmojiCompat;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

class ActionTextViewBuilder implements ActionViewBuilder {
    private static ActionTextViewBuilder instance;
    private float defaultTextSize;

    public static ActionViewBuilder build() {
        return instance == null ? instance = new ActionTextViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Button button = (Button) inflater.inflate(
                R.layout.item_interactive_chat_action_text,
                viewGroup, false);

        ActionText actionText = (ActionText) action;
        if (defaultTextSize == 0) defaultTextSize = button.getTextSize();
        if (actionText.textSize > 0) {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, actionText.textSize);
        } else {
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
        }
        CharSequence text = EmojiCompat.get().process(actionText.text);
        Spanned span = InteractiveChatComponentImpl.makeText(text);
        button.setText(span);

        button.setTag(R.id.interactive_chat_action_id, actionText.id);

        return button;
    }
}
