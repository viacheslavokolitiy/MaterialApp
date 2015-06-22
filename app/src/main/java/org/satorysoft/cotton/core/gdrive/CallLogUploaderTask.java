package org.satorysoft.cotton.core.gdrive;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import org.json.JSONException;
import org.json.JSONObject;
import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.model.CallLogData;
import org.satorysoft.cotton.ui.activity.ApplicationListActivity;
import org.satorysoft.cotton.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by viacheslavokolitiy on 16.06.2015.
 */
public class CallLogUploaderTask extends APIAsyncTask<Void, Integer, List<Metadata>>{
    private final Context context;
    private ProgressDialog dialog;
    private DriveFolder backupCallLogFolder;
    private ArrayList<DriveFolder> backupCallLogFolders = new ArrayList<>();
    private ArrayList<JSONObject> jsonObjects = new ArrayList<>();
    private FileWriter fileWriter;

    public CallLogUploaderTask(Context applicationListActivityContext) {
        super(applicationListActivityContext);
        this.context = applicationListActivityContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(R.string.text_upload_calls));
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<Metadata> metadataList) {
        super.onPostExecute(metadataList);

        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }

        Toast.makeText(context, R.string.text_call_log_backup_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected List<Metadata> doInBackgroundConnected(Void[] params) {
        List<CallLogData> callLogDataList = queryForCallHistory(new Date());
        final List<Metadata> fileMetadataList = new ArrayList<>();
        final String encodedDriveId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.APPFOLDER_DRIVE_ID, null);
        String encodedCallLogFolderId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.CALL_LOG_FOLDER_ID, null);

        getOrCreateBackupFolder(context, encodedDriveId, context.getString(R.string.text_backup_call_log_name), encodedCallLogFolderId, Constants.CALL_LOG_FOLDER_ID);


        File jsonFile = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"
                + Long.toString(new Date().getTime()) + ".json");


        for(CallLogData callLogData : callLogDataList) {
            DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                    .newDriveContents(getGoogleApiClient())
                    .await();
            if (!contentsResult.getStatus().isSuccess()) {
                return null;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(CallLog.Calls._ID, callLogData.getId());
                jsonObject.put(CallLog.Calls.NUMBER, callLogData.getPhoneNumber());
                jsonObject.put(CallLog.Calls.CACHED_NAME, callLogData.getContactName());
                jsonObjects.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            fileWriter = new FileWriter(jsonFile);
            for(JSONObject jsonObject : jsonObjects){
                fileWriter.write(jsonObject.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                .newDriveContents(getGoogleApiClient())
                .await();

        final DriveContents originalContents = contentsResult.getDriveContents();
        OutputStream outputStream = originalContents.getOutputStream();

        try {
            InputStream fileInputStream = new FileInputStream(jsonFile);
            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            int length;
            int counter = 0;
            while((length = fileInputStream.read(buffer)) > 0){
                ++counter;
                outputStream.write(buffer, 0, length);
            }

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!contentsResult.getStatus().isSuccess()) {
            return null;
        }

        MetadataChangeSet originalMetadata = new MetadataChangeSet.Builder()
                .setTitle(jsonFile.getName())
                .setMimeType("text/plain").build();

        if(!TextUtils.isEmpty(encodedDriveId)) {

            if(driveFolder != null){
                DriveFolder.DriveFileResult fileResult = driveFolder.createFile(
                        getGoogleApiClient(), originalMetadata, originalContents).await();

                if (!fileResult.getStatus().isSuccess()) {
                    return null;
                }

                DriveResource.MetadataResult metadataResult = fileResult.getDriveFile()
                        .getMetadata(getGoogleApiClient())
                        .await();

                if (!metadataResult.getStatus().isSuccess()) {
                    return null;
                }

                fileMetadataList.add(metadataResult.getMetadata());
            }
        }
        return fileMetadataList;
    }

    private List<CallLogData> queryForCallHistory(Date date){
        List<CallLogData> callLogDataList = new ArrayList<>();
        Cursor callsCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null,
                CallLog.Calls.DATE + "<?",
                new String[] { String.valueOf(date.getTime())},
                CallLog.Calls.NUMBER + " asc");
        if(callsCursor != null && callsCursor.getCount() > 0 && callsCursor.moveToFirst()){
            do {
                CallLogData data = new CallLogData();
                String id = callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls._ID));
                String phoneNumber = callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.NUMBER));
                String contactName = callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                data.setId(id);
                data.setPhoneNumber(phoneNumber);
                data.setContactName(contactName);
                callLogDataList.add(data);
            } while (callsCursor.moveToNext());
        }

        return callLogDataList;
    }
}
