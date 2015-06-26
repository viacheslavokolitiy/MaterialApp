package org.satorysoft.cotton.core.gdrive;

import android.app.ProgressDialog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 17.06.2015.
 */
public class MusicFileUploadTask extends APIAsyncTask<Void, Integer, List<Metadata>>{
    private final Context context;
    private final ArrayList<String> selectedFiles;
    private DriveFolder backupMusicFolder;
    private ArrayList<DriveFolder> backupMusicFolders = new ArrayList<>();
    private ProgressDialog dialog;

    public MusicFileUploadTask(Context context, ArrayList<String> selectedFiles){
        super(context);
        this.context = context;
        this.selectedFiles = selectedFiles;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(false);
        dialog.setMessage(context.getString(R.string.text_backup_music_in_progress));
        dialog.show();
    }

    @Override
    protected List<Metadata> doInBackgroundConnected(Void[] params) {
        final List<Metadata> fileMetadataList = new ArrayList<>();
        final String encodedDriveId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.APPFOLDER_DRIVE_ID, null);
        String encodedMusicFolderID = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.MUSIC_FOLDER_DRIVE_ID, null);

        getOrCreateBackupFolder(context, encodedDriveId, context.getString(R.string.text_backup_music_folder_name), encodedMusicFolderID,
                Constants.MUSIC_FOLDER_DRIVE_ID);

        for(String selectedFileURL : selectedFiles){
            DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                    .newDriveContents(getGoogleApiClient())
                    .await();
            if (!contentsResult.getStatus().isSuccess()) {
                return null;
            }

            File fileForUpload = new File(selectedFileURL);
            final DriveContents originalContents = contentsResult.getDriveContents();
            OutputStream outputStream = originalContents.getOutputStream();

            try {
                InputStream fileInputStream = new FileInputStream(fileForUpload);
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

            MetadataChangeSet originalMetadata = new MetadataChangeSet.Builder()
                    .setTitle(fileForUpload.getName())
                    .setMimeType("audio/mpeg").build();

            if(!TextUtils.isEmpty(encodedDriveId)) {

                if (uploadFileToDrive(fileMetadataList, originalContents, originalMetadata))
                    return null;
            }
        }
        return fileMetadataList;
    }

    @Override
    protected void onPostExecute(List<Metadata> metadataList) {
        super.onPostExecute(metadataList);

        if(dialog != null){
            dialog.dismiss();
        }

        if (metadataList != null && metadataList.size() == 0){
            EventBus.getDefault().post(new UploadPhotoTask.FileUploadFailedEvent(context.getString(R.string.text_upload_failed_error)));
        } else {
            EventBus.getDefault().post(new UploadPhotoTask.UploadSuccessfulEvent(context.getString(R.string.music_upload_success_status)));
        }
    }
}
