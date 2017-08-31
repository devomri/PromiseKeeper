package com.omri.dev.promisekeeper.Model;

/**
 * Created by omri on 8/1/17.
 * This is the basic promise item which is shown in the main activity
 */

public class PromiseListItem{
    // Data Members
    private PromiseTypes mPromiseType;
    private String mTitle;
    private String mDescription;
    private String mBaseTime;
    private String mExecuteTime; // Relevant for past promises
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

        mExecuteTime = "";
    }

    // Properties
    public PromiseTypes getmPromiseType() {
        return mPromiseType;
    }

    public void setmPromiseType(PromiseTypes mPromiseType) {
        this.mPromiseType = mPromiseType;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmBaseTime() {
        return mBaseTime;
    }

    public void setmBaseTime(String mBaseTime) {
        this.mBaseTime = mBaseTime;
    }

    public String getmGuardContactNumber() {
        return mGuardContactNumber;
    }

    public void setmGuardContactNumber(String mGuardContactNumber) {
        this.mGuardContactNumber = mGuardContactNumber;
    }

    public PromiseIntervals getmPromiseInterval() {
        return mPromiseInterval;
    }

    public void setmPromiseInterval(PromiseIntervals mPromiseInterval) {
        this.mPromiseInterval = mPromiseInterval;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmCallContactNumber() {
        return mCallContactNumber;
    }

    public void setmCallContactNumber(String mCallContactNumber) {
        this.mCallContactNumber = mCallContactNumber;
    }

    // Methods
    public String getPromiseNextTime() {
        // TODO: implement
        return "stub";
    }
}
