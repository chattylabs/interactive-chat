package chattylabs.assistant;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

interface ViewHolderBuilder {
    RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType);

    interface Binder {
        void onBind(ViewAdapter adapter, int position);
    }
}
