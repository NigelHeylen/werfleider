package nigel.com.werfleider.util;

import flow.Flow;
import mortar.Blueprint;

/**
 * Created by nigel on 15/06/16.
 */
public class FlowUtils {

  public static Blueprint getCurrentScreen(final Flow flow){

    return (Blueprint) flow.getBackstack().current().getScreen();
  }
}