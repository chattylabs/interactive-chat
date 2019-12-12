package chattylabs.assistant;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

class ImageMessageViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new ImageMessageViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_interactive_assistant_message_image,
                viewGroup, false);
        return new ImageMessageViewHolderBuilder.ChatImageViewHolder(view);
    }

    static class ChatImageViewHolder extends RecyclerView.ViewHolder implements Binder {
        ImageView imageView;

        ChatImageViewHolder(View v) {
            super(v);
            imageView = (ImageView) ((ViewGroup) v).getChildAt(0);
        }

        @Override
        public void onBind(ViewAdapter adapter, int position) {
            ImageMessage message = (ImageMessage) adapter.getItems().get(position);
            imageView.setTag(message.id);
            imageView.setImageResource(message.image);
            if (message.tintColor > 0) {
                imageView.setImageTintList(ColorStateList.valueOf(message.tintColor));
            } // TODO reset tintColor
        }
    }
}
