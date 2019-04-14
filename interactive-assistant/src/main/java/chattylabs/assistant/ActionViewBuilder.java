package chattylabs.assistant;

import android.view.View;
import android.view.ViewGroup;

public interface ActionViewBuilder {
    View createView(ViewGroup viewGroup, Action action);
}
