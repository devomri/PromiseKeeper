package com.omri.dev.promisekeeper;



import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.omri.dev.promisekeeper.Fragments.DatePickerFragment;
import com.omri.dev.promisekeeper.Fragments.TimePickerFragment;

public class CreatePromiseActivity extends AppCompatActivity {
    private static final int PROMISE_LOCATION_REQUEST_CODE = 1;
    private static final int PICK_CONTACT_REQUEST = 2;

    private EditText mPromiseTitle;
    private EditText mPromiseDescription;
    private TextView mPromiseBaseTime;
    private Spinner mPromiseRepeatSpinner;
    private RadioGroup mPromiseTypeGroup;
    private TextView mPromiseLocationText;
    private TextView mPromiseContactCallText;
    private LinearLayout mPromiseLocationLayout;
    private LinearLayout mPromiseContactCallLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_promise);

        mPromiseTitle = (EditText)findViewById(R.id.create_promise_title);
        mPromiseDescription = (EditText)findViewById(R.id.create_promise_description);
        mPromiseBaseTime = (TextView) findViewById(R.id.create_promise_base_time);
        mPromiseRepeatSpinner = (Spinner)findViewById(R.id.create_promise_repeat_spinner);
        mPromiseTypeGroup = (RadioGroup)findViewById(R.id.create_promise_rbgroup_type);
        mPromiseLocationText = (TextView)findViewById(R.id.create_promise_location_text);
        mPromiseContactCallText = (TextView)findViewById(R.id.create_promise_contact_call_text);
        mPromiseLocationLayout = (LinearLayout)findViewById(R.id.create_promise_location_layout);
        mPromiseContactCallLayout = (LinearLayout)findViewById(R.id.create_promise_contact_call_layout);

        // Populate spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.create_promise_repeat_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPromiseRepeatSpinner.setAdapter(adapter);

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
        mPromiseLocationLayout.setVisibility(locationVisibility);
        mPromiseContactCallLayout.setVisibility(contactVisibility);
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
        Intent getPromiseLocationIntent = new Intent(getApplicationContext(), GetLocationActivity.class);
        startActivityForResult(getPromiseLocationIntent, PROMISE_LOCATION_REQUEST_CODE);
    }

    public void chooseContactCall(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PROMISE_LOCATION_REQUEST_CODE: {
                    String returnedLocation = data.getData().toString();
                    mPromiseLocationText.setText(returnedLocation);

                    break;
                }
                case PICK_CONTACT_REQUEST: {
                    Uri contactUri = data.getData();
                    String[] projection = {Phone.NUMBER};

                    Cursor cursor = getContentResolver()
                            .query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();

                    // Retrieve the phone number from the NUMBER column
                    int column = cursor.getColumnIndex(Phone.NUMBER);
                    String number = cursor.getString(column);

                    mPromiseContactCallText.setText(number);
                }
            }
        }
    }
}

