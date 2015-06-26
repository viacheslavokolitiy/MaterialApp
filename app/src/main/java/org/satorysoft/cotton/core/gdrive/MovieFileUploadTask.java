package org.satorysoft.cotton.core.gdrive;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslavokolitiy on 26.06.2015.
 */
public class MovieFileUploadTask extends APIAsyncTask<Void, Integer, List<Metadata>> {
    private final Context mContext;
    private final ArrayList<String> selectedItems;
    private ProgressDialog dialog;

    public MovieFileUploadTask(Context activityContext, ArrayList<String> mSelectedItems) {
        super(activityContext);
        this.mContext = activityContext;
        this.selectedItems = mSelectedItems;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(mContext);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage(mContext.getString(R.string.text_progressdialog_backup_movies));
        dialog.show();

    }

    @Override
    protected List<Metadata> doInBackgroundConnected(Void[] params) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "movies_"
                + Long.toString(System.currentTimeMillis()) + ".zip";
        FileUtils.zip(selectedItems, path);
        final List<Metadata> fileMetadataList = new ArrayList<>();
        final String encodedDriveId = PreferenceManager.getDefaultSharedPreferences(mContext).getString(Constants.APPFOLDER_DRIVE_ID, null);
        String encodedMoviesFolderID = PreferenceManager.getDefaultSharedPreferences(mContext).getString(Constants.MOVIES_FOLDER_ID, null);

        getOrCreateBackupFolder(mContext, encodedDriveId, mContext.getString(R.string.text_movies_folder_name), encodedMoviesFolderID, Constants.MOVIES_FOLDER_ID);

        DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                .newDriveContents(getGoogleApiClient())
                .await();
        if (!contentsResult.getStatus().isSuccess()) {
            return null;
        }

        File fileForUpload = new File(path);
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
                .setMimeType("application/zip").build();

        if(!TextUtils.isEmpty(encodedDriveId)) {

            if (uploadFileToDrive(fileMetadataList, originalContents, originalMetadata))
                return null;
        }

        return fileMetadataList;
    }

    @Override
    protected void onPostExecute(List<Metadata> metadataList) {
        super.onPostExecute(metadataList);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
