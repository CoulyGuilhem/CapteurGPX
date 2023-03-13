package com.example.mickaelpot_matteoclavel_guilhemcouly.pdr;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mickaelpot_matteoclavel_guilhemcouly.R;
import com.example.mickaelpot_matteoclavel_guilhemcouly.browse.BrowseActivity;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpx.GpxActivity;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.Track;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.TrackPoint;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.TrackSeg;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mickaelpot_matteoclavel_guilhemcouly.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions monMarqueur;
    private StepPositioningHandler position;
    private List<LatLng> polylinePos = new ArrayList<>();
    private PolylineOptions pos;
    private List<Track> file;
    public Marker marker;
    public Marker start;
    public Marker myPosition;
    public Polyline polyline;
    public Polyline polylineGpx;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                Intent i = getIntent();
                finish();
                startActivity(i);
            } // Permet de refresh l'activité apres avoir accepté les permissions
        }
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("test","Permission: "+permissions[0]+ "was "+grantResults[0]);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACTIVITY_RECOGNITION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }
        com.example.mickaelpot_matteoclavel_guilhemcouly.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Intent thisIntent = getIntent();
        file = (List<Track>) thisIntent.getSerializableExtra("file");

        marker = null;
        start = null;
        myPosition = null;
        polyline = null;
        polylineGpx = null;

        /**
         * Redirige sur la page de la partie 2.0
         */
        Button b = findViewById(R.id.textView);
        Intent intent = new Intent(this, GpxActivity.class);
        b.setOnClickListener(view -> {
            if (polylineGpx != null)
                polylineGpx.remove();
            this.startActivity(intent);
        });

        /**
         * Redirige sur la page permetant de choisir un fichier gpx sauvegardé
         */
        Button b2 = findViewById(R.id.browseGpx);
        Intent intent2 = new Intent(this, BrowseActivity.class);
        b2.setOnClickListener(view -> {
            if (polylineGpx != null)
                polylineGpx.remove();
            this.startActivity(intent2);
        });

        EditText editText = findViewById(R.id.editText);

        /**
         * Question 4.1
         * Ce bouton créé un nouveau fichier gpx avec un nom choisi
         */
        Button b3 = findViewById(R.id.ButtonAdd);
        b3.setOnClickListener(view -> {
            if(editText.getText() != null && pos != null){
                //Genere le texte dans le fichier gpx avec le dernier trajet tracé
                String a = GPXWriter.writer("test",pos.getPoints());
                //Créé un fichier gpx avec le texte generé precedement
                this.generateNoteOnSD(editText.getText()+".gpx",a);
            } else {
                Toast.makeText(this, "Aucun trajet trouvé / Aucun nom saisi", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * Question 4.3
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager localisation = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if(position == null){
            Location location = localisation.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                position = new StepPositioningHandler(new LatLng(location.getLatitude(),location.getLongitude()));
            }
        }

        StepDetectionHandler stepDection = new StepDetectionHandler(sensorManager);
        DeviceAttitudeHandler deviceOrientation = new DeviceAttitudeHandler(sensorManager);


        /**
         * Question 3.19 Listener
         * Question 3.14/3.15 Il s'agit de la methode newSegment() demandée
         */
        stepDection.setListener(() -> {
            if(position != null){
                position.computeNextStep(0.0008,deviceOrientation.getYaw());
                polylinePos.add(position.getmCurrentLocation());
                pos = new PolylineOptions();
                for(int i=0;i<polylinePos.size();i++){
                    pos.add((LatLng) polylinePos.get(i)).color(Color.GREEN);;
                }
                if(polyline != null){
                    polyline.remove();
                }
                polyline = mMap.addPolyline(pos);
                /**
                 * Question 4.2
                 */
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position.getmCurrentLocation()));
            }
        });

        deviceOrientation.setListener(() -> {

            if(position != null){
                MarkerOptions myPos = new MarkerOptions().position(position.getmCurrentLocation())
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromAsset("fleche.png"))
                        .rotation((float) deviceOrientation.getYaw());
                if(myPosition != null){
                    myPosition.remove();
                }
                myPosition = mMap.addMarker(myPos);
            }
        });

    }

    public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    File gpxfile = new File(root, sFileName);
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.append(sBody);
                    writer.flush();
                    writer.close();
                    Toast.makeText(this, "Trajet sauvegardé", Toast.LENGTH_SHORT).show();
                } else {
                    //request for the permission
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /**
         * Question 3.2 et 3.17
         */
        mMap = googleMap;
        mMap.clear();
        if(file != null){
            LatLng lastPos = new LatLng(0,0);
            for(Track track: file) {
                for (TrackSeg trackSeg : track.getTrackSegs()) {
                    PolylineOptions polylineOptions = new PolylineOptions().clickable(false);
                    for (TrackPoint trackPoint : trackSeg.getTrackPoints()) {
                        polylineOptions.add(new LatLng(trackPoint.getLatitude(), trackPoint.getLongitude()));
                        lastPos = new LatLng(trackPoint.getLatitude(), trackPoint.getLongitude());
                    }
                    polylineGpx = mMap.addPolyline(polylineOptions);
                }
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPos,16));
        }
        /**
         * Question 3.18
         */
        mMap.setOnMapClickListener(location -> {
            if(pos != null){
                pos.color(Color.BLACK);
                polyline = mMap.addPolyline(pos);
            }
            polylinePos = new ArrayList<LatLng>();
            if(start != null){
                /**
                 * Question 3.16 Fin marker
                 */
                monMarqueur=new MarkerOptions().position(position.getmCurrentLocation()).title("Fin");
                marker = mMap.addMarker(monMarqueur);
            }
            position = new StepPositioningHandler(new LatLng(location.latitude, location.longitude));
            polylinePos.add(position.getmCurrentLocation());
            /**
             * Question 3.16 Debut market
             */
            MarkerOptions startOption = new MarkerOptions().position(position.getmCurrentLocation()).title("Debut");
            start = mMap.addMarker(startOption);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(position.getmCurrentLocation()));
        });
    }
}