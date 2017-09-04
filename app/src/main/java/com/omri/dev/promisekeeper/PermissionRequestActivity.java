package com.omri.dev.promisekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PermissionRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);
    }

    public void closeActivity(View view) {
        finish();
    }
}
