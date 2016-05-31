package nigel.com.werfleider.model;

import com.google.common.base.Strings;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import static nigel.com.werfleider.util.ParseStringUtils.NAME;

/**
 * Created by nigel on 29/05/16.
 */
@ParseClassName("WerfSubLocatie")
public class SubLocation extends ParseObject {

  public String getName(){
    return Strings.nullToEmpty(getString(NAME));
  }

  public void setName(final String name){
    put(NAME, name);
  }
}
