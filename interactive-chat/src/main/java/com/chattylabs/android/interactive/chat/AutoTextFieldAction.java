package com.chattylabs.android.interactive.chat;

import android.support.annotation.NonNull;
import android.widget.AutoCompleteTextView;
import android.widget.ToggleButton;

import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;

import java.util.Objects;

public class AutoTextFieldAction implements HasId, CanSkipTracking, CanStopFlow,
        HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, Action {

    final String id;
    final float textSize;
    final int order;
    final Runnable onLoaded;
    boolean skipTracking;
    boolean stopFlow;
    AutoCompleteTextView widget;

    public static class Builder {
        private String id;
        private float textSize;
        private int order;
        private Runnable onLoaded;
        private boolean skipTracking;
        private boolean stopFlow;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setTextSize(float textSizeInSp) {
            this.textSize = textSizeInSp;
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

        public Builder skipTracking(boolean skipTracking) {
            this.skipTracking = skipTracking;
            return this;
        }

        public Builder stopFlow(boolean stopFlow) {
            this.stopFlow = stopFlow;
            return this;
        }

        public AutoTextFieldAction build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Property \"id\" is required");
            }
            return new AutoTextFieldAction(this);
        }
    }

    AutoTextFieldAction(Builder builder) {
        this.id = builder.id;
        this.textSize = builder.textSize;
        this.order = builder.order;
        this.onLoaded = builder.onLoaded;
        this.skipTracking = builder.skipTracking;
        this.stopFlow = builder.stopFlow;
    }

    void attach(AutoCompleteTextView widget) {
        this.widget = widget;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getText() {
        return widget.getText().toString();
    }

    public float getTextSize() {
        return textSize;
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
    public boolean skipTracking() {
        return skipTracking;
    }

    @Override
    public boolean stopFlow() {
        return stopFlow;
    }

    @Override
    public ActionViewBuilder getActionViewBuilder() {
        return AutoTextFieldActionViewBuilder.build();
    }

    @Override
    public InteractiveChatNode buildActionFeedback() {
        return new TextFeedback.Builder()
                .setText(getText())
                .setTextSize(textSize).build();
    }

    @Override
    public int compareTo(@NonNull Action o) {
        return Integer.compare(getOrder(), o.getOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoTextFieldAction that = (AutoTextFieldAction) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
