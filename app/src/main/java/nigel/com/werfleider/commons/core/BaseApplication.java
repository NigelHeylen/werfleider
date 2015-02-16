package nigel.com.werfleider.commons.core;

import android.app.Application;
import android.content.res.Configuration;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import dagger.ObjectGraph;
import nigel.com.werfleider.commons.app.ContextModule;
import nigel.com.werfleider.commons.event.ApplicationConfigurationChanged;
import nigel.com.werfleider.commons.event.ApplicationConfigurationException;

import static com.google.common.collect.Lists.asList;

/**
 * @author Thomas Moerman
 */
public abstract class BaseApplication extends Application {

    @Inject Bus bus;

    private ObjectGraph graph;

    public BaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        graph = ObjectGraph.create(calculateModules());

        graph.inject(this);

        bus.register(this);
    }

    private final Object[] calculateModules() {
        if (getApplicationModules() == null) {
            throw new ApplicationConfigurationException("applicaton modules cannot be null");
        }

        return
                asList(
                        new ContextModule(this),
                        getApplicationModules()).toArray();
    }

    /**
     * @return Returns an optional array modules used for instantiating the activity scoped
     * {@link dagger.ObjectGraph}, which embeds the application scoped {@link dagger.ObjectGraph}.
     */
    protected Object[] getApplicationModules() {
        return new Object[] {};
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        bus.post(new ApplicationConfigurationChanged());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        bus.post(new ApplicationTerminated());
    }

    protected ObjectGraph getGraph() {
        return graph;
    }

    protected Bus getBus() {
        return bus;
    }

}