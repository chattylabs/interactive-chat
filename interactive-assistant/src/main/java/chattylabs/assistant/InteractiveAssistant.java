package chattylabs.assistant;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import chattylabs.conversations.ConversationalFlow;
import chattylabs.conversations.RecognizerListener;
import chattylabs.conversations.SynthesizerListener;

public interface InteractiveAssistant {

    static Spanned makeText(CharSequence text) {
        Spanned span;
        if (text instanceof SpannableString) {
            span = ((SpannableString) text);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                span = Html.fromHtml(text.toString(), Html.FROM_HTML_MODE_COMPACT);
            } else {
                span = Html.fromHtml(text.toString());
            }
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

    void selectLastVisitedAction();

    void enableSpeechSynthesizer(Context context, boolean enable);

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    void enableSpeechRecognizer(Context context, boolean enable);

    boolean isSpeechSynthesizerEnabled();

    boolean isSpeechRecognizerEnabled();

    void next(@NonNull Node node);

    void setupSpeech(Context context, OnSpeechStatusChecked listener);

    void setupSpeech(Context context, RecognizerListener.OnStatusChecked listener);

    void setupSpeech(Context context, SynthesizerListener.OnStatusChecked listener);

    void release();

    void pause();

    void resume();

    void removeLastState();

    void showLoading();

    void hideLoading();
}
