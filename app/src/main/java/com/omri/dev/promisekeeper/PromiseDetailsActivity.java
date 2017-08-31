package com.omri.dev.promisekeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omri.dev.promisekeeper.Model.PromiseEnumConvrsions;

public class PromiseDetailsActivity extends Activity {
    private TextView promiseTitleTextView;
    private TextView promiseNextDateTextView;
    private TextView promiseDescriptionTextView;
    private TextView promiseTypeTextView;
    private TextView promiseInterval;
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
        promiseNextDateTextView = (TextView) findViewById(R.id.promise_details_next_date);
        promiseDescriptionTextView = (TextView) findViewById(R.id.promise_details_description);
        promiseTypeTextView = (TextView)findViewById(R.id.promise_details_promise_type);
        promiseInterval = (TextView)findViewById(R.id.promise_details_promise_interval);
        promiseGuardContactLayout = (LinearLayout)findViewById(R.id.promise_details_guard_contact_layout);
        promiseGuardContactTextView = (TextView)findViewById(R.id.promise_details_guard_contact);
        promiseCallContactLayout = (LinearLayout)findViewById(R.id.promise_details_call_contact_layout);
        promiseCallContactTextView = (TextView)findViewById(R.id.promise_details_call_contact);
        promiseLocationLayout = (LinearLayout)findViewById(R.id.promise_details_location_layout);
        promiseLocationTextView = (TextView)findViewById(R.id.promise_details_location);

        Intent i = getIntent();
        promiseTitleTextView.setText(i.getStringExtra("title"));
        promiseNextDateTextView.setText(i.getStringExtra("nextDate"));
        promiseDescriptionTextView.setText(i.getStringExtra("description"));

        int promiseTypeInt = i.getIntExtra("type", 0);
        promiseTypeTextView.setText(PromiseEnumConvrsions.convertIntToPromiseType(promiseTypeInt));

        int promiseIntervalInt = i.getIntExtra("interval", 0);
        promiseInterval.setText(PromiseEnumConvrsions.convertIntoToPromiseInterval(promiseIntervalInt));

        String guardContactNumber = i.getStringExtra("guardContact");
        if (!guardContactNumber.equals("")) {
            promiseGuardContactTextView.setText(guardContactNumber);
            promiseGuardContactLayout.setVisibility(View.VISIBLE);
        }

        if (promiseIntervalInt == 1) {
            promiseLocationTextView.setText(i.getStringExtra("location"));
            promiseLocationLayout.setVisibility(View.VISIBLE);
        } else if (promiseTypeInt == 2) {
            promiseCallContactTextView.setText(i.getStringExtra("callContact"));
            promiseCallContactLayout.setVisibility(View.VISIBLE);
        }
    }
}

