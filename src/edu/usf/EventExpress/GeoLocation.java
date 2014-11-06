package edu.usf.EventExpress;

import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;
import android.content.Context;

import java.util.List;

/**
 * Created by Varik on 11/2/2014.
 */
public class GeoLocation {

    Geocoder coder;
    List<Address> address;


    public GeoLocation(Context context){

        coder = new Geocoder(context);
    }

    public LatLng getLatLngfromAddress(String strAddr) {
        try {
            address = coder.getFromLocationName(strAddr, 5);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (address == null){
            return null;
        }
        Address location = address.get(0);
        GeoPoint P = new GeoPoint((int) (location.getLatitude() * 1E6),(int) (location.getLongitude() * 1E6));
        return new LatLng(P.getLatitudeE6()/1E6, P.getLongitudeE6()/1E6);
    }
}
