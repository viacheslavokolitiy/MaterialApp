package org.satorysoft.cotton.core.gdrive;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;

import org.satorysoft.cotton.di.component.DaggerGoogleDriveComponent;
import org.satorysoft.cotton.di.module.GoogleDriveModule;
import org.satorysoft.cotton.util.Constants;

import java.util.concurrent.CountDownLatch;

/**
 * Created by viacheslavokolitiy on 15.06.2015.
 */
public abstract class APIAsyncTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result> {
    protected DriveFolder photoFolder;

    private GoogleApiClient mApiClient;

    public APIAsyncTask(Context context){
        this.mApiClient = DaggerGoogleDriveComponent
                .builder()
                .googleDriveModule(new GoogleDriveModule(context))
                .build()
                .getGoogleApiClient();
    }

    @Override
    protected Result doInBackground(Params... params) {
        final CountDownLatch latch = new CountDownLatch(Constants.LATCH_COUNT);
        mApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                latch.countDown();
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });

        mApiClient.registerConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                latch.countDown();
            }
        });

        mApiClient.connect();

        try {
            latch.await();
        } catch (InterruptedException e) {
            return null;
        }

        if (!mApiClient.isConnected()) {
            return null;
        }
        try {
            return doInBackgroundConnected(params);
        } finally {
            mApiClient.disconnect();
        }
    }

    protected abstract Result doInBackgroundConnected(Params[] params);

    protected GoogleApiClient getGoogleApiClient() {
        return mApiClient;
    }

    protected DriveFolder createFolderWithName(final Context context,
                                               DriveFolder appFolder,
                                               String name,
                                               final String folderPreferenceName){
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(name).build();
        appFolder.createFolder(getGoogleApiClient(), changeSet).setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
            @Override
            public void onResult(DriveFolder.DriveFolderResult driveFolderResult) {
                if(!driveFolderResult.getStatus().isSuccess()){
                    return;
                }

                storeFolderName(driveFolderResult, context, folderPreferenceName);

                photoFolder = driveFolderResult.getDriveFolder();
            }
        });

        return photoFolder;
    }

    private void storeFolderName(DriveFolder.DriveFolderResult driveFolderResult,
                                 Context context, String folderPreferenceName) {
        DriveId driveId = driveFolderResult.getDriveFolder().getDriveId();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(folderPreferenceName, driveId.encodeToString());
        editor.commit();
    }
}
