package org.satorysoft.cotton.di.component;

import org.satorysoft.cotton.adapter.ApplicationRiskAdapter;
import org.satorysoft.cotton.di.module.AdapterModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
@Component(modules = {AdapterModule.class})
@Singleton
public interface AdapterComponent {
    ApplicationRiskAdapter getAdapter();
}
