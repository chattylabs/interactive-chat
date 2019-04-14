package chattylabs.assistant;

import chattylabs.conversations.SpeechSynthesizer;

public interface CanSynthesizeSpeech {

    interface OnSynthesized {
        void execute();
    }

    void consumeSynthesizer(SpeechSynthesizer component,
                            OnSynthesized onSynthesized);
}
