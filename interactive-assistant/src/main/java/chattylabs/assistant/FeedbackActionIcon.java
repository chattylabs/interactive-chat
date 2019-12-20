package chattylabs.assistant;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;

class FeedbackActionIcon extends Feedback implements HasViewLayout {
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

        public FeedbackActionIcon build() {
            return new FeedbackActionIcon(this);
        }
    }

    private FeedbackActionIcon(Builder builder) {
        this.icon = builder.icon;
        this.tintColor = builder.tintColor;
    }

    public int getIcon() {
        return icon;
    }

    public int getTintColor() {
        return tintColor;
    }

    @Override @LayoutRes
    public int getViewLayout() {
        return R.layout.item_interactive_assistant_action_icon_selected;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return FeedbackActionIconViewHolderBuilder.build();
    }
}
