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
import android.telephony.SmsManager;
import android.text.TextUtils;
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
import com.omri.dev.promisekeeper.Model.PromiseIntervals;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseTypes;
import com.omri.dev.promisekeeper.Utils.DateUtils;

import static android.media.CamcorderProfile.get;

public class CreatePromiseActivity extends AppCompatActivity {
    private static final int PROMISE_LOCATION_REQUEST_CODE = 1;
    private static final int PICK_CONTACT_CALL_REQUEST = 2;
    private static final int PICK_CONTACT_GUARD_REQUEST = 3;

    private EditText mPromiseTitle;
    private EditText mPromiseDescription;
    private TextView mPromiseBaseTime;
    private Spinner mPromiseRepeatSpinner;
    private TextView mPromiseContactGuardText;
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
        mPromiseContactGuardText = (TextView)findViewById(R.id.create_promise_contact_guard_text);
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
                        String time = dayOfMonth + "/" + (month + 1) + "/" + year + " " + hourOfDay + ":" + minute;
                        mPromiseBaseTime.setText(DateUtils.reformatDateString(time));
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
        startContactActivityForResult(PICK_CONTACT_CALL_REQUEST);
    }

    public void chooseContactGuard(View view) {
        startContactActivityForResult(PICK_CONTACT_GUARD_REQUEST);
    }

    private void startContactActivityForResult(int pickContactCallRequest) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, pickContactCallRequest);
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
                case PICK_CONTACT_CALL_REQUEST: {
                    String contactToCallNumber = getPhoneNumberFromData(data);
                    mPromiseContactCallText.setText(contactToCallNumber);

                    break;
                }
                case PICK_CONTACT_GUARD_REQUEST: {
                    String contactToCallNumber = getPhoneNumberFromData(data);
                    mPromiseContactGuardText.setText(contactToCallNumber);

                    break;
                }
            }
        }
    }

    private String getPhoneNumberFromData(Intent data) {
        Uri contactUri = data.getData();
        String[] projection = {Phone.NUMBER};

        Cursor cursor = getContentResolver()
                .query(contactUri, projection, null, null, null);
        cursor.moveToFirst();

        // Retrieve the phone number from the NUMBER column
        int column = cursor.getColumnIndex(Phone.NUMBER);
        String number = cursor.getString(column);

        return number;
    }

    public void createNewPromise(View view) {
        // Get promise filled data
        String promiseTitle = mPromiseTitle.getText().toString();
        String promiseDescription = mPromiseDescription.getText().toString();
        String promiseBaseTime = mPromiseBaseTime.getText().toString();
        String promiseGuardContact = mPromiseContactGuardText.getText().toString();
        String promiseLocation = mPromiseLocationText.getText().toString();
        String promiseCallContact = mPromiseContactCallText.getText().toString();

        if (promiseGuardContact.equals(getString(R.string.create_promise_contact_guard_number_hint2))) {
            promiseGuardContact = "";
        }

        int selectedPromiseTypeId = mPromiseTypeGroup.getCheckedRadioButtonId();
        PromiseTypes promiseType = PromiseTypes.GENERAL;
        if (selectedPromiseTypeId == R.id.create_promise_rb_location) {
            promiseType = PromiseTypes.LOCATION;
        } else if (selectedPromiseTypeId == R.id.create_promise_rb_call) {
            promiseType = PromiseTypes.CALL;
        }

        String promiseIntervalString = mPromiseRepeatSpinner.getSelectedItem().toString();
        PromiseIntervals promiseInterval = PromiseIntervals.NO_REPEAT;
        String[] intervalsStringArray = getResources().getStringArray(R.array.create_promise_repeat_array);
        if (promiseIntervalString.equals(intervalsStringArray[1])) {
            promiseInterval = PromiseIntervals.DAILY;
        } else if (promiseIntervalString.equals(intervalsStringArray[2])) {
            promiseInterval = PromiseIntervals.WEEKLY;
        } else if (promiseIntervalString.equals(intervalsStringArray[3])) {
            promiseInterval = PromiseIntervals.MONTHLY;
        } else if (promiseIntervalString.equals(intervalsStringArray[4])) {
            promiseInterval = PromiseIntervals.YEARLY;
        }

        // Reset error
        mPromiseTitle.setError(null);
        mPromiseDescription.setError(null);
        mPromiseBaseTime.setError(null);
        mPromiseLocationText.setError(null);
        mPromiseContactCallText.setError(null);

        boolean isValid = true;
        View focusView = null;

        // Promise title validity
        if (TextUtils.isEmpty(promiseTitle)) {
            mPromiseTitle.setError(getString(R.string.create_promise_title_error));
            isValid = false;
            focusView = mPromiseTitle;
        }

        // Promise description validity
        if (TextUtils.isEmpty(promiseDescription)) {
            mPromiseDescription.setError(getString(R.string.create_promise_description_error));
            isValid = false;
            focusView = mPromiseDescription;
        }

        // Promise base time validity
        if (promiseBaseTime.equals(getString(R.string.create_promise_base_time_default))) {
            mPromiseBaseTime.setError(getString(R.string.create_promise_base_time_error));
            isValid = false;
            focusView = mPromiseBaseTime;
        }

        // Check location validity
        if (promiseType == PromiseTypes.LOCATION &&
                promiseLocation.equals(getString(R.string.create_promise_choose_location))) {
            mPromiseLocationText.setError(getString(R.string.create_promise_choose_location));
            isValid = false;
            focusView = mPromiseLocationText;
        }

        // Check call contact validity
        if (promiseType == PromiseTypes.CALL &&
                promiseCallContact.equals(getString(R.string.create_promise_choose_contact_to_call))) {
            mPromiseContactCallText.setError(getString(R.string.create_promise_choose_contact_to_call));
            isValid = false;
            focusView = mPromiseContactCallText;
        }

        if (!isValid) {
            focusView.requestFocus();
        } else {
            // Create a new promise
            PromiseListItem newPromiseItem =
                    new PromiseListItem(promiseType,
                                        promiseTitle,
                                        promiseDescription,
                                        promiseBaseTime,
                                        promiseGuardContact,
                                        promiseInterval,
                                        promiseLocation,
                                        promiseCallContact);

            // If there is a guard - notify him
            newPromiseItem.sendMessageToGuard("Hi, you were just nominated as a guard for my new promise.");

            Intent resultIntent = newPromiseItem.toIntent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}

