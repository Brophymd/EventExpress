package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Varik on 10/12/2014.
 */
public class Event_myevents extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_myevents);
    }

    public void CreateEvent(View v){

        Intent myIntent = new Intent(this, createEvent.class);
        startActivity(myIntent);
    }

}