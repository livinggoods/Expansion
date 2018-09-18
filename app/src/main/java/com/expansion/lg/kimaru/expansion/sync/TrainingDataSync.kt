package com.expansion.lg.kimaru.expansion.sync

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.IccmComponent
import com.expansion.lg.kimaru.expansion.mzigos.Training
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable
import com.expansion.lg.kimaru.expansion.tables.TrainingTable

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

/**
 * Created by kimaru on 3/21/17.
 */

class TrainingDataSync(internal var context: Context) {

    fun pollNewTrainings() {
        val handler = Handler(Looper.getMainLooper())
        val timer = Timer()
        val getTrainingsTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    val url = Constants(context).apiServer + Constants.API_TRAINING
                    Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    Log.d("Tremap", "BACKGROUND ")
                    Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    syncTrainings().execute(url)

                    Log.d("Tremap", " RR ")
                }
            }
        }
        timer.schedule(getTrainingsTask, 0, (60 * 500 * 1).toLong()) //every 30 minutes
    }


    private inner class syncTrainings : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            val stream: String?
            val urlString = strings[0]
            Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            Log.d("Tremap", "DO IN BKC ")
            Log.d("Tremap", "Str $urlString")
            Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")

            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    Log.d("Tremap", "TRYING ")
                    Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray(TrainingTable.JSON_ROOT)
                    val trainingTable = TrainingTable(context)
                    val trainingList = ArrayList<Training>()
                    for (x in 0 until recs.length()) {
                        trainingTable.fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                    Log.d("TREMAP", "TRAINING ERROR " + e.message)
                }

            } else {
                Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                Log.d("Tremap", "MEMEMEMEMEMEMEMEMEMEEMMEMEMEMMEEMEMEEMMEMEMEMEMEMEMEMEMEMEMEEM ")
                Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            }
            return stream
        }

        override fun onPostExecute(stream: String) {} // onPostExecute() end
    }
}
