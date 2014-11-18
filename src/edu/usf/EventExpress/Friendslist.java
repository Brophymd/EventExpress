package edu.usf.EventExpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import java.util.ArrayList;

import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import edu.usf.EventExpress.provider.EventProvider;
import edu.usf.EventExpress.provider.EventSQLiteOpenHelper;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.event.EventSelection;
import edu.usf.EventExpress.provider.eventmembers.EventMembersSelection;
import edu.usf.EventExpress.provider.eventmembers.RSVPStatus;
import edu.usf.EventExpress.provider.friendstatus.*;
import edu.usf.EventExpress.provider.user.UserColumns;
import edu.usf.EventExpress.provider.user.UserSelection;
import edu.usf.EventExpress.sync.SyncHelper;

/**
 * Created by Varik on 10/12/2014.
 */
public class Friendslist extends Activity
    implements LoaderManager.LoaderCallbacks<Cursor> {
    String userID;
//    ArrayList<String> friendNames;
    private static final int LOADER_ID = 1;
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    SimpleCursorAdapter mCursorAdapter;
    public static final String TABLE_NAME = "acceptedFriends";
    public static final Uri CONTENT_URI = Uri.parse(EventProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);
    public static final String DEFAULT_ORDER = TABLE_NAME + "._id";

    // Implement LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = CONTENT_URI;
        Log.d(TAG, "In onCreateLoader");

//        FriendStatusCursor friendStatusCursor = new FriendStatusSelection().status(FriendStatusType.accepted)
//                .query(getContentResolver());
//        String selection;
//        String[] selectionArgs;
//        friendStatusCursor.moveToFirst();
//        Log.d(TAG, "User ID is " + friendStatusCursor.getFromUserId());
//        UserSelection userSelection;
//        // if current user was the requester
//        if (friendStatusCursor.getFromUserId().equals(new SessionManager(getApplicationContext()).getUserID())) {
//            userSelection = new UserSelection().googleId(friendStatusCursor.getToUserId());
//        }
//        // if current user was the requestee
//        else {
//            userSelection = new UserSelection().googleId(friendStatusCursor.getFromUserId());
//        }
        // need to fix this; it only fetches one user right now
        return new CursorLoader(getApplicationContext(), baseUri,
                new String[] {UserColumns._ID, UserColumns.USER_NAME}, null, null,
                UserColumns.USER_NAME);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private static final String TAG = "FriendListActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist);
        userID = new SessionManager(getApplicationContext()).getUserID();
        // get database and create new view
        SQLiteDatabase db = EventSQLiteOpenHelper.getInstance(getApplicationContext()).getWritableDatabase();
        String SQL_CREATE_VIEW_ACCEPTEDFRIENDS = "CREATE VIEW IF NOT EXISTS acceptedFriends AS " +
                "SELECT user._id, user.user_name " +
                "FROM user JOIN friend_status " +
                "ON friend_status.from_user_id = user.google_id " +
                "OR friend_status.to_user_id = user.google_id " +
                "AND friend_status.status = 'accepted' " +
                "WHERE user.google_id != '" + new SessionManager(getApplicationContext()).getUserID() + "';";
        db.execSQL(SQL_CREATE_VIEW_ACCEPTEDFRIENDS);
        // display stuff
        DisplayList();
        getLoaderManager().initLoader(0, null, this);
    }

    private void DisplayList(){
        ListView mainListView = (ListView) findViewById( R.id.mainList );
        mCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[] {UserColumns.USER_NAME},
                new int[] {android.R.id.text1}, 0);
        mainListView.setAdapter(mCursorAdapter);
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
                FriendStatusContentValues FSCV = new FriendStatusContentValues();
                FSCV.putToUserEmail(friend_request_id.trim())
                        .putFromUserId(userID).putStatus(FriendStatusType.requested)
                        .putFromUserEmail(new SessionManager(getApplicationContext()).getEmail())
                        .insert(getContentResolver());
                SyncHelper.manualSync(getApplicationContext());


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