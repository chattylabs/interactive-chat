package com.chattylabs.android.interactive.chat;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * This Container delegates the click listener to the confirmation Button.
 */
public class ActionMultiOptionContainer extends LinearLayout {

    public ActionMultiOptionContainer(Context context) {
        super(context);
    }

    public ActionMultiOptionContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionMultiOptionContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ActionMultiOptionContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        getChildAt(1).setOnClickListener(listener);
    }
}
