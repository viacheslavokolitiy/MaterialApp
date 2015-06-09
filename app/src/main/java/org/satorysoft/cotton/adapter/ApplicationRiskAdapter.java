package org.satorysoft.cotton.adapter;

import android.content.Context;
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
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.ui.widget.RobotoTextView;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.IDrawableStateManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;

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
        @FindView(R.id.application_icon)
        protected ImageView applicationLogo;
        @FindView(R.id.text_application_name)
        protected RobotoTextView applicationTitle;

        public AppListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

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
}
