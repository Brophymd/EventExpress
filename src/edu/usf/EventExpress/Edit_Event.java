package edu.usf.EventExpress;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Vi Tran on 10/26/2014.
 */
public class Edit_Event extends Activity {

    EditText et_title, et_description, et_location, et_date, et_time;
    String title, description, location, time, date;
    TextView CreateorEdit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        CreateorEdit = (TextView)findViewById(R.id.textView_eventTitle);
        et_title = (EditText)findViewById(R.id.editText_titleEdit);
        et_description = (EditText)findViewById(R.id.editText_eventDescriptionEdit);
        et_location = (EditText)findViewById(R.id.editText_locationEdit);
        et_date = (EditText)findViewById(R.id.editText_dateEdit);
        et_time = (EditText)findViewById(R.id.editText_timeEdit);

        Bundle b = getIntent().getExtras();

        if(b.getBoolean("CREATE",false)){
            CreateorEdit.setText("Create Event");
        }
        else {
            CreateorEdit.setText("Edit Event");
            title = b.getString("TITLE");
            description = b.getString("DESCRIPTION");
            location = b.getString("LOCATION");
            time = b.getString("TIME");
            date = b.getString("DATE");

            et_title.setText(title);
            et_description.setText(description);
            et_location.setText(location);
            et_date.setText(date);
            et_time.setText(time);
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
                    }
                }, hour,minute, false);
                myTimePicker.setTitle(R.string.lbl_Pick_Date);
                myTimePicker.show();

            }
        };

        et_time.setOnClickListener(timeClickEvent);

    }
}