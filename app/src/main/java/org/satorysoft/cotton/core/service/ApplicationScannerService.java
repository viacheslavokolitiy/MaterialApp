package org.satorysoft.cotton.core.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import org.satorysoft.cotton.core.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.core.model.InstalledApplication;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.di.component.DaggerPermissionComponent;
import org.satorysoft.cotton.di.component.DaggerPreferenceComponent;
import org.satorysoft.cotton.di.component.PermissionComponent;
import org.satorysoft.cotton.di.component.PreferenceComponent;
import org.satorysoft.cotton.di.module.PermissionModule;
import org.satorysoft.cotton.di.module.PreferenceModule;
import org.satorysoft.cotton.util.ApplicationRiskUtil;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.DrawableConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ApplicationScannerService extends IntentService {
    private PackageManager mPackageManager;
    private PermissionComponent mPermissionComponent;
    public static final CharSequence ARRAY_DIVIDER = "__,__";
    private PreferenceComponent mPreferenceComponent;

    public ApplicationScannerService() {
        super(ApplicationScannerService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER);
        this.mPermissionComponent = DaggerPermissionComponent.builder()
                .permissionModule(new PermissionModule(getApplicationContext())).build();
        this.mPackageManager = mPermissionComponent.getPackageManager();
        int progress = 0;
        //after each scanned application and saved into database we have to send status and progress
        final List<ScannedApplication> scannedApplications = new ArrayList<>();

        for(ApplicationInfo applicationInfo : getInstalledApplications()){
            InstalledApplication installedApplication = new InstalledApplication();
            ScannedApplication scannedApplication = new ScannedApplication();

            if(!isSystemApplication(applicationInfo)){
                String applicationName = applicationInfo.loadLabel(mPackageManager).toString();
                String packageName = applicationInfo.packageName;
                Drawable applicationIcon = applicationInfo.loadIcon(mPackageManager);

                byte[] imageRepresentation = new DrawableConverter().convertDrawable(applicationIcon);

                try {
                    PackageInfo permissionsInfo = mPackageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
                    String[] applicationPermissions = permissionsInfo.requestedPermissions;

                    double applicationRiskRate = (double)new ApplicationRiskUtil(getDangerousPermissions())
                            .evaluateApplicationRisk(applicationPermissions);

                    installedApplication.setApplicationName(applicationName);
                    installedApplication.setPackageName(packageName);
                    installedApplication.setApplicationIconBytes(imageRepresentation);
                    installedApplication.setApplicationRiskRate(applicationRiskRate);
                    installedApplication.setApplicationPermissions(applicationPermissions);
                    scannedApplication.setInstalledApplication(installedApplication);
                    scannedApplication.setScanDate(System.currentTimeMillis());
                    scannedApplications.add(scannedApplication);

                    progress++;

                    Bundle progressBundle = new Bundle();
                    progressBundle.putInt(Constants.SCAN_RESULT_PROGRESS, progress);
                    receiver.send(Constants.STATUS_RUNNING, progressBundle);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if(scannedApplications.size() > 0){
            saveScanResultToDatabase(scannedApplications);
            this.mPreferenceComponent = DaggerPreferenceComponent.builder()
                    .preferenceModule(new PreferenceModule(getApplicationContext())).build();
            PreferenceModule.BooleanPreference preference = mPreferenceComponent.getBooleanPreference();
            preference.setValue(Constants.SCAN_ON_FIRST_RUN_DONE, true);

            receiver.send(Constants.STATUS_FINISHED, Bundle.EMPTY);

            stopSelf();
        }
    }

    private boolean isSystemApplication(ApplicationInfo applicationInfo){
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private List<ApplicationInfo> getInstalledApplications(){
        return mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    private List<String> getDangerousPermissions(){
        return mPermissionComponent.getPermissionList().getHighRiskPermissions();
    }

    private void saveScanResultToDatabase(List<ScannedApplication> scannedApplications){
        for(ScannedApplication scannedApplication : scannedApplications){
            ContentValues values = new ContentValues();
            values.put(ScannedApplicationContract.APPLICATION_NAME, scannedApplication
                    .getInstalledApplication()
                    .getApplicationName());
            values.put(ScannedApplicationContract.PACKAGE_NAME, scannedApplication
                    .getInstalledApplication()
                    .getPackageName());
            values.put(ScannedApplicationContract.APPLICATION_ICON, scannedApplication
                    .getInstalledApplication()
                    .getApplicationIconBytes());
            values.put(ScannedApplicationContract.APPLICATION_RISK_RATE, scannedApplication
                    .getInstalledApplication()
                    .getApplicationRiskRate());
            String[] permissions = scannedApplication.getInstalledApplication().getApplicationPermissions();
            if (permissions == null){
                values.put(ScannedApplicationContract.APPLICATION_PERMISSIONS, TextUtils.join(ARRAY_DIVIDER, new String[]{}));
            } else {
                values.put(ScannedApplicationContract.APPLICATION_PERMISSIONS, TextUtils.join(ARRAY_DIVIDER, permissions));
            }
            values.put(ScannedApplicationContract.SCAN_DATE, scannedApplication.getScanDate());

            getContentResolver().insert(ScannedApplicationContract.CONTENT_URI, values);
        }
    }
}
