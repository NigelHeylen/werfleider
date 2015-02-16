package nigel.com.werfleider.commons.core;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import dagger.ObjectGraph;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * @author Thomas Moerman
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Inject protected Bus bus;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        getGraph().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        bus.unregister(this);
    }

    protected BaseApplication getBaseApplication() {
        return (BaseApplication) getApplication();
    }

    public ObjectGraph getGraph() {
        return getBaseApplication().getGraph();
    }

    protected Bus getBus() {
        return bus;
    }

    public DisplayMetrics getDisplayMetrics() {
        final DisplayMetrics displaymetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        return displaymetrics;
    }

    public int getOrientation() {
        return getResources().getConfiguration().orientation;
    }

    public boolean isOrientationPortrait() {
        return getOrientation() == ORIENTATION_PORTRAIT;
    }

    public boolean isOrientationLandscape() {
        return getOrientation() == ORIENTATION_LANDSCAPE;
    }

}