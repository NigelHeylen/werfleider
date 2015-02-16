package nigel.com.werfleider.service.werf;

import java.util.List;

import nigel.com.werfleider.model.Werf;
import rx.Observable;

/**
 * Created by nigel on 31/01/15.
 */
public interface GetWerf {

    Observable<List<Werf>> apply();
}
