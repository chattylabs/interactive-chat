package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.text.emoji.widget.EmojiButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

public class ChatActionMultiOptionViewBuilder implements ChatActionViewBuilder {

    private final ChatActionMultiOption.OnOptionChangeListener mOnOptionChangeListener;

    ChatActionMultiOptionViewBuilder(ChatActionMultiOption.OnOptionChangeListener onOptionChangeListener) {
        mOnOptionChangeListener = onOptionChangeListener;
    }

    @Override
    public View createView(ViewGroup viewGroup, ChatAction action) {
        final Context context = viewGroup.getContext();
        final ChatActionMultiOption multiAction = (ChatActionMultiOption) action;
        final ViewGroup multiOptionAction = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_chat_action_multiple_option, viewGroup, false);
        final FlexboxLayout flexboxLayout = (FlexboxLayout) multiOptionAction.getChildAt(0);
        final EmojiButton confirmButton = (EmojiButton) multiOptionAction.getChildAt(1);
        final List<ChatActionOption> options = multiAction.getOptions();

        for (ChatActionOption option : options) {
            final FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = context.getResources()
                    .getDimensionPixelSize(R.dimen.chat_action_multi_option_margin_left);
            layoutParams.bottomMargin = context.getResources()
                    .getDimensionPixelSize(R.dimen.chat_action_multi_option_margin_bottom);

            flexboxLayout.addView(getOption(context, option, flexboxLayout), layoutParams);
        }

        confirmButton.setText(((ChatActionText) multiAction.getConfirmationAction()).text);
        return multiOptionAction;
    }

    private View getOption(Context context, ChatActionOption option, FlexboxLayout container) {
        final ToggleButton optionButton = (ToggleButton) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_chat_multi_action_text, container, false);
        optionButton.setText(option.getText());
        optionButton.setTextOff(option.getText());
        optionButton.setTextOn(option.getText());

        optionButton.setOnCheckedChangeListener((compoundButton, selected) -> {
            option.setSelected(selected);
            if (mOnOptionChangeListener != null) {
                mOnOptionChangeListener.onChange(option, selected);
            }
        });

        return optionButton;
    }
}
