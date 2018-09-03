package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ChatActionMultiOption extends ChatAction {

    private final String mId;
    private final String[] mContentDescription;
    private final Runnable mOnLoaded;
    private final List<ChatActionOption> mOptions;
    private final ChatActionText mConfirmationAction;
    private final List<ChatActionOption> mSelectedOptions;
    private final OnOptionChangeListener mOnOptionChangeListener;
    private boolean mSkipTracking;
    private boolean mStopFlow;

    private ChatActionMultiOption(Builder builder) {
        this.mId = builder.mId;
        this.mContentDescription = builder.mContentDescription;
        this.mOnLoaded = builder.mOnLoaded;
        this.mOptions = builder.mOptions;
        this.mConfirmationAction = builder.mConfirmationAction;
        this.mSelectedOptions = new ArrayList<>();
        this.mOnOptionChangeListener = builder.mOnOptionChangeListener;
        this.mSkipTracking = builder.mSkipTracking;
        this.mStopFlow = builder.mStopFlow;
    }

    /**
     * This method is not applicable for {@link ChatActionMultiOption}
     *
     * @return {@code null}
     */
    @Override
    public OnSelected onSelected() {
        return null;
    }

    @Override
    public boolean skipTracking() {
        return mSkipTracking;
    }

    @Override
    public boolean stopFlow() {
        return mStopFlow;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String[] getContentDescriptions() {
        return this.mContentDescription;
    }

    @Override
    public ChatActionViewBuilder getActionViewBuilder() {
        return new ChatActionMultiOptionViewBuilder(
                (option, isSelected) -> {
                    if (isSelected) {
                        mSelectedOptions.add(option);
                    } else {
                        mSelectedOptions.remove(option);
                    }

                    if (mOnOptionChangeListener != null) {
                        mOnOptionChangeListener.onChange(option, isSelected);
                    }
                }
        );
    }

    @Override
    public ChatNode buildActionFeedback() {
        return new ChatActionMultiOptionFeedbackText.Builder()
                .setOptions(mSelectedOptions)
                .build();
    }

    @Override
    public Runnable onLoaded() {
        return this.mOnLoaded;
    }

    @Override
    public String getId() {
        return this.mId;
    }

    @Override
    public int compareTo(@NonNull ChatAction chatAction) {
        return Integer.compare(getOrder(), chatAction.getOrder());
    }

    public List<ChatActionOption> getOptions() {
        return mOptions;
    }

    public ChatAction getConfirmationAction() {
        return mConfirmationAction;
    }

    public static class Builder {

        private String mId;
        private String[] mContentDescription;
        private Runnable mOnLoaded;
        private List<ChatActionOption> mOptions = new ArrayList<>();
        private ChatActionText mConfirmationAction;
        private OnOptionChangeListener mOnOptionChangeListener;
        private boolean mSkipTracking;
        private boolean mStopFlow;

        public Builder(@NonNull String id) {
            mId = id;
        }

        public Builder addOption(ChatActionOption option) {
            mOptions.add(option);
            return this;
        }

        public Builder setSkipTracking(boolean skipTracking) {
            mSkipTracking = skipTracking;
            return this;
        }

        public Builder setStopFlow(boolean stopFlow) {
            mStopFlow = stopFlow;
            return this;
        }

        public Builder setContentDescription(String[] contentDescription) {
            mContentDescription = contentDescription;
            return this;
        }

        public Builder setOnLoaded(Runnable loaded) {
            mOnLoaded = loaded;
            return this;
        }

        public Builder addConfirmationAction(ChatActionText confirmationAction) {
            mConfirmationAction = confirmationAction;
            return this;
        }

        public Builder setOnOptionChangeListener(OnOptionChangeListener changeListener) {
            this.mOnOptionChangeListener = changeListener;
            return this;
        }

        public ChatActionMultiOption build() {
            if (mId == null || mId.isEmpty()) {
                throw new NullPointerException("Property \"id\" is required");
            }

            if (mOptions == null || mOptions.size() < 2) {
                throw new IllegalArgumentException("More than one \"option\" should be present");
            }

            if (mConfirmationAction == null) {
                throw new NullPointerException("Forgot to set \"confirmationAction\"?");
            }

            return new ChatActionMultiOption(this);
        }
    }

    public interface OnOptionChangeListener {

        void onChange(ChatActionOption option, boolean selected);
    }
}
