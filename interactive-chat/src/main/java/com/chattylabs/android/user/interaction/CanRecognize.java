package com.chattylabs.android.user.interaction;

import com.chattylabs.sdk.android.voice.SpeechRecognizerComponent;

public interface CanRecognize {

    interface OnRecognized {
        void execute(ChatAction action);
    }

    void consumeRecognizer(SpeechRecognizerComponent component, OnRecognized onRecognized);
}
