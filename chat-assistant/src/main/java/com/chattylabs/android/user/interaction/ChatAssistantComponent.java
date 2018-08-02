package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.sdk.android.voice.VoiceInteractionComponent;

public interface ChatAssistantComponent {

    interface OnError {
        void execute(Integer errorCode);
    }

    interface OnSuccess {
        void execute(Integer successCode);
    }

    interface IBuild {
        ChatAssistantComponent build();
    }

    interface IVoiceInteractionComponent {
        IBuild withVoiceComponent(VoiceInteractionComponent voiceComponent);
    }

    interface IRecyclerView {
        IVoiceInteractionComponent withViewComponent(RecyclerView recyclerView);
    }

    class Builder implements IRecyclerView {
        VoiceInteractionComponent voiceInteractionComponent;
        RecyclerView recyclerView;

        @Override
        public IVoiceInteractionComponent withViewComponent(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return interfaceComponent;
        }

        IVoiceInteractionComponent interfaceComponent = voiceComponent -> {
            voiceInteractionComponent = voiceComponent;
            return this.interfaceBuilder;
        };

        IBuild interfaceBuilder = () -> new ChatAssistantComponentImpl(this);
    }

    void initialize(ChatNode rootNode);

    void enableVoiceInteraction(boolean enable);

    void addNode(@NonNull ChatNode node);

    ChatNode getNode(@NonNull String id);

    ChatFlow create();

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
    ChatNode getNext();

    /**
     * Will be removed
     */
    ChatNode getNode();

    /**
     * Will be removed
     */
    void setNode(ChatNode node);

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
    void addLast(ChatMessage build);

    /**
     * Will be removed
     */
    void removeLast();

    /**
     * Will be removed
     */
    void setLastVisitedNode(ChatNode node);

    /**
     * Will be removed
     */
    String getLastVisitedNode(ChatNode node);
}
