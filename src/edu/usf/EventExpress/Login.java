package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;

/**
 * Created by Varik on 10/11/2014.
 */
public class Login extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

    }

    public void login(View v){

        Intent myIntent= new Intent(this, mainScreen.class);
        startActivity(myIntent);
    }

    public void newUser(View v){

        Intent myIntent = new Intent(this, newUser.class);
        startActivity(myIntent);
    }

    public void googleLoginActivity(View v) {

        Intent myIntent = new Intent(this, GoogleLoginActivity.class);
        startActivity(myIntent);
    }
}