package com.chattylabs.android.user.interaction;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;
import com.chattylabs.sdk.android.voice.OnComponentSetup;

import java.util.Set;

public interface ChatInteractionComponent {

    interface IOptional {
        IOptional withVoiceComponent(ConversationalFlowComponent voiceComponent);
        IOptional withDoneListener(Runnable callback);
        IOptional withLastState(boolean enable);
        ChatInteractionComponent build();
    }

    class Builder {
        ConversationalFlowComponent voiceComponent;
        RecyclerView recyclerView;
        boolean withLastState;
        Runnable doneListener;

        public IOptional withViewComponent(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return combine;
        }

        IOptional combine = new IOptional() {

            @Override
            public IOptional withVoiceComponent(ConversationalFlowComponent voiceComponent) {
                Builder.this.voiceComponent = voiceComponent;
                return this;
            }

            @Override
            public IOptional withLastState(boolean enable) {
                Builder.this.withLastState = enable;
                return this;
            }

            @Override
            public IOptional withDoneListener(Runnable callback) {
                Builder.this.doneListener = callback;
                return this;
            }

            @Override
            public ChatInteractionComponent build() {
                return new ChatInteractionComponentImpl(Builder.this);
            }
        };
    }

    void addNode(@NonNull ChatNode node);

    ChatNode getNode(@NonNull String id);

    Set<String> getVisitedNodes();

    ChatFlow prepare();

    void next();

    void selectLastVisitedAction();

    void enableSpeechSynthesizer(Context context, boolean enable);

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    void enableSpeechRecognizer(Context context, boolean enable);

    void setupSpeech(Context context, OnComponentSetup onPrepared);

    void release();

    void pause();

    void resume();

    void removeLastState();

    void showLoading();

    void hideLoading();
}
