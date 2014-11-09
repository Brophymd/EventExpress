package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.event.EventSelection;


/**
 * Created by Vi Tran on 10/19/2014.
 */
public class Event_Detail_Host extends Activity {

    Button edit;
    ImageButton map;
    TextView title, description, location, date, time;
    Long eventID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_host);

        map = (ImageButton)findViewById(R.id.imageButton2);
        edit = (Button)findViewById(R.id.button_edit);
        title = (TextView)findViewById(R.id.textView_eventTitle);
        description = (TextView)findViewById(R.id.textView_eventDescription);
        location = (TextView)findViewById(R.id.textView_HD_location);
        date = (TextView)findViewById(R.id.textView_date);
        time = (TextView)findViewById(R.id.textView_time);
        eventID = getIntent().getExtras().getLong("EVENT_ID");

        setData();


        View.OnClickListener editClickEvent = new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Intent myIntent = new Intent(arg0.getContext(), Edit_Event.class);
                Bundle b = new Bundle();
                b.putLong("EVENT_ID",eventID);
                /*
                b.putString("TITLE", title.getText().toString());
                b.putString("DESCRIPTION", description.getText().toString());
                b.putString("LOCATION",location.getText().toString());
                b.putString("TIME", time.getText().toString());
                b.putString("DATE", date.getText().toString());*/

                myIntent.putExtras(b);
                startActivityForResult(myIntent,0);

            }
        };
        edit.setOnClickListener(editClickEvent);

        View.OnClickListener showOnMap = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),Event_Map.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("LOCATION", location.getText().toString());
                myBundle.putBoolean("fromEventDetail",true);
                myIntent.putExtras(myBundle);
                startActivity(myIntent);

            }
        };
        map.setOnClickListener(showOnMap);
    }

    protected void onActivityResult(int request_code, int result_code, Intent data){
        if(result_code == RESULT_OK){
            setData();


        }

    }

    private void setData(){
        EventSelection where = new EventSelection();
        where.id(eventID);
        EventCursor event = where.query(getContentResolver());
        event.moveToNext();
        title.setText(event.getEventTitle());
        description.setText(event.getEventDescription());
        location.setText(event.getEventAddress());
    }

}