package nigel.com.werfleider.service.contacts;

import nigel.com.werfleider.model.User;
import rx.Observable;

/**
 * Created by nigel on 13/12/15.
 */
public interface GetContacts {

  Observable<User> getUsers();
}
