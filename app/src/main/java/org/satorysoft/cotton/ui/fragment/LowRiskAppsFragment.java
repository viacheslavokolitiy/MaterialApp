package org.satorysoft.cotton.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.ui.fragment.base.BaseApplicationListFragment;
import org.satorysoft.cotton.util.Constants;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class LowRiskAppsFragment extends BaseApplicationListFragment {
    @FindView(R.id.recycler_low_risk_apps)
    protected RecyclerView lowRiskAppsRecycler;
    public static LowRiskAppsFragment newInstance() {
        return new LowRiskAppsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_low_risk_apps, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        populateListView(lowRiskAppsRecycler, getActivity(), Constants.LOW_RISK, Constants.MODERATE_RISK_LOWER);
    }
}
