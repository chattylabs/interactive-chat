package chattylabs.assistant;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


class FeedbackActionIconViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new FeedbackActionIconViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(viewType, viewGroup, false);
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
            FeedbackActionIcon imageSelected = (FeedbackActionIcon) adapter.getItems().get(position);
            image.setImageResource(imageSelected.icon);
            if (imageSelected.tintColor > 0) {
                image.setImageTintList(ColorStateList.valueOf(imageSelected.tintColor));
            } // TODO reset imageTintColor
        }
    }
}
