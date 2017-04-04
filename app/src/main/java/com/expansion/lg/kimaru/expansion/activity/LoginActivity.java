package com.expansion.lg.kimaru.expansion.activity;

import android.app.Activity;
import android.content.Intent;

import android.database.Cursor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.tables.UserTable;
import com.expansion.lg.kimaru.expansion.sync.HttpServer;
import com.expansion.lg.kimaru.expansion.sync.UserDataSync;

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
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume (){
        super.onResume();
        HttpServer server = new HttpServer(getBaseContext());
        server.startServer();
        UserDataSync dSync = new UserDataSync(getBaseContext());
        dSync.pollNewUsers();
    }
    @Override
    public void onBackPressed() {
//        finish();
    }

    public void startServer(){

    }

}

