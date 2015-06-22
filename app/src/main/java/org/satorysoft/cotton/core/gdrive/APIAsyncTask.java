package org.satorysoft.cotton.core.gdrive;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.di.component.DaggerGoogleDriveComponent;
import org.satorysoft.cotton.di.module.GoogleDriveModule;
import org.satorysoft.cotton.util.Constants;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by viacheslavokolitiy on 15.06.2015.
 */
public abstract class APIAsyncTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result> {
    protected DriveFolder driveFolder;

    private GoogleApiClient mApiClient;
    private ArrayList<DriveFolder> backupFolders = new ArrayList<>();

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

                driveFolder = driveFolderResult.getDriveFolder();
            }
        });

        return driveFolder;
    }

    private void storeFolderName(DriveFolder.DriveFolderResult driveFolderResult,
                                 Context context, String folderPreferenceName) {
        DriveId driveId = driveFolderResult.getDriveFolder().getDriveId();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(folderPreferenceName, driveId.encodeToString());
        editor.commit();
    }

    /**
     * Gets or creates backup folder for backup data
     * @param context context
     * @param encodedDriveId app folder id
     * @param folderName folder name
     * @param encodedTargetFolderId target folder id(e.g photo folder, contacts folder etc)
     * @param folderConstant folder constant
     * @return
     */
    protected DriveFolder getOrCreateBackupFolder(final Context context, final String encodedDriveId,
                                                  final String folderName,
                                                  String encodedTargetFolderId, final String folderConstant) {
        if(TextUtils.isEmpty(encodedTargetFolderId)){
            Query query = new Query.Builder()
                    .addFilter(Filters.eq(SearchableField.MIME_TYPE, "application/vnd.google-apps.folder")).build();
            if(!TextUtils.isEmpty(encodedDriveId)){
                Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedDriveId))
                        .queryChildren(getGoogleApiClient(), query)
                        .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                            @Override
                            public void onResult(DriveApi.MetadataBufferResult result) {
                                for(Metadata metadata : result.getMetadataBuffer()){
                                    if(!metadata.isTrashed()){
                                        String title = metadata.getTitle();
                                        if(title.equals(folderName)){
                                            driveFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), metadata.getDriveId());
                                            backupFolders.add(driveFolder);
                                        }
                                    }
                                }

                                if(backupFolders.size() == 0){
                                    DriveFolder appFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedDriveId));
                                    driveFolder = createFolderWithName(context,
                                            appFolder,
                                            folderName,
                                            folderConstant);
                                }
                            }
                        });
            }
        } else {
            driveFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedTargetFolderId));
        }

        return driveFolder;
    }
}
