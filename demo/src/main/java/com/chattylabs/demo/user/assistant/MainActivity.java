package com.chattylabs.demo.user.assistant;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.chattylabs.android.user.interaction.ChatInteractionComponent;
import com.chattylabs.sdk.android.common.PermissionsHelper;
import com.chattylabs.sdk.android.common.ThreadUtils;
import com.chattylabs.sdk.android.voice.AndroidSpeechRecognizer;
import com.chattylabs.sdk.android.voice.AndroidSpeechSynthesizer;
import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject ConversationalFlowComponent voiceComponent;
    private ChatInteractionComponent assistantComponent;
    private RecyclerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        view = findViewById(R.id.interactive_chat);

        voiceComponent.updateConfiguration(builder -> {
            builder.setSynthesizerServiceType(() -> AndroidSpeechSynthesizer.class);
            builder.setRecognizerServiceType(() -> AndroidSpeechRecognizer.class);
            return builder.build();
        });

        String[] perms = voiceComponent.requiredPermissions();
        PermissionsHelper.check(this,
                perms,
                () -> onRequestPermissionsResult(
                        PermissionsHelper.REQUEST_CODE, perms,
                        new int[] {PackageManager.PERMISSION_GRANTED}));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (PermissionsHelper.isPermissionRequest(requestCode) &&
            PermissionsHelper.isPermissionGranted(grantResults)) {
            ThreadUtils.newSerialThread().addTask(() -> assistantComponent =
                            new ChatInteractionHelper(view, voiceComponent).create());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (assistantComponent != null) assistantComponent.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (assistantComponent != null) assistantComponent.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (assistantComponent != null) {
            assistantComponent.release();
            assistantComponent.reset();
        }
    }
}
