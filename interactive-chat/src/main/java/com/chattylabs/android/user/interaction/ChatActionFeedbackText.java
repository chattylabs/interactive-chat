package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;

import java.util.Objects;

class ChatActionFeedbackText  extends ChatActionFeedback {
    protected String text;
    public final float textSize;
    public final int tintColor;

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

    protected ChatActionFeedbackText(Builder builder) {
        this.text = builder.text;
        this.textSize = builder.textSize;
        this.tintColor = builder.tintColor;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_action_feedback_text_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatActionFeedbackTextViewHolderBuilder.build();
    }

    @Override
    public Runnable onLoaded() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatActionFeedbackText that = (ChatActionFeedbackText) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
