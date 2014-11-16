package edu.usf.EventExpress.provider.event;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import edu.usf.EventExpress.provider.base.AbstractSelection;

/**
 * Selection for the {@code event} table.
 */
public class EventSelection extends AbstractSelection<EventSelection> {
    @Override
    public Uri uri() {
        return EventColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code EventCursor} object, which is positioned before the first entry, or null.
     */
    public EventCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new EventCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public EventCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public EventCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public EventSelection id(long... value) {
        addEquals(EventColumns._ID, toObjectArray(value));
        return this;
    }


    public EventSelection eventOwner(String... value) {
        addEquals(EventColumns.EVENT_OWNER, value);
        return this;
    }

    public EventSelection eventOwnerNot(String... value) {
        addNotEquals(EventColumns.EVENT_OWNER, value);
        return this;
    }

    public EventSelection eventOwnerLike(String... value) {
        addLike(EventColumns.EVENT_OWNER, value);
        return this;
    }

    public EventSelection eventRemoteId(Long... value) {
        addEquals(EventColumns.EVENT_REMOTE_ID, value);
        return this;
    }

    public EventSelection eventRemoteIdNot(Long... value) {
        addNotEquals(EventColumns.EVENT_REMOTE_ID, value);
        return this;
    }

    public EventSelection eventRemoteIdGt(long value) {
        addGreaterThan(EventColumns.EVENT_REMOTE_ID, value);
        return this;
    }

    public EventSelection eventRemoteIdGtEq(long value) {
        addGreaterThanOrEquals(EventColumns.EVENT_REMOTE_ID, value);
        return this;
    }

    public EventSelection eventRemoteIdLt(long value) {
        addLessThan(EventColumns.EVENT_REMOTE_ID, value);
        return this;
    }

    public EventSelection eventRemoteIdLtEq(long value) {
        addLessThanOrEquals(EventColumns.EVENT_REMOTE_ID, value);
        return this;
    }

    public EventSelection eventType(EventType... value) {
        addEquals(EventColumns.EVENT_TYPE, value);
        return this;
    }

    public EventSelection eventTypeNot(EventType... value) {
        addNotEquals(EventColumns.EVENT_TYPE, value);
        return this;
    }


    public EventSelection eventTitle(String... value) {
        addEquals(EventColumns.EVENT_TITLE, value);
        return this;
    }

    public EventSelection eventTitleNot(String... value) {
        addNotEquals(EventColumns.EVENT_TITLE, value);
        return this;
    }

    public EventSelection eventTitleLike(String... value) {
        addLike(EventColumns.EVENT_TITLE, value);
        return this;
    }

    public EventSelection eventDescription(String... value) {
        addEquals(EventColumns.EVENT_DESCRIPTION, value);
        return this;
    }

    public EventSelection eventDescriptionNot(String... value) {
        addNotEquals(EventColumns.EVENT_DESCRIPTION, value);
        return this;
    }

    public EventSelection eventDescriptionLike(String... value) {
        addLike(EventColumns.EVENT_DESCRIPTION, value);
        return this;
    }

    public EventSelection eventDate(Date... value) {
        addEquals(EventColumns.EVENT_DATE, value);
        return this;
    }

    public EventSelection eventDateNot(Date... value) {
        addNotEquals(EventColumns.EVENT_DATE, value);
        return this;
    }

    public EventSelection eventDate(Long... value) {
        addEquals(EventColumns.EVENT_DATE, value);
        return this;
    }

    public EventSelection eventDateAfter(Date value) {
        addGreaterThan(EventColumns.EVENT_DATE, value);
        return this;
    }

    public EventSelection eventDateAfterEq(Date value) {
        addGreaterThanOrEquals(EventColumns.EVENT_DATE, value);
        return this;
    }

    public EventSelection eventDateBefore(Date value) {
        addLessThan(EventColumns.EVENT_DATE, value);
        return this;
    }

    public EventSelection eventDateBeforeEq(Date value) {
        addLessThanOrEquals(EventColumns.EVENT_DATE, value);
        return this;
    }

    public EventSelection eventAddress(String... value) {
        addEquals(EventColumns.EVENT_ADDRESS, value);
        return this;
    }

    public EventSelection eventAddressNot(String... value) {
        addNotEquals(EventColumns.EVENT_ADDRESS, value);
        return this;
    }

    public EventSelection eventAddressLike(String... value) {
        addLike(EventColumns.EVENT_ADDRESS, value);
        return this;
    }

    public EventSelection eventLatitude(Float... value) {
        addEquals(EventColumns.EVENT_LATITUDE, value);
        return this;
    }

