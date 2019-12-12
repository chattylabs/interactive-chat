package chattylabs.assistant;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.google.android.material.chip.Chip;

import java.util.Objects;

public class ActionChipChoice implements HasId,
        HasContentDescriptions, HasOnLoaded, Action {
    final String id;
    final String text;
    final int icon;
    final int iconTintColor;
    final float textSize;
    final String[] contentDescriptions;
    final int order;
    final Runnable onLoaded;
    boolean isSelected;
    Chip chip;

    void attach(Chip chip) {
        this.chip = chip;
    }

    public static class Builder {
        private String id;
        private int icon;
        private int iconTintColor;
        private String text;
        private float textSize;
        private String[] contentDescriptions;
        private int order;
        private Runnable onLoaded;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setIcon(@DrawableRes int icon) {
            this.icon = icon;
            return this;
        }

        public Builder setIconTintColor(@ColorRes int iconTintColor) {
            this.iconTintColor = iconTintColor;
            return this;
        }

        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder setContentDescriptions(String[] contentDescriptions) {
            this.contentDescriptions = contentDescriptions;
            return this;
        }

        public Builder setOrder(int order) {
            this.order = order;
            return this;
        }

        public Builder setOnLoaded(Runnable afterLoaded) {
            this.onLoaded = afterLoaded;
            return this;
        }

        public ActionChipChoice build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Property \"id\" is required");
            }
            if (text == null || text.length() == 0) {
                throw new NullPointerException("Property \"text\" is required");
            }
            return new ActionChipChoice(this);
        }
    }

    ActionChipChoice(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.icon = builder.icon;
        this.iconTintColor = builder.iconTintColor;
        this.textSize = builder.textSize;
        this.contentDescriptions = builder.contentDescriptions;
        this.order = builder.order;
        this.onLoaded = builder.onLoaded;
    }

    @NonNull @Override
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getIcon() {
        return icon;
    }

    public int getIconTintColor() {
        return iconTintColor;
    }

    public float getTextSize() {
        return textSize;
    }

    @Override
    public String[] getContentDescriptions() {
        if (contentDescriptions != null)
            return contentDescriptions;
        else return new String[]{text};
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public Runnable onLoaded() {
        return onLoaded;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public int compareTo(@NonNull Action o) {
        return Integer.compare(getOrder(), o.getOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionText that = (ActionText) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
