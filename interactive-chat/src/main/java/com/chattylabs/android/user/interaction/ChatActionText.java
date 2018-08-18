package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import java.util.Objects;

public class ChatActionText extends ChatAction {
    public final String id;
    public final String text;
    public final String textAfter;
    public final int tintColor;
    public final float textSize;
    private final String[] contentDescriptions;
    private final int order;
    public final Runnable onLoaded;
    private final OnSelected onSelected;
    private boolean isDefault;
    private boolean skipTracking;
    private boolean stopFlow;
    private boolean keepAction;

    public static class Builder {
        private String id;
        private String text;
        private String textAfter;
        private float textSize;
        private String[] contentDescriptions;
        private int tintColor;
        private Runnable onLoaded;
        private OnSelected onSelected;
        private boolean isDefault;
        private int order;
        private boolean skipTracking;
        private boolean stopFlow;
        private boolean keepAction;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTextAfter(String textAfter) {
            this.textAfter = textAfter;
            return this;
        }

        public Builder setTextSize(float textSizeInSp) {
            this.textSize = textSizeInSp;
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

        public Builder setOnLoaded(Runnable afterLoaded) {
            this.onLoaded = afterLoaded;
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

        public ChatActionText build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Property \"id\" is required");
            }
            if (text == null || text.length() == 0) {
                throw new NullPointerException("Property \"text\" is required");
            }
            return new ChatActionText(this);
        }
    }

    private ChatActionText(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.textAfter = builder.textAfter;
        this.contentDescriptions = builder.contentDescriptions;
        this.tintColor = builder.tintColor;
        this.onLoaded = builder.onLoaded;
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
    public OnSelected onSelected() {
        return onSelected;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public boolean skipTracking() {
        return skipTracking;
    }

    @Override
    public boolean keepAction() {
        return keepAction;
    }

    @Override
    public boolean stopFlow() {
        return stopFlow;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String[] getContentDescriptions() {
        if (contentDescriptions != null)
            return contentDescriptions;
        else if (text != null)
            return new String[]{text};
        return new String[]{textAfter};
    }

    @Override
    public Runnable onLoaded() {
        return onLoaded;
    }

    @Override
    public ChatActionViewBuilder getActionViewBuilder() {
        return ChatActionTextViewBuilder.build();
    }

    @Override
    public ChatNode buildActionSelected() {
        return new ChatActionTextSelected.Builder()
                .setText(textAfter != null ? textAfter : text)
                .setTintColor(tintColor)
                .setOrder(order)
                .setTextSize(textSize).build();
    }

    @Override
    public int compareTo(@NonNull ChatAction o) {
        return Integer.compare(getOrder(), o.getOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatActionText that = (ChatActionText) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
