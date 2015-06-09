package org.satorysoft.cotton.di.component;

import org.satorysoft.cotton.di.module.ViewsModule;

import javax.inject.Singleton;

import dagger.Component;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by viacheslavokolitiy on 09.06.2015.
 */
@Component(modules = {ViewsModule.class})
@Singleton
public interface ViewsComponent {
    MaterialDialog getMaterialDialog();
}
