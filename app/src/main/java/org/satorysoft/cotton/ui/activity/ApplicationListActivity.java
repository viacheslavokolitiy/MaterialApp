package org.satorysoft.cotton.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.ui.fragment.HighRiskAppsFragment;
import org.satorysoft.cotton.ui.fragment.LowRiskAppsFragment;
import org.satorysoft.cotton.ui.fragment.MediumRiskAppsFragment;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ApplicationListActivity extends AppCompatActivity {
    @FindView(R.id.materialViewPager)
    protected MaterialViewPager materialViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        ButterKnife.bind(this);

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
                    .build();
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
                final int fadeDuration = 400;
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

    @Override
    protected void onStart() {
        super.onStart();
    }
}
