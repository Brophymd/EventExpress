package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Varik on 10/12/2014.
 */
public class EventMenu extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventmenu);

    }

    public void createdEvents(View v){

        Intent myIntent = new Intent(this, Event_myevents.class);
        startActivity(myIntent);
    }

    public void eventsInvites(View v){

        Intent myIntent = new Intent(this, Event_Invitations.class);
        startActivity(myIntent);
    }

    public void eventsAttending(View v){

        Intent myIntent = new Intent(this, Event_Attending.class);
        startActivity(myIntent);
    }

    /*public void eventsArea(View v){

        Intent myIntent = new Intent(this, Events_Area.class);
        startActivity(myIntent);
    }*/

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
        m.add(0,0,0,"MAP");

    }

    private boolean MenuChoice(MenuItem item){

        switch (item.getItemId())
        {
            case 0:
                Intent myIntent = new Intent(this, Event_Map.class);
                startActivity(myIntent);
                return true;

        }
        return false;
    }
}