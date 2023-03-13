package com.example.mickaelpot_matteoclavel_guilhemcouly.pdr;

import android.icu.util.MeasureUnit;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class StepPositioningHandler {
    private LatLng mCurrentLocation;

    /**
     * Question 3.12
     */
    public StepPositioningHandler(LatLng location){
        mCurrentLocation = location;
    }

    public void setmCurrentLocation(LatLng newLocation){
        mCurrentLocation = newLocation;
    }

    public LatLng getmCurrentLocation(){
        return mCurrentLocation;
    }

    /**
     * Question 3.13
     */
    public void computeNextStep(double dist, double brng){
        dist = dist / 6371;
        brng = Math.toRadians(brng);

        double lat1 = Math.toRadians(mCurrentLocation.latitude), lon1 = Math.toRadians(mCurrentLocation.longitude);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist) +
                Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(dist) *
                        Math.cos(lat1),
                Math.cos(dist) - Math.sin(lat1) *
                        Math.sin(lat2));

        mCurrentLocation= new LatLng(Math.toDegrees(lat2),Math.toDegrees(lon2));
    }
}
