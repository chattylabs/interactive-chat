package chattylabs.assistant;

import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


class MessageTextViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new MessageTextViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_assistant_message,
                viewGroup, false);
        return new MessageTextViewHolderBuilder.ChatMessageViewHolder(view);
    }

    static class ChatMessageViewHolder extends RecyclerView.ViewHolder implements Binder {
        TextView textView;
        float defaultTextSize;

        ChatMessageViewHolder(View v) {
            super(v);
            textView = (TextView) ((ViewGroup) v).getChildAt(0);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        public void onBind(ViewAdapter adapter, int position) {
            MessageText message = (MessageText) adapter.getItems().get(position);
            if (defaultTextSize == 0) defaultTextSize = textView.getTextSize();
            if (message.textSize > 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, message.textSize);
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
            }
            CharSequence text = InteractiveAssistant.processEmoji(message.text);
            Spanned span = InteractiveAssistant.formatHTML(text);
            textView.setTag(message.id);
            textView.setText(span);
        }
    }
}
