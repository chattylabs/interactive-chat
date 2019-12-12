package chattylabs.assistant;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


class FeedbackIconViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new FeedbackIconViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_assistant_action_icon_selected,
                viewGroup, false);
        return new ChatActionIconSelectedViewHolder(view);
    }

    static class ChatActionIconSelectedViewHolder extends RecyclerView.ViewHolder implements Binder {

        ImageView image;

        ChatActionIconSelectedViewHolder(View v) {
            super(v);
            image = (ImageView) ((ViewGroup) v).getChildAt(0);
        }

        @Override
        public void onBind(ViewAdapter adapter, int position) {
            FeedbackIcon imageSelected = (FeedbackIcon) adapter.getItems().get(position);
            image.setImageResource(imageSelected.icon);
            if (imageSelected.tintColor > 0) {
                image.setImageTintList(ColorStateList.valueOf(imageSelected.tintColor));
            } // TODO reset imageTintColor
        }
    }
}
