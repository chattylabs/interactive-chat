package chattylabs.assistant;

import androidx.emoji.text.EmojiCompat;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

class TextActionViewBuilder implements ActionViewBuilder {
    private static TextActionViewBuilder instance;
    private float defaultTextSize;

    public static ActionViewBuilder build() {
        return instance == null ? instance = new TextActionViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Button button = (Button) inflater.inflate(
                R.layout.item_interactive_assistant_action_text,
                viewGroup, false);

        TextAction textAction = (TextAction) action;
        if (defaultTextSize == 0) defaultTextSize = button.getTextSize();
        if (textAction.textSize > 0) {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, textAction.textSize);
        } else {
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
        }
        CharSequence text = InteractiveAssistant.processEmoji(textAction.text);
        Spanned span = InteractiveAssistant.makeText(text);
        button.setText(span);

        button.setTag(R.id.interactive_assistant_action_id, textAction.id);

        return button;
    }
}
