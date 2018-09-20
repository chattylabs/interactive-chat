package com.chattylabs.android.interactive.chat;

import com.chattylabs.sdk.android.voice.SpeechRecognizerComponent;

public interface CanRecognizeSpeech {

    interface OnRecognized {
        void execute(Action action);
    }

    void consumeRecognizer(SpeechRecognizerComponent component, OnRecognized onRecognized);
}
