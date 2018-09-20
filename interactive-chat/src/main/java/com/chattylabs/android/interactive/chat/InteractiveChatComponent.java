package com.chattylabs.android.interactive.chat;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;
import com.chattylabs.sdk.android.voice.OnComponentSetup;

import java.util.Set;

public interface InteractiveChatComponent {

    interface IOptional {
        IOptional withVoiceComponent(ConversationalFlowComponent voiceComponent);
        IOptional withOnDoneListener(Runnable callback);
        IOptional withLastStateEnabled(boolean enable);
        InteractiveChatComponent build();
    }

    class Builder {
        ConversationalFlowComponent voiceComponent;
        RecyclerView recyclerView;
        boolean withLastStateEnabled;
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
            public IOptional withLastStateEnabled(boolean enable) {
                Builder.this.withLastStateEnabled = enable;
                return this;
            }

            @Override
            public IOptional withOnDoneListener(Runnable callback) {
                Builder.this.doneListener = callback;
                return this;
            }

            @Override
            public InteractiveChatComponent build() {
                return new InteractiveChatComponentImpl(Builder.this);
            }
        };
    }

    void addNode(@NonNull InteractiveChatNode node);

    InteractiveChatNode getNode(@NonNull String id);

    Set<String> getVisitedNodes();

    InteractiveChatFlow prepare();

    void next();

    void selectLastVisitedAction();

    void enableSpeechSynthesizer(Context context, boolean enable);

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    void enableSpeechRecognizer(Context context, boolean enable);

    boolean isSpeechSynthesizerEnabled();

    boolean isSpeechRecognizerEnabled();

    void setupSpeech(Context context, OnComponentSetup onPrepared);

    void release();

    void pause();

    void resume();

    void removeLastState();

    void showLoading();

    void hideLoading();
}