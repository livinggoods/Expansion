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
}
