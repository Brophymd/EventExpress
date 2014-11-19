package edu.usf.EventExpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import edu.usf.EventExpress.ViewManager.UserAttendingEventFilter;
import edu.usf.EventExpress.provider.EventProvider;
import edu.usf.EventExpress.provider.EventSQLiteOpenHelper;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.event.EventContentValues;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.event.EventSelection;
import edu.usf.EventExpress.provider.eventmembers.*;
import edu.usf.EventExpress.provider.user.UserColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vi Tran on 10/19/2014.
 */
public class Event_Detail extends Activity {

    Button bt_accept, bt_decline, bt_delete;
    ImageButton map;
    TextView title, description, location, date, time, attending;
    Context context;
    static DateFormat DF = new SimpleDateFormat("MM/dd/yyyy");
    static DateFormat TF = new SimpleDateFormat("h:mm a");
    long event_id;
    String userID;
    private static final String TAG = "Event_Detail";

    private static final int LOADER_ID = 1;
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    SimpleCursorAdapter mCursorAdapter;
    public static final String TABLE_NAME = "attending_event";
    public static final Uri CONTENT_URI = Uri.parse(EventProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);
    public static final String DEFAULT_ORDER = TABLE_NAME + "._id";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        context = getApplicationContext();
        userID = new SessionManager(getApplicationContext()).getUserID();

        //Get event ID from parent activity
        Bundle b = getIntent().getExtras();
        event_id = b.getLong("_ID");

        //Set references to button widgets in Event_Detail layout
        map = (ImageButton)findViewById(R.id.imageButton2);
        title = (TextView)findViewById(R.id.textView_eventTitle);
        description = (TextView)findViewById(R.id.textView_eventDescription);
        location = (TextView)findViewById(R.id.textView_location);
        date = (TextView)findViewById(R.id.textView_date);
        time = (TextView)findViewById(R.id.textView_time);
        attending =(TextView)findViewById(R.id.textView_attending);
        attending.setPaintFlags(attending.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        bt_accept = (Button)findViewById(R.id.button_accept);
        bt_decline = (Button)findViewById(R.id.button_decline);
        bt_delete = (Button)findViewById(R.id.button_delete);
        setButtonVisibility();

        //set text views from event specified by event_id;
        setData(getEventCursor());
        View.OnClickListener showOnMap = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mylatlng != null) {
                Intent myIntent = new Intent(v.getContext(), Event_Map.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("LOCATION", location.getText().toString());
                myBundle.putBoolean("fromEventDetailHost", true);
                myIntent.putExtras(myBundle);
                startActivity(myIntent);
//                }else Toast.makeText(getApplicationContext(), "Location cannot be mapped", Toast.LENGTH_SHORT).show();

            }
        };
        map.setOnClickListener(showOnMap);

        //Alert Dialog with attendees
        attending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                        Event_Detail.this);
                builderSingle.setTitle("Attendees: ");
                mCursorAdapter = new SimpleCursorAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        null,
                        new String[] {UserColumns.USER_NAME},
                        new int[] {android.R.id.text1}, 0);
                getLoaderManager().initLoader(LOADER_ID, null, new UserAttendingEventFilter(getApplicationContext(),
                        CONTENT_URI, mCursorAdapter, event_id));

//                EventMembersSelection where = new EventMembersSelection();
//                where.eventId(event_id);
//                Cursor cursor = context.getContentResolver().query(EventMembersColumns.CONTENT_URI, null,
//                        where.sel(), where.args(), null);
//                PeopleAttendingCursorAdapter mCursorAdapter = new PeopleAttendingCursorAdapter(context, cursor,0);

//                SimpleCursorAdapter myAdapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.single_list_item, )
                builderSingle.setNegativeButton("Done",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setAdapter(mCursorAdapter,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builderSingle.show();
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline();
            }
        });
        bt_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline();
            }
        });
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        setData(getEventCursor());
    }

    private void setButtonVisibility(){
        Bundle b = getIntent().getExtras();
        int from_activity_id = b.getInt("ACTIVITY_FROM_ID");
        if(from_activity_id == 0) { //from Event_Invitations_ID
            bt_accept.setVisibility(View.VISIBLE);
            bt_decline.setVisibility(View.VISIBLE);
            bt_delete.setVisibility(View.GONE);
        }else{
            bt_accept.setVisibility(View.GONE);
            bt_decline.setVisibility(View.GONE);
            bt_delete.setVisibility(View.VISIBLE);
        }
    }

    private void accept(){
        EventMembersSelection where = new EventMembersSelection();
        EventMembersSelection x = where.eventId(event_id).and().userId(userID);
        EventMembersCursor event = where.query(getContentResolver());
        event.moveToNext();
        EventMembersContentValues values = new EventMembersContentValues();
        values.putEventMembersSynced(0)
                .putRsvpStatus(RSVPStatus.yes)
                .putEventMembersTimestamp(new Date().getTime())
                .update(context.getContentResolver(), x);
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    private void decline(){
        EventMembersSelection where = new EventMembersSelection();
        EventMembersSelection x = where.eventId(event_id).and().userId(userID);
        EventMembersCursor event = where.query(getContentResolver());
        event.moveToNext();
        EventMembersContentValues values = new EventMembersContentValues();
        values.putEventMembersSynced(0)
                .putRsvpStatus(RSVPStatus.no)
                .putEventMembersTimestamp(new Date().getTime())
                .update(context.getContentResolver(), x);
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    private void setData(EventCursor event){
        title.setText(event.getEventTitle());
        description.setText(event.getEventDescription());
        location.setText(event.getEventAddress());

        if(event.getEventDate() != null) {
            date.setText(DF.format(event.getEventDate()));
            time.setText(TF.format(event.getEventDate()));
        }
        attending.setText(Integer.toString(getEventMemberCount()));

        event.close();
    }

    private int getEventMemberCount(){
        EventMembersSelection where = new EventMembersSelection();
        where.eventId(event_id).and().rsvpStatus(RSVPStatus.yes);

        Cursor cursor = context.getContentResolver().query(EventMembersColumns.CONTENT_URI, null,
                where.sel(), where.args(), null);
        int memberCount = cursor.getCount();
        cursor.close();
        return memberCount;
    }

    private EventCursor getEventCursor(){
        EventSelection where = new EventSelection();
        where.id(event_id);

        Cursor cursor = context.getContentResolver().query(EventColumns.CONTENT_URI, null,
                where.sel(), where.args(), null);

        EventCursor event = new EventCursor(cursor);
        event.moveToFirst();
        return event;
    }

    @Override
    protected void onStop() {
        super.onStop();
        SQLiteDatabase db = EventSQLiteOpenHelper.getInstance(getApplicationContext()).getWritableDatabase();
        db.execSQL("drop view if exists " + TABLE_NAME + ";");
        db.close();
    }
}
