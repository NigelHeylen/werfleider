package nigel.com.werfleider.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import nigel.com.werfleider.R;

import static nigel.com.werfleider.util.StringUtils.capitalize;

/**
 * Created by nigel on 17/02/15.
 */
public enum DocumentType {

  PLAATSBESCHRIJF(R.string.tNoLocationsFound, R.drawable.ic_location_large), OPMERKINGEN(
      R.string.tNoRemarksFound, R.drawable.ic_notes), OPMETINGEN(R.string.tNoPostsFound,
      R.drawable.ic_calculations);

  private final int textRes;
  private final int drawableRes;

  DocumentType(@StringRes int textRes, @DrawableRes int drawableRes) {

    this.textRes = textRes;
    this.drawableRes = drawableRes;
  }

  @Override public String toString() {

    return capitalize(name().toLowerCase()).replace('_', ' ');
  }

  public int getTextRes() {
    return textRes;
  }

  public int getDrawableRes() {
    return drawableRes;
  }
}
