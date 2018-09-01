package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.text.emoji.widget.EmojiButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

public class ChatMultiOptionActionViewBuilder implements ChatActionViewBuilder {

    private final OnOptionStateChangeListener mStateChangeListener;

    public ChatMultiOptionActionViewBuilder(OnOptionStateChangeListener stateChangeListener) {
        mStateChangeListener = stateChangeListener;
    }

    @Override
    public View createView(ViewGroup viewGroup, ChatAction action) {
        final ChatMultiOptionAction multiAction = (ChatMultiOptionAction) action;
        final Context context = viewGroup.getContext();
        final ViewGroup multiOptionAction = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.item_multi_option_action, viewGroup, false);
        final EmojiButton confirmButton = multiOptionAction.findViewById(R.id.confirmAction);
        final FlowLayout flowLayout = multiOptionAction.findViewById(R.id.optionContainer);
        final List<ChatOptionAction> options = multiAction.getActions();

        for (ChatOptionAction option : options) {
            final FlowLayout.LayoutParams layoutParams = new FlowLayout
                    .LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 10;
            layoutParams.leftMargin = 10;

            flowLayout.addView(getOption(context, option, flowLayout), layoutParams);
        }

        confirmButton.setText(((ChatActionText) multiAction.getConfirmationAction()).text);
        return multiOptionAction;
    }

    private View getOption(Context context, ChatOptionAction optionAction, FlowLayout container) {
        final ToggleButton optionButton = (ToggleButton) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_chat_multi_action_text, container, false);
        optionButton.setText(optionAction.getText());
        optionButton.setTextOff(optionAction.getText());
        optionButton.setTextOn(optionAction.getText());

        optionButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b && optionAction.onSelected() != null) {
                optionAction.onSelected().execute(optionAction);
            } else if (!b && optionAction.onUnselected() != null) {
                optionAction.onUnselected().execute(optionAction);
            }

            if (mStateChangeListener != null) {
                mStateChangeListener.onStateChanged(optionAction, b);
            }
        });

        return optionButton;
    }

    interface OnOptionStateChangeListener {

        void onStateChanged(ChatOptionAction action, boolean isSelected);
    }
}
