package chattylabs.assistant;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * This Container delegates the click listener to the confirmation Button.
 */
public class MultiChoiceActionContainer extends LinearLayout {

    public MultiChoiceActionContainer(Context context) {
        super(context);
    }

    public MultiChoiceActionContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiChoiceActionContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultiChoiceActionContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        getChildAt(1).setOnClickListener(listener);
    }
}
