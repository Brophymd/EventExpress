package edu.usf.EventExpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.PersonBuffer;

/**
 * Created by Varik on 10/12/2014.
 */
public class Friendslist extends Activity implements GoogleApiClient.ConnectionCallbacks,ResultCallback<People.LoadPeopleResult> {
    String userID;
    ArrayList<String> myStringArray = new ArrayList<String>();
    ArrayAdapter listAdapter;
    ListView mainListView;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "FriendListActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist);
        userID = new SessionManager(getApplicationContext()).getUserID();
        mainListView = (ListView) findViewById( R.id.mainList );
        myStringArray.add("Mark");
        myStringArray.add("James");
        myStringArray.add("Brent");
        listAdapter = new ArrayAdapter<String>(this, R.layout.textrow, myStringArray);
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
                String temp = (String) ((TextView) view).getText();
                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
            }
        });
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) ((TextView) view).getText();
                temp = "Long press on "+ temp;
                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

    }
//    @Override
//    public void onConnected() {
//        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
//                .setResultCallback(this);
//    }

    @Override
    public void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
        if(!mGoogleApiClient.isConnecting())
            Toast.makeText(getApplicationContext(), "Client did not connect!", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onConnected(Bundle arg0){

        Log.d(TAG,"Successful Connection!");
//        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
//                .setResultCallback(this);
        List<String> userIds = new ArrayList<String>();
        userIds.add(userID);
        Plus.PeopleApi.load(mGoogleApiClient, userIds).setResultCallback(this);
    }

   @Override
   public void onConnectionSuspended(int i){
       Toast.makeText(getApplicationContext(),"Connection Suspended", Toast.LENGTH_SHORT).show();
   }
    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            Log.d(TAG, "Status was Success!");
            try {
                int count = personBuffer.getCount();

                Log.d(TAG, "personBuffer Count: "+count);
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
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
        m.add(0,0,0,"Send a Friend Request");
        m.add(0,1,1,"View Friend Requests");
    }

    private boolean MenuChoice(MenuItem item){

        switch (item.getItemId())
        {
            case 0:
                sendFriendRequest();
                return true;
            case 1:
                viewReceivedRequests();
                return true;

        }
        return false;
    }

    private void sendFriendRequest(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter friend's E-mail address");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Request",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String friend_request_id = input.getText().toString();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void viewReceivedRequests(){
        Intent myIntent = new Intent(this, Friendslist_ViewRequests.class);
        startActivity(myIntent);
    }
}