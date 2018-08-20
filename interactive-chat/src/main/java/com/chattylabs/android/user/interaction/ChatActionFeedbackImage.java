package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import java.util.Objects;

class ChatActionFeedbackImage extends ChatActionFeedback {
    public final int image;
    public final int tintColor;

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

    @Override
    public String getId() {
        return "";
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_action_feedback_image_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatActionFeedbackImageViewHolderBuilder.build();
    }

    @Override
    public Runnable onLoaded() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatActionFeedbackImage that = (ChatActionFeedbackImage) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
