package com.omri.dev.promisekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.omri.dev.promisekeeper.DAL.PromisesDAL;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseStatus;

public class AskGeneralPromiseFulfillment extends AppCompatActivity {
    private PromisesDAL mPromisesDAL;
    private PromiseListItem mPromise;

    private TextView mPromiseTitleTextView;
    private TextView mPromiseDescrtiptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_general_promise_fulfillment);

        mPromiseTitleTextView = (TextView) findViewById(R.id.ask_general_promise_title);
        mPromiseDescrtiptionTextView = (TextView) findViewById(R.id.ask_general_promise_description);

        mPromise = new PromiseListItem(getIntent());
        mPromisesDAL = new PromisesDAL(getApplicationContext());

        mPromiseTitleTextView.setText(mPromise.getmTitle());
        mPromiseDescrtiptionTextView.setText(mPromise.getmDescription());
    }

    public void promiseKept(View view) {
        changePromiseStatus(PromiseStatus.FULFILLED);
    }

    public void promiseIsNotKept(View view) {
        changePromiseStatus(PromiseStatus.UNFULFILLED);
    }

    private void changePromiseStatus(PromiseStatus status) {
        if (status == PromiseStatus.FULFILLED) {
            mPromisesDAL.markPromisefulfilled(mPromise.getmPromiseID());
        } else if (status == PromiseStatus.UNFULFILLED) {
            mPromisesDAL.markPromiseAsUnfulfilled(mPromise.getmPromiseID());

            mPromise.sendUnfulfilledPromiseToGuard();
        }

        mPromisesDAL.createNextPromiseIfNecessary(mPromise);

        finish();
    }
}
