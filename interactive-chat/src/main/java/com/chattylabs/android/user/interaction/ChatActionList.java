package com.chattylabs.android.user.interaction;

import android.support.annotation.NonNull;

import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;
import com.chattylabs.sdk.android.voice.RecognizerListener;
import com.chattylabs.sdk.android.voice.SpeechRecognizerComponent;

import java.util.ArrayList;
import java.util.Set;

import static com.chattylabs.android.user.interaction.CanCheckContentDescriptions.*;

class ChatActionList extends ArrayList<ChatAction> implements ChatNode, HasViewType, CanRecognize {

    ChatAction getVisited(Set<String> nodes) {
        for (ChatAction action : this) {
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
    public ChatViewHolderBuilder getViewHolderBuilder() {
        return ChatActionListViewHolderBuilder.build();
    }

    @Override
    public void consumeRecognizer(SpeechRecognizerComponent component,
                                  OnRecognized onRecognized) {
        component.listen((RecognizerListener.OnMostConfidentResult) result -> {
            for (ChatAction action : this) {
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
