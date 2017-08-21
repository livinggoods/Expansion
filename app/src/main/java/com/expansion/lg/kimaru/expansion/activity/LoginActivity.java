package com.expansion.lg.kimaru.expansion.activity;

import android.app.Activity;
import android.content.Intent;

import android.database.Cursor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.sync.ApiClient;
import com.expansion.lg.kimaru.expansion.sync.IccmDataSync;
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.UserTable;
import com.expansion.lg.kimaru.expansion.sync.HttpServer;
import com.expansion.lg.kimaru.expansion.sync.UserDataSync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    // get the Email and pwd fields
    EditText txtUsername, txtPassword;
    // and the button
    Button btnLogin;
    AlertDialogManager alert = new AlertDialogManager();

    SessionManagement session;

    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Session Manager
        session = new SessionManagement(getApplicationContext());
//        session.logoutUser();

        //Get our btns here;
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);


        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View arg0){
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    //Cursor theUser = dbHelper.fetchUser(thisUsername, thisPassword);

                    if (loginUser(username, password)){
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        alert.showAlertDialog(LoginActivity.this, "Wrong Credentials", "Email / password combination", true, null, null);
                    }

                }else{
                    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter both the Email and password", true, null, null);
                }
            }
        });
    }

    public boolean loginUser(String userName, String passWord){
        UserTable userTable = new UserTable(getBaseContext());
        Cursor user = userTable.fetchUser(userName, passWord);
        if (user != null){
            if (user.getCount() > 0){
                session.createLoginSesstion(user.getString(4), user.getString(1), user.getInt(0), user.getString(5));
                // at this point, let us sync the locations
                Toast.makeText(getBaseContext(), "Please wait ...", Toast.LENGTH_SHORT).show();
                if (session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("UG")){
                    new syncLocations().execute(Constants.CLOUD_ADDRESS+"/api/v1/sync/locations");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume (){
        super.onResume();

        Thread userThread = new Thread(new Runnable() {
            @Override
            public void run() {
                UserDataSync dSync = new UserDataSync(getBaseContext());
                dSync.pollNewUsers();
            }
        });
        userThread.start();

        Thread iccmThread = new Thread(new Runnable() {
            @Override
            public void run() {
                IccmDataSync iccmDataSync = new IccmDataSync(getBaseContext());
                iccmDataSync.pollNewComponents();
            }
        });
        iccmThread.start();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private class syncLocations extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("locations");
                    CountyLocationTable countyLocationTable = new CountyLocationTable(getBaseContext());
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
}

