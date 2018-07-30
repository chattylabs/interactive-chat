package com.chattylabs.android.user.assistant;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

public class Action implements Node, Identifier, Comparable<Action> {
    public final String id;
    public final String text1;
    public final String text2;
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
        void execute(Action action);
    }

    public static class Builder {
        private String id;
        private String text1;
        private String text2;
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

        public Builder setText1(String text1) {
            this.text1 = text1;
            return this;
        }

        public Builder setText2(String text2) {
            this.text2 = text2;
            return this;
        }

        public Builder setImage(@DrawableRes int image) {
            this.image = image;
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

        public Action build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Action id must be set");
            }
            if (!(text1 != null && text1.length() > 0) && !(image > 0)) {
                throw new NullPointerException("At least Action properties \"text1\" or \"image\" must be set");
            }
            return new Action(this);
        }
    }

    private Action(Builder builder) {
        this.id = builder.id;
        this.text1 = builder.text1;
        this.text2 = builder.text2;
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
    public int compareTo(@NonNull Action o) {
        return this.order < o.order ? -1 :
               (this.order > o.order ? 1 : 0);
    }
}
