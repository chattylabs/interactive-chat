package chattylabs.assistant;

import android.content.Context;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Random;

class AutoCompleteActionViewBuilder implements ActionViewBuilder {
    private static AutoCompleteActionViewBuilder instance;

    public static ActionViewBuilder build() {
        return instance == null ? instance = new AutoCompleteActionViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        AutoCompleteAction autoCompleteAction = (AutoCompleteAction) action;

        LinearLayout widget = (LinearLayout) inflater.inflate(
                R.layout.item_interactive_assistant_action_autotextfield,
                viewGroup, false);

        List<ActionText> actions = autoCompleteAction.getActions();

        for (ActionText item : actions) {
            final Button button =
                    (Button) inflater.inflate(R.layout.item_interactive_assistant_action_text,
                    viewGroup, false);
            final CharSequence text = InteractiveAssistant.processEmoji(item.getText());
            final Spanned span = InteractiveAssistant.formatHTML(text);
            button.setText(span);
            final LinearLayout buttonLayout = (LinearLayout) widget.getChildAt(1);
            button.setOnClickListener(v -> {
                widget.callOnClick();
                item.onSelected().execute(item);
            });
            buttonLayout.addView(button);
        }

        // TODO: Add adapter, etc..

        final TextInputLayout textInputLayout = widget.findViewById(R.id.autocomplete_char_counter);
        final AutoCompleteTextView autocomplete = textInputLayout.findViewById(R.id.autocomplete_text);
        if (autoCompleteAction.getTextLines() > 0) {
            autocomplete.setLines(autoCompleteAction.getTextLines());
            autocomplete.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
                    | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
            autocomplete.setMovementMethod(new ScrollingMovementMethod());
            autocomplete.setHorizontallyScrolling(false);
        } else {
            autocomplete.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    | InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        }
        if (autoCompleteAction.getCharCount() > 0) {
            textInputLayout.setCounterEnabled(true);
            textInputLayout.setCounterMaxLength(autoCompleteAction.getCharCount());
        }

        widget.setTag(R.id.interactive_assistant_action_id, autoCompleteAction.id);

        autocomplete.setOnFocusChangeListener((v, hasFocus) -> {
            if(v == autocomplete && !hasFocus) {
                InputMethodManager imm =
                        (InputMethodManager) widget.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        List<String> hints = autoCompleteAction.getHints();
        if (hints.size() > 0)
            autocomplete.setHint(hints.get(new Random().nextInt(hints.size())));

        autoCompleteAction.attach(autocomplete);

        return widget;
    }
}
