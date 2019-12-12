package chattylabs.assistant;

import java.util.ArrayList;
import java.util.Set;

import chattylabs.conversations.RecognizerListener;
import chattylabs.conversations.SpeechRecognizer;

import static chattylabs.assistant.CanCheckContentDescriptions.MATCHED;
import static chattylabs.assistant.CanCheckContentDescriptions.REPEAT;

class ActionList extends ArrayList<Action> implements Node, HasViewType, CanRecognizeSpeech {

    Action getVisited(Set<String> nodes) {
        for (Action action : this) {
            // All actions must have an ID, otherwise Exception
            if (nodes.contains(((HasId) action).getId())) {
                return action;
            }
        }
        return get(0);
    }

    @Override
    public int getViewType() {
        return R.id.interactive_assistant_action_list_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return ActionListViewHolderBuilder.build();
    }

    @Override
    public void consumeRecognizer(SpeechRecognizer speechRecognizer,
                                  OnRecognized onRecognized) {
        speechRecognizer.listen((RecognizerListener.OnMostConfidentResult) result -> {
            for (Action action : this) {
                if (action instanceof CanCheckContentDescriptions) {
                    int matches = ((CanCheckContentDescriptions) action).matches(result);
                   if (matches == MATCHED) {
                       onRecognized.execute(action);
                       break;
                   } else if (matches == REPEAT) {
                       consumeRecognizer(speechRecognizer, onRecognized);
                       break;
                   }
                }
            }
        });
    }
}
