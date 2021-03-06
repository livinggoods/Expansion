package com.expansion.lg.kimaru.expansion.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.expansion.lg.kimaru.expansion.BuildConfig;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.other.ExportDataToCsv;
import com.expansion.lg.kimaru.expansion.sync.ApiClient;
import com.expansion.lg.kimaru.expansion.sync.HttpClient;
import com.expansion.lg.kimaru.expansion.sync.HttpServer;
import com.expansion.lg.kimaru.expansion.sync.IccmDataSync;
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync;
import com.expansion.lg.kimaru.expansion.sync.UserDataSync;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.MappingTable;
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable;
import com.expansion.lg.kimaru.expansion.tables.ParishTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.expansion.lg.kimaru.expansion.tables.VillageTable;
import com.expansion.lg.kimaru.expansion.tables.WardTable;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;


public class HttpServerActivity extends AppCompatActivity implements View.OnClickListener {




    Button enableServer, buttonGetSubCounty, recreateLocationTable;
    Button resyncLocations, shareRecords, syncIccmComponents, buttonExtractJson;
    SweetAlertDialog pDialog;

    Context context;
    SessionManagement session;
    private LocationDataSync locationDataSync;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_http_server);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        session = new SessionManagement(context);
        locationDataSync = new LocationDataSync(getBaseContext());

        enableServer = (Button) findViewById(R.id.buttonStartServer);
        resyncLocations = (Button) findViewById(R.id.buttonLocationSync);
        shareRecords = (Button) findViewById(R.id.buttonShareRecords);
        buttonGetSubCounty = (Button) findViewById(R.id.buttonGetSubCounty);
        syncIccmComponents = (Button) findViewById(R.id.iccmComponents);
        recreateLocationTable = (Button) findViewById(R.id.recreateLocationTable);
        buttonExtractJson = (Button) findViewById(R.id.buttonExtractJson);
        progressDialog = new ProgressDialog(context);

        enableServer.setOnClickListener(this);
        shareRecords.setOnClickListener(this);
        resyncLocations.setOnClickListener(this);
        buttonGetSubCounty.setOnClickListener(this);
        syncIccmComponents.setOnClickListener(this);
        recreateLocationTable.setOnClickListener(this);
        buttonExtractJson.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view){
        MainActivity mainActivity = new MainActivity();
        switch (view.getId()){
            case R.id.buttonStartServer:
                HttpServer server = new HttpServer(getBaseContext());
                Log.d("Tremap Sync", "Starting server");
                server.startServer();

                Toast.makeText(getBaseContext(), "Server started successfully",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonShareRecords:
                Log.d("Tremap Sync", "Starting Client");
                HttpClient httpClient = new HttpClient(getBaseContext());
                httpClient.startClient();

                Toast.makeText(getBaseContext(), "Sharing of records enabled.",
                        Toast.LENGTH_SHORT).show();

                break;
            case R.id.buttonLocationSync:
                syncLocationsInForeGround();
                break;

            case R.id.buttonGetSubCounty:
                syncKeWardsInForeground();
                break;

            case R.id.iccmComponents:
                Thread iccmThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        IccmDataSync iccmDataSync = new IccmDataSync(getBaseContext());
                        iccmDataSync.pollNewComponents();
                    }
                });
                iccmThread.start();
                Toast.makeText(getBaseContext(), "Getting ICCM components ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.recreateLocationTable:
                CountyLocationTable countyLocationTable = new CountyLocationTable(getBaseContext());
                countyLocationTable.reCreateDb();
                syncLocationsInForeGround();
                break;

            case R.id.buttonExtractJson:
                Dexter.withActivity(this)
                        .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()){
                                    exportJsonData();
                                }
                                if (report.isAnyPermissionPermanentlyDenied()){
                                    Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .withErrorListener(new PermissionRequestErrorListener() {
                            @Override
                            public void onError(DexterError error) {
                                Toast.makeText(getApplicationContext(), "Error Occured: "+
                                error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onSameThread()
                        .check();
                break;
        }
    }


    @Override
    public void onResume (){
        super.onResume();
    }



    public void syncKeWardsInForeground(){
        new syncKeWardsInForeground().execute(new Constants(getBaseContext()).getCloudAddress() + "/api/v1/sync/ke-counties");
    }
    private class syncKeWardsInForeground extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Loading KE Locations. Please wait");
            pDialog.show();
            pDialog.setCancelable(false);
        }

        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("counties");
                    SubCountyTable subCountyTable = new SubCountyTable(context);
                    for (int x = 0; x < recs.length(); x++){
                        // I have the county Details, lets extract all subcounties
                        JSONObject county = recs.getJSONObject(x);
                        JSONArray subCounties = county.getJSONArray("subcounties");
                        for (int a = 0; a<subCounties.length(); a++){
                            // I have the subcounty, create the subcounty
                            JSONObject subCounty = subCounties.getJSONObject(a);
                            new SubCountyTable(context).fromJson(subCounty);
                            //also get the wards
                            JSONArray wards = subCounty.getJSONArray("wards");
                            for (int b=0; b<wards.length(); b++){
                                JSONObject ward = wards.getJSONObject(b);
                                //create a ward
                                new WardTable(context).fromJson(ward);
                            }
                        }
                    }
                }catch(JSONException e){
                    Log.d("TREMAP", "KE County Sync ERROR "+e.getMessage());
                    Crashlytics.logException(e);
                }

            }
            return stream;
        }
        protected void onPostExecute(String stream){
            session.flagSynced(false); // just to force the app to resync the data.
            pDialog.dismiss();
        } // onPostExecute() end
    }




    public void syncLocationsInForeGround(){
        new syncLocationsInForeGround().execute(new Constants(getBaseContext()).getCloudAddress() + "/api/v1/sync/locations");
    }
    private class syncLocationsInForeGround extends AsyncTask<String, HashMap, String> {
        Integer totalRecords = 0;
        Integer processedRecords = 0;
        String recordType = "";
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            recordType= "Location";
            progressDialog.setTitle("Syncing "+ recordType);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... strings){
            String stream = null;
            HashMap<String, String> progress = new HashMap<String, String>();
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("locations");
                    CountyLocationTable countyLocationTable = new CountyLocationTable(getBaseContext());
                    totalRecords = recs.length();
                    for (int x = 0; x < recs.length(); x++){
                        countyLocationTable.fromJson(recs.getJSONObject(x));
                        processedRecords = x;
                        progress = new HashMap<String, String>();
                        progress.put("total", totalRecords.toString());
                        progress.put("processed", processedRecords.toString());
                        progress.put("type", "Location");
                        publishProgress(progress);
                    }
                }catch(JSONException e){
                    Crashlytics.logException(e);
                }
            }
            recordType= "Parish";
            processedRecords = 0;
            progress = new HashMap<String, String>();
            progress.put("total", "100");
            progress.put("processed", "0");
            progress.put("type", "Parish");
            progress.put("message", "Downloading parishes from the Cloud ... ");
            progress.put("title", "Getting Parishes");
            progress.put("action", "Downloading");
            publishProgress(progress);

            String parishStream = hh.GetHTTPData(new Constants(context).getCloudAddress()+"/api/v1/sync/parish");
            if(parishStream !=null){
                try{
                    JSONObject reader= new JSONObject(parishStream);
                    JSONArray recs = reader.getJSONArray("parish");
                    ParishTable parishTable = new ParishTable(context);
                    totalRecords = recs.length();
                    for (int y = 0; y < recs.length(); y++){
                        JSONObject parish = recs.getJSONObject(y);
                        progress = new HashMap<String, String>();
                        progress.put("total", totalRecords.toString());
                        progress.put("processed", String.valueOf(y));
                        progress.put("type", "Parish");
                        progress.put("message", "Processing parishes ... ");
                        progress.put("title", "Processing Parish");
                        publishProgress(progress);
                        parishTable.fromJson(parish);
                    }
                }catch(JSONException e){
                    Log.d("TREMAP", "ERROR Creating parish:\n "+e.getMessage());
                    Crashlytics.logException(e);
                }
            }
            return stream;

        }
        protected void onPostExecute(String stream){
            session.flagSynced(false); // just to force the app to resync the data.
            progressDialog.dismiss();
        } // onPostExecute() end

        @Override
        protected void onProgressUpdate(HashMap... updates){
            super.onProgressUpdate(updates);
            HashMap<String, String> progress = updates[0];
            if (progress.containsKey("action")){
                progressDialog.setProgress(0);
                progressDialog.setMax(100);
                progressDialog.setMessage(progress.get("message"));
                progressDialog.setTitle(progress.get("title"));
            }else{
                progressDialog.setProgress(Integer.valueOf(progress.get("processed")));
                progressDialog.setMax(Integer.valueOf(progress.get("total")));
                progressDialog.setMessage("Syncing "+ progress.get("type") +"\n Please wait ... ");
                progressDialog.setTitle("Syncing "+ progress.get("type"));
            }

        }
    }


    private void exportJsonData(){
        // only export the scoring tool for the current recruitment
        // 1. Check if the Externa Storage is available
        String state = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(state)){
            return;
        }else{
            //We used Download Dir
            //File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File exportDir = new File(Environment.getExternalStorageDirectory() + "/Download/tremap/mapping/");
            if(!exportDir.exists()){
                exportDir.mkdirs();
            }
            File file;
            PrintWriter printWriter = null;
            file = new File(exportDir, "mapping_data.json");
            try {
                file.createNewFile();
                printWriter = new PrintWriter(new FileWriter(file));

                //we get villages
                VillageTable villageTable = new VillageTable(getApplicationContext());
                JSONObject villageJson = villageTable.getJson();
                printWriter.println(villageJson.toString());


                //we get mapping
                MappingTable mappingTable = new MappingTable(getApplicationContext());
                JSONObject mappingTableJson = mappingTable.getJson();
                printWriter.println(mappingTableJson.toString());


                //we get parishes
                ParishTable parishTable = new ParishTable(getApplicationContext());
                JSONObject parishJson = parishTable.getJson();
                Log.d("Tremap","}{{}{}{{}{}{}{}{}{}{}{}{}{}{}{}{}{}}{}{}{}{}{}{}{{}{{}");
                Log.d("Tremap",parishJson.toString());
                Log.d("Tremap","}{{}{}{{}{}{}{}{}{}{}{}{}{}{}{}{}{}}{}{}{}{}{}{}{{}{{}");
                printWriter.println(parishJson.toString());


                //we get mobilization
                MobilizationTable mobilizationTable = new MobilizationTable(getApplicationContext());
                JSONObject mobilizationJson = mobilizationTable.getMobilizationJson();
                printWriter.println(mobilizationJson.toString());

                //we get link Facilities
                LinkFacilityTable linkFacilityTable = new LinkFacilityTable(getApplicationContext());
                JSONObject linkFacilityJson =  linkFacilityTable.getJson();
                printWriter.println(linkFacilityJson.toString());




            } catch (Exception e){
                Crashlytics.logException(e);
            }
            finally {
                if(printWriter != null) printWriter.close();
            }
            Toast.makeText(getApplicationContext(), "Data exported to "+ file.getAbsolutePath() +" Folder", Toast.LENGTH_LONG).show();
        }

    }

}
