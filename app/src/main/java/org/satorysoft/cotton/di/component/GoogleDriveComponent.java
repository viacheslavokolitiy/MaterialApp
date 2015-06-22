package org.satorysoft.cotton.di.component;

import com.google.android.gms.common.api.GoogleApiClient;

import org.satorysoft.cotton.di.module.GoogleDriveModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 15.06.2015.
 */
@Component(modules = {GoogleDriveModule.class})
@Singleton
public interface GoogleDriveComponent {
    GoogleApiClient getGoogleApiClient();
}
