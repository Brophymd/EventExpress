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
    public static final String USER_NAME = "user_name";

    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] FULL_PROJECTION = new String[] {
            TABLE_NAME + "." + _ID + " AS " + BaseColumns._ID,
            TABLE_NAME + "." + GOOGLE_ID,
            TABLE_NAME + "." + USER_NAME
    };
    // @formatter:on

    private static final Set<String> ALL_COLUMNS = new HashSet<String>();
    static {
        ALL_COLUMNS.add(_ID);
        ALL_COLUMNS.add(GOOGLE_ID);
        ALL_COLUMNS.add(USER_NAME);
    }

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (ALL_COLUMNS.contains(c)) return true;
        }
        return false;
    }
}
