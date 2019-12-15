package chattylabs.assistant;

import androidx.annotation.NonNull;

import java.util.Objects;

import chattylabs.conversations.ConversationalFlow;

public class ActionText implements HasId, HasContentDescriptions,
                                   HasOnSelected, CanSkipTracking, CanSkipSelected, CanCheckContentDescriptions,
                                   HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, Action {
    final String id;
    final String text;
    final LazyTextAfter textAfter;
    final float textSize;
    final String[] contentDescriptions;
    final int order;
    final Runnable onLoaded;
    final OnSelected onSelected;
    boolean skipTracking;
    boolean skipSelected;

    public interface LazyTextAfter {
        String get();
    }

    private boolean checkWord(@NonNull String[] patterns, @NonNull String text) {
        for (String pattern : patterns) {
            if (pattern != null && ConversationalFlow.matches(text, pattern)) return true;
        }
        return false;
    }

    @Override
    public int matches(String result) {
        String[] expected = this.getContentDescriptions();
        return (expected != null && expected.length > 0 && checkWord(expected, result))
                ? MATCHED : NOT_MATCHED;
    }

    public static class Builder {
        private String id;
        private String text;
        private LazyTextAfter textAfter;
        private float textSize;
        private String[] contentDescriptions;
        private int order;
        private Runnable onLoaded;
        private OnSelected onSelected;
        private boolean skipTracking;
        private boolean skipSelected;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTextAfter(LazyTextAfter textAfter) {
            this.textAfter = textAfter;
            return this;
        }

        public Builder setTextSize(float textSizeInSp) {
            this.textSize = textSizeInSp;
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

        public Builder setOnSelected(OnSelected onSelected) {
            this.onSelected = onSelected;
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

        public ActionText build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Property \"id\" is required");
            }
            if (text == null || text.length() == 0) {
                throw new NullPointerException("Property \"text\" is required");
            }
            return new ActionText(this);
        }
    }

    ActionText(Builder builder) {
        this.id                  = builder.id;
        this.text                = builder.text;
        this.textAfter           = builder.textAfter;
        this.textSize            = builder.textSize;
        this.contentDescriptions = builder.contentDescriptions;
        this.order               = builder.order;
        this.onLoaded            = builder.onLoaded;
        this.onSelected          = builder.onSelected;
        this.skipTracking        = builder.skipTracking;
        this.skipSelected        = builder.skipSelected;
    }

    @NonNull @Override
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LazyTextAfter getTextAfter() {
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
            return new String[]{getText()};
        return new String[]{getTextAfter().get()};
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
    public OnSelected onSelected() {
        return onSelected;
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
        return ActionTextViewBuilder.build();
    }

    @Override
    public Node buildActionFeedback() {
        return new FeedbackText.Builder()
                .setText(textAfter != null ? getTextAfter().get() : text)
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
        ActionText that = (ActionText) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
