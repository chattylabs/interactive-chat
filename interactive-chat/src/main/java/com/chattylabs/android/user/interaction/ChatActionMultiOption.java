package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ChatActionMultiOption extends ChatAction implements HasId,
        HasOnSelected, CanSkipTracking, CanStopFlow,
        HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded {
    final String id;
    final Runnable onLoaded;
    final List<ChatActionOption> options;
    final ChatActionText confirmationAction;
    final OnOptionChangeListener onOptionChangeListener;
    final boolean skipTracking;
    final boolean stopFlow;

    private ChatActionMultiOption(Builder builder) {
        this.id = builder.id;
        this.onLoaded = builder.onLoaded;
        this.options = builder.options;
        this.confirmationAction = builder.confirmationAction;
        this.onOptionChangeListener = builder.onOptionChangeListener;
        this.skipTracking = builder.skipTracking;
        this.stopFlow = builder.stopFlow;
    }

    @Override
    public OnSelected onSelected() {
        return confirmationAction.onSelected();
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
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatActionViewBuilder getActionViewBuilder() {
        return new ChatActionMultiOptionViewBuilder(onOptionChangeListener);
    }

    @Override
    public ChatNode buildActionFeedback() {
        return new ChatActionMultiOptionFeedbackText.Builder().setOptions(options).build();
    }

    @Override
    public Runnable onLoaded() {
        return this.onLoaded;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int compareTo(@NonNull ChatAction chatAction) {
        return Integer.compare(getOrder(), chatAction.getOrder());
    }

    public List<ChatActionOption> getOptions() {
        return options;
    }

    public ChatActionText getConfirmationAction() {
        return confirmationAction;
    }

    public static class Builder {
        private String id;
        private Runnable onLoaded;
        private List<ChatActionOption> options = new ArrayList<>();
        private ChatActionText confirmationAction;
        private OnOptionChangeListener onOptionChangeListener;
        private boolean skipTracking;
        private boolean stopFlow;

        public Builder(@NonNull String id) {
            this.id = id;
        }

        public Builder addOption(ChatActionOption option) {
            options.add(option);
            return this;
        }

        public Builder setSkipTracking(boolean skipTracking) {
            this.skipTracking = skipTracking;
            return this;
        }

        public Builder setStopFlow(boolean stopFlow) {
            this.stopFlow = stopFlow;
            return this;
        }

        public Builder setOnLoaded(Runnable loaded) {
            onLoaded = loaded;
            return this;
        }

        public Builder setConfirmationAction(ChatActionText confirmationAction) {
            this.confirmationAction = confirmationAction;
            return this;
        }

        public Builder setOnOptionChangeListener(OnOptionChangeListener changeListener) {
            this.onOptionChangeListener = changeListener;
            return this;
        }

        public ChatActionMultiOption build() {
            if (id == null || id.isEmpty()) {
                throw new NullPointerException("Property \"id\" is required");
            }

            if (options.size() == 0) {
                throw new IllegalArgumentException("Property  \"option\" is empty");
            }

            if (confirmationAction == null) {
                throw new NullPointerException("Forgot to set \"confirmationAction\"?");
            }

            return new ChatActionMultiOption(this);
        }
    }

    public interface OnOptionChangeListener {
        void onChange(ChatActionOption option, boolean selected);
    }
}
