package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.mzigos.PartnerActivity;
import com.expansion.lg.kimaru.expansion.mzigos.Partners;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
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

/**
 * Created by kimaru on 7/19/17.
 */

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
                    Crashlytics.setString("Err: Process Community Unit",e.getMessage());
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
                    Crashlytics.setString("Err: Process Education",e.getMessage());
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
                    Crashlytics.setString("Err: Process Iccm components",e.getMessage());
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
                    Crashlytics.setString("Err: Process mapping",e.getMessage());
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
                    Crashlytics.setString("Err: Process Mobilization",e.getMessage());
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
                    Crashlytics.setString("Err: Process Parish",e.getMessage());
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
                    Crashlytics.setString("Err: Process Partner Activity",e.getMessage());
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
                    Crashlytics.setString("Err: Process Partners",e.getMessage());
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
                    Crashlytics.setString("Err: Process SubCounty",e.getMessage());
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
                    Crashlytics.setString("Err: Process Training Classes",e.getMessage());
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
                    Crashlytics.setString("Err: Process Training Roles",e.getMessage());
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
                    Crashlytics.setString("Err: Process Trainings",e.getMessage());
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
                    Crashlytics.setString("Err: Training Traninees",e.getMessage());
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
                    Crashlytics.setString("Err: Process Training Trainers",e.getMessage());
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
                    Crashlytics.setString("Err: Process Villages",e.getMessage());
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
                    Crashlytics.setString("Err: Process Wards",e.getMessage());
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
                    Crashlytics.setString("Err: Process Link Facility",e.getMessage());
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
                    Crashlytics.setString("Err: Process Chew Referral",e.getMessage());
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
                    Crashlytics.setString("Err: Process Exams",e.getMessage());
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
                    Crashlytics.setString("Err: Process Registrations",e.getMessage());
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
                    Crashlytics.setString("Err: Process Recruitment",e.getMessage());
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
                    Crashlytics.setString("Err: Process Interviews",e.getMessage());
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
            Crashlytics.setString("Err: Post Interviews",e.getMessage());
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
            Crashlytics.setString("Err: post link facilities",e.getMessage());
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
            Crashlytics.setString("Err: post chews referral",e.getMessage());
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
            Crashlytics.setString("Err: post community unit",e.getMessage());
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
            Log.d("ERR: Sync Educations", e.getMessage());
            postResults = null;
            Crashlytics.setString("Err: post Education",e.getMessage());
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
            Crashlytics.setString("Err: post Iccm Components",e.getMessage());
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
            Crashlytics.setString("Err: post Mapping",e.getMessage());
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
            Crashlytics.setString("Err: post Mobilization",e.getMessage());
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
            Crashlytics.setString("Err: post Parish",e.getMessage());
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
            Crashlytics.setString("Err: post PartnerActivity",e.getMessage());
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
            Crashlytics.setString("Err: post Partners",e.getMessage());
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
            Crashlytics.setString("Err: post SubCounty",e.getMessage());
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
            Crashlytics.setString("Err: post Training",e.getMessage());
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
            Crashlytics.setString("Err: post Training Roles",e.getMessage());
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
            Crashlytics.setString("Err: post Trainings",e.getMessage());
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
            Crashlytics.setString("Err: post Training Trainees",e.getMessage());
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
            Crashlytics.setString("Err: post Training Trainers",e.getMessage());
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
            Crashlytics.setString("Err: post Villages",e.getMessage());
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
            Crashlytics.setString("Err: post Wards",e.getMessage());
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

    /**
     * Syncs recruitment data
     */
    public void syncRecruitments () {
        RecruitmentTable recruitmentTable = new RecruitmentTable(context);
        long pendingRecords = recruitmentTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            String syncResults = null;
            try {
                JSONObject payload = recruitmentTable.getRecruitmentToSyncAsJson(Constants.SYNC_PAGINATION_SIZE * errors);
                syncResults = this.syncClient(payload, HttpServer.RECRUIRMENT_URL);

                if (syncResults != null){
                    JSONObject reader = new JSONObject(syncResults);
                    JSONArray recs = reader.getJSONArray("status");

                    for (int x = 0; x < recs.length(); x++) {
                        JSONObject rec = recs.getJSONObject(x);
                        String id = rec.getString("id");
                        String status = rec.getString("status");

                        Recruitment recruitment = recruitmentTable.getRecruitmentById(id);
                        recruitment.setSynced(status.equals("ok") ? 1 : 0);
                        recruitmentTable.addData(recruitment);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Recruitments",e.getMessage());
            }
        }
    }

    /**
     * Sync Registrations data
     */
    public void syncRegistrations () {
        RegistrationTable registrationTable = new RegistrationTable(context);
        long pendingRecords = registrationTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            String syncResults = null;
            try {
                JSONObject payload = registrationTable.getRegistrationToSyncAsJson(Constants.SYNC_PAGINATION_SIZE * errors);
                syncResults = this.syncClient(payload, HttpServer.REGISTRATION_URL);
                if (syncResults != null) {
                    JSONObject reader = new JSONObject(syncResults);
                    JSONArray recs = reader.getJSONArray("status");
                    for (int x = 0; x < recs.length(); x++) {
                        JSONObject rec = recs.getJSONObject(x);
                        String id = rec.getString("id");
                        String status = rec.getString("status");
                        Registration registration = registrationTable.getRegistrationById(id);
                        registration.setSynced(status.equals("ok") ? 1 : 0);
                        registrationTable.addData(registration);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Registrations",e.getMessage());
            }
        }
    }

    /**
     * Syncs interview data
     */
    public void syncInterviews () {
        InterviewTable interviewTable = new InterviewTable(context);
        long pendingRecords = interviewTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            try {
                JSONObject payload = interviewTable.getInterviewsToSyncAsJson(Constants.SYNC_PAGINATION_SIZE * errors);
                String syncResults = this.syncClient(payload, HttpServer.INTERVIEW_URL);
                if (syncResults != null) {
                    JSONObject reader = new JSONObject(syncResults);

                    JSONArray recs = reader.getJSONArray("status");
                    for (int x = 0; x < recs.length(); x++) {
                        JSONObject rec = recs.getJSONObject(x);
                        String id = rec.getString("id");
                        String status = rec.getString("status");
                        Interview interview = interviewTable.getInterviewById(id);
                        interview.setSynced(status.equals("ok") ? 1 : 0);
                        interviewTable.addData(interview);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Interviews",e.getMessage());
            }
        }
    }

    /**
     * Syncs the exams
     */
    public void syncExams () {
        ExamTable examTable = new ExamTable(context);
        long pendingRecords = examTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            String syncResults;
            try {
                JSONObject payload = examTable.getExamsToSyncAsJson(Constants.SYNC_PAGINATION_SIZE * errors);
                syncResults = this.syncClient(payload, HttpServer.EXAM_URL);

                if (syncResults != null) {
                    JSONObject reader = new JSONObject(syncResults);

                    JSONArray recs = reader.getJSONArray("status");

                    for (int x = 0; x < recs.length(); x++) {
                        JSONObject rec = recs.getJSONObject(x);
                        String id = rec.getString("id");
                        String status = rec.getString("status");
                        Exam exam = examTable.getExamById(id);
                        exam.setSynced(status.equals("ok") ? 1 : 0);
                        examTable.addData(exam);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Exams",e.getMessage());
            }
        }
    }

    /**
     * Sync community units
     */
    public void syncCommunityUnits () {
        CommunityUnitTable communityUnitTable = new CommunityUnitTable(context);
        long pendingRecords = communityUnitTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            try {
                JSONObject payload = communityUnitTable.getPayload(Constants.SYNC_PAGINATION_SIZE * errors);
                String syncResults = this.syncClient(payload, HttpServer.CU_URL);

                if (syncResults != null) {
                    JSONObject results = new JSONObject(syncResults);
                    JSONArray statusArr = results.getJSONArray("status");
                    for (int x = 0; x < statusArr.length(); x++) {
                        JSONObject statusObj = statusArr.getJSONObject(x);
                        String id  = statusObj.getString("id");
                        CommunityUnit cu = communityUnitTable.getCommunityUnitById(id);
                        cu.setSynced(Constants.SYNC_STATUS_SYNCED);
                        communityUnitTable.addCommunityUnitData(cu);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Community Units",e.getMessage());
            }
        }
    }

    /**
     * Sync CHEW Referral data
     */
    public void syncReferrals () {
        ChewReferralTable chewReferralTable = new ChewReferralTable(context);
        long pendingRecords = chewReferralTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            try {
                JSONObject payload = chewReferralTable.getPayload(Constants.SYNC_PAGINATION_SIZE * errors);
                String syncResults = this.syncClient(payload, HttpServer.CHEW_REFERRAL_URL);

                if (syncResults != null) {
                    JSONObject results = new JSONObject(syncResults);
                    JSONArray statusArr = results.getJSONArray("status");
                    for (int x = 0; x < statusArr.length(); x++) {
                        JSONObject statusObj = statusArr.getJSONObject(x);
                        String id  = statusObj.getString("id");
                        ChewReferral chewReferral = chewReferralTable.getChewReferralById(id);
                        chewReferral.setSynced(Constants.SYNC_STATUS_SYNCED);
                        chewReferralTable.addChewReferral(chewReferral);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Referrals",e.getMessage());
            }

        }

    }

    /**
     * Sync parish data
     */
    public void syncParishes () {
        ParishTable parishTable = new ParishTable(context);
        long pendingRecords = parishTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            try {
                JSONObject payload = parishTable.getPayload(Constants.SYNC_PAGINATION_SIZE * errors);
                String syncResults = this.syncClient(payload, HttpServer.PARISH_URL);

                if (syncResults != null) {
                    JSONObject results = new JSONObject(syncResults);
                    JSONArray statusArr = results.getJSONArray("status");
                    for (int x = 0; x < statusArr.length(); x++) {
                        JSONObject statusObj = statusArr.getJSONObject(x);
                        String id  = statusObj.getString("id");
                        Parish parish = parishTable.getParishById(id);
                        parish.setSynced(Constants.SYNC_STATUS_SYNCED);
                        parishTable.addData(parish);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Parishes",e.getMessage());
            }

        }
    }

    /**
     * Sync partner data
     */
    public void syncPartners () {
         PartnersTable partnersTable = new PartnersTable(context);
         long pendingRecords = partnersTable.getPendingRecordCount();

         if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            try {
                JSONObject payload = partnersTable.getPayload(errors * Constants.SYNC_PAGINATION_SIZE);
                String syncResults = this.syncClient(payload, HttpServer.PARTNERS_URL);
                if (syncResults != null) {
                    JSONObject results = new JSONObject(syncResults);
                    JSONArray statusArr = results.getJSONArray("status");
                    for (int x = 0; x < statusArr.length(); x++) {
                        JSONObject statusObj = statusArr.getJSONObject(x);
                        String id  = statusObj.getString("id");
                        String status = statusObj.getString("status");
                        Partners partners = partnersTable.getPartnerById(id);
                        partners.setSynced(status.equals("ok") ? Constants.SYNC_STATUS_SYNCED : Constants.SYNC_STATUS_UNSYNCED);
                        partnersTable.addData(partners);
                    }
                }

            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Partners",e.getMessage());
            }
        }
    }

    public void syncPartnersActivity() {
        PartnerActivityTable partnerActivityTable = new PartnerActivityTable(context);
        long pendingRecords = partnerActivityTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {

            try {
                JSONObject payload = partnerActivityTable.getPayload(Constants.SYNC_PAGINATION_SIZE * errors);
                String syncResults = this.syncClient(payload, HttpServer.PARTNERS_ACTIVITY_URL);

                if (syncResults != null) {
                    JSONObject results = new JSONObject(syncResults);
                    JSONArray statusArr = results.getJSONArray("status");
                    for (int x = 0; x < statusArr.length(); x++) {
                        JSONObject statusObj = statusArr.getJSONObject(x);
                        String id  = statusObj.getString("id");
                        String status = statusObj.getString("status");
                        PartnerActivity partners = partnerActivityTable.getPartnerActivityById(id);
                        partners.setSynced(status.equals("ok") ? Constants.SYNC_STATUS_SYNCED : Constants.SYNC_STATUS_UNSYNCED);
                        partnerActivityTable.addData(partners);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Partners Activity",e.getMessage());
            }
        }
    }

    /**
     * Sync mapping data
     */
    public void syncMapping () {
        MappingTable mappingTable = new MappingTable(context);
        long pendingRecords = mappingTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            try {
                JSONObject payload = mappingTable.getPayload(Constants.SYNC_PAGINATION_SIZE * errors);
                String syncResults = this.syncClient(payload, HttpServer.MAPPING_URL);
                if (syncResults != null) {
                    JSONObject results = new JSONObject(syncResults);
                    JSONArray statusArr = results.getJSONArray("status");
                    for (int x = 0; x < statusArr.length(); x++) {
                        JSONObject statusObj = statusArr.getJSONObject(x);
                        String id  = statusObj.getString("id");
                        Mapping mapping = mappingTable.getMappingById(id);
                        mapping.setSynced(Constants.SYNC_STATUS_SYNCED);
                        mappingTable.addData(mapping);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Mapping",e.getMessage());
            }
        }
    }

    /**
     * Sync village data
     */
    public void syncVillages () {
        VillageTable villageTable = new VillageTable(context);
        long pendingRecords = villageTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            try {
                JSONObject payload = villageTable.getPayload(Constants.SYNC_PAGINATION_SIZE * errors);
                String syncResults = this.syncClient(payload, HttpServer.VILLAGE_URL);
                if (syncResults != null) {
                    JSONObject results = new JSONObject(syncResults);
                    JSONArray statusArr = results.getJSONArray("status");
                    for (int x = 0; x < statusArr.length(); x++) {
                        JSONObject statusObj = statusArr.getJSONObject(x);
                        String id  = statusObj.getString("id");
                        Village village = villageTable.getVillageById(id);
                        village.setSynced(Constants.SYNC_STATUS_SYNCED);
                        villageTable.addData(village);
                    }
                }

            } catch (Exception e){
                e.printStackTrace();
                errors++;
                Crashlytics.setString("Err: Sync Villages",e.getMessage());
            }
        }
    }

    /**
     * Sync Link Facility data
     */
    public void syncLinkFacilities () {
        String syncResults;
        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(context);
        long pendingRecords = linkFacilityTable.getPendingRecordCount();

        if (pendingRecords == 0) return;

        int max = (int) Math.ceil(pendingRecords / (double) Constants.SYNC_PAGINATION_SIZE);
        int errors = 0;
        for (int i = 0; i < max; i++) {
            try {
                JSONObject payload = linkFacilityTable.getPayload(Constants.SYNC_PAGINATION_SIZE * errors);
                syncResults = this.syncClient(payload, HttpServer.LINKFACILITY_URL);
                if (syncResults != null) {
                    JSONObject results = new JSONObject(syncResults);
                    JSONArray statusArr = results.getJSONArray("status");
                    for (int x = 0; x < statusArr.length(); x++) {
                        JSONObject statusObj = statusArr.getJSONObject(x);
                        String id  = statusObj.getString("id");
                        LinkFacility linkFacility = linkFacilityTable.getLinkFacilityById(id);
                        linkFacility.setSynced(Constants.SYNC_STATUS_SYNCED);
                        linkFacilityTable.addData(linkFacility);
                    }
                }
            } catch (Exception e){
                Crashlytics.setString("Err: Sync Link Facilities",e.getMessage());
                e.printStackTrace();
                errors++;
            }
        }
    }
}