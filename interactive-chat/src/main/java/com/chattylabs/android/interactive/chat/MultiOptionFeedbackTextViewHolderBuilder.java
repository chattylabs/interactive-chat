package com.chattylabs.android.interactive.chat;

import android.os.Build;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chattylabs.android.commons.DimensionUtils;


class MultiOptionFeedbackTextViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new MultiOptionFeedbackTextViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_chat_action_text_selected,
                viewGroup, false);
        return new ChatActionMultiOptionTextSelectedViewHolder(view);
    }

    static class ChatActionMultiOptionTextSelectedViewHolder extends RecyclerView.ViewHolder implements Binder {

        TextView textView;

        ChatActionMultiOptionTextSelectedViewHolder(View v) {
            super(v);
            textView = (TextView) ((ViewGroup) v).getChildAt(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView.setLineSpacing(DimensionUtils.getDimension(v.getContext(),
                        TypedValue.COMPLEX_UNIT_SP, 6), 1.0f);
                textView.setLetterSpacing(0.01f);
            }
        }

        @Override
        public void onBind(InteractiveChatViewAdapter adapter, int position) {
            MultiOptionFeedbackText textSelected = (MultiOptionFeedbackText) adapter.getItems().get(position);
//            if (textSelected.textSize > 0) {
//                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSelected.textSize);
//            }
            CharSequence text = EmojiCompat.get().process(textSelected.text);
            Spanned span = InteractiveChatComponentImpl.makeText(text);
            textView.setText(span);
        }
    }
}
