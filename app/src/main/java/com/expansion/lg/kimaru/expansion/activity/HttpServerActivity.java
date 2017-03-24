package com.expansion.lg.kimaru.expansion.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.sync.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HttpServerActivity extends AppCompatActivity {




    Button enableServer;
    Button stopServer;

    final String SERVER_URL = "http://192.168.43.1";
    final int SERVER_PORT = 8090;
    final String RECRUIRMENT_URL = "recruitments";
    private static String url;

    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_server);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enableServer = (Button) findViewById(R.id.buttonStartServer);
        stopServer = (Button) findViewById(R.id.buttonStopServer);

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
    public void onResume (){
        super.onResume();
        pollNewRecords();
    }

    private void pollNewRecords(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        syncRecords();
                        url = SERVER_URL+":"+SERVER_PORT+"/"+RECRUIRMENT_URL;
                        new ProcessJSON().execute(url);
                    }
                });
            }
        };
        timer.schedule(task, 0, 60*1000 /4);
    }

    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            Toast.makeText(getBaseContext(), "Start Syncing", Toast.LENGTH_SHORT).show();

            /*
                Important in JSON DATA
                -------------------------
                * Square bracket ([) represents a JSON array
                * Curly bracket ({) represents a JSON object
                * JSON object contains key/value pairs
                * Each key is a String and value may be different data types
             */

            //..........Process JSON DATA................
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONObject "recruitments"...........................
//                    JSONObject coord = reader.getJSONObject("recruitments");

                    // Get the JSONArray recruitments
                    JSONArray recs = reader.getJSONArray("recruitments");
                    // Get the array first JSONObject
                    List<Recruitment> recruitmentList = new ArrayList<Recruitment>();


                    for (int x = 0; x < recs.length(); x++){
                        Recruitment recruitment = new Recruitment();

                        recruitment.setId(Integer.parseInt(recs.getJSONObject(x).getString("_id")));
                        recruitment.setName(recs.getJSONObject(x).getString("title"));
                        recruitment.setDistrict(recs.getJSONObject(x).getString("district"));
                        recruitment.setSubcounty(recs.getJSONObject(x).getString("subcounty"));
                        recruitment.setDivision(recs.getJSONObject(x).getString("division"));
                        recruitment.setLat(recs.getJSONObject(x).getString("lat"));
                        recruitment.setLon(recs.getJSONObject(x).getString("lon"));
                        recruitment.setAddedBy(Integer.parseInt(recs.getJSONObject(x).getString("added_by")));
                        recruitment.setComment(recs.getJSONObject(x).getString("comment"));
                        recruitment.setDateAdded(Integer.parseInt(recs.getJSONObject(x).getString("date_added")));
                        recruitment.setSynced(Integer.parseInt(recs.getJSONObject(x).getString("synced")));

                    }



                    // process other data as this way..............

                }catch(JSONException e){
                    Toast.makeText(getBaseContext(), "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end

}
