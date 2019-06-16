package com.ibnsaad.thedcc.activities;

import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.adapter.UsersAdapterGridScrollProgress;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.fragments.PatientHomeFragment;
import com.ibnsaad.thedcc.fragments.ProfileFragment;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.User;
import com.ibnsaad.thedcc.utils.Tools;
import com.ibnsaad.thedcc.widget.SpacingItemDecoration;

import java.util.ArrayList;
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


        replaceFragment(PatientHomeFragment.getInstance());
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Doctors");
        Tools.setSystemBarColor(this);
    }

    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        SimpleDraweeView userImage = header.findViewById(R.id.user_image);
        userName.setText(SharedHelper.getKey(this, Enums.KnownAs.name()));
        userImage.setImageURI(SharedHelper.getKey(this, Enums.PhotoUrl.name()));
        // open drawer at start
        //   drawer.openDrawer(GravityCompat.START);

    }

    public void onSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.nav_home:
                replaceFragment(PatientHomeFragment.getInstance());
                break;
            case R.id.nav_profile:
                replaceFragment(ProfileFragment.getInstance());
                break;
            case R.id.nav_out:
                SharedHelper.putBoolean(this, Enums.IS_LOG_IN.name(), false);
                recreate();
                break;

        }

    }

    private void initComponent() {


        users = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new UsersAdapterGridScrollProgress(this, item_per_display, generateListItems(item_per_display));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new UsersAdapterGridScrollProgress.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                loadNextData();
            }
        });

        mAdapter.setOnItemClickListener(new UsersAdapterGridScrollProgress.OnItemClickListener() {
            @Override
            public void onItemClick(View view, User obj, int position) {
                Toast.makeText(HomeActivity.this, obj.getKnownAs(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<User> generateListItems(int count) {
        List<User> users = new ArrayList<>();
        users.add(new User("Bassel faisal"));
        users.add(new User("Bassel faisal"));
        users.add(new User("eslam faisal"));
        users.add(new User("Bassel faisal"));
        users.add(new User("eslam faisal"));
        users.add(new User("Bassel faisal"));
        users.add(new User("Bassel faisal"));
        users.add(new User("eslam faisal"));
        users.add(new User("Bassel faisal"));
        users.add(new User("eslam faisal"));

        return users;
    }

    private void loadNextData() {
        mAdapter.setLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.insertData(generateListItems(item_per_display));
            }
        }, 2000);
    }

}
