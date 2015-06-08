package org.satorysoft.cotton.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.satorysoft.cotton.R;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ApplicationScanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_scan);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
}
