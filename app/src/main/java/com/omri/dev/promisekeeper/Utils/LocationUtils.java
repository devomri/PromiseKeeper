package com.omri.dev.promisekeeper.Utils;

import android.location.Location;
import android.location.LocationManager;

/**
 * Created by omri on 9/4/17.
 */

public class LocationUtils {
    public static Location convertStringToLocation(String locationString) {
        Location location = new Location(LocationManager.GPS_PROVIDER);

        String[] latlon = locationString.split(",");
        location.setLatitude(Double.parseDouble(latlon[0]));
        location.setLongitude(Double.parseDouble(latlon[1]));

        return location;
    }
}
