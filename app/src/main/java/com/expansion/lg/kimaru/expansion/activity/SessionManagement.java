package com.expansion.lg.kimaru.expansion.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.expansion.lg.kimaru.expansion.fragment.RecruitmentsFragment;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
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
    public static final String COMMUNITY_UNIT = "community_unit";
    public static final String MOBILIZATION = "mobilization";
    public static final String LINK_FACILITY = "link_facility";


    // Recruitment Details
    public static final String RECRUITMENT = "recruitment";
    public static final String IS_RECRUITMENT = "isRecruitment";

    //parish
    public static final String PARISH = "parish";
    public static final String IS_PARISH = "isParish";

    // Registration Details
    public static final String REGISTRATION = "registration";
    public static final String IS_REGISTRATION = "isRegistration";


    //Exam Details
    public static final String EXAM = "exam";
    public static final String IS_EXAM = "isExam";


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

    public void saveRegistration(Registration registration){
        Gson gson = new Gson();
        String registrationObject = gson.toJson(registration);
        editor.putString(REGISTRATION, registrationObject);
        editor.putBoolean(IS_REGISTRATION, true);
        editor.commit();
    }
    /**
     *
     * Get the stored recruitment
     *
     * */
    public Recruitment getSavedRecruitment (){

        Recruitment recruitment;
        Gson gson = new Gson();
        String recruitmentDetails = pref.getString(RECRUITMENT, "");
        recruitment = gson.fromJson(recruitmentDetails, Recruitment.class);

        return recruitment;
    }

    public void saveExam(Exam exam){
        Gson gson = new Gson();
        String examObject = gson.toJson(exam);
        editor.putString(EXAM, examObject);
        editor.putBoolean(IS_EXAM, true);
        editor.commit();
    }
    /**
     *
     * Get the stored exam
     *
     * */
    public Exam getSavedExam (){

        Exam exam;
        Gson gson = new Gson();
        String examDetails = pref.getString(EXAM, "");
        exam = gson.fromJson(examDetails, Exam.class);

        return exam;
    }

    public void saveParish(Parish parish){
        Gson gson = new Gson();
        String parishObject = gson.toJson(parish);
        editor.putString(PARISH, parishObject);
        editor.putBoolean(IS_PARISH, true);
        editor.commit();
    }

    /**
     *
     * Get the stored Parish
     *
     * */
    public Parish getSavedParish (){

        Parish parish;
        Gson gson = new Gson();
        String parishDetails = pref.getString(PARISH, "");
        parish = gson.fromJson(parishDetails, Parish.class);

        return parish;
    }
    public void saveLinkFacility(LinkFacility linkFacility){
        Gson gson = new Gson();
        String linkFacilityObject = gson.toJson(linkFacility);
        editor.putString(LINK_FACILITY, linkFacilityObject);
        editor.commit();
    }
    public LinkFacility getSavedLinkFacility(){
        LinkFacility linkFacility;
        Gson gson = new Gson();
        String linkFacilityDetails = pref.getString(LINK_FACILITY, "");
        linkFacility = gson.fromJson(linkFacilityDetails, LinkFacility.class);
        return  linkFacility;
    }


    public void saveRecruitment(Recruitment recruitment){
        Gson gson = new Gson();
        String recruitmentObject = gson.toJson(recruitment);
        editor.putString(RECRUITMENT, recruitmentObject);
        editor.putBoolean(IS_RECRUITMENT, true);
        editor.commit();
    }
    /**
     *
     * Get the stored Mapping
     *
     * */
    public Registration getSavedRegistration (){

        Registration registration;
        Gson gson = new Gson();
        String registrationDetails = pref.getString(REGISTRATION, "");
        registration = gson.fromJson(registrationDetails, Registration.class);

        return registration;
    }

    public void saveCommunityUnit(CommunityUnit communityUnit){
        Gson gson = new Gson();
        String cuObject = gson.toJson(communityUnit);
        editor.putString(COMMUNITY_UNIT, cuObject);
        editor.commit();
    }
    /**
     *
     * Get the stored CommunityUnit
     *
     * */
    public CommunityUnit getSavedCommunityUnit (){

        CommunityUnit communityUnit;
        Gson gson = new Gson();
        String cuDetails = pref.getString(COMMUNITY_UNIT, "");
        communityUnit = gson.fromJson(cuDetails, CommunityUnit.class);

        return communityUnit;
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


    public void saveMobilization(Mobilization mobilization){
        Gson gson = new Gson();
        String mobilizationObject = gson.toJson(mobilization);
        editor.putString(MOBILIZATION, mobilizationObject);
        editor.commit();
    }
    /**
     *
     * Get the stored Mobilization
     *
     * */
    public Mobilization getSavedMobilization (){

        Mobilization mobilization;
        Gson gson = new Gson();
        String mobilizationDetails = pref.getString(MOBILIZATION, "");
        mobilization = gson.fromJson(mobilizationDetails, Mobilization.class);
        return mobilization;
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

    public void saveCloudUrl(String cloudUrl){
        editor.putString(CLOUD_URL, cloudUrl);
        editor.commit();
    }

    public String getCloudUrl (){
        String cloudUrl = pref.getString(CLOUD_URL, HttpServer.SERVER_URL);
        return cloudUrl;
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
