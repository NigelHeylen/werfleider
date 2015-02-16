package nigel.com.werfleider.dao.helper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nigel.com.werfleider.dao.contact.ContactDbHelper;
import nigel.com.werfleider.dao.contact.ContactDbHelperBean;
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfDbHelper;
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfDbHelperBean;
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfLocatieDbHelper;
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfLocatieDbHelperBean;
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfLocatieImageDbHelper;
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfLocatieImageDbHelperBean;

/**
 * Created by nigel on 07/02/15.
 */
@Module(
        library = true,
        complete = false
)
public class DatabaseModule {
    @Provides @Singleton
    DatabaseHelper provideDatabaseHelper(final DatabaseHelperBean databaseHelperBean){
        return databaseHelperBean;
    }


    @Provides PlaatsBeschrijfDbHelper providePlaatbeschrijfHelper(final PlaatsBeschrijfDbHelperBean plaatsBeschrijfDbHelperBean) {
        return plaatsBeschrijfDbHelperBean;
    }


    @Provides PlaatsBeschrijfLocatieImageDbHelper provideLocatieImageHelper(final PlaatsBeschrijfLocatieImageDbHelperBean plaatsBeschrijfLocatieImageDbHelperBean){
        return plaatsBeschrijfLocatieImageDbHelperBean;
    }

    @Provides PlaatsBeschrijfLocatieDbHelper provideLocatieHelper(final PlaatsBeschrijfLocatieDbHelperBean plaatsBeschrijfLocatieDbHelperBean){
        return plaatsBeschrijfLocatieDbHelperBean;
    }

    @Provides ContactDbHelper provideContactDbHelper(final ContactDbHelperBean contactDbHelper){
        return contactDbHelper;
    }
}
