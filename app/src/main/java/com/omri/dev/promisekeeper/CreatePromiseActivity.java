package com.omri.dev.promisekeeper;



import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.omri.dev.promisekeeper.Fragments.DatePickerFragment;
import com.omri.dev.promisekeeper.Fragments.TimePickerFragment;

public class CreatePromiseActivity extends AppCompatActivity {
    private EditText mPromiseTitle;
    private EditText mPromiseDescription;
    private TextView mPromiseBaseTime;
    private Spinner mPromiseRepateSpinner;
    private RadioGroup mPromiseTypeGroup;
    private TextView mPromiseLocatoionText;
    private TextView mPromiseContactCallText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_promise);

        mPromiseTitle = (EditText)findViewById(R.id.create_promise_title);
        mPromiseDescription = (EditText)findViewById(R.id.create_promise_description);
        mPromiseBaseTime = (TextView) findViewById(R.id.create_promise_base_time);
        mPromiseRepateSpinner = (Spinner)findViewById(R.id.create_promise_repeat_spinner);
        mPromiseTypeGroup = (RadioGroup)findViewById(R.id.create_promise_rbgroup_type);
        mPromiseLocatoionText = (TextView)findViewById(R.id.create_promise_location_text);
        mPromiseContactCallText = (TextView)findViewById(R.id.create_promise_contact_call_text);

        // Populate spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.create_promise_repeat_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPromiseRepateSpinner.setAdapter(adapter);

        mPromiseTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.create_promise_rb_general:
                        setTextVisibility(View.GONE, View.GONE);

                        break;
                    case R.id.create_promise_rb_location:
                        setTextVisibility(View.VISIBLE, View.GONE);

                        break;
                    case R.id.create_promise_rb_call:
                        setTextVisibility(View.GONE, View.VISIBLE);

                        break;
                }
            }
        });
    }

    private void setTextVisibility(int locationVisibility, int contactVisibility) {
        mPromiseLocatoionText.setVisibility(locationVisibility);
        mPromiseContactCallText.setVisibility(contactVisibility);
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

    public void chooseLocation(View view) {
        Intent i = new Intent(getApplicationContext(), GetLocationActivity.class);
        startActivity(i);
    }

    public void chooseContactCall(View view) {
    }
}

