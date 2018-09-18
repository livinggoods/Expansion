package com.expansion.lg.kimaru.expansion.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.sync.*
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : Activity() {
    // get the Email and pwd fields
    internal lateinit var txtUsername: EditText
    internal lateinit var txtPassword: EditText
    // and the button
    internal lateinit var btnLogin: Button
    internal lateinit var buttonRefresh: Button
    internal var alert = AlertDialogManager()

    internal lateinit var session: SessionManagement

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        MainActivity.backFragment = null

        //Session Manager
        session = SessionManagement(applicationContext)
        //        session.logoutUser();

        //Get our btns here;
        txtUsername = findViewById<View>(R.id.txtUsername) as EditText
        txtPassword = findViewById<View>(R.id.txtPassword) as EditText


        btnLogin = findViewById<View>(R.id.btnLogin) as Button
        btnLogin.setOnClickListener {
            val username = txtUsername.text.toString()
            val password = txtPassword.text.toString()
            if (username.trim { it <= ' ' }.length > 0 && password.trim { it <= ' ' }.length > 0) {

                loginUserApi(username, password)

            } else {
                alert.showAlertDialog(this@LoginActivity, "Login failed..", "Please enter both the Email and password", true, null, null)
            }
        }
        buttonRefresh = findViewById<View>(R.id.buttonRefresh) as Button
        buttonRefresh.setOnClickListener {
            val userThread = Thread(Runnable {
                val dSync = UserDataSync(baseContext)
                dSync.pollNewUsers()
            })
            userThread.start()
            Toast.makeText(applicationContext, "Refreshing users", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Login into the app through the API
     * @param email User's email
     * @param password User's password
     */
    fun loginUserApi(email: String, password: String) {

        val api = TremapApiClient.getClient(this@LoginActivity).create(TremapApi::class.java)

        val progressDialog = ProgressDialog(this@LoginActivity)
        progressDialog.setMessage("Loading ...")
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.show()

        val call = api.loginUser(email, password)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                progressDialog.dismiss()

                try {

                    val code = response.code()

                    if (code >= 200 && code < 300) {

                        val body = response.body().string()
                        performPostLogin(body)

                    } else {

                        val body = response.errorBody().string()
                        val json = JSONObject(body)
                        val error = json.getString("error")
                        onFailure(call, Throwable(error))
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    onFailure(call, ex.cause)
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable?) {
                progressDialog.dismiss()

                var message = "An unexpected error has occured. Please try again"

                if (t is IOException) {
                    Toast.makeText(this@LoginActivity, "Please check your internet connection and try again", Toast.LENGTH_LONG)
                            .show()
                    return
                }

                if (t != null) message = t.message!!

                alert.showAlertDialog(this@LoginActivity, "Error", message, true, null, null)
            }
        })

    }

    /**
     * Performs post login actions
     * - Creating user session
     * -
     *
     * @param body JSON string response from the server
     * @throws JSONException
     */
    @Throws(JSONException::class)
    private fun performPostLogin(body: String) {

        val json = JSONObject(body)

        val user = json.getJSONObject("user")
        val tokenObj = json.getJSONObject("auth_token")

        val name = user.getString("name")
        val email = user.getString("email")
        val userId = user.getInt("id")
        val country = user.getString("location")

        val token = tokenObj.getString("token")

        val session = SessionManagement(this@LoginActivity)
        session.createLoginSesstion(name, email, userId, country, token)

        if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {

            syncLocations().execute(Constants(applicationContext).cloudAddress + "/api/v1/sync/locations")

        } else {

            val iccmDataSync = IccmDataSync(baseContext)
            iccmDataSync.pollNewComponents()
            val locationDataSync = LocationDataSync(baseContext)
            locationDataSync.getKeSubcounties()

        }

        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    public override fun onResume() {
        super.onResume()

        val userThread = Thread(Runnable {
            val dSync = UserDataSync(baseContext)
            dSync.pollNewUsers()
        })
        userThread.start()

        val iccmThread = Thread(Runnable {
            val iccmDataSync = IccmDataSync(baseContext)
            iccmDataSync.pollNewComponents()
        })
        iccmThread.start()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private inner class syncLocations : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("locations")
                    val countyLocationTable = CountyLocationTable(baseContext)
                    for (x in 0 until recs.length()) {
                        countyLocationTable.fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {

        } // onPostExecute() end
    }
}

