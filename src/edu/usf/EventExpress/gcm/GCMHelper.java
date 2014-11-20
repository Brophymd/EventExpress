package edu.usf.EventExpress.gcm;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import edu.usf.EventExpress.sync.EventServer;
import edu.usf.EventExpress.sync.SyncHelper;
import edu.usf.EventExpress.sync.EventServer.RegId;
import retrofit.RetrofitError;

/**
 * Created by Micah on 11/8/2014.
 */

public class GCMHelper {
    private static final String KEY_REGID = "KEY_REGID";
    private static final String KEY_APP_VERSION = "KEY_APP_VERSION";
    private static final String SENDER_ID = "266877390111";
    private static final String TAG = GCMHelper.class.getSimpleName();

    private static boolean isPlayServicesAvailable(final Context context) {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(final Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private static void storeRegistrationId(final Context context,
                                            final String regid) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_REGID, regid)
                .putInt(KEY_APP_VERSION, getAppVersion(context)).commit();
    }

    public static String getSavedRegistrationId(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_REGID, "");
    }

    private static String getRegistrationId(final Context context) {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        final String regid = prefs.getString(KEY_REGID, "");

        if (regid.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs
                .getInt(KEY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }

        return regid;
    }

    /**
     * Handle registrations. If already registered, returns.
     */
    public static void registerIfNotAlreadyDone(final Context context) {
        if (!isPlayServicesAvailable(context)) {
            return;
        }

        final String regid = getRegistrationId(context);
        if (regid.isEmpty()) {
            registerForGCM(context);
        }
    }

    private static void registerForGCM(final Context context) {
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging
                    .getInstance(context);

            final String regid = gcm.register(SENDER_ID);

            if (sendRegistrationIdToBackend(context, regid)) {
                // Persist the regID - no need to register again.
                storeRegistrationId(context, regid);
            }
        }
        catch (IOException ex) {
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }
    }

    public static boolean sendRegistrationIdToBackend(final Context context,
                                                       final String regid) {
        // Need to get an access token first
        final String token = SyncHelper.getAuthToken(context,
                SyncHelper.getSavedAccountName(context));

        if (token == null) {
            return false;
        }

        // token should be good. Transmit
        final EventServer server = SyncHelper.getRESTAdapter();
        final RegId item = new RegId();
        item.reg_id = regid;
        Log.d(TAG, "Registering for GCM");
        try {
            server.registerGCM(token, item);
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
                    break;
                case 404: // No such item, should never happen, programming error
                case 415: // Not proper body, programming error
                case 400: // Bad request; most likely already registered, so IntegryError
                    Log.i(TAG, "Probably already registered");
                    break;
                default: // Default is to consider it a networking problem
                    break;
            }
        }
        return true;
    }
}