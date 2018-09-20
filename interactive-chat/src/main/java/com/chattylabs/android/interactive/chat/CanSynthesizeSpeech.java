package com.chattylabs.android.interactive.chat;

import com.chattylabs.sdk.android.voice.SpeechSynthesizerComponent;
import com.chattylabs.sdk.android.voice.SynthesizerListener;

public interface CanSynthesizeSpeech {

    interface OnSynthesized {
        void execute();
    }

    void consumeSynthesizer(SpeechSynthesizerComponent component,
                            OnSynthesized onSynthesized);
}
