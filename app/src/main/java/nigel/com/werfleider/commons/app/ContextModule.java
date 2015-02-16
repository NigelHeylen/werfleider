package nigel.com.werfleider.commons.app;
import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Thomas Moerman
 */
@Module(library = true)
public class ContextModule {

    final Application application;

    /**
     * Module constructor, use an instance of this class instead of the class reference.
     * @param application
     */
    public ContextModule(final Application application) {
        this.application = application;
    }

    @Provides Application provideApplication() {
        return application;
    }

    @Provides Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides @Singleton Picasso providePablo(final Context context) { return Picasso.with(context); }

}