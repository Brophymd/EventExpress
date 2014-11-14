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
        try {
            Log.i(TAG, "onPerformSync started");
            Log.i(TAG, "Account name: " + account.name);
            // Need to get an access token first
            final String token = SyncHelper.getAuthToken(getContext(),
                    account.name);
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
                }
                else {
                    Log.i(TAG, "Uploading user " + userCursor.getGoogleId());
                    server.addUser(token, new EventServer.UserItem(userCursor));
                    syncResult.stats.numInserts++;
                    userContentValues.putUserSynced(1).update(getContext().getContentResolver(), userNeedSync);
                }
            }
            // Upload new events
            EventSelection eventSelection = new EventSelection();
            EventSelection eventNeedSync = eventSelection.eventSynced(0).or().eventDeleted(1);
            EventCursor eventCursor = eventNeedSync.query(getContext().getContentResolver());
            EventContentValues eventContentValues = new EventContentValues();
            while (eventCursor.moveToNext()) {
                if (eventCursor.getEventDeleted() != 0) {
                    Log.i(TAG, "Deleting event " + eventCursor.getEventTitle());
                    server.deleteEvent(token, eventCursor.getRemoteId());
                }
                else {
                    Log.i(TAG, "Uploading event " + eventCursor.getEventTitle());
                    server.addEvent(token, new EventServer.EventItem(eventCursor));
                    syncResult.stats.numInserts++;
                    eventContentValues.putEventSynced(1).update(getContext().getContentResolver(), eventNeedSync);
                }
            }
            // Download user info
            if (!extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false)) {
                // Check if we synced before
                final String lastSync = PreferenceManager
                        .getDefaultSharedPreferences(getContext()).getString(
                                KEY_LASTSYNC, null);
                final EventServer.UserItems users;
                if (lastSync != null && !lastSync.isEmpty()) {
                    users = server.getUsers(token, lastSync);
                }
                else {
                    users = server.getUsers(token, null);
                }
                if (users != null && users.items != null) {
                    for (EventServer.UserItem msg : users.items) {
                        Log.d(TAG, "got google_id: " + msg.google_id + ", name: " + msg.name);
                        if (msg.deleted != 0) {
                            Log.d(TAG, "Deleting: " + msg.google_id);
                            userSelection.googleId(msg.google_id).delete(getContext().getContentResolver());
                        }
                        else {
                            Log.d(TAG, "Adding google_id:" + msg.google_id);
                            userContentValues.putGoogleId(msg.google_id)
                                    .putUserName(msg.name)
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
            }
            // Download event info
            if (!extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false)) {
                // Check if we synced before
                final String lastSync = PreferenceManager
                        .getDefaultSharedPreferences(getContext()).getString(
                                KEY_LASTSYNC, null);
                final EventServer.EventItems events;
                if (lastSync != null && !lastSync.isEmpty()) {
                    events = server.getEvents(token, lastSync);
                }
                else {
                    events = server.getEvents(token, null);
                }
                if (events != null && events.items != null) {
                    for (EventServer.EventItem msg : events.items) {
                        Log.d(TAG, "got event: " + msg.event_title);
                        if (msg.deleted != 0) {
                            Log.d(TAG, "Deleting: " + msg.event_title);
                            eventSelection.id(msg.remote_id).delete(getContext().getContentResolver());
                        }
                        else {
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
                                    .putRemoteId(msg.remote_id)
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
            }
            userCursor.close();
            eventCursor.close();
        }
        catch (RetrofitError e) {
            Log.d(TAG, "" + e);
            final int status;
            if (e.getResponse() != null) {
                Log.e(TAG, "" + e.getResponse().getStatus() + "; "
                        + e.getResponse().getReason());
                status = e.getResponse().getStatus();
            }
            else {
                status = 999;
            }
            // An HTTP error was encountered.
            switch (status) {
                case 401: // Unauthorized
                    syncResult.stats.numAuthExceptions++;
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
}