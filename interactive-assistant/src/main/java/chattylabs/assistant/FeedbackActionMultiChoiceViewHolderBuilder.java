package chattylabs.assistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;


class FeedbackActionMultiChoiceViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new FeedbackActionMultiChoiceViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final ViewGroup multiOptionAction = (ViewGroup) inflater
                .inflate(viewType, viewGroup, false);
        return new ChatActionMultiOptionTextSelectedViewHolder(multiOptionAction);
    }

    static class ChatActionMultiOptionTextSelectedViewHolder extends RecyclerView.ViewHolder implements Binder {

        ViewGroup multiOptionAction;

        ChatActionMultiOptionTextSelectedViewHolder(View v) {
            super(v);
            multiOptionAction = (ViewGroup) v;
        }

        @Override
        public void onBind(ViewAdapter adapter, int position) {
            FeedbackActionMultiChoice optionsSelected = (FeedbackActionMultiChoice) adapter.getItems().get(position);
            multiOptionAction.getChildAt(1).setVisibility(View.GONE);
            FlexboxLayout optionsLayout = (FlexboxLayout) multiOptionAction.getChildAt(0);
            optionsLayout.removeAllViews();
            for (ActionChipChoice actionChipChoice : optionsSelected.actionChipChoices) {
                optionsLayout.addView(ActionMultiChoiceViewBuilder.getOption(optionsLayout.getContext(),
                        actionChipChoice, optionsLayout, 0, null, true));
            }
        }
    }
}
