package chattylabs.assistant;

import androidx.annotation.LayoutRes;

public interface HasViewLayout extends HasViewHolderBuilder {

    @LayoutRes int getViewLayout();
}
