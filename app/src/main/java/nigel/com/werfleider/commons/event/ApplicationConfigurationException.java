package nigel.com.werfleider.commons.event;


/**
 * @author Thomas Moerman
 */
public class ApplicationConfigurationException extends RuntimeException {

    public ApplicationConfigurationException(final String message) {
        super(message);
    }

    public ApplicationConfigurationException(final String message, final Exception e) {
        super(message, e);
    }

}