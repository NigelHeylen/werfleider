package nigel.com.werfleider.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import mortar.Mortar;
import mortar.MortarScope;
import mortar.Presenter;

/**
 * Created by nigel on 19/10/15.
 */
public class StartActivityForResultPresenter
    extends Presenter<StartActivityForResultPresenter.Activity> {

  private Config config;

  private ActivityResultListener activityResultListener;

  public static class Config {

    final Intent intent;

    final int requestCode;

    public Config(final Intent intent, final int requestCode) {

      this.intent = intent;

      this.requestCode = requestCode;
    }
  }

  @Override protected MortarScope extractScope(final Activity view) {

    return Mortar.getScope(view.getMortarContext());
  }

  public interface Activity {
    void startActivityForResult(Intent intent, int requestCode);

    Context getMortarContext();
  }

  public interface ActivityResultListener {

    void onActivityResult(final int requestCode, final int resultCode, final Intent data);
  }

  public final void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Make sure it's the expected requestCode and resultCode and do your thing.
    // If it isn't, no-op, someone else will handle it.

    if (activityResultListener != null) {
      activityResultListener.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override protected void onLoad(final Bundle savedInstanceState) {

    super.onLoad(savedInstanceState);
    if (getView() == null || config == null) return;

    update();
  }

  public void setConfig(final Config config) {

    this.config = config;
    update();
  }

  private void update() {

    getView().startActivityForResult(config.intent, config.requestCode);
  }

  public void setActivityResultListener(final ActivityResultListener activityResultListener) {

    this.activityResultListener = activityResultListener;
  }

  public void removeListener() {

    this.activityResultListener = null;
  }
}
