package nigel.com.werfleider.service.contacts;

import com.parse.ParseUser;
import java.util.List;
import nigel.com.werfleider.model.ParseYard;
import rx.Observable;

/**
 * Created by nigel on 13/12/15.
 */
public interface GetContacts {

  Observable<List<ParseUser>> getUsers();

}
