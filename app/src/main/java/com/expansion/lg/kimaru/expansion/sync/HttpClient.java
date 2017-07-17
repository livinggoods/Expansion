package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.other.WifiState;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.body.JSONObjectBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by kimaru on 4/7/17.
 */

public class HttpClient {
    Context context;
    private static String url = HttpServer.SERVER_URL+":"+HttpServer.SERVER_PORT;

    public HttpClient(Context context){
        this.context = context;
    }


    // POST MY RECORDS
    public String testPostJsonObject(JSONObject json) throws Exception {
        AsyncHttpPost p = new AsyncHttpPost(HttpServer.SERVER_URL+":"+HttpServer.SERVER_PORT+"/echo");
        p.setBody(new JSONObjectBody(json));
        JSONObject ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get();
        return ret.getString(RegistrationTable.JSON_ROOT);
    }

    public String postRegistrations() throws Exception {
        String postResults;
        RegistrationTable registrationTable = new RegistrationTable(context);
        try {
            postResults = this.ApiClient(registrationTable.getRegistrationJson(),
                    RegistrationTable.JSON_ROOT, HttpServer.REGISTRATION_URL);
        } catch (Exception e){
            Log.d("ERROR : Sync Regs", e.getMessage());
            postResults = null;
        }
        return postResults;
    }
    public String postInterviews() throws Exception {
        String postResults;
        InterviewTable interviewTable = new InterviewTable(context);
        try {
            postResults = this.ApiClient(interviewTable.getInterviewJson(),
                    InterviewTable.JSON_ROOT, HttpServer.INTERVIEW_URL);
        } catch (Exception e){
            Log.d("ERROR : Sync Inter", e.getMessage());
            postResults = null;
        }
        return postResults;
    }
    public String postExams() throws Exception {
        String postResults;
        ExamTable examTable = new ExamTable(context);
        try {
            postResults = this.ApiClient(examTable.getExamJson(), ExamTable.JSON_ROOT,
                    HttpServer.EXAM_URL);
        } catch (Exception e){
            Log.d("ERROR : Sync Exams", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    public String postRecruitments() throws Exception {
        String postResults;
        RecruitmentTable recruitmentTable = new RecruitmentTable(context);
        try {
            postResults = this.ApiClient(recruitmentTable.getRecruitmentJson(),
                    RecruitmentTable.JSON_ROOT, HttpServer.RECRUIRMENT_URL);
        } catch (Exception e){
            Log.d("ERROR : Sync Recs", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    /**
     *
     * Added new methods to sync new data fragments
     */

    public String postSubCounties() throws Exception {
        String postResults;
        SubCountyTable subCountyTable = new SubCountyTable(context);
        try {
            postResults = this.ApiClient(subCountyTable.getJson(),
                    SubCountyTable.JSON_ROOT, HttpServer.SUBCOUNTY_URL);
        } catch (Exception e){
            Log.d("ERR: Sync subCounty", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    public String postLinkFacilities() throws Exception {
        String postResults;
        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(context);
        try {
            postResults = this.ApiClient(linkFacilityTable.getJson(),
                    LinkFacilityTable.JSON_ROOT, HttpServer.LINKFACILITY_URL);
        } catch (Exception e){
            Log.d("ERR: Sync LinkFacility", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    public String postChewReferral() throws Exception {
        String postResults;
        ChewReferralTable chewReferralTable = new ChewReferralTable(context);
        try {
            postResults = this.ApiClient(chewReferralTable.getChewReferralJson(),
                    ChewReferralTable.JSON_ROOT, HttpServer.CHEW_REFERRAL_URL);
        } catch (Exception e){
            Log.d("ERR: Sync LinkFacility", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    public String postCommunityUnit() throws Exception {
        String postResults;
        CommunityUnitTable communityUnitTable = new CommunityUnitTable(context);
        try {
            postResults = this.ApiClient(communityUnitTable.getJson(),
                    CommunityUnitTable.CU_JSON_ROOT, HttpServer.CU_URL);
        } catch (Exception e){
            Log.d("ERR: Sync LinkFacility", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    /**
     *
     * End
     */

    private String ApiClient(JSONObject json, String JsonRoot, String apiEndpoint) throws Exception {
        //  get the server URL
        AsyncHttpPost p = new AsyncHttpPost(HttpServer.SERVER_URL+":"+HttpServer.SERVER_PORT+"/"
                +apiEndpoint);
        p.setBody(new JSONObjectBody(json));
        JSONObject ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get();
        return ret.getString(JsonRoot);
    }


    // Callback for the API
    private String syncClient(JSONObject json, String apiEndpoint) throws Exception {
        //  get the server URL
        AsyncHttpPost p = new AsyncHttpPost(Constants.API_SERVER+apiEndpoint);
        p.setBody(new JSONObjectBody(json));
        JSONObject ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get();
//        return ret.getString(expectedJsonRoot);
        Log.d("RESULTS : Sync", ret.toString());
        Log.d("API  : Url", Constants.API_SERVER+apiEndpoint);
        return ret.toString();
    }

    // GET RECORDS FROM THE SERVER
    public void startClient(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String registrationUrl, recruitmentUrl, examUrl, interviewUrl,
                                linkFacilityUrl, chewReferralUrl, communityUnitUrl, subCountyUrl;
                        WifiState wifiState = new WifiState(context);
                        if (wifiState.canReachPeerServer()) {
                            //Subcounty
                            subCountyUrl = url + "/" + HttpServer.SUBCOUNTY_URL;
                            new ProcessSubCounty().execute(subCountyUrl);

                            //Link Favilities
                            linkFacilityUrl = url + "/" + HttpServer.LINKFACILITY_URL;
                            new ProcessLinkFacility().execute(linkFacilityUrl);

                            //chews
                            chewReferralUrl = url + "/" + HttpServer.CHEW_REFERRAL_URL;
                            new ProcessChewReferral().execute(chewReferralUrl);

                            //CUs
                            communityUnitUrl = url + "/" + HttpServer.CU_URL;
                            new ProcessCommunityUnit().execute(communityUnitUrl);

                            //Recruitments
                            recruitmentUrl = url + "/" + HttpServer.RECRUIRMENT_URL;
                            new ProcessRecruitment().execute(recruitmentUrl);

                            //registrations
                            registrationUrl = url + "/" + HttpServer.REGISTRATION_URL;
                            new ProcessRegistrations().execute(registrationUrl);

                            //Interviews
                            interviewUrl = url + "/" + HttpServer.INTERVIEW_URL;
                            new ProcessInterviews().execute(interviewUrl);

                            //Exams
                            examUrl = url + "/" + HttpServer.EXAM_URL;
                            new ProcessExams().execute(examUrl);

                            // Poll for records
                            new PollRecords().execute();

                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 60*1000);
    }
    private class PollRecords extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            try {
                String status = postRecruitments();
            } catch (Exception e) {}

            try {
                String status = postRegistrations();
            } catch (Exception e) {}

            try {
                String status = postExams();
            } catch (Exception e) {}

            try {
                String status = postInterviews();
            } catch (Exception e) {}

            try {
                String status = postCommunityUnit();
            } catch (Exception e) {}

            try {
                String status = postChewReferral();
            } catch (Exception e) {}

            try {
                String status = postLinkFacilities();
            } catch (Exception e) {}

            try {
                String status = postSubCounties();
            } catch (Exception e) {}

            return "";
        }
    }

    private class ProcessExams extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(ExamTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new ExamTable(context).fromJson(recs.getJSONObject(x));
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
             // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end


    private class ProcessRegistrations extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    JSONArray recs = reader.getJSONArray(RegistrationTable.JSON_ROOT);
                    // Get the array first JSONObject

                    for (int x = 0; x < recs.length(); x++){
                        new RegistrationTable(context).fromJsonObject(recs.getJSONObject(x));
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
            return stream;
        }

        protected void onPostExecute(String stream){
             // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end


    private class ProcessRecruitment extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(RecruitmentTable.JSON_ROOT);
                    // Get the array first JSONObject
                    List<Recruitment> recruitmentList = new ArrayList<Recruitment>();


                    for (int x = 0; x < recs.length(); x++){
                        new RecruitmentTable(context).fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
             // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end

    private class ProcessSubCounty extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(SubCountyTable.JSON_ROOT);
                    for (int x = 0; x < recs.length(); x++){
                        new SubCountyTable(context).fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            // if statement end
        } // onPostExecute() end
    }

    private class ProcessInterviews extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(InterviewTable.JSON_ROOT);
                    // Get the array first JSONObject
                    List<Recruitment> recruitmentList = new ArrayList<Recruitment>();
                    for (int x = 0; x < recs.length(); x++){
                        new InterviewTable(context).fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
             // if statement end
        } // onPostExecute() end
    }

    private class ProcessLinkFacility extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(LinkFacilityTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new LinkFacilityTable(context).fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            // if statement end
        }
    }


    private class ProcessChewReferral extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(ChewReferralTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new ChewReferralTable(context).fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            // if statement end
        }
    }


    private class ProcessCommunityUnit extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(CommunityUnitTable.CU_JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new CommunityUnitTable(context).CuFromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            // if statement end
        }
    }

    //we add method to pull users from the

    public String getJsonData(String urlString){
        String stream;
        ApiClient hh = new ApiClient();
        stream = hh.GetHTTPData(urlString);
        return stream;
    }


    public void syncRecruitments () {
        String syncResults;
        RecruitmentTable recruitmentTable = new RecruitmentTable(context);
        try {
            syncResults = this.syncClient(recruitmentTable.getRecruitmentToSyncAsJson(),
                    HttpServer.RECRUIRMENT_URL);
        } catch (Exception e){
            syncResults = null;
        }

        if (syncResults != null){
            processSyncedRecruitments(syncResults);
        }
    }
    private void processSyncedRecruitments(String stream){
        try {

            JSONObject reader = new JSONObject(stream);

            // Get the JSONArray recruitments
            JSONArray recs = reader.getJSONArray("status");
            RecruitmentTable recruitmentTable = new RecruitmentTable(context);

            for (int x = 0; x < recs.length(); x++) {
                Recruitment recruitment = recruitmentTable.getRecruitmentById(
                        recs.getJSONObject(x).getString("id"));
                recruitment.setSynced(recs.getJSONObject(x).getString("status") == "ok" ? 1 : 0);
                // update recruitment
                recruitmentTable.addData(recruitment);
            }
        }catch (Exception e){}

    }

    public void syncRegistrations () {
        String syncResults;
        RegistrationTable registrationTable = new RegistrationTable(context);
        try {
            syncResults = this.syncClient(registrationTable.getRegistrationToSyncAsJson(),
                    HttpServer.REGISTRATION_URL);
        } catch (Exception e){
            syncResults = null;
        }

        if (syncResults != null){
            processSyncedRegistrations(syncResults);
        }
    }
    private void processSyncedRegistrations(String stream){
        try {

            JSONObject reader = new JSONObject(stream);

            // Get the JSONArray recruitments
            JSONArray recs = reader.getJSONArray("status");
            RegistrationTable registrationTable = new RegistrationTable(context);

            for (int x = 0; x < recs.length(); x++) {
                Registration registration = registrationTable.getRegistrationById(
                        recs.getJSONObject(x).getString("id"));
                registration.setSynced(recs.getJSONObject(x).getString("status") == "ok" ? 1 : 0);
                // update recruitment
                registrationTable.addData(registration);
            }
        }catch (Exception e){}

    }

    public void syncInterviews () {
        String syncResults;
        InterviewTable interviewTable = new InterviewTable(context);
        try {
            syncResults = this.syncClient(interviewTable.getInterviewsToSyncAsJson(),
                    HttpServer.INTERVIEW_URL);
        } catch (Exception e){
            syncResults = null;
        }

        if (syncResults != null){
            processSyncedInterviews(syncResults);
        }
    }
    private void processSyncedInterviews(String stream){
        try {

            JSONObject reader = new JSONObject(stream);

            // Get the JSONArray
            JSONArray recs = reader.getJSONArray("status");
            InterviewTable interviewTable = new InterviewTable(context);

            for (int x = 0; x < recs.length(); x++) {
                Interview interview = interviewTable.getInterviewById(
                        recs.getJSONObject(x).getString("id"));
                interview.setSynced(recs.getJSONObject(x).getString("status") == "ok" ? 1 : 0);
                // update recruitment
                interviewTable.addData(interview);
            }
        }catch (Exception e){}

    }

    public void syncExams () {
        String syncResults;
        ExamTable examTable = new ExamTable(context);
        try {
            syncResults = this.syncClient(examTable.getExamsToSyncAsJson(),
                    HttpServer.EXAM_URL);
        } catch (Exception e){
            syncResults = null;
        }

        if (syncResults != null){
            processSyncedExams(syncResults);
        }
    }
    private void processSyncedExams(String stream){
        try {

            JSONObject reader = new JSONObject(stream);

            // Get the JSONArray
            JSONArray recs = reader.getJSONArray("status");
            ExamTable examTable = new ExamTable(context);

            for (int x = 0; x < recs.length(); x++) {
                Exam exam = examTable.getExamById(
                        recs.getJSONObject(x).getString("id"));
                exam.setSynced(recs.getJSONObject(x).getString("status") == "ok" ? 1 : 0);
                // update recruitment
                examTable.addData(exam);
            }
        }catch (Exception e){}

    }


}
