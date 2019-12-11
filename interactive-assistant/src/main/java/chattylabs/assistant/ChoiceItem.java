package chattylabs.assistant;

import android.widget.ToggleButton;

import java.util.Objects;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class ChoiceItem implements HasId,
        HasContentDescriptions, HasOnLoaded, Action {
    final String id;
    final String text;
    final String textAfter;
    final float textSize;
    final String[] contentDescriptions;
    final int order;
    final Runnable onLoaded;
    boolean isSelected;
    ToggleButton toggleButton;

    void attach(ToggleButton toggleButton) {
        this.toggleButton = toggleButton;
    }

    public static class Builder {
        private String id;
        private String text;
        private String textAfter;
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

        public Builder setTextAfter(String textAfter) {
            this.textAfter = textAfter;
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

        public ChoiceItem build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Property \"id\" is required");
            }
            if (text == null || text.length() == 0) {
                throw new NullPointerException("Property \"text\" is required");
            }
            return new ChoiceItem(this);
        }
    }

    ChoiceItem(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.textAfter = builder.textAfter;
        this.textSize = builder.textSize;
        this.contentDescriptions = builder.contentDescriptions;
        this.order = builder.order;
        this.onLoaded = builder.onLoaded;
    }

    @NotNull @Override
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTextAfter() {
        return textAfter;
    }

    public float getTextSize() {
        return textSize;
    }

    @Override
    public String[] getContentDescriptions() {
        if (contentDescriptions != null)
            return contentDescriptions;
        else if (text != null)
            return new String[]{text};
        return new String[]{textAfter};
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
        TextAction that = (TextAction) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
