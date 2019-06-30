package com.ibnsaad.thedcc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.navigation.NavigationView;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.adapter.UsersAdapterGridScrollProgress;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.fragments.DoctorsFragment;
import com.ibnsaad.thedcc.fragments.PatientHomeFragment;
import com.ibnsaad.thedcc.fragments.ProfileFragment;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.User;
import com.ibnsaad.thedcc.utils.Tools;

import java.util.List;

public class HomeActivity extends SplashActivity {

    private static final String TAG = "HomeActivity";
    private int item_per_display = 10;
    private UsersAdapterGridScrollProgress mAdapter;
    private RecyclerView recyclerView;
    private List<User> users;
    private ActionBar actionBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolbar();
        initNavigationMenu();
        // initComponent();
        if (SharedHelper.getKey(this, Enums.UserType.name()).equals(getString(R.string.doctors))) {
            replaceFragment(DoctorsFragment.getInstance());
        } else {

            replaceFragment(PatientHomeFragment.getInstance());
        }

//        replaceFragment(PatientHomeFragment.getInstance());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Doctors");
        Tools.setSystemBarColor(this);
    }

    private void initNavigationMenu() {
        NavigationView nav_view = findViewById(R.id.nav_view);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                drawer.closeDrawers();
                onSelected(item);
                actionBar.setTitle(item.getTitle());
                Log.d(TAG, "onNavigationItemSelected: " + item.getTitle());
                return true;
            }
        });

        nav_view.setCheckedItem(R.id.nav_home);
        View header = nav_view.getHeaderView(0);
        TextView userName = header.findViewById(R.id.user_name);
        TextView specialization = header.findViewById(R.id.specialization);
        SimpleDraweeView userImage = header.findViewById(R.id.user_image);
        userName.setText(SharedHelper.getKey(this, Enums.KnownAs.name()));
        userImage.setImageURI(SharedHelper.getKey(this, Enums.PhotoUrl.name()));
        userImage.setImageURI(SharedHelper.getKey(this, Enums.PhotoUrl.name()));
        specialization.setText(SharedHelper.getKey(this, Enums.Spetialization.name()));
        // open drawer at start
        //   drawer.openDrawer(GravityCompat.START);

    }

    public void onSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.nav_home:
                if (SharedHelper.getKey(this, Enums.UserType.name()).equals(getString(R.string.doctors))) {
                    replaceFragment(DoctorsFragment.getInstance());
                } else {
                    replaceFragment(PatientHomeFragment.getInstance());
                }

                break;
            case R.id.nav_profile:
                replaceFragment(new ProfileFragment(SharedHelper.getKey(this, Enums.ID.name()), true));
                break;
            case R.id.nav_out:
                SharedHelper.clearSharedPreferences(this);
                SharedHelper.putBoolean(this, Enums.IS_LOG_IN.name(), false);
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                break;

        }

    }


}
