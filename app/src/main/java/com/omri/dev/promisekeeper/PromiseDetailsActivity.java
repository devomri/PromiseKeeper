package com.omri.dev.promisekeeper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.omri.dev.promisekeeper.DAL.PromisesDAL;
import com.omri.dev.promisekeeper.Model.PromiseEnumConvrsions;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseStatus;
import com.omri.dev.promisekeeper.Model.PromiseTypes;
import com.omri.dev.promisekeeper.Utils.DateUtils;

import java.util.Date;

public class PromiseDetailsActivity extends AppCompatActivity {
    private TextView promiseTitleTextView;
    private TextView promiseDescriptionTextView;
    private TextView promiseTypeTextView;
    private TextView promiseStatusTextView;
    private TextView promiseInterval;
    private TextView promiseBaseTime;
    private LinearLayout promiseGuardContactLayout;
    private TextView promiseGuardContactTextView;
    private LinearLayout promiseCallContactLayout;
    private TextView promiseCallContactTextView;
    private LinearLayout promiseLocationLayout;
    private TextView promiseLocationTextView;
    private LinearLayout promiseGeneralAskLayout;

    private PromiseListItem mPromise;
    private PromisesDAL promisesDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promise_details);

        promisesDAL = new PromisesDAL(getApplicationContext());

        promiseTitleTextView = (TextView)findViewById(R.id.promise_details_title);
        promiseDescriptionTextView = (TextView) findViewById(R.id.promise_details_description);
        promiseTypeTextView = (TextView)findViewById(R.id.promise_details_promise_type);
        promiseStatusTextView = (TextView)findViewById(R.id.promise_details_promise_status);
        promiseInterval = (TextView)findViewById(R.id.promise_details_promise_interval);
        promiseBaseTime = (TextView)findViewById(R.id.promise_details_promise_base_time);
        promiseGuardContactLayout = (LinearLayout)findViewById(R.id.promise_details_guard_contact_layout);
        promiseGuardContactTextView = (TextView)findViewById(R.id.promise_details_guard_contact);
        promiseCallContactLayout = (LinearLayout)findViewById(R.id.promise_details_call_contact_layout);
        promiseCallContactTextView = (TextView)findViewById(R.id.promise_details_call_contact);
        promiseLocationLayout = (LinearLayout)findViewById(R.id.promise_details_location_layout);
        promiseLocationTextView = (TextView)findViewById(R.id.promise_details_location);
        promiseGeneralAskLayout = (LinearLayout)findViewById(R.id.promise_details_general_layout);

        mPromise = new PromiseListItem(getIntent());

        promiseTitleTextView.setText(mPromise.getmTitle());
        promiseDescriptionTextView.setText(mPromise.getmDescription());
        promiseBaseTime.setText(mPromise.getmBaseTime());

        promiseTypeTextView.setText(PromiseEnumConvrsions.convertIntToPromiseTypeText(mPromise.getmPromiseType()));

        promiseStatusTextView.setText(PromiseEnumConvrsions.convertIntToPromiseStatusString(mPromise.getmPromiseStatus()));

        promiseInterval.setText(PromiseEnumConvrsions.convertIntToPromiseIntervalText(mPromise.getmPromiseInterval()));

        String guardContactNumber = mPromise.getmGuardContactNumber();
        if (!guardContactNumber.equals("")) {
            promiseGuardContactTextView.setText(guardContactNumber);
            promiseGuardContactLayout.setVisibility(View.VISIBLE);
        }

        switch (mPromise.getmPromiseType()) {
            case GENERAL: {
                Date now = new Date();
                Date promiseDate = DateUtils.convertStringToDate(mPromise.getmBaseTime());
                if (promiseDate.before(now) && mPromise.getmPromiseStatus() == PromiseStatus.ACTIVE) {
                    promiseGeneralAskLayout.setVisibility(View.VISIBLE);
                }

                break;
            }
            case LOCATION: {
                promiseLocationTextView.setText(mPromise.getmLocation());
                promiseLocationLayout.setVisibility(View.VISIBLE);

                break;
            }
            case CALL: {
                promiseCallContactTextView.setText(mPromise.getmCallContactNumber());
                promiseCallContactLayout.setVisibility(View.VISIBLE);

                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Delete option only for future promises
        if (mPromise.getmPromiseStatus() == PromiseStatus.ACTIVE) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings_delete) {
            deletePromiseIfSure();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deletePromiseIfSure() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE: {
                        // Deletes the promise from the DB
                        promisesDAL.deleteFuturePromise(mPromise.getmPromiseID());

                        // Notify the guard (if exists)
                        mPromise.sendMessageToGuard("I have just deleted my promise.");

                        finish();
                    }
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void promiseKept(View view) {
        changePromiseStatus(PromiseStatus.FULFILLED);
    }

    public void promiseIsNotKept(View view) {
        changePromiseStatus(PromiseStatus.UNFULFILLED);
    }

    private void changePromiseStatus(PromiseStatus status) {
        PromiseListItem currPromise = promisesDAL.getPromiseByID(mPromise.getmPromiseID());
        if (currPromise.getmPromiseStatus() == PromiseStatus.ACTIVE) {
            if (status == PromiseStatus.FULFILLED) {
                promisesDAL.markPromisefulfilled(mPromise.getmPromiseID());
            } else if (status == PromiseStatus.UNFULFILLED) {
                promisesDAL.markPromiseAsUnfulfilled(mPromise.getmPromiseID());

                mPromise.sendUnfulfilledPromiseToGuard();
            }

            promisesDAL.createNextPromiseIfNecessary(mPromise);
        }

        finish();
    }
}

