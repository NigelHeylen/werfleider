package nigel.com.werfleider.service.contacts;

import com.parse.ParseUser;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

import static nigel.com.werfleider.ui.contact.Contacts.ALL_USERS;
import static nigel.com.werfleider.util.ParseStringUtils.CONTACTS;

/**
 * Created by nigel on 13/12/15.
 */
public class GetContactsRx implements GetContacts {

  @Inject public GetContactsRx() {
  }

  @Override public Observable<List<ParseUser>> getUsers() {

      return Observable.create(subscriber -> ParseUser.getQuery().findInBackground((users, e) -> {

        if (e != null) {

          subscriber.onError(e);
        } else {

          subscriber.onNext(users);
        }

        subscriber.onCompleted();
      }));
  }
}
