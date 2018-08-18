package com.chattylabs.android.user.interaction;

import android.os.Build;
import android.support.text.emoji.EmojiCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

class ChatActionTextViewBuilder implements ChatActionViewBuilder {
    private static ChatActionTextViewBuilder instance;

    public static ChatActionViewBuilder build() {
        return instance == null ? instance = new ChatActionTextViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, ChatAction action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Button button = (Button) inflater.inflate(
                R.layout.item_interactive_chat_action_text,
                viewGroup, false);

        ChatActionText actionText = (ChatActionText) action;
        if (actionText.textSize > 0) {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, actionText.textSize);
        }
        Spanned span;
        String text = (String) EmojiCompat.get().process(actionText.text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            span = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        }
        else {
            span = Html.fromHtml(text);
        }
        button.setText(span);

        button.setTag(R.id.interactive_chat_action_id, actionText.id);

        return button;
    }
}
