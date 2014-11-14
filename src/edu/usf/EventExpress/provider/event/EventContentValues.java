package edu.usf.EventExpress.provider.event;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import edu.usf.EventExpress.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code event} table.
 */
public class EventContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return EventColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, EventSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public EventContentValues putEventOwner(String value) {
        if (value == null) throw new IllegalArgumentException("value for eventOwner must not be null");
        mContentValues.put(EventColumns.EVENT_OWNER, value);
        return this;
    }



    public EventContentValues putRemoteId(Long value) {
        mContentValues.put(EventColumns.REMOTE_ID, value);
        return this;
    }

    public EventContentValues putRemoteIdNull() {
        mContentValues.putNull(EventColumns.REMOTE_ID);
        return this;
    }


    public EventContentValues putEventType(EventType value) {
        if (value == null) throw new IllegalArgumentException("value for eventType must not be null");
        mContentValues.put(EventColumns.EVENT_TYPE, value.ordinal());
        return this;
    }



    public EventContentValues putEventTitle(String value) {
        mContentValues.put(EventColumns.EVENT_TITLE, value);
        return this;
    }

    public EventContentValues putEventTitleNull() {
        mContentValues.putNull(EventColumns.EVENT_TITLE);
        return this;
    }


    public EventContentValues putEventDescription(String value) {
        mContentValues.put(EventColumns.EVENT_DESCRIPTION, value);
        return this;
    }

    public EventContentValues putEventDescriptionNull() {
        mContentValues.putNull(EventColumns.EVENT_DESCRIPTION);
        return this;
    }


    public EventContentValues putEventDate(Date value) {
        mContentValues.put(EventColumns.EVENT_DATE, value == null ? null : value.getTime());
        return this;
    }

    public EventContentValues putEventDateNull() {
        mContentValues.putNull(EventColumns.EVENT_DATE);
        return this;
    }

    public EventContentValues putEventDate(Long value) {
        mContentValues.put(EventColumns.EVENT_DATE, value);
        return this;
    }


    public EventContentValues putEventAddress(String value) {
        mContentValues.put(EventColumns.EVENT_ADDRESS, value);
        return this;
    }

    public EventContentValues putEventAddressNull() {
        mContentValues.putNull(EventColumns.EVENT_ADDRESS);
        return this;
    }


    public EventContentValues putEventLatitude(Float value) {
        mContentValues.put(EventColumns.EVENT_LATITUDE, value);
        return this;
    }

    public EventContentValues putEventLatitudeNull() {
        mContentValues.putNull(EventColumns.EVENT_LATITUDE);
        return this;
    }


    public EventContentValues putEventLongitude(Float value) {
        mContentValues.put(EventColumns.EVENT_LONGITUDE, value);
        return this;
    }

    public EventContentValues putEventLongitudeNull() {
        mContentValues.putNull(EventColumns.EVENT_LONGITUDE);
        return this;
    }


    public EventContentValues putEventTimestamp(Date value) {
        if (value == null) throw new IllegalArgumentException("value for eventTimestamp must not be null");
        mContentValues.put(EventColumns.EVENT_TIMESTAMP, value.getTime());
        return this;
    }


    public EventContentValues putEventTimestamp(long value) {
        mContentValues.put(EventColumns.EVENT_TIMESTAMP, value);
        return this;
    }


    public EventContentValues putEventDeleted(int value) {
        mContentValues.put(EventColumns.EVENT_DELETED, value);
        return this;
    }



    public EventContentValues putEventSynced(int value) {
        mContentValues.put(EventColumns.EVENT_SYNCED, value);
        return this;
    }


}
