package chattylabs.assistant;

public interface OnSpeechStatusChecked {
    void onStatusChecked(int synthesizerStatus, int recognizerStatus);
}
