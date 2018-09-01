package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Custom LinearLayout - Container to identify multi option
 */
public class ChatMultiOptionActionContainer extends LinearLayout {

    public ChatMultiOptionActionContainer(Context context) {
        super(context);
    }

    public ChatMultiOptionActionContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatMultiOptionActionContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChatMultiOptionActionContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        findViewById(R.id.confirmAction).setOnClickListener(listener);
    }
}
