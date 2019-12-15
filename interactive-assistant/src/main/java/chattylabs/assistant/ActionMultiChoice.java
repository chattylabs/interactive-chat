package chattylabs.assistant;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import chattylabs.conversations.ConversationalFlow;

public class ActionMultiChoice implements HasId,
                                          HasOnSelected, CanSkipTracking, CanSkipSelected, CanHandleState, CanCheckContentDescriptions,
                                          HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, Action {

    @VisibleForTesting
    static final String SELECTED_OPTIONS = BuildConfig.LIBRARY_PACKAGE_NAME + ".MULTI_OPTION_SELECTED_OPTIONS";

    final String id;
    final Runnable onLoaded;
    final List<ActionChipChoice> actionChipChoices;
    final ActionText confirmationAction;
    final OnOptionChangeListener onOptionChangeListener;
    final boolean skipTracking;
    final boolean skipSelected;

    private ActionMultiChoice(Builder builder) {
        this.id                     = builder.id;
        this.onLoaded               = builder.onLoaded;
        this.actionChipChoices      = builder.actionChipChoices;
        this.confirmationAction     = builder.confirmationAction;
        this.onOptionChangeListener = builder.onOptionChangeListener;
        this.skipTracking           = builder.skipTracking;
        this.skipSelected           = builder.skipSelected;
    }

    @NonNull @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Runnable onLoaded() {
        return this.onLoaded;
    }

    public List<ActionChipChoice> getActionChipChoices() {
        return actionChipChoices;
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
    public boolean skipSelected() {
        return skipSelected;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ActionViewBuilder getActionViewBuilder() {
        Collections.sort(actionChipChoices);
        return new ActionMultiChoiceViewBuilder(onOptionChangeListener);
    }

    @Override
    public Node buildActionFeedback() {
        Collections.sort(actionChipChoices);
        return new FeedbackMultiChoice.Builder()
                .setActionChipChoices(actionChipChoices).build();
    }

    @Override
    public int compareTo(@NonNull Action action) {
        return Integer.compare(getOrder(), action.getOrder());
    }

    public ActionText getConfirmationAction() {
        return confirmationAction;
    }

    @Override
    public void saveState(SharedPreferences sharedPreferences) {
        HashSet<String> selectedOptions = new HashSet<>();
        for (ActionChipChoice actionChipChoice : actionChipChoices) {
            if (actionChipChoice.isSelected) selectedOptions.add(actionChipChoice.id);
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
            for (ActionChipChoice actionChipChoice : actionChipChoices) {
                actionChipChoice.setSelected(selectedOptions.contains(actionChipChoice.getId()));
            }
        }
    }

    private boolean checkWord(@NonNull String[] patterns, @NonNull String text) {
        for (String pattern : patterns) {
            if (pattern != null && ConversationalFlow.matches(text, pattern)) return true;
        }
        return false;
    }

    @Override
    public int matches(String result) {
        boolean atLeastOneOptionWasSelected = false;

        List<ActionChipChoice> currentActionChipChoices = this.getActionChipChoices();

        for(ActionChipChoice actionChipChoice : currentActionChipChoices) {
            String[] expected = actionChipChoice.getContentDescriptions();
            if (expected != null && expected.length > 0 && checkWord(expected, result)) {
                actionChipChoice.chip.setChecked(true);
                atLeastOneOptionWasSelected = true;
            }
        }

        ActionText actionText = this.getConfirmationAction();

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
        private List<ActionChipChoice> actionChipChoices = new ArrayList<>();
        private ActionText confirmationAction;
        private OnOptionChangeListener onOptionChangeListener;
        private boolean skipTracking;
        private boolean skipSelected;

        public Builder(@NonNull String id) {
            this.id = id;
        }

        public Builder addOption(ActionChipChoice actionChipChoice) {
            actionChipChoices.add(actionChipChoice);
            return this;
        }

        public Builder skipTracking(boolean skipTracking) {
            this.skipTracking = skipTracking;
            return this;
        }

        public Builder skipSelected(boolean skipSelected) {
            this.skipSelected = skipSelected;
            return this;
        }

        public Builder setOnLoaded(Runnable loaded) {
            onLoaded = loaded;
            return this;
        }

        public Builder setConfirmationAction(ActionText confirmationAction) {
            this.confirmationAction = confirmationAction;
            return this;
        }

        public Builder setOnOptionChangeListener(OnOptionChangeListener changeListener) {
            this.onOptionChangeListener = changeListener;
            return this;
        }

        public ActionMultiChoice build() {
            if (id == null || id.isEmpty()) {
                throw new NullPointerException("Property \"id\" is required");
            }

            if (actionChipChoices.isEmpty()) {
                throw new IllegalArgumentException("Property \"actionChipChoices\" is empty");
            }

            if (confirmationAction == null) {
                throw new NullPointerException("Forgot to set \"confirmationAction\" property?");
            }

            return new ActionMultiChoice(this);
        }
    }

    public interface OnOptionChangeListener {
        void onChange(ActionChipChoice actionChipChoice, boolean selected);
    }
}
