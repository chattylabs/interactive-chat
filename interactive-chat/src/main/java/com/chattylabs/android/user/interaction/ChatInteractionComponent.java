package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;
import com.chattylabs.sdk.android.voice.OnComponentSetup;

import java.util.Set;

public interface ChatInteractionComponent {

    interface IBuild {
        ChatInteractionComponent build();
    }

    interface IVoiceComponent {
        IBuild withVoiceComponent(ConversationalFlowComponent voiceComponent);
    }

    interface IRecyclerView {
        ICombine withViewComponent(RecyclerView recyclerView);
    }

    interface ICombine extends IVoiceComponent, IBuild {}

    class Builder implements IRecyclerView {
        ConversationalFlowComponent voiceComponent;
        RecyclerView recyclerView;

        @Override
        public ICombine withViewComponent(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return combine;
        }

        ICombine combine = new ICombine() {
            @Override
            public ChatInteractionComponent build() {
                return new ChatInteractionComponentImpl(Builder.this);
            }

            @Override
            public IBuild withVoiceComponent(ConversationalFlowComponent voiceComponent) {
                Builder.this.voiceComponent = voiceComponent;
                return this;
            }
        };
    }

    void init(ChatNode root);

    void addNode(@NonNull ChatNode node);

    ChatNode getNode(@NonNull String id);

    Set<String> getVisitedNodes();

    ChatFlow create();

    void next();

    void selectLastVisitedAction();

    void enableSpeechSynthesizer(boolean enable);

    void enableSpeechRecognizer(boolean enable);

    void setupSpeech(Context context, OnComponentSetup onPrepared);

    void release();

    void pause();

    void resume();

    void reset();

    void onDone(Runnable callback);

    void showLoading();

    void hideLoading();
}
