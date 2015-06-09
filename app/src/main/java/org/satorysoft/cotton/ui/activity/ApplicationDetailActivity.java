package org.satorysoft.cotton.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.adapter.PermissionListAdapter;
import org.satorysoft.cotton.core.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.core.model.SelectedApplication;
import org.satorysoft.cotton.core.receiver.PackageRemovedReceiver;
import org.satorysoft.cotton.di.component.AdapterComponent;
import org.satorysoft.cotton.di.component.DaggerAdapterComponent;
import org.satorysoft.cotton.di.component.DaggerPermissionComponent;
import org.satorysoft.cotton.di.component.DaggerViewsComponent;
import org.satorysoft.cotton.di.component.PermissionComponent;
import org.satorysoft.cotton.di.component.ViewsComponent;
import org.satorysoft.cotton.di.module.AdapterModule;
import org.satorysoft.cotton.di.module.PermissionModule;
import org.satorysoft.cotton.di.module.ViewsModule;
import org.satorysoft.cotton.ui.animator.SlideInFromLeftItemAnimator;
import org.satorysoft.cotton.ui.widget.RobotoButton;
import org.satorysoft.cotton.ui.widget.RobotoTextView;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.DpUtil;
import org.satorysoft.cotton.util.IDrawableStateManager;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by viacheslavokolitiy on 09.06.2015.
 */
public class ApplicationDetailActivity extends AppCompatActivity implements IDrawableStateManager {
    @FindView(R.id.activity_app_details_bar)
    protected Toolbar applicationDetailToolbar;
    private PermissionComponent mPermissionComponent;
    private PackageManager mPackageManager;
    private AdapterComponent mAdapterComponent;
    private ViewsComponent mViewsComponent;
    private MaterialDialog materialDialog;

    @FindView(R.id.application_icon_detail)
    protected ImageView applicationLogo;
    @FindView(R.id.text_application_name_detail)
    protected RobotoTextView applicationName;
    @FindView(R.id.recycler_permissions)
    protected RecyclerView recyclerView;
    @FindView(R.id.btn_trust_application)
    protected RobotoButton trustButton;
    @FindView(R.id.btn_delete_application)
    protected RobotoButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(applicationDetailToolbar);
        this.mViewsComponent = DaggerViewsComponent.builder().viewsModule(new ViewsModule(this))
                .build();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            Intent intent = getIntent();
            SelectedApplication selectedApplication = (SelectedApplication) intent.getSerializableExtra(Constants.SCANNED_APPLICATION);
            setCustomActionBarTitle(selectedApplication.getTitle());
            EventBus.getDefault().post(new PopulateCardViewEvent(selectedApplication));
            Cursor cursor = getContentResolver().query(ScannedApplicationContract.CONTENT_URI, null,
                    ScannedApplicationContract.APPLICATION_NAME + "=?", new String[]{selectedApplication.getTitle()}, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    double risk = cursor.getDouble(cursor.getColumnIndex(ScannedApplicationContract.APPLICATION_RISK_RATE));
                    if (risk < Constants.MODERATE_RISK_LOWER) {
                        RobotoButton robotoButton = ButterKnife.findById(this, R.id.btn_trust_application);
                        robotoButton.setVisibility(View.GONE);

                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT
                        );

                        layoutParams.setMargins(0, DpUtil.dpToPx(60), 0, 0);
                        ButterKnife.findById(this, R.id.btn_delete_application).setLayoutParams(layoutParams);
                    } else {
                        ButterKnife.findById(this, R.id.btn_trust_application).setVisibility(View.VISIBLE);
                    }
                } while (cursor.moveToNext());
            }

            registerReceiver(new PackageRemovedReceiver(), new IntentFilter(Constants.IntentActions.INTENT_REMOVE_APP));
        }
    }

    public void setCustomActionBarTitle(String title) {
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.layout_action_bar_title, null);
        ((RobotoTextView)v.findViewById(R.id.text_custom_action_bar_title)).setText(title);
        getSupportActionBar().setCustomView(v);
    }

    public static class PopulateCardViewEvent {
        private final SelectedApplication selectedApplication;

        public PopulateCardViewEvent(SelectedApplication selectedApplication) {
            this.selectedApplication = selectedApplication;
        }

        public SelectedApplication getSelectedApplication() {
            return selectedApplication;
        }
    }

    public void setApplicationDetail(PopulateCardViewEvent event, ImageView applicationLogo,
                                     RobotoTextView applicationName) {
        byte[] icon = event.getSelectedApplication().getIcon();
        String name = event.getSelectedApplication().getTitle();
        applicationLogo.setImageDrawable(restoreDrawable(icon));
        applicationName.setText(name);
    }

    public void setPermissions(RecyclerView recyclerView, PopulateCardViewEvent event, Context context){
        this.mPermissionComponent = DaggerPermissionComponent.builder().permissionModule(new PermissionModule(context)).build();
        this.mAdapterComponent = DaggerAdapterComponent.builder().adapterModule(new AdapterModule(context)).build();
        this.mPackageManager = mPermissionComponent.getPackageManager();
        PermissionListAdapter adapter = mAdapterComponent.getPermissionsAdapter();
        List<String> permissions = Arrays.asList(event.getSelectedApplication().getPermissions());
        for(String permission : permissions){
            try {
                PermissionInfo info = mPackageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA);
                String permissionName;
                if(info.loadDescription(mPackageManager) != null) {
                    permissionName = info.loadDescription(mPackageManager).toString();
                    adapter.addItem(permissionName);
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInFromLeftItemAnimator(recyclerView));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public byte[] convertToBytes(Drawable drawable) {
        return new byte[0];
    }

    @Override public Drawable restoreDrawable(byte[] bytes){
        return new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }

    public void deleteApplication(Context context, RobotoTextView applicationName) {
        Cursor cursor = context.getContentResolver().query(ScannedApplicationContract.CONTENT_URI,
                null, ScannedApplicationContract.APPLICATION_NAME + "=?",
                new String[]{applicationName.getText().toString()}, null);
        String packageName = "";
        if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
            do {
                packageName = cursor.getString(cursor.getColumnIndex(ScannedApplicationContract.PACKAGE_NAME));
            } while (cursor.moveToNext());
        }

        if(cursor != null) {
            cursor.close();
        }

        if(!TextUtils.isEmpty(packageName)){
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
        }
    }

    public void onEvent(PopulateCardViewEvent event){
        setApplicationDetail(event, applicationLogo, applicationName);
        setPermissions(recyclerView, event, this);
    }

    @OnClick(R.id.btn_trust_application)
    public void onTrust(){
        materialDialog = mViewsComponent.getMaterialDialog();
        materialDialog.setTitle("Trust this application ?")
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        trustApplication(applicationName);
                        materialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                    }
                })
                .setMessage("This action will put application to trusted applications list. " +
                        "Do you really want this ?")
                .show();
    }

    private void trustApplication(RobotoTextView applicationName) {
        String name = applicationName.getText().toString();
        ContentValues values = new ContentValues();
        values.put(ScannedApplicationContract.APPLICATION_RISK_RATE, 0.11);
        getContentResolver().update(ScannedApplicationContract.CONTENT_URI, values,
                ScannedApplicationContract.APPLICATION_NAME + "=?", new String[]{name});
        EventBus.getDefault().post(new UpdateApplicationListEvent());
    }

    public static class UpdateApplicationListEvent {
    }

    public void onEvent(UpdateApplicationListEvent event){
        finish();
    }

    @OnClick(R.id.btn_delete_application)
    public void onDelete(){
        deleteApplication(this, applicationName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
