package org.satorysoft.cotton.di.component;

import org.satorysoft.cotton.di.module.PreferenceModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
@Component(modules = {PreferenceModule.class})
@Singleton
public interface PreferenceComponent {
    PreferenceModule.BooleanPreference getBooleanPreference();
}
