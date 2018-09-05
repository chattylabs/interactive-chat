package com.chattylabs.android.user.interaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;

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

        FlexboxLayout viewGroup;

        ChatActionSetViewHolder(View v) {
            super(v);
            viewGroup = (FlexboxLayout) v;
        }

        @Override
        public void onBind(ChatInteractionViewAdapter adapter, int position) {
            final Context context = viewGroup.getContext();
            ChatActionList actionList = (ChatActionList) adapter.getItems().get(position);
            viewGroup.removeAllViews();
            for (ChatAction action : actionList) {
                View actionView = ((HasActionViewBuilder) action).getActionViewBuilder()
                        .createView(viewGroup, action);
                actionView.setTag(R.id.interactive_chat_item_position, position);
                actionView.setOnClickListener(v -> adapter.getActionListener().onClick(v, action));
                final FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) actionView.getLayoutParams();
                layoutParams.leftMargin = context.getResources()
                        .getDimensionPixelSize(R.dimen.item_interactive_chat_margin_left);
                layoutParams.bottomMargin = context.getResources()
                        .getDimensionPixelSize(R.dimen.item_interactive_chat_margin_bottom);
                viewGroup.addView(actionView, layoutParams);
            }
        }
    }
}
