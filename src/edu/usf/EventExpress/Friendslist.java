package edu.usf.EventExpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created by Varik on 10/12/2014.
 */
public class Friendslist extends Activity {
    private String userid = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist);
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

        if(item.getItemId() == 0){
            sendFriendRequest();
            return true;
        }
        if(item.getItemId() == 1){
            viewReceivedRequests();
            return true;
        }
        return false;
    }

    private void sendFriendRequest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter friend's UserID");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Request",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userid = input.getText().toString();
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