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
import com.parse.ParseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Parcer;
import mortar.Blueprint;
import nigel.com.werfleider.android.ActionBarModule;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.ui.home.HomeScreen;
import nigel.com.werfleider.ui.login.LoginScreen;
import nigel.com.werfleider.util.FlowOwner;

public class CorePresenter implements Blueprint {

  @Override public String getMortarScopeName() {
    return getClass().getName();
  }

  @Override public Object getDaggerModule() {
    return new Module();
  }

  @dagger.Module( //
      includes = ActionBarModule.class,
      injects = MainView.class,
      addsTo = ApplicationModule.class, //
      library = true //
  )
  public static class Module {
    @Provides @MainScope Flow provideFlow(final Presenter corePresenter) {
      return corePresenter.getFlow();
    }
  }

  @Singleton
  public static class Presenter extends FlowOwner<Blueprint, MainView> {
    private final ActionBarOwner actionBarOwner;

    @Inject Presenter(Parcer<Object> flowParcer, ActionBarOwner actionBarOwner) {
      super(flowParcer);
      this.actionBarOwner = actionBarOwner;
    }

    @Override public void showScreen(Blueprint newScreen, Flow.Direction direction) {
      boolean hasUp = newScreen instanceof HasParent;
      String title = newScreen.getClass().getSimpleName();
      actionBarOwner.setConfig(new ActionBarOwner.Config(false, hasUp, title, null));

      super.showScreen(newScreen, direction);
    }

    @Override protected Blueprint getFirstScreen() {
        if(ParseUser.getCurrentUser() != null) {
            return new HomeScreen();
        } else {
            return new LoginScreen();
        }
    }

  }
}
