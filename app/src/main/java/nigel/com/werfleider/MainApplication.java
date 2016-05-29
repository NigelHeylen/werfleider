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
package nigel.com.werfleider;

import android.app.Activity;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseObject;

import io.fabric.sdk.android.Fabric;
import mortar.Mortar;
import mortar.MortarScope;
import nigel.com.werfleider.commons.core.BaseApplication;
import nigel.com.werfleider.core.ApplicationModule;
import nigel.com.werfleider.model.Contact;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.Yard;

public class MainApplication extends BaseApplication {

    private MortarScope rootScope;

    private Activity activeActivity;

    @Override
    protected Object[] getApplicationModules() {

        return new Object[]{
                ApplicationModule.class};
    }

    @Override public void onCreate() {

        super.onCreate();
        Fabric.with(
                this,
                new Crashlytics());

        rootScope =
                Mortar.createRootScope(
                        BuildConfig.DEBUG,
                        getGraph());

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Yard.class);
        ParseObject.registerSubclass(Document.class);
        ParseObject.registerSubclass(DocumentLocation.class);
        ParseObject.registerSubclass(ParseDocumentImage.class);
        ParseObject.registerSubclass(Contact.class);
        Parse.initialize(
                this,
                "oTMmhRqniCH4RUXpHRvuLoq6tjBm2CokTqYcptdv",
                "Rkth6TnPi92kwUFAsdv3eqlSgl5Wn9HpNxDvKe4t");

    }

    @Override public Object getSystemService(String name) {

        if (Mortar.isScopeSystemService(name)) {
            return rootScope;
        }
        return super.getSystemService(name);
    }

    public void setActiveActivity(final Activity activeActivity) {

        this.activeActivity = activeActivity;
    }

    public MainActivity getActiveActivity() {

        return (MainActivity) activeActivity;
    }
}
