package com.omri.dev.promisekeeper;

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

import com.omri.dev.promisekeeper.Model.PromiseListItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private PromisesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<PromiseListItem> mFuturePromises;
    private List<PromiseListItem> mFulfillesPromises;
    private List<PromiseListItem> mUnfulfilledPromises;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.promises_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PromisesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        fetchPromisesFromDB();

        loadFuturePromises();
    }

    private void fetchPromisesFromDB() {
        mFuturePromises = new ArrayList<>();
        mFuturePromises.add(new PromiseListItem("Future promise 1",
                "I will try to keep my promises next time I promise them.",
                "01/01/2017",
                "01/08/2017"));
        mFuturePromises.add(new PromiseListItem("Future promise 2",
                "This is the second promise",
                "01/01/2017",
                "01/09/2017"));

        mFulfillesPromises = new ArrayList<>();
        mFulfillesPromises.add(new PromiseListItem("Fulfilled Promise 1",
                                                    "This is the text of fulfilled promise 1",
                                                    "01/01/2001",
                                                    "01/02/2001"));

        mUnfulfilledPromises = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            mUnfulfilledPromises.add(new PromiseListItem("Unfulfilled Promise 1",
                    "Not fulfilled 1",
                    "01/01/2001",
                    "01/02/2001"));
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

    private void loadFuturePromises() {
        setTitle(R.string.future_promises);
        mAdapter.loadFuturePromises(mFuturePromises);
    }

    private void loadFulfilledPromises() {
        setTitle(R.string.fulfilled_promises);
        mAdapter.loadFuturePromises(mFulfillesPromises);
    }

    private void loadUnfulfilledPromises() {
        setTitle(R.string.unfulfilled_promises);
        mAdapter.loadFuturePromises(mUnfulfilledPromises);
    }
}
