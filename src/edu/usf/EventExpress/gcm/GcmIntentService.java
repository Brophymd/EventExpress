package edu.usf.EventExpress.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import edu.usf.EventExpress.GoogleLoginActivity;
import edu.usf.EventExpress.R;
import edu.usf.EventExpress.sync.SyncHelper;

/**
 * Created by Micah on 10/31/2014.
 */
public class GcmIntentService extends IntentService {
    public static final String KEY_SYNC_REQUEST = "edu.usf.EventExpress.KEY_SYNC_REQUEST";
    private static final String TAG = GcmBroadcastReceiver.class.getSimpleName();

    public GcmIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that
             * GCM will be extended in the future with new message types, just
             * ignore any message types you're not interested in, or that you
             * don't recognize.
             */

            // Handle GCM sync message
            if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType) && intent.getBooleanExtra(KEY_SYNC_REQUEST, true)) {
                // We reached the limit of 100 queued messages. Request a full
                // sync
                SyncHelper.manualSync(this);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}