package chattylabs.assistant;

import android.content.SharedPreferences;

public interface CanHandleState {
    void saveState(SharedPreferences sharedPreferences);

    void restoreSavedState(SharedPreferences sharedPreferences);
}
