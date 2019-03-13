package com.expansion.lg.kimaru.expansion.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.database.Cursor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.sync.ApiClient;
import com.expansion.lg.kimaru.expansion.sync.IccmDataSync;
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync;
import com.expansion.lg.kimaru.expansion.sync.TremapApi;
import com.expansion.lg.kimaru.expansion.sync.TremapApiClient;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.UserTable;
import com.expansion.lg.kimaru.expansion.sync.HttpServer;
import com.expansion.lg.kimaru.expansion.sync.UserDataSync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    // get the Email and pwd fields
    EditText txtUsername, txtPassword;
    // and the button
    Button btnLogin, buttonRefresh;
    AlertDialogManager alert = new AlertDialogManager();

    SessionManagement session;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_login);
        MainActivity.backFragment = null;

        //Session Manager
        session = new SessionManagement(getApplicationContext());
//        session.logoutUser();

        //Get our btns here;
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);


        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                if (username.trim().length() > 0 && password.trim().length() > 0) {

                    loginUserApi(username, password);

                } else {
                    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter both the Email and password", true, null, null);
                }
            }
        });
        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread userThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserDataSync dSync = new UserDataSync(getBaseContext());
                        dSync.pollNewUsers();
                    }
                });
                userThread.start();
                Toast.makeText(getApplicationContext(), "Refreshing users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Login into the app through the API
     * @param email User's email
     * @param password User's password
     */
    public void loginUserApi(final String email, String password) {

        TremapApi api = TremapApiClient.getClient(LoginActivity.this).create(TremapApi.class);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<ResponseBody> call = api.loginUser(email, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                try {

                    int code = response.code();

                    if (code >= 200 && code < 300) {

                        Answers.getInstance().logLogin(new LoginEvent()
                                .putMethod("LoginUserApi")
                                .putSuccess(true)
                                .putCustomAttribute("User ", txtUsername.toString()));


                        String body = response.body().string();
                        performPostLogin(body);

                    } else {

                        String body = response.errorBody().string();
                        JSONObject json = new JSONObject(body);
                        String error = json.getString("error");
                        onFailure(call, new Throwable(error));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    onFailure(call, ex.getCause());
                    Crashlytics.logException(ex);}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

                String message = "An unexpected error has occured. Please try again";

                if (t instanceof IOException) {
                    Toast.makeText(LoginActivity.this, "Please check your internet connection and try again", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (t != null) message = t.getMessage();

                Crashlytics.setUserEmail(email);
                Crashlytics.setString("message",message);
                alert.showAlertDialog(LoginActivity.this, "Error", message, true, null, null);
            }
        });

    }

    /**
     * Performs post login actions
     * - Creating user session
     * -
     *
     * @param body JSON string response from the server
     * @throws JSONException
     */
    private void performPostLogin(String body) throws JSONException {

        JSONObject json = new JSONObject(body);

        JSONObject user = json.getJSONObject("user");
        JSONObject tokenObj = json.getJSONObject("auth_token");

        String name = user.getString("name");
        String email = user.getString("email");
        int userId = user.getInt("id");
        String country = user.getString("location");

        String token = tokenObj.getString("token");

        SessionManagement session = new SessionManagement(LoginActivity.this);
        session.createLoginSesstion(name, email, userId, country, token);

        if (session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("UG")) {

            new syncLocations().execute(new Constants(getApplicationContext()).getCloudAddress() + "/api/v1/sync/locations");

        } else {

            IccmDataSync iccmDataSync = new IccmDataSync(getBaseContext());
            iccmDataSync.pollNewComponents();
            LocationDataSync locationDataSync = new LocationDataSync(getBaseContext());
            locationDataSync.getKeSubcounties();

        }

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onResume() {
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
        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if (stream != null) {
                try {
                    JSONObject reader = new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("locations");
                    CountyLocationTable countyLocationTable = new CountyLocationTable(getBaseContext());
                    for (int x = 0; x < recs.length(); x++) {
                        countyLocationTable.fromJson(recs.getJSONObject(x));
                    }
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                }

            }
            return stream;
        }

        protected void onPostExecute(String stream) {

        } // onPostExecute() end
    }
}

