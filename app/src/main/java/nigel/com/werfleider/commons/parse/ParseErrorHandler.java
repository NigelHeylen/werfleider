package nigel.com.werfleider.commons.parse;

import com.parse.ParseException;

/**
 * Created by nigel on 25/06/16.
 */
public interface ParseErrorHandler {

  public void handleParseError(ParseException ex);
}
