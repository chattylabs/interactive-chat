package chattylabs.assistant;

import androidx.annotation.LayoutRes;

class FeedbackActionText extends Feedback implements HasViewLayout {
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

        public FeedbackActionText build() {
            return new FeedbackActionText(this);
        }
    }

    private FeedbackActionText(Builder builder) {
        this.text = builder.text;
        this.textSize = builder.textSize;
    }

    public String getText() {
        return text;
    }

    public float getTextSize() {
        return textSize;
    }

    @Override @LayoutRes
    public int getViewLayout() {
        return R.layout.item_interactive_assistant_action_text_selected;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return FeedbackActionTextViewHolderBuilder.build();
    }
}
