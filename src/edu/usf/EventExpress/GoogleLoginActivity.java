package edu.usf.EventExpress;

import java.io.InputStream;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import org.w3c.dom.Text;

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

    /* Client used to interact with Google APIs */
    private GoogleApiClient mGoogleApiClient;

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

                //session.createLoginSession(personName, email);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
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

}


