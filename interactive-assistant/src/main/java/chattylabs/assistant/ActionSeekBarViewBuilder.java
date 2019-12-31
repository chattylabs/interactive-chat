package chattylabs.assistant;

import android.content.Context;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

import static chattylabs.assistant.ActionMultiChoice.OnOptionChangeListener;

class ActionSeekBarViewBuilder implements ActionViewBuilder {

    private SeekBar.OnSeekBarChangeListener listener;

    ActionSeekBarViewBuilder(SeekBar.OnSeekBarChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        final Context context = viewGroup.getContext();
        final ActionSeekBar seekBarAction = (ActionSeekBar) action;
        final ViewGroup layout = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.item_interactive_assistant_action_seekbar, viewGroup, false);
        final AppCompatSeekBar seekBar = (AppCompatSeekBar) layout.getChildAt(0);
        final MaterialButton confirmButton = (MaterialButton) layout.getChildAt(1);

        seekBar.setMax(seekBarAction.maxProgress);
        seekBar.setProgress(seekBarAction.progress);
        seekBar.setOnSeekBarChangeListener(this.listener);
        seekBarAction.setLazyProgress(seekBar::getProgress);

        final Spanned span = InteractiveAssistant.formatHTML(seekBarAction.confirmationAction.text);
        final CharSequence text = InteractiveAssistant.processEmoji(span);
        confirmButton.setText(text);
        return layout;
    }
}
