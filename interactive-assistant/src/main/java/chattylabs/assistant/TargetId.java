package chattylabs.assistant;


import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public interface TargetId {
    void to(@NonNull String id, String... ids);
    void to(@StringRes int id, @StringRes Integer... ids);
}
