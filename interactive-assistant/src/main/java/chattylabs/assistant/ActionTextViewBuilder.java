package chattylabs.assistant;

import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

class ActionTextViewBuilder implements ActionViewBuilder {
    private static ActionTextViewBuilder instance;
    private float defaultTextSize;

    public static ActionViewBuilder build() {
        return instance == null ? instance = new ActionTextViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        MaterialButton button = (MaterialButton) inflater.inflate(
                R.layout.item_interactive_assistant_action_text,
                viewGroup, false);

        ActionText actionText = (ActionText) action;
        if (defaultTextSize == 0) defaultTextSize = button.getTextSize();
        if (actionText.textSize > 0) {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, actionText.textSize);
        } else {
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
        }
        Spanned span = InteractiveAssistant.formatHTML(actionText.text);
        CharSequence text = InteractiveAssistant.processEmoji(span);
        button.setText(text);

        button.setTag(R.id.interactive_assistant_action_id, actionText.id);

        return button;
    }
}
