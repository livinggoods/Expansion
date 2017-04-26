package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Base64;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;

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
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask getLocationsTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = Constants.CLOUD_ADDRESS+"/api/v1/sync/locations";
                        new syncLocations().execute(url);
                    }
                });
            }
        };
        timer.schedule(getLocationsTask, 0, 60*1000 * 30); //every 30 minutes
    }



    private class syncLocations extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            return stream;
        }
        protected void onPostExecute(String stream){
            // uncomment the following line for debuggin purposes
            Toast.makeText(context, "Syncing Locations", Toast.LENGTH_SHORT).show();
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("locations");
                    CountyLocationTable countyLocationTable = new CountyLocationTable(context);
                    for (int x = 0; x < recs.length(); x++){
                        countyLocationTable.fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    Toast.makeText(context, "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end
    }
}
