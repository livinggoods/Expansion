package com.expansion.lg.kimaru.expansion.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by kimaru on 3/9/17.
 */

public class SessionManagement {
    SharedPreferences pref;
    Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "expansion";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NAME = "name";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_RECRUITMENT = "recruitment";
    public static final String KEY_REGISTRATION = "registration";
    public static final String KEY_APPLICANT = "applicant";


    //constructor
    public SessionManagement (Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSesstion (String name, String email){
        //storing login values as TRUE
        editor.putBoolean(IS_LOGIN, true);

        //store name in pref
        editor.putString(KEY_NAME, name);

        //put email in pref
        editor.putString(KEY_EMAIL, email);

        //commit / Save the values

        editor.commit();

    }

    // set the recruitment ID
    public void createRecruitmentId (Integer recruitmentID){
        editor.putInt(KEY_RECRUITMENT, recruitmentID);
        editor.commit();
    }

    // set the Registration ID
    public void createRegistrationId (Integer registrationID){
        editor.putInt(KEY_RECRUITMENT, registrationID);
        editor.commit();
    }

    // set the applicant ID
    public void createApplicantId (Integer applicantId){
        editor.putInt(KEY_RECRUITMENT, applicantId);
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */

    public void checkLogin(){
        //check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            //Close all other Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //start login
            _context.startActivity(i);
        }
    }

    /**
     * Get the stored session Data
     *
     *
     * */
    public HashMap<String, String> getUserDetails (){
        HashMap<String, String> user = new HashMap<String, String>();
        //UserName
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        //email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        //return user
        return user;
    }

    /**
     *
     * Get the stored Recruitment
     *
     * */
    public Integer getRecruitmentId (){
        return pref.getInt(KEY_RECRUITMENT, 0);
    }
    /**
     *
     * Get the stored Registration
     *
     * */
    public Integer getRegistrationId (){
        return pref.getInt(KEY_REGISTRATION, 0);
    }
    /**
     *
     * Get the stored Applicant
     *
     * */
    public Integer getApplicantId (){
        return pref.getInt(KEY_RECRUITMENT, 0);
    }

    /**
     * When user logs out, clear the sessionData
     */
    public void logoutUser(){
        //cleat all data from SharedPreferences
        editor.clear();
        editor.commit();

        // Redirect user to login after logging out
        Intent i = new Intent(_context, LoginActivity.class);
        //close all other activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //start Login Activity
        _context.startActivity(i);
    }

    /**
     * Check for Login
     * It is reference in checkLogin()
     * THis is a boolean flag
     */
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
