package com.raghu.android.wakemeup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.raghu.android.wakemeup.Activities.ProgressActivity;
import com.raghu.android.wakemeup.PagerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.raghu.android.wakemeup.R.*;


public class TabsActivity extends AppCompatActivity {

    private static final String TAG = "logs";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.raghu.android.wakemeup.R.layout.activity_tabs);
        Toolbar toolbar = (Toolbar) findViewById(com.raghu.android.wakemeup.R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        MobileAds.initialize(this, "ca-app-pub-9326529278822780~1309642359");

        mDrawerLayout = (DrawerLayout)findViewById(com.raghu.android.wakemeup.R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(TabsActivity.this, mDrawerLayout, toolbar, com.raghu.android.wakemeup.R.string.drawer_open, com.raghu.android.wakemeup.R.string.drawer_close);
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        TabLayout tabLayout = (TabLayout) findViewById(com.raghu.android.wakemeup.R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Todo's"));
        tabLayout.addTab(tabLayout.newTab().setText("Time Table"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(com.raghu.android.wakemeup.R.id.pager);

        mAdView = findViewById(com.raghu.android.wakemeup.R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        NavigationView navigationView = findViewById(com.raghu.android.wakemeup.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        switch(id){

                            case com.raghu.android.wakemeup.R.id.nav_settings :
                                Intent settingsIntent = new Intent(TabsActivity.this, SettingsActivity.class);
                                startActivity(settingsIntent);
                                break;

                            case com.raghu.android.wakemeup.R.id.nav_help:
                                Intent helpIntent = new Intent(TabsActivity.this, HelpActivity.class);
                                startActivity(helpIntent);
                                break;
                                
                            case com.raghu.android.wakemeup.R.id.nav_progress :
                                Intent progressIntent = new Intent(TabsActivity.this, ProgressActivity.class);
                                startActivity(progressIntent);
                        }

                        return true;
                    }
                });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}