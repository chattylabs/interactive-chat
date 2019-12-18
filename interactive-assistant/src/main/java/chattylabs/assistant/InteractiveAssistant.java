package chattylabs.assistant;

import android.Manifest;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.provider.FontRequest;
import androidx.core.text.HtmlCompat;
import androidx.emoji.text.EmojiCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

import chattylabs.conversations.ConversationalFlow;
import chattylabs.conversations.RecognizerListener;
import chattylabs.conversations.SynthesizerListener;

public interface InteractiveAssistant {

    String INTERACTIVE_CHAT = "interactive_chat";

    static Spanned formatHTML(CharSequence text) {
        Spanned span;
        if (text instanceof SpannableString) {
            span = ((SpannableString) text);
        } else {
            span = HtmlCompat.fromHtml(text.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT);
        }
        return span;
    }

    static CharSequence processEmoji(CharSequence text) {
        try {
            return EmojiCompat.get().process(text);
        } catch (Exception ignored){
            return text;
        }
    }

    void showVolumeControls();

    interface IOptional {
        IOptional withVoiceComponent(ConversationalFlow voiceComponent);
        IOptional withFontRequest(FontRequest fontRequest);
        IOptional withOnDoneListener(Runnable callback);
        IOptional withLastStateEnabled(boolean enable);
        InteractiveAssistant build();
    }

    class Builder {
        ConversationalFlow voiceComponent;
        RecyclerView recyclerView;
        FontRequest fontRequest;
        boolean withLastStateEnabled;
        Runnable doneListener;

        public IOptional withViewComponent(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return combine;
        }

        IOptional combine = new IOptional() {

            @Override
            public IOptional withVoiceComponent(ConversationalFlow voiceComponent) {
                Builder.this.voiceComponent = voiceComponent;
                return this;
            }

            @Override
            public IOptional withFontRequest(FontRequest fontRequest) {
                Builder.this.fontRequest = fontRequest;
                return null;
            }

            @Override
            public IOptional withLastStateEnabled(boolean enable) {
                Builder.this.withLastStateEnabled = enable;
                return this;
            }

            @Override
            public IOptional withOnDoneListener(Runnable callback) {
                Builder.this.doneListener = callback;
                return this;
            }

            @Override
            public InteractiveAssistant build() {
                return new InteractiveAssistantImpl(Builder.this);
            }
        };
    }

    void addNode(@NonNull Node node);

    Node getNode(@NonNull String id);

    Set<String> getVisitedNodes();

    Flow prepare();

    void next();

    void next(Node node);

    void selectLastVisitedAction();

    void enableSpeechSynthesizer(boolean enable);

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    void enableSpeechRecognizer(boolean enable);

    boolean isSpeechSynthesizerEnabled();

    boolean isSpeechRecognizerEnabled();

    void setupSpeech(OnSpeechStatusChecked listener);

    void setupSpeech(RecognizerListener.OnStatusChecked listener);

    void setupSpeech(SynthesizerListener.OnStatusChecked listener);

    void loadSynthesizerInstallation(SynthesizerListener.OnStatusChecked listener);

    void release();

    void pause();

    void resume();

    void setCurrentNode(Node node);

    void removeLastState();

    void showLoading();

    void hideLoading();
}
