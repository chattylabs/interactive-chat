package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.text.emoji.widget.EmojiButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

public class ChatMultiOptionActionViewBuilder implements ChatActionViewBuilder {

    @Override
    public View createView(ViewGroup viewGroup, ChatAction action) {
        final Context context = viewGroup.getContext();
        final ViewGroup flowLayout = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.view_flowlayout, viewGroup, false);
        final List<ChatOptionAction> options = ((ChatMultiOptionAction) action).getActions();

        for (ChatOptionAction option : options) {
            final EmojiButton button = (EmojiButton) LayoutInflater.from(context)
                    .inflate(R.layout.item_interactive_chat_action_text, flowLayout, false);
            button.setText(option.getText());
            final FlowLayout.LayoutParams layoutParams = new FlowLayout
                    .LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 10;
            layoutParams.leftMargin = 10;

            flowLayout.addView(button, layoutParams);
        }
        return flowLayout;
    }
}
