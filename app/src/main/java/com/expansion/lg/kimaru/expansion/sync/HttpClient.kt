package com.expansion.lg.kimaru.expansion.sync

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.util.Log

/**
 * Created by kimaru on 7/19/17.
 */


import com.expansion.lg.kimaru.expansion.mzigos.Exam
import com.expansion.lg.kimaru.expansion.mzigos.Interview
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.mzigos.TrainingClass
import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainee
import com.expansion.lg.kimaru.expansion.mzigos.Village
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.other.WifiState
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.EducationTable
import com.expansion.lg.kimaru.expansion.tables.ExamTable
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable
import com.expansion.lg.kimaru.expansion.tables.InterviewTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable
import com.expansion.lg.kimaru.expansion.tables.MappingTable
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable
import com.expansion.lg.kimaru.expansion.tables.ParishTable
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable
import com.expansion.lg.kimaru.expansion.tables.PartnersTable
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable
import com.expansion.lg.kimaru.expansion.tables.TrainingClassTable
import com.expansion.lg.kimaru.expansion.tables.TrainingRolesTable
import com.expansion.lg.kimaru.expansion.tables.TrainingTable
import com.expansion.lg.kimaru.expansion.tables.TrainingTraineeTable
import com.expansion.lg.kimaru.expansion.tables.TrainingTrainersTable
import com.expansion.lg.kimaru.expansion.tables.VillageTable
import com.expansion.lg.kimaru.expansion.tables.WardTable
import com.koushikdutta.async.http.AsyncHttpClient
import com.koushikdutta.async.http.AsyncHttpPost
import com.koushikdutta.async.http.body.JSONObjectBody

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

class HttpClient(internal var context: Context) {
    internal var isRunning = false
    internal var constants: Constants

    private val url: String

    init {
        constants = Constants(context)
        url = constants.peerServer + ":" + constants.peerServerPort
    }

    // Implement two methods:
    // POST = To send all records to the Peer Server
    // GET = Get all records that are in the Peer Server


    //GET METHODS

