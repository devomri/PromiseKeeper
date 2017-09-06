package com.omri.dev.promisekeeper.Model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.provider.CallLog;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.omri.dev.promisekeeper.AskGeneralPromiseFulfillment;
import com.omri.dev.promisekeeper.Utils.DateUtils;
import com.omri.dev.promisekeeper.Utils.LocationUtils;

import java.util.Date;

/**
 * The promise verifier class is responsible for verifying the fulfillment/unfulfillment
 * of the machine-verifiable promises (for now only location and call promises)
 */

public class PromiseVerifier {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final float MAX_DISTANCE = 500;

    private Context mContext;
    private FusedLocationProviderClient mFusedLocationClient;

    public PromiseVerifier(Context context) {
        mContext = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    public interface PromiseVerificationResult {
        void onVerificationFinished(boolean isPromiseKept);
    }

    @SuppressWarnings({"MissingPermission"}) // Permission was already taken
    public void verifyPromise(final PromiseListItem promise, final PromiseVerificationResult callback) {
        switch (promise.getmPromiseType()) {
            case LOCATION: {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                boolean isPromiseInDistance = true;

                                if (location != null) {
                                    // Check if the promise is within the location
                                    Location promiseLoc = LocationUtils.convertStringToLocation(promise.getmLocation());
                                    if (promiseLoc.distanceTo(location) > MAX_DISTANCE) {
                                        isPromiseInDistance = false;
                                    }
                                }

                                callback.onVerificationFinished(isPromiseInDistance);
                            }
                        });

                break;
            }
            case CALL: {
                boolean isCallInLastDay = isCallInLastDay(promise);
                callback.onVerificationFinished(isCallInLastDay);

                break;
            }
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private boolean isCallInLastDay(PromiseListItem promise) {
        boolean isCallInLastDay = false;

        Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);

        Date yesterday = DateUtils.getYesterday();

        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String contactCallNumberFormatted = promise.getmCallContactNumber().replace(" ", "").replace("-","");

            // If we reached the previous day
            if (callDayTime.before(yesterday)) {
                break;
            }

            if (phNumber.equals(contactCallNumberFormatted)) {
                isCallInLastDay = true;

                break;
            }
        }
        cursor.close();

        return isCallInLastDay;
    }


}
