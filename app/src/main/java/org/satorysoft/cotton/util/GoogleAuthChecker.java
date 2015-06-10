package org.satorysoft.cotton.util;

import android.content.Context;

import org.satorysoft.cotton.di.component.DaggerPreferenceComponent;
import org.satorysoft.cotton.di.component.PreferenceComponent;
import org.satorysoft.cotton.di.module.PreferenceModule;

/**
 * Created by viacheslavokolitiy on 10.06.2015.
 */
public class GoogleAuthChecker {
    private PreferenceComponent mPreferenceComponent;
    private PreferenceModule.BooleanPreference mBooleanPreference;
    private final Context context;

    public GoogleAuthChecker(Context context){
        this.context = context;
        this.mPreferenceComponent = DaggerPreferenceComponent.builder()
                .preferenceModule(new PreferenceModule(context))
                .build();
        this.mBooleanPreference = mPreferenceComponent.getBooleanPreference();
    }

    public boolean isUserAuthenticated(){
        if(mBooleanPreference.getValue(Constants.GOOGLE_AUTH_VALID, true)){
            return true;
        } else {
            return false;
        }
    }
}
