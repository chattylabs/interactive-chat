package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

public class ChatActionOption extends ChatAction {

    private final String mId;
    private final String[] mContentDescription;
    private final String mText;

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
        throw new UnsupportedOperationException("Register OnOptionChangeListener to " +
                "ChatActionMultiOption");
    }

    @Override
    public boolean skipTracking() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean stopFlow() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public ChatNode buildActionFeedback() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable onLoaded() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    @Override
    public int compareTo(@NonNull ChatAction action) {
        return Integer.compare(getOrder(), action.getOrder());
    }
}
