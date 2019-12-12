package chattylabs.assistant;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


class ImageFeedbackViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new ImageFeedbackViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_assistant_action_image_selected,
                viewGroup, false);
        return new ImageFeedbackViewHolderBuilder.ChatActionImageSelectedViewHolder(view);
    }

    static class ChatActionImageSelectedViewHolder extends RecyclerView.ViewHolder implements Binder {

        ImageView image;

        ChatActionImageSelectedViewHolder(View v) {
            super(v);
            image = (ImageView) ((ViewGroup) v).getChildAt(0);
        }

        @Override
        public void onBind(ViewAdapter adapter, int position) {
            ImageFeedback imageSelected = (ImageFeedback) adapter.getItems().get(position);
            image.setImageResource(imageSelected.image);
            if (imageSelected.tintColor > 0) {
                image.setImageTintList(ColorStateList.valueOf(imageSelected.tintColor));
            } // TODO reset tintColor
        }
    }
}
