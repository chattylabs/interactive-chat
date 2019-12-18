package chattylabs.assistant;

import android.content.Context;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

import static chattylabs.assistant.ActionMultiChoice.OnOptionChangeListener;

class ActionMultiChoiceViewBuilder implements ActionViewBuilder {

    private final OnOptionChangeListener onOptionChangeListener;
    private float defaultTextSize;

    ActionMultiChoiceViewBuilder(OnOptionChangeListener onOptionChangeListener) {
        this.onOptionChangeListener = onOptionChangeListener;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        final Context context = viewGroup.getContext();
        final ActionMultiChoice multiAction = (ActionMultiChoice) action;
        final ViewGroup multiOptionAction = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_assistant_action_multichoice, viewGroup, false);
        final FlexboxLayout optionsLayout = (FlexboxLayout) multiOptionAction.getChildAt(0);
        final MaterialButton confirmButton = (MaterialButton) multiOptionAction.getChildAt(1);
        final List<ActionChipChoice> actionChipChoices = multiAction.getActionChipChoices();

        optionsLayout.removeAllViews();
        for (ActionChipChoice actionChipChoice : actionChipChoices) {
            optionsLayout.addView(getOption(context, actionChipChoice, optionsLayout,
                    defaultTextSize, onOptionChangeListener, false));
        }

        final Spanned span = InteractiveAssistant.formatHTML(multiAction.getConfirmationAction().text);
        final CharSequence text = InteractiveAssistant.processEmoji(span);
        confirmButton.setText(text);
        return multiOptionAction;
    }

    static View getOption(Context context, ActionChipChoice actionChipChoice, FlexboxLayout container,
                          float defaultTextSize, OnOptionChangeListener onOptionChangeListener, boolean isFeedback) {
        final Chip chip = (Chip) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_assistant_action_chip, container, false);

        final Spanned span = InteractiveAssistant.formatHTML(actionChipChoice.getText());
        final CharSequence text = InteractiveAssistant.processEmoji(span);

        chip.setText(text);

        if (defaultTextSize == 0) defaultTextSize = chip.getTextSize();
        if (actionChipChoice.textSize > 0) {
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, actionChipChoice.textSize);
        } else {
            chip.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
        }

        if (!isFeedback)
            chip.setOnCheckedChangeListener((compoundButton, selected) -> {
                actionChipChoice.setSelected(selected);
                if (onOptionChangeListener != null) {
                    onOptionChangeListener.onChange(actionChipChoice, selected);
                }
            });

        if (actionChipChoice.getIcon() != 0) {
            chip.setChipIconResource(actionChipChoice.getIcon());
            if (actionChipChoice.getIconTintColor() != 0)
                chip.setChipIconTintResource(actionChipChoice.getIconTintColor());
        }
        chip.setSelected(actionChipChoice.isSelected());
        chip.setChecked(actionChipChoice.isSelected());
        chip.setClickable(!isFeedback);

        actionChipChoice.attach(chip);

        return chip;
    }
}
