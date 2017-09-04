package com.omri.dev.promisekeeper.Model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.omri.dev.promisekeeper.Utils.LocationUtils;

/**
 * Created by omri on 9/4/17.
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

    @SuppressWarnings({"MissingPermission"}) // Permission was already taken
    public boolean verifyPromise(final PromiseListItem promise) {
        final boolean[] isPromiseKept = {true};

        switch (promise.getmPromiseType()) {
            case GENERAL: {
                // Ask the user whether the promise is kept

                break;
            }
            case LOCATION: {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    // Check if the promise is within the location
                                    Location promiseLoc = LocationUtils.convertStringToLocation(promise.getmLocation());
                                    if (promiseLoc.distanceTo(location) > MAX_DISTANCE) {
                                        isPromiseKept[0] = false;
                                    }
                                }
                            }
                        });

                break;
            }
            case CALL: {
                break;
            }
        }

        return isPromiseKept[0];
    }


}
