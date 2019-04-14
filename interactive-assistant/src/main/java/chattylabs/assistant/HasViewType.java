package chattylabs.assistant;

public interface HasViewType extends HasViewHolderBuilder {

    /**
     * Use id resources to uniquely identify item view types.
     */
    int getViewType();
}
