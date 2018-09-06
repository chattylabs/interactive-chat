package com.chattylabs.android.user.interaction;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

class ChatActionMultiOptionFeedbackText extends ChatActionFeedback implements HasViewType {
    String text;
    final float textSize;

    private ChatActionMultiOptionFeedbackText(Builder builder) {
        this.textSize = builder.textSize;
        this.text = getOptionsText(builder.options);
    }

    public float getTextSize() {
        return textSize;
    }

    @Override
    public int getViewType() {
        return R.id.interactive_chat_action_feedback_text_view_type;
    }

    @Override
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatActionMultiOptionFeedbackTextViewHolderBuilder.build();
    }

    private String getOptionsText(List<ChatActionOption> options) {
        final List<String> selectedOptionText = new ArrayList<>();
        for (ChatActionOption option : options) {
            if (option.isSelected()) {
                selectedOptionText.add(option.getTextAfter() != null ?
                        option.getTextAfter() : option.getText());
            }
        }
        return TextUtils.join(", ", selectedOptionText);
    }

    public static class Builder {

        private List<ChatActionOption> options;
        private float textSize;

        public Builder setOptions(List<ChatActionOption> options) {
            this.options = options;
            return this;
        }

        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public ChatActionMultiOptionFeedbackText build() {
            return new ChatActionMultiOptionFeedbackText(this);
        }
    }
}
