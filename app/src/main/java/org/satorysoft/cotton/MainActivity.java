package org.satorysoft.cotton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.satorysoft.cotton.ui.activity.ApplicationListActivity;
import org.satorysoft.cotton.ui.activity.ApplicationScanActivity;
import org.satorysoft.cotton.util.ScanChecker;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        EventBus.getDefault().register(this);
        ScanChecker scanChecker = new ScanChecker(this);
        scanChecker.check();

    }

    public void onEvent(ScanChecker.ShowApplicationScanScreenEvent event){
        startActivity(new Intent(MainActivity.this, ApplicationScanActivity.class));
    }

    public void onEvent(ScanChecker.ShowApplicationListScreenEvent event){
        startActivity(new Intent(MainActivity.this, ApplicationListActivity.class));
    }
}
