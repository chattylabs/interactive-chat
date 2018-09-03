package com.chattylabs.android.user.interaction;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatActionMultiOptionFeedbackText extends ChatActionFeedbackText {

    private final List<ChatActionOption> options;

    private ChatActionMultiOptionFeedbackText(Builder builder) {
        super(builder);
        this.options = builder.options;
        text = getOptionsText();
    }

    private String getOptionsText() {
        final List<String> selectedOptionText = new ArrayList<>();
        for (ChatActionOption option : options) {
            selectedOptionText.add(option.getText());
        }
        return TextUtils.join("  ", selectedOptionText);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public static class Builder extends ChatActionFeedbackText.Builder {

        private List<ChatActionOption> options;

        public Builder() {
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
