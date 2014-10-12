package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Varik on 10/12/2014.
 */
public class mainScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
    }

    public void friendsList(View v){

        Intent myIntent = new Intent(this, Friendslist.class);
        startActivity(myIntent);
    }

    public void eventMenu(View v){

        Intent myIntent = new Intent(this,EventMenu.class);
        startActivity(myIntent);
    }

    public void eventsInvites(View v){

        Intent myIntent = new Intent(this, Event_Invitations.class);
        startActivity(myIntent);
    }
}