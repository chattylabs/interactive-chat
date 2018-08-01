package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class Message implements Node {
    public final String id;
    public final String text;
    public final int imageId;
    public final int tintColorId;
    public final boolean aloud;
    public final boolean showAsFirst;
    public final boolean showAsAnswer;
    public final Runnable onLoaded;
    public final float textSize;

    public static class Builder {
        private String id;
        private String text;
        private int imageId;
        private int tintColorId;
        private boolean aloud;
        private boolean showAsFirst;
        private boolean showAsAnswer;
        private Runnable onLoaded;
        private float textSize;

        public Builder() {
        }

        public Builder(String id) {
            this.id = id;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setImage(@DrawableRes int resId) {
            this.imageId = resId;
            return this;
        }

        public Builder setTintColor(@ColorRes int resId) {
            this.tintColorId = resId;
            return this;
        }

        public Builder setAloud(boolean aloud) {
            this.aloud = aloud;
            return this;
        }

        public Builder setShowAsFirst(boolean showAsFirst) {
            this.showAsFirst = showAsFirst;
            return this;
        }

        public Builder setShowAsAnswer(boolean showAsAnswer) {
            this.showAsAnswer = showAsAnswer;
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

        public Message build() {
            if (!(text != null && text.length() > 0) && imageId <= 0) {
                throw new NullPointerException("At least Message properties \"text\" " +
                        "or \"image\" must be set");
            }
            return new Message(this);
        }
    }

    private Message(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.imageId = builder.imageId;
        this.tintColorId = builder.tintColorId;
        this.aloud = builder.aloud;
        this.showAsFirst = builder.showAsFirst;
        this.showAsAnswer = builder.showAsAnswer;
        this.onLoaded = builder.onLoaded;
        this.textSize = builder.textSize;
    }

    @Override
    public String getId() {
        return id;
    }
}
