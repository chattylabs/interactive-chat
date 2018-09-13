package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.text.emoji.EmojiCompat;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.chattylabs.android.user.interaction.ChatActionMultiOption.OnOptionChangeListener;

class ChatActionMultiOptionViewBuilder implements ChatActionViewBuilder {

    private final OnOptionChangeListener onOptionChangeListener;
    private float defaultTextSize;

    ChatActionMultiOptionViewBuilder(OnOptionChangeListener onOptionChangeListener) {
        this.onOptionChangeListener = onOptionChangeListener;
    }

    @Override
    public View createView(ViewGroup viewGroup, ChatAction action) {
        final Context context = viewGroup.getContext();
        final ChatActionMultiOption multiAction = (ChatActionMultiOption) action;
        final ViewGroup multiOptionAction = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_chat_action_multi_option, viewGroup, false);
        final FlexboxLayout optionsLayout = (FlexboxLayout) multiOptionAction.getChildAt(0);
        final Button confirmButton = (Button) multiOptionAction.getChildAt(1);
        final List<ChatActionOption> options = multiAction.getOptions();

        for (ChatActionOption option : options) {
            final FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = context.getResources()
                    .getDimensionPixelSize(R.dimen.item_interactive_chat_margin_left);
            layoutParams.bottomMargin = context.getResources()
                    .getDimensionPixelSize(R.dimen.item_interactive_chat_margin_bottom);
            optionsLayout.addView(getOption(context, option, optionsLayout), layoutParams);
        }

        final CharSequence text = EmojiCompat.get().process(
                multiAction.getConfirmationAction().text);
        final Spanned span = ChatInteractionComponentImpl.makeText(text);
        confirmButton.setText(span);
        return multiOptionAction;
    }

    private View getOption(Context context, ChatActionOption option, FlexboxLayout container) {
        final ToggleButton optionButton = (ToggleButton) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_chat_option_text, container, false);

        final CharSequence text = EmojiCompat.get().process(option.getText());
        final Spanned span = ChatInteractionComponentImpl.makeText(text);

        optionButton.setText(span);
        optionButton.setTextOff(span);
        optionButton.setTextOn(span);

        if (defaultTextSize == 0) defaultTextSize = optionButton.getTextSize();
        if (option.textSize > 0) {
            optionButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, option.textSize);
        } else {
            optionButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
        }

        optionButton.setOnCheckedChangeListener((compoundButton, selected) -> {
            option.setSelected(selected);
            if (onOptionChangeListener != null) {
                onOptionChangeListener.onChange(option, selected);
            }
        });

        optionButton.setSelected(option.isSelected());

        return optionButton;
    }
}
