package chattylabs.assistant;


import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.Objects;

import chattylabs.conversations.SpeechSynthesizer;
import chattylabs.conversations.SynthesizerListener;

public class MessageText implements Node, HasId, HasOnLoaded, HasText, HasViewLayout,
                                    CanSynthesizeSpeech {

    final String id;
    String text;
    final float textSize;
    final int tintColor;
    final boolean aloud;
    final Runnable onLoaded;
    final boolean isFirstMessage;
    final boolean isInboundMessage;

    @Override
    public void consumeSynthesizer(SpeechSynthesizer speechSynthesizer,
                                   OnSynthesized onSynthesized) {
        speechSynthesizer.playTextNow(this.getText(),
                                      (SynthesizerListener.OnDone) utteranceId -> onSynthesized.execute(),
                                      (SynthesizerListener.OnError) (utteranceId, errorCode) -> speechSynthesizer.shutdown());
    }

    public static final class Builder {
        private String id;
        private String text;
        private float textSize;
        private int tintColor;
        private boolean aloud;
        private Runnable onLoaded;
        private boolean isFirstMessage;
        private boolean isInboundMessage;

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

        public Builder setFirstMessage(boolean firstMessage) {
            this.isFirstMessage = firstMessage;
            return this;
        }

        public Builder setInboundMessage(boolean inboundMessage) {
            isInboundMessage = inboundMessage;
            return this;
        }

        public MessageText build() {
            if (text == null || text.length() == 0) {
                throw new NullPointerException("Property \"text\" is required");
            }
            return new MessageText(this);
        }
    }

    private MessageText(Builder builder) {
        this.id             = builder.id;
        this.text           = builder.text;
        this.tintColor      = builder.tintColor;
        this.aloud          = builder.aloud;
        this.onLoaded       = builder.onLoaded;
        this.textSize       = builder.textSize;
        this.isFirstMessage = builder.isFirstMessage;
        this.isInboundMessage = builder.isInboundMessage;
    }

    public static Builder newBuilder(String id) {
        return new Builder(id);
    }

    @Override @LayoutRes
    public int getViewLayout() {
        return isFirstMessage ?
                    isInboundMessage ? R.layout.item_interactive_assistant_message_inbound_first
                    : R.layout.item_interactive_assistant_message_outbound_first
               : isInboundMessage ? R.layout.item_interactive_assistant_message_inbound
                                  : R.layout.item_interactive_assistant_message_outbound;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return MessageTextViewHolderBuilder.build();
    }

    @NonNull
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public boolean isFirstMessage() {
        return isFirstMessage;
    }

    public boolean isInboundMessage() {
        return isInboundMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageText that = (MessageText) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
