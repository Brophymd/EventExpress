package edu.usf.EventExpress;

import android.content.SharedPreferences;
import android.content.Context;


/**
 * Created by Varik on 10/16/2014.
 */
public class SessionManager {
    SharedPreferences myPref;
    SharedPreferences.Editor editor;
    private static final String IS_LOGIN = "Is_Logged_In";
    private static final String KEY_USERID = "UserID";
    //private static final String KEY_EMAIL = "Email";
    private static final String PREF_NAME = "EventExpress";
    int PRIVATE_MODE = 0;
    Context _context;

    public SessionManager(Context context){
        this._context = context;
        myPref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = myPref.edit();
    }

    public void createLoginSession(String userid){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERID, userid);
        //editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return myPref.getBoolean(IS_LOGIN,false);
    }

    public void logOut(){
        editor.clear();
        editor.commit();
    }

    public String getUserID(){
        return myPref.getString(KEY_USERID, null);
    }

    /*public String getEmail(){
        return myPref.getString(KEY_EMAIL, null);
    }*/
}
