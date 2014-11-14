package edu.usf.EventExpress.provider.friendstatus;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import edu.usf.EventExpress.provider.base.AbstractSelection;

/**
 * Selection for the {@code friend_status} table.
 */
public class FriendStatusSelection extends AbstractSelection<FriendStatusSelection> {
    @Override
    public Uri uri() {
        return FriendStatusColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code FriendStatusCursor} object, which is positioned before the first entry, or null.
     */
    public FriendStatusCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new FriendStatusCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public FriendStatusCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public FriendStatusCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public FriendStatusSelection id(long... value) {
        addEquals(FriendStatusColumns._ID, toObjectArray(value));
        return this;
    }


    public FriendStatusSelection fromUserId(String... value) {
        addEquals(FriendStatusColumns.FROM_USER_ID, value);
        return this;
    }

    public FriendStatusSelection fromUserIdNot(String... value) {
        addNotEquals(FriendStatusColumns.FROM_USER_ID, value);
        return this;
    }

    public FriendStatusSelection fromUserIdLike(String... value) {
        addLike(FriendStatusColumns.FROM_USER_ID, value);
        return this;
    }

    public FriendStatusSelection toUserId(String... value) {
        addEquals(FriendStatusColumns.TO_USER_ID, value);
        return this;
    }

    public FriendStatusSelection toUserIdNot(String... value) {
        addNotEquals(FriendStatusColumns.TO_USER_ID, value);
        return this;
    }

    public FriendStatusSelection toUserIdLike(String... value) {
        addLike(FriendStatusColumns.TO_USER_ID, value);
        return this;
    }

    public FriendStatusSelection status(FriendStatusType... value) {
        addEquals(FriendStatusColumns.STATUS, value);
        return this;
    }

    public FriendStatusSelection statusNot(FriendStatusType... value) {
        addNotEquals(FriendStatusColumns.STATUS, value);
        return this;
    }


    public FriendStatusSelection sentTime(Date... value) {
        addEquals(FriendStatusColumns.SENT_TIME, value);
        return this;
    }

    public FriendStatusSelection sentTimeNot(Date... value) {
        addNotEquals(FriendStatusColumns.SENT_TIME, value);
        return this;
    }

    public FriendStatusSelection sentTime(Long... value) {
        addEquals(FriendStatusColumns.SENT_TIME, value);
        return this;
    }

    public FriendStatusSelection sentTimeAfter(Date value) {
        addGreaterThan(FriendStatusColumns.SENT_TIME, value);
        return this;
    }

    public FriendStatusSelection sentTimeAfterEq(Date value) {
        addGreaterThanOrEquals(FriendStatusColumns.SENT_TIME, value);
        return this;
    }

    public FriendStatusSelection sentTimeBefore(Date value) {
        addLessThan(FriendStatusColumns.SENT_TIME, value);
        return this;
    }

    public FriendStatusSelection sentTimeBeforeEq(Date value) {
        addLessThanOrEquals(FriendStatusColumns.SENT_TIME, value);
        return this;
    }

    public FriendStatusSelection responseTime(Date... value) {
        addEquals(FriendStatusColumns.RESPONSE_TIME, value);
        return this;
    }

    public FriendStatusSelection responseTimeNot(Date... value) {
        addNotEquals(FriendStatusColumns.RESPONSE_TIME, value);
        return this;
    }

    public FriendStatusSelection responseTime(Long... value) {
        addEquals(FriendStatusColumns.RESPONSE_TIME, value);
        return this;
    }

    public FriendStatusSelection responseTimeAfter(Date value) {
        addGreaterThan(FriendStatusColumns.RESPONSE_TIME, value);
        return this;
    }

    public FriendStatusSelection responseTimeAfterEq(Date value) {
        addGreaterThanOrEquals(FriendStatusColumns.RESPONSE_TIME, value);
        return this;
    }

    public FriendStatusSelection responseTimeBefore(Date value) {
        addLessThan(FriendStatusColumns.RESPONSE_TIME, value);
        return this;
    }

    public FriendStatusSelection responseTimeBeforeEq(Date value) {
        addLessThanOrEquals(FriendStatusColumns.RESPONSE_TIME, value);
        return this;
    }

    public FriendStatusSelection friendStatusTimestamp(Date... value) {
        addEquals(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP, value);
        return this;
    }

    public FriendStatusSelection friendStatusTimestampNot(Date... value) {
        addNotEquals(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP, value);
        return this;
    }

    public FriendStatusSelection friendStatusTimestamp(long... value) {
        addEquals(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public FriendStatusSelection friendStatusTimestampAfter(Date value) {
        addGreaterThan(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP, value);
        return this;
    }

    public FriendStatusSelection friendStatusTimestampAfterEq(Date value) {
        addGreaterThanOrEquals(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP, value);
        return this;
    }

    public FriendStatusSelection friendStatusTimestampBefore(Date value) {
        addLessThan(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP, value);
        return this;
    }

    public FriendStatusSelection friendStatusTimestampBeforeEq(Date value) {
        addLessThanOrEquals(FriendStatusColumns.FRIEND_STATUS_TIMESTAMP, value);
        return this;
    }

    public FriendStatusSelection friendStatusDeleted(int... value) {
        addEquals(FriendStatusColumns.FRIEND_STATUS_DELETED, toObjectArray(value));
        return this;
    }

    public FriendStatusSelection friendStatusDeletedNot(int... value) {
        addNotEquals(FriendStatusColumns.FRIEND_STATUS_DELETED, toObjectArray(value));
        return this;
    }

    public FriendStatusSelection friendStatusDeletedGt(int value) {
        addGreaterThan(FriendStatusColumns.FRIEND_STATUS_DELETED, value);
        return this;
    }

    public FriendStatusSelection friendStatusDeletedGtEq(int value) {
        addGreaterThanOrEquals(FriendStatusColumns.FRIEND_STATUS_DELETED, value);
        return this;
    }

    public FriendStatusSelection friendStatusDeletedLt(int value) {
        addLessThan(FriendStatusColumns.FRIEND_STATUS_DELETED, value);
        return this;
    }

    public FriendStatusSelection friendStatusDeletedLtEq(int value) {
        addLessThanOrEquals(FriendStatusColumns.FRIEND_STATUS_DELETED, value);
        return this;
    }

    public FriendStatusSelection friendStatusSynced(int... value) {
        addEquals(FriendStatusColumns.FRIEND_STATUS_SYNCED, toObjectArray(value));
        return this;
    }

    public FriendStatusSelection friendStatusSyncedNot(int... value) {
        addNotEquals(FriendStatusColumns.FRIEND_STATUS_SYNCED, toObjectArray(value));
        return this;
    }

    public FriendStatusSelection friendStatusSyncedGt(int value) {
        addGreaterThan(FriendStatusColumns.FRIEND_STATUS_SYNCED, value);
        return this;
    }

    public FriendStatusSelection friendStatusSyncedGtEq(int value) {
        addGreaterThanOrEquals(FriendStatusColumns.FRIEND_STATUS_SYNCED, value);
        return this;
    }

    public FriendStatusSelection friendStatusSyncedLt(int value) {
        addLessThan(FriendStatusColumns.FRIEND_STATUS_SYNCED, value);
        return this;
    }

    public FriendStatusSelection friendStatusSyncedLtEq(int value) {
        addLessThanOrEquals(FriendStatusColumns.FRIEND_STATUS_SYNCED, value);
        return this;
    }
}
