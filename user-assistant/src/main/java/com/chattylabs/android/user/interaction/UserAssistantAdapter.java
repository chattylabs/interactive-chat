package com.chattylabs.android.user.interaction;

import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

public class UserAssistantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MESSAGE_IN = 0;
    private static final int MESSAGE_IN_AS_FIRST = 1;
    private static final int MESSAGE_OUT = 2;
    private static final int ACTIONS = 3;
    private static final int LOADING = 4;

    private OnActionListener listener;
    private List<Node> items;

    public interface OnActionListener {
        void onClick(@NonNull View view, @NonNull Action action);
    }

    UserAssistantAdapter(OnActionListener listener, List<Node> items) {
        this.listener = listener;
        this.items = items;
    }

    public void addItem(Node item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

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
        //notifyDataSetChanged();
    }

    public Node getItem(int position) {
        return this.items.get(position);
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        Node item = items.get(position);
        if (item instanceof ActionSet) {
            return ACTIONS;
        }
        else if (item instanceof Loading) {
            return LOADING;
        }
        else if (item instanceof Message && ((Message) item).showAsAnswer) {
            return MESSAGE_OUT;
        }
        return (item instanceof Message && ((Message) item).showAsFirst) ?
                MESSAGE_IN_AS_FIRST : MESSAGE_IN;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case LOADING:
                v = inflater.inflate(R.layout.item_user_assistant_loading, viewGroup, false);
                viewHolder = new LoadingViewHolder(v);
                break;
            case ACTIONS:
                v = inflater.inflate(R.layout.item_user_assistant_actions, viewGroup, false);
                viewHolder = new ActionViewHolder(v);
                break;
            case MESSAGE_OUT:
                v = inflater.inflate(R.layout.item_user_assistant_action_selected, viewGroup, false);
                viewHolder = new MessageViewHolder(v);
                break;
            case MESSAGE_IN_AS_FIRST:
                v = inflater.inflate(R.layout.item_user_assistant_system_message_as_first, viewGroup, false);
                viewHolder = new MessageViewHolder(v);
                break;
            default:
                v = inflater.inflate(R.layout.item_user_assistant_system_message, viewGroup, false);
                viewHolder = new MessageViewHolder(v);
                break;
        }
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case ACTIONS:
                configureActionViewHolder((ActionViewHolder) viewHolder, position);
                break;
            case LOADING:
                // no actions
                break;
            default:
                configureMessageViewHolder((MessageViewHolder) viewHolder, position);
                break;
        }
    }

    private void configureActionViewHolder(ActionViewHolder viewHolder, int position) {
        @SuppressWarnings("unchecked") ActionSet actionSet = (ActionSet) items.get(position);
        viewHolder.actions.removeAllViews();
        for (Action action : actionSet) {
            LayoutInflater inflater = LayoutInflater.from(viewHolder.actions.getContext());
            View button = inflater.inflate(action.image > 0
                                                      ? R.layout.item_user_assistant_imagebutton
                                                      : R.layout.item_user_assistant_button,
                                                      viewHolder.actions, false);
            if (action.image > 0) {
                //noinspection ConstantConditions
                ((ImageButton)button).setImageResource(action.image);
                if (action.tintColor > 0) {
                    ((ImageButton)button).setImageTintList(ColorStateList.valueOf(action.tintColor));
                }
            } else {
                if (action.textSize > 0) {
                    ((Button) button).setTextSize(TypedValue.COMPLEX_UNIT_SP, action.textSize);
                }
                Spanned span;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    span = Html.fromHtml(action.text1, Html.FROM_HTML_MODE_COMPACT);
                }
                else {
                    span = Html.fromHtml(action.text1);
                }
                ((Button) button).setText(span);
            }
            button.setTag(R.id.user_assistant_action_id, action.id);
            button.setTag(R.id.user_assistant_item_position, position);
            button.setOnClickListener(v -> listener.onClick(v, action));
            viewHolder.actions.addView(button);
        }
    }

    private void configureMessageViewHolder(MessageViewHolder viewHolder, int position) {
        Message message = (Message) items.get(position);
        if (message.imageId > 0) {
            viewHolder.text.setVisibility(View.GONE);
            viewHolder.image.setVisibility(View.VISIBLE);
            viewHolder.image.setTag(message.id);
            viewHolder.image.setImageResource(message.imageId);
            if (message.tintColorId > 0) {
                viewHolder.image.setImageTintList(ColorStateList.valueOf(message.tintColorId));
            }
        } else {
            viewHolder.image.setVisibility(View.GONE);
            viewHolder.text.setVisibility(View.VISIBLE);
            if (message.textSize > 0) {
                viewHolder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, message.textSize);
            }
            Spanned span;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                span = Html.fromHtml(message.text, Html.FROM_HTML_MODE_COMPACT);
            }
            else {
                span = Html.fromHtml(message.text);
            }
            viewHolder.text.setTag(message.id);
            viewHolder.text.setText(span);
        }
    }

    // Return the size of your items (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
