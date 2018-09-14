package com.chattylabs.android.user.interaction;

import com.chattylabs.sdk.android.voice.SpeechSynthesizerComponent;
import com.chattylabs.sdk.android.voice.SynthesizerListener;

public interface CanSynthesize {

    boolean isSynthesizerConsumed(HasText item,
                                  SpeechSynthesizerComponent component,
                                  SynthesizerListener.OnDone onDone);
}
