package com.omri.dev.promisekeeper.Model;

import java.io.Serializable;

/**
 * Created by omri on 8/1/17.
 * This is the basic promise item which is shown in the main activity
 */

public class PromiseListItem{
    // Data Members
    private String mTitle;
    private String mDescription;
    private String mCreateDate;
    private String mPromiseNextTime;

    // Constructor
    public PromiseListItem(String title,
                           String description,
                           String createDate,
                           String promiseNextTime) {
        this.mTitle = title;
        this.mDescription = description;
        this.mCreateDate = createDate;
        this.mPromiseNextTime = promiseNextTime;

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

    public String getmCreateDate() {
        return mCreateDate;
    }

    public void setmCreateDate(String mCreateDate) {
        this.mCreateDate = mCreateDate;
    }

    public String getmPromiseNextTime() {
        return mPromiseNextTime;
    }

    public void setmPromiseNextTime(String mPromiseNextTime) {
        this.mPromiseNextTime = mPromiseNextTime;
    }
}
