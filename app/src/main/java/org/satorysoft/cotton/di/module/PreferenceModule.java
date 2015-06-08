package org.satorysoft.cotton.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
@Module
public class PreferenceModule {
    private final Context context;

    public PreferenceModule(Context context){
        this.context = context;
    }

    @Provides
    public BooleanPreference provideBooleanPreference(){
        return new BooleanPreference();
    }

    public class BooleanPreference {

        private SharedPreferences getPreferences(){
            return PreferenceManager.getDefaultSharedPreferences(context);
        }

        public boolean getValue(String key, boolean defValue){
            SharedPreferences sharedPreferences = getPreferences();
            return sharedPreferences.getBoolean(key, defValue);
        }

        public void setValue(String key, boolean valueToSave){
            SharedPreferences.Editor editor = getPreferences().edit();
            editor.putBoolean(key, valueToSave);
            editor.commit();
        }
    }
}
