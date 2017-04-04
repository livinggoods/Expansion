package com.expansion.lg.kimaru.expansion.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.expansion.lg.kimaru.expansion.fragment.RecruitmentsFragment;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.mzigos.Village;
import com.google.gson.Gson;

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
    public static final String KEY_USERID = "userid";
    public static final String KEY_USER_COUNTRY = "country";


    // Recruitment Details
    public static final String RECRUITMENT_ID = "recruitmentId";
    public static final String RECRUITMENT_NAME = "recruitmentName";
    public static final String RECRUITMENT_DISTRICT = "recruitmentDistrict";
    public static final String RECRUITMENT_SUBCOUNTY = "recruitmentSubcounty";
    public static final String RECRUITMENT_DIVISION = "recruitmentDivision";
    public static final String RECRUITMENT_LAT = "recruitmentLat";
    public static final String RECRUITMENT_LON = "recruitmentLon";
    private static final String IS_RECRUITMENT = "IsRecruitmentSet";



    // Registration Details
    public static final String REGISTRATION_ID = "registrationId";
    public static final String REGISTRATION_NAME = "registrationName";
    public static final String REGISTRATION_PHONE = "registrationPhone";
    public static final String REGISTRATION_GENDER = "registrationGender";
    public static final String REGISTRATION_VILLAGE = "registrationVillage";
    public static final String REGISTRATION_OCCUPATION = "registrationOccupation";
    private static final String IS_REGISTRATION = "IsRegistrationSet";

    //Exam Details
    public static final String EXAM_ID = "examID";
    public static final String EXAM_MATH = "examMath";
    public static final String EXAM_PERSONALITY = "examPersonality";
    public static final String EXAM_ENGLISH = "examEnglish";
    private static final String IS_EXAM = "IsExamSet";

    //Need to save the selected Mapping
    public static final String MAPPING = "mapping";

    //Need to save the selected SubCounty
    public static final String SUBCOUNTY = "subcounty";

    //Need to save the selected Village
    public static final String VILLAGE = "village";


    //interview Details
    private static final String IS_INTERVIEW = "IsInterviewSet";



    public static final String KEY_APPLICANT = "applicant";

    public static final String IS_INITIAL_RUN = "initialRun";


    //constructor
    public SessionManagement (Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveMapping(Mapping mapping){
        Gson gson = new Gson();
        String mappingObject = gson.toJson(mapping);
        editor.putString(MAPPING, mappingObject);
        editor.commit();
    }
    /**
     *
     * Get the stored Mapping
     *
     * */
    public Mapping getSavedMapping (){

        Mapping mapping;
        Gson gson = new Gson();
        String mappingDetails = pref.getString(MAPPING, "");
        mapping = gson.fromJson(mappingDetails, Mapping.class);

        return mapping;
    }


    public void saveSubCounty(SubCounty subCounty){
        Gson gson = new Gson();
        String mappingObject = gson.toJson(subCounty);
        editor.putString(SUBCOUNTY, mappingObject);
        editor.commit();
    }
    /**
     *
     * Get the stored Subcounty
     *
     * */
    public SubCounty getSavedSubCounty (){

        SubCounty subCounty;
        Gson gson = new Gson();
        String subCountyDetails = pref.getString(SUBCOUNTY, "");
        subCounty = gson.fromJson(subCountyDetails, SubCounty.class);

        return subCounty;
    }
    public void saveVillage(Village village){
        Gson gson = new Gson();
        String villageObject = gson.toJson(village);
        editor.putString(VILLAGE, villageObject);
        editor.commit();
    }
    /**
     *
     * Get the stored Viilage
     *
     * */
    public Village getSavedVillage (){

        Village village;
        Gson gson = new Gson();
        String villageDetails = pref.getString(VILLAGE, "");
        village = gson.fromJson(villageDetails, Village.class);
        return village;
    }


    public void createLoginSesstion (String name, String email, Integer userId, String country){
        //storing login values as TRUE
        editor.putBoolean(IS_LOGIN, true);

        //store name in pref
        editor.putString(KEY_NAME, name);

        //put email in pref
        editor.putString(KEY_EMAIL, email);

        //put userid
        editor.putInt(KEY_USERID, userId);

        //put user country
        editor.putString(KEY_USER_COUNTRY, country);

        //commit / Save the values

        editor.commit();

    }


    // set the recruitment ID
    public void createRecruitmentSession (Integer recruitmentID, String name, String district,
                                          String subcounty, String division, String lat, String lon){
        editor.putInt(RECRUITMENT_ID, recruitmentID);
        editor.putString(RECRUITMENT_NAME, name);
        editor.putString(RECRUITMENT_DISTRICT, district);
        editor.putString(RECRUITMENT_SUBCOUNTY, subcounty);
        editor.putString(RECRUITMENT_DIVISION, division);
        editor.putString(RECRUITMENT_LAT, lat);
        editor.putString(RECRUITMENT_LON, lon);
        editor.putBoolean(IS_RECRUITMENT, true);
        editor.commit();
    }

    // set the Registration Session
    public void createRegistrationSession (Integer registrationId, String registrationName,
                                           String registrationPhone, String registrationGender,
                                           String registrationVillage, String registrationOccupation ){

        editor.putInt(REGISTRATION_ID, registrationId);
        editor.putString(REGISTRATION_NAME, registrationName);
        editor.putString(REGISTRATION_PHONE, registrationPhone);
        editor.putString(REGISTRATION_GENDER, registrationGender);
        editor.putString(REGISTRATION_VILLAGE, registrationVillage);
        editor.putString(REGISTRATION_OCCUPATION, registrationOccupation);
        editor.putBoolean(IS_REGISTRATION, true);
        editor.commit();
    }

    // set the Exam Session
    public void createExamSession (Integer examId, Integer examMath,
                                   Integer examPersonality, Integer examEnglish){

        editor.putInt(EXAM_ID, examId);
        editor.putInt(EXAM_MATH, examMath);
        editor.putInt(EXAM_PERSONALITY, examPersonality);
        editor.putInt(EXAM_ENGLISH, examEnglish);
        editor.putBoolean(IS_EXAM, true);
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
     * Check recruitment method will check recruitment status
     * If false it will redirect user to recruitment page
     * Else won't do anything
     * */

    public void checkRecruitment(){
        //check recruitment
        if(!this.isRecruitmentSet()){
            Intent i = new Intent(_context, RecruitmentsFragment.class);
            //Close all other Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //start login
            _context.startActivity(i);
        }
    }


    /**
     * Check recruitment method will check recruitment status
     * If false it will redirect user to recruitment page
     * Else won't do anything
     * */

    public void checkRegistration(){
        //check registration
        if(!this.isRegistrationSet()){
            Intent i = new Intent(_context, RecruitmentsFragment.class);
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
        //userId
        user.put(KEY_USERID, String.valueOf(pref.getInt(KEY_USERID, 0)));

        user.put(KEY_USER_COUNTRY, String.valueOf(pref.getString(KEY_USER_COUNTRY, "ug")));

        //return user
        return user;
    }

    /**
     *
     * Get the stored Recruitment
     *
     * */
    public HashMap<String, String> getRecruitmentSession (){
        HashMap<String, String> recruitment = new HashMap<String, String>();

        recruitment.put(RECRUITMENT_ID, String.valueOf(pref.getInt(RECRUITMENT_ID, 0)));
        recruitment.put(RECRUITMENT_NAME, pref.getString(RECRUITMENT_NAME, null));
        recruitment.put(RECRUITMENT_DISTRICT, pref.getString(RECRUITMENT_DISTRICT, null));
        recruitment.put(RECRUITMENT_SUBCOUNTY, pref.getString(RECRUITMENT_SUBCOUNTY, null));
        recruitment.put(RECRUITMENT_DIVISION, pref.getString(RECRUITMENT_DIVISION, null));
        recruitment.put(RECRUITMENT_LAT, pref.getString(RECRUITMENT_LAT, null));
        recruitment.put(RECRUITMENT_LON, pref.getString(RECRUITMENT_LON, null));

        return recruitment;
    }
    /**
     *
     * Get the stored Registration
     *
     * */
    public HashMap<String, String> getRegistrationSession (){
        HashMap<String, String> registration = new HashMap<String, String>();

        registration.put(REGISTRATION_ID, String.valueOf(pref.getInt(REGISTRATION_ID, 0)));
        registration.put(REGISTRATION_NAME, pref.getString(REGISTRATION_NAME, null));
        registration.put(REGISTRATION_PHONE, pref.getString(REGISTRATION_PHONE, null));
        registration.put(REGISTRATION_GENDER, pref.getString(REGISTRATION_GENDER, null));
        registration.put(REGISTRATION_VILLAGE, pref.getString(REGISTRATION_VILLAGE, null));
        registration.put(REGISTRATION_OCCUPATION, pref.getString(REGISTRATION_OCCUPATION, null));

        return registration;
    }

    /**
     *
     * Get the stored Exam
     *
     * */
    public HashMap<String, Integer> getExamSession (){
        HashMap<String, Integer> exam = new HashMap<String, Integer>();

        exam.put(EXAM_ID, pref.getInt(EXAM_ID, 0));
        exam.put(EXAM_MATH, pref.getInt(EXAM_MATH, 0));
        exam.put(EXAM_PERSONALITY, pref.getInt(EXAM_PERSONALITY, 0));
        exam.put(EXAM_ENGLISH, pref.getInt(EXAM_ENGLISH, 0));

        return exam;
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


    /**
     * Check for Recruitment
     * THis is a boolean flag
     */
    public boolean isRecruitmentSet(){
        return pref.getBoolean(IS_RECRUITMENT, false);
    }

    /**
     * Check for Registration
     * THis is a boolean flag
     */
    public boolean isRegistrationSet(){
        return pref.getBoolean(IS_REGISTRATION, false);
    }


    /**
     * Check for Exam
     * THis is a boolean flag
     */

    public boolean isExamSet(){
        return pref.getBoolean(IS_EXAM, false);
    }

    /**
     * Check for Exam
     * THis is a boolean flag
     */

    public boolean isInterviewSet(){
        return pref.getBoolean(IS_INTERVIEW, false);
    }

    public boolean isInitialRun() {return pref.getBoolean(IS_INITIAL_RUN, true);}
    public void setInitialRun(boolean status) {
        editor.putBoolean(IS_INITIAL_RUN, status);
        editor.commit();
    }
}
