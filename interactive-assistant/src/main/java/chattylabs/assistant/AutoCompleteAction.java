package chattylabs.assistant;

import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class AutoCompleteAction implements HasId, CanSkipTracking, CanStopFlow,
        HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, Action {

    final String id;
    final float textSize;
    final int textLines;
    final int charCount;
    final List<String> hints;
    final List<TextAction> actions;
    final int order;
    final Runnable onLoaded;
    boolean skipTracking;
    boolean stopFlow;
    AutoCompleteTextView widget;

    public static class Builder {
        private String id;
        private float textSize;
        private int textLines;
        private int charCount;
        private List<String> hints;
        private List<TextAction> actions;
        private int order;
        private Runnable onLoaded;
        private boolean skipTracking;
        private boolean stopFlow;

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

        public Builder addAction(TextAction action) {
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

        public Builder stopFlow(boolean stopFlow) {
            this.stopFlow = stopFlow;
            return this;
        }

        public AutoCompleteAction build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Property \"id\" is required");
            }
            return new AutoCompleteAction(this);
        }
    }

    AutoCompleteAction(Builder builder) {
        this.id = builder.id;
        this.textSize = builder.textSize;
        this.textLines = builder.textLines;
        this.charCount = builder.charCount;
        this.hints = builder.hints;
        this.actions = builder.actions;
        this.order = builder.order;
        this.onLoaded = builder.onLoaded;
        this.skipTracking = builder.skipTracking;
        this.stopFlow = builder.stopFlow;
    }

    void attach(AutoCompleteTextView widget) {
        this.widget = widget;
    }

    @NotNull @Override
    public String getId() {
        return id;
    }

    public String getText() {
        return widget.getText().toString();
    }

    public List<String> getHints() {
        return hints;
    }

    public List<TextAction> getActions() {
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
    public boolean stopFlow() {
        return stopFlow;
    }

    @Override
    public ActionViewBuilder getActionViewBuilder() {
        return AutoCompleteActionViewBuilder.build();
    }

    @Override
    public Node buildActionFeedback() {
        return new TextFeedback.Builder()
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
