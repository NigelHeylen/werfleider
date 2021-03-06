package nigel.com.werfleider.service.contacts;

import com.parse.ParseUser;
import java.util.List;
import javax.inject.Inject;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import rx.Observable;

/**
 * Created by nigel on 13/12/15.
 */
public class GetContactsRx implements GetContacts {

  @Inject ParseErrorHandler parseErrorHandler;

  @Override public Observable<List<ParseUser>> getUsers() {

      return Observable.create(subscriber -> ParseUser.getQuery().findInBackground((users, e) -> {

        if (e != null) {

          subscriber.onError(e);
          parseErrorHandler.handleParseError(e);
        } else {

          subscriber.onNext(users);
        }

        subscriber.onCompleted();
      }));
  }

}
