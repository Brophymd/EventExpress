package edu.usf.EventExpress.provider.event;

import java.util.Date;

import android.database.Cursor;

import edu.usf.EventExpress.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code event} table.
 */
public class EventCursor extends AbstractCursor {
    public EventCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code event_owner} value.
     * Cannot be {@code null}.
     */
    public String getEventOwner() {
        Integer index = getCachedColumnIndexOrThrow(EventColumns.EVENT_OWNER);
        return getString(index);
    }

    /**
     * Get the {@code event_type} value.
     * Cannot be {@code null}.
     */
    public EventType getEventType() {
        Integer intValue = getIntegerOrNull(EventColumns.EVENT_TYPE);
        if (intValue == null) return null;
        return EventType.values()[intValue];
    }

    /**
     * Get the {@code event_title} value.
     * Can be {@code null}.
     */
    public String getEventTitle() {
        Integer index = getCachedColumnIndexOrThrow(EventColumns.EVENT_TITLE);
        return getString(index);
    }

    /**
     * Get the {@code event_description} value.
     * Can be {@code null}.
     */
    public String getEventDescription() {
        Integer index = getCachedColumnIndexOrThrow(EventColumns.EVENT_DESCRIPTION);
        return getString(index);
    }

    /**
     * Get the {@code event_date} value.
     * Can be {@code null}.
     */
    public Date getEventDate() {
        return getDate(EventColumns.EVENT_DATE);
    }

    /**
     * Get the {@code event_address} value.
     * Can be {@code null}.
     */
    public String getEventAddress() {
        Integer index = getCachedColumnIndexOrThrow(EventColumns.EVENT_ADDRESS);
        return getString(index);
    }

    /**
     * Get the {@code event_latitude} value.
     * Can be {@code null}.
     */
    public Float getEventLatitude() {
        return getFloatOrNull(EventColumns.EVENT_LATITUDE);
    }

    /**
     * Get the {@code event_longitude} value.
     * Can be {@code null}.
     */
    public Float getEventLongitude() {
        return getFloatOrNull(EventColumns.EVENT_LONGITUDE);
    }

    /**
     * Get the {@code timestamp} value.
     * Cannot be {@code null}.
     */
    public String getTimestamp() {
        Integer index = getCachedColumnIndexOrThrow(EventColumns.TIMESTAMP);
        return getString(index);
    }

    /**
     * Get the {@code deleted} value.
     */
    public int getDeleted() {
        return getIntegerOrNull(EventColumns.DELETED);
    }

    /**
     * Get the {@code synced} value.
     */
    public int getSynced() {
        return getIntegerOrNull(EventColumns.SYNCED);
    }
}
