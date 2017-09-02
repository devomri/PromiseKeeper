package com.omri.dev.promisekeeper.Model;

import com.omri.dev.promisekeeper.Utils.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by omri on 8/1/17.
 * This is the basic promise item which is shown in the main activity
 */

public class PromiseListItem{
    // Data Members
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

        mPromiseStatus = PromiseStatus.ACTIVE;
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

    public PromiseStatus getmPromiseStatus() {
        return mPromiseStatus;
    }

    public void setmPromiseStatus(PromiseStatus mPromiseStatus) {
        this.mPromiseStatus = mPromiseStatus;
    }
}
