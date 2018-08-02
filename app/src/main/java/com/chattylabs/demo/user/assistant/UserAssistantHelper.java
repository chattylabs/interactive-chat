package com.chattylabs.demo.user.assistant;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.android.user.interaction.Action;
import com.chattylabs.android.user.interaction.Flow;
import com.chattylabs.android.user.interaction.Message;
import com.chattylabs.android.user.interaction.Node;
import com.chattylabs.android.user.interaction.UserAssistantComponent;
import com.chattylabs.sdk.android.voice.VoiceInteractionComponent;


class UserAssistantHelper {

    static final String WELCOME = "welcome";
    static final String QUIET_PLACE = "quiet_place";
    static final String QUIET_PLACE_YES = "quiet_place_yes";
    static final String QUIET_PLACE_NO = "quiet_place_no";
    static final String COMEBACK_LATER = "comeback_later";
    static final String SELECT_ICON = "select_icon";
    static final String ICON_1 = "icon_1";
    static final String ICON_2 = "icon_2";
    static final String ICON_3 = "icon_3";
    static final String DONE = "done";
    static final String LIKED_YES = "liked_yes";
    static final String LIKED_NO = "liked_no";

    private Context context;
    private UserAssistantComponent assistant;

    UserAssistantHelper(RecyclerView recyclerView,
                        VoiceInteractionComponent voiceInteractionComponent) {
        this.context = recyclerView.getContext();
        assistant = new UserAssistantComponent.Builder()
                .withViewComponent(recyclerView)
                .withVoiceComponent(voiceInteractionComponent)
                .build();
    }

    public UserAssistantComponent create() {

        Node rootNode = buildFlow();

        assistant.prepareForVoiceInteraction(context, successCode -> {
            assistant.initialize(rootNode);
        }, errorCode -> {});

        return assistant;
    }

    private Node buildFlow() {
        assistant.addNode(new Message.Builder(WELCOME)
                .setText(getString(R.string.demo_welcome)).build());

        assistant.addNode(new Message.Builder(QUIET_PLACE)
                .setText(getString(R.string.demo_ask_for_quiet_place)).build());

        assistant.addNode(new Action.Builder(QUIET_PLACE_YES)
                .setText1(getString(R.string.demo_yes))
                .setOnSelected(action -> assistant.enableVoiceInteraction(true))
                .build());

        assistant.addNode(new Action.Builder(QUIET_PLACE_NO)
                .setText1(getString(R.string.demo_no)).build());

        assistant.addNode(new Message.Builder(COMEBACK_LATER)
                .setText(getString(R.string.demo_comeback_later)).build());

        assistant.addNode(new Message.Builder(SELECT_ICON)
                .setText(getString(R.string.demo_select_icon)).build());

        assistant.addNode(new Action.Builder(ICON_1)
                .setImage(R.drawable.ic_sentiment_dissatisfied_black_24dp).build());
        assistant.addNode(new Action.Builder(ICON_2)
                .setImage(R.drawable.ic_sentiment_neutral_black_24dp).build());
        assistant.addNode(new Action.Builder(ICON_3)
                .setImage(R.drawable.ic_sentiment_satisfied_black_24dp).build());

        assistant.addNode(new Message.Builder(DONE).setText(getString(R.string.demo_done)).build());

        assistant.addNode(new Action.Builder(LIKED_YES)
                .setText1(getString(R.string.demo_thumbs_up)).setTextSize(24).build());

        assistant.addNode(new Action.Builder(LIKED_NO)
                .setText1(getString(R.string.demo_thumbs_down)).setTextSize(24).build());

        Flow flow = assistant.create();
        flow.from(WELCOME).to(QUIET_PLACE);
        flow.from(QUIET_PLACE).to(QUIET_PLACE_YES, QUIET_PLACE_NO);
        flow.from(QUIET_PLACE_NO).to(COMEBACK_LATER);
        flow.from(QUIET_PLACE_YES).to(SELECT_ICON);
        flow.from(SELECT_ICON).to(ICON_1, ICON_2, ICON_3);
        flow.from(ICON_1).to(DONE);
        flow.from(ICON_2).to(DONE);
        flow.from(ICON_3).to(DONE);
        flow.from(DONE).to(LIKED_YES, LIKED_NO);

        return assistant.getNode(WELCOME);
    }

    private String getString(@StringRes int resId) {
        return context.getResources().getString(resId);
    }
}
