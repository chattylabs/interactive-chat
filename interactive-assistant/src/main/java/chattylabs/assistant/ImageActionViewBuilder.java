package chattylabs.assistant;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

class ImageActionViewBuilder implements ActionViewBuilder {
    private static ImageActionViewBuilder instance;

    public static ActionViewBuilder build() {
        return instance == null ? instance = new ImageActionViewBuilder() : instance;
    }

    @Override
    public View createView(ViewGroup viewGroup, Action action) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ImageButton button = (ImageButton) inflater.inflate(
                R.layout.item_interactive_assistant_action_image,
                viewGroup, false);

        ImageAction imageAction = (ImageAction) action;
        button.setImageResource(imageAction.image);
        if (imageAction.tintColor > 0) {
            button.setImageTintList(ColorStateList.valueOf(imageAction.tintColor));
        }
        button.setTag(R.id.interactive_assistant_action_id, imageAction.id);

        return button;
    }
}
