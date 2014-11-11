package edu.usf.EventExpress;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.event.EventSelection;
import edu.usf.EventExpress.provider.eventmembers.EventMembersCursor;
import edu.usf.EventExpress.provider.eventmembers.EventMembersSelection;
import edu.usf.EventExpress.provider.eventmembers.RSVPStatus;

import java.util.ArrayList;

/**
 * Created by Varik on 10/12/2014.
 */
public class Event_Invitations extends Activity {

    String userID;
    ArrayList<String> myStringArray;
    ArrayAdapter listAdapter;
    ListView mainListView;
    ArrayList<Long> eventIDList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_invitations);
        loadList();


    }

    public void eventDetail(View v) {

        Intent myIntent = new Intent(this, Event_Detail.class);
        startActivity(myIntent);
    }

    private void loadList(){
        SessionManager session = new SessionManager(getApplicationContext());
        userID = session.getUserID();
        myStringArray = new ArrayList<String>();
        eventIDList = new ArrayList<Long>();

        EventMembersSelection emSel = new EventMembersSelection();
        emSel.userId().and().rsvpStatus(RSVPStatus.INVITED);
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