package edu.usf.EventExpress;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import edu.usf.EventExpress.provider.EventSQLiteOpenHelper;
import edu.usf.EventExpress.provider.event.*;
//import edu.usf.EventExpress.provider.event.EventCursorAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Varik on 10/12/2014.
 */
public class Event_myevents extends Activity {

    SimpleCursorAdapter myAdapter;
    public String userID;
    ArrayList<String> myStringArray;
    ArrayList<Long> eventIDList;
    ArrayAdapter listAdapter;
    ListView mainListView;
    static int fromCreate = 1;
    static int fromEdit = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_myevents);


        //Calendar rightNow = Calendar.getInstance();
        //EventContentValues values = new EventContentValues();
        //values.putEventAddress("612 St se largo fl 33771").putEventDate(rightNow.getTimeInMillis()).putEventTitle("Program-a-Bull").putEventOwner("Vi").putEventType(EventType.OPEN);

        String[] columns = new String[]{
                EventColumns.EVENT_TITLE,
                EventColumns.EVENT_DATE,
                EventColumns._ID
        };

        int[] to = new int[]{
                R.id.text_Title_row,
                R.id.text_Date_row,
                R.id.text_ID_row
        };

        Context context = getApplicationContext();

        //context.getContentResolver().insert(EventColumns.CONTENT_URI,values.values());

        EventSelection where = new EventSelection();
        Cursor cursor = context.getContentResolver().query(EventColumns.CONTENT_URI, null,
                where.sel(), where.args(), null);

        myAdapter = new SimpleCursorAdapter(this, R.layout.two_line_list_item,
                cursor,
                columns,
                to,
                0
                );

        //Find ListView to populate
        ListView lvItems = (ListView) findViewById(R.id.listView_myEvents);

        lvItems.setAdapter(myAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
                 Bundle b = new Bundle();
                TextView id = (TextView) view.findViewById(R.id.text_ID_row);
                String id_string = id.getText().toString();
                Long event_id = Long.parseLong(id_string);

                b.putLong("_ID", event_id.longValue());

                Intent intent = new Intent(view.getContext(), Event_Detail_Host.class);
                intent.putExtras(b);
                startActivityForResult(intent, 0);
            }
        });

    }



    public void CreateEvent(View v){

        Bundle myBundle = new Bundle();
        myBundle.putBoolean("CREATE",true);
        Intent myIntent = new Intent(this, Edit_Event.class);
        myIntent.putExtras(myBundle);
        startActivityForResult(myIntent, fromCreate);
    }

    protected void onActivityResult(int request_code, int result_code, Intent data){
        if(result_code == RESULT_OK){
            setList();


        }

    }

    private void setList(){
        myStringArray = new ArrayList<String>();
        eventIDList = new ArrayList<Long>();
        mainListView = (ListView) findViewById( R.id.listView_myEvents);

        SessionManager session = new SessionManager(getApplicationContext());
        userID = session.getUserID();

        EventSelection where = new EventSelection();
        where.eventOwner(userID);
        EventCursor event = where.query(getContentResolver());

        while(event.moveToNext()){
            myStringArray.add(event.getEventTitle());
            eventIDList.add(event.getId());
        }

        listAdapter = new ArrayAdapter<String>(this, R.layout.textrow, myStringArray);
        mainListView.setAdapter(listAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                //String temp = (String) ((TextView) view).getText();
                //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(view.getContext(), Event_Detail_Host.class);
                Bundle myBundle = new Bundle();
                myBundle.putLong("EVENT_ID",eventIDList.get(pos));
                myIntent.putExtras(myBundle);
                startActivityForResult(myIntent, fromEdit);
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

    private void fillList(){

    }

}