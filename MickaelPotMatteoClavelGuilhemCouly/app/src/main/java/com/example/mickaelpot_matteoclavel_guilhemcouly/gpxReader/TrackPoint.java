package com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader;

import java.io.Serializable;

/**
 * Created by artz on 24/12/14.
 */
public class TrackPoint implements Serializable {

    private double latitude;
    private double longitude;

    public TrackPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
