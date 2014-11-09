package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.event.EventSelection;


import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Varik on 10/12/2014.
 */
public class Event_myevents extends Activity {
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
        setList();

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

}