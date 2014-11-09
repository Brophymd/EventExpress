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
import edu.usf.EventExpress.provider.EventProvider;
import edu.usf.EventExpress.provider.event.EventColumns;
import edu.usf.EventExpress.provider.event.EventCursor;
import edu.usf.EventExpress.provider.user.UserColumns;
import edu.usf.EventExpress.provider.user.UserContentValues;
import edu.usf.EventExpress.provider.user.UserCursor;
import edu.usf.EventExpress.provider.user.UserSelection;
import retrofit.RetrofitError;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Global variables
    private static final String TAG = EventProvider.class.getSimpleName();
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
            UserSelection userSelection = new UserSelection();
            UserCursor userCursor = userSelection.synced(0).or().deleted(1)
                    .query(getContext().getContentResolver());
            UserContentValues userContentValues = new UserContentValues();
            // Upload new user
            while (userCursor.moveToNext()) {
                if (userCursor.getDeleted() != 0) {
                    // Delete the item
                    // we don't do any deletions in the user table right now
                }
                else {
                    server.addUser(token, userCursor);
                    syncResult.stats.numInserts++;
                    userContentValues.putSynced(1);
                    getContext().getContentResolver().update(UserColumns.CONTENT_URI,
                            userContentValues.values(), null, null);
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
                    for (EventColumns msg : events.items) {
//                        Log.d(TAG, "got url:" + msg.url + ", sha: " + msg.sha);
//                        if (msg.getDeleted() != 0) {
////                            Log.d(TAG, "Deleting:" + msg.url);
////                            db.deleteItem(item);
//                        }
//                        else {
////                            Log.d(TAG, "Adding url:" + item.url);
////                            item.synced = 1;
////                            db.putItem(item);
//                        }
                    }
                }
                // Save sync timestamp
                PreferenceManager.getDefaultSharedPreferences(getContext())
                        .edit().putString(KEY_LASTSYNC, events.latestTimestamp)
                        .commit();
            }
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