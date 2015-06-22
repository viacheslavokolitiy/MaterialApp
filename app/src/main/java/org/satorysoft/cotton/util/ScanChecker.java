package org.satorysoft.cotton.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.satorysoft.cotton.di.component.DaggerPreferenceComponent;
import org.satorysoft.cotton.di.component.PreferenceComponent;
import org.satorysoft.cotton.di.module.PreferenceModule;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ScanChecker {
    private Context mContext;
    private PreferenceComponent mPreferenceComponent;

    public ScanChecker(Context context){
        this.mContext = context;
        this.mPreferenceComponent = DaggerPreferenceComponent.builder()
                .preferenceModule(new PreferenceModule(context))
                .build();
    }

    public void check(){
        boolean isScanNeeded = mPreferenceComponent.getBooleanPreference()
                .getValue(Constants.SCAN_ON_FIRST_RUN_DONE, false);
        if(!isScanNeeded){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.GOOGLE_AUTH_VALID, false);
            editor.commit();
            EventBus.getDefault().post(new ShowApplicationScanScreenEvent());
        } else {
            EventBus.getDefault().post(new ShowApplicationListScreenEvent());
        }
    }

    public static class ShowApplicationScanScreenEvent {
    }

    public static class ShowApplicationListScreenEvent {
    }
}
