package chattylabs.assistant;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.util.Objects;

import chattylabs.conversations.ConversationalFlow;

public class ActionIcon implements HasId, HasContentDescriptions,
                                   HasOnSelected, CanSkipTracking, CanSkipSelected, CanCheckContentDescriptions,
                                   HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, Action {
    final String id;
    final int icon;
    final int iconAfter;
    final String text;
    final int tintColor;
    final String[] contentDescriptions;
    final int order;
    final Runnable onLoaded;
    final Action.OnSelected onSelected;
    boolean skipTracking;
    boolean skipSelected;

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
        private int icon;
        private int iconAfter;
        private String text;
        private int tintColor;
        private String[] contentDescriptions;
        private int order;
        private Runnable onLoaded;
        private OnSelected onSelected;
        private boolean skipTracking;
        private boolean skipSelected;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setIcon(@DrawableRes int icon) {
            this.icon = icon;
            return this;
        }

        public Builder setIconAfter(@DrawableRes int iconAfter) {
            this.iconAfter = iconAfter;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTintColor(@ColorRes int tintColor) {
            this.tintColor = tintColor;
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

        public ActionIcon build() {
            if (id == null || id.length() == 0) {
                throw new NullPointerException("Property \"id\" is required");
            }
            if (icon == 0) {
                throw new NullPointerException("Property \"icon\" is required");
            }
            if (text == null || text.length() == 0) {
                throw new NullPointerException("Property \"text\" is required");
            }
            return new ActionIcon(this);
        }
    }

    private ActionIcon(Builder builder) {
        this.id                  = builder.id;
        this.icon                = builder.icon;
        this.iconAfter           = builder.iconAfter;
        this.text                = builder.text;
        this.tintColor           = builder.tintColor;
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

    public int getIcon() {
        return icon;
    }

    public int getIconAfter() {
        return iconAfter;
    }

    public String getText() {
        return text;
    }

    public int getTintColor() {
        return tintColor;
    }

    @Override
    public String[] getContentDescriptions() {
        return contentDescriptions;
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
    public Node buildActionFeedback() {
        return new FeedbackActionIcon.Builder()
                .setIcon(iconAfter != 0 ? iconAfter : icon)
                .setTintColor(tintColor).build();
    }

    @Override
    public ActionViewBuilder getActionViewBuilder() {
        return ActionIconViewBuilder.build();
    }

    @Override
    public int compareTo(@NonNull Action o) {
        return Integer.compare(getOrder(), o.getOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionIcon that = (ActionIcon) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
