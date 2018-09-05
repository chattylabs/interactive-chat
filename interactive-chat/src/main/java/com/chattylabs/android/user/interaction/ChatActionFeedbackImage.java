package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

class ChatActionFeedbackImage extends ChatActionFeedback implements HasViewType {
    final int image;
    final int tintColor;

    public static class Builder {
        private int image;
        private int tintColor;

        public Builder() {}

        public Builder setImage(@DrawableRes int image) {
            this.image = image;
            return this;
        }

        public Builder setTintColor(@ColorRes int tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public ChatActionFeedbackImage build() {
            return new ChatActionFeedbackImage(this);
        }
    }

    private ChatActionFeedbackImage(Builder builder) {
        this.image = builder.image;
        this.tintColor = builder.tintColor;
    }

    public int getImage() {
        return image;
    }

    public int getTintColor() {
        return tintColor;
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_action_feedback_image_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatActionFeedbackImageViewHolderBuilder.build();
    }
}
