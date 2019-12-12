package chattylabs.assistant;


import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.util.Objects;

public class ImageMessage implements Node, HasId, HasOnLoaded, HasViewType {
    final String id;
    final int image;
    final int tintColor;
    final Runnable onLoaded;

    public static class Builder {
        private String id;
        private int image;
        private int tintColor;
        private Runnable onLoaded;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setImage(@DrawableRes int resId) {
            this.image = resId;
            return this;
        }

        public Builder setTintColor(@ColorRes int resId) {
            this.tintColor = resId;
            return this;
        }

        public Builder setOnLoaded(Runnable afterLoaded) {
            this.onLoaded = afterLoaded;
            return this;
        }

        public ImageMessage build() {
            if (image <= 0) {
                throw new NullPointerException("Property \"image\" is required");
            }
            return new ImageMessage(this);
        }
    }

    private ImageMessage(Builder builder) {
        this.id = builder.id;
        this.image = builder.image;
        this.tintColor = builder.tintColor;
        this.onLoaded = builder.onLoaded;
    }

    @Override
    public int getViewType() {
        return R.id.interactive_assistant_message_image_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return ImageMessageViewHolderBuilder.build();
    }

    @NonNull
    @Override
    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public int getTintColor() {
        return tintColor;
    }

    @Override
    public Runnable onLoaded() {
        return onLoaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageMessage that = (ImageMessage) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
