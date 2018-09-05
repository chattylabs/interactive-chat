package com.chattylabs.android.user.interaction;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

class ChatActionMultiOptionFeedbackText extends ChatActionFeedbackText {

    private ChatActionMultiOptionFeedbackText(Builder builder) {
        super(builder);
        this.text = getOptionsText(builder.options);
    }

    private String getOptionsText(List<ChatActionOption> options) {
        final List<String> selectedOptionText = new ArrayList<>();
        for (ChatActionOption option : options) {
            if (option.isSelected()) {
                selectedOptionText.add(option.getText());
            }
        }
        return TextUtils.join(", ", selectedOptionText);
    }

    public static class Builder extends ChatActionFeedbackText.Builder {

        private List<ChatActionOption> options;

        @Override @Deprecated
        public ChatActionFeedbackText.Builder setText(String text) {
            throw new UnsupportedOperationException();
        }

        public Builder setOptions(List<ChatActionOption> options) {
            this.options = options;
            return this;
        }

        public ChatActionMultiOptionFeedbackText build() {
            return new ChatActionMultiOptionFeedbackText(this);
        }
    }
}
