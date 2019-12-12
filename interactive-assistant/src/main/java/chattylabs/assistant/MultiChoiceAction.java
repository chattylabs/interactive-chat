package chattylabs.assistant;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import chattylabs.conversations.ConversationalFlow;

public class MultiChoiceAction implements HasId,
        HasOnSelected, CanSkipTracking, CanStopFlow, CanHandleState, CanCheckContentDescriptions,
        HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, Action {

    @VisibleForTesting
    static final String SELECTED_OPTIONS = BuildConfig.APPLICATION_ID + ".MULTI_OPTION_SELECTED_OPTIONS";

    final String id;
    final Runnable onLoaded;
    final List<ChoiceItem> choiceItems;
    final TextAction confirmationAction;
    final OnOptionChangeListener onOptionChangeListener;
    final boolean skipTracking;
    final boolean stopFlow;

    private MultiChoiceAction(Builder builder) {
        this.id = builder.id;
        this.onLoaded = builder.onLoaded;
        this.choiceItems = builder.choiceItems;
        this.confirmationAction = builder.confirmationAction;
        this.onOptionChangeListener = builder.onOptionChangeListener;
        this.skipTracking = builder.skipTracking;
        this.stopFlow = builder.stopFlow;
    }

    @NonNull @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Runnable onLoaded() {
        return this.onLoaded;
    }

    public List<ChoiceItem> getChoiceItems() {
        return choiceItems;
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
        Collections.sort(choiceItems);
        return new MultiChoiceActionViewBuilder(onOptionChangeListener);
    }

    @Override
    public Node buildActionFeedback() {
        Collections.sort(choiceItems);
        return new MultiChoiceTextFeedback.Builder()
                .setChoiceItems(choiceItems).build();
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
        for (ChoiceItem choiceItem : choiceItems) {
            if (choiceItem.isSelected) selectedOptions.add(choiceItem.id);
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
            for (ChoiceItem choiceItem : choiceItems) {
                choiceItem.setSelected(selectedOptions.contains(choiceItem.getId()));
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

        List<ChoiceItem> currentChoiceItems = this.getChoiceItems();

        for(ChoiceItem choiceItem : currentChoiceItems) {
            String[] expected = choiceItem.getContentDescriptions();
            if (expected != null && expected.length > 0 && checkWord(expected, result)) {
                choiceItem.toggleButton.setChecked(true);
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
        private List<ChoiceItem> choiceItems = new ArrayList<>();
        private TextAction confirmationAction;
        private OnOptionChangeListener onOptionChangeListener;
        private boolean skipTracking;
        private boolean stopFlow;

        public Builder(@NonNull String id) {
            this.id = id;
        }

        public Builder addOption(ChoiceItem choiceItem) {
            choiceItems.add(choiceItem);
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

            if (choiceItems.isEmpty()) {
                throw new IllegalArgumentException("Property \"choiceItems\" is empty");
            }

            if (confirmationAction == null) {
                throw new NullPointerException("Forgot to set \"confirmationAction\" property?");
            }

            return new MultiChoiceAction(this);
        }
    }

    public interface OnOptionChangeListener {
        void onChange(ChoiceItem choiceItem, boolean selected);
    }
}
