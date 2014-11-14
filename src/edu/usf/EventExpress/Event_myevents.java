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
    EventCursorAdapter myEventCursorAdaptor;
    public String userID;
    ListView lvItems;
    static int fromCreate = 1;
    static int fromList = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_myevents);
        lvItems = (ListView) findViewById(R.id.listView_myEvents);
        fillList();

//        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                String temp = (String) ((TextView) view).getText();
//                temp = "Long press on "+ temp;
//                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
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
            onRestart();
            fillList();
        }

    }

    private void fillList(){
        Context context = getApplicationContext();

        EventSelection where = new EventSelection();
        Cursor cursor = context.getContentResolver().query(EventColumns.CONTENT_URI, null,
                where.sel(), where.args(), null);
        myEventCursorAdaptor = new EventCursorAdapter(context,cursor, 0);

        lvItems.setAdapter(myEventCursorAdaptor);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
                Bundle b = new Bundle();
                TextView id = (TextView) view.findViewById(R.id.text_ID_row);
                String id_string = id.getText().toString();
                Long event_id = Long.parseLong(id_string);

                b.putLong("_ID", event_id.longValue());

                Intent intent = new Intent(view.getContext(), Event_Detail_Host.class);
                intent.putExtras(b);
                startActivityForResult(intent, fromList);
            }
        });
    }

}