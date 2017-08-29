package com.omri.dev.promisekeeper;



import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.omri.dev.promisekeeper.Fragments.DatePickerFragment;
import com.omri.dev.promisekeeper.Fragments.TimePickerFragment;

public class CreatePromiseActivity extends AppCompatActivity {
    private EditText mPromiseTitle;
    private EditText mPromiseDescription;
    private TextView mPromiseBaseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_promise);

        this.mPromiseTitle = (EditText)findViewById(R.id.create_promise_title);
        this.mPromiseDescription = (EditText)findViewById(R.id.create_promise_description);
        this.mPromiseBaseTime = (TextView) findViewById(R.id.create_promise_base_time);
    }

    public void chooseDateAndTime(View view) {
        DatePickerFragment dateFragment = new DatePickerFragment();
        dateFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                // Show the time dialog
                TimePickerFragment timeFragment = new TimePickerFragment();
                timeFragment.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = dayOfMonth + "/" + month + "/" + year + " " + hourOfDay + ":" + minute;
                        mPromiseBaseTime.setText(time);
                    }
                });
                timeFragment.show(getFragmentManager(), "timePicker");
            }
        });
        dateFragment.show(getFragmentManager(), "datePicker");
    }
}

