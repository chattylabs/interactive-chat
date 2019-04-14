package chattylabs.assistant;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

class MultiChoiceTextFeedback extends Feedback implements HasViewType {
    String text;

    private MultiChoiceTextFeedback(Builder builder) {
        this.text = getOptionsText(builder.choiceItems);
    }

    @Override
    public int getViewType() {
        return R.id.interactive_assistant_multi_option_feedback_text_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return MultiChoiceTextFeedbackViewHolderBuilder.build();
    }

    private String getOptionsText(List<ChoiceItem> choiceItems) {
        final List<String> selectedOptionText = new ArrayList<>();
        for (ChoiceItem choiceItem : choiceItems) {
            if (choiceItem.isSelected()) {
                selectedOptionText.add(choiceItem.getTextAfter() != null ?
                        choiceItem.getTextAfter() : choiceItem.getText());
            }
        }
        return TextUtils.join(" + ", selectedOptionText);
    }

    public static class Builder {

        private List<ChoiceItem> choiceItems;

        public Builder setChoiceItems(List<ChoiceItem> choiceItems) {
            this.choiceItems = choiceItems;
            return this;
        }

        public MultiChoiceTextFeedback build() {
            return new MultiChoiceTextFeedback(this);
        }
    }
}
