package com.omri.dev.promisekeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PromiseDetailsActivity extends Activity {
    private TextView promiseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promise_details);

        promiseTextView = (TextView)findViewById(R.id.promise_details_title);

        Intent i = getIntent();
        String promiseTitle = i.getStringExtra("title");
        promiseTextView.setText(promiseTitle);
    }
}

