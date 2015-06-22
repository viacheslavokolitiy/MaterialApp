package org.satorysoft.cotton.core.gdrive;

import android.app.ProgressDialog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

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
 * Created by viacheslavokolitiy on 15.06.2015.
 */
public class UploadPhotoTask extends APIAsyncTask<String, Void, List<Metadata>>{
    private final ArrayList<String> images;
    private final Context context;
    private ProgressDialog dialog;

    private ArrayList<DriveFolder> photoFolderIds = new ArrayList<>();

    public UploadPhotoTask(Context context, ArrayList<String> images) {
        super(context);
        this.context = context;
        this.images = images;

    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(R.string.text_backing_up_photos_progress));
        dialog.show();
    }

    @Override
    protected List<Metadata> doInBackgroundConnected(String[] params) {
        final List<Metadata> fileMetadataList = new ArrayList<>();

        String encodedPhotoFolderId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.PHOTO_FOLDER_ID, null);
        final String encodedDriveId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.APPFOLDER_DRIVE_ID, null);

        //no photo folder created yet
        if(TextUtils.isEmpty(encodedPhotoFolderId)){
            Query query = new Query.Builder()
                    .addFilter(Filters.eq(SearchableField.MIME_TYPE, "application/vnd.google-apps.folder")).build();
            Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedDriveId))
                    .queryChildren(getGoogleApiClient(), query)
                    .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(DriveApi.MetadataBufferResult result) {
                            for (Metadata metadata : result.getMetadataBuffer()){
                                if(!metadata.isTrashed()){
                                    String title = metadata.getTitle();
                                    if(title.equals(context.getString(R.string.text_photo_folder_name))){
                                        photoFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), metadata.getDriveId());
                                        photoFolderIds.add(photoFolder);
                                    }
                                }
                            }

                            if(photoFolderIds.size() == 0){
                                DriveFolder appFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedDriveId));
                                photoFolder = createFolderWithName(context,appFolder,
                                        context.getString(R.string.text_photo_folder_name),
                                        Constants.PHOTO_FOLDER_ID);
                            }
                        }
                    });
        } else {
            photoFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedPhotoFolderId));
        }

        for(String imageURL : images){
            DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                    .newDriveContents(getGoogleApiClient())
                    .await();
            if (!contentsResult.getStatus().isSuccess()) {
                return null;
            }


            File fileForUpload = new File(imageURL);
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
                    .setMimeType("image/jpg").build();

            if(!TextUtils.isEmpty(encodedDriveId)) {

                if(photoFolder != null){
                    DriveFolder.DriveFileResult fileResult = photoFolder.createFile(
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
            EventBus.getDefault().post(new FileUploadFailedEvent(context.getString(R.string.text_upload_failed_error)));
        } else {
            EventBus.getDefault().post(new UploadSuccessfulEvent(context.getString(R.string.text_upload_success)));
        }
    }

    public static class FileUploadFailedEvent {
        private String message;
        public FileUploadFailedEvent(String string) {
            this.message = string;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class UploadSuccessfulEvent {
        private String message;
        public UploadSuccessfulEvent(String string) {
            this.message = string;
        }

        public String getMessage() {
            return message;
        }
    }
}
