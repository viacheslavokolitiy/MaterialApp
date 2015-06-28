package org.satorysoft.cotton.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.adapter.PhotoGridAdapter;
import org.satorysoft.cotton.core.gdrive.UploadPhotoTask;
import org.satorysoft.cotton.di.component.AdapterComponent;
import org.satorysoft.cotton.di.component.DaggerAdapterComponent;
import org.satorysoft.cotton.di.module.AdapterModule;
import org.satorysoft.cotton.util.ActionBarOwner;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.NetworkUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 10.06.2015.
 */
public class BackupPhotoActivity extends AppCompatActivity {
    @Bind(R.id.toolbar_backup_photos)
    protected Toolbar backupPhotoToolbar;
    @Bind(R.id.photo_grid)
    protected GridView photoGrid;

    private AdapterComponent mAdapterComponent;
    private PhotoGridAdapter mPhotoGridAdapter;
    private ArrayList<String> selectedImages;
    private boolean needShowActionMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_photos);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        this.selectedImages = new ArrayList<>();
        setSupportActionBar(backupPhotoToolbar);
        new ActionBarOwner(this).setCustomActionBarTitle(getSupportActionBar(), getString(R.string.text_backup_photos));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mAdapterComponent = DaggerAdapterComponent.builder().adapterModule(new AdapterModule(this)).build();
        mPhotoGridAdapter = mAdapterComponent.getPhotoGridAdapter();
        fillPhotoGrid(this, photoGrid);
    }

    private void fillPhotoGrid(Context localContext, GridView photoGrid) {
        photoGrid.setAdapter(mPhotoGridAdapter);
        new PhotoTask(localContext).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnItemClick(R.id.photo_grid)
    public void onPhotoClick(int position){
        selectPhotoForBackup(this, photoGrid, position);
    }

    private void selectPhotoForBackup(Context localContext, GridView photoGrid, int position) {
        String imageURL = getImageURLS(localContext).get(position);
        if(!selectedImages.contains(imageURL)){
            selectedImages.add(imageURL);
            mPhotoGridAdapter.addSelection(position, true);
        } else {
            selectedImages.remove(imageURL);
            mPhotoGridAdapter.removeSelection(position);
            selectedImages.trimToSize();
        }

        if(needShowActionMode){
            EventBus.getDefault().post(new ShowActionModeEvent());
            needShowActionMode = false;
        }
    }

    private class PhotoTask extends AsyncTask<Void, Integer, List<String>> {
        private final Context context;
        private ProgressDialog progressDialog;

        public PhotoTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            final List<String> imageURLs = getImageURLS(context);

            return imageURLs;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            photoGrid.setNumColumns(3);
            for(String imageURI : result){
                File targetFile = new File(imageURI);
                mPhotoGridAdapter.addImage(targetFile.getAbsolutePath());
            }
        }
    }

    private List<String> getImageURLS(Context context) {
        final List<String> imageURLs = new ArrayList<>();
        String[] columns = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, null);

        if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
            do {
                String imageURL = cursor.getString(0);
                imageURLs.add(imageURL);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return imageURLs;
    }

    public static class ShowActionModeEvent {
    }

    public void onEvent(ActionModeDestroyedEvent event){
        mPhotoGridAdapter.clearSelection();
        needShowActionMode = true;
        selectedImages.clear();
        selectedImages.trimToSize();
    }

    public static class ActionModeDestroyedEvent {
    }

    public void onEvent(ShowActionModeEvent actionModeEvent){
        startSupportActionMode(new SelectPhotoCallback(this));
    }

    private class SelectPhotoCallback implements ActionMode.Callback {
        private final Context context;

        public SelectPhotoCallback(Context context){
            this.context = context;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_contextual, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.item_upload_to_drive:
                    EventBus.getDefault().post(new BackupPhotoEvent(context));
                    break;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            EventBus.getDefault().post(new ActionModeDestroyedEvent());
        }
    }

    public void onEvent(BackupPhotoEvent event){
        backupPhotos(event.getContext());
    }

    private void backupPhotos(Context context) {
        if(selectedImages.size() > 0){
            if(NetworkUtil.isNetworkOnline(context)){
                new UploadPhotoTask(context, selectedImages).execute();
            } else {
                EventBus.getDefault().post(new NoInternetConnectionEvent(context.getString(R.string.text_no_inet_conn)));
            }
        }
    }

    public static class BackupPhotoEvent {
        private Context context;
        public BackupPhotoEvent(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }
    }

    public void onEvent(UploadPhotoTask.UploadSuccessfulEvent event){
        Toast.makeText(getBaseContext(), event.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void onEvent(UploadPhotoTask.FileUploadFailedEvent event){
        Toast.makeText(getBaseContext(), event.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void onEvent(NoInternetConnectionEvent event){
        Toast.makeText(getBaseContext(), event.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public static class NoInternetConnectionEvent {
        private String message;
        public NoInternetConnectionEvent(String string) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
