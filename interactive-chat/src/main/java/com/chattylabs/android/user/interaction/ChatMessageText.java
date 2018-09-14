package com.chattylabs.android.user.interaction;

import android.support.annotation.ColorRes;

import com.chattylabs.sdk.android.voice.SpeechSynthesizerComponent;
import com.chattylabs.sdk.android.voice.SynthesizerListener;

import java.util.Objects;

public class ChatMessageText implements ChatNode, HasId, HasOnLoaded, HasText, HasViewType,
    CanSynthesize {

    final String id;
    final String text;
    final float textSize;
    final int tintColor;
    final boolean aloud;
    final Runnable onLoaded;
    final boolean treatedAsFirst;

    @Override
    public boolean isSynthesizerConsumed(HasText item,
                                         SpeechSynthesizerComponent component,
                                         SynthesizerListener.OnDone onDone) {
        component.playText(item.getText(), onDone);
        return true;
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

        public ChatMessageText build() {
            if (text == null || text.length() == 0) {
                throw new NullPointerException("Property \"text\" is required");
            }
            return new ChatMessageText(this);
        }
    }

    private ChatMessageText(Builder builder) {
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
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatMessageTextViewHolderBuilder.build();
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
        ChatMessageText that = (ChatMessageText) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
