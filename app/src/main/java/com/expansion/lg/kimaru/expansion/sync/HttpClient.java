package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.other.WifiState;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
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
            postResults = null;
        }
        return postResults;
    }

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
                        String registrationUrl, recruitmentUrl, examUrl, interviewUrl;
                        WifiState wifiState = new WifiState(context);
                        if (wifiState.canReachPeerServer()) {
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


                            //Poll server for new records
                            // Recruitments
                            try {
                                String status = postRecruitments();
                            } catch (Exception e) {
                            }

                            try {
                                String status = postRegistrations();
                            } catch (Exception e) {
                            }

                            try {
                                String status = postExams();
                            } catch (Exception e) {
                            }

                            try {
                                String status = postInterviews();
                            } catch (Exception e) {
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 60*1000);
    }

    private class ProcessExams extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            // Toast.makeText(getBaseContext(), "Sync Exams", Toast.LENGTH_SHORT).show();
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(ExamTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){

                        Exam exam=new Exam();
                        exam.setId(recs.getJSONObject(x).getString(ExamTable.ID));
                        exam.setApplicant(recs.getJSONObject(x).getString(ExamTable.APPLICANT));
                        exam.setRecruitment(recs.getJSONObject(x).getString(ExamTable.RECRUITMENT));
                        exam.setCountry(recs.getJSONObject(x).getString(ExamTable.COUNTRY));
                        exam.setMath(recs.getJSONObject(x).getDouble(ExamTable.MATH));
                        exam.setPersonality(recs.getJSONObject(x).getDouble(ExamTable.PERSONALITY));
                        exam.setEnglish(recs.getJSONObject(x).getDouble(ExamTable.ENGLISH));
                        exam.setAddedBy(recs.getJSONObject(x).getInt(ExamTable.ADDED_BY));
                        exam.setComment(recs.getJSONObject(x).getString(ExamTable.COMMENT));
                        exam.setDateAdded(recs.getJSONObject(x).getLong(ExamTable.DATE_ADDED));
                        exam.setSynced(recs.getJSONObject(x).getInt(ExamTable.SYNCED));

                        // add recruitment
                        ExamTable examTable = new ExamTable(context);
                        examTable.addData(exam);

                    }



                    // process other data as this way..............

                }catch(JSONException e){
                    // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(),
                    // Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end


    private class ProcessRegistrations extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            return stream;
        }

        protected void onPostExecute(String stream){
            // Toast.makeText(getBaseContext(), "Sync Registrations", Toast.LENGTH_SHORT).show();
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    JSONArray recs = reader.getJSONArray(RegistrationTable.JSON_ROOT);
                    // Get the array first JSONObject
                    List<Recruitment> recruitmentList = new ArrayList<Recruitment>();

                    for (int x = 0; x < recs.length(); x++){
                        Registration registration=new Registration();
                        registration.setId(recs.getJSONObject(x).getString(RegistrationTable.ID));
                        registration.setName(recs.getJSONObject(x).getString(RegistrationTable.NAME));
                        registration.setPhone(recs.getJSONObject(x).getString(RegistrationTable.PHONE));
                        registration.setGender(recs.getJSONObject(x).getString(RegistrationTable.GENDER));
                        registration.setDob(recs.getJSONObject(x).getLong(RegistrationTable.DOB));
                        registration.setDistrict(recs.getJSONObject(x).getString(RegistrationTable.DISTRICT));
                        registration.setCountry(recs.getJSONObject(x).getString(RegistrationTable.COUNTRY));
                        registration.setRecruitment(recs.getJSONObject(x).getString(RegistrationTable.RECRUITMENT));
                        registration.setSubcounty(recs.getJSONObject(x).getString(RegistrationTable.SUB_COUNTY));
                        registration.setDivision(recs.getJSONObject(x).getString(RegistrationTable.DIVISION));
                        registration.setVillage(recs.getJSONObject(x).getString(RegistrationTable.VILLAGE));
                        registration.setMark(recs.getJSONObject(x).getString(RegistrationTable.MARK));
                        registration.setReadEnglish(recs.getJSONObject(x).getInt(RegistrationTable.READ_ENGLISH));
                        registration.setDateMoved(recs.getJSONObject(x).getLong(RegistrationTable.DATE_MOVED));
                        registration.setLangs(recs.getJSONObject(x).getString(RegistrationTable.LANGS));
                        registration.setBrac(recs.getJSONObject(x).getInt(RegistrationTable.BRAC));
                        registration.setBracChp(recs.getJSONObject(x).getInt(RegistrationTable.BRAC_CHP));
                        registration.setEducation(recs.getJSONObject(x).getString(RegistrationTable.EDUCATION));
                        registration.setOccupation(recs.getJSONObject(x).getString(RegistrationTable.OCCUPATION));
                        registration.setCommunity(recs.getJSONObject(x).getInt(RegistrationTable.COMMUNITY));
                        registration.setAddedBy(recs.getJSONObject(x).getInt(RegistrationTable.ADDED_BY));
                        registration.setComment(recs.getJSONObject(x).getString(RegistrationTable.COMMENT));
                        registration.setProceed(recs.getJSONObject(x).getInt(RegistrationTable.PROCEED));
                        registration.setDateAdded(recs.getJSONObject(x).getLong(RegistrationTable.DATE_ADDED));
                        registration.setSynced(recs.getJSONObject(x).getInt(RegistrationTable.SYNCED));
                        registration.setPicture("");

                        // add recruitment
                        RegistrationTable registrationTable = new RegistrationTable(context);
                        registrationTable.addData(registration);

                    }



                    // process other data as this way..............

                }catch(JSONException e){
                    // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end


    private class ProcessRecruitment extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(RecruitmentTable.JSON_ROOT);
                    // Get the array first JSONObject
                    List<Recruitment> recruitmentList = new ArrayList<Recruitment>();


                    for (int x = 0; x < recs.length(); x++){
                        Recruitment recruitment = new Recruitment();




                        recruitment.setId(recs.getJSONObject(x).getString(RecruitmentTable.ID));
                        recruitment.setName(recs.getJSONObject(x).getString(RecruitmentTable.NAME));
                        recruitment.setDistrict(recs.getJSONObject(x).getString(RecruitmentTable.DISTRICT));
                        recruitment.setSubcounty(recs.getJSONObject(x).getString(RecruitmentTable.SUB_COUNTY));
                        recruitment.setDivision(recs.getJSONObject(x).getString(RecruitmentTable.DIVISION));
                        recruitment.setCountry(recs.getJSONObject(x).getString(RecruitmentTable.COUNTRY));
                        recruitment.setLat(recs.getJSONObject(x).getString(RecruitmentTable.LAT));
                        recruitment.setLon(recs.getJSONObject(x).getString(RecruitmentTable.LON));
                        recruitment.setAddedBy(Integer.parseInt(recs.getJSONObject(x).getString(RecruitmentTable.ADDED_BY)));
                        recruitment.setComment(recs.getJSONObject(x).getString(RecruitmentTable.COMMENT));
                        recruitment.setDateAdded(Long.parseLong(recs.getJSONObject(x).getString(RecruitmentTable.DATE_ADDED)));
                        recruitment.setSynced(Integer.parseInt(recs.getJSONObject(x).getString(RecruitmentTable.SYNCED)));

                        // add recruitment
                        RecruitmentTable recruitmentTable = new RecruitmentTable(context);
                        recruitmentTable.addData(recruitment);

                    }



                    // process other data as this way..............

                }catch(JSONException e){
                    // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end

    private class ProcessInterviews extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            // Toast.makeText(getBaseContext(), "Sync Interviews", Toast.LENGTH_SHORT).show();
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(InterviewTable.JSON_ROOT);
                    // Get the array first JSONObject
                    List<Recruitment> recruitmentList = new ArrayList<Recruitment>();
                    for (int x = 0; x < recs.length(); x++){

                        Interview interview = new Interview();

                        interview.setId(recs.getJSONObject(x).getString(InterviewTable.ID));
                        interview.setApplicant(recs.getJSONObject(x).getString(InterviewTable.APPLICANT));
                        interview.setRecruitment(recs.getJSONObject(x).getString(InterviewTable.RECRUITMENT));
                        interview.setMotivation(recs.getJSONObject(x).getInt(InterviewTable.MOTIVATION));
                        interview.setCommunity(recs.getJSONObject(x).getInt(InterviewTable.COMMUNITY));
                        interview.setMentality(recs.getJSONObject(x).getInt(InterviewTable.MENTALITY));
                        interview.setSelling(recs.getJSONObject(x).getInt(InterviewTable.SELLING));
                        interview.setHealth(recs.getJSONObject(x).getInt(InterviewTable.HEALTH));
                        interview.setInvestment(recs.getJSONObject(x).getInt(InterviewTable.INVESTMENT));
                        interview.setCountry(recs.getJSONObject(x).getString(InterviewTable.COUNTRY));
                        interview.setInterpersonal(recs.getJSONObject(x).getInt(InterviewTable.INTERPERSONAL));
                        interview.setSelected(recs.getJSONObject(x).getInt(InterviewTable.SELECTED) == 1);
                        interview.setAddedBy(recs.getJSONObject(x).getInt(InterviewTable.ADDED_BY));
                        interview.setComment(recs.getJSONObject(x).getString(InterviewTable.COMMENT));
                        interview.setCommitment(recs.getJSONObject(x).getInt(InterviewTable.COMMITMENT));
                        interview.setDateAdded(recs.getJSONObject(x).getLong(InterviewTable.DATE_ADDED));
                        interview.setSynced(recs.getJSONObject(x).getInt(InterviewTable.SYNCED));
                        interview.setCanJoin(recs.getJSONObject(x).getInt(InterviewTable.CANJOIN) ==1);

                        // add recruitment
                        InterviewTable interviewTable = new InterviewTable(context);
                        interviewTable.addData(interview);

                    }



                    // process other data as this way..............

                }catch(JSONException e){
                    // Toast.makeText(getBaseContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end
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
            syncResults = this.syncClient(registrationTable.getRecruitmentToSyncAsJson(),
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
