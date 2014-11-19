package edu.usf.EventExpress.ViewManager;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import edu.usf.EventExpress.Event_Invitations;
import edu.usf.EventExpress.SessionManager;
import edu.usf.EventExpress.provider.EventProvider;
import edu.usf.EventExpress.provider.EventSQLiteOpenHelper;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.eventmembers.RSVPStatus;

/**
 * Created by Micah on 11/18/2014.
 */
public class EventRsvpStatusFilter
        implements LoaderManager.LoaderCallbacks<Cursor> {


    protected Context mApplicationContext;
    protected int mRsvpStatus;
    protected SimpleCursorAdapter mCursorAdapter;
    protected Uri mContentUri;
    protected String TABLE_NAME;

    public EventRsvpStatusFilter(Context context, String TABLE_NAME, Uri contentUri, SimpleCursorAdapter cursorAdapter, RSVPStatus rsvpStatus) {
        this.mApplicationContext = context;
        this.TABLE_NAME = TABLE_NAME;
        this.mContentUri = contentUri;
        this.mCursorAdapter = cursorAdapter;
        switch (rsvpStatus) {
            case invited:
                this.mRsvpStatus = 0;
                break;
            case uninvited:
                this.mRsvpStatus = 1;
                break;
            case no:
                this.mRsvpStatus = 2;
                break;
            case yes:
                this.mRsvpStatus = 3;
                break;
            case maybe:
                this.mRsvpStatus = 4;
                break;
        }
        // create a view for invited events
        SQLiteDatabase db = EventSQLiteOpenHelper.getInstance(this.mApplicationContext).getWritableDatabase();
        String SQL_CREATE_VIEW_INVITEDEVENTS = "CREATE VIEW IF NOT EXISTS " + this.TABLE_NAME +
                " AS " +
                "SELECT event._id, event.event_title, event.event_date " +
                "FROM event JOIN event_members ON " +
                "(event_members.user_id = '" + new SessionManager(context).getUserID() + "' " +
                "AND event_members.event_id = event._id " +
                "AND event.event_deleted = '0' " +
                "AND event_members.rsvp_status = '" + this.mRsvpStatus + "');";
        db.execSQL(SQL_CREATE_VIEW_INVITEDEVENTS);
    }

    // Implement LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = this.mContentUri;

        return new CursorLoader(this.mApplicationContext, baseUri,
                new String[] {EventColumns._ID, EventColumns.EVENT_TITLE, EventColumns.EVENT_DATE}, null, null,
                EventColumns.EVENT_DATE);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}