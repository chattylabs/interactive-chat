package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.sdk.android.voice.ComponentSetup;
import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;
import com.chattylabs.sdk.android.voice.SynthesizerListener;

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

    void initialize(ChatNode root);

    void enableSpeech(boolean enable);

    void addNode(@NonNull ChatNode node);

    ChatNode getNode(@NonNull String id);

    ChatFlow create();

    void next();

    void selectLastAction();

    boolean isSpeechReady();

    void prepareSpeech(Context context, ComponentSetup onPrepared);

    void release();

    void pause();

    void resume();

    void showLoading();

    void hideLoading();
}
