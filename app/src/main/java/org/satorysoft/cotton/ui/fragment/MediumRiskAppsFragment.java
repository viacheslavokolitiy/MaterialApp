package org.satorysoft.cotton.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.satorysoft.cotton.R;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class MediumRiskAppsFragment extends Fragment {
    public static MediumRiskAppsFragment newInstance() {
        return new MediumRiskAppsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medium_risk, container, false);
    }
}