    public EventSelection eventLatitudeNot(Float... value) {
        addNotEquals(EventColumns.EVENT_LATITUDE, value);
        return this;
    }

    public EventSelection eventLatitudeGt(float value) {
        addGreaterThan(EventColumns.EVENT_LATITUDE, value);
        return this;
    }

    public EventSelection eventLatitudeGtEq(float value) {
        addGreaterThanOrEquals(EventColumns.EVENT_LATITUDE, value);
        return this;
    }

    public EventSelection eventLatitudeLt(float value) {
        addLessThan(EventColumns.EVENT_LATITUDE, value);
        return this;
    }

    public EventSelection eventLatitudeLtEq(float value) {
        addLessThanOrEquals(EventColumns.EVENT_LATITUDE, value);
        return this;
    }

    public EventSelection eventLongitude(Float... value) {
        addEquals(EventColumns.EVENT_LONGITUDE, value);
        return this;
    }

    public EventSelection eventLongitudeNot(Float... value) {
        addNotEquals(EventColumns.EVENT_LONGITUDE, value);
        return this;
    }

    public EventSelection eventLongitudeGt(float value) {
        addGreaterThan(EventColumns.EVENT_LONGITUDE, value);
        return this;
    }

    public EventSelection eventLongitudeGtEq(float value) {
        addGreaterThanOrEquals(EventColumns.EVENT_LONGITUDE, value);
        return this;
    }

    public EventSelection eventLongitudeLt(float value) {
        addLessThan(EventColumns.EVENT_LONGITUDE, value);
        return this;
    }

    public EventSelection eventLongitudeLtEq(float value) {
        addLessThanOrEquals(EventColumns.EVENT_LONGITUDE, value);
        return this;
    }

    public EventSelection eventTimestamp(Date... value) {
        addEquals(EventColumns.EVENT_TIMESTAMP, value);
        return this;
    }

    public EventSelection eventTimestampNot(Date... value) {
        addNotEquals(EventColumns.EVENT_TIMESTAMP, value);
        return this;
    }

    public EventSelection eventTimestamp(long... value) {
        addEquals(EventColumns.EVENT_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public EventSelection eventTimestampAfter(Date value) {
        addGreaterThan(EventColumns.EVENT_TIMESTAMP, value);
        return this;
    }

    public EventSelection eventTimestampAfterEq(Date value) {
        addGreaterThanOrEquals(EventColumns.EVENT_TIMESTAMP, value);
        return this;
    }

    public EventSelection eventTimestampBefore(Date value) {
        addLessThan(EventColumns.EVENT_TIMESTAMP, value);
        return this;
    }

    public EventSelection eventTimestampBeforeEq(Date value) {
        addLessThanOrEquals(EventColumns.EVENT_TIMESTAMP, value);
        return this;
    }

    public EventSelection eventDeleted(int... value) {
        addEquals(EventColumns.EVENT_DELETED, toObjectArray(value));
        return this;
    }

    public EventSelection eventDeletedNot(int... value) {
        addNotEquals(EventColumns.EVENT_DELETED, toObjectArray(value));
        return this;
    }

    public EventSelection eventDeletedGt(int value) {
        addGreaterThan(EventColumns.EVENT_DELETED, value);
        return this;
    }

    public EventSelection eventDeletedGtEq(int value) {
        addGreaterThanOrEquals(EventColumns.EVENT_DELETED, value);
        return this;
    }

    public EventSelection eventDeletedLt(int value) {
        addLessThan(EventColumns.EVENT_DELETED, value);
        return this;
    }

    public EventSelection eventDeletedLtEq(int value) {
        addLessThanOrEquals(EventColumns.EVENT_DELETED, value);
        return this;
    }

    public EventSelection eventSynced(int... value) {
        addEquals(EventColumns.EVENT_SYNCED, toObjectArray(value));
        return this;
    }

    public EventSelection eventSyncedNot(int... value) {
        addNotEquals(EventColumns.EVENT_SYNCED, toObjectArray(value));
        return this;
    }

    public EventSelection eventSyncedGt(int value) {
        addGreaterThan(EventColumns.EVENT_SYNCED, value);
        return this;
    }

    public EventSelection eventSyncedGtEq(int value) {
        addGreaterThanOrEquals(EventColumns.EVENT_SYNCED, value);
        return this;
    }

    public EventSelection eventSyncedLt(int value) {
        addLessThan(EventColumns.EVENT_SYNCED, value);
        return this;
    }

    public EventSelection eventSyncedLtEq(int value) {
        addLessThanOrEquals(EventColumns.EVENT_SYNCED, value);
        return this;
    }
}
