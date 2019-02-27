package com.chattylabs.android.interactive.chat;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

class ImageFeedback extends Feedback implements HasViewType {
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

        public ImageFeedback build() {
            return new ImageFeedback(this);
        }
    }

    private ImageFeedback(Builder builder) {
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
        return R.id.interactive_chat_feedback_image_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return ImageFeedbackViewHolderBuilder.build();
    }
}
