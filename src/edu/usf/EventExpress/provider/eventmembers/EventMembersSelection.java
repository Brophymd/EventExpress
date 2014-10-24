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


    public EventMembersSelection eventId(int... value) {
        addEquals(EventMembersColumns.EVENT_ID, toObjectArray(value));
        return this;
    }

    public EventMembersSelection eventIdNot(int... value) {
        addNotEquals(EventMembersColumns.EVENT_ID, toObjectArray(value));
        return this;
    }

    public EventMembersSelection eventIdGt(int value) {
        addGreaterThan(EventMembersColumns.EVENT_ID, value);
        return this;
    }

    public EventMembersSelection eventIdGtEq(int value) {
        addGreaterThanOrEquals(EventMembersColumns.EVENT_ID, value);
        return this;
    }

    public EventMembersSelection eventIdLt(int value) {
        addLessThan(EventMembersColumns.EVENT_ID, value);
        return this;
    }

    public EventMembersSelection eventIdLtEq(int value) {
        addLessThanOrEquals(EventMembersColumns.EVENT_ID, value);
        return this;
    }

    public EventMembersSelection userId(int... value) {
        addEquals(EventMembersColumns.USER_ID, toObjectArray(value));
        return this;
    }

    public EventMembersSelection userIdNot(int... value) {
        addNotEquals(EventMembersColumns.USER_ID, toObjectArray(value));
        return this;
    }

    public EventMembersSelection userIdGt(int value) {
        addGreaterThan(EventMembersColumns.USER_ID, value);
        return this;
    }

    public EventMembersSelection userIdGtEq(int value) {
        addGreaterThanOrEquals(EventMembersColumns.USER_ID, value);
        return this;
    }

    public EventMembersSelection userIdLt(int value) {
        addLessThan(EventMembersColumns.USER_ID, value);
        return this;
    }

    public EventMembersSelection userIdLtEq(int value) {
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

}
