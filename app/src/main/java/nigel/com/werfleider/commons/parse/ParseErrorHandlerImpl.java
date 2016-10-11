package nigel.com.werfleider.commons.parse;

import android.content.Context;
import android.widget.Toast;
import com.parse.ParseException;
import flow.Flow;
import javax.inject.Inject;
import nigel.com.werfleider.ui.login.LoginScreen;

/**
 * Created by nigel on 25/06/16.
 */
public class ParseErrorHandlerImpl implements ParseErrorHandler {

  @Inject Flow flow;

  @Inject Context context;

  @Override public void handleParseError(ParseException ex) {

    ex.printStackTrace();
    switch (ex.getCode()) {
      case ParseException.INVALID_SESSION_TOKEN:
        flow.goTo(new LoginScreen());
        Toast.makeText(context, "Foutieve sessie token. Gelieve opnieuw aan te melden",
            Toast.LENGTH_LONG).show();
        break;
      default:
        break;
    }
  }
}
