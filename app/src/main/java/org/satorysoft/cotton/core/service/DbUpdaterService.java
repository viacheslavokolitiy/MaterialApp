package org.satorysoft.cotton.core.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import org.satorysoft.cotton.core.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.ui.activity.ApplicationDetailActivity;
import org.satorysoft.cotton.util.Constants;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 10.06.2015.
 */
public class DbUpdaterService extends IntentService {

    public DbUpdaterService() {
        super(DbUpdaterService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        String packageName = extras.getString(Constants.ADDED_PACKAGE_NAME);

        getContentResolver().delete(ScannedApplicationContract.CONTENT_URI, ScannedApplicationContract.PACKAGE_NAME + "=?",
                new String[]{packageName.replace("package:", "")});

        EventBus.getDefault().post(new ApplicationDetailActivity.UpdateApplicationListEvent());
    }
}
