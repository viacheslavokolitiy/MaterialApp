package org.satorysoft.cotton.core.receiver;

import android.content.Context;
import android.content.Intent;

import org.satorysoft.cotton.core.receiver.base.BaseApplicationReceiver;
import org.satorysoft.cotton.util.Constants;

/**
 * Created by viacheslavokolitiy on 10.06.2015.
 */
public class ApplicationInstalledReceiver extends BaseApplicationReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Intent scanIntent = createApplicationIntent(intent, Constants.IntentActions.INTENT_SCAN_APPS);
        context.startService(scanIntent);
    }
}
