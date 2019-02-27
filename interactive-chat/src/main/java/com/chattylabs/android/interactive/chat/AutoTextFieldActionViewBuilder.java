package com.chattylabs.android.interactive.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

class AutoTextFieldActionViewBuilder implements ActionViewBuilder {
    private static AutoTextFieldActionViewBuilder instance;

    public static ActionViewBuilder build() {
        return instance == null ? instance = new AutoTextFieldActionViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        AutoCompleteTextView widget = (AutoCompleteTextView) inflater.inflate(
                R.layout.item_interactive_chat_action_autotextfield,
                viewGroup, false);

        AutoTextFieldAction autoTextFieldAction = (AutoTextFieldAction) action;
        // TODO: Add adapter, etc..

        widget.setTag(R.id.interactive_chat_action_id, autoTextFieldAction.id);

        autoTextFieldAction.attach(widget);

        return widget;
    }
}
