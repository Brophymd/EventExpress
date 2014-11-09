package edu.usf.EventExpress.provider.eventmembers;

import java.util.HashSet;
import java.util.Set;

import android.net.Uri;
import android.provider.BaseColumns;

import edu.usf.EventExpress.provider.EventProvider;

/**
 * Columns for the {@code event_members} table.
 */
public class EventMembersColumns implements BaseColumns {
    public static final String TABLE_NAME = "event_members";
    public static final Uri CONTENT_URI = Uri.parse(EventProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    public static final String _ID = BaseColumns._ID;
    public static final String EVENT_ID = "event_id";
    public static final String USER_ID = "user_id";
    public static final String RSVP_STATUS = "rsvp_status";
    public static final String EVENT_MEMBERS_TIMESTAMP = "event_members_timestamp";
    public static final String EVENT_MEMBERS_DELETED = "event_members_deleted";
    public static final String EVENT_MEMBERS_SYNCED = "event_members_synced";

    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] FULL_PROJECTION = new String[] {
            TABLE_NAME + "." + _ID + " AS " + BaseColumns._ID,
            TABLE_NAME + "." + EVENT_ID,
            TABLE_NAME + "." + USER_ID,
            TABLE_NAME + "." + RSVP_STATUS,
            TABLE_NAME + "." + EVENT_MEMBERS_TIMESTAMP,
            TABLE_NAME + "." + EVENT_MEMBERS_DELETED,
            TABLE_NAME + "." + EVENT_MEMBERS_SYNCED
    };
    // @formatter:on

    private static final Set<String> ALL_COLUMNS = new HashSet<String>();
    static {
        ALL_COLUMNS.add(_ID);
        ALL_COLUMNS.add(EVENT_ID);
        ALL_COLUMNS.add(USER_ID);
        ALL_COLUMNS.add(RSVP_STATUS);
        ALL_COLUMNS.add(EVENT_MEMBERS_TIMESTAMP);
        ALL_COLUMNS.add(EVENT_MEMBERS_DELETED);
        ALL_COLUMNS.add(EVENT_MEMBERS_SYNCED);
    }

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (ALL_COLUMNS.contains(c)) return true;
        }
        return false;
    }
}
