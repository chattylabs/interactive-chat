package com.chattylabs.android.user.interaction;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chattylabs.sdk.android.common.DimensionUtils;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    TextView text;
    ImageView image;

    ChatMessageViewHolder(View v) {
        super(v);
        text = v.findViewById(R.id.interactive_chat_message_item_text);
        image = v.findViewById(R.id.interactive_chat_message_item_image);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            text.setLineSpacing(DimensionUtils.getDimension(v.getContext(),
                    TypedValue.COMPLEX_UNIT_SP, 6), 1.0f);
            text.setLetterSpacing(0.01f);
        }
    }

}
