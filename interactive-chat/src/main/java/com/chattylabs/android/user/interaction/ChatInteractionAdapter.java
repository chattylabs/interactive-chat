package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ChatInteractionAdapter extends ChatInteractionViewAdapter<RecyclerView.ViewHolder> {

    private OnActionListener actionListener;
    private List<ChatNode> items = new ArrayList<>();
    private SimpleArrayMap<Integer, ChatViewHolderBuilder> viewHolders = new SimpleArrayMap<>();

    public interface OnActionListener {
        void onClick(@NonNull View view, @NonNull ChatAction action);
    }

    ChatInteractionAdapter(OnActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public OnActionListener getActionListener() {
        return actionListener;
    }

    public void addItem(ChatNode item) {
        int viewType = item.getViewType();
        if (!viewHolders.containsKey(viewType))
            viewHolders.put(viewType, item.getViewHolderBuilder());
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void checkViewHolders() {
        for (ChatNode item : items) {
            int viewType = item.getViewType();
            if (!viewHolders.containsKey(viewType))
                viewHolders.put(viewType, item.getViewHolderBuilder());
        }
    }

    @Override
    public List<ChatNode> getItems() {
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

    public ChatNode getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        ChatNode item = items.get(position);
        return item.getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return viewHolders.get(viewType).createViewHolder(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ((ChatViewHolderBuilder.Binder) viewHolder).onBind(this, position);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
