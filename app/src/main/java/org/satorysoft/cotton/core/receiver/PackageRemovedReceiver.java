package org.satorysoft.cotton.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.satorysoft.cotton.core.receiver.base.BaseApplicationReceiver;
import org.satorysoft.cotton.util.Constants;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 09.06.2015.
 */
public class PackageRemovedReceiver extends BaseApplicationReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent packageRemoveIntent = createApplicationIntent(intent, Constants.IntentActions.INTENT_REMOVE_APP);
        //EventBus.getDefault().post(new ApplicationRemovedEvent());
        context.startService(packageRemoveIntent);
    }
}
