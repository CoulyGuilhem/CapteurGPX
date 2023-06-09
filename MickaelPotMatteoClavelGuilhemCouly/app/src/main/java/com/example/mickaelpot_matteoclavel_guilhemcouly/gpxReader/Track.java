package com.example.mickaelpot_matteoclavel_guilhemcouly.gpxReader;

import com.google.android.gms.maps.model.LatLngBounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artz on 24/12/14.
 */
public class Track implements Serializable {

    public List<TrackSeg> trackSegs;
    private LatLngBounds.Builder bounds;

    public Track()
    {
        trackSegs = new ArrayList<TrackSeg>();
    }

    public void addTrackSeg(TrackSeg trackSeg)
    {
        trackSegs.add(trackSeg);
    }

    public void removeTrackSeg(TrackSeg trackSeg)
    {
        trackSegs.remove(trackSeg);
    }

    public List<TrackSeg> getTrackSegs()
    {
        return trackSegs;
    }

}
