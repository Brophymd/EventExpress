package edu.usf.EventExpress.provider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import edu.usf.EventExpress.BuildConfig;
import edu.usf.EventExpress.Event_Detail_Host;
import edu.usf.EventExpress.Event_Invitations;
import edu.usf.EventExpress.Friendslist;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.eventmembers.EventMembersColumns;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusColumns;
import edu.usf.EventExpress.provider.user.UserColumns;

public class EventProvider extends ContentProvider {
    private static final String TAG = EventProvider.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = "edu.usf.EventExpress.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    public static final String QUERY_NOTIFY = "QUERY_NOTIFY";
    public static final String QUERY_GROUP_BY = "QUERY_GROUP_BY";

    private static final int URI_TYPE_EVENT = 0;
    private static final int URI_TYPE_EVENT_ID = 1;

    private static final int URI_TYPE_EVENT_MEMBERS = 2;
    private static final int URI_TYPE_EVENT_MEMBERS_ID = 3;

    private static final int URI_TYPE_FRIEND_STATUS = 4;
    private static final int URI_TYPE_FRIEND_STATUS_ID = 5;

    private static final int URI_TYPE_USER = 6;
    private static final int URI_TYPE_USER_ID = 7;

    // stuff for views
    private static final int URI_TYPE_ACCEPTEDFRIENDS = 8;
    private static final int URI_TYPE_ACCEPTEDFRIENDS_ID = 9;

    private static final int URI_TYPE_INVITEDEVENTS = 10;
    private static final int URI_TYPE_INVITEDEVENTS_ID = 11;

