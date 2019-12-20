package chattylabs.assistant;

import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AutoCompleteAction implements HasId, CanSkipTracking, CanSkipSelected,
                                           HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, Action {

    final String id;
    final float textSize;
    final int textLines;
    final int charCount;
    final List<String> hints;
    final List<ActionText> actions;
    final int order;
    final Runnable onLoaded;
    boolean skipTracking;
    boolean skipSelected;
    AutoCompleteTextView widget;

    public static class Builder {
        private String id;
        private float textSize;
        private int textLines;
        private int charCount;
        private List<String> hints;
        private List<ActionText> actions;
        private int order;
        private Runnable onLoaded;
        private boolean skipTracking;
        private boolean skipSelected;

        public Builder(String id) {
            this.id = id;
            hints = new ArrayList<>();
            actions = new ArrayList<>();
        }

        public Builder setTextSize(float textSizeInSp) {
            this.textSize = textSizeInSp;
            return this;
        }

        public Builder setTextLines(int textLines) {
            this.textLines = textLines;
            return this;
        }

        public Builder setCharCount(int charCount) {
            this.charCount = charCount;
            return this;
        }

        public Builder addHints(List<String> hints) {
            this.hints.addAll(hints);
            return this;
        }

        public Builder addAction(ActionText action) {
            this.actions.add(action);
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

        public Builder skipTracking(boolean skipTracking) {
            this.skipTracking = skipTracking;
            return this;
        }

        public Builder skipSelected(boolean skipSelected) {
            this.skipSelected = skipSelected;
            return this;
        }

        public AutoCompleteAction build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Property \"id\" is required");
            }
            return new AutoCompleteAction(this);
        }
    }

    private AutoCompleteAction(Builder builder) {
        this.id           = builder.id;
        this.textSize     = builder.textSize;
        this.textLines    = builder.textLines;
        this.charCount    = builder.charCount;
        this.hints        = builder.hints;
        this.actions      = builder.actions;
        this.order        = builder.order;
        this.onLoaded     = builder.onLoaded;
        this.skipTracking = builder.skipTracking;
        this.skipSelected = builder.skipSelected;
    }

    void attach(AutoCompleteTextView widget) {
        this.widget = widget;
    }

    @NonNull @Override
    public String getId() {
        return id;
    }

    public String getText() {
        return widget.getText().toString();
    }

    public List<String> getHints() {
        return hints;
    }

    public List<ActionText> getActions() {
        return actions;
    }

    public float getTextSize() {
        return textSize;
    }

    public int getTextLines() {
        return textLines;
    }

    public int getCharCount() {
        return charCount;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public Runnable onLoaded() {
        return onLoaded;
    }

    @Override
    public boolean skipTracking() {
        return skipTracking;
    }

    @Override
    public boolean skipSelected() {
        return skipSelected;
    }

    @Override
    public ActionViewBuilder getActionViewBuilder() {
        return AutoCompleteActionViewBuilder.build();
    }

    @Override
    public Node buildActionFeedback() {
        return new FeedbackActionText.Builder()
                .setText(getText())
                .setTextSize(textSize).build();
    }

    @Override
    public int compareTo(@NonNull Action o) {
        return Integer.compare(getOrder(), o.getOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoCompleteAction that = (AutoCompleteAction) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
