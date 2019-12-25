package chattylabs.assistant;

import androidx.annotation.NonNull;

import chattylabs.conversations.SpeechRecognizer;

public interface CanCheckContentDescriptions {

    int MATCHED = 1;
    int NOT_MATCHED = 2;
    int REPEAT = 3;

    int matches(@NonNull SpeechRecognizer speechRecognizer, @NonNull String result);
}
