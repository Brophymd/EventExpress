package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import edu.usf.EventExpress.ViewManager.EventRsvpStatusFilter;
import edu.usf.EventExpress.provider.EventProvider;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.eventmembers.RSVPStatus;

/**
 * Created by Varik on 10/12/2014.
 */
public class Event_Attending extends Activity {
    public static final String TABLE_NAME = "attendedEvents";
    private static final Uri CONTENT_URI = Uri.parse(EventProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);
    public static final String DEFAULT_ORDER = TABLE_NAME + "._id";
    private static final int LOADER_ID = 1;
    public static final int EVENT_ATTENDING_ACTIVITY_ID = 1;
    String userID;
    SimpleCursorAdapter mCursorAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_attending);
        userID = new SessionManager(getApplicationContext()).getUserID();
        DisplayList();
        getLoaderManager().initLoader(LOADER_ID,
                null,
                new EventRsvpStatusFilter(getApplicationContext(), TABLE_NAME, CONTENT_URI, mCursorAdapter, RSVPStatus.yes));
    }
    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID, null, new EventRsvpStatusFilter(getApplicationContext(), TABLE_NAME, CONTENT_URI, mCursorAdapter, RSVPStatus.yes));
    }

    private void DisplayList(){
        ListView mainListView = (ListView) findViewById(R.id.listView_attendedEvents);
        mCursorAdapter = new MemberCursorAdapter(this,
                R.layout.two_line_list_item,
                null,
                new String[] {EventColumns._ID, EventColumns.EVENT_TITLE, EventColumns.EVENT_DATE},
                new int[] {R.id.text_ID_row, R.id.text_Title_row, R.id.text_Date_row}, 0);
        mainListView.setAdapter(mCursorAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
                Bundle b = new Bundle();
                TextView id = (TextView) view.findViewById(R.id.text_ID_row);
                String id_string = id.getText().toString();
                Long event_id = Long.parseLong(id_string);

                b.putLong("_ID", event_id.longValue());
                b.putInt("ACTIVITY_FROM_ID", EVENT_ATTENDING_ACTIVITY_ID); //Tell event_detail that event comes from invited events

                Intent intent = new Intent(view.getContext(), Event_Detail.class);
                intent.putExtras(b);
                startActivityForResult(intent, 0);
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
}