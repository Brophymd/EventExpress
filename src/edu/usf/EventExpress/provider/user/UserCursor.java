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
     * Get the {@code name} value.
     * Cannot be {@code null}.
     */
    public String getName() {
        Integer index = getCachedColumnIndexOrThrow(UserColumns.NAME);
        return getString(index);
    }

    /**
     * Get the {@code timestamp} value.
     * Cannot be {@code null}.
     */
    public String getTimestamp() {
        Integer index = getCachedColumnIndexOrThrow(UserColumns.TIMESTAMP);
        return getString(index);
    }

    /**
     * Get the {@code deleted} value.
     */
    public int getDeleted() {
        return getIntegerOrNull(UserColumns.DELETED);
    }

    /**
     * Get the {@code synced} value.
     */
    public int getSynced() {
        return getIntegerOrNull(UserColumns.SYNCED);
    }
}
