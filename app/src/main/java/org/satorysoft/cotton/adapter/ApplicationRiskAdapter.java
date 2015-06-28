package org.satorysoft.cotton.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.core.model.SelectedApplication;
import org.satorysoft.cotton.core.service.ApplicationScannerService;
import org.satorysoft.cotton.ui.activity.ApplicationDetailActivity;
import org.satorysoft.cotton.ui.widget.RobotoTextView;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.IDrawableStateManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ApplicationRiskAdapter extends RecyclerView.Adapter<ApplicationRiskAdapter.AppListViewHolder>
    implements IDrawableStateManager {
    private List<ScannedApplication> scannedApplications = new ArrayList<>();
    private Context context;

    @Inject
    public ApplicationRiskAdapter(Context context){
        this.context = context;
    }

    @Override
    public AppListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_application_item, parent, false);
        return new AppListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppListViewHolder holder, int position) {
        holder.applicationLogo.setImageDrawable(restoreDrawable(scannedApplications
                .get(position)
                .getInstalledApplication()
                .getApplicationIconBytes()));
        holder.applicationTitle.setText(scannedApplications
                .get(position)
                .getInstalledApplication()
                .getApplicationName());
    }

    @Override
    public int getItemCount() {
        return scannedApplications.size();
    }

    public void addItem(ScannedApplication application){
        scannedApplications.add(0, application);
        notifyItemInserted(0);
    }

    class AppListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.application_icon)
        protected ImageView applicationLogo;
        @Bind(R.id.text_application_name)
        protected RobotoTextView applicationTitle;

        public AppListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //select application from list
            Drawable drawable = applicationLogo.getDrawable();
            String title = applicationTitle.getText().toString();
            String[] applicationPermissions;

            Cursor cursor = context.getContentResolver().query(ScannedApplicationContract.CONTENT_URI,
                    null, ScannedApplicationContract.APPLICATION_NAME + "=?",
                    new String[]{title}, null);
            if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
                applicationPermissions = cursor.getString(cursor.getColumnIndex(ScannedApplicationContract
                        .APPLICATION_PERMISSIONS))
                        .split(ApplicationScannerService.ARRAY_DIVIDER.toString());
            } else {
                applicationPermissions = new String[]{};
            }

            if(cursor != null){
                cursor.close();
            }

            SelectedApplication selectedApplication = new SelectedApplication();
            selectedApplication.setIcon(convertToBytes(drawable));
            selectedApplication.setTitle(title);
            selectedApplication.setPermissions(applicationPermissions);

            Intent intent = new Intent(context, ApplicationDetailActivity.class);
            intent.putExtra(Constants.SCANNED_APPLICATION, selectedApplication);
            EventBus.getDefault().post(new SelectedApplicationEvent(intent));
        }
    }

    @Override public byte[] convertToBytes(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, Constants.COMPRESS_QUALITY, stream);
        return stream.toByteArray();
    }

    @SuppressWarnings("deprecation")
    @Override public Drawable restoreDrawable(byte[] bytes){
        return new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, Constants.OFFSET, bytes.length));
    }

    public class SelectedApplicationEvent {
        private final Intent intent;
        public SelectedApplicationEvent(Intent intent) {
            this.intent = intent;
        }

        public Intent getIntent() {
            return intent;
        }
    }
}
