package com.chattylabs.android.interactive.chat;

public interface HasViewType extends HasViewHolderBuilder {

    /**
     * Use id resources to uniquely identify item view types.
     */
    int getViewType();
}
