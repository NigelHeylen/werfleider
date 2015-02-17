package nigel.com.werfleider.service.werf;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.model.Werf;
import rx.Observable;

/**
 * Created by nigel on 31/01/15.
 */
public class GetWerfMock implements GetWerf {

    @Inject
    public GetWerfMock() {
    }



    @Override public Observable<List<Werf>> apply() {

//        return Observable.empty();
        return Observable.empty();
    }
}
