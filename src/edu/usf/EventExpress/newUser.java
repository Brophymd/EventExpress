package edu.usf.EventExpress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Varik on 10/12/2014.                  NOT USED ANYMORE
 */
public class newUser extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuser);
    }

    public void register(View v){
    String userID = ((EditText)findViewById(R.id.editText)).getText().toString();
    String email = ((EditText)findViewById(R.id.editText2)).getText().toString();
    //Toast.makeText(getApplicationContext(), userID, Toast.LENGTH_SHORT).show();
    //Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();

    finish();

    }
}