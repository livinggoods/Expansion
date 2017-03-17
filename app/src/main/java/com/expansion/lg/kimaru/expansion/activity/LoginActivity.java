package com.expansion.lg.kimaru.expansion.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

import com.expansion.lg.kimaru.expansion.R;

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

        //Get our btns here;
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        Toast.makeText(getApplicationContext(), "User login status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View arg0){
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (username.equals("kimaru") && password.equals("kimaru")){
                        session.createLoginSesstion("David Kimaru", "kimarudg@ooo.com");
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        alert.showAlertDialog(LoginActivity.this, "Wrong Credentials", "Username/pwd combination", false);
                    }

                }else{
                    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password", false);
                }
            }
        });
    }

}

