package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

/**
 * Created by Micah on 10/19/2014.
 */
public abstract class BaseActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    /* Request code used to invoke sign in user interactions. */
    protected static final int RC_SIGN_IN = 0;
    /* Client used to interact with Google APIs */
    protected GoogleApiClient mGoogleApiClient;
    /* A flag indicating that a PendingIntent is in progress and prevents
         * us from starting further intents
         */
    protected boolean mIntentInProgress;
    protected ConnectionResult mConnectionResult;
    protected String mAccount;

    public void onConnectionFailed(ConnectionResult result) {
            if (result.hasResolution()) {
                try {
                    result.startResolutionForResult(this, RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    mGoogleApiClient.connect();
                }
            }
        mConnectionResult = result;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                             Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if(responseCode == Activity.RESULT_OK) {
                if(mGoogleApiClient != null) {
                    if (!mGoogleApiClient.isConnecting()) {
                        mGoogleApiClient.connect();
                    }
                }
            }
        }
    }

    public void onConnected(Bundle arg0) {
    }

    public void onConnectionSuspended(int arg0) {
    }
}
