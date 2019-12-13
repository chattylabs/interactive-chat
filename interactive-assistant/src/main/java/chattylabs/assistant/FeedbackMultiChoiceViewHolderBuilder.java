package chattylabs.assistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;


class FeedbackMultiChoiceViewHolderBuilder implements ViewHolderBuilder {

    public static ViewHolderBuilder build() {
        return new FeedbackMultiChoiceViewHolderBuilder();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final ViewGroup multiOptionAction = (ViewGroup) inflater
                .inflate(R.layout.item_interactive_assistant_action_multichoice, viewGroup, false);
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
            FeedbackMultiChoice optionsSelected = (FeedbackMultiChoice) adapter.getItems().get(position);
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
