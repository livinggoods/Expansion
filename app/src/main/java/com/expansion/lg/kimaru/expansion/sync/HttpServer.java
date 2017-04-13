package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.expansion.lg.kimaru.expansion.tables.UserTable;
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

    private AsyncHttpClient client = new AsyncHttpClient(asyncServer);

    public static final String SERVER_URL = Constants.PEER_SERVER;
    public static final int SERVER_PORT = Constants.PEER_SERVER_PORT;
    public static final String RECRUIRMENT_URL = "recruitments";
    public static final String REGISTRATION_URL = "registrations";
    public static final String EXAM_URL = "exams";
    public static final String INTERVIEW_URL = "interviews";
    public static String url;
    Context context;

    public HttpServer(Context context){
        this.context = context;
    }

    public void startServer(){
        server.get("/"+RECRUIRMENT_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                RecruitmentTable recruitmentData = new RecruitmentTable(context);
                response.send(recruitmentData.getRecruitmentJson());
            }
        });
        server.get("/"+REGISTRATION_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                RegistrationTable registrationTable = new RegistrationTable(context);
                response.send(registrationTable.getRegistrationJson());
            }
        });
        server.get("/"+EXAM_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                ExamTable examTable = new ExamTable(context);
                response.send(examTable.getExamJson());
            }
        });
        server.get("/"+INTERVIEW_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                InterviewTable interviewTable = new InterviewTable(context);
                response.send(interviewTable.getInterviewJson());
            }
        });
        server.get("/users", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                UserTable userTable = new UserTable(context);
                response.send(userTable.getUsersJson());
            }
        });
        server.get("/education", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                EducationTable educationTable = new EducationTable(context);
                response.send(educationTable.getEducationJson());
            }
        });

        //POST METHODS

        server.post("/"+RECRUIRMENT_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
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
            }
        });
        server.post("/"+REGISTRATION_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
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
            }
        });
        server.post("/"+INTERVIEW_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
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
            }
        });
        server.post("/"+EXAM_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
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
            }
        });

        server.listen(asyncServer, SERVER_PORT);
    }

    private void processRecruitments(JSONObject json){
        try{
            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(RecruitmentTable.JSON_ROOT);
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
                recruitment.setCounty(recs.getJSONObject(x).getString(RecruitmentTable.COUNTY));

                // add recruitment
                RecruitmentTable recruitmentTable = new RecruitmentTable(context);
                recruitmentTable.addData(recruitment);

            }



            // process other data as this way..............

        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void processRegistrations(JSONObject json){
        try{
            JSONArray recs = json.getJSONArray(RegistrationTable.JSON_ROOT);
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
                registration.setSubcounty(recs.getJSONObject(x).getString(RegistrationTable.SUB_COUNTY));
                registration.setDivision(recs.getJSONObject(x).getString(RegistrationTable.DIVISION));
                registration.setRecruitment(recs.getJSONObject(x).getString(RegistrationTable.RECRUITMENT));
                registration.setCountry(recs.getJSONObject(x).getString(RegistrationTable.COUNTRY));
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
    }

    private void processInterviews(JSONObject json){
        try{
            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(InterviewTable.JSON_ROOT);
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
                interview.setCountry(recs.getJSONObject(x).getString(InterviewTable.COUNTRY));
                interview.setSelling(recs.getJSONObject(x).getInt(InterviewTable.SELLING));
                interview.setHealth(recs.getJSONObject(x).getInt(InterviewTable.HEALTH));
                interview.setInvestment(recs.getJSONObject(x).getInt(InterviewTable.INVESTMENT));
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
    }
    private void processExams(JSONObject json){
        try{

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(ExamTable.JSON_ROOT);

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
        }catch(JSONException e){
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
