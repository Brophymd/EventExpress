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
import edu.usf.EventExpress.provider.friendstatus.FriendStatusType;
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
            if (userCursor.getUserDeleted() != 0) {
                // Delete the item
                // we don't do any deletions in the user table right now
            } else {
                try {
                    Log.i(TAG, "Uploading user " + userCursor.getUserEmail());
                    server.addUser(token, new EventServer.UserItem(userCursor));
                    syncResult.stats.numInserts++;
                    userContentValues.putUserSynced(1).update(getContext().getContentResolver(), userNeedSync);
                } catch (RetrofitError e) {
                    handleRetrofitError(e, syncResult);
                }
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
                if (users != null && users.items != null) {
                    for (EventServer.UserItem msg : users.items) {
                        Log.d(TAG, "got user: " + msg.user_email + ", name: " + msg.name);
                        if (msg.deleted != 0) {
                            Log.d(TAG, "Deleting " + msg.user_email);
                            userSelection.googleId(msg.google_id).delete(getContext().getContentResolver());
                        } else {
                            Log.d(TAG, "Adding user:" + msg.user_email);
                            userContentValues.putGoogleId(msg.google_id)
                                    .putUserEmail(msg.user_email)
                                    .putUserName(msg.name)
                                    .putUserTimestamp(msg.timestamp)
                                    .putUserSynced(1)
                                    .insert(getContext().getContentResolver());
                        }
                    }
                }
                // Save sync timestamp
                if (users != null) {
                    users.latestTimestamp = new Timestamp(new Date().getTime()).toString();
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putString(KEY_LASTSYNC, users.latestTimestamp)
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
                    server.addEvent(token, new EventServer.EventItem(eventCursor));
                    syncResult.stats.numInserts++;
                    eventContentValues.putEventSynced(1).update(getContext().getContentResolver(), eventNeedSync);
                } catch (RetrofitError e) {
                    handleRetrofitError(e, syncResult);
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
                if (events != null && events.items != null) {
                    for (EventServer.EventItem msg : events.items) {
                        Log.d(TAG, "got event: " + msg.event_title);
                        if (msg.deleted != 0) {
                            Log.d(TAG, "Deleting: " + msg.event_title);
                            eventSelection.eventRemoteId(msg.remote_id).delete(getContext().getContentResolver());
                        } else {
                            Log.d(TAG, "Adding event:" + msg.event_title);
                            eventContentValues.putEventTitle(msg.event_title)
                                    .putEventAddress(msg.event_address)
                                    .putEventDate(msg.event_date)
                                    .putEventDescription(msg.event_description)
                                    .putEventLatitude(msg.event_latitude)
                                    .putEventLongitude(msg.event_longitude)
                                    .putEventOwner(msg.event_owner)
                                    .putEventTimestamp(msg.timestamp)
                                    .putEventType(msg.event_type)
                                    .putEventRemoteId(msg.remote_id)
                                    .putEventTimestamp(msg.timestamp)
                                    .putEventSynced(1)
                                    .insert(getContext().getContentResolver());
                        }
                    }
                }
                // Save sync timestamp
                if (events != null) {
                    events.latestTimestamp = new Timestamp(new Date().getTime()).toString();
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putString(KEY_LASTSYNC, events.latestTimestamp)
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
                Log.i(TAG, "Uploading event member " + eventMembersCursor.getUserEmail()
                        + " for event " + eventMembersCursor.getEventTitle());
                server.addAttendee(token, new EventServer.EventMembersItem(eventMembersCursor));
                syncResult.stats.numInserts++;
                eventMembersContentValues.putEventMembersSynced(1).update(getContext().getContentResolver(),
                        eventMembersNeedSync);
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
                // An HTTP error was encountered.
                switch (status) {
                    case 401: // Unauthorizedt
                        // attendee may already exist, so try patching:
                        try {
                            Log.i(TAG, "Modifying event member " + eventMembersCursor.getUserEmail()
                                    + " for event " + eventMembersCursor.getEventTitle());
                            server.updateAttendee(token,
                                    eventMembersCursor.getAttendeesRemoteId(),
                                    new EventServer.EventMembersItem(eventMembersCursor));
                            syncResult.stats.numInserts++;
                            eventMembersContentValues.putEventMembersSynced(1).update(getContext().getContentResolver(),
                                    eventMembersNeedSync);
                        }
                        catch (RetrofitError e2) {
                            handleRetrofitError(e2, syncResult);
                        }
                        break;
                    case 404: // No such item, should never happen, programming error
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
                if (eventMembers != null && eventMembers.items != null) {
                    for (EventServer.EventMembersItem msg : eventMembers.items) {
                        Log.d(TAG, "got attendee " + msg.user_id + " from event " + msg.event_id);
                        Log.d(TAG, "Adding local attendee " + msg.user_id + " to event " + msg.event_id);
                        UserSelection find_user = userSelection.googleId(msg.user_id);
                        UserCursor userCursor1 = find_user.query(getContext().getContentResolver());
                        Long user_id  = userCursor1.getId();
                        EventMembersSelection to_modify = eventMembersSelection.eventId(msg.event_id)
                                .and().userId(user_id);
                        if (to_modify != null) {
                            eventMembersContentValues.putRsvpStatus(msg.rsvp_status)
                                    .putEventMembersSynced(1)
                                    .update(getContext().getContentResolver(), to_modify);
                        }
                        else {
                            eventMembersContentValues.putEventId(msg.event_id)
                                    .putUserId(user_id)
                                    .putRsvpStatus(msg.rsvp_status)
                                    .putEventMembersSynced(1)
                                    .insert(getContext().getContentResolver());
                        }
                    }
                }
                // Save sync timestamp
                if (eventMembers != null) {
                    eventMembers.latestTimestamp = new Timestamp(new Date().getTime()).toString();
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putString(KEY_LASTSYNC, eventMembers.latestTimestamp)
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
        while (friendStatusCursor.moveToNext()) {
            try {
                Log.i(TAG, friendStatusCursor.getFromUserId() + " is friending " + friendStatusCursor.getToUserId());
                server.addFriend(token, new EventServer.FriendStatusItem(friendStatusCursor));
                syncResult.stats.numInserts++;
                friendStatusContentValues.putFriendStatusSynced(1).update(getContext().getContentResolver(),
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
                // An HTTP error was encountered.
                switch (status) {
                    case 401: // Unauthorized
                        // friend may already exist, so try patching:
                        try {
                            Log.i(TAG, "relationship modified between: "
                                    + friendStatusCursor.getFromUserId() + " and " + friendStatusCursor.getToUserId());
                            server.updateFriend(token,
                                    friendStatusCursor.getFriendsRemoteId(),
                                    new EventServer.FriendStatusItem(friendStatusCursor));
                            syncResult.stats.numInserts++;
                            friendStatusContentValues.putFriendStatusSynced(1).update(getContext().getContentResolver(),
                                    friendsNeedSync);
                        }
                        catch (RetrofitError e2) {
                            handleRetrofitError(e2, syncResult);
                        }
                        break;
                    case 404: // No such item, should never happen, programming error
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
                if (friends != null && friends.items != null) {
                    for (EventServer.FriendStatusItem msg : friends.items) {
                        Log.d(TAG, "friend status: " + msg.from_user_id + " and " + msg.to_user_id);
                        Log.d(TAG, "Adding local friend status " + msg.from_user_id + " and " + msg.to_user_id);
                        FriendStatusSelection to_modify = friendStatusSelection.fromUserId(msg.from_user_id)
                                .and().toUserId(msg.to_user_id);
                        if (to_modify != null) {
                            friendStatusContentValues.putStatus(msg.status)
                                    .putResponseTime(msg.response_time)
                                    .putFriendStatusSynced(1)
                                    .update(getContext().getContentResolver(), to_modify);
                        }
                        else {
                            friendStatusContentValues.putFriendsRemoteId(msg.remote_id)
                                    .putFromUserId(msg.from_user_id)
                                    .putToUserId(msg.to_user_id)
                                    .putStatus(msg.status)
                                    .putSentTime(msg.sent_time)
                                    .putResponseTime(msg.response_time)
                                    .putFriendStatusSynced(1)
                                    .insert(getContext().getContentResolver());
                        }
                    }
                }
                // Save sync timestamp
                if (friends != null) {
                    friends.latestTimestamp = new Timestamp(new Date().getTime()).toString();
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putString(KEY_LASTSYNC, friends.latestTimestamp)
                            .commit();
                }
            } catch (RetrofitError e) {
                handleRetrofitError(e, syncResult);
            }
        }
        userCursor.close();
        eventCursor.close();
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
            case 404: // only used if friend not found
                if (e.getResponse().getReason().equals("Friend not found")) {
                    // TODO: raise notification
                }
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