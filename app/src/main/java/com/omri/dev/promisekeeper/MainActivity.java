package com.omri.dev.promisekeeper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.omri.dev.promisekeeper.DAL.PromisesDAL;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.PromisesCheckManager.PromisesAlarmsShooter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CREATE_PROMISE_REQUEST_CODE = 1;
    private static final int PERMISSIONS_REQUEST_CODE = 2;
    private static final int PERMISSION_REQUEST_ACTIVITY_RESULT = 3;

    private static final String[] PERMISSIONS_LIST = new String[]{ Manifest.permission.READ_CALL_LOG,
                                                          Manifest.permission.ACCESS_FINE_LOCATION,
                                                          Manifest.permission.ACCESS_COARSE_LOCATION,
                                                          Manifest.permission.SEND_SMS };

    private RecyclerView mRecyclerView;
    private PromisesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private PromisesDAL mPromisesDAL;

    private PromisesAlarmsShooter mPromisesAlarmsShooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CreatePromiseActivity.class);
                startActivityForResult(i, CREATE_PROMISE_REQUEST_CODE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkForPermissions();

        mPromisesAlarmsShooter = new PromisesAlarmsShooter(getApplicationContext());

        mRecyclerView = (RecyclerView)findViewById(R.id.promises_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PromisesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mPromisesDAL = new PromisesDAL(getApplicationContext());

        loadFuturePromises();

        showAboutActivityIfFirstTime();
    }

    private void showAboutActivityIfFirstTime() {
        SharedPreferences pref = getSharedPreferences("PromiseKeeperPref", Context.MODE_PRIVATE);
        String about_activity_execution_code = "about_activity_executed";

        if(pref.getBoolean(about_activity_execution_code, true)){
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);

            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean(about_activity_execution_code, false);
            ed.apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        refetchPromises();
    }

    private void checkForPermissions() {
        // Check if there is at least one permission that is not granted
        boolean isSomePermissionDenied = false;

        for (int i = 0; i < PERMISSIONS_LIST.length && !isSomePermissionDenied ;++i) {
            isSomePermissionDenied = ActivityCompat.checkSelfPermission(this, PERMISSIONS_LIST[i]) == PackageManager.PERMISSION_DENIED;
        }

        if (isSomePermissionDenied) {
            // Inform the user
            Intent i = new Intent(this, PermissionRequestActivity.class);
            startActivityForResult(i, PERMISSION_REQUEST_ACTIVITY_RESULT);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_future_promises: {
                loadFuturePromises();

                break;
            }
            case R.id.nav_fulfilled_promises: {
                loadFulfilledPromises();

                break;
            }
            case R.id.nav_unfulfilled_promises: {
                loadUnfulfilledPromises();

                break;
            }
            case R.id.nav_about: {
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);

                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CREATE_PROMISE_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    PromiseListItem newPromise = new PromiseListItem(data);
                    mPromisesDAL.createNewPromise(newPromise);
                    loadFuturePromises();

                    mPromisesAlarmsShooter.createAnAlarmForPromise(newPromise);
                }

                break;
            }
            case PERMISSION_REQUEST_ACTIVITY_RESULT: {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, PERMISSIONS_REQUEST_CODE);

                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length == PERMISSIONS_LIST.length){
                    boolean isSomePermissionDenied = false;

                    for (int i = 0; i < grantResults.length && !isSomePermissionDenied; i++) {
                        isSomePermissionDenied = (grantResults[i] == PackageManager.PERMISSION_DENIED);
                    }

                    if (isSomePermissionDenied){
                        // Re-ask the user
                        checkForPermissions();
                    }
                }
            }
        }
    }

    private void refetchPromises() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // If future promises is checked
        if (navigationView.getMenu().findItem(R.id.nav_future_promises).isChecked()) {
            loadFuturePromises();
        } else if (navigationView.getMenu().findItem(R.id.nav_fulfilled_promises).isChecked()) {
            loadFulfilledPromises();
        } else if (navigationView.getMenu().findItem(R.id.nav_unfulfilled_promises).isChecked()) {
            loadUnfulfilledPromises();
        }
    }

    private void loadFuturePromises() {
        setTitle(R.string.future_promises);
        mAdapter.updatePromisesDataSet(mPromisesDAL.getFuturePromises());
    }

    private void loadFulfilledPromises() {
        setTitle(R.string.fulfilled_promises);
        mAdapter.updatePromisesDataSet(mPromisesDAL.getFulfilledPromises());
    }

    private void loadUnfulfilledPromises() {
        setTitle(R.string.unfulfilled_promises);
        mAdapter.updatePromisesDataSet(mPromisesDAL.getUnfulfilledPromises());
    }
}
