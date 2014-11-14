package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.event.EventSelection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Created by Vi Tran on 10/19/2014.
 */
public class Event_Detail_Host extends Activity {

    Button edit, cancel, inviteFriends;
    ImageButton map;
    TextView title, description, location, date, time;
    LatLng mylatlng;
    static DateFormat DF = new SimpleDateFormat("MM/dd/yyyy");
    static DateFormat TF = new SimpleDateFormat("h:mm a");
    long event_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_host);

        Bundle b = getIntent().getExtras();
        event_id = b.getLong("_ID");

        Context context = getApplicationContext();

        EventSelection where = new EventSelection();
        where.id(event_id);

        Cursor cursor = context.getContentResolver().query(EventColumns.CONTENT_URI, null,
                where.sel(), where.args(), null);

        EventCursor event = new EventCursor(cursor);
        event.moveToFirst();


        map = (ImageButton)findViewById(R.id.imageButton2);
        edit = (Button)findViewById(R.id.button_edit);
        cancel = (Button)findViewById(R.id.button_cancel);
        title = (TextView)findViewById(R.id.textView_eventTitle);
        description = (TextView)findViewById(R.id.textView_eventDescription);
        location = (TextView)findViewById(R.id.textView_HD_location);
        date = (TextView)findViewById(R.id.textView_date);
        time = (TextView)findViewById(R.id.textView_time);
        inviteFriends = (Button)findViewById(R.id.button_invite_Host);
        //eventID = getIntent().getExtras().getLong("EVENT_ID");

        //setData();



        title.setText(event.getEventTitle());
        description.setText(event.getEventDescription());
        location.setText(event.getEventAddress());
        if(event.getEventLatitude() != null && event.getEventLongitude()!= null)
            mylatlng = new LatLng(event.getEventLatitude(),event.getEventLongitude());

        if(event.getEventDate() != null) {
            date.setText(DF.format(event.getEventDate()));
            time.setText(TF.format(event.getEventDate()));
        }

        View.OnClickListener editClickEvent = new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Intent myIntent = new Intent(arg0.getContext(), Edit_Event.class);
                Bundle b = new Bundle();
                b.putLong("EVENT_ID",event_id);

                myIntent.putExtras(b);
                startActivityForResult(myIntent,0);

            }
        };
        edit.setOnClickListener(editClickEvent);

        View.OnClickListener showOnMap = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mylatlng != null) {
                    Intent myIntent = new Intent(v.getContext(), Event_Map.class);
                    Bundle myBundle = new Bundle();
                    myBundle.putString("LOCATION", location.getText().toString());
                    myBundle.putBoolean("fromEventDetailHost", true);
                    myIntent.putExtras(myBundle);
                    startActivity(myIntent);
                }else Toast.makeText(getApplicationContext(), "Location cannot be mapped", Toast.LENGTH_SHORT).show();

            }
        };
        map.setOnClickListener(showOnMap);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

        inviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Friend_Invite.class);
                startActivity(myIntent);
            }
        });
    }

//    @Override
//    public void onRestart(){
//        EventSelection where = new EventSelection();
//        where.id(event_id);
//        Context context = getApplicationContext();
//        Cursor cursor = context.getContentResolver().query(EventColumns.CONTENT_URI, null,
//                where.sel(), where.args(), null);
//
//        EventCursor event = new EventCursor(cursor);
//        event.moveToFirst();
//        if(event.getEventLatitude() != null && event.getEventLongitude() != null)
//             mylatlng = new LatLng(event.getEventLatitude(),event.getEventLongitude());
//    }
    protected void onActivityResult(int request_code, int result_code, Intent data){
        if(result_code == RESULT_OK){
            onRestart();
            setData();
        }

    }

    private void deleteEvent(){
        EventSelection where = new EventSelection();
        where.id(event_id);
        where.delete(getContentResolver());
        //Intent intent = new Intent(getApplicationContext(), Event_myevents.class);
        //startActivity(intent);
        Intent returnIntent = new Intent();
        ////returnIntent.putExtra("result",result);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    private void setData(){
        EventSelection where = new EventSelection();
        where.id(event_id);
        EventCursor event = where.query(getContentResolver());
        event.moveToNext();
        title.setText(event.getEventTitle());
        description.setText(event.getEventDescription());
        location.setText(event.getEventAddress());
        date.setText(DF.format(event.getEventDate()));
        time.setText(TF.format(event.getEventDate()));
    }

}