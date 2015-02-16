package nigel.com.werfleider.commons.bus;

/**
 * @author Thomas Moerman
 */
public interface BusGateway {

    /**
     * Identical to {@link com.squareup.otto.Bus#post(Object)}.
     * @param event
     */
    void post(Object event);

}