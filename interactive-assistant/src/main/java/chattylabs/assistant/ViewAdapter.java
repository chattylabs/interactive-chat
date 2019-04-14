package chattylabs.assistant;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

abstract class ViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    public abstract List<Node> getItems();
    public abstract AssistantAdapter.OnActionListener getActionListener();
}
