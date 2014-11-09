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

    public EventSelection timestamp(String... value) {
        addEquals(EventColumns.TIMESTAMP, value);
        return this;
    }

    public EventSelection timestampNot(String... value) {
        addNotEquals(EventColumns.TIMESTAMP, value);
        return this;
    }

    public EventSelection timestampLike(String... value) {
        addLike(EventColumns.TIMESTAMP, value);
        return this;
    }

    public EventSelection deleted(int... value) {
        addEquals(EventColumns.DELETED, toObjectArray(value));
        return this;
    }

    public EventSelection deletedNot(int... value) {
        addNotEquals(EventColumns.DELETED, toObjectArray(value));
        return this;
    }

    public EventSelection deletedGt(int value) {
        addGreaterThan(EventColumns.DELETED, value);
        return this;
    }

    public EventSelection deletedGtEq(int value) {
        addGreaterThanOrEquals(EventColumns.DELETED, value);
        return this;
    }

    public EventSelection deletedLt(int value) {
        addLessThan(EventColumns.DELETED, value);
        return this;
    }

    public EventSelection deletedLtEq(int value) {
        addLessThanOrEquals(EventColumns.DELETED, value);
        return this;
    }

    public EventSelection synced(int... value) {
        addEquals(EventColumns.SYNCED, toObjectArray(value));
        return this;
    }

    public EventSelection syncedNot(int... value) {
        addNotEquals(EventColumns.SYNCED, toObjectArray(value));
        return this;
    }

    public EventSelection syncedGt(int value) {
        addGreaterThan(EventColumns.SYNCED, value);
        return this;
    }

    public EventSelection syncedGtEq(int value) {
        addGreaterThanOrEquals(EventColumns.SYNCED, value);
        return this;
    }

    public EventSelection syncedLt(int value) {
        addLessThan(EventColumns.SYNCED, value);
        return this;
    }

    public EventSelection syncedLtEq(int value) {
        addLessThanOrEquals(EventColumns.SYNCED, value);
        return this;
    }
}
