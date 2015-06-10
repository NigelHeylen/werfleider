package nigel.com.werfleider.model;

import static org.apache.commons.lang3.StringUtils.capitalize;

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
