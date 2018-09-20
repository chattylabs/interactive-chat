package com.chattylabs.android.interactive.chat;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

class MultiOptionFeedbackText extends Feedback implements HasViewType {
    String text;

    private MultiOptionFeedbackText(Builder builder) {
        this.text = getOptionsText(builder.options);
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_multi_option_feedback_text_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return MultiOptionFeedbackTextViewHolderBuilder.build();
    }

    private String getOptionsText(List<Option> options) {
        final List<String> selectedOptionText = new ArrayList<>();
        for (Option option : options) {
            if (option.isSelected()) {
                selectedOptionText.add(option.getTextAfter() != null ?
                        option.getTextAfter() : option.getText());
            }
        }
        return TextUtils.join(" + ", selectedOptionText);
    }

    public static class Builder {

        private List<Option> options;

        public Builder setOptions(List<Option> options) {
            this.options = options;
            return this;
        }

        public MultiOptionFeedbackText build() {
            return new MultiOptionFeedbackText(this);
        }
    }
}
