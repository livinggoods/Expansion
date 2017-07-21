package com.expansion.lg.kimaru.expansion.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.sync.ApiClient;
import com.expansion.lg.kimaru.expansion.sync.HttpClient;
import com.expansion.lg.kimaru.expansion.sync.HttpServer;
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HttpServerActivity extends AppCompatActivity implements View.OnClickListener {




    Button enableServer, buttonGetSubCounty;
    Button resyncLocations, shareRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_server);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enableServer = (Button) findViewById(R.id.buttonStartServer);
        resyncLocations = (Button) findViewById(R.id.buttonLocationSync);
        shareRecords = (Button) findViewById(R.id.buttonShareRecords);
        buttonGetSubCounty = (Button) findViewById(R.id.buttonGetSubCounty);

        enableServer.setOnClickListener(this);
        shareRecords.setOnClickListener(this);
        resyncLocations.setOnClickListener(this);
        buttonGetSubCounty.setOnClickListener(this);

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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationDataSync locationDataSync = new LocationDataSync(getBaseContext());
                        locationDataSync.pollLocations();
                    }
                });
                thread.start();
                break;

            case R.id.buttonGetSubCounty:
                Toast.makeText(getBaseContext(), "Starting", Toast.LENGTH_SHORT).show();
                Thread locationThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationDataSync locationDataSync = new LocationDataSync(getBaseContext());
                        locationDataSync.getKeSubcounties();
                    }
                });
                locationThread.start();
                Toast.makeText(getBaseContext(), "Sync started", Toast.LENGTH_SHORT).show();
                break;


        }
    }


    @Override
    public void onResume (){
        super.onResume();
    }

}
