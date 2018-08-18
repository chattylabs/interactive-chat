package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;

import java.util.Objects;

public class ChatMessageFirst implements ChatNode, ChatNodeText {
    public final String id;
    public final String text;
    public final int tintColor;
    public final boolean aloud;
    public final Runnable onLoaded;
    public final float textSize;

    public static class Builder {
        private String id;
        private String text;
        private int tintColor;
        private boolean aloud;
        private Runnable onLoaded;
        private float textSize;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTintColor(@ColorRes int resId) {
            this.tintColor = resId;
            return this;
        }

        public Builder setAloud(boolean aloud) {
            this.aloud = aloud;
            return this;
        }

        public Builder setOnLoaded(Runnable afterLoaded) {
            this.onLoaded = afterLoaded;
            return this;
        }

        public Builder setTextSize(float textSizeInSp) {
            this.textSize = textSizeInSp;
            return this;
        }

        public ChatMessageFirst build() {
            if (text == null || text.length() == 0) {
                throw new NullPointerException("Property \"text\" is required");
            }
            return new ChatMessageFirst(this);
        }
    }

    private ChatMessageFirst(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.tintColor = builder.tintColor;
        this.aloud = builder.aloud;
        this.onLoaded = builder.onLoaded;
        this.textSize = builder.textSize;
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_message_first_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatMessageFirstViewHolderBuilder.build();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Runnable onLoaded() {
        return onLoaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageFirst that = (ChatMessageFirst) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
