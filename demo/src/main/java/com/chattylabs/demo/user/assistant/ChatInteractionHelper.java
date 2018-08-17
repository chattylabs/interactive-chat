package com.chattylabs.demo.user.assistant;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.android.user.interaction.ChatAction;
import com.chattylabs.android.user.interaction.ChatFlow;
import com.chattylabs.android.user.interaction.ChatInteractionComponent;
import com.chattylabs.android.user.interaction.ChatMessage;
import com.chattylabs.android.user.interaction.ChatNode;
import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;

import net.hockeyapp.android.FeedbackManager;


class ChatInteractionHelper {

    private static final String WELCOME_ID = "welcome";
    private static final String QUIET_PLACE_ID = "quiet_place";
    private static final String QUIET_PLACE_YES_ID = "quiet_place_yes";
    private static final String QUIET_PLACE_NO_ID = "quiet_place_no";
    private static final String COMEBACK_LATER_ID = "comeback_later";
    private static final String SELECT_ICON_ID = "select_icon";
    private static final String ICON_1_ID = "icon_1";
    private static final String ICON_2_ID = "icon_2";
    private static final String ICON_3_ID = "icon_3";
    private static final String DONE_ID = "done";
    private static final String LIKED_YES_ID = "liked_yes";
    private static final String LIKED_NO_ID = "liked_no";

    private Context context;
    private ChatInteractionComponent assistant;

    ChatInteractionHelper(RecyclerView recyclerView,
                          ConversationalFlowComponent voiceComponent) {
        this.context = recyclerView.getContext();
        assistant = new ChatInteractionComponent.Builder()
                .withViewComponent(recyclerView)
                .withVoiceComponent(voiceComponent)
                .build();
    }

    public ChatInteractionComponent create() {

        ChatNode rootNode = buildFlow();

        assistant.setupSpeech(context, status -> assistant.init(rootNode));

        return assistant;
    }

    private ChatNode buildFlow() {
        assistant.addNode(new ChatMessage.Builder(WELCOME_ID)
                .setText(getString(R.string.demo_welcome))
                .setOnLoaded(() ->
                        FeedbackManager.setActivityForScreenshot((Activity) context)).build());

        assistant.addNode(new ChatMessage.Builder(QUIET_PLACE_ID)
                .setText(getString(R.string.demo_ask_for_quiet_place)).build());

        assistant.addNode(new ChatAction.Builder(QUIET_PLACE_YES_ID)
                .setText(getString(R.string.demo_yes))
                .setOnSelected(action -> {
                    assistant.enableSpeechSynthesizer(true);
                    assistant.enableSpeechRecognizer(true);
                })
                .build());

        assistant.addNode(new ChatAction.Builder(QUIET_PLACE_NO_ID)
                .setText(getString(R.string.demo_no)).build());

        assistant.addNode(new ChatMessage.Builder(COMEBACK_LATER_ID)
                .setText(getString(R.string.demo_comeback_later)).build());

        assistant.addNode(new ChatMessage.Builder(SELECT_ICON_ID)
                .setText(getString(R.string.demo_select_icon)).build());

        assistant.addNode(new ChatAction.Builder(ICON_1_ID)
                .setImage(R.drawable.ic_sentiment_dissatisfied_black_24dp)
                .setContentDescriptions(getStringArray(R.array.dissatisfied)).build());
        assistant.addNode(new ChatAction.Builder(ICON_2_ID)
                .setImage(R.drawable.ic_sentiment_neutral_black_24dp)
                .setContentDescriptions(getStringArray(R.array.neutral)).build());
        assistant.addNode(new ChatAction.Builder(ICON_3_ID)
                .setImage(R.drawable.ic_sentiment_satisfied_black_24dp)
                .setContentDescriptions(getStringArray(R.array.satisfied)).build());

        assistant.addNode(new ChatMessage.Builder(DONE_ID)
                .setText(getString(R.string.demo_done))
                .setOnLoaded(() ->
                        FeedbackManager.setActivityForScreenshot((Activity) context)).build());

        assistant.addNode(new ChatAction.Builder(LIKED_YES_ID)
                .setText(getString(R.string.demo_thumbs_up))
                .setContentDescriptions(getStringArray(R.array.thumbsup))
                .setTextSize(24).build());

        assistant.addNode(new ChatAction.Builder(LIKED_NO_ID)
                .setText(getString(R.string.demo_thumbs_down))
                .setContentDescriptions(getStringArray(R.array.thumbsdown))
                .setTextSize(24).build());

        ChatFlow flow = assistant.create();
        flow.from(WELCOME_ID).to(QUIET_PLACE_ID);
        flow.from(QUIET_PLACE_ID).to(QUIET_PLACE_YES_ID, QUIET_PLACE_NO_ID);
        flow.from(QUIET_PLACE_NO_ID).to(COMEBACK_LATER_ID);
        flow.from(QUIET_PLACE_YES_ID).to(SELECT_ICON_ID);
        flow.from(SELECT_ICON_ID).to(ICON_1_ID, ICON_2_ID, ICON_3_ID);
        flow.from(ICON_1_ID).to(DONE_ID);
        flow.from(ICON_2_ID).to(DONE_ID);
        flow.from(ICON_3_ID).to(DONE_ID);
        flow.from(DONE_ID).to(LIKED_YES_ID, LIKED_NO_ID);

        return assistant.getNode(WELCOME_ID);
    }

    private String getString(@StringRes int resId) {
        return context.getResources().getString(resId);
    }

    private String[] getStringArray(@ArrayRes int resId) {
        return context.getResources().getStringArray(resId);
    }
}
