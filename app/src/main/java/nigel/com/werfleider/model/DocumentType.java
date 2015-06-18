package nigel.com.werfleider.model;

import static nigel.com.werfleider.util.StringUtils.*;

/**
 * Created by nigel on 17/02/15.
 */
public enum  DocumentType {

    AS_BUILT, OPMERKINGEN, OPMETINGEN;


    @Override public String toString() {

        return capitalize(name().toLowerCase()).replace(
                '_',
                ' ');
    }
}