    fun startClient() {
        val handler = Handler()
        Log.d("Tremap Sync", "Client Starting")
        val combinedTimer = Timer()
        val combinedTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    val registrationUrl: String
                    val recruitmentUrl: String
                    val examUrl: String
                    val interviewUrl: String
                    val linkFacilityUrl: String
                    val chewReferralUrl: String
                    val communityUnitUrl: String
                    val wifiState = WifiState(context)
                    Log.d("Tremap Sync", "Checking Wifi connection")
                    if (wifiState.canReachPeerServer()) {
                        Log.d("Tremap Sync", "Wifi Connected")

                        //Link Facility
                        linkFacilityUrl = url + "/" + HttpServer.LINKFACILITY_URL
                        Log.d("Tremap Sync", "Link Facility Process")
                        ProcessLinkFacility().execute(linkFacilityUrl)

                        //Community Url
                        communityUnitUrl = url + "/" + HttpServer.CU_URL
                        Log.d("Tremap Sync", "Community Unit Process")
                        ProcessCommunityUnit().execute(communityUnitUrl)

                        //chew referral
                        chewReferralUrl = url + "/" + HttpServer.CHEW_REFERRAL_URL
                        Log.d("Tremap Sync", "Chew Referral Process")
                        ProcessChewReferral().execute(chewReferralUrl)

                        //Recruitments
                        recruitmentUrl = url + "/" + HttpServer.RECRUIRMENT_URL
                        Log.d("Tremap Sync", "Recruitments Process")
                        ProcessRecruitment().execute(recruitmentUrl)

                        //registrations
                        registrationUrl = url + "/" + HttpServer.REGISTRATION_URL
                        Log.d("Tremap Sync", "Registrations Process")
                        ProcessRegistrations().execute(registrationUrl)

                        //Interviews
                        interviewUrl = url + "/" + HttpServer.INTERVIEW_URL
                        Log.d("Tremap Sync", "Interviews Process")
                        ProcessInterviews().execute(interviewUrl)

                        //Exams
                        examUrl = url + "/" + HttpServer.EXAM_URL
                        Log.d("Tremap Sync", "Exams Process")
                        ProcessExams().execute(examUrl)

                        val educationUrl = url + "/" + HttpServer.EDUCATION_URL
                        ProcessEducation().execute(educationUrl)

                        val iccmUrl = url + "/" + HttpServer.ICCM_COMPONENTS_URL
                        ProcessIccmComponents().execute(iccmUrl)

                        val mappingUrl = url + "/" + HttpServer.MAPPING_URL
                        ProcessMapping().execute(mappingUrl)

                        val mobilizationUrl = url + "/" + HttpServer.MOBILIZATION_URL
                        ProcessMobilization().execute(mobilizationUrl)

                        val parishUrl = url + "/" + HttpServer.PARTNERS_URL
                        ProcessParish().execute(parishUrl)

                        val partnerActivityUrl = url + "/" + HttpServer.PARTNERS_ACTIVITY_URL
                        ProcessPartnerActivity().execute(partnerActivityUrl)

                        val partnerUrl = url + "/" + HttpServer.PARTNERS_URL
                        ProcessPartners().execute(partnerUrl)

                        val subCountyUrl = url + "/" + HttpServer.SUBCOUNTY_URL
                        ProcessSubCounty().execute(subCountyUrl)

                        val trainingClassUrl = url + "/" + HttpServer.TRAINING_CLASSES_URL
                        ProcessTrainingClasses().execute(trainingClassUrl)

                        val trainingRolesUrl = url + "/" + HttpServer.TRAINING_ROLES_URL
                        ProcessTrainingRoles().execute(trainingRolesUrl)

                        val trainingUrl = url + "/" + HttpServer.TRAININGS_URL
                        ProcessTrainings().execute(trainingUrl)

                        val trainingTraineeUrl = url + "/" + HttpServer.TRAINING_TRAINERS_URL
                        ProcessTrainingTrainees().execute(trainingTraineeUrl)

                        val trainingTrainersUrl = url + "/" + HttpServer.TRAINING_TRAINERS_URL
                        ProcessTrainingTrainers().execute(trainingTrainersUrl)

                        val villageUrl = url + "/" + HttpServer.VILLAGE_URL
                        ProcessVillages().execute(villageUrl)

                        val wardUrl = url + "/" + HttpServer.WARDS_URL
                        ProcessWards().execute(wardUrl)

                    } else {
                        Log.d("Tremap Sync Err", "WiFi not Connected")
                    }
                }
            }
        }
        combinedTimer.schedule(combinedTask, 0, (60 * 1000).toLong())

        val postRecordsTimer = Timer()
        val postRecordsTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    val wifiState = WifiState(context)
                    Log.d("Tremap Sync", "Posting Records Start")
                    if (wifiState.canReachPeerServer()) {
                        Log.d("Tremap", "=====================+++++++++++++++++++=============")
                        Log.d("Tremap Sync", "Post my records")
                        PostMyRecords().execute()
                    } else {
                        Log.d("Tremap Sync Err", "WiFi not Connected")
                    }
                }
            }
        }
        postRecordsTimer.schedule(postRecordsTask, 0, (60 * 1000).toLong())
    }

    private inner class ProcessCommunityUnit : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            Log.d("Tremap Sync", "Community Unit Async task")
            Log.d("Tremap Sync", "Community Unit URL $urlString")
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            Log.d("Tremap Sync", "Community Unit Async task started")
            if (stream != null) {
                Log.d("Tremap Sync", "Community Unit Stream is not null ")
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(CommunityUnitTable.CU_JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        Log.d("Tremap Sync", "Community Unit gotten, try saving")
                        CommunityUnitTable(context).CuFromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    Log.d("Tremap Sync ERR", "Community Unit " + e.message)
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessEducation : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(EducationTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        EducationTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessIccmComponents : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(IccmComponentTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        IccmComponentTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessMapping : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(MappingTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        MappingTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessMobilization : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(MobilizationTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        MobilizationTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessParish : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(ParishTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        ParishTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessPartnerActivity : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(PartnerActivityTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        PartnerActivityTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessPartners : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(PartnersTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        PartnersTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessSubCounty : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(SubCountyTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        SubCountyTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessTrainingClasses : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(TrainingClassTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        TrainingClassTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessTrainingRoles : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(TrainingRolesTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        TrainingRolesTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessTrainings : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(TrainingTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        TrainingTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessTrainingTrainees : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(TrainingTraineeTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        TrainingTraineeTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessTrainingTrainers : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(TrainingTrainersTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        TrainingTrainersTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessVillages : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(VillageTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        VillageTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessWards : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(WardTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        WardTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessLinkFacility : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            Log.d("Tremap Sync", "Link Facility Async task")
            Log.d("Tremap Sync", "Link Facility URL $urlString")

            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            Log.d("Tremap Sync", "Link Facility Async task")

            if (stream != null) {
                Log.d("Tremap Sync", "Link Facility stream is not null")
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)

                    // Get the JSONArray recruitments
                    val recs = reader.getJSONArray(LinkFacilityTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        LinkFacilityTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    Log.d("Tremap Sync ERR", "Link Facility " + e.message)
                }

            } else {
                Log.d("Tremap Sync", "Link Facility stream is null")
            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }

    private inner class ProcessChewReferral : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            Log.d("Tremap Sync", "CHEW referral Async task")
            Log.d("Tremap Sync", "CHEW referral URL $urlString")
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            Log.d("Tremap Sync", "CHEW Async task started")
            if (stream != null) {
                Log.d("Tremap Sync", "CHEW Referral stream is not null")
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)

                    // Get the JSONArray recruitments
                    val recs = reader.getJSONArray(ChewReferralTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        ChewReferralTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    Log.d("Tremap Sync ERR", "CHEW Referral " + e.message)
                }

            } else {
                Log.d("Tremap Sync", "CHEW Referral stream is null")
            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        }
    }


    private inner class ProcessExams : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]

            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)

                    // Get the JSONArray recruitments
                    val recs = reader.getJSONArray(ExamTable.JSON_ROOT)

                    for (x in 0 until recs.length()) {
                        ExamTable(context).fromJson(recs.getJSONObject(x))
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end


    private inner class ProcessRegistrations : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]

            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)

                    val recs = reader.getJSONArray(RegistrationTable.JSON_ROOT)
                    // Get the array first JSONObject

                    for (x in 0 until recs.length()) {
                        RegistrationTable(context).fromJsonObject(recs.getJSONObject(x))
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end


    private inner class ProcessRecruitment : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]

            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)

                    // Get the JSONArray recruitments
                    val recs = reader.getJSONArray(RecruitmentTable.JSON_ROOT)
                    // Get the array first JSONObject
                    Log.d("Tremap", "Processing REcruitmnt ")
                    for (x in 0 until recs.length()) {
                        RecruitmentTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    Log.d("Tremap", "Processing error " + e.message)
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end


    private inner class ProcessInterviews : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]

            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    val reader = JSONObject(stream)
                    // Get the JSONArray recruitments
                    val recs = reader.getJSONArray(InterviewTable.JSON_ROOT)
                    // Get the array first JSONObject
                    val recruitmentList = ArrayList<Recruitment>()
                    for (x in 0 until recs.length()) {
                        InterviewTable(context).fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            // Return the data from specified url
            return stream
        }

        override fun onPostExecute(stream: String) {
            // if statement end
        } // onPostExecute() end
    }


    /**
     * Now methods to post the records
     *
     */

    private inner class PostMyRecords : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String {
            Log.d("Tremap POST", "Posting the records in the device to peer server")
            try {
                Log.d("Tremap Sync LOG", "+++++++++========++++++++===+++=+===+=+======+++++")
                val status = postRecruitments()
                Log.d("Tremap Sync LOG", "Recruitments Status " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERROR", "postRecruitments " + e.message)
            }

            try {
                val status = postRegistrations()
                Log.d("Tremap Sync LOG", "Registrations Status " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERR", "Error in posting registrations " + e.message)
            }

            try {
                val status = postExams()
                Log.d("Tremap Sync LOG", "Exams Status " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERR", "Error in posting exams " + e.message)
            }

            try {
                val status = postInterviews()
                Log.d("Tremap Sync LOG", "Interviews Status " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERR", "Error in posting interviews " + e.message)
            }

            try {
                val status = postCommunityUnit()
                Log.d("Tremap Sync LOG", "Community Unit Status " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERR", "Error in posting community units " + e.message)
            }

            try {
                val status = postChewReferral()
                Log.d("Tremap Sync LOG", "CHEW REFs Status " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERR", "Error in posting CHEWs " + e.message)
            }

            try {
                val status = postLinkFacilities()
                Log.d("Tremap Sync LOG", "Link Facility Status " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERR", "Error in posting Link Facilities " + e.message)
            }

            try {
                val status = postEducation()
                Log.d("Tremap Sync LOG", "Education " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERR", "Error in posting Link Facilities " + e.message)
            }

            try {
                val status = postIccmComponents()
                Log.d("Tremap Sync LOG", "Education " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERR", "Error in posting Link Facilities " + e.message)
            }

            try {
                val status = postMapping()
                Log.d("Tremap Sync LOG", "Education " + status!!)
            } catch (e: Exception) {
                Log.d("Tremap Sync ERR", "Error in posting Link Facilities " + e.message)
            }

            try {
                val status = postMobilization()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postParsh()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postPartnerActivity()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postPartners()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postSubCounty()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postTrainingClasses()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postTrainingRoles()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postTrainings()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postTrainingTrainees()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postTrainingTrainers()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postVillages()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val status = postWards()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }
    }


    /**
     *
     * @param json: Json object to send to server, this is the body of the POST message
     * @param JsonRoot: The root string of the JSON
     * @param apiEndpoint: The endpoint for the API
     * @return: String Response from the server
     * @throws Exception: Exception if error occurs
     */
    @Throws(Exception::class)
    private fun peerClientServer(json: JSONObject, JsonRoot: String, apiEndpoint: String): String {
        //  get the server URL
        val p = AsyncHttpPost(url + "/"
                + apiEndpoint)
        p.body = JSONObjectBody(json)
        val ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get()
        return ret.getString(JsonRoot)
    }

    @Throws(Exception::class)
    fun postRegistrations(): String? {
        Log.d("Tremap POST", "Posting Registrations to peer server")
        var postResults: String?
        val registrationTable = RegistrationTable(context)
        try {
            postResults = this.peerClientServer(registrationTable.registrationJson,
                    RegistrationTable.JSON_ROOT, HttpServer.REGISTRATION_URL)
            Log.d("Tremap Sync", "Registrations posted")
        } catch (e: Exception) {
            Log.d("ERROR : Sync Regs", e.message)
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postInterviews(): String? {
        Log.d("Tremap POST", "Posting Interviews to peer server")
        var postResults: String?
        val interviewTable = InterviewTable(context)
        try {
            postResults = this.peerClientServer(interviewTable.interviewJson,
                    InterviewTable.JSON_ROOT, HttpServer.INTERVIEW_URL)
            Log.d("Tremap Sync", "Interviews posted")
        } catch (e: Exception) {
            Log.d("ERROR : Sync Inter", e.message)
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postExams(): String? {
        Log.d("Tremap POST", "Posting Exams to peer server")
        var postResults: String?
        val examTable = ExamTable(context)
        try {
            postResults = this.peerClientServer(examTable.examJson, ExamTable.JSON_ROOT,
                    HttpServer.EXAM_URL)
            Log.d("Tremap Sync", "Exams posted")
        } catch (e: Exception) {
            Log.d("ERROR : Sync Exams", e.message)
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postRecruitments(): String? {
        Log.d("Tremap POST", "Posting Recruitments to peer server")
        var postResults: String?
        val recruitmentTable = RecruitmentTable(context)
        try {
            postResults = this.peerClientServer(recruitmentTable.recruitmentJson,
                    RecruitmentTable.JSON_ROOT, HttpServer.RECRUIRMENT_URL)
            Log.d("Tremap Sync", "Recruitments posted")
        } catch (e: Exception) {
            Log.d("ERROR : Sync Recs", e.message)
            postResults = null
        }

        return postResults
    }

    /**
     *
     * Added new methods to sync new data fragments
     */


    @Throws(Exception::class)
    fun postLinkFacilities(): String? {
        Log.d("Tremap POST", "Posting Link Facilities to peer server")
        var postResults: String?
        val linkFacilityTable = LinkFacilityTable(context)
        try {
            postResults = this.peerClientServer(linkFacilityTable.json,
                    LinkFacilityTable.JSON_ROOT, HttpServer.LINKFACILITY_URL)
            Log.d("Tremap Sync", "Link Facilities posted")
        } catch (e: Exception) {
            Log.d("ERR: Sync LinkFacility", e.message)
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postChewReferral(): String? {
        Log.d("Tremap POST", "Posting Chews/referrals to peer server")
        var postResults: String?
        val chewReferralTable = ChewReferralTable(context)
        try {
            postResults = this.peerClientServer(chewReferralTable.chewReferralJson,
                    ChewReferralTable.JSON_ROOT, HttpServer.CHEW_REFERRAL_URL)
            Log.d("Tremap Sync", "CHEWS posted")
        } catch (e: Exception) {
            Log.d("ERR: Sync chews", e.message)
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postCommunityUnit(): String? {
        Log.d("Tremap POST", "Posting Community Units to peer server")
        var postResults: String?
        val communityUnitTable = CommunityUnitTable(context)
        try {
            postResults = this.peerClientServer(communityUnitTable.json,
                    CommunityUnitTable.CU_JSON_ROOT, HttpServer.CU_URL)
            Log.d("Tremap Sync", "Community Units posted")
        } catch (e: Exception) {
            Log.d("ERR: Sync CUs", e.message)
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postEducation(): String? {
        Log.d("Tremap POST", "Posting Education to peer server")
        var postResults: String?
        val table = EducationTable(context)
        try {
            postResults = this.peerClientServer(table.educationJson,
                    EducationTable.JSON_ROOT, HttpServer.EDUCATION_URL)
            Log.d("Tremap Sync", "Education records posted")
        } catch (e: Exception) {
            Log.d("ERR: Sync Educationss", e.message)
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postIccmComponents(): String? {
        var postResults: String?
        val table = IccmComponentTable(context)
        try {
            postResults = this.peerClientServer(table.iccmJson,
                    IccmComponentTable.JSON_ROOT, HttpServer.ICCM_COMPONENTS_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postMapping(): String? {
        var postResults: String?
        val table = MappingTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    MappingTable.JSON_ROOT, HttpServer.MAPPING_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postMobilization(): String? {
        var postResults: String?
        val table = MobilizationTable(context)
        try {
            postResults = this.peerClientServer(table.mobilizationJson,
                    MobilizationTable.JSON_ROOT, HttpServer.MOBILIZATION_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postParsh(): String? {
        var postResults: String?
        val table = ParishTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    ParishTable.JSON_ROOT, HttpServer.PARISH_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postPartnerActivity(): String? {
        var postResults: String?
        val table = PartnerActivityTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    PartnerActivityTable.JSON_ROOT, HttpServer.PARTNERS_ACTIVITY_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postPartners(): String? {
        var postResults: String?
        val table = PartnersTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    PartnersTable.JSON_ROOT, HttpServer.PARTNERS_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postSubCounty(): String? {
        var postResults: String?
        val table = SubCountyTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    SubCountyTable.JSON_ROOT, HttpServer.SUBCOUNTY_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postTrainingClasses(): String? {
        var postResults: String?
        val table = TrainingClassTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    TrainingClassTable.JSON_ROOT, HttpServer.TRAINING_CLASSES_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postTrainingRoles(): String? {
        var postResults: String?
        val table = TrainingRolesTable(context)
        try {
            postResults = this.peerClientServer(table.trainingRoleJson,
                    TrainingRolesTable.JSON_ROOT, HttpServer.TRAINING_ROLES_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postTrainings(): String? {
        var postResults: String?
        val table = TrainingTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    TrainingTable.JSON_ROOT, HttpServer.TRAININGS_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postTrainingTrainees(): String? {
        var postResults: String?
        val table = TrainingTraineeTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    TrainingTraineeTable.JSON_ROOT, HttpServer.TRAINING_TRAINEES_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postTrainingTrainers(): String? {
        var postResults: String?
        val table = TrainingTrainersTable(context)
        try {
            postResults = this.peerClientServer(table.trainingTrainerJson,
                    TrainingTrainersTable.JSON_ROOT, HttpServer.TRAINING_TRAINERS_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postVillages(): String? {
        var postResults: String?
        val table = VillageTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    VillageTable.JSON_ROOT, HttpServer.VILLAGE_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }

    @Throws(Exception::class)
    fun postWards(): String? {
        var postResults: String?
        val table = WardTable(context)
        try {
            postResults = this.peerClientServer(table.json,
                    WardTable.JSON_ROOT, HttpServer.WARDS_URL)
        } catch (e: Exception) {
            Log.d("SYNC ERROR", e.message)
            e.printStackTrace()
            postResults = null
        }

        return postResults
    }


    //THE FOLLOWING SYNC METHODS HELPS US TO UPLOAD DATA TO THE CLOUD (https://expansion.lg-apps.com)

    // Callback for the API
    @Throws(Exception::class)
    private fun syncClient(json: JSONObject, apiEndpoint: String): String {
        //  get the server URL
        Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        Log.d("Tremap", "URL: " + Constants(context).apiServer + "/" + apiEndpoint)
        Log.d("Tremap", "URL: " + json.toString())
        Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")

        val p = AsyncHttpPost(Constants(context).apiServer + "/" + apiEndpoint)
        p.body = JSONObjectBody(json)
        val ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get()
        //        return ret.getString(expectedJsonRoot);
        Log.d("RESULTS : Sync", ret.toString())
        Log.d("API  : Url", Constants(context).apiServer + apiEndpoint)
        return ret.toString()
    }

    fun syncRecruitments() {
        var syncResults: String?
        val recruitmentTable = RecruitmentTable(context)
        try {
            syncResults = this.syncClient(recruitmentTable.recruitmentToSyncAsJson,
                    HttpServer.RECRUIRMENT_URL)
        } catch (e: Exception) {
            syncResults = null
        }

        if (syncResults != null) {
            processSyncedRecruitments(syncResults)
        }
    }

    private fun processSyncedRecruitments(stream: String) {
        try {

            val reader = JSONObject(stream)

            // Get the JSONArray recruitments
            val recs = reader.getJSONArray("status")
            val recruitmentTable = RecruitmentTable(context)

            for (x in 0 until recs.length()) {
                val recruitment = recruitmentTable.getRecruitmentById(
                        recs.getJSONObject(x).getString("id"))
                recruitment!!.synced = if (recs.getJSONObject(x).getString("status") === "ok") 1 else 0
                // update recruitment
                recruitmentTable.addData(recruitment)
            }
        } catch (e: Exception) {
        }

    }

    fun syncRegistrations() {
        var syncResults: String?
        val registrationTable = RegistrationTable(context)
        try {
            syncResults = this.syncClient(registrationTable.registrationToSyncAsJson,
                    HttpServer.REGISTRATION_URL)
        } catch (e: Exception) {
            syncResults = null
        }

        if (syncResults != null) {
            processSyncedRegistrations(syncResults)
        }
    }

    private fun processSyncedRegistrations(stream: String) {
        try {

            val reader = JSONObject(stream)

            // Get the JSONArray recruitments
            val recs = reader.getJSONArray("status")
            val registrationTable = RegistrationTable(context)

            for (x in 0 until recs.length()) {
                val registration = registrationTable.getRegistrationById(
                        recs.getJSONObject(x).getString("id"))
                registration!!.synced = if (recs.getJSONObject(x).getString("status") === "ok") 1 else 0
                // update recruitment
                registrationTable.addData(registration)
            }
        } catch (e: Exception) {
        }

    }

    fun syncInterviews() {
        var syncResults: String?
        val interviewTable = InterviewTable(context)
        try {
            syncResults = this.syncClient(interviewTable.interviewsToSyncAsJson,
                    HttpServer.INTERVIEW_URL)
        } catch (e: Exception) {
            syncResults = null
        }

        if (syncResults != null) {
            processSyncedInterviews(syncResults)
        }
    }

    private fun processSyncedInterviews(stream: String) {
        try {

            val reader = JSONObject(stream)

            // Get the JSONArray
            val recs = reader.getJSONArray("status")
            val interviewTable = InterviewTable(context)

            for (x in 0 until recs.length()) {
                val interview = interviewTable.getInterviewById(
                        recs.getJSONObject(x).getString("id"))
                interview!!.synced = if (recs.getJSONObject(x).getString("status") === "ok") 1 else 0
                // update recruitment
                interviewTable.addData(interview)
            }
        } catch (e: Exception) {
        }

    }

    fun syncExams() {
        var syncResults: String?
        val examTable = ExamTable(context)
        try {
            syncResults = this.syncClient(examTable.examsToSyncAsJson,
                    HttpServer.EXAM_URL)
        } catch (e: Exception) {
            syncResults = null
        }

        if (syncResults != null) {
            processSyncedExams(syncResults)
        }
    }

    private fun processSyncedExams(stream: String) {
        try {

            val reader = JSONObject(stream)

            // Get the JSONArray
            val recs = reader.getJSONArray("status")
            val examTable = ExamTable(context)

            for (x in 0 until recs.length()) {
                val exam = examTable.getExamById(
                        recs.getJSONObject(x).getString("id"))
                exam!!.synced = if (recs.getJSONObject(x).getString("status") === "ok") 1 else 0
                // update recruitment
                examTable.addData(exam)
            }
        } catch (e: Exception) {
        }

    }

    fun syncCommunityUnits() {
        var syncResults: String?
        val communityUnitTable = CommunityUnitTable(context)
        try {
            syncResults = this.syncClient(communityUnitTable.json,
                    HttpServer.CU_URL)
        } catch (e: Exception) {
            syncResults = null
        }

    }

    fun syncReferrals() {
        var syncResults: String?
        val chewReferralTable = ChewReferralTable(context)
        try {
            syncResults = this.syncClient(chewReferralTable.chewReferralJson,
                    HttpServer.CHEW_REFERRAL_URL)
        } catch (e: Exception) {
            syncResults = null
        }

    }

    fun syncParishes() {
        var syncResults: String?
        val parishTable = ParishTable(context)
        Log.e("Parish JSON", parishTable.json.toString())
        try {
            syncResults = this.syncClient(parishTable.json,
                    HttpServer.PARISH_URL)
        } catch (e: Exception) {
            syncResults = null
        }

    }


    fun syncPartners() {
        var syncResults: String?
        val partnersTable = PartnersTable(context)
        try {
            syncResults = this.syncClient(partnersTable.json,
                    HttpServer.PARTNERS_URL)
        } catch (e: Exception) {
            syncResults = null
        }

    }

    fun syncPartnersCommunityUnits() {
        var syncResults: String?
        val partnerActivityTable = PartnerActivityTable(context)
        try {
            syncResults = this.syncClient(partnerActivityTable.json,
                    HttpServer.PARTNERS_ACTIVITY_URL)
        } catch (e: Exception) {
            syncResults = null
        }

    }


    fun syncMapping() {
        var syncResults: String?
        val mappingTable = MappingTable(context)
        try {
            syncResults = this.syncClient(mappingTable.json,
                    HttpServer.MAPPING_URL)
        } catch (e: Exception) {
            syncResults = null
        }

    }

    fun syncVillages() {
        val syncResults: String
        val villageTable = VillageTable(context)
        try {
            syncResults = this.syncClient(villageTable.json,
                    HttpServer.VILLAGE_URL)
        } catch (e: Exception) {
            Log.d("Tremap", "================ERROR PUSHING VILLAGES =================")
            Log.d("Tremap", e.message)
        }

    }

    fun syncLinkFacilities() {
        var syncResults: String?
        val linkFacilityTable = LinkFacilityTable(context)
        try {
            syncResults = this.syncClient(linkFacilityTable.json,
                    HttpServer.LINKFACILITY_URL)
        } catch (e: Exception) {
            syncResults = null
        }

    }
}