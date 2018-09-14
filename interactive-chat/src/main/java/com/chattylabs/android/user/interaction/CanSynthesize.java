package com.chattylabs.android.user.interaction;

import com.chattylabs.sdk.android.voice.SpeechSynthesizerComponent;
import com.chattylabs.sdk.android.voice.SynthesizerListener;

public interface CanSynthesize {

    interface OnSynthesized {
        void execute();
    }

    void consumeSynthesizer(SpeechSynthesizerComponent component,
                            OnSynthesized onSynthesized);
}
