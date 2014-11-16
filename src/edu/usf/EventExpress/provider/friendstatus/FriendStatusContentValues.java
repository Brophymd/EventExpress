package edu.usf.EventExpress.provider.friendstatus;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import edu.usf.EventExpress.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code friend_status} table.
 */
public class FriendStatusContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return FriendStatusColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, FriendStatusSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public FriendStatusContentValues putFriendsRemoteId(Long value) {
        mContentValues.put(FriendStatusColumns.FRIENDS_REMOTE_ID, value);
        return this;
    }

    public FriendStatusContentValues putFriendsRemoteIdNull() {
        mContentValues.putNull(FriendStatusColumns.FRIENDS_REMOTE_ID);
        return this;
    }


    public FriendStatusContentValues putFromUserId(String value) {
        if (value == null) throw new IllegalArgumentException("value for fromUserId must not be null");
        mContentValues.put(FriendStatusColumns.FROM_USER_ID, value);
        return this;
    }



    public FriendStatusContentValues putToUserId(String value) {
        if (value == null) throw new IllegalArgumentException("value for toUserId must not be null");
        mContentValues.put(FriendStatusColumns.TO_USER_ID, value);
        return this;
    }



    public FriendStatusContentValues putStatus(FriendStatusType value) {
        if (value == null) throw new IllegalArgumentException("value for status must not be null");
        mContentValues.put(FriendStatusColumns.STATUS, value.ordinal());
        return this;
    }



    public FriendStatusContentValues putSentTime(Date value) {
        mContentValues.put(FriendStatusColumns.SENT_TIME, value == null ? null : value.getTime());
        return this;
    }

    public FriendStatusContentValues putSentTimeNull() {
        mContentValues.putNull(FriendStatusColumns.SENT_TIME);
        return this;
    }

    public FriendStatusContentValues putSentTime(Long value) {
        mContentValues.put(FriendStatusColumns.SENT_TIME, value);
        return this;
    }


    public FriendStatusContentValues putResponseTime(Date value) {
        mContentValues.put(FriendStatusColumns.RESPONSE_TIME, value == null ? null : value.getTime());
        return this;
    }

    public FriendStatusContentValues putResponseTimeNull() {
        mContentValues.putNull(FriendStatusColumns.RESPONSE_TIME);
        return this;
    }

    public FriendStatusContentValues putResponseTime(Long value) {
        mContentValues.put(FriendStatusColumns.RESPONSE_TIME, value);
        return this;
    }


    public FriendStatusContentValues putFriendStatusTimestamp(Date value) {
        if (value == null) throw new IllegalArgumentException("value for friendStatusTimestamp must not be null");
        mContentValues.put(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP, value.getTime());
        return this;
    }


    public FriendStatusContentValues putFriendStatusTimestamp(long value) {
        mContentValues.put(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP, value);
        return this;
    }


    public FriendStatusContentValues putFriendStatusDeleted(int value) {
        mContentValues.put(FriendStatusColumns.FRIEND_STATUS_DELETED, value);
        return this;
    }



    public FriendStatusContentValues putFriendStatusSynced(int value) {
        mContentValues.put(FriendStatusColumns.FRIEND_STATUS_SYNCED, value);
        return this;
    }


}
