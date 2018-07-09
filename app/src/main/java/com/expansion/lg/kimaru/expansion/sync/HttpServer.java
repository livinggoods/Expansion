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

    // Endpoints
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
    public static final String USERS_URL = "users";
    public static final String EDUCATION_URL = "education";
    public static final String ICCM_COMPONENTS_URL = "iccm/components";
    public static final String MOBILIZATION_URL = "mobilizations";
    public static final String TRAININGS_URL = "trainings";
    public static final String TRAINING_CLASSES_URL = "training/classes";
    public static final String TRAINING_ROLES_URL = "training/roles";
    public static final String TRAINING_TRAINEES_URL = "training/trainees";
    public static final String TRAINING_TRAINERS_URL = "training/trainers";
    public static final String WARDS_URL = "wards";

    public static String url;


    public HttpServer(Context context) {
        this.context = context;
        this.constants = new Constants(context);
        this.serverPort = Integer.valueOf(constants.getPeerServerPort());
    }

    public void startServer() {
        Log.d("Tremap Sync", "Starting server");

        server.get("/" + RECRUIRMENT_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                RecruitmentTable recruitmentData = new RecruitmentTable(context);
                response.send(recruitmentData.getRecruitmentJson());

            }
        });

        server.get("/" + REGISTRATION_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                RegistrationTable registrationTable = new RegistrationTable(context);
                response.send(registrationTable.getRegistrationJson());
            }
        });

        server.get("/" + EXAM_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                ExamTable examTable = new ExamTable(context);
                response.send(examTable.getExamJson());
            }
        });

        server.get("/" + INTERVIEW_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                InterviewTable interviewTable = new InterviewTable(context);
                response.send(interviewTable.getInterviewJson());
            }
        });

        //// postCommunityUnit postChewReferral postLinkFacilities postSubCounties
        server.get("/" + SUBCOUNTY_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                SubCountyTable subCountyTable = new SubCountyTable(context);
                response.send(subCountyTable.getJson());
            }
        });

        server.get("/" + CU_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                CommunityUnitTable communityUnitTable = new CommunityUnitTable(context);
                response.send(communityUnitTable.getJson());
            }
        });

        server.get("/" + CHEW_REFERRAL_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                ChewReferralTable chewReferralTable = new ChewReferralTable(context);
                response.send(chewReferralTable.getChewReferralJson());
            }
        });

        server.get("/" + LINKFACILITY_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                LinkFacilityTable linkFacilityTable = new LinkFacilityTable(context);
                response.send(linkFacilityTable.getJson());
            }
        });
        //

        server.get("/" + USERS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                UserTable userTable = new UserTable(context);
                response.send(userTable.getUsersJson());
            }
        });

        server.get("/" + EDUCATION_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                EducationTable educationTable = new EducationTable(context);
                response.send(educationTable.getEducationJson());
            }
        });

        server.get("/" + ICCM_COMPONENTS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                IccmComponentTable data = new IccmComponentTable(context);
                response.send(data.getIccmJson());
            }
        });

        server.get("/" + MAPPING_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                MappingTable data = new MappingTable(context);
                response.send(data.getJson());
            }
        });

        server.get("/" + MOBILIZATION_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                MobilizationTable data = new MobilizationTable(context);
                response.send(data.getMobilizationJson());
            }
        });

        server.get("/" + PARTNERS_ACTIVITY_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                PartnerActivityTable data = new PartnerActivityTable(context);
                response.send(data.getJson());
            }
        });

        server.get("/" + PARTNERS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                PartnersTable data = new PartnersTable(context);
                response.send(data.getJson());
            }
        });

        server.get("/" + TRAININGS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                TrainingTable data = new TrainingTable(context);
                response.send(data.getJson());
            }
        });

        server.get("/" + TRAINING_CLASSES_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                TrainingClassTable data = new TrainingClassTable(context);
                response.send(data.getJson());
            }
        });

        server.get("/" + TRAINING_ROLES_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                TrainingRolesTable data = new TrainingRolesTable(context);
                response.send(data.getTrainingRoleJson());
            }
        });

        server.get("/" + TRAINING_TRAINEES_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                TrainingTraineeTable data = new TrainingTraineeTable(context);
                response.send(data.getJson());
            }
        });

        server.get("/" + TRAINING_TRAINERS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                TrainingTrainersTable data = new TrainingTrainersTable(context);
                response.send(data.getTrainingTrainerJson());
            }
        });

        server.get("/" + VILLAGE_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                VillageTable data = new VillageTable(context);
                response.send(data.getJson());
            }
        });

        server.get("/" + WARDS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                WardTable data = new WardTable(context);
                response.send(data.getJson());
            }
        });


        //POST METHODS

        server.post("/" + RECRUIRMENT_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processRecruitments(json);
                    }
                    response.send(json);
                    // response.send(typeReceived);
                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });
        server.post("/" + REGISTRATION_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processRegistrations(json);
                    }
                    response.send(json);
                    // response.send(typeReceived);
                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });
        server.post("/" + INTERVIEW_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processInterviews(json);
                    }
                    response.send(json);
                    // response.send(typeReceived);
                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + EXAM_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processExams(json);
                    }
                    response.send(json);
                    // response.send(typeReceived);
                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        //postCommunityUnit postChewReferral postLinkFacilities postSubCounties
        server.post("/" + CU_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processCus(json);
                    }
                    response.send(json);
                    // response.send(typeReceived);
                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + CHEW_REFERRAL_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processChewReferral(json);
                    }
                    response.send(json);
                    // response.send(typeReceived);
                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + LINKFACILITY_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processLinkFacility(json);
                    }
                    response.send(json);
                    // response.send(typeReceived);
                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + SUBCOUNTY_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processSubCounty(json);
                        response.send("Body not instalce of JSON Object Body");
                    } else {
                        processSubCounty(json);
                        response.send("processed");
                    }

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + EDUCATION_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processEducation(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + ICCM_COMPONENTS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processIccmComponent(json);

                    }
                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + MAPPING_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processMapping(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + MOBILIZATION_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processMobilization(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + PARTNERS_ACTIVITY_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processPartnerActivity(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + PARTNERS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processPartners(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + TRAININGS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processTrainings(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + TRAINING_CLASSES_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processTrainingClasses(json);

                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + TRAINING_ROLES_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processTrainingRoles(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + TRAINING_TRAINEES_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processTrainingTrainees(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + TRAINING_TRAINERS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processTrainingTrainers(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + VILLAGE_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processVillages(json);
                    }

                    response.send(json);

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.post("/" + WARDS_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    assertNotNull(request.getHeaders().get("Host"));
                    JSONObject json = new JSONObject();
                    if (request.getBody() instanceof JSONObjectBody) {
                        json = ((JSONObjectBody) request.getBody()).get();
                        processWards(json);
                        response.send("Body not instalce of JSON Object Body");
                    } else {
                        processSubCounty(json);
                        response.send("processed");
                    }

                } catch (Exception e) {
                    response.send(e.getMessage());
                }
            }
        });

        server.listen(asyncServer, serverPort);
    }

    private void processCus(JSONObject json) {
        Log.d("Tremap Sync", "Processing CUs");
        try {
            JSONArray recs = json.getJSONArray(CommunityUnitTable.CU_JSON_ROOT);
            for (int x = 0; x < recs.length(); x++) {
                new CommunityUnitTable(context).CuFromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            Log.d("Tremap Server", "ERR CUs " + e.getMessage());
        }
    }

    private void processSubCounty(JSONObject json) {
        Log.d("Tremap Sync", "Processing subcounties");
        try {
            JSONArray recs = json.getJSONArray(SubCountyTable.JSON_ROOT);
            for (int x = 0; x < recs.length(); x++) {
                Log.d("Tremap ", "Received subcounty data " + recs.getJSONObject(x));
                new SubCountyTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            Log.d("Tremap Server", "ERR SUBCOUNTY " + e.getMessage());
        }
    }

    private void processLinkFacility(JSONObject json) {
        Log.d("Tremap Sync", "Processing Link Facilities");
        try {
            JSONArray recs = json.getJSONArray(LinkFacilityTable.JSON_ROOT);
            // Get the array first JSONObject
            for (int x = 0; x < recs.length(); x++) {
                new LinkFacilityTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            Log.d("Tremap Server", "ERR Link Facility " + e.getMessage());
        }
    }

    private void processChewReferral(JSONObject json) {
        Log.d("Tremap Server CHEW", "Processing Received Chews");
        Log.d("Tremap Server CHEW", "Received data " + json);
        try {
            JSONArray recs = json.getJSONArray(ChewReferralTable.JSON_ROOT);
            Log.d("Tremap Server CHEW", "Processing Received Chews");
            // Get the array first JSONObject
            for (int x = 0; x < recs.length(); x++) {
                new ChewReferralTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            Log.d("Tremap Server CHEW", "ERROR " + e.getMessage());
        }
    }

    private void processRecruitments(JSONObject json) {
        try {
            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(RecruitmentTable.JSON_ROOT);
            // Get the array first JSONObject
            List<Recruitment> recruitmentList = new ArrayList<Recruitment>();

            for (int x = 0; x < recs.length(); x++) {
                new RecruitmentTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processRegistrations(JSONObject json) {
        try {
            JSONArray recs = json.getJSONArray(RegistrationTable.JSON_ROOT);
            // Get the array first JSONObject
            for (int x = 0; x < recs.length(); x++) {
                new RegistrationTable(context).fromJsonObject(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processInterviews(JSONObject json) {
        try {
            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(InterviewTable.JSON_ROOT);
            // Get the array first JSONObject
            List<Recruitment> recruitmentList = new ArrayList<Recruitment>();
            for (int x = 0; x < recs.length(); x++) {
                new InterviewTable(context).fromJson(recs.getJSONObject(x));

            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processExams(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(ExamTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new ExamTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processEducation(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(EducationTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new EducationTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processIccmComponent(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(IccmComponentTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new IccmComponentTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processMapping(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(MappingTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new MappingTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processMobilization(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(MobilizationTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new MobilizationTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processPartnerActivity(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(PartnerActivityTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new PartnerActivityTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processPartners(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(PartnersTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new PartnersTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainings(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new TrainingTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainingClasses(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingClassTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new TrainingClassTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainingRoles(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingRolesTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new TrainingRolesTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainingTrainees(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingTraineeTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new TrainingTraineeTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processTrainingTrainers(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(TrainingTrainersTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new TrainingTraineeTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processVillages(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(VillageTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new VillageTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processWards(JSONObject json) {
        try {

            // Get the JSONArray recruitments
            JSONArray recs = json.getJSONArray(WardTable.JSON_ROOT);

            for (int x = 0; x < recs.length(); x++) {
                new WardTable(context).fromJson(recs.getJSONObject(x));
            }
        } catch (JSONException e) {
            // Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
