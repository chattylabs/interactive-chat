package chattylabs.assistant.demo;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.ArrayRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import chattylabs.assistant.ChoiceItem;
import chattylabs.assistant.ImageAction;
import chattylabs.assistant.MultiChoiceAction;
import chattylabs.assistant.TextAction;
import chattylabs.assistant.Flow;
import chattylabs.assistant.InteractiveAssistant;
import chattylabs.assistant.TextMessage;
import chattylabs.assistant.Node;
import chattylabs.conversations.ConversationalFlow;

@SuppressLint("MissingPermission")
class AssistantFlowBuilder {

    /*
     * These fields represent the ids of every node we will add into the graph
     */

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
    private static final String OPTION_4_ID = "OPTION_4_ID";

    private Context context;
    private InteractiveAssistant component;

    AssistantFlowBuilder(RecyclerView recyclerView,
                         ConversationalFlow voiceComponent) {

        context = recyclerView.getContext();

        component = new InteractiveAssistant.Builder()
                // Since you can customize the component, you must provide your own
                // RecyclerView implementation.
                // This is the only required element.
                .withViewComponent(recyclerView)
                // The following is an optional element.
                // You might choose to configure
                // the speech component using a specific addon.
                .withVoiceComponent(voiceComponent)
                // As we have provided an option in the menu to clear the assistant state
                // we can keep the state enabled, and demonstrate how it keeps track of the nodes.
                .withLastStateEnabled(true)
                .build();
    }

    public InteractiveAssistant create() {

        Flow flow = component.prepare();

        // The rootNode is the node where the flow should start.
        Node rootNode = build(flow);

        // We make sure we are setting up the proper speech component.
        // Once it is ready, we start the flow.
        component.setupSpeech(context, (synthesizerStatus, recognizerStatus) ->
                flow.start(rootNode));

        return component;
    }

    private Node build(Flow flow) {

        component.addNode(new TextMessage.Builder(WELCOME_ID)
                .setText(getString(R.string.demo_welcome)).build());

        component.addNode(new TextMessage.Builder(QUIET_PLACE_ID)
                .setText(getString(R.string.demo_ask_for_quiet_place)).build());

        component.addNode(new TextAction.Builder(QUIET_PLACE_YES_ID)
                .setText(getString(R.string.demo_yes))
                .setOnSelected(action -> {
                    component.enableSpeechSynthesizer(context, true);
                    component.enableSpeechRecognizer(context, true);
                })
                .build());

        component.addNode(new TextAction.Builder(QUIET_PLACE_NO_ID)
                .setText(getString(R.string.demo_no)).skipTracking(true).build());

        component.addNode(new TextMessage.Builder(COMEBACK_LATER_ID)
                .setText(getString(R.string.demo_comeback_later)).build());

        component.addNode(new TextMessage.Builder(EXPLANATION_1_ID)
                .setText(getString(R.string.demo_explanation_1)).build());

        component.addNode(new TextMessage.Builder(EXPLANATION_2_ID)
                .setText(getString(R.string.demo_explanation_2)).build());

        component.addNode(new TextAction.Builder(OK_ID)
                .setText(getString(R.string.demo_ok)).build());

        component.addNode(new TextMessage.Builder(MULTI_OPTIONS_MSG_ID)
                .setText(getString(R.string.demo_options_message)).build());

        component.addNode(new MultiChoiceAction.Builder(MULTI_OPTIONS_ID)
                .addOption(new ChoiceItem.Builder(OPTION_1_ID)
                        .setText(context.getString(R.string.demo_option_1))
                        .setOrder(2).build())
                .addOption(new ChoiceItem.Builder(OPTION_2_ID)
                        .setText(context.getString(R.string.demo_option_2))
                        .setOrder(3).build())
                .addOption(new ChoiceItem.Builder(OPTION_3_ID)
                        .setText(context.getString(R.string.demo_option_3))
                        .setOrder(1).build())
                .addOption(new ChoiceItem.Builder(OPTION_4_ID)
                        .setText(context.getString(R.string.demo_option_4))
                        .setOrder(4).build())
                .setConfirmationAction(new TextAction.Builder(OK_ID)
                        .setText(getString(R.string.demo_ok))
                        .setOnSelected(action -> {
                            final MultiChoiceAction multiAction = ((MultiChoiceAction) action);
                            // Iterate options from multiAction and check isSelected
                        })
                        .build())
                .build());

        component.addNode(new TextMessage.Builder(SELECT_ICON_1_ID)
                .setText(getString(R.string.demo_select_icon_1)).build());

        component.addNode(new TextMessage.Builder(SELECT_ICON_2_ID)
                .setText(getString(R.string.demo_select_icon_2)).build());

        component.addNode(new ImageAction.Builder(ICON_1_ID)
                .setImage(R.drawable.ic_sentiment_dissatisfied_black_24dp)
                .setContentDescriptions(getStringArray(R.array.dissatisfied)).build());
        component.addNode(new ImageAction.Builder(ICON_2_ID)
                .setImage(R.drawable.ic_sentiment_neutral_black_24dp)
                .setContentDescriptions(getStringArray(R.array.neutral)).build());
        component.addNode(new ImageAction.Builder(ICON_3_ID)
                .setImage(R.drawable.ic_sentiment_satisfied_black_24dp)
                .setContentDescriptions(getStringArray(R.array.satisfied)).build());

        component.addNode(new TextMessage.Builder(PART_OF_DAY_1_ID)
                .setText(getString(R.string.demo_part_of_day_1)).build());

        component.addNode(new TextMessage.Builder(PART_OF_DAY_2_ID)
                .setText(getString(R.string.demo_part_of_day_2)).build());

        component.addNode(new TextAction.Builder(MORNING_ID)
                .setText(getString(R.string.demo_morning)).build());
        component.addNode(new TextAction.Builder(NOON_ID)
                .setText(getString(R.string.demo_noon)).build());
        component.addNode(new TextAction.Builder(EVENING_ID)
                .setText(getString(R.string.demo_evening)).build());

        component.addNode(new TextMessage.Builder(DONE_ID)
                .setText(getString(R.string.demo_done)).build());

        component.addNode(new TextAction.Builder(LIKED_YES_ID)
                .setText(getString(R.string.demo_thumbs_up))
                .setContentDescriptions(getStringArray(R.array.thumbsup))
                .setTextSize(24).build());

        component.addNode(new TextAction.Builder(LIKED_NO_ID)
                .setText(getString(R.string.demo_thumbs_down))
                .setContentDescriptions(getStringArray(R.array.thumbsdown))
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

        return component.getNode(WELCOME_ID);
    }

    private String getString(@StringRes int resId) {
        return context.getResources().getString(resId);
    }

    private String[] getStringArray(@ArrayRes int resId) {
        return context.getResources().getStringArray(resId);
    }
}
