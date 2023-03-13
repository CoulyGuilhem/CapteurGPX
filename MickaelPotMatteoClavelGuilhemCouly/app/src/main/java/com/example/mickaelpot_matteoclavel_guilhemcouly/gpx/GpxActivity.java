package com.example.mickaelpot_matteoclavel_guilhemcouly.gpx;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.mickaelpot_matteoclavel_guilhemcouly.R;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.GPX;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.Parser;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.Track;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.TrackPoint;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.TrackSeg;
import com.example.mickaelpot_matteoclavel_guilhemcouly.pdr.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class GpxActivity extends FragmentActivity implements OnMapReadyCallback {

    PolylineOptions polylineOptions2 = new PolylineOptions();

    /**
     * Code de la partie 2
     *
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpx);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /**
         * Question 2.4
         */
        polylineOptions2.add(new LatLng(45.189,5.704));
        polylineOptions2.add(new LatLng(45.188, 5.725));
        polylineOptions2.add(new LatLng(45.191, 5.733));
        Button b = findViewById(R.id.textView);
        Intent intent = new Intent(this, MapsActivity.class);
        b.setOnClickListener(view -> {
            this.startActivity(intent);
        });

    }
    /**
     * Question 2.2
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        /**
         * Question 2.3
         */
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.1796267,5.8207681),11.0f));
        googleMap.addPolyline(polylineOptions2);
        /**
         * Question 2.5
         *
         */
        try {
            /**
             * Question 2.1 -
             */
            InputStream is = getAssets().open("GPX/bikeandrun.gpx");
            try {
                GPX gpx = Parser.parse(is);
                for(Track track: gpx.getTracks()){
                    for(TrackSeg trackSeg: track.getTrackSegs()){
                        PolylineOptions polylineOptions = new PolylineOptions().clickable(false);
                        for(TrackPoint trackPoint: trackSeg.getTrackPoints()){
                            polylineOptions.add(new LatLng(trackPoint.getLatitude(), trackPoint.getLongitude()));
                        }
                        googleMap.addPolyline(polylineOptions);
                    }
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
