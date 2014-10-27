package edu.usf.EventExpress;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Varik on 10/26/2014.
 */
public class Event_Map extends Activity {

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Location myLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_map);

        InitilizeMap();

    }

    private void InitilizeMap() {
        LatLng mylatlng;
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        if (locationManager == null){
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        if (myLocation == null){
            myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider( new Criteria(), true));
        }
        googleMap.setMyLocationEnabled(true);
        mylatlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mylatlng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));


    }
}