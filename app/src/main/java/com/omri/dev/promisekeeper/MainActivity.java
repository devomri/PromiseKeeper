package com.omri.dev.promisekeeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.DateKeyListener;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.omri.dev.promisekeeper.DAL.PromisesDAL;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.PromisesCheckManager.PromisesAlarmsShooter;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CREATE_PROMISE_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST = 2;
    private static final int PERMISSION_REQUEST_ACTIVITY_RESULT = 3;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        refetchPromises();
    }

    private void checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
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
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALL_LOG,
                                     Manifest.permission.ACCESS_FINE_LOCATION,
                                     Manifest.permission.ACCESS_COARSE_LOCATION,
                                     Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST);

                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length == 3){
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED ||
                        grantResults[1] == PackageManager.PERMISSION_DENIED ||
                        grantResults[2] == PackageManager.PERMISSION_DENIED){
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
        } else if (navigationView.getMenu().findItem(R.id.nav_future_promises).isChecked()) {
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
