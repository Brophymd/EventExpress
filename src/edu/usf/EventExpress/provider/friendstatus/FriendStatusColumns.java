package edu.usf.EventExpress.provider.friendstatus;

import java.util.HashSet;
import java.util.Set;

import android.net.Uri;
import android.provider.BaseColumns;

import edu.usf.EventExpress.provider.EventProvider;

/**
 * Columns for the {@code friend_status} table.
 */
public class FriendStatusColumns implements BaseColumns {
    public static final String TABLE_NAME = "friend_status";
    public static final Uri CONTENT_URI = Uri.parse(EventProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    public static final String _ID = BaseColumns._ID;
    public static final String FRIENDS_REMOTE_ID = "friends_remote_id";
    public static final String FROM_USER_ID = "from_user_id";
    public static final String TO_USER_ID = "to_user_id";
    public static final String FROM_USER_EMAIL = "from_user_email";
    public static final String TO_USER_EMAIL = "to_user_email";
    public static final String STATUS = "status";
    public static final String SENT_TIME = "sent_time";
    public static final String RESPONSE_TIME = "response_time";
    public static final String FRIEND_STATUS_TIMESTAMP = "friend_status_timestamp";
    public static final String FRIEND_STATUS_DELETED = "friend_status_deleted";
    public static final String FRIEND_STATUS_SYNCED = "friend_status_synced";

    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] FULL_PROJECTION = new String[] {
            TABLE_NAME + "." + _ID + " AS " + BaseColumns._ID,
            TABLE_NAME + "." + FRIENDS_REMOTE_ID,
            TABLE_NAME + "." + FROM_USER_ID,
            TABLE_NAME + "." + TO_USER_ID,
            TABLE_NAME + "." + FROM_USER_EMAIL,
            TABLE_NAME + "." + TO_USER_EMAIL,
            TABLE_NAME + "." + STATUS,
            TABLE_NAME + "." + SENT_TIME,
            TABLE_NAME + "." + RESPONSE_TIME,
            TABLE_NAME + "." + FRIEND_STATUS_TIMESTAMP,
            TABLE_NAME + "." + FRIEND_STATUS_DELETED,
            TABLE_NAME + "." + FRIEND_STATUS_SYNCED
    };
    // @formatter:on

    private static final Set<String> ALL_COLUMNS = new HashSet<String>();
    static {
        ALL_COLUMNS.add(_ID);
        ALL_COLUMNS.add(FRIENDS_REMOTE_ID);
        ALL_COLUMNS.add(FROM_USER_ID);
        ALL_COLUMNS.add(TO_USER_ID);
        ALL_COLUMNS.add(FROM_USER_EMAIL);
        ALL_COLUMNS.add(TO_USER_EMAIL);
        ALL_COLUMNS.add(STATUS);
        ALL_COLUMNS.add(SENT_TIME);
        ALL_COLUMNS.add(RESPONSE_TIME);
        ALL_COLUMNS.add(FRIEND_STATUS_TIMESTAMP);
        ALL_COLUMNS.add(FRIEND_STATUS_DELETED);
        ALL_COLUMNS.add(FRIEND_STATUS_SYNCED);
    }

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (ALL_COLUMNS.contains(c)) return true;
        }
        return false;
    }
}
