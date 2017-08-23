package com.omri.dev.promisekeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PromiseDetailsActivity extends Activity {
    private TextView promiseTitleTextView;
    private TextView promiseNextDateTextView;
    private TextView promiseDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promise_details);

        promiseTitleTextView = (TextView)findViewById(R.id.promise_details_title);
        promiseNextDateTextView = (TextView) findViewById(R.id.promise_details_next_date);
        promiseDescriptionTextView = (TextView) findViewById(R.id.promise_details_description);

        Intent i = getIntent();
        promiseTitleTextView.setText(i.getStringExtra("title"));
        promiseNextDateTextView.setText(i.getStringExtra("nextDate"));
        promiseDescriptionTextView.setText(i.getStringExtra("description"));


    }
}

