package com.omri.dev.promisekeeper.Fragments;

/**
 * Created by omri on 8/25/17.
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.app.TimePickerDialog.OnTimeSetListener;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment{
    private OnTimeSetListener onTimeSetListener;

    public void setOnTimeSetListener(OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this.onTimeSetListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
