package edu.usf.EventExpress;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import edu.usf.EventExpress.provider.event.EventColumns;

import java.util.Date;

/**
 * Created by Vi Tran on 11/13/2014.
 */
public class EventCursorAdapter extends CursorAdapter{
    private LayoutInflater mInflater;

    public EventCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

//        if(cursor.getPosition()%2==1) {
//            view.setBackgroundColor(context.getResources().getColor(R.color.background_odd));
//        }
//        else {
//            view.setBackgroundColor(context.getResources().getColor(R.color.background_even));
//        }

        TextView tv_title = (TextView) view.findViewById(R.id.text_Title_row);
        TextView tv_date = (TextView)view.findViewById(R.id.text_Date_row);
        TextView tv_id = (TextView)view.findViewById(R.id.text_ID_row);

        tv_title.setText(cursor.getString(cursor.getColumnIndex(EventColumns.EVENT_TITLE)));

        Long datemillis =  cursor.getLong(cursor.getColumnIndex(EventColumns.EVENT_DATE));
        Date date = new Date(datemillis);
        tv_date.setText(date.toString());

        tv_id.setText(cursor.getString(cursor.getColumnIndex(EventColumns._ID)));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.two_line_list_item, parent, false);
    }

}
