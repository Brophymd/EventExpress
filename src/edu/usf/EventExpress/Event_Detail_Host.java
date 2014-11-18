package edu.usf.EventExpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.android.gms.maps.model.LatLng;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.event.EventSelection;
import edu.usf.EventExpress.provider.eventmembers.EventMembersColumns;
import edu.usf.EventExpress.provider.eventmembers.EventMembersSelection;
import edu.usf.EventExpress.provider.eventmembers.RSVPStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Created by Vi Tran on 10/19/2014.
 */
public class Event_Detail_Host extends Activity {

    Button edit, cancel, inviteFriends;
    ImageButton map;
    TextView title, description, location, date, time, attending;
    LatLng mylatlng;
    String memberCount;
    Context context;
    static DateFormat DF = new SimpleDateFormat("MM/dd/yyyy");
    static DateFormat TF = new SimpleDateFormat("h:mm a");
    long event_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_host);

        Bundle b = getIntent().getExtras();
        event_id = b.getLong("_ID");

        context = getApplicationContext();

//        EventSelection where = new EventSelection();
//        where.id(event_id);
//
//        Cursor cursor = context.getContentResolver().query(EventColumns.CONTENT_URI, null,
//                where.sel(), where.args(), null);
//
//        EventCursor event = new EventCursor(cursor);
//        event.moveToFirst();

        map = (ImageButton)findViewById(R.id.imageButton2);
        edit = (Button)findViewById(R.id.button_edit);
        cancel = (Button)findViewById(R.id.button_cancel);
        title = (TextView)findViewById(R.id.textView_eventTitle);
        description = (TextView)findViewById(R.id.textView_eventDescription);
        location = (TextView)findViewById(R.id.textView_HD_location);
        date = (TextView)findViewById(R.id.textView_date);
        time = (TextView)findViewById(R.id.textView_time);
        attending =(TextView)findViewById(R.id.textView_HD_attending);
        attending.setPaintFlags(attending.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        inviteFriends = (Button)findViewById(R.id.button_invite_Host);
        //eventID = getIntent().getExtras().getLong("EVENT_ID");

        setData(getEventCursor());


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
//                if(mylatlng != null) {
                    Intent myIntent = new Intent(v.getContext(), Event_Map.class);
                    Bundle myBundle = new Bundle();
                    myBundle.putString("LOCATION", location.getText().toString());
                    myBundle.putBoolean("fromEventDetailHost", true);
                    myIntent.putExtras(myBundle);
                    startActivity(myIntent);
//                }else Toast.makeText(getApplicationContext(), "Location cannot be mapped", Toast.LENGTH_SHORT).show();

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
                Bundle b = new Bundle();
                b.putLong("EVENT_ID",event_id);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }
        });

        attending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                        Event_Detail_Host.this);
                builderSingle.setTitle("Attendees: ");
//
//                SimpleCursorAdapter myAdapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.single_list_item, )
//                builderSingle.setNegativeButton("Done",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                builderSingle.setAdapter(arrayAdapter,
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String strName = arrayAdapter.getItem(which);
//                                AlertDialog.Builder builderInner = new AlertDialog.Builder(
//                                        DialogActivity.this);
//                                builderInner.setMessage(strName);
//                                builderInner.setTitle("Your Selected Item is");
//                                builderInner.setPositiveButton("Ok",
//                                        new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//                                                dialog.dismiss();
//                                            }
//                                        });
//                                builderInner.show();
//                            }
//                        });
                builderSingle.show();
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
            setData(getEventCursor());
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

    private void setData(EventCursor event){
        title.setText(event.getEventTitle());
        description.setText(event.getEventDescription());
        location.setText(event.getEventAddress());
//        if(event.getEventLatitude() != null && event.getEventLongitude()!= null)
//            mylatlng = new LatLng(event.getEventLatitude(),event.getEventLongitude());

        if(event.getEventDate() != null) {
            date.setText(DF.format(event.getEventDate()));
            time.setText(TF.format(event.getEventDate()));
        }
        attending.setText(Integer.toString(getEventMemberCount()));

        event.close();
    }

    private int getEventMemberCount(){
        EventMembersSelection where = new EventMembersSelection();
        where.eventId(event_id).and().rsvpStatus(RSVPStatus.yes);

        Cursor cursor = context.getContentResolver().query(EventMembersColumns.CONTENT_URI, null,
                where.sel(), where.args(), null);
        int memberCount = cursor.getCount();
        cursor.close();
        return memberCount;
    }

    private EventCursor getEventCursor(){
        EventSelection where = new EventSelection();
        where.id(event_id);

        Cursor cursor = context.getContentResolver().query(EventColumns.CONTENT_URI, null,
                where.sel(), where.args(), null);

        EventCursor event = new EventCursor(cursor);
        event.moveToFirst();
        return event;
    }

}