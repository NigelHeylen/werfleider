/*
 * Copyright 2013 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nigel.com.werfleider.core;

import android.os.Parcelable;
import android.util.SparseArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dagger.Module;
import dagger.Provides;
import flow.Parcer;
import java.lang.reflect.Type;
import javax.inject.Singleton;
import nigel.com.werfleider.MainApplication;
import nigel.com.werfleider.android.SparseArrayTypeAdapter;
import nigel.com.werfleider.commons.app.AndroidServicesModule;
import nigel.com.werfleider.commons.app.ContextModule;
import nigel.com.werfleider.commons.bus.OttoBusModule;

@Module(includes = {
    ContextModule.class, OttoBusModule.class, AndroidServicesModule.class
},
    library = true,
    injects = MainApplication.class) public class ApplicationModule {

  @Provides @Singleton Gson provideGson() {
    Type sparseArrayType = new TypeToken<SparseArray<Parcelable>>() {
    }.getType();
    return new GsonBuilder().registerTypeAdapter(sparseArrayType,
        new SparseArrayTypeAdapter<>(Parcelable.class)).create();
  }

  @Provides @Singleton Parcer<Object> provideParcer(Gson gson) {
    return new GsonParcer<>(gson);
  }
}
