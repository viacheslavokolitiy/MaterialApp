package org.satorysoft.cotton.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.adapter.HighRiskApplicationsAdapter;
import org.satorysoft.cotton.core.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.core.model.InstalledApplication;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.di.component.AdapterComponent;
import org.satorysoft.cotton.di.component.DaggerAdapterComponent;
import org.satorysoft.cotton.di.module.AdapterModule;
import org.satorysoft.cotton.ui.animator.SlideInFromLeftItemAnimator;
import org.satorysoft.cotton.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class HighRiskAppsFragment extends Fragment {
    private AdapterComponent adapterComponent;

    @FindView(R.id.recycler)
    protected RecyclerView recycler;
    private RecyclerViewMaterialAdapter mAdapter;

    public static HighRiskAppsFragment newInstance() {
        return new HighRiskAppsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_risk_apps, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateListView(recycler, getActivity());
    }

    private void populateListView(RecyclerView recyclerView, Context context) {

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

        HighRiskApplicationsAdapter adapter = adapterComponent.getAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInFromLeftItemAnimator(recyclerView));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        for(ScannedApplication scannedApplication : scannedApplicationList){
            if(scannedApplication.getInstalledApplication().getApplicationRiskRate() > Constants.HIGH_RISK){
                adapter.addItem(scannedApplication);
            }
        }

        mAdapter = new RecyclerViewMaterialAdapter(adapter);

        recyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);
    }

}
