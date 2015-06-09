package org.satorysoft.cotton.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by viacheslavokolitiy on 09.06.2015.
 */
@Module
public class ViewsModule {
    private final Context context;
    
    public ViewsModule(Context context){
        this.context = context;
    }

    @Provides
    public MaterialDialog provideMaterialDialog(){
        return new MaterialDialog(context);
    }
}
