package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import com.expansion.lg.kimaru.expansion.mzigos.IccmComponent;
import com.expansion.lg.kimaru.expansion.mzigos.User;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable;
import com.expansion.lg.kimaru.expansion.tables.UserTable;

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

public class IccmDataSync {

    Context context;

    public IccmDataSync(Context context){
        this.context = context;
    }

    public void pollNewComponents(){
        final Handler handler = new Handler(Looper.getMainLooper());
        Timer timer = new Timer();
        TimerTask getUsersTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = Constants.CLOUD_ADDRESS+"/api/v1/sync/iccm-components";
                        new syncIccmComponents().execute(url);
                    }
                });
            }
        };
        timer.schedule(getUsersTask, 0, 60*1000 * 30); //every 30 minutes
    }


    private class syncIccmComponents extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("components");
                    IccmComponentTable iccmComponentTable = new IccmComponentTable(context);
                    List<IccmComponent> componentList = new ArrayList<IccmComponent>();
                    for (int x = 0; x < recs.length(); x++){
                        // create the component
                        iccmComponentTable.fromJson(recs.getJSONObject(x));
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
