package chattylabs.assistant;

import androidx.annotation.LayoutRes;

import java.util.List;

class FeedbackActionMultiChoice extends Feedback implements HasViewLayout {
    List<ActionChipChoice> actionChipChoices;

    private FeedbackActionMultiChoice(Builder builder) {
        this.actionChipChoices = builder.actionChipChoices;
    }

    @Override @LayoutRes
    public int getViewLayout() {
        return R.layout.item_interactive_assistant_action_multichoice;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return FeedbackActionMultiChoiceViewHolderBuilder.build();
    }

    public static class Builder {

        private List<ActionChipChoice> actionChipChoices;

        public Builder setActionChipChoices(List<ActionChipChoice> actionChipChoices) {
            this.actionChipChoices = actionChipChoices;
            return this;
        }

        public FeedbackActionMultiChoice build() {
            return new FeedbackActionMultiChoice(this);
        }
    }
}
