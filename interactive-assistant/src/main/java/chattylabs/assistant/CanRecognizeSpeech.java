package chattylabs.assistant;

import chattylabs.conversations.SpeechRecognizer;

public interface CanRecognizeSpeech {

    interface OnRecognized {
        void execute(Action action);
    }

    void consumeRecognizer(SpeechRecognizer speechRecognizer, OnRecognized onRecognized);
}
