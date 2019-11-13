package chattylabs.assistant.demo;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.chattylabs.android.commons.PermissionsHelper;
import com.chattylabs.android.commons.ThreadUtils;
import chattylabs.assistant.InteractiveAssistant;

import javax.inject.Inject;

import chattylabs.conversations.ConversationalFlow;
import dagger.android.support.DaggerAppCompatActivity;

public class AssistantActivity extends DaggerAppCompatActivity {

    @Inject ConversationalFlow voiceComponent;
    private InteractiveAssistant chatComponent;
    private RecyclerView recyclerview;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /*
     * By taping on this option you clear the current state of the assistant, and this
     * activity is restarted.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                chatComponent.removeLastState();
                recreate();
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        recyclerview = findViewById(R.id.interactive_chat);

        // We need to request for the mic permission before to start the demo
        // Since this is a demo, to keep it simple we don't handle non given permissions
        String[] perms = voiceComponent.requiredPermissions();
        PermissionsHelper.check(this,
                perms,
                () -> onRequestPermissionsResult(
                        202, perms,
                        new int[] {PackageManager.PERMISSION_GRANTED}), 202);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Prefer to resume the assistant in onStart
        // This is to avoid unexpected behaviours with some system dialogs
        // that leaves the activity on an unknown activity focus state
        if (chatComponent != null)
            chatComponent.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Prefer to pause the assistant in onStop
        // This will also stop any current speech utterance
        if (chatComponent != null)
            chatComponent.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (202 == requestCode && PermissionsHelper.allGranted(grantResults)) {
            // Since the voice component needs to set up the speech engines, which might take some
            // microseconds, we want to make sure we run this on a separated Thread.
            ThreadUtils.newSerialThread().addTask(() -> {
                // The AssistantFlowBuilder is a helper class to separate the logic outside this activity
                chatComponent = new AssistantFlowBuilder(recyclerview, voiceComponent).create();
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // We will release any pending internal operation
        // and restart the component to its default values
        if (chatComponent != null)
            chatComponent.release();
    }
}
