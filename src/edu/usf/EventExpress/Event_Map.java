package edu.usf.EventExpress;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;

import java.net.URI;

/**
 * Created by Varik on 10/26/2014.
 */
public class Event_Map extends Activity {

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Location myLocation;
    private Bundle myBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_map);
        myBundle = getIntent().getExtras();
        InitilizeMap();
        if(myBundle != null && myBundle.getBoolean("fromEventDetail",false)){
            ShowGeoLocation();
        }


    }

    private void InitilizeMap() {
        LatLng mylatlng;
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        if (locationManager == null){
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        // NEED TO SET FOR IF  THESE RETURN NULL
        if (myLocation == null){
            myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider( new Criteria(), true));
        }
        googleMap.setMyLocationEnabled(true);
        mylatlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mylatlng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));


    }

    private void ShowGeoLocation(){
        LatLng mylatlng;
        String addr = myBundle.getString("LOCATION");

        GeoLocation x = new GeoLocation(getApplicationContext());
        mylatlng = x.getLatLngfromAddress(addr);
        if(mylatlng == null){
            Toast.makeText(getApplicationContext(),"Error here",Toast.LENGTH_SHORT).show();
        }
        MarkerOptions marker = new MarkerOptions().position(mylatlng).title("Here");
        googleMap.addMarker(marker);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mylatlng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));



    }

    private void guidance(){
        if(myBundle != null && myBundle.getBoolean("fromEventDetail",false)){
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=" + myBundle.getString("LOCATION")));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        super.onCreateOptionsMenu(m);
        CreateMenu(m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return MenuChoice(item);
    }

    private void CreateMenu(Menu m){
        m.add(0,0,0,"Guidance to Event");
        //m.add(0,1,1,"View Friend Requests");
    }

    private boolean MenuChoice(MenuItem item){

        switch (item.getItemId())
        {
            case 0:
                guidance();
                return true;


        }
        return false;
    }
}