package com.expansion.lg.kimaru.expansion.sync

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.mzigos.User
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.UserTable

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

/**
 * Created by kimaru on 3/21/17.
 */

class UserDataSync(internal var context: Context) {

    fun pollNewUsers() {
        val handler = Handler(Looper.getMainLooper())
        val timer = Timer()
        val getUsersTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    val url = Constants(context).cloudAddress + "/api/v1/users/json"
                    syncUsers().execute(url)
                }
            }
        }
        timer.schedule(getUsersTask, 0, (60 * 1000 * 30).toLong()) //every 30 minutes
    }


    private inner class syncUsers : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]

            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("users")
                    val userList = ArrayList<User>()
                    for (x in 0 until recs.length()) {
                        val user = User()
                        user.id = recs.getJSONObject(x).getInt("id")
                        //transmitted in base64
                        val appName = Base64.decode(recs.getJSONObject(x).getString("app_name"),
                                Base64.DEFAULT)
                        var pwd: String? = null
                        try {
                            pwd = String(appName, Charset.forName("UTF-8"))
                        } catch (e: Exception) {
                        }

                        user.password = pwd!!
                        user.username = recs.getJSONObject(x).getString("username")
                        user.email = recs.getJSONObject(x).getString("email")
                        user.name = recs.getJSONObject(x).getString("name")
                        user.country = recs.getJSONObject(x).getString("country")
                        val userTable = UserTable(context)
                        val id = userTable.addUser(user)

                    }
                    // process other data as this way..............

                } catch (e: JSONException) {
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {} // onPostExecute() end
    }
}
