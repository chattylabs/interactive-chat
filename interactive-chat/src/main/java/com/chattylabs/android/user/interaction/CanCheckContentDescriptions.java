package com.chattylabs.android.user.interaction;

public interface CanCheckContentDescriptions {

    int MATCHED = 1;
    int NOT_MATCHED = 2;
    int REPEAT = 3;

    int matches(String result);
}
