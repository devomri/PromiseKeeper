package com.omri.dev.promisekeeper.Model;

import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.omri.dev.promisekeeper.Utils.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * This is the basic promise item which is shown in the main activity
 */

public class PromiseListItem{
    // Data Members
    private String mPromiseID;
    private PromiseTypes mPromiseType;
    private PromiseStatus mPromiseStatus;
    private String mTitle;
    private String mDescription;
    private String mBaseTime;
    private String mGuardContactNumber;
    private PromiseIntervals mPromiseInterval;
    private String mLocation;
    private String mCallContactNumber;

    // Constructor
    public PromiseListItem(PromiseTypes type,
                           String title,
                           String description,
                           String baseTime,
                           String guardContactNumber,
                           PromiseIntervals promiseInterval,
                           String location,
                           String callContactNumber) {
        mPromiseType = type;
        mTitle = title;
        mDescription = description;
        mBaseTime = baseTime;
        mGuardContactNumber = guardContactNumber;
        mPromiseInterval = promiseInterval;
        mLocation = location;
        mCallContactNumber = callContactNumber;

        mPromiseID = java.util.UUID.randomUUID().toString();
        mPromiseStatus = PromiseStatus.ACTIVE;
    }

    public PromiseListItem(PromiseTypes type,
                           String title,
                           String description,
                           String baseTime,
                           String guardContactNumber,
                           PromiseIntervals promiseInterval,
                           String location,
                           String callContactNumber,
                           String id,
                           PromiseStatus status) {
        this(type, title, description, baseTime, guardContactNumber, promiseInterval, location, callContactNumber);

        mPromiseID = id;
        mPromiseStatus = status;
    }

    public PromiseListItem(Intent i) {
        mPromiseID = i.getStringExtra("id");
        mTitle = i.getStringExtra("title");
        mDescription = i.getStringExtra("description");
        mBaseTime = i.getStringExtra("baseTime");

        int promiseTypeInt = i.getIntExtra("type", 0);
        mPromiseType = PromiseEnumConvrsions.convertIntToPromiseType(promiseTypeInt);

        int promiseStatusInt = i.getIntExtra("status", 0);
        mPromiseStatus = PromiseEnumConvrsions.convertIntToPromiseStatus(promiseStatusInt);

        int promiseIntervalInt = i.getIntExtra("interval", 0);
        mPromiseInterval = PromiseEnumConvrsions.convertIntToPromiseInterval(promiseIntervalInt);

        mGuardContactNumber = i.getStringExtra("guardContact");
        mLocation = i.getStringExtra("location");
        mCallContactNumber = i.getStringExtra("callContact");
    }

    // Properties
    public PromiseTypes getmPromiseType() {
        return mPromiseType;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }


    public String getmBaseTime() {
        return mBaseTime;
    }

    public String getmGuardContactNumber() {
        return mGuardContactNumber;
    }

    public PromiseIntervals getmPromiseInterval() {
        return mPromiseInterval;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmCallContactNumber() {
        return mCallContactNumber;
    }

    public PromiseStatus getmPromiseStatus() {
        return mPromiseStatus;
    }

    public String getmPromiseID() {
        return mPromiseID;
    }

    public void setmPromiseStatus(PromiseStatus mPromiseStatus) {
        this.mPromiseStatus = mPromiseStatus;
    }

    // Methods
    public String getPromiseNextTime(){
        Date baseTime = DateUtils.convertStringToDate(mBaseTime);

        int daysToAdd;
        switch (mPromiseInterval) {
            case NO_REPEAT: {
                daysToAdd = 0;
                break;
            }
            case DAILY: {
                daysToAdd = 1;
                break;
            }
            case WEEKLY: {
                daysToAdd = 7;
                break;
            }
            case MONTHLY: {
                daysToAdd = 30;
                break;
            }
            case YEARLY: {
                daysToAdd = 365;
                break;
            }
            default: {
                daysToAdd = 0;
            }
        }

        Date nextTime = DateUtils.addDays(baseTime ,daysToAdd);

        return DateUtils.convertDateToString(nextTime);
    }

    public Intent toIntent(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        this.populateIntent(intent);

        return intent;
    }

    public Intent toIntent() {
        Intent intent = new Intent();
        this.populateIntent(intent);

        return intent;
    }

    private void populateIntent(Intent intent) {
        intent.putExtra("id", this.getmPromiseID());
        intent.putExtra("title", this.getmTitle());
        intent.putExtra("baseTime", this.getmBaseTime());
        intent.putExtra("description", this.getmDescription());
        intent.putExtra("type", this.getmPromiseType().ordinal());
        intent.putExtra("status", this.getmPromiseStatus().ordinal());
        intent.putExtra("interval", this.getmPromiseInterval().ordinal());
        intent.putExtra("guardContact", this.getmGuardContactNumber());
        intent.putExtra("location", this.getmLocation());
        intent.putExtra("callContact", this.getmCallContactNumber());
    }

    public String toDetaildedText() {
        String promiseString =
                String.format("Promise details: %s, %s, At %s, Interval: %s",
                        mTitle,
                        mDescription,
                        mBaseTime,
                        PromiseEnumConvrsions.convertIntToPromiseIntervalText(mPromiseInterval));

        return promiseString;
    }

    public void sendMessageToGuard(String message) {
        if (mGuardContactNumber.length() > 0) {
            String validPhoneNumber = mGuardContactNumber.replace(" ", "").replace("-","");

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(validPhoneNumber,
                    null,
                    message + " " + toDetaildedText(),
                    null,
                    null);
        }
    }

    public void sendUnfulfilledPromiseToGuard() {
        sendMessageToGuard("Promise was not kept!");
    }
}
