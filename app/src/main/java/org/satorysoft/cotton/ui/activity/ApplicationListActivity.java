package org.satorysoft.cotton.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.adapter.ApplicationRiskAdapter;
import org.satorysoft.cotton.core.FileFinder;
import org.satorysoft.cotton.core.gdrive.CallLogUploaderTask;
import org.satorysoft.cotton.core.gdrive.UploadPhotoTask;
import org.satorysoft.cotton.ui.fragment.HighRiskAppsFragment;
import org.satorysoft.cotton.ui.fragment.LowRiskAppsFragment;
import org.satorysoft.cotton.ui.fragment.MediumRiskAppsFragment;
import org.satorysoft.cotton.ui.fragment.dialog.MediaFileListDialog;
import org.satorysoft.cotton.util.FileUtils;
import org.satorysoft.cotton.util.GoogleAuthChecker;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ApplicationListActivity extends AppCompatActivity {
    private static final int BACKUP_PHOTOS = 0;
    private static final int BACKUP_CALL_HISTORY = 1;
    private static final int BACKUP_MUSIC = 2;
    private static final int BACKUP_MOVIES = 3;
    private static final int SCHEDULED_BACKUP = 4;
    private static final int RESTORE_DATA = 5;
    @Bind(R.id.materialViewPager)
    protected MaterialViewPager materialViewPager;
    private boolean isUserAuthenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        Toolbar toolbar = materialViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");

            new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withActionBarDrawerToggle(true)
                    .withHeader(R.layout.drawer_header)
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName(getString(R.string.text_drawer_backup_photo)).withIcon(FontAwesome.Icon.faw_photo),
                            new PrimaryDrawerItem().withName(getString(R.string.text_drawer_backup_call_history)).withIcon(FontAwesome.Icon.faw_mobile_phone),
                            new PrimaryDrawerItem().withName(getString(R.string.text_backup_music)).withIcon(FontAwesome.Icon.faw_file_audio_o),
                            new PrimaryDrawerItem().withName(getString(R.string.text_drawer_backup_movies)).withIcon(FontAwesome.Icon.faw_file_movie_o),
                            new PrimaryDrawerItem().withName(getString(R.string.text_drawer_scheduled_backup)).withIcon(FontAwesome.Icon.faw_clock_o),
                            new PrimaryDrawerItem().withName(getString(R.string.text_drawer_restore)).withIcon(FontAwesome.Icon.faw_refresh)
                    ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(AdapterView<?> parent, View view,
                                           int position, long id, IDrawerItem drawerItem) {
                    switch (position){
                        case BACKUP_PHOTOS:
                            checkAuth();
                            if(isUserAuthenticated){
                                startActivity(new Intent(ApplicationListActivity.this, BackupPhotoActivity.class));
                            }
                            return false;
                        case BACKUP_CALL_HISTORY:
                            checkAuth();
                            if(isUserAuthenticated){
                                initiateCallLogBackup();
                            }
                            return false;
                        case BACKUP_MUSIC:
                            checkAuth();
                            if(isUserAuthenticated){
                                FileFinder fileFinder = new FileFinder();
                                fileFinder.findFilesWithExtension(FileUtils.getFileExtensionList());
                            }
                            return false;
                        case BACKUP_MOVIES:
                            checkAuth();
                            if(isUserAuthenticated){
                                FileFinder fileFinder = new FileFinder();
                                fileFinder.findFilesWithExtension(FileUtils.mediaFormats());
                            }
                            return false;
                        case SCHEDULED_BACKUP:
                            checkAuth();
                            return false;
                        case RESTORE_DATA:
                            checkAuth();
                            return false;
                        default:
                            return false;
                    }
                }
            })
                    .build()
                    .setSelection(-1);
        }

        materialViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            int oldPosition = -1;

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return HighRiskAppsFragment.newInstance();
                    case 1:
                        return MediumRiskAppsFragment.newInstance();
                    case 2:
                        return LowRiskAppsFragment.newInstance();
                    default:
                        return HighRiskAppsFragment.newInstance();
                }
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);

                //only if position changed
                if (position == oldPosition)
                    return;
                oldPosition = position;

                int color = 0;
                switch (position) {
                    case 0:
                        color = getResources().getColor(R.color.md_red_300);
                        break;
                    case 1:
                        color = getResources().getColor(R.color.md_yellow_300);
                        break;
                    case 2:
                        color = getResources().getColor(R.color.md_green_300);
                        break;
                }

                String imageURL = "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg";
                final int fadeDuration = 100;
                materialViewPager.setImageUrl(imageURL,fadeDuration);
                materialViewPager.setColor(color, fadeDuration);

            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 4) {
                    case 0:
                        return getString(R.string.text_tab_first);
                    case 1:
                        return getString(R.string.text_tab_second);
                    case 2:
                        return getString(R.string.text_tab_third);
                }
                return "";
            }
        });

        materialViewPager.getViewPager().setOffscreenPageLimit(materialViewPager.getViewPager().getAdapter().getCount());
        materialViewPager.getPagerTitleStrip().setViewPager(materialViewPager.getViewPager());
        materialViewPager.getViewPager().setCurrentItem(0);
    }

    private void initiateCallLogBackup() {
        new CallLogUploaderTask(this).execute();
    }

    public void checkAuth(){
        GoogleAuthChecker googleAuthChecker = new GoogleAuthChecker(ApplicationListActivity.this);
        if(!googleAuthChecker.isUserAuthenticated()){
            isUserAuthenticated = false;
            startActivity(new Intent(ApplicationListActivity.this, GoogleAuthActivity.class));
        } else {
            isUserAuthenticated = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onEvent(ApplicationRiskAdapter.SelectedApplicationEvent event){
        startActivity(event.getIntent());
    }

    public void onEvent(FileFinder.MusicFileFoundEvent event){
        MediaFileListDialog.newInstance(event.getFoundedMediaFiles()).show(getSupportFragmentManager(), "dialog");
    }

    public void onEvent(UploadPhotoTask.FileUploadFailedEvent event){
        Toast.makeText(getBaseContext(), event.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void onEvent(UploadPhotoTask.UploadSuccessfulEvent event){
        Toast.makeText(getBaseContext(), event.getMessage(), Toast.LENGTH_LONG).show();
    }
}
