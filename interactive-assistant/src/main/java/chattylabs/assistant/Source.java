package chattylabs.assistant;


import androidx.annotation.NonNull;

public interface Source {
    Target from(@NonNull Node node);
}
