package org.satorysoft.cotton.ui.fragment.base;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import org.satorysoft.cotton.adapter.ApplicationRiskAdapter;
import org.satorysoft.cotton.core.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.core.model.InstalledApplication;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.di.component.AdapterComponent;
import org.satorysoft.cotton.di.component.DaggerAdapterComponent;
import org.satorysoft.cotton.di.module.AdapterModule;
import org.satorysoft.cotton.ui.animator.SlideInFromLeftItemAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslavokolitiy on 09.06.2015.
 */
public abstract class BaseApplicationListFragment extends Fragment {
    protected AdapterComponent adapterComponent;
    protected RecyclerViewMaterialAdapter mAdapter;

    protected void populateListView(RecyclerView recyclerView, Context context, double lowerRisk, double higherRisk){
        Cursor cursor = context.getContentResolver().query(ScannedApplicationContract.CONTENT_URI,
                null, null, null, null);
        List<ScannedApplication> scannedApplicationList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                String applicationName = cursor.getString(cursor.getColumnIndex(ScannedApplicationContract.APPLICATION_NAME));
                byte[] value = cursor.getBlob(cursor.getColumnIndex(ScannedApplicationContract.APPLICATION_ICON));
                double risk = cursor.getDouble(cursor.getColumnIndex(ScannedApplicationContract.APPLICATION_RISK_RATE));
                ScannedApplication scannedApplication = new ScannedApplication();
                InstalledApplication installedApplication = new InstalledApplication();
                installedApplication.setApplicationName(applicationName);
                installedApplication.setApplicationIconBytes(value);
                installedApplication.setApplicationRiskRate(risk);
                scannedApplication.setInstalledApplication(installedApplication);
                scannedApplicationList.add(scannedApplication);
            } while (cursor.moveToNext());
        }

        if(cursor != null){
            cursor.close();
        }

        this.adapterComponent = DaggerAdapterComponent.builder().adapterModule(new AdapterModule(getActivity())).build();

        ApplicationRiskAdapter adapter = adapterComponent.getAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInFromLeftItemAnimator(recyclerView));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        for(ScannedApplication scannedApplication : scannedApplicationList){
            if(scannedApplication.getInstalledApplication().getApplicationRiskRate() > lowerRisk
                    && scannedApplication.getInstalledApplication().getApplicationRiskRate() <= higherRisk){
                adapter.addItem(scannedApplication);
            }
        }

        mAdapter = new RecyclerViewMaterialAdapter(adapter);

        recyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);
    }
}
