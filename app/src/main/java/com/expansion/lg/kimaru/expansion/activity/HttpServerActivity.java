package com.expansion.lg.kimaru.expansion.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.dbhelpers.Recruitment;
import com.expansion.lg.kimaru.expansion.dbhelpers.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.dbhelpers.Registration;
import com.expansion.lg.kimaru.expansion.dbhelpers.RegistrationTable;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONArray;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class HttpServerActivity extends AppCompatActivity {

    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer asyncServer = new AsyncServer();
    Button enableServer;
    Button stopServer;

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
        //startServer();
    }
//
//    private void setRepeatingAsyncTask() {
//
//        final Handler handler = new Handler();
//        Timer timer = new Timer();
//
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            AsyncTaskParseJson jsonTask = new AsyncTaskParseJson();
//                            jsonTask.execute();
//                        } catch (Exception e) {
//                            // error, do something
//                        }
//                    }
//                });
//            }
//        };
//
//        timer.schedule(task, 0, 60*1000);  // interval of one minute
//
//    }

    private void pollNewRecords(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // get new records
                            Toast.makeText(getBaseContext(), "Fetching new Records", Toast.LENGTH_SHORT).show();
                        } catch (Exception e){
                            //Error in fetching records
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 60*1000 /2);
    }

    private void startServer(){
        server.get("/recruitments", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                RecruitmentTable recruitmentData = new RecruitmentTable(getBaseContext());
                response.send(recruitmentData.getRecruitmentJson());
            }
        });
        server.post("/reruitments", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                response.send("Details saved");
            }
        });

        server.listen(asyncServer, 8090);
    }

    public void syncRecords(){
        // open the server URL for recruitments

    }
}
