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
     * Cannot be {@code null}.
     */
    public String getGoogleId() {
        Integer index = getCachedColumnIndexOrThrow(UserColumns.GOOGLE_ID);
        return getString(index);
    }

    /**
     * Get the {@code user_email} value.
     * Cannot be {@code null}.
     */
    public String getUserEmail() {
        Integer index = getCachedColumnIndexOrThrow(UserColumns.USER_EMAIL);
        return getString(index);
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
    public Date getUserTimestamp() {
        return getDate(UserColumns.USER_TIMESTAMP);
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
