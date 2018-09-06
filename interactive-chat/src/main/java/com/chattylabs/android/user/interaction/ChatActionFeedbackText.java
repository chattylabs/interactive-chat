package com.chattylabs.android.user.interaction;

class ChatActionFeedbackText extends ChatActionFeedback implements HasViewType {
    String text;
    final float textSize;

    public static class Builder {
        private String text;
        private float textSize;

        public Builder() {}

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public ChatActionFeedbackText build() {
            return new ChatActionFeedbackText(this);
        }
    }

    ChatActionFeedbackText(Builder builder) {
        this.text = builder.text;
        this.textSize = builder.textSize;
    }

    public String getText() {
        return text;
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
        return ChatActionFeedbackTextViewHolderBuilder.build();
    }
}
