package edu.usf.EventExpress.provider.event;

import java.util.HashSet;
import java.util.Set;

import android.net.Uri;
import android.provider.BaseColumns;

import edu.usf.EventExpress.provider.EventProvider;

/**
 * Columns for the {@code event} table.
 */
public class EventColumns implements BaseColumns {
    public static final String TABLE_NAME = "event";
    public static final Uri CONTENT_URI = Uri.parse(EventProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    public static final String _ID = BaseColumns._ID;
    public static final String EVENT_OWNER = "event_owner";
    public static final String REMOTE_ID = "remote_id";
    public static final String EVENT_TYPE = "event_type";
    public static final String EVENT_TITLE = "event_title";
    public static final String EVENT_DESCRIPTION = "event_description";
    public static final String EVENT_DATE = "event_date";
    public static final String EVENT_ADDRESS = "event_address";
    public static final String EVENT_LATITUDE = "event_latitude";
    public static final String EVENT_LONGITUDE = "event_longitude";
    public static final String EVENT_TIMESTAMP = "event_timestamp";
    public static final String EVENT_DELETED = "event_deleted";
    public static final String EVENT_SYNCED = "event_synced";

    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] FULL_PROJECTION = new String[] {
            TABLE_NAME + "." + _ID + " AS " + BaseColumns._ID,
            TABLE_NAME + "." + EVENT_OWNER,
            TABLE_NAME + "." + REMOTE_ID,
            TABLE_NAME + "." + EVENT_TYPE,
            TABLE_NAME + "." + EVENT_TITLE,
            TABLE_NAME + "." + EVENT_DESCRIPTION,
            TABLE_NAME + "." + EVENT_DATE,
            TABLE_NAME + "." + EVENT_ADDRESS,
            TABLE_NAME + "." + EVENT_LATITUDE,
            TABLE_NAME + "." + EVENT_LONGITUDE,
            TABLE_NAME + "." + EVENT_TIMESTAMP,
            TABLE_NAME + "." + EVENT_DELETED,
            TABLE_NAME + "." + EVENT_SYNCED
    };
    // @formatter:on

    private static final Set<String> ALL_COLUMNS = new HashSet<String>();
    static {
        ALL_COLUMNS.add(_ID);
        ALL_COLUMNS.add(EVENT_OWNER);
        ALL_COLUMNS.add(REMOTE_ID);
        ALL_COLUMNS.add(EVENT_TYPE);
        ALL_COLUMNS.add(EVENT_TITLE);
        ALL_COLUMNS.add(EVENT_DESCRIPTION);
        ALL_COLUMNS.add(EVENT_DATE);
        ALL_COLUMNS.add(EVENT_ADDRESS);
        ALL_COLUMNS.add(EVENT_LATITUDE);
        ALL_COLUMNS.add(EVENT_LONGITUDE);
        ALL_COLUMNS.add(EVENT_TIMESTAMP);
        ALL_COLUMNS.add(EVENT_DELETED);
        ALL_COLUMNS.add(EVENT_SYNCED);
    }

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (ALL_COLUMNS.contains(c)) return true;
        }
        return false;
    }
}
