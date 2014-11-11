package edu.usf.EventExpress;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.*;
import edu.usf.EventExpress.provider.event.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vi Tran on 10/26/2014.
 */
public class Edit_Event extends Activity {

    EditText et_title, et_description, et_location, et_date, et_time;
    String title, description, location, time, date;
    TextView CreateorEdit;
    Button Save;
    String userID;
    Long eventID;
    boolean fromCreate;
    static DateFormat DF = new SimpleDateFormat("MM/dd/yyyy");
    static DateFormat TF = new SimpleDateFormat("h:mm a");
    Date DateandTime;
    int selYear, selMonth, selDay, selHour, selMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);
        fromCreate = false;
        Save = (Button)findViewById(R.id.button_save);
        CreateorEdit = (TextView)findViewById(R.id.textView_editEvent);
        et_title = (EditText)findViewById(R.id.editText_titleEdit);
        et_description = (EditText)findViewById(R.id.editText_eventDescriptionEdit);
        et_location = (EditText)findViewById(R.id.editText_locationEdit);
        et_date = (EditText)findViewById(R.id.editText_dateEdit);
        et_time = (EditText)findViewById(R.id.editText_timeEdit);
        userID = new SessionManager(getApplicationContext()).getUserID();


        Bundle b = getIntent().getExtras();

        if(b.getBoolean("CREATE",false)){
            CreateorEdit.setText("Create Event");
            fromCreate = true;
        }
        else {
            CreateorEdit.setText("Edit Event");
            eventID = getIntent().getExtras().getLong("EVENT_ID");
            EventSelection where = new EventSelection();
            where.id(eventID);
            EventCursor event = where.query(getContentResolver());
            event.moveToNext();

            et_title.setText(event.getEventTitle());
            et_description.setText(event.getEventDescription());
            et_location.setText(event.getEventAddress());
            et_date.setText(DF.format(event.getEventDate()));
            et_time.setText(TF.format(event.getEventDate()));
            DateandTime = event.getEventDate();
            Calendar loadDate = Calendar.getInstance();
            loadDate.setTime(DateandTime);
            selYear = loadDate.get(Calendar.YEAR);
            selMonth = loadDate.get(Calendar.MONTH);
            selDay = loadDate.get(Calendar.DAY_OF_MONTH);
            selHour = loadDate.get(Calendar.HOUR_OF_DAY);
            selMinute = loadDate.get(Calendar.MINUTE);



        }


        View.OnClickListener dateClickEvent = new View.OnClickListener() {
            public void onClick(View arg0) {
                Calendar todaysDate = Calendar.getInstance();
                int year = todaysDate.get(Calendar.YEAR);
                int month = todaysDate.get(Calendar.MONTH);
                int day = todaysDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDatePicker = new DatePickerDialog(Edit_Event.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yearPick, int monthPick, int dayPick) {
                        monthPick += 1;
                        et_date.setText(monthPick + "/" + dayPick + "/" + yearPick);
                        //selectedDate = new Date(yearPick - 1900,monthPick - 1,dayPick);
                        selYear = yearPick;
                        selMonth = monthPick;
                        selDay = dayPick;
                    }
                }, year, month, day);
                myDatePicker.setTitle(R.string.lbl_Pick_Date);
                myDatePicker.show();

            }
        };

        et_date.setOnClickListener(dateClickEvent);

        View.OnClickListener timeClickEvent = new View.OnClickListener() {
            public void onClick(View arg0) {
                Time currentTime = new Time();
                currentTime.setToNow();
                int hour = currentTime.hour ;
                int minute = currentTime.minute;

                TimePickerDialog myTimePicker = new TimePickerDialog(Edit_Event.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourPick, int minutePick) {

                        int displayHour = hourPick;
                        String AMPM = "AM";

                        if (hourPick>12) {
                            AMPM = "PM";
                            displayHour -= 12;
                        }

                        if(hourPick == 0)
                            displayHour = 12;
                        et_time.setText(displayHour + ":" + String.format("%02d",minutePick)  + " " + AMPM);
                        selHour = hourPick;
                        selMinute = minutePick;
                    }
                }, hour,minute, false);
                myTimePicker.setTitle(R.string.lbl_Pick_Date);
                myTimePicker.show();

            }
        };

        et_time.setOnClickListener(timeClickEvent);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, selYear);
                cal.set(Calendar.MONTH, selMonth);
                cal.set(Calendar.DAY_OF_MONTH, selDay);
                cal.set(Calendar.HOUR_OF_DAY, selHour);
                cal.set(Calendar.MINUTE,selMinute);
                DateandTime = cal.getTime();
                Context context = getApplicationContext();
                if(fromCreate) {
                    EventContentValues values = new EventContentValues();
                    values.putEventOwner(userID).putEventType(EventType.OPEN)
                            .putEventTitle(et_title.getText().toString())
                            .putEventDescription(et_description.getText().toString())
                            .putEventAddress(et_location.getText().toString())
                            .putEventDate(DateandTime);
                    context.getContentResolver().insert(EventColumns.CONTENT_URI, values.values());
                }
                else{
                    EventSelection where = new EventSelection();
                    EventSelection x = where.id(eventID);
                    EventCursor event = where.query(getContentResolver());
                    event.moveToNext();
                    EventContentValues values = new EventContentValues();
                    values.putEventOwner(event.getEventOwner()).putEventType(EventType.OPEN)
                            .putEventTitle(et_title.getText().toString())
                            .putEventDescription(et_description.getText().toString())
                            .putEventAddress(et_location.getText().toString())
                            .putEventDate(DateandTime)
                            .update(context.getContentResolver(), x);
                    //context.getContentResolver().update(EventColumns.CONTENT_URI, values.values(), x.sel(), null);

                }



                Intent returnIntent = new Intent();
                ////returnIntent.putExtra("result",result);
                setResult(RESULT_OK,returnIntent);
                finish();

                //Intent intent = new Intent(getApplicationContext(),Event_myevents.class);
                //startActivity(intent);
            }
        });



    }
}