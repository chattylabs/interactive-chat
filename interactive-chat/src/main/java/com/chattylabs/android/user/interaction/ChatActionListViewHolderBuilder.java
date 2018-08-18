package com.chattylabs.android.user.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


class ChatActionListViewHolderBuilder implements ChatViewHolderBuilder {

    public static ChatViewHolderBuilder build() {
        return new ChatActionListViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_chat_action_list,
                viewGroup, false);
        return new ChatActionListViewHolderBuilder.ChatActionSetViewHolder(view);
    }

    static class ChatActionSetViewHolder extends RecyclerView.ViewHolder implements Binder {

        ViewGroup actions;

        ChatActionSetViewHolder(View v) {
            super(v);
            actions = (ViewGroup) v;
        }

        @Override
        public void onBind(ChatInteractionViewAdapter adapter, int position) {
            ChatActionList actionSet = (ChatActionList) adapter.getItems().get(position);
            actions.removeAllViews();
            for (ChatAction action : actionSet) {
                View button = action.getActionViewBuilder().createView(actions, action);
                button.setTag(R.id.interactive_chat_item_position, position);
                button.setOnClickListener(v -> adapter.getActionListener().onClick(v, action));
                actions.addView(button);
            }
        }
    }
}
