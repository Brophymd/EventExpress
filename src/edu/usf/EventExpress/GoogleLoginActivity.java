package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import edu.usf.EventExpress.provider.user.UserColumns;
import edu.usf.EventExpress.provider.user.UserContentValues;
import edu.usf.EventExpress.provider.user.UserCursor;
import edu.usf.EventExpress.provider.user.UserSelection;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vi Tran on 10/17/2014.
 */
public class GoogleLoginActivity extends Activity implements
    ConnectionCallbacks, OnConnectionFailedListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    //Logcat tag
    private static final String TAG = "GoogleLoginActivity";

    //Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String SENDER_ID = "266877390111";

    /* Client used to interact with Google APIs */
    private GoogleApiClient mGoogleApiClient;

    /* Object for interacting with GCM API */
    GoogleCloudMessaging gcm;
    String regid;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnHome, btnEventMenu, btnEventInvited;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail, welcomeMsg, txtTitle;
    private LinearLayout llProfileLayout;
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_login);
        btnHome = (Button)findViewById(R.id.btn_friend_list);
        btnSignIn = (SignInButton)findViewById(R.id.btn_sign_in);
        btnSignOut = (Button)findViewById(R.id.btn_sign_out);
        btnEventMenu = (Button)findViewById(R.id.btn_event_menu);
        btnEventInvited = (Button)findViewById(R.id.events_invited);
        //btnRevokeAccess = (Button)findViewById(R.id.btn_revoke_access);
        imgProfilePic = (ImageView)findViewById(R.id.imgProfilePic);
        txtName = (TextView)findViewById(R.id.txtName);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        welcomeMsg = (TextView)findViewById(R.id.textView_welcome);
        txtTitle = (TextView)findViewById(R.id.textView_appTitle);
        llProfileLayout = (LinearLayout)findViewById(R.id.llProfile);

        new SessionManager(getApplicationContext());

        // Check for Google Play Services APK
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(getApplicationContext());

            if (regid.isEmpty()) {
                registerInBackground();
            }

            //Button click listeners
            btnSignIn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Signin button clicked

                    signInWithGplus();
                }
            });
            btnSignOut.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Signout button clicked
                    signOutFromGplus();
                }
            });
            /*btnRevokeAccess.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //nothing atm
                }
            });*/

            // Initializing google plus api client
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();
        }
        else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    public void friendlist(View v){
        Intent myIntent= new Intent(this, Friendslist.class);
        startActivity(myIntent);
    }

    public void eventmenu(View v){
        Intent myIntent = new Intent(this, EventMenu.class);
        startActivity(myIntent);
    }

    public void eventsinvited(View v){
        Intent myIntent = new Intent(this, Event_Invitations.class);
        startActivity(myIntent);
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {

        if(mSignInClicked)
            Toast.makeText(this, "User has connected!", Toast.LENGTH_LONG).show();
        mSignInClicked = false;
        // Get user's information
        getProfileInformation();

        // Update the UI after signin
        updateUI(true);


    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            txtTitle.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            //btnRevokeAccess.setVisibility(View.VISIBLE);
            btnEventMenu.setVisibility(View.VISIBLE);
            btnEventInvited.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
            btnHome.setVisibility(View.VISIBLE);
            welcomeMsg.setVisibility(View.VISIBLE);
        } else {
            btnEventMenu.setVisibility(View.GONE);
            btnEventInvited.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
            txtTitle.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            //btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
            btnHome.setVisibility(View.GONE);
            welcomeMsg.setVisibility(View.GONE);
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                /* LOCAL STORAGE TEST */
                String googleId = currentPerson.getId();
                //insert user
                UserContentValues values = new UserContentValues();
                values.putGoogleId(googleId).putUserName(personName);
                Context context = getApplicationContext();
                context.getContentResolver().insert(UserColumns.CONTENT_URI, values.values());
                //query for user and display in log
                UserSelection where = new UserSelection();
                where.userNameNot("superbutts");
                UserCursor user = where.query(context.getContentResolver());
                if (user != null && user.moveToFirst()) {
                    do {
                        String dbGoogleId = user.getGoogleId();
                        String dbUserName = user.getUserName();
                        Log.d(TAG, "dbGoogleId: " + dbGoogleId + ", dbUserName: " + dbUserName);
                    } while (user.moveToNext());
                }
                else {
                    Log.d(TAG, "Failed to query db :(");
                }
                String token = null;
                try {
                    token = GoogleAuthUtil.getToken(getApplicationContext(), email, "oauth2:profile email");
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                } catch (GoogleAuthException e) {
                    Log.e(TAG, e.getMessage());
                }
                /* END LOCAL STORAGE TEST */

                //session.createLoginSession(personName, email);

                Log.d(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                txtName.setText(personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

     /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }

    }

    /**
     * Sign-out from google
     * */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
    }

    /**
     * Revoking access from google
     * */
    /*private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
    }*/

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(GoogleLoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // Persist the regID - no need to register again.
                    storeRegistrationId(getApplicationContext(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, "registerInBackground done: " + msg + "\n");
            }
        }.execute(null, null, null);
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }
}