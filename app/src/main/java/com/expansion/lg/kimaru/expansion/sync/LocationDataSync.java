package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.expansion.lg.kimaru.expansion.tables.WardTable;

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

    public LocationDataSync(Context context){
        this.context = context;
    }

    public void pollLocations(){
        CountyLocationTable countyLocationTable = new CountyLocationTable(context);
        countyLocationTable.createLocations();
    }

    public void getKeSubcounties(){
        new syncKeWards().execute(Constants.CLOUD_ADDRESS+"/api/v1/sync/ke-counties");
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
}
