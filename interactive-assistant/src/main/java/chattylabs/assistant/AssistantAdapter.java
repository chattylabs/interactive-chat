package chattylabs.assistant;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.SimpleArrayMap;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AssistantAdapter extends ViewAdapter<RecyclerView.ViewHolder> {

    private OnActionListener actionListener;
    private List<Node> items = new ArrayList<>();
    private SimpleArrayMap<Integer, ViewHolderBuilder> viewHolders = new SimpleArrayMap<>();

    public interface OnActionListener {
        void onClick(@NonNull View view, @NonNull Action action);
    }

    AssistantAdapter(OnActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public OnActionListener getActionListener() {
        return actionListener;
    }

    public void addItem(Node item) {
        if (item instanceof HasViewType) {
            int viewType = ((HasViewType) item).getViewType();
            if (!viewHolders.containsKey(viewType))
                viewHolders.put(viewType, ((HasViewType) item).getViewHolderBuilder());
        }
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void checkViewHolders() {
        for (Node item : items) {
            if (item instanceof HasViewType) {
                int viewType = ((HasViewType) item).getViewType();
                if (!viewHolders.containsKey(viewType))
                    viewHolders.put(viewType, ((HasViewType) item).getViewHolderBuilder());
            }
        }
    }

    @Override
    public List<Node> getItems() {
        return items;
    }

    public int getLastPositionOf(Class type) {
        for (int a = items.size() - 1; a >= 0; a--) {
            if (type.isInstance(items.get(a))) return a;
        }
        return -1;
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public Node getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        Node item = items.get(position);
        return ((HasViewType) item).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return viewHolders.get(viewType).createViewHolder(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ((ViewHolderBuilder.Binder) viewHolder).onBind(this, position);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
