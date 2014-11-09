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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Varik on 10/12/2014.
 */
public class Event_myevents extends Activity {

    SimpleCursorAdapter myAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_myevents);


//        mainListView = (ListView) findViewById( R.id.listView_myEvents);
//        myStringArray.add("SushiHut Dinner");
//        myStringArray.add("Halloween Party!");
//
//        listAdapter = new ArrayAdapter<String>(this, R.layout.textrow, myStringArray);
//        mainListView.setAdapter(listAdapter);
//        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
//                //String temp = (String) ((TextView) view).getText();
//                //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(view.getContext(), Event_Detail_Host.class);
//                startActivity(intent);
//            }
//        });
//        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                String temp = (String) ((TextView) view).getText();
//                temp = "Long press on "+ temp;
//                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//
        /*
         *new list stuff
            */

        Calendar rightNow = Calendar.getInstance();
        EventContentValues values = new EventContentValues();
        values.putEventAddress("612 stupid st se largo fl 33771").putEventDate(rightNow.getTimeInMillis()).putEventTitle("Program-a-Bull").putEventOwner("Vi").putEventType(EventType.OPEN);

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

        context.getContentResolver().insert(EventColumns.CONTENT_URI,values.values());

        EventSelection where = new EventSelection();
        //where.eventTitle("Sushi Hut Dinner");
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
                //String temp = (String) ((TextView) view).getText();
                //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                Bundle b = new Bundle();
                TextView id = (TextView) view.findViewById(R.id.text_ID_row);
                String id_string = id.getText().toString();
                //System.out.println("ID is: " + id.getText().toString());

                b.putString("_ID", id_string);

                Intent intent = new Intent(view.getContext(), Event_Detail_Host.class);
                intent.putExtras(b);
                startActivityForResult(intent, 0);
            }
        });

    }

    public void CreateEvent(View v){

        Intent myIntent = new Intent(this, createEvent.class);
        startActivity(myIntent);
    }

}