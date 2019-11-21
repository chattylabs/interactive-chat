package chattylabs.assistant;


import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public interface SourceId {
    TargetId from(@NonNull String id);
    TargetId from(@StringRes int id);
}
