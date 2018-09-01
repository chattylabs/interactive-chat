package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public class ChatOptionAction extends ChatAction {

    private final OnSelected mOnSelected;
    private final String[] mContentDescription;
    private final String mText;
    private final OnUnselected mOnUnselected;

    public static class Builder {

        private OnSelected mOnSelected;
        private String[] mContentDescription;
        private String mText;
        private OnUnselected mOnUnselected;

        public Builder(String text) {
            mText = text;
        }

        public Builder setOnSelected(OnSelected onSelected) {
            mOnSelected = onSelected;
            return this;
        }

        public Builder setOnUnselected(OnUnselected onUnselected) {
            mOnUnselected = onUnselected;
            return this;
        }

        public Builder setContentDescription(String[] contentDescription) {
            mContentDescription = contentDescription;
            return this;
        }

        public ChatOptionAction build() {
            return new ChatOptionAction(this);
        }
    }

    ChatOptionAction(Builder builder) {
        this.mOnSelected = builder.mOnSelected;
        this.mContentDescription = builder.mContentDescription;
        this.mText = builder.mText;
        this.mOnUnselected = builder.mOnUnselected;
    }

    @Override
    public OnSelected onSelected() {
        return mOnSelected;
    }

    public OnUnselected onUnselected() {
        return mOnUnselected;
    }

    @Override
    public boolean skipTracking() {
        return false;
    }

    @Override
    public boolean stopFlow() {
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String[] getContentDescriptions() {
        return mContentDescription;
    }

    @Override
    public ChatActionViewBuilder getActionViewBuilder() {
        return null;
    }

    @Override
    public ChatNode buildActionFeedback() {
        return null;
    }

    @Override
    public Runnable onLoaded() {
        return null;
    }

    @Override
    public String getId() {
        return "";
    }

    public String getText() {
        return mText;
    }

    @Override
    public int compareTo(@NonNull ChatAction action) {
        return Integer.compare(getOrder(), action.getOrder());
    }

    interface OnUnselected {
        void execute(ChatAction action);
    }
}
