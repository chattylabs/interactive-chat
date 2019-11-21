package chattylabs.assistant;

import android.os.Build;
import androidx.emoji.text.EmojiCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chattylabs.android.commons.DimensionUtils;


class TextMessageViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new TextMessageViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_assistant_message,
                viewGroup, false);
        return new TextMessageViewHolderBuilder.ChatMessageViewHolder(view);
    }

    static class ChatMessageViewHolder extends RecyclerView.ViewHolder implements Binder {
        TextView textView;
        float defaultTextSize;

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
        public void onBind(ViewAdapter adapter, int position) {
            TextMessage message = (TextMessage) adapter.getItems().get(position);
            if (defaultTextSize == 0) defaultTextSize = textView.getTextSize();
            if (message.textSize > 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, message.textSize);
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
            }
            CharSequence text = InteractiveAssistant.processEmoji(message.text);
            Spanned span = InteractiveAssistant.makeText(text);
            textView.setTag(message.id);
            textView.setText(span);
        }
    }
}
