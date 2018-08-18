package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import java.util.Objects;

class ChatActionTextSelected implements ChatNode, Comparable<ChatActionTextSelected> {
    public final String text;
    public final float textSize;
    public final int tintColor;
    public final int order;

    public static class Builder {
        private String text;
        private float textSize;
        private int tintColor;
        private int order;

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

        public Builder setOrder(int order) {
            this.order = order;
            return this;
        }

        public ChatActionTextSelected build() {
            return new ChatActionTextSelected(this);
        }
    }

    private ChatActionTextSelected(Builder builder) {
        this.text = builder.text;
        this.textSize = builder.textSize;
        this.tintColor = builder.tintColor;
        this.order = builder.order;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_action_text_selected_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatActionTextSelectedViewHolderBuilder.build();
    }

    @Override
    public Runnable onLoaded() {
        return null;
    }

    @Override
    public int compareTo(@NonNull ChatActionTextSelected o) {
        return Integer.compare(this.order, o.order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatActionTextSelected that = (ChatActionTextSelected) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
