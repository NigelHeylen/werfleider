package nigel.com.werfleider.commons.bus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Thread safe access to the {@link com.squareup.otto.Bus} confined to the main thread.
 *
 * @author Thomas Moerman
 */
public class AsyncBusGateway implements BusGateway {

    @Inject
    Bus bus;

    final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Post the specified event on the injected {@link com.squareup.otto.Bus} instance safely from another thread
     * than the main UI thread.
     *
     * Use case: scheduled tasks that want to publish events consumable by presentation logic.
     *
     * @param event
     */
    @Override
    public void post(final Object event) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                bus.post(event);
            }
        });
    }

}