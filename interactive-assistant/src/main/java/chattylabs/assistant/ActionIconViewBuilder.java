package chattylabs.assistant;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

class ActionIconViewBuilder implements ActionViewBuilder {
    private static ActionIconViewBuilder instance;

    public static ActionViewBuilder build() {
        return instance == null ? instance = new ActionIconViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        MaterialButton button = (MaterialButton) inflater.inflate(
                R.layout.item_interactive_assistant_action_icon,
                viewGroup, false);

        ActionIcon actionIcon = (ActionIcon) action;
        button.setText(actionIcon.text);
        button.setIcon(ContextCompat.getDrawable(viewGroup.getContext(), actionIcon.icon));
        if (actionIcon.tintColor > 0) {
            button.setIconTint(ColorStateList.valueOf(actionIcon.tintColor));
        }
        button.setTag(R.id.interactive_assistant_action_id, actionIcon.id);

        return button;
    }
}
