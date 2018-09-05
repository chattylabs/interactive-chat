package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.Objects;

public class ChatActionImage extends ChatAction implements HasId, HasContentDescriptions,
        HasOnSelected, CanSkipTracking, CanStopFlow,
        HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded {
    final String id;
    final int image;
    final int imageAfter;
    final int tintColor;
    final String[] contentDescriptions;
    final int order;
    final Runnable onLoaded;
    final ChatAction.OnSelected onSelected;
    boolean skipTracking;
    boolean stopFlow;

    public static class Builder {
        private String id;
        private int image;
        private int imageAfter;
        private int tintColor;
        private String[] contentDescriptions;
        private int order;
        private Runnable onLoaded;
        private OnSelected onSelected;
        private boolean skipTracking;
        private boolean stopFlow;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setImage(@DrawableRes int image) {
            this.image = image;
            return this;
        }

        public Builder setImageAfter(@DrawableRes int imageAfter) {
            this.imageAfter = imageAfter;
            return this;
        }

        public Builder setTintColor(@ColorRes int tintColor) {
            this.tintColor = tintColor;
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

        public ChatActionImage build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Property \"id\" is required");
            }
            if (image <= 0) {
                throw new NullPointerException("Property \"image\" is required");
            }
            return new ChatActionImage(this);
        }
    }

    private ChatActionImage(Builder builder) {
        this.id = builder.id;
        this.image = builder.image;
        this.imageAfter = builder.imageAfter;
        this.tintColor = builder.tintColor;
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

    public int getImage() {
        return image;
    }

    public int getImageAfter() {
        return imageAfter;
    }

    public int getTintColor() {
        return tintColor;
    }

    @Override
    public String[] getContentDescriptions() {
        return contentDescriptions;
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
    public ChatNode buildActionFeedback() {
        return new ChatActionFeedbackImage.Builder()
                .setImage(imageAfter > 0 ? imageAfter : image)
                .setTintColor(tintColor).build();
    }

    @Override
    public ChatActionViewBuilder getActionViewBuilder() {
        return ChatActionImageViewBuilder.build();
    }

    @Override
    public int compareTo(@NonNull ChatAction o) {
        return Integer.compare(getOrder(), o.getOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatActionImage that = (ChatActionImage) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
