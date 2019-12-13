package chattylabs.assistant;

import java.util.List;

class FeedbackMultiChoice extends Feedback implements HasViewType {
    List<ActionChipChoice> actionChipChoices;

    private FeedbackMultiChoice(Builder builder) {
        this.actionChipChoices = builder.actionChipChoices;
    }

    @Override
    public int getViewType() {
        return R.id.interactive_assistant_multi_option_feedback_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return FeedbackMultiChoiceViewHolderBuilder.build();
    }

    public static class Builder {

        private List<ActionChipChoice> actionChipChoices;

        public Builder setActionChipChoices(List<ActionChipChoice> actionChipChoices) {
            this.actionChipChoices = actionChipChoices;
            return this;
        }

        public FeedbackMultiChoice build() {
            return new FeedbackMultiChoice(this);
        }
    }
}
