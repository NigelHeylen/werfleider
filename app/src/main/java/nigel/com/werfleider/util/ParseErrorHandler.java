package nigel.com.werfleider.util;

import android.content.Context;
import android.widget.Toast;
import com.parse.ParseException;
import flow.Flow;
import nigel.com.werfleider.ui.login.LoginScreen;

/**
 * Created by nigel on 25/06/16.
 */
public class ParseErrorHandler {

  public static void handleParseError(ParseException ex, Flow flow, final Context context){

    switch (ex.getCode()){
      case ParseException.INVALID_SESSION_TOKEN : flow.goTo(new LoginScreen());
        Toast.makeText(context, "Foutieve sessie token. Gelieve opnieuw aan te melden", Toast.LENGTH_LONG).show();
        break;
      default: break;
    }
  }
}
