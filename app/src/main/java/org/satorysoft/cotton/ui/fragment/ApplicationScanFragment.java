package org.satorysoft.cotton.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.receiver.ScanResultReceiver;
import org.satorysoft.cotton.core.service.ApplicationScannerService;
import org.satorysoft.cotton.ui.activity.ApplicationListActivity;
import org.satorysoft.cotton.util.Constants;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ApplicationScanFragment extends Fragment implements ScanResultReceiver.Receiver{
    @FindView(R.id.arc_progress_scan_apps)
    protected ArcProgress scanProgress;
    private ScanResultReceiver mReceiver;

    public static ApplicationScanFragment newInstance(){
        return new ApplicationScanFragment();
    }

    public ApplicationScanFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_scan, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();
        mReceiver = new ScanResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        final Intent intent = new Intent(Constants.IntentActions.SCAN_APPS_INTENT, null, getActivity(), ApplicationScannerService.class);
        intent.putExtra(Constants.RECEIVER, mReceiver);
        getActivity().startService(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode){
            case Constants.STATUS_RUNNING:
                int progress = data.getInt(Constants.SCAN_RESULT_PROGRESS);
                scanProgress.setProgress(progress);
                break;
            case Constants.STATUS_FINISHED:
                scanProgress.setProgress(Constants.SCAN_FINISHED);
                getActivity().startActivity(new Intent(getActivity(), ApplicationListActivity.class));
                break;
        }
    }
}
