package com.expansion.lg.kimaru.expansion.other

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper

import com.expansion.lg.kimaru.expansion.mzigos.Education
import com.expansion.lg.kimaru.expansion.sync.ApiClient
import com.expansion.lg.kimaru.expansion.tables.EducationTable

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by kimaru on 3/22/17.
 */

class SetUpApp(internal var context: Context) {

    fun setUpEducation() {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val url = Constants(context).apiServer + "/education"
            syncEducationFromCloud().execute(url)
        }
    }

    private inner class syncEducationFromCloud : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]

            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("education")
                    val educationTable = EducationTable(context)
                    for (x in 0 until recs.length()) {
                        // create the education
                        educationTable.createEducationFromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {} // onPostExecute() end
    }
}
