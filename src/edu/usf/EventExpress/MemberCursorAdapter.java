package edu.usf.EventExpress;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by Vi Tran on 11/18/2014.
 */
public class MemberCursorAdapter extends SimpleCursorAdapter {

    public MemberCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override

    public void setViewText(TextView v, String text){
        if(v.getId() == R.id.text_Date_row){
            try{
                Date date = new Date(Long.parseLong(text));
                text = date.toString();
            }
            catch(NumberFormatException e){
                text = "";
            }
        }
    }
}
