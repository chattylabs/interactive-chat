package chattylabs.assistant;

import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


class FeedbackActionTextViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new FeedbackActionTextViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(viewType, viewGroup, false);
        return new FeedbackActionTextViewHolderBuilder.ChatActionTextSelectedViewHolder(view);
    }

    static class ChatActionTextSelectedViewHolder extends RecyclerView.ViewHolder implements Binder {

        TextView textView;
        float defaultTextSize;

        ChatActionTextSelectedViewHolder(View v) {
            super(v);
            textView = (TextView) ((ViewGroup) v).getChildAt(0);
        }

        @Override
        public void onBind(ViewAdapter adapter, int position) {
            FeedbackActionText textSelected = (FeedbackActionText) adapter.getItems().get(position);
            if (defaultTextSize == 0) defaultTextSize = textView.getTextSize();
            if (textSelected.textSize > 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSelected.textSize);
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
            }
            Spanned span = InteractiveAssistant.formatHTML(textSelected.text);
            CharSequence text = InteractiveAssistant.processEmoji(span);
            textView.setText(text);
        }
    }
}
