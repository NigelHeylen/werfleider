package nigel.com.werfleider.commons.parse;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nigel on 25/06/16.
 */
@Module(library = true, complete = false) public class ErrorHandlerModule {

  @Provides ParseErrorHandler parseErrorHandler(final ParseErrorHandlerImpl errorHandler){
    return errorHandler;
  }
}
