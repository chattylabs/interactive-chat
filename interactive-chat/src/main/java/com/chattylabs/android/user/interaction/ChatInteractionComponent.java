package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;
import com.chattylabs.sdk.android.voice.OnComponentSetup;

public interface ChatInteractionComponent {

    interface IBuild {
        ChatInteractionComponent build();
    }

    interface IVoiceComponent {
        IBuild withVoiceComponent(ConversationalFlowComponent voiceComponent);
    }

    interface IRecyclerView {
        IVoiceComponent withViewComponent(RecyclerView recyclerView);
    }

    class Builder implements IRecyclerView {
        ConversationalFlowComponent voiceComponent;
        RecyclerView recyclerView;

        @Override
        public IVoiceComponent withViewComponent(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return interfaceComponent;
        }

        IVoiceComponent interfaceComponent = voiceComponentArgument -> {
            this.voiceComponent = voiceComponentArgument;
            return this.interfaceBuilder;
        };

        IBuild interfaceBuilder = () -> new ChatInteractionComponentImpl(this);
    }

    void init(ChatNode root);

    void addNode(@NonNull ChatNode node);

    ChatNode getNode(@NonNull String id);

    ChatFlow create();

    void next();

    void selectLastAction();

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
