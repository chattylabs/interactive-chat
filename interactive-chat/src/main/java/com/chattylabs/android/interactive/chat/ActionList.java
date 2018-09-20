package com.chattylabs.android.interactive.chat;

import com.chattylabs.sdk.android.voice.RecognizerListener;
import com.chattylabs.sdk.android.voice.SpeechRecognizerComponent;

import java.util.ArrayList;
import java.util.Set;

import static com.chattylabs.android.interactive.chat.CanCheckContentDescriptions.*;

class ActionList extends ArrayList<Action> implements InteractiveChatNode, HasViewType, CanRecognizeSpeech {

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
        return R.id.interactive_chat_action_list_view_type;
    }

    @Override
    public ViewHolderBuilder getViewHolderBuilder() {
        return ActionListViewHolderBuilder.build();
    }

    @Override
    public void consumeRecognizer(SpeechRecognizerComponent component,
                                  OnRecognized onRecognized) {
        component.listen((RecognizerListener.OnMostConfidentResult) result -> {
            for (Action action : this) {
                if (action instanceof CanCheckContentDescriptions) {
                    int matches = ((CanCheckContentDescriptions) action).matches(result);
                   if (matches == MATCHED) {
                       onRecognized.execute(action);
                       break;
                   } else if (matches == REPEAT) {
                       consumeRecognizer(component, onRecognized);
                       break;
                   }
                }
            }
        });
    }
}
