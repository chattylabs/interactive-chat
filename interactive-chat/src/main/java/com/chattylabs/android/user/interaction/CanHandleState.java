package com.chattylabs.android.user.interaction;

import android.content.SharedPreferences;

public interface CanHandleState {
    void saveState(SharedPreferences sharedPreferences);

    void restoreSavedState(SharedPreferences sharedPreferences);
}