    private static final int URI_TYPE_ATTENDINGEVENT = 12;
    private static final int URI_TYPE_ATTENDINGEVENT_ID = 13;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, EventColumns.TABLE_NAME, URI_TYPE_EVENT);
        URI_MATCHER.addURI(AUTHORITY, EventColumns.TABLE_NAME + "/#", URI_TYPE_EVENT_ID);
        URI_MATCHER.addURI(AUTHORITY, EventMembersColumns.TABLE_NAME, URI_TYPE_EVENT_MEMBERS);
        URI_MATCHER.addURI(AUTHORITY, EventMembersColumns.TABLE_NAME + "/#", URI_TYPE_EVENT_MEMBERS_ID);
        URI_MATCHER.addURI(AUTHORITY, FriendStatusColumns.TABLE_NAME, URI_TYPE_FRIEND_STATUS);
        URI_MATCHER.addURI(AUTHORITY, FriendStatusColumns.TABLE_NAME + "/#", URI_TYPE_FRIEND_STATUS_ID);
        URI_MATCHER.addURI(AUTHORITY, UserColumns.TABLE_NAME, URI_TYPE_USER);
        URI_MATCHER.addURI(AUTHORITY, UserColumns.TABLE_NAME + "/#", URI_TYPE_USER_ID);

        // add my own view
        URI_MATCHER.addURI(AUTHORITY, Friendslist.TABLE_NAME, URI_TYPE_ACCEPTEDFRIENDS);
        URI_MATCHER.addURI(AUTHORITY, Friendslist.TABLE_NAME + "/#", URI_TYPE_ACCEPTEDFRIENDS_ID);
        URI_MATCHER.addURI(AUTHORITY, Event_Invitations.TABLE_NAME, URI_TYPE_INVITEDEVENTS);
        URI_MATCHER.addURI(AUTHORITY, Event_Invitations.TABLE_NAME + "/#", URI_TYPE_INVITEDEVENTS_ID);
        URI_MATCHER.addURI(AUTHORITY, Event_Detail_Host.TABLE_NAME, URI_TYPE_ATTENDINGEVENT);
        URI_MATCHER.addURI(AUTHORITY, Event_Detail_Host.TABLE_NAME + "/#", URI_TYPE_ATTENDINGEVENT_ID);
    }

    public EventSQLiteOpenHelper mEventSQLiteOpenHelper;

    @Override
    public boolean onCreate() {
        if (DEBUG) {
            // Enable logging of SQL statements as they are executed.
            try {
                Class<?> sqliteDebugClass = Class.forName("android.database.sqlite.SQLiteDebug");
                Field field = sqliteDebugClass.getDeclaredField("DEBUG_SQL_STATEMENTS");
                field.setAccessible(true);
                field.set(null, true);

                // Uncomment the following block if you also want logging of execution time (more verbose)
                // field = sqliteDebugClass.getDeclaredField("DEBUG_SQL_TIME");
                // field.setAccessible(true);
                // field.set(null, true);
            } catch (Throwable t) {
                if (DEBUG) Log.w(TAG, "Could not enable SQLiteDebug logging", t);
            }
        }

        mEventSQLiteOpenHelper = EventSQLiteOpenHelper.getInstance(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_EVENT:
                return TYPE_CURSOR_DIR + EventColumns.TABLE_NAME;
            case URI_TYPE_EVENT_ID:
                return TYPE_CURSOR_ITEM + EventColumns.TABLE_NAME;

            case URI_TYPE_EVENT_MEMBERS:
                return TYPE_CURSOR_DIR + EventMembersColumns.TABLE_NAME;
            case URI_TYPE_EVENT_MEMBERS_ID:
                return TYPE_CURSOR_ITEM + EventMembersColumns.TABLE_NAME;

            case URI_TYPE_FRIEND_STATUS:
                return TYPE_CURSOR_DIR + FriendStatusColumns.TABLE_NAME;
            case URI_TYPE_FRIEND_STATUS_ID:
                return TYPE_CURSOR_ITEM + FriendStatusColumns.TABLE_NAME;

            case URI_TYPE_USER:
                return TYPE_CURSOR_DIR + UserColumns.TABLE_NAME;
            case URI_TYPE_USER_ID:
                return TYPE_CURSOR_ITEM + UserColumns.TABLE_NAME;

            case URI_TYPE_ACCEPTEDFRIENDS:
                return TYPE_CURSOR_DIR + Friendslist.TABLE_NAME;
            case URI_TYPE_ACCEPTEDFRIENDS_ID:
                return TYPE_CURSOR_ITEM + Friendslist.TABLE_NAME;

            case URI_TYPE_INVITEDEVENTS:
                return TYPE_CURSOR_DIR + Event_Invitations.TABLE_NAME;
            case URI_TYPE_INVITEDEVENTS_ID:
                return TYPE_CURSOR_ITEM + Event_Invitations.TABLE_NAME;

            case URI_TYPE_ATTENDINGEVENT:
                return TYPE_CURSOR_DIR + Event_Detail_Host.TABLE_NAME;
            case URI_TYPE_ATTENDINGEVENT_ID:
                return TYPE_CURSOR_ITEM + Event_Detail_Host.TABLE_NAME;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        String table = uri.getLastPathSegment();
        long rowId = mEventSQLiteOpenHelper.getWritableDatabase().insertOrThrow(table, null, values);
        if (rowId == -1) return null;
        String notify;
        if (rowId != -1 && ((notify = uri.getQueryParameter(QUERY_NOTIFY)) == null || "true".equals(notify))) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uri.buildUpon().appendEncodedPath(String.valueOf(rowId)).build();
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        String table = uri.getLastPathSegment();
        SQLiteDatabase db = mEventSQLiteOpenHelper.getWritableDatabase();
        int res = 0;
        db.beginTransaction();
        try {
            for (ContentValues v : values) {
                long id = db.insert(table, null, v);
                db.yieldIfContendedSafely();
                if (id != -1) {
                    res++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        String notify;
        if (res != 0 && ((notify = uri.getQueryParameter(QUERY_NOTIFY)) == null || "true".equals(notify))) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return res;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        QueryParams queryParams = getQueryParams(uri, selection, null);
        int res = mEventSQLiteOpenHelper.getWritableDatabase().update(queryParams.table, values, queryParams.selection, selectionArgs);
        String notify;
        if (res != 0 && ((notify = uri.getQueryParameter(QUERY_NOTIFY)) == null || "true".equals(notify))) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return res;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        QueryParams queryParams = getQueryParams(uri, selection, null);
        int res = mEventSQLiteOpenHelper.getWritableDatabase().delete(queryParams.table, queryParams.selection, selectionArgs);
        String notify;
        if (res != 0 && ((notify = uri.getQueryParameter(QUERY_NOTIFY)) == null || "true".equals(notify))) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return res;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String groupBy = uri.getQueryParameter(QUERY_GROUP_BY);
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + groupBy);
        QueryParams queryParams = getQueryParams(uri, selection, projection);
        Cursor res = mEventSQLiteOpenHelper.getReadableDatabase().query(queryParams.tablesWithJoins, projection, queryParams.selection, selectionArgs, groupBy,
                null, sortOrder == null ? queryParams.orderBy : sortOrder);
        res.setNotificationUri(getContext().getContentResolver(), uri);
        return res;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        HashSet<Uri> urisToNotify = new HashSet<Uri>(operations.size());
        for (ContentProviderOperation operation : operations) {
            urisToNotify.add(operation.getUri());
        }
        SQLiteDatabase db = mEventSQLiteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int numOperations = operations.size();
            ContentProviderResult[] results = new ContentProviderResult[numOperations];
            int i = 0;
            for (ContentProviderOperation operation : operations) {
                results[i] = operation.apply(this, results, i);
                if (operation.isYieldAllowed()) {
                    db.yieldIfContendedSafely();
                }
                i++;
            }
            db.setTransactionSuccessful();
            for (Uri uri : urisToNotify) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return results;
        } finally {
            db.endTransaction();
        }
    }

    private static class QueryParams {
        public String table;
        public String tablesWithJoins;
        public String selection;
        public String orderBy;
    }

    private QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_EVENT:
            case URI_TYPE_EVENT_ID:
                res.table = EventColumns.TABLE_NAME;
                res.tablesWithJoins = EventColumns.TABLE_NAME;
                res.orderBy = EventColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_EVENT_MEMBERS:
            case URI_TYPE_EVENT_MEMBERS_ID:
                res.table = EventMembersColumns.TABLE_NAME;
                res.tablesWithJoins = EventMembersColumns.TABLE_NAME;
                if (EventColumns.hasColumns(projection)) {
                    res.tablesWithJoins += " LEFT OUTER JOIN " + EventColumns.TABLE_NAME + " ON " + EventMembersColumns.TABLE_NAME + "." + EventMembersColumns.EVENT_ID + "=" + EventColumns.TABLE_NAME + "." + EventColumns._ID;
                }
                res.orderBy = EventMembersColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_FRIEND_STATUS:
            case URI_TYPE_FRIEND_STATUS_ID:
                res.table = FriendStatusColumns.TABLE_NAME;
                res.tablesWithJoins = FriendStatusColumns.TABLE_NAME;
                res.orderBy = FriendStatusColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_USER:
            case URI_TYPE_USER_ID:
                res.table = UserColumns.TABLE_NAME;
                res.tablesWithJoins = UserColumns.TABLE_NAME;
                res.orderBy = UserColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_ACCEPTEDFRIENDS:
            case URI_TYPE_ACCEPTEDFRIENDS_ID:
                res.table = Friendslist.TABLE_NAME;
                res.tablesWithJoins = Friendslist.TABLE_NAME;
                res.orderBy = Friendslist.DEFAULT_ORDER;
                break;

            case URI_TYPE_INVITEDEVENTS:
            case URI_TYPE_INVITEDEVENTS_ID:
                res.table = Event_Invitations.TABLE_NAME;
                res.tablesWithJoins = Event_Invitations.TABLE_NAME;
                res.orderBy = Event_Invitations.DEFAULT_ORDER;
                break;

            case URI_TYPE_ATTENDINGEVENT:
            case URI_TYPE_ATTENDINGEVENT_ID:
                res.table = Event_Detail_Host.TABLE_NAME;
                res.tablesWithJoins = Event_Detail_Host.TABLE_NAME;
                res.orderBy = Event_Detail_Host.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_EVENT_ID:
            case URI_TYPE_EVENT_MEMBERS_ID:
            case URI_TYPE_FRIEND_STATUS_ID:
            case URI_TYPE_USER_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = BaseColumns._ID + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = BaseColumns._ID + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }

    public static Uri notify(Uri uri, boolean notify) {
        return uri.buildUpon().appendQueryParameter(QUERY_NOTIFY, String.valueOf(notify)).build();
    }

    public static Uri groupBy(Uri uri, String groupBy) {
        return uri.buildUpon().appendQueryParameter(QUERY_GROUP_BY, groupBy).build();
    }
}
