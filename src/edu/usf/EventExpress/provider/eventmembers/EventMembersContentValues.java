package edu.usf.EventExpress.provider.eventmembers;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import edu.usf.EventExpress.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code event_members} table.
 */
public class EventMembersContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return EventMembersColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, EventMembersSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public EventMembersContentValues putEventId(long value) {
        mContentValues.put(EventMembersColumns.EVENT_ID, value);
        return this;
    }



    public EventMembersContentValues putUserId(long value) {
        mContentValues.put(EventMembersColumns.USER_ID, value);
        return this;
    }



    public EventMembersContentValues putRsvpStatus(RSVPStatus value) {
        mContentValues.put(EventMembersColumns.RSVP_STATUS, value == null ? null : value.ordinal());
        return this;
    }

    public EventMembersContentValues putRsvpStatusNull() {
        mContentValues.putNull(EventMembersColumns.RSVP_STATUS);
        return this;
    }


    public EventMembersContentValues putEventMembersTimestamp(String value) {
        if (value == null) throw new IllegalArgumentException("value for eventMembersTimestamp must not be null");
        mContentValues.put(EventMembersColumns.EVENT_MEMBERS_TIMESTAMP, value);
        return this;
    }



    public EventMembersContentValues putEventMembersDeleted(int value) {
        mContentValues.put(EventMembersColumns.EVENT_MEMBERS_DELETED, value);
        return this;
    }



    public EventMembersContentValues putEventMembersSynced(int value) {
        mContentValues.put(EventMembersColumns.EVENT_MEMBERS_SYNCED, value);
        return this;
    }


}
