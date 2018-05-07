package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.expansion.lg.kimaru.expansion.mzigos.Education;
import com.expansion.lg.kimaru.expansion.sync.ApiClient;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kimaru on 3/22/17.
 */

public class SetUpApp {
    Context context;

    public SetUpApp(Context context){
        this.context=context;
    }

    public void setUpEducation(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String url = new Constants(context).getApiServer()+"/education";
                new syncEducationFromCloud().execute(url);
            }
        });
    }

    private class syncEducationFromCloud extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("education");
                    EducationTable educationTable = new EducationTable(context);
                    for (int x = 0; x < recs.length(); x++){
                        // create the education
                        educationTable.createEducationFromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                }
            }
            return stream;
        }
        protected void onPostExecute(String stream){
        } // onPostExecute() end
    }
}
