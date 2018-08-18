package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import java.util.Objects;

public class ChatMessageImage implements ChatNode {
    public final String id;
    public final int image;
    public final int tintColor;
    public final Runnable onLoaded;

    public static class Builder {
        private String id;
        private int image;
        private int tintColor;
        private Runnable onLoaded;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setImage(@DrawableRes int resId) {
            this.image = resId;
            return this;
        }

        public Builder setTintColor(@ColorRes int resId) {
            this.tintColor = resId;
            return this;
        }

        public Builder setOnLoaded(Runnable afterLoaded) {
            this.onLoaded = afterLoaded;
            return this;
        }

        public ChatMessageImage build() {
            if (image <= 0) {
                throw new NullPointerException("Property \"image\" is required");
            }
            return new ChatMessageImage(this);
        }
    }

    private ChatMessageImage(Builder builder) {
        this.id = builder.id;
        this.image = builder.image;
        this.tintColor = builder.tintColor;
        this.onLoaded = builder.onLoaded;
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_message_image_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatMessageImageViewHolderBuilder.build();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Runnable onLoaded() {
        return onLoaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageImage that = (ChatMessageImage) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
