package org.satorysoft.cotton.di.module;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import dagger.Module;
import dagger.Provides;

/**
 * Created by viacheslavokolitiy on 15.06.2015.
 */
@Module
public class GoogleDriveModule {
    private final Context context;

    public GoogleDriveModule(Context context){
        this.context = context;
    }

    @Provides
    public GoogleApiClient provideApiClient(){
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE);

        return builder.build();
    }
}
