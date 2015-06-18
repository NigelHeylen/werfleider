package nigel.com.werfleider.util;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by nigel on 11/06/15.
 */
public class StringUtils {

    public static final String EMPTY = "";

    public static final int INDEX_NOT_FOUND = -1;

    public static String capitalize(final String string) {

        if (string == null) {
            throw new NullPointerException("string");
        }
        if (string.equals("")) {
            throw new NullPointerException("string");
        }

        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    public static String substringAfterLast(String str, String separator) {

        if (isNullOrEmpty(str)) {
            return str;
        }
        if (isNullOrEmpty(separator)) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND || pos == (str.length() - separator.length())) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    public static String emptyToOne(final String string){

        if(isNullOrEmpty(string)) return "1";
        return string;
    }
}
