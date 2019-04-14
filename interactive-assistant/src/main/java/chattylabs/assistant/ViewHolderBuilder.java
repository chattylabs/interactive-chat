package chattylabs.assistant;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

interface ViewHolderBuilder {
    RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType);

    interface Binder {
        void onBind(ViewAdapter adapter, int position);
    }
}
