package chattylabs.assistant;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

class FeedbackIcon extends Feedback implements HasViewType {
    final int icon;
    final int tintColor;

    public static class Builder {
        private int icon;
        private int tintColor;

        public Builder() {}

        public Builder setIcon(@DrawableRes int icon) {
            this.icon = icon;
            return this;
        }

        public Builder setTintColor(@ColorRes int tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public FeedbackIcon build() {
            return new FeedbackIcon(this);
        }
    }

    private FeedbackIcon(Builder builder) {
        this.icon = builder.icon;
        this.tintColor = builder.tintColor;
    }

    public int getIcon() {
        return icon;
    }

    public int getTintColor() {
        return tintColor;
    }

    @Override
    public int getViewType() {
        return R.id.interactive_assistant_feedback_image_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return FeedbackIconViewHolderBuilder.build();
    }
}
