package chattylabs.assistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

class ActionListViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new ActionListViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        if (viewType == R.id.interactive_assistant_action_multi_option_view_type) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            FrameLayout frame = new FrameLayout(viewGroup.getContext());
            frame.setLayoutParams(params);
            view = frame;
        } else {
            view = inflater.inflate(R.layout.item_interactive_assistant_action_list,
                    viewGroup, false);
        }
        return new ActionListViewHolderBuilder.ChatActionSetViewHolder(view);
    }

    static class ChatActionSetViewHolder extends RecyclerView.ViewHolder implements Binder {

        ViewGroup viewGroup;

        ChatActionSetViewHolder(View v) {
            super(v);
            viewGroup = (ViewGroup) v;
        }

        @Override
        public void onBind(ViewAdapter adapter, int position) {
            ActionList actionList = (ActionList) adapter.getItems().get(position);
            viewGroup.removeAllViews();
            for (Action action : actionList) {
                View actionView = ((HasActionViewBuilder) action).getActionViewBuilder()
                        .createView(viewGroup, action);
                actionView.setTag(R.id.interactive_assistant_item_position, position);
                actionView.setOnClickListener(v -> adapter.getActionListener().onClick(v, action));
                viewGroup.addView(actionView);
            }
        }
    }
}
