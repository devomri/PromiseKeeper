package com.omri.dev.promisekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.omri.dev.promisekeeper.Model.PromiseIntervals;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseStatus;
import com.omri.dev.promisekeeper.Model.PromiseTypes;
import com.omri.dev.promisekeeper.PromisesCheckManager.PromisesAlarmsShooter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CREATE_PROMISE_REQUEST_CODE = 1;

    private RecyclerView mRecyclerView;
    private PromisesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private PromisesDAL mPromisesDAL;
    private List<PromiseListItem> mFuturePromises;
    private List<PromiseListItem> mFulfilledPromises;
    private List<PromiseListItem> mUnfulfilledPromises;

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

        mPromisesAlarmsShooter = new PromisesAlarmsShooter(getApplicationContext());

        mRecyclerView = (RecyclerView)findViewById(R.id.promises_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PromisesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mPromisesDAL = new PromisesDAL();

        fetchPromisesFromDB();

        loadFuturePromises();
    }

    private void fetchPromisesFromDB() {
        mFuturePromises = mPromisesDAL.getFuturePromises();
        mFulfilledPromises = mPromisesDAL.getFulfilledPromises();
        mUnfulfilledPromises = mPromisesDAL.getUnfulfilledPromises();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_future_promises:
                loadFuturePromises();

                break;
            case R.id.nav_fulfilled_promises:
                loadFulfilledPromises();

                break;
            case R.id.nav_unfulfilled_promises:
                loadUnfulfilledPromises();

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CREATE_PROMISE_REQUEST_CODE) {
                PromiseListItem newPromise = new PromiseListItem(data);
                mFuturePromises.add(newPromise);
                mAdapter.notifyDataSetChanged();

                mPromisesAlarmsShooter.createAnAlarmForPromise(newPromise);
            }
        }
    }

    private void loadFuturePromises() {
        setTitle(R.string.future_promises);
        mAdapter.updatePromisesDataSet(mFuturePromises);
    }

    private void loadFulfilledPromises() {
        setTitle(R.string.fulfilled_promises);
        mAdapter.updatePromisesDataSet(mFulfilledPromises);
    }

    private void loadUnfulfilledPromises() {
        setTitle(R.string.unfulfilled_promises);
        mAdapter.updatePromisesDataSet(mUnfulfilledPromises);
    }
}
