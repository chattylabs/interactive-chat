package com.chattylabs.android.interactive.chat;

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

import static com.chattylabs.android.interactive.chat.MultiChoiceAction.OnOptionChangeListener;

class MultiChoiceActionViewBuilder implements ActionViewBuilder {

    private final OnOptionChangeListener onOptionChangeListener;
    private float defaultTextSize;

    MultiChoiceActionViewBuilder(OnOptionChangeListener onOptionChangeListener) {
        this.onOptionChangeListener = onOptionChangeListener;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        final Context context = viewGroup.getContext();
        final MultiChoiceAction multiAction = (MultiChoiceAction) action;
        final ViewGroup multiOptionAction = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_chat_action_multichoice, viewGroup, false);
        final FlexboxLayout optionsLayout = (FlexboxLayout) multiOptionAction.getChildAt(0);
        final Button confirmButton = (Button) multiOptionAction.getChildAt(1);
        final List<Choice> choices = multiAction.getChoices();

        for (Choice choice : choices) {
            final FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = context.getResources()
                    .getDimensionPixelSize(R.dimen.item_interactive_chat_margin_left);
            layoutParams.bottomMargin = context.getResources()
                    .getDimensionPixelSize(R.dimen.item_interactive_chat_margin_bottom);
            optionsLayout.addView(getOption(context, choice, optionsLayout), layoutParams);
        }

        final CharSequence text = EmojiCompat.get().process(
                multiAction.getConfirmationAction().text);
        final Spanned span = InteractiveChatComponent.makeText(text);
        confirmButton.setText(span);
        return multiOptionAction;
    }

    private View getOption(Context context, Choice choice, FlexboxLayout container) {
        final ToggleButton optionButton = (ToggleButton) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_chat_toggle_button, container, false);

        final CharSequence text = EmojiCompat.get().process(choice.getText());
        final Spanned span = InteractiveChatComponent.makeText(text);

        optionButton.setText(span);
        optionButton.setTextOff(span);
        optionButton.setTextOn(span);

        if (defaultTextSize == 0) defaultTextSize = optionButton.getTextSize();
        if (choice.textSize > 0) {
            optionButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, choice.textSize);
        } else {
            optionButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
        }

        optionButton.setOnCheckedChangeListener((compoundButton, selected) -> {
            choice.setSelected(selected);
            if (onOptionChangeListener != null) {
                onOptionChangeListener.onChange(choice, selected);
            }
        });

        optionButton.setSelected(choice.isSelected());

        choice.attach(optionButton);

        return optionButton;
    }
}
