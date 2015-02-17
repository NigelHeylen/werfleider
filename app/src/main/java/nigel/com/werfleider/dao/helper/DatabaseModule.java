package nigel.com.werfleider.dao.helper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nigel.com.werfleider.dao.contact.ContactDbHelper;
import nigel.com.werfleider.dao.contact.ContactDbHelperBean;
import nigel.com.werfleider.dao.document.DocumentDbHelper;
import nigel.com.werfleider.dao.document.DocumentDbHelperBean;
import nigel.com.werfleider.dao.document.DocumentLocatieDbHelper;
import nigel.com.werfleider.dao.document.DocumentLocatieDbHelperBean;
import nigel.com.werfleider.dao.document.DocumentLocatieImageDbHelper;
import nigel.com.werfleider.dao.document.DocumentLocatieImageDbHelperBean;

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


    @Provides DocumentDbHelper providePlaatbeschrijfHelper(final DocumentDbHelperBean plaatsBeschrijfDbHelperBean) {
        return plaatsBeschrijfDbHelperBean;
    }


    @Provides DocumentLocatieImageDbHelper provideLocatieImageHelper(final DocumentLocatieImageDbHelperBean documentLocatieImageDbHelperBean){
        return documentLocatieImageDbHelperBean;
    }

    @Provides DocumentLocatieDbHelper provideLocatieHelper(final DocumentLocatieDbHelperBean plaatsBeschrijfLocatieDbHelperBean){
        return plaatsBeschrijfLocatieDbHelperBean;
    }

    @Provides ContactDbHelper provideContactDbHelper(final ContactDbHelperBean contactDbHelper){
        return contactDbHelper;
    }
}
