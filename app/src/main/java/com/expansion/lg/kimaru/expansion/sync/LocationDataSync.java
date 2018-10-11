package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.ParishTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.expansion.lg.kimaru.expansion.tables.VillageTable;
import com.expansion.lg.kimaru.expansion.tables.WardTable;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kimaru on 3/21/17.
 */

public class LocationDataSync {

    Context context;
    public int totalParishes = 0;
    public int processedParishes = 0;

    public LocationDataSync(Context context){
        this.context = context;
    }

    public void pollLocations(){
        CountyLocationTable countyLocationTable = new CountyLocationTable(context);
        new syncCountyLocations().execute(new Constants(context).getCloudAddress()+"/api/v1/sync/locations");
    }

    public void getKeSubcounties(){
        new syncKeWards().execute(new Constants(context).getCloudAddress()+"/api/v1/sync/ke-counties");
    }


    private class syncKeWards extends AsyncTask<String, Void, String> {
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
                }

            }
            return stream;
        }
        protected void onPostExecute(String stream){

        } // onPostExecute() end
    }

    public void getParishDatafromCloud(){
        new syncParishesFromCloud().execute(new Constants(context).getCloudAddress()+"/api/v1/sync/parish");
    }

    public class syncParishesFromCloud extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("parish");
                    ParishTable parishTable = new ParishTable(context);
                    totalParishes = recs.length();
                    for (int x = 0; x < recs.length(); x++){
                        JSONObject parish = recs.getJSONObject(x);
                        parish.put(ParishTable.SYNCED, 1);
                        parishTable.fromJson(parish);
                        processedParishes = x;
                        publishProgress((int) ((x / recs.length()*100)));
                    }
                }catch(JSONException e){
                    Log.d("TREMAP", "ERROR Creating parish:\n "+e.getMessage());
                }

            }
            return stream;
        }
        protected void onProgressUpdate(Integer... progress) {
            //This method runs on the UI thread, it receives progress updates
            //from the background thread and publishes them to the status bar
        }

        protected void onPostExecute(String stream){

        } // onPostExecute() end
    }

    public void getVillageDatafromCloud(){
        new syncVillagesFromCloud().execute(new Constants(context).getCloudAddress()+"/api/v1/sync/villages");
    }
    private class syncVillagesFromCloud extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("villages");
                    VillageTable villageTable = new VillageTable(context);
                    for (int x = 0; x < recs.length(); x++){
                        JSONObject village = recs.getJSONObject(x);
                        village.put(VillageTable.SYNCED, 1);
                        villageTable.fromJson(village);
                    }
                }catch(JSONException e){
                    Log.d("TREMAP", "Error creating Village:\n "+e.getMessage());
                }

            }
            return stream;
        }
        protected void onPostExecute(String stream){

        } // onPostExecute() end
    }




    private class syncCountyLocations extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("locations");
                    CountyLocationTable countyLocationTable = new CountyLocationTable(context);
                    for (int x = 0; x < recs.length(); x++){
                        countyLocationTable.fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                }

            }
            return stream;
        }
        protected void onPostExecute(String stream){

        } // onPostExecute() end
    }

    public int getProcessedParishes() {
        return processedParishes;
    }

    public int getTotalParishes() {
        return totalParishes;
    }
}
