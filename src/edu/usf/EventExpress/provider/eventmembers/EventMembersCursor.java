package edu.usf.EventExpress.provider.eventmembers;

import java.util.Date;

import android.database.Cursor;

import edu.usf.EventExpress.provider.base.AbstractCursor;
import edu.usf.EventExpress.provider.event.*;
import edu.usf.EventExpress.provider.user.*;

/**
 * Cursor wrapper for the {@code event_members} table.
 */
public class EventMembersCursor extends AbstractCursor {
    public EventMembersCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code attendees_remote_id} value.
     * Can be {@code null}.
     */
    public Long getAttendeesRemoteId() {
        return getLongOrNull(EventMembersColumns.ATTENDEES_REMOTE_ID);
    }

    /**
     * Get the {@code event_id} value.
     */
    public long getEventId() {
        return getLongOrNull(EventMembersColumns.EVENT_ID);
    }

    /**
     * Get the {@code user_id} value.
     */
    public long getUserId() {
        return getLongOrNull(EventMembersColumns.USER_ID);
    }

    /**
     * Get the {@code rsvp_status} value.
     * Can be {@code null}.
     */
    public RSVPStatus getRsvpStatus() {
        Integer intValue = getIntegerOrNull(EventMembersColumns.RSVP_STATUS);
        if (intValue == null) return null;
        return RSVPStatus.values()[intValue];
    }

    /**
     * Get the {@code event_members_timestamp} value.
     * Cannot be {@code null}.
     */
    public Date getEventMembersTimestamp() {
        return getDate(EventMembersColumns.EVENT_MEMBERS_TIMESTAMP);
    }

    /**
     * Get the {@code event_members_deleted} value.
     */
    public int getEventMembersDeleted() {
        return getIntegerOrNull(EventMembersColumns.EVENT_MEMBERS_DELETED);
    }

    /**
     * Get the {@code event_members_synced} value.
     */
    public int getEventMembersSynced() {
        return getIntegerOrNull(EventMembersColumns.EVENT_MEMBERS_SYNCED);
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
     * Get the {@code event_remote_id} value.
     * Can be {@code null}.
     */
    public Long getEventRemoteId() {
        return getLongOrNull(EventColumns.EVENT_REMOTE_ID);
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
     * Get the {@code event_timestamp} value.
     * Cannot be {@code null}.
     */
    public Date getEventTimestamp() {
        return getDate(EventColumns.EVENT_TIMESTAMP);
    }

    /**
     * Get the {@code event_deleted} value.
     */
    public int getEventDeleted() {
        return getIntegerOrNull(EventColumns.EVENT_DELETED);
    }

    /**
     * Get the {@code event_synced} value.
     */
    public int getEventSynced() {
        return getIntegerOrNull(EventColumns.EVENT_SYNCED);
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
