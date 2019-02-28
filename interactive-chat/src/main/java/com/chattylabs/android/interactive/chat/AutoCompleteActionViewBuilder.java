package com.chattylabs.android.interactive.chat;

import android.support.text.emoji.EmojiCompat;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

class AutoCompleteActionViewBuilder implements ActionViewBuilder {
    private static AutoCompleteActionViewBuilder instance;

    public static ActionViewBuilder build() {
        return instance == null ? instance = new AutoCompleteActionViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        AutoCompleteAction autoCompleteAction = (AutoCompleteAction) action;

        LinearLayout widget = (LinearLayout) inflater.inflate(
                R.layout.item_interactive_chat_action_autotextfield,
                viewGroup, false);

        List<TextAction> actions = autoCompleteAction.getActions();

        for (TextAction item : actions) {
            Button button = (Button) inflater.inflate(R.layout.item_interactive_chat_action_text,
                    viewGroup, false);
            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = widget.getContext().getResources()
                    .getDimensionPixelSize(R.dimen.item_interactive_chat_margin_left);
            layoutParams.topMargin = widget.getContext().getResources()
                    .getDimensionPixelSize(R.dimen.item_interactive_chat_margin_top);
            final CharSequence text = EmojiCompat.get().process(item.getText());
            final Spanned span = InteractiveChatComponent.makeText(text);
            button.setText(span);
            button.setOnClickListener(v -> item.onSelected());
            ((LinearLayout) widget.getChildAt(1)).addView(button, layoutParams);
        }

        // TODO: Add adapter, etc..

        widget.setTag(R.id.interactive_chat_action_id, autoCompleteAction.id);

        autoCompleteAction.attach((AutoCompleteTextView) widget.getChildAt(0));

        return widget;
    }
}
