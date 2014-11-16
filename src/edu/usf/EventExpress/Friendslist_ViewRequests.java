package edu.usf.EventExpress;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Varik on 10/12/2014.
 */
public class Friendslist_ViewRequests extends Activity {

    String userID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_viewrequests);
        userID = new SessionManager(getApplicationContext()).getUserID();
        ArrayList<String> friendRequestEmail = new ArrayList<String>();
        ArrayList<Long> friendequestID = new ArrayList<Long>();

    }
}