package org.satorysoft.cotton.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.util.ActionBarOwner;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 30.06.2015.
 */
public class RestorePhotosActivity extends AppCompatActivity {
    @Bind(R.id.toolbar_restore_photos)
    protected Toolbar restorePhotosBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_photos);
        ButterKnife.bind(this);
        setSupportActionBar(restorePhotosBar);
        new ActionBarOwner(this).setCustomActionBarTitle(getSupportActionBar(), getString(R.string.text_toolbar_restore_photos));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
