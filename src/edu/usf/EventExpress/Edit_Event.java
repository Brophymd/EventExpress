package edu.usf.EventExpress;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by Vi Tran on 10/26/2014.
 */
public class Edit_Event extends Activity {

    EditText et_title, et_description, et_location, et_date, et_time;
    String title, description, location, time, date;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        et_title = (EditText)findViewById(R.id.editText_titleEdit);
        et_description = (EditText)findViewById(R.id.editText_eventDescriptionEdit);
        et_location = (EditText)findViewById(R.id.editText_locationEdit);
        et_date = (EditText)findViewById(R.id.editText_dateEdit);
        et_time = (EditText)findViewById(R.id.editText_timeEdit);

        Bundle b = getIntent().getExtras();
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
}