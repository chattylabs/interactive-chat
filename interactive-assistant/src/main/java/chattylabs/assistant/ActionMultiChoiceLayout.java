package chattylabs.assistant;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * This Container delegates the click listener to the confirmation Button.
 */
public class ActionMultiChoiceLayout extends LinearLayout {

    public ActionMultiChoiceLayout(Context context) {
        this(context, null);
    }

    public ActionMultiChoiceLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionMultiChoiceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ActionMultiChoiceLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        getChildAt(1).setOnClickListener(listener);
    }
}
