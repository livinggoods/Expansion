package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

/**
 * Created by kimaru on 7/19/17.
 */


import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.mzigos.TrainingClass;
import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainee;
import com.expansion.lg.kimaru.expansion.mzigos.Village;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.other.WifiState;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.MappingTable;
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable;
import com.expansion.lg.kimaru.expansion.tables.ParishTable;
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
import com.expansion.lg.kimaru.expansion.tables.VillageTable;
import com.expansion.lg.kimaru.expansion.tables.WardTable;
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

public class HttpClient{
    Context context;
    boolean isRunning = false;
    Constants constants;

    private String url;
    public HttpClient(Context context){
        this.context = context;
        constants = new Constants(context);
        url = constants.getPeerServer() + ":" + constants.getPeerServerPort();
    }

    // Implement two methods:
    // POST = To send all records to the Peer Server
    // GET = Get all records that are in the Peer Server


    //GET METHODS

    public void startClient(){
        final Handler handler = new Handler();
        Log.d("Tremap Sync", "Client Starting");
        Timer combinedTimer = new Timer();
        TimerTask combinedTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String registrationUrl, recruitmentUrl, examUrl, interviewUrl,
                                linkFacilityUrl, chewReferralUrl, communityUnitUrl;
                        WifiState wifiState = new WifiState(context);
                        Log.d("Tremap Sync", "Checking Wifi connection");
                        if (wifiState.canReachPeerServer()) {
                            Log.d("Tremap Sync", "Wifi Connected");

                            //Link Facility
                            linkFacilityUrl = url + "/" + HttpServer.LINKFACILITY_URL;
                            Log.d("Tremap Sync", "Link Facility Process");
                            new ProcessLinkFacility().execute(linkFacilityUrl);

                            //Community Url
                            communityUnitUrl = url + "/" + HttpServer.CU_URL;
                            Log.d("Tremap Sync", "Community Unit Process");
                            new ProcessCommunityUnit().execute(communityUnitUrl);

                            //chew referral
                            chewReferralUrl = url + "/" + HttpServer.CHEW_REFERRAL_URL;
                            Log.d("Tremap Sync", "Chew Referral Process");
                            new ProcessChewReferral().execute(chewReferralUrl);

                            //Recruitments
                            recruitmentUrl = url + "/" + HttpServer.RECRUIRMENT_URL;
                            Log.d("Tremap Sync", "Recruitments Process");
                            new ProcessRecruitment().execute(recruitmentUrl);

                            //registrations
                            registrationUrl = url + "/" + HttpServer.REGISTRATION_URL;
                            Log.d("Tremap Sync", "Registrations Process");
                            new ProcessRegistrations().execute(registrationUrl);

                            //Interviews
                            interviewUrl = url + "/" + HttpServer.INTERVIEW_URL;
                            Log.d("Tremap Sync", "Interviews Process");
                            new ProcessInterviews().execute(interviewUrl);

                            //Exams
                            examUrl = url + "/" + HttpServer.EXAM_URL;
                            Log.d("Tremap Sync", "Exams Process");
                            new ProcessExams().execute(examUrl);

                            String educationUrl = url + "/" + HttpServer.EDUCATION_URL;
                            new ProcessEducation().execute(educationUrl);

                            String iccmUrl = url + "/" + HttpServer.ICCM_COMPONENTS_URL;
                            new ProcessIccmComponents().execute(iccmUrl);

                            String mappingUrl = url + "/" + HttpServer.MAPPING_URL;
                            new ProcessMapping().execute(mappingUrl);

                            String mobilizationUrl = url + "/" + HttpServer.MOBILIZATION_URL;
                            new ProcessMobilization().execute(mobilizationUrl);

                            String parishUrl = url + "/" + HttpServer.PARTNERS_URL;
                            new ProcessParish().execute(parishUrl);

                            String partnerActivityUrl = url + "/" + HttpServer.PARTNERS_ACTIVITY_URL;
                            new ProcessPartnerActivity().execute(partnerActivityUrl);

                            String partnerUrl = url + "/" + HttpServer.PARTNERS_URL;
                            new ProcessPartners().execute(partnerUrl);

                            String subCountyUrl = url + "/" + HttpServer.SUBCOUNTY_URL;
                            new ProcessSubCounty().execute(subCountyUrl);

                            String trainingClassUrl = url + "/" + HttpServer.TRAINING_CLASSES_URL;
                            new ProcessTrainingClasses().execute(trainingClassUrl);

                            String trainingRolesUrl = url + "/" + HttpServer.TRAINING_ROLES_URL;
                            new ProcessTrainingRoles().execute(trainingRolesUrl);

                            String trainingUrl = url + "/" + HttpServer.TRAININGS_URL;
                            new ProcessTrainings().execute(trainingUrl);

                            String trainingTraineeUrl = url + "/" + HttpServer.TRAINING_TRAINERS_URL;
                            new ProcessTrainingTrainees().execute(trainingTraineeUrl);

                            String trainingTrainersUrl = url + "/" + HttpServer.TRAINING_TRAINERS_URL;
                            new ProcessTrainingTrainers().execute(trainingTrainersUrl);

                            String villageUrl = url + "/" + HttpServer.VILLAGE_URL;
                            new ProcessVillages().execute(villageUrl);

                            String wardUrl = url + "/" + HttpServer.WARDS_URL;
                            new ProcessWards().execute(wardUrl);

                        }else{
                            Log.d("Tremap Sync Err", "WiFi not Connected");
                        }
                    }
                });
            }
        };
        combinedTimer.schedule(combinedTask, 0, 60*1000);

        Timer postRecordsTimer = new Timer();
        TimerTask postRecordsTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        WifiState wifiState = new WifiState(context);
                        Log.d("Tremap Sync", "Posting Records Start");
                        if (wifiState.canReachPeerServer()) {
                            Log.d("Tremap", "=====================+++++++++++++++++++=============");
                            Log.d("Tremap Sync", "Post my records");
                            new PostMyRecords().execute();
                        }else{
                            Log.d("Tremap Sync Err", "WiFi not Connected");
                        }
                    }
                });
            }
        };
        postRecordsTimer.schedule(postRecordsTask, 0, 60*1000);
    }

    private class ProcessCommunityUnit extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            Log.d("Tremap Sync", "Community Unit Async task");
            Log.d("Tremap Sync", "Community Unit URL " + urlString);
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            Log.d("Tremap Sync", "Community Unit Async task started");
            if(stream !=null){
                Log.d("Tremap Sync", "Community Unit Stream is not null ");
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(CommunityUnitTable.CU_JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        Log.d("Tremap Sync", "Community Unit gotten, try saving");
                        new CommunityUnitTable(context).CuFromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    Log.d("Tremap Sync ERR", "Community Unit "+ e.getMessage());
                }
            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            // if statement end
        }
    }

    private class ProcessEducation extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(EducationTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new EducationTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessIccmComponents extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(IccmComponentTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new IccmComponentTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessMapping extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(MappingTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new MappingTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessMobilization extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(MobilizationTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new MobilizationTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessParish extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(ParishTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new ParishTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessPartnerActivity extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(PartnerActivityTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new PartnerActivityTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessPartners extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(PartnersTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new PartnersTable(context).fromJson(recs.getJSONObject(x));
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
        }
    }

    private class ProcessTrainingClasses extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(TrainingClassTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new TrainingClassTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessTrainingRoles extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(TrainingRolesTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new TrainingRolesTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessTrainings extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(TrainingTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new TrainingTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessTrainingTrainees extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(TrainingTraineeTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new TrainingTraineeTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessTrainingTrainers extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(TrainingTrainersTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new TrainingTrainersTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessVillages extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(VillageTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new VillageTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessWards extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(WardTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new WardTable(context).fromJson(recs.getJSONObject(x));
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

    private class ProcessLinkFacility extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            Log.d("Tremap Sync", "Link Facility Async task");
            Log.d("Tremap Sync", "Link Facility URL " + urlString);

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            Log.d("Tremap Sync", "Link Facility Async task");

            if(stream !=null){
                Log.d("Tremap Sync", "Link Facility stream is not null");
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(LinkFacilityTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new LinkFacilityTable(context).fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    Log.d("Tremap Sync ERR", "Link Facility "+ e.getMessage());
                }
            }else{
                Log.d("Tremap Sync", "Link Facility stream is null");
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
            Log.d("Tremap Sync", "CHEW referral Async task");
            Log.d("Tremap Sync", "CHEW referral URL " + urlString);
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            Log.d("Tremap Sync", "CHEW Async task started");
            if(stream !=null){
                Log.d("Tremap Sync", "CHEW Referral stream is not null");
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray(ChewReferralTable.JSON_ROOT);

                    for (int x = 0; x < recs.length(); x++){
                        new ChewReferralTable(context).fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    Log.d("Tremap Sync ERR", "CHEW Referral "+ e.getMessage());
                }
            }else{
                Log.d("Tremap Sync", "CHEW Referral stream is null");
            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            // if statement end
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
                    Log.d("Tremap", "Processing REcruitmnt ");
                    for (int x = 0; x < recs.length(); x++){
                        new RecruitmentTable(context).fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    Log.d("Tremap", "Processing error "+e.getMessage());
                }
            }
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
             // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end


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


    /**
     * Now methods to post the records
     *
     */

    private class PostMyRecords extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            Log.d("Tremap POST", "Posting the records in the device to peer server");
            try {
                Log.d("Tremap Sync LOG", "+++++++++========++++++++===+++=+===+=+======+++++");
                String status = postRecruitments();
                Log.d("Tremap Sync LOG", "Recruitments Status "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERROR", "postRecruitments "+e.getMessage());
            }

            try {
                String status = postRegistrations();
                Log.d("Tremap Sync LOG", "Registrations Status "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERR", "Error in posting registrations "+e.getMessage());
            }

            try {
                String status = postExams();
                Log.d("Tremap Sync LOG", "Exams Status "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERR", "Error in posting exams "+e.getMessage());
            }

            try {
                String status = postInterviews();
                Log.d("Tremap Sync LOG", "Interviews Status "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERR", "Error in posting interviews "+e.getMessage());
            }

            try {
                String status = postCommunityUnit();
                Log.d("Tremap Sync LOG", "Community Unit Status "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERR", "Error in posting community units "+e.getMessage());
            }

            try {
                String status = postChewReferral();
                Log.d("Tremap Sync LOG", "CHEW REFs Status "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERR", "Error in posting CHEWs "+e.getMessage());
            }

            try {
                String status = postLinkFacilities();
                Log.d("Tremap Sync LOG", "Link Facility Status "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERR", "Error in posting Link Facilities "+e.getMessage());
            }

            try {
                String status = postEducation();
                Log.d("Tremap Sync LOG", "Education "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERR", "Error in posting Link Facilities "+e.getMessage());
            }

            try {
                String status = postIccmComponents();
                Log.d("Tremap Sync LOG", "Education "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERR", "Error in posting Link Facilities "+e.getMessage());
            }

            try {
                String status = postMapping();
                Log.d("Tremap Sync LOG", "Education "+status);
            } catch (Exception e) {
                Log.d("Tremap Sync ERR", "Error in posting Link Facilities "+e.getMessage());
            }

            try {
                String status = postMobilization();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postParsh();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postPartnerActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postPartners();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postSubCounty();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postTrainingClasses();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postTrainingRoles();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postTrainings();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postTrainingTrainees();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postTrainingTrainers();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postVillages();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String status = postWards();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
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
    private String peerClientServer(JSONObject json, String JsonRoot, String apiEndpoint) throws Exception {
        //  get the server URL
        AsyncHttpPost p = new AsyncHttpPost(url+"/"
                +apiEndpoint);
        p.setBody(new JSONObjectBody(json));
        JSONObject ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get();
        return ret.getString(JsonRoot);
    }

    public String postRegistrations() throws Exception {
        Log.d("Tremap POST", "Posting Registrations to peer server");
        String postResults;
        RegistrationTable registrationTable = new RegistrationTable(context);
        try {
            postResults = this.peerClientServer(registrationTable.getRegistrationJson(),
                    RegistrationTable.JSON_ROOT, HttpServer.REGISTRATION_URL);
            Log.d("Tremap Sync", "Registrations posted");
        } catch (Exception e){
            Log.d("ERROR : Sync Regs", e.getMessage());
            postResults = null;
        }
        return postResults;
    }
    public String postInterviews() throws Exception {
        Log.d("Tremap POST", "Posting Interviews to peer server");
        String postResults;
        InterviewTable interviewTable = new InterviewTable(context);
        try {
            postResults = this.peerClientServer(interviewTable.getInterviewJson(),
                    InterviewTable.JSON_ROOT, HttpServer.INTERVIEW_URL);
            Log.d("Tremap Sync", "Interviews posted");
        } catch (Exception e){
            Log.d("ERROR : Sync Inter", e.getMessage());
            postResults = null;
        }
        return postResults;
    }
    public String postExams() throws Exception {
        Log.d("Tremap POST", "Posting Exams to peer server");
        String postResults;
        ExamTable examTable = new ExamTable(context);
        try {
            postResults = this.peerClientServer(examTable.getExamJson(), ExamTable.JSON_ROOT,
                    HttpServer.EXAM_URL);
            Log.d("Tremap Sync", "Exams posted");
        } catch (Exception e){
            Log.d("ERROR : Sync Exams", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    public String postRecruitments() throws Exception {
        Log.d("Tremap POST", "Posting Recruitments to peer server");
        String postResults;
        RecruitmentTable recruitmentTable = new RecruitmentTable(context);
        try {
            postResults = this.peerClientServer(recruitmentTable.getRecruitmentJson(),
                    RecruitmentTable.JSON_ROOT, HttpServer.RECRUIRMENT_URL);
            Log.d("Tremap Sync", "Recruitments posted");
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


    public String postLinkFacilities() throws Exception {
        Log.d("Tremap POST", "Posting Link Facilities to peer server");
        String postResults;
        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(context);
        try {
            postResults = this.peerClientServer(linkFacilityTable.getJson(),
                    LinkFacilityTable.JSON_ROOT, HttpServer.LINKFACILITY_URL);
            Log.d("Tremap Sync", "Link Facilities posted");
        } catch (Exception e){
            Log.d("ERR: Sync LinkFacility", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    public String postChewReferral() throws Exception {
        Log.d("Tremap POST", "Posting Chews/referrals to peer server");
        String postResults;
        ChewReferralTable chewReferralTable = new ChewReferralTable(context);
        try {
            postResults = this.peerClientServer(chewReferralTable.getChewReferralJson(),
                    ChewReferralTable.JSON_ROOT, HttpServer.CHEW_REFERRAL_URL);
            Log.d("Tremap Sync", "CHEWS posted");
        } catch (Exception e){
            Log.d("ERR: Sync chews", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    public String postCommunityUnit() throws Exception {
        Log.d("Tremap POST", "Posting Community Units to peer server");
        String postResults;
        CommunityUnitTable communityUnitTable = new CommunityUnitTable(context);
        try {
            postResults = this.peerClientServer(communityUnitTable.getJson(),
                    CommunityUnitTable.CU_JSON_ROOT, HttpServer.CU_URL);
            Log.d("Tremap Sync", "Community Units posted");
        } catch (Exception e){
            Log.d("ERR: Sync CUs", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    public String postEducation() throws Exception {
        Log.d("Tremap POST", "Posting Education to peer server");
        String postResults;
        EducationTable table = new EducationTable(context);
        try {
            postResults = this.peerClientServer(table.getEducationJson(),
                    EducationTable.JSON_ROOT, HttpServer.EDUCATION_URL);
            Log.d("Tremap Sync", "Education records posted");
        } catch (Exception e){
            Log.d("ERR: Sync Educationss", e.getMessage());
            postResults = null;
        }
        return postResults;
    }

    public String postIccmComponents() throws Exception {
        String postResults;
        IccmComponentTable table = new IccmComponentTable(context);
        try {
            postResults = this.peerClientServer(table.getIccmJson(),
                    IccmComponentTable.JSON_ROOT, HttpServer.ICCM_COMPONENTS_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postMapping() throws Exception {
        String postResults;
        MappingTable table = new MappingTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    MappingTable.JSON_ROOT, HttpServer.MAPPING_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postMobilization() throws Exception {
        String postResults;
        MobilizationTable table = new MobilizationTable(context);
        try {
            postResults = this.peerClientServer(table.getMobilizationJson(),
                    MobilizationTable.JSON_ROOT, HttpServer.MOBILIZATION_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postParsh() throws Exception {
        String postResults;
        ParishTable table = new ParishTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    ParishTable.JSON_ROOT, HttpServer.PARISH_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postPartnerActivity() throws Exception {
        String postResults;
        PartnerActivityTable table = new PartnerActivityTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    PartnerActivityTable.JSON_ROOT, HttpServer.PARTNERS_ACTIVITY_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postPartners() throws Exception {
        String postResults;
        PartnersTable table = new PartnersTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    PartnersTable.JSON_ROOT, HttpServer.PARTNERS_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postSubCounty() throws Exception {
        String postResults;
        SubCountyTable table = new SubCountyTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    SubCountyTable.JSON_ROOT, HttpServer.SUBCOUNTY_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postTrainingClasses() throws Exception {
        String postResults;
        TrainingClassTable table = new TrainingClassTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    TrainingClassTable.JSON_ROOT, HttpServer.TRAINING_CLASSES_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postTrainingRoles() throws Exception {
        String postResults;
        TrainingRolesTable table = new TrainingRolesTable(context);
        try {
            postResults = this.peerClientServer(table.getTrainingRoleJson(),
                    TrainingRolesTable.JSON_ROOT, HttpServer.TRAINING_ROLES_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postTrainings() throws Exception {
        String postResults;
        TrainingTable table = new TrainingTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    TrainingTable.JSON_ROOT, HttpServer.TRAININGS_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postTrainingTrainees() throws Exception {
        String postResults;
        TrainingTraineeTable table = new TrainingTraineeTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    TrainingTraineeTable.JSON_ROOT, HttpServer.TRAINING_TRAINEES_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postTrainingTrainers() throws Exception {
        String postResults;
        TrainingTrainersTable table = new TrainingTrainersTable(context);
        try {
            postResults = this.peerClientServer(table.getTrainingTrainerJson(),
                    TrainingTrainersTable.JSON_ROOT, HttpServer.TRAINING_TRAINERS_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postVillages() throws Exception {
        String postResults;
        VillageTable table = new VillageTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    VillageTable.JSON_ROOT, HttpServer.VILLAGE_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }

    public String postWards() throws Exception {
        String postResults;
        WardTable table = new WardTable(context);
        try {
            postResults = this.peerClientServer(table.getJson(),
                    WardTable.JSON_ROOT, HttpServer.WARDS_URL);
        } catch (Exception e){
            Log.d("SYNC ERROR", e.getMessage());
            e.printStackTrace();
            postResults = null;
        }
        return postResults;
    }


    //THE FOLLOWING SYNC METHODS HELPS US TO UPLOAD DATA TO THE CLOUD (https://expansion.lg-apps.com)

    // Callback for the API
    private String syncClient(JSONObject json, String apiEndpoint) throws Exception {
        //  get the server URL
        Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Log.d("Tremap", "URL: "+ new Constants(context).getApiServer()+"/"+apiEndpoint);
        Log.d("Tremap", "URL: "+ json.toString());
        Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        AsyncHttpPost p = new AsyncHttpPost(new Constants(context).getApiServer()+"/"+apiEndpoint);
        p.setBody(new JSONObjectBody(json));
        JSONObject ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get();
//        return ret.getString(expectedJsonRoot);
        Log.d("RESULTS : Sync", ret.toString());
        Log.d("API  : Url", new Constants(context).getApiServer()+apiEndpoint);
        return ret.toString();
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
    public void syncCommunityUnits () {
        String syncResults;
        CommunityUnitTable communityUnitTable = new CommunityUnitTable(context);
        try {
            syncResults = this.syncClient(communityUnitTable.getJson(),
                    HttpServer.CU_URL);
        } catch (Exception e){
            syncResults = null;
        }

        if (syncResults != null){
            try {

                JSONObject reader = new JSONObject(syncResults);
                JSONArray recs = reader.getJSONArray("status");
                for (int x = 0; x < recs.length(); x++) {
                    communityUnitTable.CuFromJson(recs.getJSONObject(x));
                }
            }catch (Exception e){}
        }
    }

    public void syncReferrals () {
        String syncResults;
        ChewReferralTable chewReferralTable = new ChewReferralTable(context);
        try {
            syncResults = this.syncClient(chewReferralTable.getChewReferralJson(),
                    HttpServer.CHEW_REFERRAL_URL);
        } catch (Exception e){
            syncResults = null;
        }
        if (syncResults != null){
            try {

                JSONObject reader = new JSONObject(syncResults);
                JSONArray recs = reader.getJSONArray("status");

                for (int x = 0; x < recs.length(); x++) {
                    chewReferralTable.fromJson(recs.getJSONObject(x));
                }
            }catch (Exception e){}
        }
    }
    public void syncParishes () {
        String syncResults;
        ParishTable parishTable = new ParishTable(context);
        try {
            syncResults = this.syncClient(parishTable.getJson(),
                    HttpServer.PARISH_URL);
        } catch (Exception e){
            syncResults = null;
        }
        if (syncResults != null){
            try {

                JSONObject reader = new JSONObject(syncResults);
                JSONArray recs = reader.getJSONArray("status");

                for (int x = 0; x < recs.length(); x++) {
                    parishTable.fromJson(recs.getJSONObject(x));
                }
            }catch (Exception e){}
        }
    }


    public void syncPartners () {
        String syncResults;
         PartnersTable partnersTable = new PartnersTable(context);
        try {
            syncResults = this.syncClient(partnersTable.getJson(),
                    HttpServer.PARTNERS_URL);
        } catch (Exception e){
            syncResults = null;
        }
    }

    public void syncPartnersCommunityUnits () {
        String syncResults;
        PartnerActivityTable partnerActivityTable = new PartnerActivityTable(context);
        try {
            syncResults = this.syncClient(partnerActivityTable.getJson(),
                    HttpServer.PARTNERS_ACTIVITY_URL);
        } catch (Exception e){
            syncResults = null;
        }
    }


    public void syncMapping () {
        String syncResults;
        MappingTable mappingTable = new MappingTable(context);
        try {
            syncResults = this.syncClient(mappingTable.getJson(),
                    HttpServer.MAPPING_URL);
        } catch (Exception e){
            syncResults = null;
        }
        if (syncResults != null){
            try {

                JSONObject reader = new JSONObject(syncResults);
                JSONArray recs = reader.getJSONArray("status");
                for (int x = 0; x < recs.length(); x++) {
                    mappingTable.fromJson(recs.getJSONObject(x));
                }
            }catch (Exception e){}
        }
    }

    public void syncVillages () {
        String syncResults;
        VillageTable villageTable = new VillageTable(context);
        try {
            syncResults = this.syncClient(villageTable.getJson(),
                    HttpServer.VILLAGE_URL);
        } catch (Exception e){
            Log.d("Tremap", "================ERROR PUSHING VILLAGES =================");
            Log.d("Tremap", e.getMessage());
        }
    }

    public void syncLinkFacilities () {
        String syncResults;
        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(context);
        try {
            syncResults = this.syncClient(linkFacilityTable.getJson(),
                    HttpServer.LINKFACILITY_URL);
        } catch (Exception e){
            syncResults = null;
        }
        if (syncResults != null){
            try {

                JSONObject reader = new JSONObject(syncResults);
                JSONArray recs = reader.getJSONArray("status");
                for (int x = 0; x < recs.length(); x++) {
                    linkFacilityTable.fromJson(recs.getJSONObject(x));
                }
            }catch (Exception e){}
        }
    }
}