package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * This Container delegates the click listener to the confirmation Button.
 */
public class ChatActionMultiOptionContainer extends LinearLayout {

    public ChatActionMultiOptionContainer(Context context) {
        super(context);
    }

    public ChatActionMultiOptionContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatActionMultiOptionContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChatActionMultiOptionContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        getChildAt(1).setOnClickListener(listener);
    }
}
