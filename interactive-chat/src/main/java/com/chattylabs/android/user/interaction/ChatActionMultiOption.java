package com.chattylabs.android.user.interaction;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ChatActionMultiOption implements HasId,
        HasOnSelected, CanSkipTracking, CanStopFlow, CanHandleState, CanCheckContentDescriptions,
        HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, ChatAction {

    @VisibleForTesting
    static final String SELECTED_OPTIONS = BuildConfig.APPLICATION_ID + ".MULTI_OPTION_SELECTED_OPTIONS";

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
    public String getId() {
        return this.id;
    }

    @Override
    public Runnable onLoaded() {
        return this.onLoaded;
    }

    public List<ChatActionOption> getOptions() {
        return options;
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
        Collections.sort(options);
        return new ChatActionMultiOptionViewBuilder(onOptionChangeListener);
    }

    @Override
    public ChatNode buildActionFeedback() {
        Collections.sort(options);
        return new ChatActionMultiOptionFeedbackText.Builder()
                .setOptions(options).build();
    }

    @Override
    public int compareTo(@NonNull ChatAction chatAction) {
        return Integer.compare(getOrder(), chatAction.getOrder());
    }

    public ChatActionText getConfirmationAction() {
        return confirmationAction;
    }

    @Override
    public void saveState(SharedPreferences sharedPreferences) {
        HashSet<String> selectedOptions = new HashSet<>();
        for (ChatActionOption option: options) {
            if (option.isSelected) selectedOptions.add(option.id);
        }
        if (!selectedOptions.isEmpty())
            sharedPreferences.edit().putStringSet(
                    SELECTED_OPTIONS, selectedOptions).apply();
    }

    @Override
    public void restoreSavedState(SharedPreferences sharedPreferences) {
        HashSet<String> selectedOptions =
                (HashSet<String>) sharedPreferences.getStringSet(SELECTED_OPTIONS, new HashSet<>());
        if (!selectedOptions.isEmpty()) {
            for (ChatActionOption option: options) {
                option.setSelected(selectedOptions.contains(option.getId()));
            }
        }
    }

    private boolean checkWord(@NonNull String[] patterns, @NonNull String text) {
        for (String pattern : patterns) {
            if (pattern != null && ConversationalFlowComponent.matches(text, pattern)) return true;
        }
        return false;
    }

    @Override
    public int matches(String result) {
        boolean atLeastOneOptionWasSelected = false;

        List<ChatActionOption> currentOptions = this.getOptions();

        for(ChatActionOption option: currentOptions) {
            String[] expected = option.getContentDescriptions();
            if (expected != null && expected.length > 0 && checkWord(expected, result)) {
                option.toggleButton.setChecked(true);
                atLeastOneOptionWasSelected = true;
            }
        }

        ChatActionText actionText = this.getConfirmationAction();

        String[] expected = actionText.getContentDescriptions();
        if (expected != null && expected.length > 0 && checkWord(expected, result)) {
            return MATCHED;
        } else if (atLeastOneOptionWasSelected) {
            return REPEAT;
        }
        return NOT_MATCHED;
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

            if (options.isEmpty()) {
                throw new IllegalArgumentException("Property \"options\" is empty");
            }

            if (confirmationAction == null) {
                throw new NullPointerException("Forgot to set \"confirmationAction\" property?");
            }

            return new ChatActionMultiOption(this);
        }
    }

    public interface OnOptionChangeListener {
        void onChange(ChatActionOption option, boolean selected);
    }
}
