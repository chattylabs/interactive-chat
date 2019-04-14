package chattylabs.assistant;

import androidx.annotation.NonNull;

public interface Target {
    void to(@NonNull Node node, Node... optNodes);
}
