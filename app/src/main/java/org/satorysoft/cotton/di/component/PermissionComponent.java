package org.satorysoft.cotton.di.component;

import android.content.pm.PackageManager;

import org.satorysoft.cotton.core.model.PermissionList;
import org.satorysoft.cotton.di.module.PermissionModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
@Component(modules = {PermissionModule.class})
@Singleton
public interface PermissionComponent {
    PermissionList getPermissionList();
    PackageManager getPackageManager();
}
