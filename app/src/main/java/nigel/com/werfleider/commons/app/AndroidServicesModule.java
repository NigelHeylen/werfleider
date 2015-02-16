package nigel.com.werfleider.commons.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.LOCATION_SERVICE;

/**
 * @author Thomas Moerman
 */
@Module(
        complete = false,
        library = true)
public class AndroidServicesModule {

    @Provides
    public Resources provideResources(final Context context) {
        return context.getResources();
    }

    @Provides
    public AssetManager provideAssetManager(final Resources resources) {
        return resources.getAssets();
    }

    @Provides
    public LocationManager provideLocationManager(final Context context) {
        return (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    @Provides
    public SharedPreferences provideDefaultSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}