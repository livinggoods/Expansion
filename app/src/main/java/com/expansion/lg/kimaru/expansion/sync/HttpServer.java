package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainer;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.MappingTable;
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable;
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable;
import com.expansion.lg.kimaru.expansion.tables.PartnersTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.expansion.lg.kimaru.expansion.tables.TrainingClassTable;
import com.expansion.lg.kimaru.expansion.tables.TrainingRolesTable;
import com.expansion.lg.kimaru.expansion.tables.TrainingTable;
import com.expansion.lg.kimaru.expansion.tables.TrainingTraineeTable;
import com.expansion.lg.kimaru.expansion.tables.TrainingTrainersTable;
import com.expansion.lg.kimaru.expansion.tables.UserTable;
import com.expansion.lg.kimaru.expansion.tables.VillageTable;
import com.expansion.lg.kimaru.expansion.tables.WardTable;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by kimaru on 3/22/17.
 */

public class HttpServer {
    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer asyncServer = new AsyncServer();

    Context context;

    private AsyncHttpClient client = new AsyncHttpClient(asyncServer);

    public Constants constants;


    public int serverPort;
    public static final String RECRUIRMENT_URL = "recruitments";
    public static final String REGISTRATION_URL = "registrations";
    public static final String EXAM_URL = "exams";
    public static final String INTERVIEW_URL = "interviews";
    public static final String LINKFACILITY_URL = "link_facilities";
    public static final String CHEW_REFERRAL_URL = "chew_referral";
    public static final String CU_URL = "community_unit";
    public static final String SUBCOUNTY_URL = "subcounty";
    public static final String MAPPING_URL = "mapping";
    public static final String PARISH_URL = "parish";
    public static final String PARTNERS_URL = "partners";
    public static final String PARTNERS_ACTIVITY_URL = "partners/activity";
    public static final String VILLAGE_URL = "villages";

    public static String url;


    public HttpServer(Context context){
        this.context = context;
        this.constants = new Constants(context);
        this.serverPort = Integer.valueOf(constants.getPeerServerPort());
    }

