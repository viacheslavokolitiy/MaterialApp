package org.satorysoft.cotton.di.module;

import android.content.Context;

import org.satorysoft.cotton.adapter.ApplicationRiskAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
@Module
public class AdapterModule {
    private final Context context;

    public AdapterModule(Context context){
        this.context = context;
    }

    @Provides
    public ApplicationRiskAdapter provideHighRiskAppsAdapter(){
        return new ApplicationRiskAdapter(context);
    }
}
