package nigel.com.werfleider.ui.contact;

import com.parse.ParseUser;

/**
 * Created by nigel on 13/12/15.
 */
public class SocialAction {

  final ParseUser user;
  final Action unfollow;

  public SocialAction(ParseUser user, Action unfollow) {
    this.user = user;
    this.unfollow = unfollow;
  }

  public ParseUser getUser() {
    return user;
  }

  public Action getUnfollow() {
    return unfollow;
  }

  public enum Action {

    FOLLOW, UNFOLLOW
  }
}
