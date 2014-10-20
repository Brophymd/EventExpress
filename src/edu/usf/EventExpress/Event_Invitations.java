package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by Varik on 10/12/2014.
 */
public class Event_Invitations extends Activity {

    private String userid = "";
    ArrayList<String> myStringArray = new ArrayList<String>();
    ArrayAdapter listAdapter;
    ListView mainListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_invitations);
        mainListView = (ListView) findViewById( R.id.listView_invitations );
        myStringArray.add("Bulls Picnic");
        myStringArray.add("IEEE Program-a-Bull");

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

    public void eventDetail(View v) {

        Intent myIntent = new Intent(this, Event_Detail.class);
        startActivity(myIntent);
    }


}