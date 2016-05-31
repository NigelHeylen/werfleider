package nigel.com.werfleider.model;

import com.google.common.base.Strings;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.SUB_LOCATIONS;

/**
 * Created by nigel on 29/05/16.
 */
@ParseClassName("WerfLocatie")
public class Location extends ParseObject {

  public String getName(){
    return Strings.nullToEmpty(getString(NAME));
  }

  public void setName(final String name){
    put(NAME, name);
  }

  public List<SubLocation> getSubLocations(){
    return getList(SUB_LOCATIONS) != null ? getList(SUB_LOCATIONS) : newArrayList();
  }

  public void setSubLocations(List<SubLocation> subLocations) {
    put(SUB_LOCATIONS, subLocations);
  }
}
