package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;

class ChatActionFeedbackText  extends ChatActionFeedback implements HasViewType, HasOnLoaded {
    String text;
    final float textSize;
    final int tintColor;

    public static class Builder {
        private String text;
        private float textSize;
        private int tintColor;

        public Builder() {}

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder setTintColor(@ColorRes int tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public ChatActionFeedbackText build() {
            return new ChatActionFeedbackText(this);
        }
    }

    ChatActionFeedbackText(Builder builder) {
        this.text = builder.text;
        this.textSize = builder.textSize;
        this.tintColor = builder.tintColor;
    }

    public String getText() {
        return text;
    }

    public float getTextSize() {
        return textSize;
    }

    public int getTintColor() {
        return tintColor;
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_action_feedback_text_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatActionFeedbackTextViewHolderBuilder.build();
    }

    @Override @Deprecated
    public Runnable onLoaded() {
        throw new IllegalAccessError();
    }
}
