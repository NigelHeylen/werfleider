package nigel.com.werfleider.ui.presenter;

import android.view.View;
import mortar.ViewPresenter;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nigel on 13/12/15.
 */
public class ReactiveViewPresenter<V extends View> extends ViewPresenter<V>{

  private CompositeSubscription subscription = new CompositeSubscription();

  @Override protected void onExitScope() {
    super.onExitScope();

    unsubscribe();
  }

  public void unsubscribe() {
    subscription.unsubscribe();
    subscription = new CompositeSubscription();
  }

  protected void subscribe(final Subscription newSubscription){

    subscription.add(newSubscription);
  }
}
