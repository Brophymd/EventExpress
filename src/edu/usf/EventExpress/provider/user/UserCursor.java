package edu.usf.EventExpress.provider.user;

import java.util.Date;

import android.database.Cursor;

import edu.usf.EventExpress.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code user} table.
 */
public class UserCursor extends AbstractCursor {
    public UserCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code google_id} value.
     */
    public long getGoogleId() {
        return getLongOrNull(UserColumns.GOOGLE_ID);
    }

    /**
     * Get the {@code user_name} value.
     * Cannot be {@code null}.
     */
    public String getUserName() {
        Integer index = getCachedColumnIndexOrThrow(UserColumns.USER_NAME);
        return getString(index);
    }

    /**
     * Get the {@code user_timestamp} value.
     * Cannot be {@code null}.
     */
    public String getUserTimestamp() {
        Integer index = getCachedColumnIndexOrThrow(UserColumns.USER_TIMESTAMP);
        return getString(index);
    }

    /**
     * Get the {@code user_deleted} value.
     */
    public int getUserDeleted() {
        return getIntegerOrNull(UserColumns.USER_DELETED);
    }

    /**
     * Get the {@code user_synced} value.
     */
    public int getUserSynced() {
        return getIntegerOrNull(UserColumns.USER_SYNCED);
    }
}
