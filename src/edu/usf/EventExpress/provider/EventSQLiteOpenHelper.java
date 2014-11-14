package edu.usf.EventExpress.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import edu.usf.EventExpress.BuildConfig;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.eventmembers.EventMembersColumns;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusColumns;
import edu.usf.EventExpress.provider.user.UserColumns;

public class EventSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = EventSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;
    private static EventSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final EventSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    private static final String SQL_CREATE_TABLE_EVENT = "CREATE TABLE IF NOT EXISTS "
            + EventColumns.TABLE_NAME + " ( "
            + EventColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EventColumns.EVENT_OWNER + " TEXT NOT NULL, "
            + EventColumns.REMOTE_ID + " INTEGER, "
            + EventColumns.EVENT_TYPE + " INTEGER NOT NULL, "
            + EventColumns.EVENT_TITLE + " TEXT, "
            + EventColumns.EVENT_DESCRIPTION + " TEXT, "
            + EventColumns.EVENT_DATE + " INTEGER, "
            + EventColumns.EVENT_ADDRESS + " TEXT, "
            + EventColumns.EVENT_LATITUDE + " REAL, "
            + EventColumns.EVENT_LONGITUDE + " REAL, "
            + EventColumns.EVENT_TIMESTAMP + " INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + EventColumns.EVENT_DELETED + " INTEGER NOT NULL DEFAULT '0', "
            + EventColumns.EVENT_SYNCED + " INTEGER NOT NULL DEFAULT '0' "
            + " );";

    private static final String SQL_CREATE_INDEX_EVENT_EVENT_OWNER = "CREATE INDEX IDX_EVENT_EVENT_OWNER "
            + " ON " + EventColumns.TABLE_NAME + " ( " + EventColumns.EVENT_OWNER + " );";

    private static final String SQL_CREATE_TABLE_EVENT_MEMBERS = "CREATE TABLE IF NOT EXISTS "
            + EventMembersColumns.TABLE_NAME + " ( "
            + EventMembersColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EventMembersColumns.EVENT_ID + " INTEGER NOT NULL, "
            + EventMembersColumns.USER_ID + " INTEGER NOT NULL, "
            + EventMembersColumns.RSVP_STATUS + " INTEGER DEFAULT 'INVITED', "
            + EventMembersColumns.EVENT_MEMBERS_TIMESTAMP + " INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + EventMembersColumns.EVENT_MEMBERS_DELETED + " INTEGER NOT NULL DEFAULT '0', "
            + EventMembersColumns.EVENT_MEMBERS_SYNCED + " INTEGER NOT NULL DEFAULT '0' "
            + ", CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES event (_id) ON DELETE CASCADE"
            + ", CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user (_id) ON DELETE CASCADE"
            + " );";

    private static final String SQL_CREATE_TABLE_FRIEND_STATUS = "CREATE TABLE IF NOT EXISTS "
            + FriendStatusColumns.TABLE_NAME + " ( "
            + FriendStatusColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FriendStatusColumns.FROM_USER_ID + " TEXT NOT NULL, "
            + FriendStatusColumns.TO_USER_ID + " TEXT NOT NULL, "
            + FriendStatusColumns.STATUS + " INTEGER NOT NULL, "
            + FriendStatusColumns.SENT_TIME + " INTEGER DEFAULT CURRENT_TIMESTAMP, "
            + FriendStatusColumns.RESPONSE_TIME + " INTEGER, "
            + FriendStatusColumns.FRIEND_STATUS_TIMESTAMP + " INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + FriendStatusColumns.FRIEND_STATUS_DELETED + " INTEGER NOT NULL DEFAULT '0', "
            + FriendStatusColumns.FRIEND_STATUS_SYNCED + " INTEGER NOT NULL DEFAULT '0' "
            + " );";

    private static final String SQL_CREATE_INDEX_FRIEND_STATUS_FROM_USER_ID = "CREATE INDEX IDX_FRIEND_STATUS_FROM_USER_ID "
            + " ON " + FriendStatusColumns.TABLE_NAME + " ( " + FriendStatusColumns.FROM_USER_ID + " );";

    private static final String SQL_CREATE_INDEX_FRIEND_STATUS_TO_USER_ID = "CREATE INDEX IDX_FRIEND_STATUS_TO_USER_ID "
            + " ON " + FriendStatusColumns.TABLE_NAME + " ( " + FriendStatusColumns.TO_USER_ID + " );";

    private static final String SQL_CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + UserColumns.TABLE_NAME + " ( "
            + UserColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + UserColumns.GOOGLE_ID + " TEXT NOT NULL, "
            + UserColumns.USER_EMAIL + " TEXT NOT NULL, "
            + UserColumns.USER_NAME + " TEXT NOT NULL, "
            + UserColumns.USER_TIMESTAMP + " INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + UserColumns.USER_DELETED + " INTEGER NOT NULL DEFAULT '0', "
            + UserColumns.USER_SYNCED + " INTEGER NOT NULL DEFAULT '0' "
            + ", CONSTRAINT unique_name unique (google_id) on conflict replace"
            + " );";

    private static final String SQL_CREATE_INDEX_USER_GOOGLE_ID = "CREATE INDEX IDX_USER_GOOGLE_ID "
            + " ON " + UserColumns.TABLE_NAME + " ( " + UserColumns.GOOGLE_ID + " );";

    // @formatter:on

    public static EventSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static EventSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */

    private static EventSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new EventSQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    private EventSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        mOpenHelperCallbacks = new EventSQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static EventSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new EventSQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private EventSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new EventSQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_EVENT);
        db.execSQL(SQL_CREATE_INDEX_EVENT_EVENT_OWNER);
        db.execSQL(SQL_CREATE_TABLE_EVENT_MEMBERS);
        db.execSQL(SQL_CREATE_TABLE_FRIEND_STATUS);
        db.execSQL(SQL_CREATE_INDEX_FRIEND_STATUS_FROM_USER_ID);
        db.execSQL(SQL_CREATE_INDEX_FRIEND_STATUS_TO_USER_ID);
        db.execSQL(SQL_CREATE_TABLE_USER);
        db.execSQL(SQL_CREATE_INDEX_USER_GOOGLE_ID);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
