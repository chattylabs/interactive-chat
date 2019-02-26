package com.chattylabs.android.interactive.chat;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class SeparatorItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalHeightSeparation;

    SeparatorItemDecoration(int verticalHeightSeparation) {
        this.verticalHeightSeparation = verticalHeightSeparation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        //if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
        outRect.bottom = verticalHeightSeparation;
    }
}
