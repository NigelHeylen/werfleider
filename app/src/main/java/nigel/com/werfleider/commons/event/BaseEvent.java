package nigel.com.werfleider.commons.event;

import org.joda.time.DateTime;

import static java.lang.String.format;
import static org.joda.time.DateTime.now;

/**
 * @author Thomas Moerman
 */
public abstract class BaseEvent {

    private final DateTime created = now();

    public DateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return format("%s(%s)", getClass().getSimpleName(), created);
    }

}