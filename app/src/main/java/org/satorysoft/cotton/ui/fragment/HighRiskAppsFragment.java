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
import org.satorysoft.cotton.adapter.ApplicationRiskAdapter;
import org.satorysoft.cotton.core.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.core.model.InstalledApplication;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.di.component.AdapterComponent;
import org.satorysoft.cotton.di.component.DaggerAdapterComponent;
import org.satorysoft.cotton.di.module.AdapterModule;
import org.satorysoft.cotton.ui.animator.SlideInFromLeftItemAnimator;
import org.satorysoft.cotton.ui.fragment.base.BaseApplicationListFragment;
import org.satorysoft.cotton.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class HighRiskAppsFragment extends BaseApplicationListFragment {
    @FindView(R.id.recycler)
    protected RecyclerView recycler;

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
        populateListView(recycler, getActivity(), Constants.MODERATE_RISK, Constants.HIGH_RISK);
    }
}
