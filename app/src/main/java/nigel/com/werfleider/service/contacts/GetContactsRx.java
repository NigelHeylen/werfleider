package nigel.com.werfleider.service.contacts;

import nigel.com.werfleider.model.User;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by nigel on 13/12/15.
 */
public class GetContactsRx implements GetContacts {


  @Override public Observable<User> getUsers() {

    Observable.create(subscriber -> {

    })
    return null;
  }
}
