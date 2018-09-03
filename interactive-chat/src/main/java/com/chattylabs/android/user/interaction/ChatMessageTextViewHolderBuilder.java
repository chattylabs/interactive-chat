package com.chattylabs.android.user.interaction;

import android.os.Build;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chattylabs.sdk.android.common.DimensionUtils;

class ChatMessageTextViewHolderBuilder implements ChatViewHolderBuilder {

    public static ChatViewHolderBuilder build() {
        return new ChatMessageTextViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_chat_message,
                viewGroup, false);
        return new ChatMessageTextViewHolderBuilder.ChatMessageViewHolder(view);
    }

    static class ChatMessageViewHolder extends RecyclerView.ViewHolder implements Binder {
        TextView textView;

        ChatMessageViewHolder(View v) {
            super(v);
            textView = (TextView) ((ViewGroup) v).getChildAt(0);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView.setLineSpacing(DimensionUtils.getDimension(v.getContext(),
                        TypedValue.COMPLEX_UNIT_SP, 6), 1.0f);
                textView.setLetterSpacing(0.01f);
            }
        }

        @Override
        public void onBind(ChatInteractionViewAdapter adapter, int position) {
            ChatMessageText message = (ChatMessageText) adapter.getItems().get(position);
            if (message.textSize > 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, message.textSize);
            }
            CharSequence text = EmojiCompat.get().process(message.text);
            Spanned span = ChatInteractionComponentImpl.makeText(text);
            textView.setTag(message.id);
            textView.setText(span);
        }
    }
}
