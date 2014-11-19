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
import edu.usf.EventExpress.provider.EventProvider;
import edu.usf.EventExpress.provider.EventSQLiteOpenHelper;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.eventmembers.RSVPStatus;
import edu.usf.EventExpress.provider.user.UserColumns;

/**
 * Created by Micah on 11/18/2014.
 */
public class UserAttendingEventFilter
        implements LoaderManager.LoaderCallbacks<Cursor> {


    protected Context mApplicationContext;
    protected SimpleCursorAdapter mCursorAdapter;
    protected Uri mContentUri;
    protected int mEventId;

    public UserAttendingEventFilter(Context context, Uri contentUri, SimpleCursorAdapter cursorAdapter, int eventId) {
        this.mApplicationContext = context;
        this.mContentUri = contentUri;
        this.mCursorAdapter = cursorAdapter;
        // create a view for invited events
        SQLiteDatabase db = EventSQLiteOpenHelper.getInstance(this.mApplicationContext).getWritableDatabase();
        String SQL_CREATE_VIEW_ATTENDINGEVENT = "CREATE VIEW IF NOT EXISTS attending_event AS " +
                "SELECT user._id, user.user_name " +
                "FROM user JOIN event_members " +
                "ON event_members.user_id = user.google_id " +
                "WHERE event_members.rsvp_status = '3' AND event_members.event_id = '" + eventId + "';";
        db.execSQL(SQL_CREATE_VIEW_ATTENDINGEVENT);
    }

    // Implement LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = this.mContentUri;

        return new CursorLoader(this.mApplicationContext, baseUri,
                new String[] {UserColumns._ID, UserColumns.USER_NAME}, null, null,
                UserColumns.USER_NAME);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}