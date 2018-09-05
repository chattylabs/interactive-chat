package com.chattylabs.demo.user.assistant;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.android.user.interaction.ChatActionImage;
import com.chattylabs.android.user.interaction.ChatActionMultiOption;
import com.chattylabs.android.user.interaction.ChatActionOption;
import com.chattylabs.android.user.interaction.ChatActionText;
import com.chattylabs.android.user.interaction.ChatFlow;
import com.chattylabs.android.user.interaction.ChatInteractionComponent;
import com.chattylabs.android.user.interaction.ChatMessageText;
import com.chattylabs.android.user.interaction.ChatNode;
import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;

import net.hockeyapp.android.FeedbackManager;


class ChatInteractionHelper {

    private static final String WELCOME_ID = "WELCOME_ID";
    private static final String QUIET_PLACE_ID = "QUIET_PLACE_ID";
    private static final String QUIET_PLACE_YES_ID = "QUIET_PLACE_YES_ID";
    private static final String QUIET_PLACE_NO_ID = "QUIET_PLACE_NO_ID";
    private static final String COMEBACK_LATER_ID = "COMEBACK_LATER_ID";
    private static final String SELECT_ICON_1_ID = "SELECT_ICON_1_ID";
    private static final String SELECT_ICON_2_ID = "SELECT_ICON_2_ID";
    private static final String ICON_1_ID = "ICON_1_ID";
    private static final String ICON_2_ID = "ICON_2_ID";
    private static final String ICON_3_ID = "ICON_3_ID";
    private static final String DONE_ID = "DONE_ID";
    private static final String LIKED_YES_ID = "LIKED_YES_ID";
    private static final String LIKED_NO_ID = "LIKED_NO_ID";
    private static final String PART_OF_DAY_1_ID = "PART_OF_DAY_1_ID";
    private static final String PART_OF_DAY_2_ID = "PART_OF_DAY_2_ID";
    private static final String MORNING_ID = "MORNING_ID";
    private static final String NOON_ID = "NOON_ID";
    private static final String EVENING_ID = "EVENING_ID";
    private static final String EXPLANATION_1_ID = "EXPLANATION_1_ID";
    private static final String EXPLANATION_2_ID = "EXPLANATION_2_ID";
    private static final String OK_ID = "OK_ID";
    private static final String MULTI_OPTIONS_MSG_ID = "MULTI_OPTIONS_MSG_ID";
    private static final String MULTI_OPTIONS_ID = "MULTI_OPTIONS_ID";
    private static final String OPTION_1_ID = "OPTION_1_ID";
    private static final String OPTION_2_ID = "OPTION_2_ID";
    private static final String OPTION_3_ID = "OPTION_3_ID";
    private static final String OPTION_OK_ID = "OPTION_OK_ID";

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

        ChatFlow flow = assistant.create();
        ChatNode rootNode = buildFlow(flow);

        assistant.setupSpeech(context, status -> flow.start(rootNode));

