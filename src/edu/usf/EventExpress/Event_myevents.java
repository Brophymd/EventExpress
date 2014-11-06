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
public class Event_myevents extends Activity {
    //private String userid = "";
    ArrayList<String> myStringArray;
    ArrayAdapter listAdapter;
    ListView mainListView;
    static int fromCreate = 1;
    static int fromEdit = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_myevents);
        myStringArray = new ArrayList<String>();
        mainListView = (ListView) findViewById( R.id.listView_myEvents);
        myStringArray.add("SushiHut Dinner");
        //myStringArray.add("Halloween Party!");


        listAdapter = new ArrayAdapter<String>(this, R.layout.textrow, myStringArray);
        mainListView.setAdapter(listAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                //String temp = (String) ((TextView) view).getText();
                //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Event_Detail_Host.class);
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

    public void CreateEvent(View v){

        Bundle myBundle = new Bundle();
        myBundle.putBoolean("CREATE",true);
        Intent myIntent = new Intent(this, Edit_Event.class);
        myIntent.putExtras(myBundle);
        startActivityForResult(myIntent, fromCreate);
    }

    protected void onActivityResult(int request_code, int result_code, Intent data){
        if(result_code == 1){
            if(request_code == fromCreate){


            }
            if(request_code == fromEdit){

            }
        }

    }

}