package edu.usf.EventExpress.provider.friendstatus;

import java.util.Date;

import android.database.Cursor;

import edu.usf.EventExpress.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code friend_status} table.
 */
public class FriendStatusCursor extends AbstractCursor {
    public FriendStatusCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code from_user_id} value.
     */
    public long getFromUserId() {
        return getLongOrNull(FriendStatusColumns.FROM_USER_ID);
    }

    /**
     * Get the {@code to_user_id} value.
     */
    public long getToUserId() {
        return getLongOrNull(FriendStatusColumns.TO_USER_ID);
    }

    /**
     * Get the {@code status} value.
     * Cannot be {@code null}.
     */
    public FriendStatusType getStatus() {
        Integer intValue = getIntegerOrNull(FriendStatusColumns.STATUS);
        if (intValue == null) return null;
        return FriendStatusType.values()[intValue];
    }

    /**
     * Get the {@code sent_time} value.
     * Can be {@code null}.
     */
    public Date getSentTime() {
        return getDate(FriendStatusColumns.SENT_TIME);
    }

    /**
     * Get the {@code response_time} value.
     * Can be {@code null}.
     */
    public Date getResponseTime() {
        return getDate(FriendStatusColumns.RESPONSE_TIME);
    }

    /**
     * Get the {@code friend_status_timestamp} value.
     * Cannot be {@code null}.
     */
    public Date getFriendStatusTimestamp() {
        return getDate(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP);
    }

    /**
     * Get the {@code friend_status_deleted} value.
     */
    public int getFriendStatusDeleted() {
        return getIntegerOrNull(FriendStatusColumns.FRIEND_STATUS_DELETED);
    }

    /**
     * Get the {@code friend_status_synced} value.
     */
    public int getFriendStatusSynced() {
        return getIntegerOrNull(FriendStatusColumns.FRIEND_STATUS_SYNCED);
    }
}
