package chattylabs.assistant;

class TextFeedback extends Feedback implements HasViewType {
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

        public TextFeedback build() {
            return new TextFeedback(this);
        }
    }

    TextFeedback(Builder builder) {
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
        return R.id.interactive_assistant_feedback_text_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return TextFeedbackViewHolderBuilder.build();
    }
}
