package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.sdk.android.voice.VoiceInteractionComponent;

public interface UserAssistantComponent {

    interface OnError {
        void execute(Integer errorCode);
    }

    interface OnSuccess {
        void execute(Integer successCode);
    }

    class Builder {
        VoiceInteractionComponent component;
        RecyclerView recyclerView;

        public Builder withRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        public Builder withVoiceInteraction(VoiceInteractionComponent component) {
            this.component = component;
            return this;
        }

        public UserAssistantComponentImpl build() {
            return new UserAssistantComponentImpl(this);
        }
    }

    void initialize(Node initialNode);

    void enableVoiceInteraction(boolean enable);

    void addNode(@NonNull Node node);

    Flow create();

    void next();

    boolean isVoiceInteractionInitialized();

    void prepareForVoiceInteraction(Context context, OnSuccess onSuccess, OnError onError);

    void release();

    void pause();

    void resume();

    void showLoading();

    void hideLoading();

    /**
     * Will be removed
     */
    Node getNext();

    /**
     * Will be removed
     */
    Node getNode();

    /**
     * Will be removed
     */
    void setNode(Node node);

    /**
     * Will be removed
     */
    void trackLastAction();

    /**
     * Will be removed
     */
    void resumeLastAction();

    /**
     * Will be removed
     */
    void addLast(Message build);

    /**
     * Will be removed
     */
    void removeLast();

    /**
     * Will be removed
     */
    void setLastVisitedNode(Node node);

    /**
     * Will be removed
     */
    String getLastVisitedNode(Node node);
}
