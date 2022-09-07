package com.karru.util.path_plot;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karru.landing.home.model.Location;

import java.io.Serializable;

/**
 * <h1>LatLongBounds</h1>
 * used to hold the latLng bounds
 * @author 3Embed
 * @since on 02-04-2018.
 */
public class LatLongBounds implements Serializable
{
    /*"northeast":{},
    "southwest":{}*/
    @SerializedName("northeast")
    @Expose
    private Location northeast;
    @SerializedName("southwest")
    @Expose
    private Location southwest;
    @SerializedName("polylineOptions")
    @Expose
    private PolylineOptions polylineOptions;
    @SerializedName("duration")
    @Expose
    private String duration;

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public PolylineOptions getPolylineOptions() {
        return polylineOptions;
    }
    public void setPolylineOptions(PolylineOptions polylineOptions) { this.polylineOptions = polylineOptions; }
    public Location getNortheast() { return northeast; }
    public Location getSouthwest() {
        return southwest;
    }
}
