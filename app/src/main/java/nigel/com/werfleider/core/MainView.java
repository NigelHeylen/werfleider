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

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import mortar.Blueprint;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.util.CanShowScreen;
import nigel.com.werfleider.util.ScreenConductor;

public class MainView extends FrameLayout implements CanShowScreen<Blueprint> {

    @Inject CorePresenter.Presenter corePresenter;
    private ScreenConductor<Blueprint> screenMaestro;


    @InjectView(R.id.container) FrameLayout container;
    @InjectView(R.id.toolbar) Toolbar toolbar;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        screenMaestro = new ScreenConductor<>(getContext(), container);
        corePresenter.takeView(this);
    }

    public Flow getFlow() {
        return corePresenter.getFlow();
    }

    @Override public void showScreen(Blueprint screen, Flow.Direction direction) {
        screenMaestro.showScreen(screen, direction);
    }

    public FrameLayout getContainer() {
        return container;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
