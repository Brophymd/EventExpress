package edu.usf.EventExpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

/**
 * Created by Varik on 10/12/2014.
 */
public class Friendslist extends Activity{
    String userID;
    ArrayList<String> myStringArray = new ArrayList<String>();
    ArrayAdapter listAdapter;
    ListView mainListView;
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