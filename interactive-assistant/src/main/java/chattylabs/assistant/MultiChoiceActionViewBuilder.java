package chattylabs.assistant;

import android.content.Context;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static chattylabs.assistant.MultiChoiceAction.OnOptionChangeListener;

class MultiChoiceActionViewBuilder implements ActionViewBuilder {

    private final OnOptionChangeListener onOptionChangeListener;
    private float defaultTextSize;

    MultiChoiceActionViewBuilder(OnOptionChangeListener onOptionChangeListener) {
        this.onOptionChangeListener = onOptionChangeListener;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        final Context context = viewGroup.getContext();
        final MultiChoiceAction multiAction = (MultiChoiceAction) action;
        final ViewGroup multiOptionAction = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_assistant_action_multichoice, viewGroup, false);
        final FlexboxLayout optionsLayout = (FlexboxLayout) multiOptionAction.getChildAt(0);
        final Button confirmButton = (Button) multiOptionAction.getChildAt(1);
        final List<ChoiceItem> choiceItems = multiAction.getChoiceItems();

        for (ChoiceItem choiceItem : choiceItems) {
            final FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = context.getResources()
                    .getDimensionPixelSize(R.dimen.item_interactive_chat_margin_left);
            layoutParams.bottomMargin = context.getResources()
                    .getDimensionPixelSize(R.dimen.item_interactive_assistant_margin_bottom);
            optionsLayout.addView(getOption(context, choiceItem, optionsLayout), layoutParams);
        }

        final CharSequence text = InteractiveAssistant.processEmoji(
                multiAction.getConfirmationAction().text);
        final Spanned span = InteractiveAssistant.makeText(text);
        confirmButton.setText(span);
        return multiOptionAction;
    }

    private View getOption(Context context, ChoiceItem choiceItem, FlexboxLayout container) {
        final ToggleButton optionButton = (ToggleButton) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_assistant_toggle_button, container, false);

        final CharSequence text = InteractiveAssistant.processEmoji(choiceItem.getText());
        final Spanned span = InteractiveAssistant.makeText(text);

        optionButton.setText(span);
        optionButton.setTextOff(span);
        optionButton.setTextOn(span);

        if (defaultTextSize == 0) defaultTextSize = optionButton.getTextSize();
        if (choiceItem.textSize > 0) {
            optionButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, choiceItem.textSize);
        } else {
            optionButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
        }

        optionButton.setOnCheckedChangeListener((compoundButton, selected) -> {
            choiceItem.setSelected(selected);
            if (onOptionChangeListener != null) {
                onOptionChangeListener.onChange(choiceItem, selected);
            }
        });

        optionButton.setSelected(choiceItem.isSelected());

        choiceItem.attach(optionButton);

        return optionButton;
    }
}
