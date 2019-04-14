package chattylabs.assistant;

import android.os.Build;
import androidx.emoji.text.EmojiCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chattylabs.android.commons.DimensionUtils;


class TextFeedbackViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new TextFeedbackViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_assistant_action_text_selected,
                viewGroup, false);
        return new TextFeedbackViewHolderBuilder.ChatActionTextSelectedViewHolder(view);
    }

    static class ChatActionTextSelectedViewHolder extends RecyclerView.ViewHolder implements Binder {

        TextView textView;
        float defaultTextSize;

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
        public void onBind(ViewAdapter adapter, int position) {
            TextFeedback textSelected = (TextFeedback) adapter.getItems().get(position);
            if (defaultTextSize == 0) defaultTextSize = textView.getTextSize();
            if (textSelected.textSize > 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSelected.textSize);
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
            }
            CharSequence text = EmojiCompat.get().process(textSelected.text);
            Spanned span = InteractiveAssistant.makeText(text);
            textView.setText(span);
        }
    }
}
