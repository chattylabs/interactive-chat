package com.chattylabs.android.interactive.chat;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MultiChoiceAction implements HasId,
        HasOnSelected, CanSkipTracking, CanStopFlow, CanHandleState, CanCheckContentDescriptions,
        HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, Action {

    @VisibleForTesting
    static final String SELECTED_OPTIONS = BuildConfig.APPLICATION_ID + ".MULTI_OPTION_SELECTED_OPTIONS";

    final String id;
    final Runnable onLoaded;
    final List<Choice> choices;
    final TextAction confirmationAction;
    final OnOptionChangeListener onOptionChangeListener;
    final boolean skipTracking;
    final boolean stopFlow;

    private MultiChoiceAction(Builder builder) {
        this.id = builder.id;
        this.onLoaded = builder.onLoaded;
        this.choices = builder.choices;
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

    public List<Choice> getChoices() {
        return choices;
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
    public ActionViewBuilder getActionViewBuilder() {
        Collections.sort(choices);
        return new MultiChoiceActionViewBuilder(onOptionChangeListener);
    }

    @Override
    public InteractiveChatNode buildActionFeedback() {
        Collections.sort(choices);
        return new MultiChoiceTextFeedback.Builder()
                .setChoices(choices).build();
    }

    @Override
    public int compareTo(@NonNull Action action) {
        return Integer.compare(getOrder(), action.getOrder());
    }

    public TextAction getConfirmationAction() {
        return confirmationAction;
    }

    @Override
    public void saveState(SharedPreferences sharedPreferences) {
        HashSet<String> selectedOptions = new HashSet<>();
        for (Choice choice : choices) {
            if (choice.isSelected) selectedOptions.add(choice.id);
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
            for (Choice choice : choices) {
                choice.setSelected(selectedOptions.contains(choice.getId()));
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

        List<Choice> currentChoices = this.getChoices();

        for(Choice choice : currentChoices) {
            String[] expected = choice.getContentDescriptions();
            if (expected != null && expected.length > 0 && checkWord(expected, result)) {
                choice.toggleButton.setChecked(true);
                atLeastOneOptionWasSelected = true;
            }
        }

        TextAction textAction = this.getConfirmationAction();

        String[] expected = textAction.getContentDescriptions();
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
        private List<Choice> choices = new ArrayList<>();
        private TextAction confirmationAction;
        private OnOptionChangeListener onOptionChangeListener;
        private boolean skipTracking;
        private boolean stopFlow;

        public Builder(@NonNull String id) {
            this.id = id;
        }

        public Builder addOption(Choice choice) {
            choices.add(choice);
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

        public Builder setConfirmationAction(TextAction confirmationAction) {
            this.confirmationAction = confirmationAction;
            return this;
        }

        public Builder setOnOptionChangeListener(OnOptionChangeListener changeListener) {
            this.onOptionChangeListener = changeListener;
            return this;
        }

        public MultiChoiceAction build() {
            if (id == null || id.isEmpty()) {
                throw new NullPointerException("Property \"id\" is required");
            }

            if (choices.isEmpty()) {
                throw new IllegalArgumentException("Property \"choices\" is empty");
            }

            if (confirmationAction == null) {
                throw new NullPointerException("Forgot to set \"confirmationAction\" property?");
            }

            return new MultiChoiceAction(this);
        }
    }

    public interface OnOptionChangeListener {
        void onChange(Choice choice, boolean selected);
    }
}
