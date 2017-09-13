package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.IccmComponent;
import com.expansion.lg.kimaru.expansion.mzigos.Training;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable;
import com.expansion.lg.kimaru.expansion.tables.TrainingTable;

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

public class TrainingDataSync {

    Context context;

    public TrainingDataSync(Context context){
        this.context = context;
    }

    public void pollNewTrainings(){
        final Handler handler = new Handler(Looper.getMainLooper());
        Timer timer = new Timer();
        TimerTask getTrainingsTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = Constants.API_SERVER+Constants.API_TRAINING;
                        Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        Log.d("Tremap", "BACKGROUND ");
                        Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        new syncTrainings().execute(url);

                        Log.d("Tremap", " RR ");
                    }
                });
            }
        };
        timer.schedule(getTrainingsTask, 0, 60*500 * 1); //every 30 minutes
    }


    private class syncTrainings extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream;
            String urlString = strings[0];
            Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Log.d("Tremap", "DO IN BKC ");
            Log.d("Tremap", "Str "+ urlString);
            Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    Log.d("Tremap", "TRYING ");
                    Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray(TrainingTable.JSON_ROOT);
                    TrainingTable trainingTable = new TrainingTable(context);
                    List<Training> trainingList = new ArrayList<Training>();
                    for (int x = 0; x < recs.length(); x++){
                        trainingTable.fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                    Log.d("TREMAP", "TRAINING ERROR "+ e.getMessage());
                }
            }else {
                Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                Log.d("Tremap", "MEMEMEMEMEMEMEMEMEMEEMMEMEMEMMEEMEMEEMMEMEMEMEMEMEMEMEMEMEMEEM ");
                Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
            return stream;
        }
        protected void onPostExecute(String stream){
        } // onPostExecute() end
    }
}
