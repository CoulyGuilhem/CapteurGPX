package com.example.mickaelpot_matteoclavel_guilhemcouly.pdr;

import android.os.Environment;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GPXWriter {

        public static String writer(String name, List<LatLng> coords){

            String addTrack = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<gpx version=\"1.1\" creator=\"Endomondo.com\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd\" xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:gpxtpx=\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1\" xmlns:gpxx=\"http://www.garmin.com/xmlschemas/GpxExtensions/v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                    "  <metadata>\n" +
                    "    <author>\n" +
                    "      <name>Thibaud Michel</name>\n" +
                    "      <email id=\"thibaud.michel\" domain=\"gmail.com\"/>\n" +
                    "    </author>\n" +
                    "  </metadata>\n " +
                    "  <trk>\n"+
                    "   <type>"+name+"</type>\n" +
                    "   <trkseg>\n";
            for(int i = 0; i<coords.size();i++){
                addTrack = addTrack +
                        "   <trkpt lat=\""+coords.get(i).latitude+"\" lon=\""+coords.get(i).longitude+"\">\n"+
                        "       <time>2013-07-27T04:10:37Z</time>\n"+
                        "   </trkpt>\n";
            }
            addTrack = addTrack + "</trkseg>\n" +
                    "  </trk>\n" +
                    "</gpx>";
            return addTrack;
        }
}