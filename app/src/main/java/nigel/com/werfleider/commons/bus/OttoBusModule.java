package nigel.com.werfleider.commons.bus;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.squareup.otto.ThreadEnforcer.ANY;

/**
 * @author Thomas Moerman
 */
@Module(library = true)
public class OttoBusModule {

    /**
     * @return Returns a {@link com.squareup.otto.Bus} enforced to the main thread.
     */
    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus(ANY);
    }

    @Provides
    @Singleton BusGateway provideBusGateway(final AsyncBusGateway asyncBusGateway) {
        return asyncBusGateway;
    }

}