/*
 * Copyright 2013 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nigel.com.werfleider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import flow.Flow;
import javax.inject.Inject;
import mortar.Mortar;
import mortar.MortarActivityScope;
import mortar.MortarScope;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.core.MainView;
import nigel.com.werfleider.model.CreateImage;
import nigel.com.werfleider.model.EndCameraEvent;
import nigel.com.werfleider.model.StartCameraEvent;

import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.CATEGORY_LAUNCHER;
import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;
import static nigel.com.werfleider.util.MediaFileHandler.MEDIA_TYPE_IMAGE;
import static nigel.com.werfleider.util.MediaFileHandler.getOutputMediaFileUri;


public class MainActivity extends AppCompatActivity implements ActionBarOwner.View {
    @Inject Bus bus;

    private MortarActivityScope activityScope;
    private ActionBarOwner.MenuAction actionBarMenuAction;

    @Inject ActionBarOwner actionBarOwner;
    private Flow mainFlow;
    private int imageLocationIndex;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isWrongInstance()) {
            finish();
            return;
        }

        MortarScope parentScope = Mortar.getScope(getApplication());

        activityScope = Mortar.requireActivityScope(parentScope, new CorePresenter());
        Mortar.inject(this, this);

        activityScope.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainView coreView = ButterKnife.findById(this, R.id.main);

        mainFlow = coreView.getFlow();

        Toolbar toolbar;
        toolbar = coreView.getToolbar();

        setSupportActionBar(toolbar);

        actionBarOwner.takeView(this);

        bus.register(this);

    }

    @Override public Object getSystemService(String name) {
        if (Mortar.isScopeSystemService(name)) {
            return activityScope;
        }
        return super.getSystemService(name);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        activityScope.onSaveInstanceState(outState);
    }

    /**
     * Inform the view about back events.
     */
    @Override public void onBackPressed() {
        // Give the view a chance to handle going back. If it declines the honor, let super do its thing.
        if (!mainFlow.goBack()) {
            super.onBackPressed();
        }
    }

    /**
     * Inform the view about up events.
     */
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return mainFlow.goUp();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Configure the action bar menu as required by {@link ActionBarOwner.View}.
     */
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        if (actionBarMenuAction != null) {
            menu.add(actionBarMenuAction.title)
                .setShowAsActionFlags(SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            @Override public boolean onMenuItemClick(MenuItem menuItem) {
                                actionBarMenuAction.action.call();
                                return true;
                            }
                        });
        }
        return true;
    }

    @Override protected void onDestroy() {
        super.onDestroy();

        actionBarOwner.dropView(this);

        // activityScope may be null in case isWrongInstance() returned true in onCreate()
        if (isFinishing() && activityScope != null) {
            MortarScope parentScope = Mortar.getScope(getApplication());
            parentScope.destroyChild(activityScope);
            activityScope = null;
        }
    }

    @Override public Context getMortarContext() {
        return this;
    }

    @Override public void setShowHomeEnabled(boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override public void setUpButtonEnabled(boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(enabled);
        actionBar.setHomeButtonEnabled(enabled);
    }

    @Override public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override public void setMenu(ActionBarOwner.MenuAction action) {
        if (action != actionBarMenuAction) {
            actionBarMenuAction = action;
            invalidateOptionsMenu();
        }
    }


    /**
     * Dev tools and the play store (and others?) launch with a different intent, and so
     * lead to a redundant instance of this activity being spawned. <a
     * href="http://stackoverflow.com/questions/17702202/find-out-whether-the-current-activity-will-be-task-root-eventually-after-pendin"
     * >Details</a>.
     */
    private boolean isWrongInstance() {
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            boolean isMainAction = intent.getAction() != null && intent.getAction().equals(ACTION_MAIN);
            return intent.hasCategory(CATEGORY_LAUNCHER) && isMainAction;
        }
        return false;
    }

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent

                Toast.makeText(this, "Image saved to:\n" +
                                       currentUri, Toast.LENGTH_LONG).show();

                bus.post(new CreateImage(currentUri, imageLocationIndex));
                takePicture();
            } else if (resultCode == RESULT_CANCELED) {

                bus.post(new EndCameraEvent());
            } else {
                // Image capture failed, advise user
            }
        }
    }

    private String currentUri;

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        currentUri = fileUri.getPath();

        startActivityForResult(
                intent,
                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
                );
    }

    @Subscribe public void reactToCameraEvent(final StartCameraEvent startCameraEvent){
        this.imageLocationIndex = startCameraEvent.position;
        takePicture();
    }

}