        return assistant;
    }

    private ChatNode buildFlow(ChatFlow flow) {

        assistant.addNode(new ChatMessageText.Builder(WELCOME_ID)
                .setText(getString(R.string.demo_welcome)).build());

        assistant.addNode(new ChatMessageText.Builder(QUIET_PLACE_ID)
                .setText(getString(R.string.demo_ask_for_quiet_place)).build());

        assistant.addNode(new ChatActionText.Builder(QUIET_PLACE_YES_ID)
                .setText(getString(R.string.demo_yes))
                .setOnSelected(action -> {
                    assistant.enableSpeechSynthesizer(true);
                    assistant.enableSpeechRecognizer(true);
                })
                .build());

        assistant.addNode(new ChatActionText.Builder(QUIET_PLACE_NO_ID)
                .setText(getString(R.string.demo_no)).skipTracking(true).build());

        assistant.addNode(new ChatMessageText.Builder(COMEBACK_LATER_ID)
                .setText(getString(R.string.demo_comeback_later)).build());

        assistant.addNode(new ChatMessageText.Builder(EXPLANATION_1_ID)
                .setText(getString(R.string.demo_explanation_1)).build());

        assistant.addNode(new ChatMessageText.Builder(EXPLANATION_2_ID)
                .setText(getString(R.string.demo_explanation_2)).build());

        assistant.addNode(new ChatActionText.Builder(OK_ID)
                .setText(getString(R.string.demo_ok)).build());

        assistant.addNode(new ChatMessageText.Builder(MULTI_OPTIONS_MSG_ID)
                .setText(getString(R.string.demo_options_message)).build());

        assistant.addNode(new ChatActionMultiOption.Builder(MULTI_OPTIONS_ID)
                .addOption(new ChatActionOption.Builder(OPTION_1_ID)
                        .setText(context.getString(R.string.demo_option_1)).build())
                .addOption(new ChatActionOption.Builder(OPTION_2_ID)
                        .setText(context.getString(R.string.demo_option_2)).build())
                .addOption(new ChatActionOption.Builder(OPTION_3_ID)
                        .setText(context.getString(R.string.demo_option_3)).build())
                .addOption(new ChatActionOption.Builder(OPTION_OK_ID)
                        .setText(context.getString(R.string.demo_option_4)).build())
                .setConfirmationAction(new ChatActionText.Builder(OK_ID)
                        .setText(getString(R.string.demo_ok))
                        .setOnSelected(action -> {
                            final ChatActionMultiOption multiAction = ((ChatActionMultiOption) action);
                            // Iterate options from multiAction and check isSelected
                        })
                        .build())
                .build());

        assistant.addNode(new ChatMessageText.Builder(SELECT_ICON_1_ID)
                .setText(getString(R.string.demo_select_icon_1)).build());

        assistant.addNode(new ChatMessageText.Builder(SELECT_ICON_2_ID)
                .setText(getString(R.string.demo_select_icon_2)).build());

        assistant.addNode(new ChatActionImage.Builder(ICON_1_ID)
                .setImage(R.drawable.ic_sentiment_dissatisfied_black_24dp)
                .setContentDescriptions(getStringArray(R.array.dissatisfied)).build());
        assistant.addNode(new ChatActionImage.Builder(ICON_2_ID)
                .setImage(R.drawable.ic_sentiment_neutral_black_24dp)
                .setContentDescriptions(getStringArray(R.array.neutral)).build());
        assistant.addNode(new ChatActionImage.Builder(ICON_3_ID)
                .setImage(R.drawable.ic_sentiment_satisfied_black_24dp)
                .setContentDescriptions(getStringArray(R.array.satisfied)).build());

        assistant.addNode(new ChatMessageText.Builder(PART_OF_DAY_1_ID)
                .setText(getString(R.string.demo_part_of_day_1)).build());

        assistant.addNode(new ChatMessageText.Builder(PART_OF_DAY_2_ID)
                .setText(getString(R.string.demo_part_of_day_2)).build());

        assistant.addNode(new ChatActionText.Builder(MORNING_ID)
                .setText(getString(R.string.demo_morning)).build());
        assistant.addNode(new ChatActionText.Builder(NOON_ID)
                .setText(getString(R.string.demo_noon)).build());
        assistant.addNode(new ChatActionText.Builder(EVENING_ID)
                .setText(getString(R.string.demo_evening)).build());

        assistant.addNode(new ChatMessageText.Builder(DONE_ID)
                .setText(getString(R.string.demo_done)).build());

        assistant.addNode(new ChatActionText.Builder(LIKED_YES_ID)
                .setText(getString(R.string.demo_thumbs_up))
                .setContentDescriptions(getStringArray(R.array.thumbsup))
                .setOnSelected(action -> {
                    FeedbackManager.takeScreenshot(context);
                    FeedbackManager.showFeedbackActivity(context);
                })
                .setTextSize(24).build());

        assistant.addNode(new ChatActionText.Builder(LIKED_NO_ID)
                .setText(getString(R.string.demo_thumbs_down))
                .setContentDescriptions(getStringArray(R.array.thumbsdown))
                .setOnSelected(action -> {
                    FeedbackManager.takeScreenshot(context);
                    FeedbackManager.showFeedbackActivity(context);
                })
                .setTextSize(24).build());

        flow.from(WELCOME_ID).to(QUIET_PLACE_ID);
        flow.from(QUIET_PLACE_ID).to(QUIET_PLACE_YES_ID, QUIET_PLACE_NO_ID);
        flow.from(QUIET_PLACE_NO_ID).to(COMEBACK_LATER_ID);
        flow.from(QUIET_PLACE_YES_ID).to(EXPLANATION_1_ID);
        flow.from(EXPLANATION_1_ID).to(EXPLANATION_2_ID);
        flow.from(EXPLANATION_2_ID).to(OK_ID);
        flow.from(OK_ID).to(MULTI_OPTIONS_MSG_ID);
        flow.from(MULTI_OPTIONS_MSG_ID).to(MULTI_OPTIONS_ID);
        flow.from(MULTI_OPTIONS_ID).to(SELECT_ICON_1_ID);
        flow.from(SELECT_ICON_1_ID).to(SELECT_ICON_2_ID);
        flow.from(SELECT_ICON_2_ID).to(ICON_1_ID, ICON_2_ID, ICON_3_ID);
        flow.from(ICON_1_ID).to(PART_OF_DAY_1_ID);
        flow.from(ICON_2_ID).to(PART_OF_DAY_1_ID);
        flow.from(ICON_3_ID).to(PART_OF_DAY_1_ID);
        flow.from(PART_OF_DAY_1_ID).to(PART_OF_DAY_2_ID);
        flow.from(PART_OF_DAY_2_ID).to(MORNING_ID, NOON_ID, EVENING_ID);
        flow.from(MORNING_ID).to(DONE_ID);
        flow.from(NOON_ID).to(DONE_ID);
        flow.from(EVENING_ID).to(DONE_ID);
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
