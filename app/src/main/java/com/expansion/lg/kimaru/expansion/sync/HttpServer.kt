package com.expansion.lg.kimaru.expansion.sync

import android.content.Context
import android.util.Log
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.mzigos.Exam
import com.expansion.lg.kimaru.expansion.mzigos.Interview
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainer
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.EducationTable
import com.expansion.lg.kimaru.expansion.tables.ExamTable
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable
import com.expansion.lg.kimaru.expansion.tables.InterviewTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable
import com.expansion.lg.kimaru.expansion.tables.MappingTable
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable
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
import com.expansion.lg.kimaru.expansion.tables.UserTable
import com.expansion.lg.kimaru.expansion.tables.VillageTable
import com.expansion.lg.kimaru.expansion.tables.WardTable
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.http.AsyncHttpClient
import com.koushikdutta.async.http.NameValuePair
import com.koushikdutta.async.http.body.JSONObjectBody
import com.koushikdutta.async.http.body.MultipartFormDataBody
import com.koushikdutta.async.http.body.StringBody
import com.koushikdutta.async.http.body.UrlEncodedFormBody
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.koushikdutta.async.http.server.HttpServerRequestCallback

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import junit.framework.Assert.assertNotNull

/**
 * Created by kimaru on 3/22/17.
 */

class HttpServer(internal var context: Context) {
    private val server = AsyncHttpServer()
    private val asyncServer = AsyncServer()

    private val client = AsyncHttpClient(asyncServer)

    var constants: Constants


    var serverPort: Int = 0


    init {
        this.constants = Constants(context)
        this.serverPort = Integer.valueOf(constants.peerServerPort)
    }

