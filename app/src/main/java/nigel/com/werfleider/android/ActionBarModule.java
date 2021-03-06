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
package nigel.com.werfleider.android;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import nigel.com.werfleider.MainActivity;
import nigel.com.werfleider.core.ApplicationModule;

@Module(injects = MainActivity.class, addsTo = ApplicationModule.class)
public class ActionBarModule {
  @Provides @Singleton ActionBarOwner provideActionBarOwner() {
    return new ActionBarOwner();
  }

  @Provides @Singleton StartActivityForResultPresenter provideActivityForResultPresenter() {
    return new StartActivityForResultPresenter();
  }
}
