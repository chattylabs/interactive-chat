package com.chattylabs.android.interactive.chat;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

class MultiChoiceTextFeedback extends Feedback implements HasViewType {
    String text;

    private MultiChoiceTextFeedback(Builder builder) {
        this.text = getOptionsText(builder.choices);
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_multi_option_feedback_text_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return MultiChoiceTextFeedbackViewHolderBuilder.build();
    }

    private String getOptionsText(List<Choice> choices) {
        final List<String> selectedOptionText = new ArrayList<>();
        for (Choice choice : choices) {
            if (choice.isSelected()) {
                selectedOptionText.add(choice.getTextAfter() != null ?
                        choice.getTextAfter() : choice.getText());
            }
        }
        return TextUtils.join(" + ", selectedOptionText);
    }

    public static class Builder {

        private List<Choice> choices;

        public Builder setChoices(List<Choice> choices) {
            this.choices = choices;
            return this;
        }

        public MultiChoiceTextFeedback build() {
            return new MultiChoiceTextFeedback(this);
        }
    }
}
