package chattylabs.assistant;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

class MessageImageViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new MessageImageViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(viewType, viewGroup, false);
        return new ChatMessageImageViewHolder(view);
    }

    static class ChatMessageImageViewHolder extends RecyclerView.ViewHolder implements Binder {
        ImageView imageView;

        ChatMessageImageViewHolder(View v) {
            super(v);
            imageView = (ImageView) ((ViewGroup) v).getChildAt(0);
        }

        @Override
        public void onBind(ViewAdapter adapter, int position) {
            MessageImage message = (MessageImage) adapter.getItems().get(position);
            imageView.setTag(message.id);
            imageView.setImageResource(message.image);
            if (message.tintColor > 0) {
                imageView.setImageTintList(ColorStateList.valueOf(message.tintColor));
            } // TODO reset imageTintColor
        }
    }
}
