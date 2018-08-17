package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.Objects;

public class ChatAction implements ChatNode, Comparable<ChatAction> {
    public final String id;
    public final String text;
    public final String textAfter;
    public final String[] contentDescriptions;
    public final int image;
    public final int tintColor;
    public final OnSelected onSelected;
    public final float textSize;
    public final int order;
    public boolean isDefault;
    public boolean skipTracking;
    public boolean stopFlow;
    public boolean keepAction;

    public interface OnSelected {
        void execute(ChatAction action);
    }

    public static class Builder {
        private String id;
        private String text;
        private String textAfter;
        private String[] contentDescriptions;
        private int image;
        private int tintColor;
        private OnSelected onSelected;
        private boolean isDefault;
        private float textSize;
        private int order;
        private boolean skipTracking;
        private boolean stopFlow;
        private boolean keepAction;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTextAfter(String textAfter) {
            this.textAfter = textAfter;
            return this;
        }

        public Builder setImage(@DrawableRes int image) {
            this.image = image;
            return this;
        }

        public Builder setContentDescriptions(String[] contentDescriptions) {
            this.contentDescriptions = contentDescriptions;
            return this;
        }

        public Builder setTintColor(@ColorRes int tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public Builder setOnSelected(OnSelected onSelected) {
            this.onSelected = onSelected;
            return this;
        }

        public Builder setAsDefault(boolean aDefault) {
            isDefault = aDefault;
            return this;
        }

        public Builder setTextSize(float textSizeInSp) {
            this.textSize = textSizeInSp;
            return this;
        }

        public Builder setOrder(int order) {
            this.order = order;
            return this;
        }

        public Builder skipTracking(boolean skipTracking) {
            this.skipTracking = skipTracking;
            return this;
        }

        public Builder stopFlow(boolean stopFlow) {
            this.stopFlow = stopFlow;
            return this;
        }

        public Builder keepAction(boolean keepAction) {
            this.keepAction = keepAction;
            return this;
        }

        public ChatAction build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("An Action object requires an ID");
            }
            if (!(text != null && text.length() > 0) && !(image > 0)) {
                throw new NullPointerException("At least Action properties \"text\" or \"image\" must be set");
            }
            return new ChatAction(this);
        }
    }

    private ChatAction(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.textAfter = builder.textAfter;
        this.contentDescriptions = builder.contentDescriptions;
        this.image = builder.image;
        this.tintColor = builder.tintColor;
        this.onSelected = builder.onSelected;
        this.isDefault = builder.isDefault;
        this.textSize = builder.textSize;
        this.order = builder.order;
        this.skipTracking = builder.skipTracking;
        this.stopFlow = builder.stopFlow;
        this.keepAction = builder.keepAction;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int compareTo(@NonNull ChatAction o) {
        return Integer.compare(this.order, o.order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatAction that = (ChatAction) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
