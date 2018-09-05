package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public class ChatActionOption extends ChatAction {

    private final String mId;
    private final String[] mContentDescription;
    private final String mText;
    private boolean mIsSelected;

    public static class Builder {

        private final String mId;
        private String[] mContentDescription;
        private String mText;

        public Builder(String id) {
            mId = id;
        }

        public Builder setText(String text) {
            mText = text;
            return this;
        }

        public Builder setContentDescription(String[] contentDescription) {
            mContentDescription = contentDescription;
            return this;
        }

        public ChatActionOption build() {
            if (mId == null || mId.isEmpty()) {
                throw new NullPointerException("provide \"id\" for the option.");
            }
            if (mText == null || mText.isEmpty()) {
                throw new NullPointerException("option displayable text should not null");
            }
            return new ChatActionOption(this);
        }
    }

    ChatActionOption(Builder builder) {
        this.mId = builder.mId;
        this.mContentDescription = builder.mContentDescription;
        this.mText = builder.mText;
    }

    @Override
    public OnSelected onSelected() {
        throw new IllegalStateException("Register OnOptionChangeListener to " +
                "ChatActionMultiOption");
    }

    @Override
    public boolean skipTracking() {
        throw new IllegalStateException();
    }

    @Override
    public boolean stopFlow() {
        throw new IllegalStateException();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String[] getContentDescriptions() {
        return mContentDescription;
    }

    @Override
    public ChatActionViewBuilder getActionViewBuilder() {
        throw new IllegalStateException();
    }

    @Override
    public ChatNode buildActionFeedback() {
        throw new IllegalStateException();
    }

    @Override
    public Runnable onLoaded() {
        throw new IllegalStateException();
    }

    @Override
    public String getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    @Override
    public int compareTo(@NonNull ChatAction action) {
        return Integer.compare(getOrder(), action.getOrder());
    }
}
