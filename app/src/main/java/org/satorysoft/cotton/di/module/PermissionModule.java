package org.satorysoft.cotton.di.module;

import android.content.Context;
import android.content.pm.PackageManager;

import org.satorysoft.cotton.core.model.PermissionList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
@Module
public class PermissionModule {

    private final Context context;

    public PermissionModule(Context context){
        this.context = context;
    }

    @Provides
    public PermissionList providePermissionsList(){
        return new PermissionList();
    }

    @Provides
    public PackageManager providePackageManager(){
        return context.getPackageManager();
    }
}
