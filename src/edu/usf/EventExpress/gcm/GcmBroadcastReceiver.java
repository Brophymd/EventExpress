package edu.usf.EventExpress.gcm;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import edu.usf.EventExpress.sync.SyncHelper;

/**
 * Created by Micah on 10/31/2014.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    // Incoming Intent key for extended data
    public static final String KEY_SYNC_REQUEST = "edu.usf.EventExpress.KEY_SYNC_REQUEST";

    @Override
    public void onReceive(Context context, Intent intent) {
//        ComponentName comp = new ComponentName(context.getPackageName(),
//                GcmIntentService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);
        // Get a GCM object instance
        GoogleCloudMessaging gcm =
                GoogleCloudMessaging.getInstance(context);
        // Get the type of GCM message
        String messageType = gcm.getMessageType(intent);
        /*
         * Test the message type and examine the message contents.
         * Since GCM is a general-purpose messaging system, you
         * may receive normal messages that don't require a sync
         * adapter run.
         * The following code tests for a a boolean flag indicating
         * that the message is requesting a transfer from the device.
         */
        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)
                && intent.getBooleanExtra(KEY_SYNC_REQUEST, false)) {
            SyncHelper.manualSync(context);
        }
    }
}
