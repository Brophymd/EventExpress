package edu.usf.EventExpress.provider.eventmembers;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import edu.usf.EventExpress.provider.base.AbstractSelection;

/**
 * Selection for the {@code event_members} table.
 */
public class EventMembersSelection extends AbstractSelection<EventMembersSelection> {
    @Override
    public Uri uri() {
        return EventMembersColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code EventMembersCursor} object, which is positioned before the first entry, or null.
     */
    public EventMembersCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new EventMembersCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public EventMembersCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public EventMembersCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public EventMembersSelection id(long... value) {
        addEquals(EventMembersColumns._ID, toObjectArray(value));
        return this;
    }


    public EventMembersSelection attendeesRemoteId(Long... value) {
        addEquals(EventMembersColumns.ATTENDEES_REMOTE_ID, value);
        return this;
    }

    public EventMembersSelection attendeesRemoteIdNot(Long... value) {
        addNotEquals(EventMembersColumns.ATTENDEES_REMOTE_ID, value);
        return this;
    }

    public EventMembersSelection attendeesRemoteIdGt(long value) {
        addGreaterThan(EventMembersColumns.ATTENDEES_REMOTE_ID, value);
        return this;
    }

    public EventMembersSelection attendeesRemoteIdGtEq(long value) {
        addGreaterThanOrEquals(EventMembersColumns.ATTENDEES_REMOTE_ID, value);
        return this;
    }

    public EventMembersSelection attendeesRemoteIdLt(long value) {
        addLessThan(EventMembersColumns.ATTENDEES_REMOTE_ID, value);
        return this;
    }

    public EventMembersSelection attendeesRemoteIdLtEq(long value) {
        addLessThanOrEquals(EventMembersColumns.ATTENDEES_REMOTE_ID, value);
        return this;
    }

    public EventMembersSelection eventId(long... value) {
        addEquals(EventMembersColumns.EVENT_ID, toObjectArray(value));
        return this;
    }

    public EventMembersSelection eventIdNot(long... value) {
        addNotEquals(EventMembersColumns.EVENT_ID, toObjectArray(value));
        return this;
    }

    public EventMembersSelection eventIdGt(long value) {
        addGreaterThan(EventMembersColumns.EVENT_ID, value);
        return this;
    }

    public EventMembersSelection eventIdGtEq(long value) {
        addGreaterThanOrEquals(EventMembersColumns.EVENT_ID, value);
        return this;
    }

    public EventMembersSelection eventIdLt(long value) {
        addLessThan(EventMembersColumns.EVENT_ID, value);
        return this;
    }

    public EventMembersSelection eventIdLtEq(long value) {
        addLessThanOrEquals(EventMembersColumns.EVENT_ID, value);
        return this;
    }

    public EventMembersSelection userId(long... value) {
        addEquals(EventMembersColumns.USER_ID, toObjectArray(value));
        return this;
    }

    public EventMembersSelection userIdNot(long... value) {
        addNotEquals(EventMembersColumns.USER_ID, toObjectArray(value));
        return this;
    }

    public EventMembersSelection userIdGt(long value) {
        addGreaterThan(EventMembersColumns.USER_ID, value);
        return this;
    }

    public EventMembersSelection userIdGtEq(long value) {
        addGreaterThanOrEquals(EventMembersColumns.USER_ID, value);
        return this;
    }

    public EventMembersSelection userIdLt(long value) {
        addLessThan(EventMembersColumns.USER_ID, value);
        return this;
    }

    public EventMembersSelection userIdLtEq(long value) {
        addLessThanOrEquals(EventMembersColumns.USER_ID, value);
        return this;
    }

    public EventMembersSelection rsvpStatus(RSVPStatus... value) {
        addEquals(EventMembersColumns.RSVP_STATUS, value);
        return this;
    }

    public EventMembersSelection rsvpStatusNot(RSVPStatus... value) {
        addNotEquals(EventMembersColumns.RSVP_STATUS, value);
        return this;
    }


    public EventMembersSelection eventMembersTimestamp(Date... value) {
        addEquals(EventMembersColumns.EVENT_MEMBERS_TIMESTAMP, value);
        return this;
    }

    public EventMembersSelection eventMembersTimestampNot(Date... value) {
        addNotEquals(EventMembersColumns.EVENT_MEMBERS_TIMESTAMP, value);
        return this;
    }

    public EventMembersSelection eventMembersTimestamp(long... value) {
        addEquals(EventMembersColumns.EVENT_MEMBERS_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public EventMembersSelection eventMembersTimestampAfter(Date value) {
        addGreaterThan(EventMembersColumns.EVENT_MEMBERS_TIMESTAMP, value);
        return this;
    }

    public EventMembersSelection eventMembersTimestampAfterEq(Date value) {
        addGreaterThanOrEquals(EventMembersColumns.EVENT_MEMBERS_TIMESTAMP, value);
        return this;
    }

    public EventMembersSelection eventMembersTimestampBefore(Date value) {
        addLessThan(EventMembersColumns.EVENT_MEMBERS_TIMESTAMP, value);
        return this;
    }

    public EventMembersSelection eventMembersTimestampBeforeEq(Date value) {
        addLessThanOrEquals(EventMembersColumns.EVENT_MEMBERS_TIMESTAMP, value);
        return this;
    }

    public EventMembersSelection eventMembersDeleted(int... value) {
        addEquals(EventMembersColumns.EVENT_MEMBERS_DELETED, toObjectArray(value));
        return this;
    }

    public EventMembersSelection eventMembersDeletedNot(int... value) {
        addNotEquals(EventMembersColumns.EVENT_MEMBERS_DELETED, toObjectArray(value));
        return this;
    }

    public EventMembersSelection eventMembersDeletedGt(int value) {
        addGreaterThan(EventMembersColumns.EVENT_MEMBERS_DELETED, value);
        return this;
    }

    public EventMembersSelection eventMembersDeletedGtEq(int value) {
        addGreaterThanOrEquals(EventMembersColumns.EVENT_MEMBERS_DELETED, value);
        return this;
    }

    public EventMembersSelection eventMembersDeletedLt(int value) {
        addLessThan(EventMembersColumns.EVENT_MEMBERS_DELETED, value);
        return this;
    }

    public EventMembersSelection eventMembersDeletedLtEq(int value) {
        addLessThanOrEquals(EventMembersColumns.EVENT_MEMBERS_DELETED, value);
        return this;
    }

    public EventMembersSelection eventMembersSynced(int... value) {
        addEquals(EventMembersColumns.EVENT_MEMBERS_SYNCED, toObjectArray(value));
        return this;
    }

    public EventMembersSelection eventMembersSyncedNot(int... value) {
        addNotEquals(EventMembersColumns.EVENT_MEMBERS_SYNCED, toObjectArray(value));
        return this;
    }

    public EventMembersSelection eventMembersSyncedGt(int value) {
        addGreaterThan(EventMembersColumns.EVENT_MEMBERS_SYNCED, value);
        return this;
    }

    public EventMembersSelection eventMembersSyncedGtEq(int value) {
        addGreaterThanOrEquals(EventMembersColumns.EVENT_MEMBERS_SYNCED, value);
        return this;
    }

    public EventMembersSelection eventMembersSyncedLt(int value) {
        addLessThan(EventMembersColumns.EVENT_MEMBERS_SYNCED, value);
        return this;
    }

    public EventMembersSelection eventMembersSyncedLtEq(int value) {
        addLessThanOrEquals(EventMembersColumns.EVENT_MEMBERS_SYNCED, value);
        return this;
    }
}
