package com.omri.dev.promisekeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omri.dev.promisekeeper.Model.PromiseEnumConvrsions;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseTypes;

public class PromiseDetailsActivity extends Activity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promise_details);

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

        PromiseListItem promise = new PromiseListItem(getIntent());

        promiseTitleTextView.setText(promise.getmTitle());
        promiseDescriptionTextView.setText(promise.getmDescription());
        promiseBaseTime.setText(promise.getmBaseTime());

        promiseTypeTextView.setText(PromiseEnumConvrsions.convertIntToPromiseTypeText(promise.getmPromiseType()));

        promiseStatusTextView.setText(PromiseEnumConvrsions.convertIntToPromiseStatusString(promise.getmPromiseStatus()));

        promiseInterval.setText(PromiseEnumConvrsions.convertIntToPromiseIntervalText(promise.getmPromiseInterval()));

        String guardContactNumber = promise.getmGuardContactNumber();
        if (!guardContactNumber.equals("")) {
            promiseGuardContactTextView.setText(guardContactNumber);
            promiseGuardContactLayout.setVisibility(View.VISIBLE);
        }

        if (promise.getmPromiseType() == PromiseTypes.LOCATION) {
            promiseLocationTextView.setText(promise.getmLocation());
            promiseLocationLayout.setVisibility(View.VISIBLE);
        } else if (promise.getmPromiseType() == PromiseTypes.CALL) {
            promiseCallContactTextView.setText(promise.getmCallContactNumber());
            promiseCallContactLayout.setVisibility(View.VISIBLE);
        }
    }
}

