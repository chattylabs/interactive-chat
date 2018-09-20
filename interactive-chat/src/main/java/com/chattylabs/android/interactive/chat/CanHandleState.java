package com.chattylabs.android.interactive.chat;

import android.content.SharedPreferences;

public interface CanHandleState {
    void saveState(SharedPreferences sharedPreferences);

    void restoreSavedState(SharedPreferences sharedPreferences);
}
