package edu.usf.EventExpress.provider.friendstatus;

import java.util.Date;

import android.database.Cursor;

import edu.usf.EventExpress.provider.base.AbstractCursor;
import edu.usf.EventExpress.provider.user.*;
import edu.usf.EventExpress.provider.user.*;

/**
 * Cursor wrapper for the {@code friend_status} table.
 */
public class FriendStatusCursor extends AbstractCursor {
    public FriendStatusCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code friends_remote_id} value.
     * Can be {@code null}.
     */
    public Long getFriendsRemoteId() {
        return getLongOrNull(FriendStatusColumns.FRIENDS_REMOTE_ID);
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
