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
public class MediumRiskAppsFragment extends BaseApplicationListFragment {
    @FindView(R.id.recycler_moderate_risk_apps)
    protected RecyclerView moderateAppsRiskRecycler;
    public static MediumRiskAppsFragment newInstance() {
        return new MediumRiskAppsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medium_risk, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateListView(moderateAppsRiskRecycler, getActivity(), Constants.MODERATE_RISK_LOWER, Constants.MODERATE_RISK);
    }
}
