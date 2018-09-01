package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatMultiOptionAction extends ChatAction {

    private final String mId;
    private final String[] mContentDescription;
    private final Runnable mOnLoaded;
    private final List<ChatOptionAction> mActions;
    private final ChatActionText mConfirmationAction;
    private final List<ChatOptionAction> mSelectedOptions;

    private ChatMultiOptionAction(Builder builder) {
        this.mId = builder.mId;
        this.mContentDescription = builder.mContentDescription;
        this.mOnLoaded = builder.mOnLoaded;
        this.mActions = builder.mActions;
        this.mConfirmationAction = builder.mConfirmationAction;
        this.mSelectedOptions = new ArrayList<>();
    }

    @Override
    public OnSelected onSelected() {
        return null;
    }

    @Override
    public boolean skipTracking() {
        return false;
    }

    @Override
    public boolean stopFlow() {
        return false;
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
        return new ChatMultiOptionActionViewBuilder(
                (action, isSelected) -> {
                    if (isSelected) {
                        mSelectedOptions.add(action);
                    } else {
                        mSelectedOptions.remove(action);
                    }
                }
        );
    }

    @Override
    public ChatNode buildActionFeedback() {
        return new ChatActionFeedbackText.Builder()
                .setText(getSelectedOptions())
                .build();
    }

    private String getSelectedOptions() {
        final List<String> selectedOptionText = new ArrayList<>();
        for (ChatOptionAction option : mSelectedOptions) {
            selectedOptionText.add(option.getText());
        }
        return TextUtils.join("  ", selectedOptionText);
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

    public List<ChatOptionAction> getActions() {
        return mActions;
    }

    public ChatAction getConfirmationAction() {
        return mConfirmationAction;
    }

    public static class Builder {

        private String mId;
        private String[] mContentDescription;
        private Runnable mOnLoaded;
        private List<ChatOptionAction> mActions = new ArrayList<>();
        private ChatActionText mConfirmationAction;

        public Builder(String id) {
            mId = id;
        }

        public Builder addAction(ChatOptionAction action) {
            mActions.add(action);
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

        public ChatMultiOptionAction build() {
            return new ChatMultiOptionAction(this);
        }
    }
}
