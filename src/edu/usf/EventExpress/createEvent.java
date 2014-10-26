package edu.usf.EventExpress;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;


import java.util.Calendar;


/**
 * Created by Varik on 10/12/2014.
 */
public class createEvent extends Activity {

    EditText time,date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        time = (EditText)findViewById(R.id.editText_time);
        date = (EditText)findViewById(R.id.editText_date);

        View.OnClickListener dateClickEvent = new View.OnClickListener() {
            public void onClick(View arg0) {
                Calendar todaysDate = Calendar.getInstance();
                int year = todaysDate.get(Calendar.YEAR);
                int month = todaysDate.get(Calendar.MONTH);
                int day = todaysDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDatePicker = new DatePickerDialog(createEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yearPick, int monthPick, int dayPick) {
                        monthPick += 1;
                        date.setText(monthPick + "/" + dayPick + "/" + yearPick);
                    }
                }, year, month, day);
                myDatePicker.setTitle(R.string.lbl_Pick_Date);
                myDatePicker.show();

            }
        };

        date.setOnClickListener(dateClickEvent);

        View.OnClickListener timeClickEvent = new View.OnClickListener() {
            public void onClick(View arg0) {
                Time currentTime = new Time();
                currentTime.setToNow();
                int hour = currentTime.hour ;
                int minute = currentTime.minute;

                TimePickerDialog myTimePicker = new TimePickerDialog(createEvent.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourPick, int minutePick) {

                        int displayHour = hourPick;
                        String AMPM = "AM";

                        if (hourPick>12) {
                            AMPM = "PM";
                            displayHour -= 12;
                        }
                        time.setText(displayHour + ":" + String.format("%02d",minutePick)  + " " + AMPM);
                    }
                }, hour,minute, false);
                myTimePicker.setTitle(R.string.lbl_Pick_Date);
                myTimePicker.show();

            }
        };

        time.setOnClickListener(timeClickEvent);
    }
}