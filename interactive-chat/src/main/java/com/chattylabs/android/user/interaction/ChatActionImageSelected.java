package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.Objects;

class ChatActionImageSelected implements ChatNode, Comparable<ChatActionImageSelected> {
    public final int image;
    public final int tintColor;
    public final int order;

    public static class Builder {
        private int image;
        private int tintColor;
        private int order;

        public Builder() {}

        public Builder setImage(@DrawableRes int image) {
            this.image = image;
            return this;
        }

        public Builder setTintColor(@ColorRes int tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public Builder setOrder(int order) {
            this.order = order;
            return this;
        }

        public ChatActionImageSelected build() {
            return new ChatActionImageSelected(this);
        }
    }

    private ChatActionImageSelected(Builder builder) {
        this.image = builder.image;
        this.tintColor = builder.tintColor;
        this.order = builder.order;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_action_image_selected_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatActionImageSelectedViewHolderBuilder.build();
    }

    @Override
    public Runnable onLoaded() {
        return null;
    }

    @Override
    public int compareTo(@NonNull ChatActionImageSelected o) {
        return Integer.compare(this.order, o.order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatActionImageSelected that = (ChatActionImageSelected) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
