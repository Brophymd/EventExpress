package edu.usf.EventExpress.sync;

/**
 * Created by Micah on 11/7/2014.
 */

import android.accounts.Account;
import android.content.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.usf.EventExpress.gcm.GCMHelper;
import edu.usf.EventExpress.provider.event.EventContentValues;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.event.EventSelection;
import edu.usf.EventExpress.provider.eventmembers.EventMembersContentValues;
import edu.usf.EventExpress.provider.eventmembers.EventMembersCursor;
import edu.usf.EventExpress.provider.eventmembers.EventMembersSelection;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusContentValues;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusCursor;
import edu.usf.EventExpress.provider.friendstatus.FriendStatusSelection;
import edu.usf.EventExpress.provider.user.UserContentValues;
import edu.usf.EventExpress.provider.user.UserCursor;
import edu.usf.EventExpress.provider.user.UserSelection;
import retrofit.RetrofitError;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Global variables
    private static final String TAG = SyncAdapter.class.getSimpleName();
    private static final String KEY_LASTSYNC = "key_lastsync";
    private static final String KEY_EMAIL = "Email";
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
        Log.i(TAG, "onPerformSync started");
        Log.i(TAG, "Account name: " + account.name);
        // Need to get an access token first
        final String token = SyncHelper.getAuthToken(getContext(),
                account.name);
        Log.d(TAG, "token: " + token);
        if (token == null) {
            Log.e(TAG, "Token was null. Aborting sync");
            // Sync is rescheduled by SyncHelper
            return;
        }
        // Just to make sure. Can happen if sync happens in background first
        // time
        if (null == SyncHelper.getSavedAccountName(getContext())) {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit().putString(SyncHelper.KEY_ACCOUNT, account.name)
                    .commit();
        }
        // token should be good. Transmit
        // Register for GCM if we need to
        GCMHelper.registerIfNotAlreadyDone(getContext());
        final EventServer server = SyncHelper.getRESTAdapter();
        // Upload new users
        UserSelection userSelection = new UserSelection();
        UserSelection userNeedSync = userSelection.userSynced(0).or().userDeleted(1);
        UserCursor userCursor = userNeedSync.query(getContext().getContentResolver());
        UserContentValues userContentValues = new UserContentValues();
        while (userCursor.moveToNext()) {
            try {
                Log.i(TAG, "Uploading user " + userCursor.getUserEmail());
                server.addUser(token, new EventServer.UserItem(userCursor));
                syncResult.stats.numInserts++;
                userContentValues.putUserSynced(1).update(getContext().getContentResolver(), userNeedSync);
            } catch (RetrofitError e) {
                handleRetrofitError(e, syncResult);
            }
        }
        // Download user info
        if (!extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false)) {
            // Check if we synced before
            final String lastSync = PreferenceManager
                    .getDefaultSharedPreferences(getContext()).getString(
                            KEY_LASTSYNC, null);
            final EventServer.UserItems users;
            try {
                Log.i(TAG, "Downloading users");
                users = server.getUsers(token,
                        getContext().getSharedPreferences("EventExpress", Context.MODE_PRIVATE).getString(KEY_EMAIL, ""));
                if (users != null && users.objects != null) {
                    for (EventServer.UserItem msg : users.objects) {
                        Log.d(TAG, "got user: " + msg.user_email + ", name: " + msg.name);
                        // EITHER DELETE
                        if (msg.deleted != 0) {
                            Log.d(TAG, "Deleting " + msg.user_email);
                            new UserSelection().googleId(msg.google_id).delete(getContext().getContentResolver());
                        } else {
                            Log.d(TAG, "Adding user:" + msg.user_email);
                            UserContentValues todo = userContentValues.putGoogleId(msg.google_id)
                                    .putUserEmail(msg.user_email)
                                    .putUserName(msg.name)
                                    .putUserTimestamp(msg.timestamp)
                                    .putUserSynced(1);
                            // OR UPDATE
                            int nRows =  todo.update(getContext().getContentResolver(),
                                    new UserSelection().googleId(msg.google_id));
                            // OR INSERT
                            if (nRows == 0) {
                                todo.insert(getContext().getContentResolver());
                            }
                        }
                    }
                }
                // Save sync timestamp
                if (users != null) {
                    users.latest_timestamp = new Timestamp(new Date().getTime()).toString();
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putString(KEY_LASTSYNC, users.latest_timestamp)
                            .commit();
                }
            } catch (RetrofitError e) {
                handleRetrofitError(e, syncResult);
            }
        }
        // Upload new events
        EventSelection eventSelection = new EventSelection();
        EventSelection eventNeedSync = eventSelection.eventSynced(0).or().eventDeleted(1);
        EventCursor eventCursor = eventNeedSync.query(getContext().getContentResolver());
        EventContentValues eventContentValues = new EventContentValues();
        while (eventCursor.moveToNext()) {
            if (eventCursor.getEventDeleted() != 0) {
                try {
                    Log.i(TAG, "Deleting event " + eventCursor.getEventTitle());
                    server.deleteEvent(token, eventCursor.getEventRemoteId());
                } catch (RetrofitError e) {
                    handleRetrofitError(e, syncResult);
                }
            } else {
                try {
                    Log.i(TAG, "Uploading event " + eventCursor.getEventTitle());
                    EventServer.EventResponse response = server.addEvent(token, new EventServer.EventItem(eventCursor));
                    syncResult.stats.numInserts++;
                    eventContentValues.putEventSynced(1)
                            .putEventRemoteId(response.id)
                            .update(getContext().getContentResolver(), eventNeedSync);
                } catch (RetrofitError e) {
                    Log.d(TAG, "" + e);
                    final int status;
                    if (e.getResponse() != null) {
                        Log.e(TAG, "" + e.getResponse().getStatus() + "; "
                                + e.getResponse().getReason());
                        status = e.getResponse().getStatus();
                    } else {
                        status = 999;
                    }
                    // If conflict, try updating; otherwise handleRetrofitError
                    switch (status) {
                        case 409: // Conflict
                            // attendee may already exist, so try patching:
                            try {
                                Log.i(TAG, "Modifying event " + eventCursor.getEventTitle());
                                server.updateEvent(token,
                                        eventCursor.getEventRemoteId(),
                                        new EventServer.EventItem(eventCursor));
                                syncResult.stats.numInserts++;
                                eventContentValues.putEventSynced(1).update(getContext().getContentResolver(),
                                        eventNeedSync);
                            }
                            // Something else is happening
                            catch (RetrofitError e2) {
                                handleRetrofitError(e2, syncResult);
                            }
                            break;
                        default: handleRetrofitError(e, syncResult);
                    }
                }
            }
        }
        // Download event info
        if (!extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false)) {
            // Check if we synced before
            final String lastSync = PreferenceManager
                    .getDefaultSharedPreferences(getContext()).getString(
                            KEY_LASTSYNC, null);
            final EventServer.EventItems events;
            try {
                Log.i(TAG, "Downloading events");
                if (lastSync != null && !lastSync.isEmpty()) {
                    events = server.getEvents(token, lastSync);
                } else {
                    events = server.getEvents(token, null);
                }
                if (events != null && events.objects != null) {
                    for (EventServer.EventResponse msg : events.objects) {
                        Log.d(TAG, "got event: " + msg.event_title);
                        if (msg.deleted != 0) {
                            Log.d(TAG, "Deleting: " + msg.event_title);
                            new EventSelection().eventRemoteId(msg.id).delete(getContext().getContentResolver());
                        } else {
                            Log.d(TAG, "Adding event:" + msg.event_title);
                            EventContentValues todo = eventContentValues.putEventTitle(msg.event_title)
                                    .putEventAddress(msg.event_address)
                                    .putEventDate(msg.event_date)
                                    .putEventDescription(msg.event_description)
                                    .putEventLatitude(msg.event_latitude)
                                    .putEventLongitude(msg.event_longitude)
                                    .putEventOwner(msg.event_owner)
                                    .putEventTimestamp(msg.timestamp)
                                    .putEventType(msg.event_type)
                                    .putEventRemoteId(msg.id)
                                    .putEventTimestamp(msg.timestamp)
                                    .putEventSynced(1);
                            int nRows = todo.update(getContext().getContentResolver(),
                                    new EventSelection().eventRemoteId(msg.id));
                            if (nRows == 0) {
                                todo.insert(getContext().getContentResolver());
                            }
                        }
                    }
                }
                // Save sync timestamp
                if (events != null) {
                    events.latest_timestamp = new Timestamp(new Date().getTime()).toString();
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putString(KEY_LASTSYNC, events.latest_timestamp)
                            .commit();
                }
            } catch (RetrofitError e) {
                handleRetrofitError(e, syncResult);
            }
        }
        // Upload new event members
        EventMembersSelection eventMembersSelection = new EventMembersSelection();
        EventMembersSelection eventMembersNeedSync = eventMembersSelection.eventMembersSynced(0)
                .or().eventMembersDeleted(1);
        EventMembersCursor eventMembersCursor = eventMembersNeedSync.query(getContext().getContentResolver());
        EventMembersContentValues eventMembersContentValues = new EventMembersContentValues();
        while (eventMembersCursor.moveToNext()) {
            try {
                Log.i(TAG, "Uploading event member " + eventMembersCursor.getUserId()
                        + " for event " + eventMembersCursor.getEventTitle());
                // Add EventMember and get response
                EventServer.EventMembersResponse response = server.addAttendee(token,
                        new EventServer.EventMembersItem(eventMembersCursor));
                syncResult.stats.numInserts++;
                // Update local database to reflect synchronization; we now have the remote id, and can put synced
                eventMembersContentValues.putEventMembersSynced(1)
                        .putAttendeesRemoteId(response.id)
                        .update(getContext().getContentResolver(), eventMembersNeedSync);
            } catch (RetrofitError e) {
                Log.d(TAG, "" + e);
                final int status;
                if (e.getResponse() != null) {
                    Log.e(TAG, "" + e.getResponse().getStatus() + "; "
                            + e.getResponse().getReason());
                    status = e.getResponse().getStatus();
                } else {
                    status = 999;
                }
                // If insert conflict, try update; otherwise call handleRetrofitError
                switch (status) {
                    case 409: // Conflict
                        // attendee may already exist, so try patching:
                        try {
                            Log.i(TAG, "Modifying event member " + eventMembersCursor.getUserId()
                                    + " for event " + eventMembersCursor.getEventTitle());
                            server.updateAttendee(token,
                                    eventMembersCursor.getAttendeesRemoteId(),
                                    new EventServer.EventMembersItem(eventMembersCursor));
                            syncResult.stats.numInserts++;
                            eventMembersContentValues.putEventMembersSynced(1).update(getContext().getContentResolver(),
                                    eventMembersNeedSync);
                        }
                        // Something else is happening
                        catch (RetrofitError e2) {
                            handleRetrofitError(e2, syncResult);
                        }
                        break;
                    default: handleRetrofitError(e, syncResult);
                }
            }
        }
        // Download event members
        if (!extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false)) {
            // Check if we synced before
            final String lastSync = PreferenceManager
                    .getDefaultSharedPreferences(getContext()).getString(
                            KEY_LASTSYNC, null);
            final EventServer.EventMembersItems eventMembers;
            try {
                Log.i(TAG, "Downloading event members");
                if (lastSync != null && !lastSync.isEmpty()) {
                    eventMembers = server.getAttendees(token, lastSync);
                } else {
                    eventMembers = server.getAttendees(token, null);
                }
                // there are event members to delete/insert/update
                if (eventMembers != null && eventMembers.objects != null) {
                    for (EventServer.EventMembersResponse msg : eventMembers.objects) {
                        Log.d(TAG, "got attendee " + msg.user_id + " from event " + msg.event_id);
                        // EITHER DELETE
                        if (msg.deleted != 0) {
                            Log.d(TAG, "Deleting " + msg.user_id + " from " + msg.event_id);
                            new EventMembersSelection().attendeesRemoteId(msg.id).delete(getContext().getContentResolver());
                        }
                        else {
                            // build values to update/insert
                            EventMembersContentValues todo = eventMembersContentValues
                                    .putUserId(msg.user_id)
                                    .putRsvpStatus(msg.rsvp_status)
                                    .putEventMembersTimestamp(msg.timestamp)
                                    .putEventMembersSynced(1)
                                    .putAttendeesRemoteId(msg.id);
                            // OR UPDATE
                            int nRows = todo.update(getContext().getContentResolver(),
                                    new EventMembersSelection().attendeesRemoteId(msg.id));
                            // OR INSERT
                            if (nRows == 0) {
                                // need to find event on phone with given remote event id
                                eventCursor = new EventSelection().eventRemoteId(msg.event_id)
                                        .query(getContext().getContentResolver());
                                while (eventCursor.moveToNext()) {
                                    // insert
                                    todo.putEventId(eventCursor.getId()).insert(getContext().getContentResolver());
                                }
                            }
                        }
                    }
                }
                // Save sync timestamp
                if (eventMembers != null) {
                    eventMembers.latest_timestamp = new Timestamp(new Date().getTime()).toString();
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putString(KEY_LASTSYNC, eventMembers.latest_timestamp)
                            .commit();
                }
            } catch (RetrofitError e) {
                handleRetrofitError(e, syncResult);
            }
        }
        // Upload new friends
        FriendStatusSelection friendStatusSelection = new FriendStatusSelection();
        FriendStatusSelection friendsNeedSync = friendStatusSelection.friendStatusSynced(0)
                .or().friendStatusDeleted(1);
        FriendStatusCursor friendStatusCursor = friendsNeedSync.query(getContext().getContentResolver());
        FriendStatusContentValues friendStatusContentValues = new FriendStatusContentValues();
        // Iterate over friends which are either dirty (sync = 0) or marked for deletion
        while (friendStatusCursor.moveToNext()) {
            // First try inserting
            try {
                Log.i(TAG, friendStatusCursor.getFromUserId() + " is friending " + friendStatusCursor.getToUserId());
                EventServer.FriendStatusResponse response = server.addFriend(token,
                        new EventServer.FriendStatusItem(friendStatusCursor));
                syncResult.stats.numInserts++;
                // update remote id to id given in response, set synced, update
                friendStatusContentValues.putFriendStatusSynced(1)
                        .putFriendsRemoteId(response.id)
                        .update(getContext().getContentResolver(),
                                friendsNeedSync);
            } catch (RetrofitError e) {
                Log.d(TAG, "" + e);
                final int status;
                if (e.getResponse() != null) {
                    Log.e(TAG, "" + e.getResponse().getStatus() + "; "
                            + e.getResponse().getReason());
                    status = e.getResponse().getStatus();
                } else {
                    status = 999;
                }
                // If insert conflict, try update; otherwise call handleRetrofitError
                switch (status) {
                    case 409: // Conflict
                        // friend status may already exist, so try patching:
                        try {
                            Log.i(TAG, "relationship modified between: "
                                    + friendStatusCursor.getFromUserId() + " and " + friendStatusCursor.getToUserId());
                            server.updateFriend(token,
                                    friendStatusCursor.getFriendsRemoteId(),
                                    new EventServer.FriendStatusItem(friendStatusCursor));
                            syncResult.stats.numInserts++;
                            friendStatusContentValues.putFriendStatusSynced(1).update(getContext().getContentResolver(),
                                    friendsNeedSync);
                            break;
                        }
                        // Something else is happening
                        catch (RetrofitError e2) {
                            handleRetrofitError(e2, syncResult);
                        }
                        break;
                    case 404: // Friend not found -- FALL THROUGH TO DEFAULT FOR CLEANUP :(
                        // TODO: raise notification
                    default:
                        Log.i(TAG, "Error occurred; deleting local friend status entry");
                        new FriendStatusSelection().fromUserEmail(friendStatusCursor.getFromUserEmail())
                                .toUserEmail(friendStatusCursor.getToUserEmail())
                                .delete(getContext().getContentResolver());
                        handleRetrofitError(e, syncResult);
                }
            }
        }
        // Download friends
        if (!extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false)) {
            // Check if we synced before
            final String lastSync = PreferenceManager
                    .getDefaultSharedPreferences(getContext()).getString(
                            KEY_LASTSYNC, null);
            final EventServer.FriendStatusItems friends;
            try {
                Log.i(TAG, "Downloading friends");
                if (lastSync != null && !lastSync.isEmpty()) {
                    friends = server.getFriends(token, lastSync);
                } else {
                    friends = server.getFriends(token, null);
                }
                // there are event members to delete/insert/update
                if (friends != null && friends.objects != null) {
                    for (EventServer.FriendStatusResponse msg : friends.objects) {
                        Log.d(TAG, "friend status: " + msg.from_user_id + " and " + msg.to_user_id);
                        // EITHER DELETE
                        if (msg.deleted != 0) {
                            Log.d(TAG, "Deleting friend status: " + msg.from_user_id + " and " + msg.to_user_id);
                            new FriendStatusSelection().friendsRemoteId(msg.id).delete(getContext().getContentResolver());
                        } else {
                            // build values to update/insert
                            FriendStatusContentValues todo = friendStatusContentValues
                                    .putFromUserId(msg.from_user_id)
                                    .putToUserId(msg.to_user_id)
                                    .putFromUserEmail(msg.from_user_email)
                                    .putToUserEmail(msg.to_user_email)
                                    .putStatus(msg.status)
                                    .putSentTime(msg.sent_time)
                                    .putResponseTime(msg.response_time)
                                    .putFriendsRemoteId(msg.id)
                                    .putFriendStatusTimestamp(msg.timestamp)
                                    .putFriendStatusSynced(1);
                            // OR UPDATE
                            int nRows = todo.update(getContext().getContentResolver(),
                                    new FriendStatusSelection().friendsRemoteId(msg.id));
                            // OR INSERT
                            if (nRows == 0) {
                                todo.insert(getContext().getContentResolver());
                            }
                        }
                    }
                }
                // Save sync timestamp
                if (friends != null) {
                    friends.latest_timestamp = new Timestamp(new Date().getTime()).toString();
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putString(KEY_LASTSYNC, friends.latest_timestamp)
                            .commit();
                }
            } catch (RetrofitError e) {
                handleRetrofitError(e, syncResult);
            }
        }
        userCursor.close();
        eventCursor.close();
        eventMembersCursor.close();
        friendStatusCursor.close();
    }

    private void handleRetrofitError(RetrofitError e, SyncResult syncResult) {
        Log.d(TAG, "" + e);
        final int status;
        if (e.getResponse() != null) {
            Log.e(TAG, "" + e.getResponse().getStatus() + "; "
                    + e.getResponse().getReason());
            status = e.getResponse().getStatus();
        } else {
            status = 999;
        }
        // An HTTP error was encountered.
        switch (status) {
            case 401: // Unauthorized
                syncResult.stats.numAuthExceptions++;
                break;
            case 404: // something not found
            case 415: // Not proper body, programming error
            case 400: // Didn't specify url, programming error
                syncResult.databaseError = true;
                break;
            default: // Default is to consider it a networking problem
                syncResult.stats.numIoExceptions++;
                break;
        }
    }
}