    public void startServer(){
        Log.d("Tremap Sync", "Starting server");

        server.get("/"+RECRUIRMENT_URL, (request, response) -> {
            RecruitmentTable recruitmentData = new RecruitmentTable(context);
            response.send(recruitmentData.getRecruitmentJson());
        });

        server.get("/"+REGISTRATION_URL, (request, response) -> {
            RegistrationTable registrationTable = new RegistrationTable(context);
            response.send(registrationTable.getRegistrationJson());
        });

        server.get("/"+EXAM_URL, (request, response) -> {
            ExamTable examTable = new ExamTable(context);
            response.send(examTable.getExamJson());
        });

        server.get("/"+INTERVIEW_URL, (request, response) -> {
            InterviewTable interviewTable = new InterviewTable(context);
            response.send(interviewTable.getInterviewJson());
        });

        //// postCommunityUnit postChewReferral postLinkFacilities postSubCounties
        server.get("/"+SUBCOUNTY_URL, (request, response) -> {
            SubCountyTable subCountyTable = new SubCountyTable(context);
            response.send(subCountyTable.getJson());
        });

        server.get("/"+CU_URL, (request, response) -> {
            CommunityUnitTable communityUnitTable = new CommunityUnitTable(context);
            response.send(communityUnitTable.getJson());
        });

        server.get("/"+CHEW_REFERRAL_URL, (request, response) -> {
            ChewReferralTable chewReferralTable = new ChewReferralTable(context);
            response.send(chewReferralTable.getChewReferralJson());
        });

        server.get("/"+LINKFACILITY_URL, (request, response) -> {
            LinkFacilityTable linkFacilityTable = new LinkFacilityTable(context);
            response.send(linkFacilityTable.getJson());
        });
        //

        server.get("/users", (request, response) -> {
            UserTable userTable = new UserTable(context);
            response.send(userTable.getUsersJson());
        });

        server.get("/education", (request, response) -> {
            EducationTable educationTable = new EducationTable(context);
            response.send(educationTable.getEducationJson());
        });

        server.get("/iccm/components", (request, response) -> {
            IccmComponentTable data = new IccmComponentTable(context);
            response.send(data.getIccmJson());
        });

        server.get("/mappings", (request, response) -> {
            MappingTable data = new MappingTable(context);
            response.send(data.getJson());
        });

        server.get("/mobilizations", (request, response) -> {
            MobilizationTable data = new MobilizationTable(context);
            response.send(data.getMobilizationJson());
        });

        server.get("/partner/activities", (request, response) -> {
            PartnerActivityTable data = new PartnerActivityTable(context);
            response.send(data.getJson());
        });

        server.get("/partners", (request, response) -> {
            PartnersTable data = new PartnersTable(context);
            response.send(data.getJson());
        });

        server.get("/trainings", (request, response) -> {
            TrainingTable data = new TrainingTable(context);
            response.send(data.getJson());
        });

        server.get("/training/classes", (request, response) -> {
            TrainingClassTable data = new TrainingClassTable(context);
            response.send(data.getJson());
        });

        server.get("/training/roles", (request, response) -> {
            TrainingRolesTable data = new TrainingRolesTable(context);
            response.send(data.getTrainingRoleJson());
        });

        server.get("/training/trainees", (request, response) -> {
            TrainingTraineeTable data = new TrainingTraineeTable(context);
            response.send(data.getJson());
        });

        server.get("/training/trainers", (request, response) -> {
            TrainingTrainersTable data = new TrainingTrainersTable(context);
            response.send(data.getTrainingTrainerJson());
        });

        server.get("/villages", (request, response) -> {
            VillageTable data = new VillageTable(context);
            response.send(data.getJson());
        });

        server.get("/wards", (request, response) -> {
            WardTable data = new WardTable(context);
            response.send(data.getJson());
        });



        //POST METHODS

        server.post("/"+RECRUIRMENT_URL, (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processRecruitments(json);
                }
                response.send(json);
                // response.send(typeReceived);
            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });
        server.post("/"+REGISTRATION_URL, (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processRegistrations(json);
                }
                response.send(json);
                // response.send(typeReceived);
            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });
        server.post("/"+INTERVIEW_URL, (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processInterviews(json);
                }
                response.send(json);
                // response.send(typeReceived);
            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/"+EXAM_URL, (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processExams(json);
                }
                response.send(json);
                // response.send(typeReceived);
            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        //postCommunityUnit postChewReferral postLinkFacilities postSubCounties
        server.post("/"+CU_URL, (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processCus(json);
                }
                response.send(json);
                // response.send(typeReceived);
            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/"+CHEW_REFERRAL_URL, (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processChewReferral(json);
                }
                response.send(json);
                // response.send(typeReceived);
            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/"+LINKFACILITY_URL, (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processLinkFacility(json);
                }
                response.send(json);
                // response.send(typeReceived);
            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/"+SUBCOUNTY_URL, (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processSubCounty(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/education", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processEducation(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/iccm/components", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processIccmComponent(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/mapping", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processMapping(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/mobilization", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processMobilization(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/partner/activity", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processPartnerActivity(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/partners", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processPartners(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/trainings", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processTrainings(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/training/classes", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processTrainingClasses(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/training/roles", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processTrainingRoles(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/training/trainees", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processTrainingTrainees(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/training/trainers", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processTrainingTrainers(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/villages", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processVillages(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.post("/wards", (request, response) -> {
            try {
                assertNotNull(request.getHeaders().get("Host"));
                JSONObject json = new JSONObject();
                if (request.getBody() instanceof JSONObjectBody) {
                    json = ((JSONObjectBody)request.getBody()).get();
                    processWards(json);
                    response.send("Body not instalce of JSON Object Body");
                }else{
                    processSubCounty(json);
                    response.send("processed");
                }

            }
            catch (Exception e) {
                response.send(e.getMessage());
            }
        });

        server.listen(asyncServer, serverPort);
    }

    private void processCus(JSONObject json){
        Log.d("Tremap Sync", "Processing CUs");
        try{
            JSONArray recs = json.getJSONArray(CommunityUnitTable.CU_JSON_ROOT);
            for (int x = 0; x < recs.length(); x++){
                new CommunityUnitTable(context).CuFromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            Log.d("Tremap Server", "ERR CUs "+e.getMessage());
        }
    }

    private void processSubCounty(JSONObject json){
        Log.d("Tremap Sync", "Processing subcounties");
        try{
            JSONArray recs = json.getJSONArray(SubCountyTable.JSON_ROOT);
            for (int x = 0; x < recs.length(); x++){
                Log.d("Tremap ", "Received subcounty data "+recs.getJSONObject(x));
                new SubCountyTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            Log.d("Tremap Server", "ERR SUBCOUNTY "+e.getMessage());
        }
    }

    private void processLinkFacility(JSONObject json){
        Log.d("Tremap Sync", "Processing Link Facilities");
        try{
            JSONArray recs = json.getJSONArray(LinkFacilityTable.JSON_ROOT);
            // Get the array first JSONObject
            for (int x = 0; x < recs.length(); x++){
                new LinkFacilityTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            Log.d("Tremap Server", "ERR Link Facility "+e.getMessage());
        }
    }
    private void processChewReferral(JSONObject json){
        Log.d("Tremap Server CHEW", "Processing Received Chews");
        Log.d("Tremap Server CHEW", "Received data "+ json);
        try{
            JSONArray recs = json.getJSONArray(ChewReferralTable.JSON_ROOT);
            Log.d("Tremap Server CHEW", "Processing Received Chews");
            // Get the array first JSONObject
            for (int x = 0; x < recs.length(); x++){
                new ChewReferralTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            Log.d("Tremap Server CHEW", "ERROR "+ e.getMessage());
        }
    }

    private void processRecruitments(JSONObject json){
        try{
            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(RecruitmentTable.JSON_ROOT);
            // Get the array first JSONObject
            List<Recruitment> recruitmentList = new ArrayList<Recruitment>();

            for (int x = 0; x < recs.length(); x++){
                new RecruitmentTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void processRegistrations(JSONObject json){
        try{
            JSONArray recs = json.getJSONArray(RegistrationTable.JSON_ROOT);
            // Get the array first JSONObject
            for (int x = 0; x < recs.length(); x++){
                new RegistrationTable(context).fromJsonObject(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void processInterviews(JSONObject json){
        try{
            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(InterviewTable.JSON_ROOT);
            // Get the array first JSONObject
            List<Recruitment> recruitmentList = new ArrayList<Recruitment>();
            for (int x = 0; x < recs.length(); x++){
                new InterviewTable(context).fromJson(recs.getJSONObject(x));

            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void processExams(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(ExamTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new ExamTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processEducation(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(EducationTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new EducationTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processIccmComponent(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(IccmComponentTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new IccmComponentTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processMapping(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(MappingTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new MappingTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processMobilization(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(MobilizationTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new MobilizationTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processPartnerActivity(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(PartnerActivityTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new PartnerActivityTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processPartners(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(PartnersTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new PartnersTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainings(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new TrainingTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainingClasses(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingClassTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new TrainingClassTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainingRoles(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingRolesTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new TrainingRolesTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainingTrainees(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingTraineeTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new TrainingTraineeTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainingTrainers(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingTrainersTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new TrainingTraineeTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processVillages(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(VillageTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new VillageTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processWards(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(WardTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++){
                new WardTable(context).fromJson(recs.getJSONObject(x));
            }
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
