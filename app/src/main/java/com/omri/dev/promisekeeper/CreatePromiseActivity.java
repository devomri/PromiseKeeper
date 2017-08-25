package com.omri.dev.promisekeeper;



import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.omri.dev.promisekeeper.Fragments.DatePickerFragment;
import com.omri.dev.promisekeeper.Fragments.TimePickerFragment;

public class CreatePromiseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_promise);
    }

    public void chooseDateAndTime(View view) {
        DatePickerFragment dateFragment = new DatePickerFragment();
        dateFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Get the date from the user

                // Show the time dialog
                TimePickerFragment timeFragment = new TimePickerFragment();
                timeFragment.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                    }
                });
                timeFragment.show(getFragmentManager(), "timePicker");
            }
        });
        dateFragment.show(getFragmentManager(), "datePicker");
    }
}

