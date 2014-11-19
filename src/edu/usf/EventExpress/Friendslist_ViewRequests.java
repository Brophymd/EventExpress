package edu.usf.EventExpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusContentValues;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusCursor;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusSelection;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusType;

import java.util.ArrayList;

/**
 * Created by Varik on 10/12/2014.
 */
public class Friendslist_ViewRequests extends Activity {
    private static final String TAG = Friendslist_ViewRequests.class.getSimpleName();
    ArrayList<String> friendRequestEmail;
    ArrayList<Long> friendRequestID;
    String userID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_viewrequests);
        userID = new SessionManager(getApplicationContext()).getUserID();
        DisplayList();

    }

    private void DisplayList(){
        ListView mainListView = (ListView) findViewById( R.id.listView_invitations );
        friendRequestEmail = new ArrayList<String>();
        friendRequestID = new ArrayList<Long>();
        FriendStatusSelection FSS = new FriendStatusSelection();
        FriendStatusCursor FSC = FSS.status(FriendStatusType.requested)
                .query(getContentResolver());
        Log.d(TAG, "Got FriendStatusCursor");

        while(FSC.moveToNext()){
            String email = FSC.getFromUserEmail();
            if(!email.equals(new SessionManager(getApplicationContext()).getEmail())) {
                friendRequestEmail.add(email);
            }
        }
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.textrow, friendRequestEmail);
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
                final String selected = (String) ((TextView) view).getText();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Accept Friend: "+selected+" ?");
                builder.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Accept Friend Request
                        AcceptFriendRequest(selected);
                    }
                });
                builder.setNegativeButton("Refuse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Refuse Friend Request
                        RefuseFriendRequest(selected);

                    }
                });
                builder.show();
            }
        });
    }

    private void AcceptFriendRequest(String selected){
        Log.d(TAG, "In AcceptFriendRequest, selected = " + selected);
        FriendStatusSelection friendStatusSelection = new FriendStatusSelection();
        FriendStatusContentValues friendStatusContentValues = new FriendStatusContentValues();
        friendStatusContentValues.putStatus(FriendStatusType.accepted)
                .putFriendStatusSynced(0)
                .update(getApplicationContext().getContentResolver(), friendStatusSelection
                        .fromUserEmail(selected)
                        .and()
                        .toUserEmail(new SessionManager(getApplicationContext()).getEmail()));

        DisplayList();
    }

    private void RefuseFriendRequest(String selected){
        FriendStatusSelection friendStatusSelection = new FriendStatusSelection();
        FriendStatusContentValues friendStatusContentValues = new FriendStatusContentValues();
        friendStatusContentValues.putStatus(FriendStatusType.rejected)
                .putFriendStatusSynced(0)
                .update(getApplicationContext().getContentResolver(), friendStatusSelection
                        .fromUserEmail(selected)
                        .and()
                        .toUserEmail(new SessionManager(getApplicationContext()).getEmail()));

        DisplayList();

    }
}