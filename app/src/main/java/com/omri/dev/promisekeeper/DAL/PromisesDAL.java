package com.omri.dev.promisekeeper.DAL;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.omri.dev.promisekeeper.Model.PromiseEnumConvrsions;
import com.omri.dev.promisekeeper.Model.PromiseIntervals;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseStatus;
import com.omri.dev.promisekeeper.Model.PromiseTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omri on 9/3/17.
 */

public class PromisesDAL{
    private PromiseDBHelper mHelper;

    public PromisesDAL(Context context) {
        mHelper = new PromiseDBHelper(context);
    }

    public ArrayList<PromiseListItem> getFuturePromises() {
        return getPromisesByStatus(PromiseStatus.ACTIVE);
    }

    public ArrayList<PromiseListItem> getFulfilledPromises() {
        return getPromisesByStatus(PromiseStatus.FULFILLED);
    }

    public ArrayList<PromiseListItem> getUnfulfilledPromises() {
        return getPromisesByStatus(PromiseStatus.UNFULFILLED);
    }

    private ArrayList<PromiseListItem> getPromisesByStatus (PromiseStatus status) {
        ArrayList<PromiseListItem> promises = new ArrayList<>();

        String sql = "";
        switch (status) {
            case ACTIVE: {
                sql = PromiseTable.SQL_GET_ALL_FUTURE_PROMISES;
                break;
            }
            case FULFILLED: {
                sql = PromiseTable.SQL_GET_ALL_FULFILLED_PROMISES;
                break;
            }
            case UNFULFILLED: {
                sql = PromiseTable.SQL_GET_ALL_UNFULFILLED_PROMISES;
                break;
            }
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String ID = cursor.getString(cursor.getColumnIndexOrThrow(PromiseTable.COLUMN_NAME_ID));
            String typeString = cursor.getString(cursor.getColumnIndexOrThrow(PromiseTable.COLUMN_NAME_TYPE));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(PromiseTable.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(PromiseTable.COLUMN_NAME_DESCRIPTION));
            String baseTime = cursor.getString(cursor.getColumnIndexOrThrow(PromiseTable.COLUMN_NAME_BASE_TIME));
            String guardContact = cursor.getString(cursor.getColumnIndexOrThrow(PromiseTable.COLUMN_NAME_GUARD_CONTACT));
            String intervalString = cursor.getString(cursor.getColumnIndexOrThrow(PromiseTable.COLUMN_NAME_INTERVAL));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(PromiseTable.COLUMN_NAME_LOCATION));
            String callContact = cursor.getString(cursor.getColumnIndexOrThrow(PromiseTable.COLUMN_NAME_CALL_CONTACT));

            int intervalInt = Integer.parseInt(intervalString);
            PromiseIntervals interval = PromiseEnumConvrsions.convertIntToPromiseInterval(intervalInt);

            int typeInt = Integer.parseInt(typeString);
            PromiseTypes type = PromiseEnumConvrsions.convertIntToPromiseType(typeInt);

            promises.add(new PromiseListItem(type,
                                            title,
                                            description,
                                            baseTime,
                                            guardContact,
                                            interval,
                                            location,
                                            callContact,
                                            ID,
                                            status));
        }
        cursor.close();

        return promises;
    }

    public void markPromiseAsUnfulfilled(String promiseID) {
        String sql =
                String.format(PromiseTable.SQL_UPDATE_PROMIE_STATUS,
                        Integer.toString(PromiseStatus.UNFULFILLED.ordinal()),
                        promiseID);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    public void markPromisefulfilled(String promiseID) {
        String sql =
                String.format(PromiseTable.SQL_UPDATE_PROMIE_STATUS,
                        Integer.toString(PromiseStatus.FULFILLED.ordinal()),
                        promiseID);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    public void createNextPromiseIfNecessary(PromiseListItem originalPromise) {
        // If there is a need for further promise
        if (originalPromise.getmPromiseInterval() != PromiseIntervals.NO_REPEAT) {
            PromiseListItem newPromise =
                    new PromiseListItem(originalPromise.getmPromiseType(),
                            originalPromise.getmTitle(),
                            originalPromise.getmDescription(),
                            originalPromise.getPromiseNextTime(), // Next time of previous promise is base time of the new one
                            originalPromise.getmGuardContactNumber(),
                            originalPromise.getmPromiseInterval(),
                            originalPromise.getmLocation(),
                            originalPromise.getmCallContactNumber());

            this.createNewPromise(newPromise);
        }
    }

    public void createNewPromise(PromiseListItem newPromise) {
        String sql =
                String.format(PromiseTable.SQL_CREATE_PROMISE,
                        newPromise.getmPromiseID(),
                        Integer.toString(newPromise.getmPromiseType().ordinal()),
                        Integer.toString(newPromise.getmPromiseStatus().ordinal()),
                        newPromise.getmTitle(),
                        newPromise.getmDescription(),
                        newPromise.getmBaseTime(),
                        newPromise.getmGuardContactNumber(),
                        Integer.toString(newPromise.getmPromiseInterval().ordinal()),
                        newPromise.getmLocation(),
                        newPromise.getmCallContactNumber());

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sql);
    }
}
