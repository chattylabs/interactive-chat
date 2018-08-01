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


class UserAssistantFlow {

    private Context context;
    private UserAssistantComponent assistant;

    UserAssistantFlow(RecyclerView recyclerView, VoiceInteractionComponent voiceInteractionComponent) {
        this.context = recyclerView.getContext();
        assistant = new UserAssistantComponent.Builder()
                .withRecyclerView(recyclerView)
                .withVoiceInteraction(voiceInteractionComponent)
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
        Message welcome = new Message.Builder("welcome")
                .setText(getString(R.string.demo_welcome)).build();
        assistant.addNode(welcome);

        Node quiet_place = new Message.Builder("quiet_place")
                .setText(getString(R.string.demo_ask_for_quiet_place)).build();
        assistant.addNode(quiet_place);

        Node quiet_place_yes = new Action.Builder("quiet_place_yes")
                .setText1(getString(R.string.demo_yes))
                .setOnSelected(action -> assistant.enableVoiceInteraction(true))
                .build();
        assistant.addNode(quiet_place_yes);

        Node quiet_place_no = new Action.Builder("quiet_place_no")
                .setText1(getString(R.string.demo_no)).build();
        assistant.addNode(quiet_place_no);

        Node comeback_later = new Message.Builder("comeback_later")
                .setText(getString(R.string.demo_comeback_later)).build();
        assistant.addNode(comeback_later);

        Node select_icon = new Message.Builder("select_icon")
                .setText(getString(R.string.demo_select_icon)).build();
        assistant.addNode(select_icon);

        Node icon_1 = new Action.Builder("icon_1")
                .setImage(R.drawable.ic_sentiment_dissatisfied_black_24dp).build();
        Node icon_2 = new Action.Builder("icon_2")
                .setImage(R.drawable.ic_sentiment_neutral_black_24dp).build();
        Node icon_3 = new Action.Builder("icon_3")
                .setImage(R.drawable.ic_sentiment_satisfied_black_24dp).build();
        assistant.addNode(icon_1);
        assistant.addNode(icon_2);
        assistant.addNode(icon_3);

        Node done = new Message.Builder("done").setText(getString(R.string.demo_done)).build();
        assistant.addNode(done);

        Node liked_yes = new Action.Builder("liked_yes")
                .setText1(getString(R.string.demo_thumbs_up)).setTextSize(24).build();
        assistant.addNode(liked_yes);

        Node liked_no = new Action.Builder("liked_no")
                .setText1(getString(R.string.demo_thumbs_down)).setTextSize(24).build();
        assistant.addNode(liked_no);

        Flow flow = assistant.create();
        flow.from(welcome).to(quiet_place);
        flow.from(quiet_place).to(quiet_place_yes, quiet_place_no);
        flow.from(quiet_place_no).to(comeback_later);
        flow.from(quiet_place_yes).to(comeback_later);
        flow.from(select_icon).to(icon_1, icon_2, icon_3);
        flow.from(icon_1).to(done);
        flow.from(icon_2).to(done);
        flow.from(icon_3).to(done);
        flow.from(done).to(liked_yes, liked_no);

        return welcome;
    }

    private String getString(@StringRes int resId) {
        return context.getResources().getString(resId);
    }
}
