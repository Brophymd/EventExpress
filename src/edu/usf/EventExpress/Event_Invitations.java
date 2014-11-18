package edu.usf.EventExpress;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import edu.usf.EventExpress.provider.EventProvider;
import edu.usf.EventExpress.provider.EventSQLiteOpenHelper;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.eventmembers.EventMembersCursor;
import edu.usf.EventExpress.provider.eventmembers.EventMembersSelection;
import edu.usf.EventExpress.provider.eventmembers.RSVPStatus;
import edu.usf.EventExpress.provider.user.UserCursor;
import edu.usf.EventExpress.provider.user.UserSelection;

import java.util.ArrayList;

/**
 * Created by Varik on 10/12/2014.
 */
public class Event_Invitations extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID = 1;
    public static final String TABLE_NAME = "invitedEvents";
    private static final Uri CONTENT_URI = Uri.parse(EventProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);
    public static final String DEFAULT_ORDER = TABLE_NAME + "._id";
    String userID;
    ArrayList<String> myStringArray;
    ArrayAdapter listAdapter;
    ListView mainListView;
    ArrayList<Long> eventIDList;
    SimpleCursorAdapter mCursorAdapter;

    // Implement LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = CONTENT_URI;

        return new CursorLoader(getApplicationContext(), baseUri,
                new String[] {EventColumns._ID, EventColumns.EVENT_TITLE, EventColumns.EVENT_DATE}, null, null,
                EventColumns.EVENT_DATE);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_invitations);
        userID = new SessionManager(getApplicationContext()).getUserID();
        loadList();
        // create a view for invited events
        SQLiteDatabase db = EventSQLiteOpenHelper.getInstance(getApplicationContext()).getWritableDatabase();
        String SQL_CREATE_VIEW_INVITEDEVENTS = "CREATE VIEW IF NOT EXISTS invitedEvents AS " +
                "SELECT event._id, event.event_title, event.event_date " +
                "FROM event JOIN event_members " +
                "ON event_members.event_id = event._id " +
                "WHERE event_members.rsvp_status = 'invited';";
        db.execSQL(SQL_CREATE_VIEW_INVITEDEVENTS);
        DisplayList();
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void DisplayList(){
        ListView mainListView = (ListView) findViewById( R.id.listView_invitations);
        mCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item,
                null,
                new String[] {EventColumns.EVENT_TITLE, EventColumns.EVENT_DATE},
                new int[] {android.R.id.text1, android.R.id.text2}, 0);
        mainListView.setAdapter(mCursorAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
                String temp = (String) ((TextView) view).getText();
                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
            }
        });
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) ((TextView) view).getText();
                temp = "Long press on " + temp;
                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    /*public void eventDetail(View v) {

        Intent myIntent = new Intent(this, Event_Detail.class);
        startActivity(myIntent);
    }*/

    private void loadList(){
        SessionManager session = new SessionManager(getApplicationContext());
        userID = session.getUserID();
        myStringArray = new ArrayList<String>();
        eventIDList = new ArrayList<Long>();

        UserSelection userSel = new UserSelection();
        UserCursor ucur = userSel.googleId(userID).query(getContentResolver());
        Long databaseID = null;
        while(ucur.moveToNext()){
            databaseID = ucur.getId();
        }
        EventMembersSelection emSel = new EventMembersSelection();
        emSel.userId(userID).and().rsvpStatus(RSVPStatus.invited);
        EventMembersCursor emcursor = emSel.query(getContentResolver());

        while(emcursor.moveToNext()){
            myStringArray.add(emcursor.getEventTitle());
            eventIDList.add(emcursor.getId());
        }

        mainListView = (ListView) findViewById( R.id.listView_invitations );
        listAdapter = new ArrayAdapter<String>(this, R.layout.textrow, myStringArray);
        mainListView.setAdapter(listAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
                //String temp = (String) ((TextView) view).getText();
                //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Event_Detail.class);
                startActivity(intent);
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


}