package chattylabs.assistant;

public class LoadingView implements Node, HasViewType {

    @Override
    public int getViewType() {
        return R.id.interactive_assistant_loading_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return LoadingViewHolderBuilder.build();
    }
}
