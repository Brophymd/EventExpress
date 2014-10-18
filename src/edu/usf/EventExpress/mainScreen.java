package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Varik on 10/12/2014.
 */
public class mainScreen extends Activity {

    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        session = new SessionManager(getApplicationContext());
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
        m.add(0,0,0,"Logout and Exit");

    }

    private boolean MenuChoice(MenuItem item){

        switch (item.getItemId())
        {
            case 0:
                session.logOut();
                android.os.Process.killProcess(android.os.Process.myPid());

                return true;

        }
        return false;
    }
}