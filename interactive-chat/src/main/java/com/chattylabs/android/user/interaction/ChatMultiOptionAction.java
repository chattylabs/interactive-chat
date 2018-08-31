package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ChatMultiOptionAction extends ChatAction {

    private final String mId;
    private final int mOrder;
    private final OnSelected mOnSelected;
    private final boolean mSkipTracking;
    private final boolean mStopFlow;
    private final String[] mContentDescription;
    private final Runnable mOnLoaded;
    private final List<ChatOptionAction> mActions;

    public static class Builder {

        private String mId;
        private OnSelected mOnSelected;
        private boolean mSkipTracking;
        private boolean mStopFlow;
        private int mOrder;
        private String[] mContentDescription;
        private Runnable mOnLoaded;
        private List<ChatOptionAction> mActions = new ArrayList<>();

        public Builder(String id) {
            mId = id;
        }

        public Builder addAction(ChatOptionAction action) {
            mActions.add(action);
            return this;
        }

        public Builder setOnSelected(OnSelected onSelected) {
            mOnSelected = onSelected;
            return this;
        }

        public Builder setSkipTracking(boolean skipTracking) {
            mSkipTracking = skipTracking;
            return this;
        }

        public Builder stopFlow(boolean stopFlow) {
            mStopFlow = stopFlow;
            return this;
        }

        public Builder setOrder(int order) {
            mOrder = order;
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

        public ChatMultiOptionAction build() {
            return new ChatMultiOptionAction(this);
        }
    }

    private ChatMultiOptionAction(Builder builder) {
        this.mId = builder.mId;
        this.mOrder = builder.mOrder;
        this.mOnSelected = builder.mOnSelected;
        this.mSkipTracking = builder.mSkipTracking;
        this.mStopFlow = builder.mStopFlow;
        this.mContentDescription = builder.mContentDescription;
        this.mOnLoaded = builder.mOnLoaded;
        this.mActions = builder.mActions;
    }

    @Override
    public OnSelected onSelected() {
        return this.mOnSelected;
    }

    @Override
    public boolean skipTracking() {
        return this.mSkipTracking;
    }

    @Override
    public boolean stopFlow() {
        return this.mStopFlow;
    }

    @Override
    public int getOrder() {
        return this.mOrder;
    }

    @Override
    public String[] getContentDescriptions() {
        return this.mContentDescription;
    }

    @Override
    public ChatActionViewBuilder getActionViewBuilder() {
        return new ChatMultiOptionActionViewBuilder();
    }

    @Override
    public ChatNode buildActionFeedback() {
        return null;
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
}
