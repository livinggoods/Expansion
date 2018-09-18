package com.expansion.lg.kimaru.expansion.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.expansion.lg.kimaru.expansion.activity.SessionManagement.Companion.CLOUD_URL

import com.expansion.lg.kimaru.expansion.fragment.RecruitmentsFragment
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.Exam
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization
import com.expansion.lg.kimaru.expansion.mzigos.Parish
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.mzigos.Village
import com.expansion.lg.kimaru.expansion.sync.HttpServer
import com.google.gson.Gson

import java.util.HashMap

/**
 * Created by kimaru on 3/9/17.
 */

class SessionManagement//constructor
(internal var _context: Context) {
    internal var pref: SharedPreferences
    internal var editor: Editor

    internal var PRIVATE_MODE = 0
    /**
     *
     * Get the stored recruitment
     *
     */


    val savedRecruitment: Recruitment
        get() {

            val recruitment: Recruitment
            val gson = Gson()
            val recruitmentDetails = pref.getString(RECRUITMENT, "")
            recruitment = gson.fromJson(recruitmentDetails, Recruitment::class.java)

            return recruitment
        }
    /**
     *
     * Get the stored exam
     *
     */
    val savedExam: Exam
        get() {

            val exam: Exam
            val gson = Gson()
            val examDetails = pref.getString(EXAM, "")
            exam = gson.fromJson(examDetails, Exam::class.java)

            return exam
        }

    /**
     *
     * Get the stored Parish
     *
     */
    val savedParish: Parish
        get() {

            val parish: Parish
            val gson = Gson()
            val parishDetails = pref.getString(PARISH, "")
            parish = gson.fromJson(parishDetails, Parish::class.java)

            return parish
        }
    val savedLinkFacility: LinkFacility
        get() {
            val linkFacility: LinkFacility
            val gson = Gson()
            val linkFacilityDetails = pref.getString(LINK_FACILITY, "")
            linkFacility = gson.fromJson(linkFacilityDetails, LinkFacility::class.java)
            return linkFacility
        }
    /**
     *
     * Get the stored Mapping
     *
     */
    val savedRegistration: Registration
        get() {

            val registration: Registration
            val gson = Gson()
            val registrationDetails = pref.getString(REGISTRATION, "")
            registration = gson.fromJson(registrationDetails, Registration::class.java)

            return registration
        }
    /**
     *
     * Get the stored CommunityUnit
     *
     */
    val savedCommunityUnit: CommunityUnit?
        get() {

            val communityUnit: CommunityUnit
            val gson = Gson()
            val cuDetails = pref.getString(COMMUNITY_UNIT, "")
            if (cuDetails!!.equals("", ignoreCase = true)) {
                return null
            } else {
                communityUnit = gson.fromJson(cuDetails, CommunityUnit::class.java)
                return communityUnit
            }
        }
    /**
     *
     * Get the stored Mapping
     *
     */
    val savedMapping: Mapping
        get() {

            val mapping: Mapping
            val gson = Gson()
            val mappingDetails = pref.getString(MAPPING, "")
            mapping = gson.fromJson(mappingDetails, Mapping::class.java)

            return mapping
        }
    /**
     *
     * Get the stored Subcounty
     *
     */
    val savedSubCounty: SubCounty
        get() {

            val subCounty: SubCounty
            val gson = Gson()
            val subCountyDetails = pref.getString(SUBCOUNTY, "")
            subCounty = gson.fromJson(subCountyDetails, SubCounty::class.java)

            return subCounty
        }
    /**
     *
     * Get the stored Viilage
     *
     */
    val savedVillage: Village
        get() {

            val village: Village
            val gson = Gson()
            val villageDetails = pref.getString(VILLAGE, "")
            village = gson.fromJson(villageDetails, Village::class.java)
            return village
        }
    /**
     *
     * Get the stored Mobilization
     *
     */
    val savedMobilization: Mobilization
        get() {

            val mobilization: Mobilization
            val gson = Gson()
            val mobilizationDetails = pref.getString(MOBILIZATION, "")
            mobilization = gson.fromJson(mobilizationDetails, Mobilization::class.java)
            return mobilization
        }


    val trainingEndpoint: String
        get() = pref.getString(API_TRAINING, "")

    val cloudUrl: String
        get() = pref.getString(CLOUD_URL, "https://expansion.lg-apps.com")


    /**
     * Get the stored session Data
     *
     *
     */
    //UserName
    //email
    //userId
    //return user
    val userDetails: HashMap<String, String>
        get() {
            val user = HashMap<String, String>()
            user[KEY_NAME] = pref.getString(KEY_NAME, null)
            user[KEY_EMAIL] = pref.getString(KEY_EMAIL, null)
            user[KEY_USERID] = pref.getInt(KEY_USERID, 0).toString()

            user[KEY_USER_COUNTRY] = pref.getString(KEY_USER_COUNTRY, "ug").toString()
            return user
        }

    /**
     * Check for Login
     * It is reference in checkLogin()
     * THis is a boolean flag
     */
    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGIN, false)


    /**
     * Check for Recruitment
     * THis is a boolean flag
     */
    val isRecruitmentSet: Boolean
        get() = pref.getBoolean(IS_RECRUITMENT, false)

    /**
     * Check for Registration
     * THis is a boolean flag
     */
    val isRegistrationSet: Boolean
        get() = pref.getBoolean(IS_REGISTRATION, false)


    /**
     * Check for Exam
     * THis is a boolean flag
     */

    val isExamSet: Boolean
        get() = pref.getBoolean(IS_EXAM, false)

    /**
     * Check for Exam
     * THis is a boolean flag
     */

    val isInterviewSet: Boolean
        get() = pref.getBoolean(IS_INTERVIEW, false)

    var isInitialRun: Boolean
        get() = pref.getBoolean(IS_INITIAL_RUN, true)
        set(status) {
            editor.putBoolean(IS_INITIAL_RUN, status)
            editor.commit()
        }
    val apiPrefix: String
        get() = pref.getString(API_PREFIX, "api")
    val apiVersion: String
        get() = pref.getString(API_VERSION, "v1")
    val apiSuffix: String
        get() = pref.getString(API_SUFFIX, "sync")
    val peerServer: String
        get() = pref.getString(PEER_SERVER, "http://192.168.43.1")
    val peerServerPort: String
        get() = pref.getString(PEER_SERVER_PORT, "8090")
    val apiTrainingEndpoint: String
        get() = pref.getString(API_TRAINING_ENDPOINT, "trainings")

    val isInitialDataSynced: Boolean
        get() = pref.getBoolean("initialDataLoaded", false)

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun saveRegistration(registration: Registration) {
        val gson = Gson()
        val registrationObject = gson.toJson(registration)
        editor.putString(REGISTRATION, registrationObject)
        editor.putBoolean(IS_REGISTRATION, true)
        editor.commit()
    }

    fun saveExam(exam: Exam) {
        val gson = Gson()
        val examObject = gson.toJson(exam)
        editor.putString(EXAM, examObject)
        editor.putBoolean(IS_EXAM, true)
        editor.commit()
    }

    fun saveParish(parish: Parish) {
        val gson = Gson()
        val parishObject = gson.toJson(parish)
        editor.putString(PARISH, parishObject)
        editor.putBoolean(IS_PARISH, true)
        editor.commit()
    }

    fun saveLinkFacility(linkFacility: LinkFacility) {
        val gson = Gson()
        val linkFacilityObject = gson.toJson(linkFacility)
        editor.putString(LINK_FACILITY, linkFacilityObject)
        editor.commit()
    }


    fun saveRecruitment(recruitment: Recruitment) {
        val gson = Gson()
        val recruitmentObject = gson.toJson(recruitment)
        editor.putString(RECRUITMENT, recruitmentObject)
        editor.putBoolean(IS_RECRUITMENT, true)
        editor.commit()
    }

    fun saveCommunityUnit(communityUnit: CommunityUnit) {
        val gson = Gson()
        val cuObject = gson.toJson(communityUnit)
        editor.putString(COMMUNITY_UNIT, cuObject)
        editor.commit()
    }

    fun saveMapping(mapping: Mapping) {
        val gson = Gson()
        val mappingObject = gson.toJson(mapping)
        editor.putString(MAPPING, mappingObject)
        editor.commit()
    }


    fun saveSubCounty(subCounty: SubCounty) {
        val gson = Gson()
        val mappingObject = gson.toJson(subCounty)
        editor.putString(SUBCOUNTY, mappingObject)
        editor.commit()
    }

    fun saveVillage(village: Village) {
        val gson = Gson()
        val villageObject = gson.toJson(village)
        editor.putString(VILLAGE, villageObject)
        editor.commit()
    }


    fun saveMobilization(mobilization: Mobilization) {
        val gson = Gson()
        val mobilizationObject = gson.toJson(mobilization)
        editor.putString(MOBILIZATION, mobilizationObject)
        editor.commit()
    }


    fun createLoginSesstion(name: String, email: String, userId: Int?, country: String, cookie: String) {
        //storing login values as TRUE
        editor.putBoolean(IS_LOGIN, true)

        //store name in pref
        editor.putString(KEY_NAME, name)

        //put email in pref
        editor.putString(KEY_EMAIL, email)

        //put userid
        editor.putInt(KEY_USERID, userId!!)

        //put user country
        editor.putString(KEY_USER_COUNTRY, country)

        editor.putString(KEY_TOKEN, cookie)

        //commit / Save the values

        editor.commit()

    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */

    fun checkLogin() {
        //check login status
        if (!this.isLoggedIn) {
            val i = Intent(_context, LoginActivity::class.java)
            //Close all other Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //start login
            _context.startActivity(i)
        }
    }

    fun saveTrainingEndpoint(endpoint: String) {
        editor.putString(API_TRAINING, endpoint)
        editor.commit()
    }

    /**
     * Check recruitment method will check recruitment status
     * If false it will redirect user to recruitment page
     * Else won't do anything
     */

    fun checkRecruitment() {
        //check recruitment
        if (!this.isRecruitmentSet) {
            val i = Intent(_context, RecruitmentsFragment::class.java)
            //Close all other Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            //start login
            _context.startActivity(i)
        }
    }


    /**
     * Check recruitment method will check recruitment status
     * If false it will redirect user to recruitment page
     * Else won't do anything
     */

    fun checkRegistration() {
        //check registration
        if (!this.isRegistrationSet) {
            val i = Intent(_context, RecruitmentsFragment::class.java)
            //Close all other Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            //start loginadmin
            _context.startActivity(i)
        }
    }

    fun saveCloudUrl(cloudUrl: String) {
        editor.putString(CLOUD_URL, cloudUrl)
        editor.commit()
    }


    /**
     * When user logs out, clear the sessionData
     */
    fun logoutUser() {
        //cleat all data from SharedPreferences
        editor.clear()
        editor.commit()

        // Redirect user to login after logging out
        val i = Intent(_context, LoginActivity::class.java)
        //close all other activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        //start Login Activity
        _context.startActivity(i)
    }


    fun saveApiPrefix(apiPrefix: String) {
        editor.putString(API_PREFIX, apiPrefix)
        editor.commit()
    }

    fun saveApiVersion(apiVersion: String) {
        editor.putString(API_VERSION, apiVersion)
        editor.commit()
    }

    fun saveApiSuffix(apiSuffix: String) {
        editor.putString(API_SUFFIX, apiSuffix)
        editor.commit()
    }

    fun savePeerServer(peerServer: String) {
        editor.putString(PEER_SERVER, peerServer)
        editor.commit()
    }

    fun savePeerServerPort(peerServerPort: String) {
        editor.putString(PEER_SERVER_PORT, peerServerPort)
        editor.commit()
    }

    fun saveApiTrainingEndpoint(apiTrainingEndpoint: String) {
        editor.putString(API_TRAINING_ENDPOINT, apiTrainingEndpoint)
        editor.commit()
    }

    fun flagSynced(synced: Boolean) {
        editor.putBoolean("initialDataLoaded", synced)
        editor.commit()
    }

    companion object {

        private val PREF_NAME = "expansion"
        private val IS_LOGIN = "IsLoggedIn"
        val KEY_NAME = "name"
        val KEY_EMAIL = "email"
        val KEY_USERID = "userid"
        val KEY_TOKEN = "token"
        val KEY_USER_COUNTRY = "country"
        val COMMUNITY_UNIT = "community_unit"
        val MOBILIZATION = "mobilization"
        val LINK_FACILITY = "link_facility"

        val CLOUD_URL = "cloud_url"
        // Recruitment Details
        val RECRUITMENT = "recruitment"
        val IS_RECRUITMENT = "isRecruitment"

        //parish
        val PARISH = "parish"
        val IS_PARISH = "isParish"

        // Registration Details
        val REGISTRATION = "registration"
        val IS_REGISTRATION = "isRegistration"


        //Exam Details
        val EXAM = "exam"
        val IS_EXAM = "isExam"


        //Need to save the selected Mapping
        val MAPPING = "mapping"

        //Need to save the selected SubCounty
        val SUBCOUNTY = "subcounty"

        //Need to save the selected Village
        val VILLAGE = "village"

        //interview Details
        private val IS_INTERVIEW = "IsInterviewSet"


        val KEY_APPLICANT = "applicant"

        val IS_INITIAL_RUN = "initialRun"

        val API_TRAINING = "api_training"
        val API_TRAINING_ENDPOINT = "api_training_endpoint"
        val API_PREFIX = "api_prefix"
        val API_VERSION = "api_version"
        val API_SUFFIX = "api_suffix"
        val PEER_SERVER = "peer_server"
        val PEER_SERVER_PORT = "peer_server_port"
    }
}
