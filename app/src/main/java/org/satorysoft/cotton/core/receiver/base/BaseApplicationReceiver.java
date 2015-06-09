package org.satorysoft.cotton.core.receiver.base;

import android.content.BroadcastReceiver;
import android.content.Intent;

import org.satorysoft.cotton.util.Constants;

/**
 * Created by viacheslavokolitiy on 10.06.2015.
 */
public abstract class BaseApplicationReceiver extends BroadcastReceiver {
    protected Intent createApplicationIntent(Intent intent, String actionName){
        String receivedPackageName = intent.getData().toString();
        final Intent appIntent = new Intent();
        appIntent.putExtra(Constants.ADDED_PACKAGE_NAME, receivedPackageName);
        appIntent.setAction(actionName);

        return appIntent;
    }
}
