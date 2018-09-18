package com.expansion.lg.kimaru.expansion.sync

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Base64

import com.expansion.lg.kimaru.expansion.mzigos.IccmComponent
import com.expansion.lg.kimaru.expansion.mzigos.User
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable
import com.expansion.lg.kimaru.expansion.tables.UserTable

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

/**
 * Created by kimaru on 3/21/17.
 */

class IccmDataSync(internal var context: Context) {

    fun pollNewComponents() {
        val handler = Handler(Looper.getMainLooper())
        val timer = Timer()
        val getUsersTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    val url = Constants(context).apiServer + "/iccm-components"
                    syncIccmComponents().execute(url)
                }
            }
        }
        timer.schedule(getUsersTask, 0, (60 * 1000 * 30).toLong()) //every 30 minutes
    }


    private inner class syncIccmComponents : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]

            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("components")
                    val iccmComponentTable = IccmComponentTable(context)
                    val componentList = ArrayList<IccmComponent>()
                    for (x in 0 until recs.length()) {
                        // create the component
                        iccmComponentTable.fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {} // onPostExecute() end
    }
}
