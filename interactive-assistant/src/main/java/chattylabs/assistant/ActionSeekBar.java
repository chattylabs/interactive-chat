package chattylabs.assistant;

import android.widget.SeekBar;

import androidx.annotation.NonNull;

public class ActionSeekBar implements HasId,
                                      HasOnSelected, CanSkipTracking, CanSkipSelected,
                                      HasActionViewBuilder, MustBuildActionFeedback, HasOnLoaded, Action {
    final String id;
    final Runnable onLoaded;
    final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;
    final ActionText confirmationAction;
    final boolean skipTracking;
    final boolean skipSelected;
    final int maxProgress;
    int progress;
    private LazyProvider lazyProgress;

    private ActionSeekBar(Builder builder) {
        this.id                      = builder.id;
        this.onLoaded                = builder.onLoaded;
        this.onSeekBarChangeListener = builder.onSeekBarChangeListener;
        this.confirmationAction      = builder.confirmationAction;
        this.maxProgress             = builder.maxProgress;
        this.progress                = builder.progress;
        this.skipTracking            = builder.skipTracking;
        this.skipSelected            = builder.skipSelected;
    }

    @Override public int getOrder() {
        return 0;
    }

    @Override public boolean skipSelected() {
        return skipSelected;
    }

    @Override public boolean skipTracking() {
        return skipTracking;
    }

    @Override public ActionViewBuilder getActionViewBuilder() {
        return new ActionSeekBarViewBuilder(onSeekBarChangeListener);
    }

    @NonNull @Override public String getId() {
        return this.id;
    }

    public int getProgress() {
        return lazyProgress.get();
    }

    void setLazyProgress(LazyProvider provider) {
        this.lazyProgress = provider;
    }

    @Override public Runnable onLoaded() {
        return this.onLoaded;
    }

    @Override public OnSelected onSelected() {
        return confirmationAction.onSelected();
    }

    @Override public Node buildActionFeedback() {
        return new FeedbackActionText.Builder()
                   .setText(confirmationAction.textAfter.get())
                   .setTextSize(confirmationAction.textSize).build();
    }

    @Override public int compareTo(Action o) {
        return Integer.compare(getOrder(), o.getOrder());
    }

    interface LazyProvider {
        int get();
    }

    public static class Builder {
        private String id;
        private Runnable onLoaded;
        private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;
        private ActionText confirmationAction;
        private int maxProgress;
        private int progress;
        private boolean skipTracking;
        private boolean skipSelected;

        public Builder(@NonNull String id) {
            this.id = id;
        }

        public Builder skipTracking(boolean skipTracking) {
            this.skipTracking = skipTracking;
            return this;
        }

        public Builder skipSelected(boolean skipSelected) {
            this.skipSelected = skipSelected;
            return this;
        }

        public Builder setOnLoaded(Runnable loaded) {
            onLoaded = loaded;
            return this;
        }

        public Builder setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
            this.onSeekBarChangeListener = onSeekBarChangeListener;
            return this;
        }

        public Builder setConfirmationAction(ActionText confirmationAction) {
            this.confirmationAction = confirmationAction;
            return this;
        }

        public Builder setMaxProgress(int maxProgress) {
            this.maxProgress = maxProgress;
            return this;
        }

        public Builder setProgress(int progress) {
            this.progress = progress;
            return this;
        }

        public ActionSeekBar build() {
            if (id == null || id.isEmpty()) {
                throw new NullPointerException("Property \"id\" is required");
            }

            if (confirmationAction == null) {
                throw new NullPointerException("Forgot to set \"confirmationAction\" property?");
            }

            return new ActionSeekBar(this);
        }
    }
}
