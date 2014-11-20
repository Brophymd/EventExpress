package edu.usf.EventExpress.gcm;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Micah on 10/31/2014.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    // Incoming Intent key for extended data
    private static final String TAG = GcmBroadcastReceiver.class.getSimpleName();

    public GcmBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        Log.d(TAG, "in onReceive!");
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
