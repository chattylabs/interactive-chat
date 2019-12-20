package chattylabs.assistant;

import androidx.annotation.LayoutRes;

public class LoadingView implements Node, HasViewLayout {

    @Override @LayoutRes
    public int getViewLayout() {
        return R.layout.item_interactive_assistant_loading;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return LoadingViewHolderBuilder.build();
    }
}
