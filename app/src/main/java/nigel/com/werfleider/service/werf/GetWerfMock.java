package nigel.com.werfleider.service.werf;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.model.Werf;
import rx.Observable;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 31/01/15.
 */
public class GetWerfMock implements GetWerf {

    @Inject
    public GetWerfMock() {
    }



    @Override public Observable<List<Werf>> apply() {
        final List<Werf> mockList = newArrayList(new Werf("Nigel", "ldwqdqd"));

//        return Observable.empty();
        return Observable.just(mockList);
    }
}
