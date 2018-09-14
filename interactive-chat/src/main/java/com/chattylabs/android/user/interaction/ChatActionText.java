package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import java.util.Objects;

public class ChatActionText implements HasId, HasContentDescriptions,
        HasOnSelected, CanSkipTracking, CanStopFlow,
        HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, ChatAction {
    final String id;
    final String text;
    final String textAfter;
    final float textSize;
    final String[] contentDescriptions;
    final int order;
    final Runnable onLoaded;
    final OnSelected onSelected;
    boolean skipTracking;
    boolean stopFlow;

    public static class Builder {
        private String id;
        private String text;
        private String textAfter;
        private float textSize;
        private String[] contentDescriptions;
        private int order;
        private Runnable onLoaded;
        private OnSelected onSelected;
        private boolean skipTracking;
        private boolean stopFlow;

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

        public Builder setOrder(int order) {
            this.order = order;
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

        public Builder skipTracking(boolean skipTracking) {
            this.skipTracking = skipTracking;
            return this;
        }

        public Builder stopFlow(boolean stopFlow) {
            this.stopFlow = stopFlow;
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

    ChatActionText(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.textAfter = builder.textAfter;
        this.textSize = builder.textSize;
        this.contentDescriptions = builder.contentDescriptions;
        this.order = builder.order;
        this.onLoaded = builder.onLoaded;
        this.onSelected = builder.onSelected;
        this.skipTracking = builder.skipTracking;
        this.stopFlow = builder.stopFlow;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTextAfter() {
        return textAfter;
    }

    public float getTextSize() {
        return textSize;
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
    public int getOrder() {
        return order;
    }

    @Override
    public Runnable onLoaded() {
        return onLoaded;
    }

    @Override
    public OnSelected onSelected() {
        return onSelected;
    }

    @Override
    public boolean skipTracking() {
        return skipTracking;
    }

    @Override
    public boolean stopFlow() {
        return stopFlow;
    }

    @Override
    public ChatActionViewBuilder getActionViewBuilder() {
        return ChatActionTextViewBuilder.build();
    }

    @Override
    public ChatNode buildActionFeedback() {
        return new ChatActionFeedbackText.Builder()
                .setText(textAfter != null ? textAfter : text)
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
