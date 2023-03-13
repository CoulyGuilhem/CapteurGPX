package com.example.mickaelpot_matteoclavel_guilhemcouly.browse;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mickaelpot_matteoclavel_guilhemcouly.R;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.GPX;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.Parser;
import com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader.Track;
import com.example.mickaelpot_matteoclavel_guilhemcouly.pdr.MapsActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BrowseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ArrayList<String> names = new ArrayList<>();
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        File[] files = root.listFiles();
        for(int i = 0; i < files.length; i++){
            if(files[i].getName().contains(".gpx")){
                names.add(files[i].getName());
            }
        }


        ListView listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("file", getTracks(i));
            Toast.makeText(this, "Fichier chargé", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }

    public ArrayList<Track> getTracks(int i){
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        File[] files = root.listFiles();
        GPX gpx = new GPX();
        try {
            InputStream is = new FileInputStream(files[i]);

            try {
                gpx = Parser.parse(is);

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (ArrayList<Track>) gpx.getTracks();
    }
}
