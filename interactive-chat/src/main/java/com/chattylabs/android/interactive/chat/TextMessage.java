package com.chattylabs.android.interactive.chat;

import android.support.annotation.ColorRes;

import com.chattylabs.sdk.android.voice.SpeechSynthesizerComponent;
import com.chattylabs.sdk.android.voice.SynthesizerListener;

import java.util.Objects;

public class TextMessage implements InteractiveChatNode, HasId, HasOnLoaded, HasText, HasViewType,
        CanSynthesizeSpeech {

    final String id;
    final String text;
    final float textSize;
    final int tintColor;
    final boolean aloud;
    final Runnable onLoaded;
    final boolean treatedAsFirst;

    @Override
    public void consumeSynthesizer(SpeechSynthesizerComponent component,
                                   OnSynthesized onSynthesized) {
        component.playText(this.getText(),
                (SynthesizerListener.OnDone) utteranceId -> onSynthesized.execute());
    }

    public static class Builder {
        private String id;
        private String text;
        private float textSize;
        private int tintColor;
        private boolean aloud;
        private Runnable onLoaded;
        private boolean treatedAsFirst;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTextSize(float textSizeInSp) {
            this.textSize = textSizeInSp;
            return this;
        }

        public Builder setTintColor(@ColorRes int resId) {
            this.tintColor = resId;
            return this;
        }

        public Builder setAloud(boolean aloud) {
            this.aloud = aloud;
            return this;
        }

        public Builder setOnLoaded(Runnable afterLoaded) {
            this.onLoaded = afterLoaded;
            return this;
        }

        public Builder setTreatedAsFirst(boolean treatedAsFirst) {
            this.treatedAsFirst = treatedAsFirst;
            return this;
        }

        public TextMessage build() {
            if (text == null || text.length() == 0) {
                throw new NullPointerException("Property \"text\" is required");
            }
            return new TextMessage(this);
        }
    }

    private TextMessage(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.tintColor = builder.tintColor;
        this.aloud = builder.aloud;
        this.onLoaded = builder.onLoaded;
        this.textSize = builder.textSize;
        this.treatedAsFirst = builder.treatedAsFirst;
    }

    @Override
    public int getViewType() {
        return treatedAsFirst ?
                R.id.interactive_chat_message_first_view_type :
                R.id.interactive_chat_message_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return TextMessageViewHolderBuilder.build();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    public int getTintColor() {
        return tintColor;
    }

    public boolean isAloud() {
        return aloud;
    }

    @Override
    public Runnable onLoaded() {
        return onLoaded;
    }

    public boolean isTreatedAsFirst() {
        return treatedAsFirst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextMessage that = (TextMessage) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
