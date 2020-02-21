package com.ridesharerental.services.Iconstant;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by user113 on 11/28/2017.
 */

public class SessionManager {

    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences file name
    public static final String PREF_NAME = "New_proj";

    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_GCM_ID = "gcmId";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_USER_IMAGE = "userImage";
    public static final String KEY_USER_PASSWORD= "password";
    public static final String KEY_USER_SIGN= "signature_image";
    public static final String KEY_MESSAGE_COUNT= "msg";

    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /*getting the and store the user details*/
    public void set_user_details(String userid, String email,String user_name,String user_image, String password,String signature_image)
    {
        editor.putString(KEY_USER_ID,userid);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_USERNAME,user_name);
        editor.putString(KEY_USER_IMAGE,user_image);
        editor.putString(KEY_USER_PASSWORD,password);
        editor.putString(KEY_USER_SIGN,signature_image);
        editor.commit();

    }

    public void set_profile(String profile)
    {
        editor.putString(KEY_USER_IMAGE,profile);
        editor.commit();
    }

    public void set_msg_count(String count)
    {
        editor.putString(KEY_MESSAGE_COUNT,count);
        editor.commit();
    }

    /*sending the user details for an usage*/
    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, ""));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, ""));
        user.put(KEY_USER_IMAGE, pref.getString(KEY_USER_IMAGE, ""));
        user.put(KEY_USER_PASSWORD, pref.getString(KEY_USER_PASSWORD, ""));
        user.put(KEY_MESSAGE_COUNT, pref.getString(KEY_MESSAGE_COUNT, ""));
        user.put(KEY_USER_SIGN, pref.getString(KEY_USER_SIGN, ""));

        return user;
    }

    public void session_logout()
    {
        editor.clear();
        editor.commit();
    }
}
