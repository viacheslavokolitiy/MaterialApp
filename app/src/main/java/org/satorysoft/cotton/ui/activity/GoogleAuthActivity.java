package org.satorysoft.cotton.ui.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import org.satorysoft.cotton.ui.widget.RobotoButton;
import org.satorysoft.cotton.util.Constants;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 10.06.2015.
 */
public class GoogleAuthActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
                   GoogleApiClient.OnConnectionFailedListener {
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1500;
    @Bind(R.id.btn_signup_gdrive)
    protected RobotoButton authButton;
    private GoogleApiClient mGoogleAPIClient;
    private ArrayList<String> appFolders = new ArrayList<>();
    private ArrayList<DriveId> appIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_auth);
        ButterKnife.bind(this);
        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @OnClick(R.id.btn_signup_gdrive)
    public void onSignUp(){
        mGoogleAPIClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Drive.DriveApi.requestSync(mGoogleAPIClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.MIME_TYPE, "application/vnd.google-apps.folder")).build();
        Drive.DriveApi.getRootFolder(mGoogleAPIClient)
                .queryChildren(mGoogleAPIClient, query)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(DriveApi.MetadataBufferResult result) {
                        for (Metadata metadatas : result.getMetadataBuffer()) {
                            if(!metadatas.isTrashed()) {
                                String title = metadatas.getTitle();
                                if (title.equals("CottonData")) {
                                    appFolders.add(title);
                                    appIds.add(metadatas.getDriveId());
                                }
                            }
                        }

                        if(appFolders.size() == 0){
                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle("CottonData").build();
                            Drive.DriveApi.getRootFolder(mGoogleAPIClient).createFolder(
                                    mGoogleAPIClient, changeSet).setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                                @Override
                                public void onResult(DriveFolder.DriveFolderResult driveFolderResult) {
                                    if (!driveFolderResult.getStatus().isSuccess()) {
                                        return;
                                    }

                                    DriveId driveId = driveFolderResult.getDriveFolder().getDriveId();
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString(Constants.APPFOLDER_DRIVE_ID, driveId.encodeToString());
                                    editor.putBoolean(Constants.GOOGLE_AUTH_VALID, true);
                                    editor.commit();

                                    finish();
                                }
                            });
                        } else {
                            DriveId appFolderId = appIds.get(0);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Constants.APPFOLDER_DRIVE_ID, appFolderId.encodeToString());
                            editor.putBoolean(Constants.GOOGLE_AUTH_VALID, true);
                            editor.commit();
                            finish();
                        }
                    }
                });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            mGoogleAPIClient.connect();
        }
    }
}