    fun startServer() {
        Log.d("Tremap Sync", "Starting server")

        server.get("/$RECRUIRMENT_URL") { request, response ->
            val recruitmentData = RecruitmentTable(context)
            response.send(recruitmentData.recruitmentJson)
        }

        server.get("/$REGISTRATION_URL") { request, response ->
            val registrationTable = RegistrationTable(context)
            response.send(registrationTable.registrationJson)
        }

        server.get("/$EXAM_URL") { request, response ->
            val examTable = ExamTable(context)
            response.send(examTable.examJson)
        }

        server.get("/$INTERVIEW_URL") { request, response ->
            val interviewTable = InterviewTable(context)
            response.send(interviewTable.interviewJson)
        }

        //// postCommunityUnit postChewReferral postLinkFacilities postSubCounties
        server.get("/$SUBCOUNTY_URL") { request, response ->
            val subCountyTable = SubCountyTable(context)
            response.send(subCountyTable.json)
        }

        server.get("/$CU_URL") { request, response ->
            val communityUnitTable = CommunityUnitTable(context)
            response.send(communityUnitTable.json)
        }

        server.get("/$CHEW_REFERRAL_URL") { request, response ->
            val chewReferralTable = ChewReferralTable(context)
            response.send(chewReferralTable.chewReferralJson)
        }

        server.get("/$LINKFACILITY_URL") { request, response ->
            val linkFacilityTable = LinkFacilityTable(context)
            response.send(linkFacilityTable.json)
        }
        //

        server.get("/$USERS_URL") { request, response ->
            val userTable = UserTable(context)
            response.send(userTable.usersJson)
        }

        server.get("/$EDUCATION_URL") { request, response ->
            val educationTable = EducationTable(context)
            response.send(educationTable.educationJson)
        }

        server.get("/$ICCM_COMPONENTS_URL") { request, response ->
            val data = IccmComponentTable(context)
            response.send(data.iccmJson)
        }

        server.get("/$MAPPING_URL") { request, response ->
            val data = MappingTable(context)
            response.send(data.json)
        }

        server.get("/$MOBILIZATION_URL") { request, response ->
            val data = MobilizationTable(context)
            response.send(data.mobilizationJson)
        }

        server.get("/$PARTNERS_ACTIVITY_URL") { request, response ->
            val data = PartnerActivityTable(context)
            response.send(data.json)
        }

        server.get("/$PARTNERS_URL") { request, response ->
            val data = PartnersTable(context)
            response.send(data.json)
        }

        server.get("/$TRAININGS_URL") { request, response ->
            val data = TrainingTable(context)
            response.send(data.json)
        }

        server.get("/$TRAINING_CLASSES_URL") { request, response ->
            val data = TrainingClassTable(context)
            response.send(data.json)
        }

        server.get("/$TRAINING_ROLES_URL") { request, response ->
            val data = TrainingRolesTable(context)
            response.send(data.trainingRoleJson)
        }

        server.get("/$TRAINING_TRAINEES_URL") { request, response ->
            val data = TrainingTraineeTable(context)
            response.send(data.json)
        }

        server.get("/$TRAINING_TRAINERS_URL") { request, response ->
            val data = TrainingTrainersTable(context)
            response.send(data.trainingTrainerJson)
        }

        server.get("/$VILLAGE_URL") { request, response ->
            val data = VillageTable(context)
            response.send(data.json)
        }

        server.get("/$WARDS_URL") { request, response ->
            val data = WardTable(context)
            response.send(data.json)
        }


        //POST METHODS

        server.post("/$RECRUIRMENT_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processRecruitments(json)
                }
                response.send(json)
                // response.send(typeReceived);
            } catch (e: Exception) {
                response.send(e.message)
            }
        }
        server.post("/$REGISTRATION_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processRegistrations(json)
                }
                response.send(json)
                // response.send(typeReceived);
            } catch (e: Exception) {
                response.send(e.message)
            }
        }
        server.post("/$INTERVIEW_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processInterviews(json)
                }
                response.send(json)
                // response.send(typeReceived);
            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$EXAM_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processExams(json)
                }
                response.send(json)
                // response.send(typeReceived);
            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        //postCommunityUnit postChewReferral postLinkFacilities postSubCounties
        server.post("/$CU_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processCus(json)
                }
                response.send(json)
                // response.send(typeReceived);
            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$CHEW_REFERRAL_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processChewReferral(json)
                }
                response.send(json)
                // response.send(typeReceived);
            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$LINKFACILITY_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processLinkFacility(json)
                }
                response.send(json)
                // response.send(typeReceived);
            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$SUBCOUNTY_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processSubCounty(json)
                    response.send("Body not instalce of JSON Object Body")
                } else {
                    processSubCounty(json)
                    response.send("processed")
                }

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$EDUCATION_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processEducation(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$ICCM_COMPONENTS_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processIccmComponent(json)

                }
                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$MAPPING_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processMapping(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$MOBILIZATION_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processMobilization(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$PARTNERS_ACTIVITY_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processPartnerActivity(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$PARTNERS_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processPartners(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$TRAININGS_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processTrainings(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$TRAINING_CLASSES_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processTrainingClasses(json)

                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$TRAINING_ROLES_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processTrainingRoles(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$TRAINING_TRAINEES_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processTrainingTrainees(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$TRAINING_TRAINERS_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processTrainingTrainers(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$VILLAGE_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processVillages(json)
                }

                response.send(json)

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.post("/$WARDS_URL") { request, response ->
            try {
                assertNotNull(request.headers.get("Host"))
                var json = JSONObject()
                if (request.body is JSONObjectBody) {
                    json = (request.body as JSONObjectBody).get()
                    processWards(json)
                    response.send("Body not instalce of JSON Object Body")
                } else {
                    processSubCounty(json)
                    response.send("processed")
                }

            } catch (e: Exception) {
                response.send(e.message)
            }
        }

        server.listen(asyncServer, serverPort)
    }

    private fun processCus(json: JSONObject) {
        Log.d("Tremap Sync", "Processing CUs")
        try {
            val recs = json.getJSONArray(CommunityUnitTable.CU_JSON_ROOT)
            for (x in 0 until recs.length()) {
                CommunityUnitTable(context).CuFromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            Log.d("Tremap Server", "ERR CUs " + e.message)
        }

    }

    private fun processSubCounty(json: JSONObject) {
        Log.d("Tremap Sync", "Processing subcounties")
        try {
            val recs = json.getJSONArray(SubCountyTable.JSON_ROOT)
            for (x in 0 until recs.length()) {
                Log.d("Tremap ", "Received subcounty data " + recs.getJSONObject(x))
                SubCountyTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            Log.d("Tremap Server", "ERR SUBCOUNTY " + e.message)
        }

    }

    private fun processLinkFacility(json: JSONObject) {
        Log.d("Tremap Sync", "Processing Link Facilities")
        try {
            val recs = json.getJSONArray(LinkFacilityTable.JSON_ROOT)
            // Get the array first JSONObject
            for (x in 0 until recs.length()) {
                LinkFacilityTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            Log.d("Tremap Server", "ERR Link Facility " + e.message)
        }

    }

    private fun processChewReferral(json: JSONObject) {
        Log.d("Tremap Server CHEW", "Processing Received Chews")
        Log.d("Tremap Server CHEW", "Received data $json")
        try {
            val recs = json.getJSONArray(ChewReferralTable.JSON_ROOT)
            Log.d("Tremap Server CHEW", "Processing Received Chews")
            // Get the array first JSONObject
            for (x in 0 until recs.length()) {
                ChewReferralTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            Log.d("Tremap Server CHEW", "ERROR " + e.message)
        }

    }

    private fun processRecruitments(json: JSONObject) {
        try {
            // Get the JSONArray recruitments
            val recs = json.getJSONArray(RecruitmentTable.JSON_ROOT)
            // Get the array first JSONObject
            val recruitmentList = ArrayList<Recruitment>()

            for (x in 0 until recs.length()) {
                RecruitmentTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processRegistrations(json: JSONObject) {
        try {
            val recs = json.getJSONArray(RegistrationTable.JSON_ROOT)
            // Get the array first JSONObject
            for (x in 0 until recs.length()) {
                RegistrationTable(context).fromJsonObject(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun processInterviews(json: JSONObject) {
        try {
            // Get the JSONArray recruitments
            val recs = json.getJSONArray(InterviewTable.JSON_ROOT)
            // Get the array first JSONObject
            val recruitmentList = ArrayList<Recruitment>()
            for (x in 0 until recs.length()) {
                InterviewTable(context).fromJson(recs.getJSONObject(x))

            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processExams(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(ExamTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                ExamTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processEducation(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(EducationTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                EducationTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processIccmComponent(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(IccmComponentTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                IccmComponentTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processMapping(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(MappingTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                MappingTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processMobilization(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(MobilizationTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                MobilizationTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processPartnerActivity(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(PartnerActivityTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                PartnerActivityTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processPartners(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(PartnersTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                PartnersTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processTrainings(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(TrainingTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                TrainingTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processTrainingClasses(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(TrainingClassTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                TrainingClassTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processTrainingRoles(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(TrainingRolesTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                TrainingRolesTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processTrainingTrainees(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(TrainingTraineeTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                TrainingTraineeTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processTrainingTrainers(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(TrainingTrainersTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                TrainingTraineeTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processVillages(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(VillageTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                VillageTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    private fun processWards(json: JSONObject) {
        try {

            // Get the JSONArray recruitments
            val recs = json.getJSONArray(WardTable.JSON_ROOT)

            for (x in 0 until recs.length()) {
                WardTable(context).fromJson(recs.getJSONObject(x))
            }
        } catch (e: JSONException) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }

    }

    companion object {

        // Endpoints
        val RECRUIRMENT_URL = "recruitments"
        val REGISTRATION_URL = "registrations"
        val EXAM_URL = "exams"
        val INTERVIEW_URL = "interviews"
        val LINKFACILITY_URL = "link_facilities"
        val CHEW_REFERRAL_URL = "chew_referral"
        val CU_URL = "community_unit"
        val SUBCOUNTY_URL = "subcounty"
        val MAPPING_URL = "mapping"
        val PARISH_URL = "parish"
        val PARTNERS_URL = "partners"
        val PARTNERS_ACTIVITY_URL = "partners/activity"
        val VILLAGE_URL = "villages"
        val USERS_URL = "users"
        val EDUCATION_URL = "education"
        val ICCM_COMPONENTS_URL = "iccm/components"
        val MOBILIZATION_URL = "mobilizations"
        val TRAININGS_URL = "trainings"
        val TRAINING_CLASSES_URL = "training/classes"
        val TRAINING_ROLES_URL = "training/roles"
        val TRAINING_TRAINEES_URL = "training/trainees"
        val TRAINING_TRAINERS_URL = "training/trainers"
        val WARDS_URL = "wards"

        var url: String? = null
    }
}
