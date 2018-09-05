package com.chattylabs.android.user.interaction;

public interface HasViewType extends HasViewHolderBuilder {

    /**
     * Use id resources to uniquely identify item view types.
     */
    int getViewType();
}
