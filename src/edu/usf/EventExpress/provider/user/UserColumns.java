package edu.usf.EventExpress.provider.user;

import java.util.HashSet;
import java.util.Set;

import android.net.Uri;
import android.provider.BaseColumns;

import edu.usf.EventExpress.provider.EventProvider;

/**
 * Columns for the {@code user} table.
 */
public class UserColumns implements BaseColumns {
    public static final String TABLE_NAME = "user";
    public static final Uri CONTENT_URI = Uri.parse(EventProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    public static final String _ID = BaseColumns._ID;
    public static final String GOOGLE_ID = "google_id";
    public static final String NAME = "name";
    public static final String USER_TIMESTAMP = "user_timestamp";
    public static final String USER_DELETED = "user_deleted";
    public static final String USER_SYNCED = "user_synced";

    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] FULL_PROJECTION = new String[] {
            TABLE_NAME + "." + _ID + " AS " + BaseColumns._ID,
            TABLE_NAME + "." + GOOGLE_ID,
            TABLE_NAME + "." + NAME,
            TABLE_NAME + "." + USER_TIMESTAMP,
            TABLE_NAME + "." + USER_DELETED,
            TABLE_NAME + "." + USER_SYNCED
    };
    // @formatter:on

    private static final Set<String> ALL_COLUMNS = new HashSet<String>();
    static {
        ALL_COLUMNS.add(_ID);
        ALL_COLUMNS.add(GOOGLE_ID);
        ALL_COLUMNS.add(NAME);
        ALL_COLUMNS.add(USER_TIMESTAMP);
        ALL_COLUMNS.add(USER_DELETED);
        ALL_COLUMNS.add(USER_SYNCED);
    }

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (ALL_COLUMNS.contains(c)) return true;
        }
        return false;
    }
}
