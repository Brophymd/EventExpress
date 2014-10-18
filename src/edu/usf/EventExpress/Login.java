package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Varik on 10/11/2014.
 */
public class Login extends Activity {

    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        session = new SessionManager(getApplicationContext());
        if(session.isLoggedIn()){
            Intent myIntent= new Intent(this, mainScreen.class);
            startActivity(myIntent);
        }

    }

    public void login(View v){
        String userid = ((EditText)findViewById(R.id.editText4)).toString();

        session.createLoginSession(userid);
        Intent myIntent= new Intent(this, mainScreen.class);
        startActivity(myIntent);
    }

    public void newUser(View v){

        Intent myIntent = new Intent(this, newUser.class);
        startActivity(myIntent);
    }


}