package com.chattylabs.android.user.interaction;

import android.os.Build;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chattylabs.sdk.android.common.DimensionUtils;


class ChatActionFeedbackTextViewHolderBuilder implements ChatViewHolderBuilder {

    public static ChatViewHolderBuilder build() {
        return new ChatActionFeedbackTextViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_chat_action_text_selected,
                viewGroup, false);
        return new ChatActionFeedbackTextViewHolderBuilder.ChatActionTextSelectedViewHolder(view);
    }

    static class ChatActionTextSelectedViewHolder extends RecyclerView.ViewHolder implements Binder {

        TextView textView;

        ChatActionTextSelectedViewHolder(View v) {
            super(v);
            textView = (TextView) ((ViewGroup) v).getChildAt(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView.setLineSpacing(DimensionUtils.getDimension(v.getContext(),
                        TypedValue.COMPLEX_UNIT_SP, 6), 1.0f);
                textView.setLetterSpacing(0.01f);
            }
        }

        @Override
        public void onBind(ChatInteractionViewAdapter adapter, int position) {
            ChatActionFeedbackText textSelected = (ChatActionFeedbackText) adapter.getItems().get(position);
            if (textSelected.textSize > 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSelected.textSize);
            }
            Spanned span;
            String textString = (String) EmojiCompat.get().process(textSelected.text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                span = Html.fromHtml(textString, Html.FROM_HTML_MODE_COMPACT);
            }
            else {
                span = Html.fromHtml(textString);
            }
            textView.setText(span);
        }
    }
}
