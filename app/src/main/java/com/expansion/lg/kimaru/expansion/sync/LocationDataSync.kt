package com.expansion.lg.kimaru.expansion.sync

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import com.expansion.lg.kimaru.expansion.tables.ParishTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable
import com.expansion.lg.kimaru.expansion.tables.VillageTable
import com.expansion.lg.kimaru.expansion.tables.WardTable
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

/**
 * Created by kimaru on 3/21/17.
 */

class LocationDataSync(internal var context: Context) {
    var totalParishes = 0
    var processedParishes = 0

    fun pollLocations() {
        val countyLocationTable = CountyLocationTable(context)
        syncCountyLocations().execute(Constants(context).cloudAddress + "/api/v1/sync/locations")
    }

    fun getKeSubcounties() {
        syncKeWards().execute(Constants(context).cloudAddress + "/api/v1/sync/ke-counties")
    }


    private inner class syncKeWards : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("counties")
                    val subCountyTable = SubCountyTable(context)
                    for (x in 0 until recs.length()) {
                        // I have the county Details, lets extract all subcounties
                        val county = recs.getJSONObject(x)
                        val subCounties = county.getJSONArray("subcounties")
                        for (a in 0 until subCounties.length()) {
                            // I have the subcounty, create the subcounty
                            val subCounty = subCounties.getJSONObject(a)
                            SubCountyTable(context).fromJson(subCounty)
                            //also get the wards
                            val wards = subCounty.getJSONArray("wards")
                            for (b in 0 until wards.length()) {
                                val ward = wards.getJSONObject(b)
                                //create a ward
                                WardTable(context).fromJson(ward)
                            }
                        }
                    }
                } catch (e: JSONException) {
                    Log.d("TREMAP", "KE County Sync ERROR " + e.message)
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {

        } // onPostExecute() end
    }

    fun getParishDatafromCloud() {
        syncParishesFromCloud().execute(Constants(context).cloudAddress + "/api/v1/sync/parish")
    }

    inner class syncParishesFromCloud : AsyncTask<String, Int, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("parish")
                    val parishTable = ParishTable(context)
                    totalParishes = recs.length()
                    for (x in 0 until recs.length()) {
                        val parish = recs.getJSONObject(x)
                        parishTable.fromJson(parish)
                        processedParishes = x
                        publishProgress(x / recs.length() * 100)
                    }
                } catch (e: JSONException) {
                    Log.d("TREMAP", "ERROR Creating parish:\n " + e.message)
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {

        } // onPostExecute() end
    }

    fun getVillageDatafromCloud() {
        syncVillagesFromCloud().execute(Constants(context).cloudAddress + "/api/v1/sync/villages")
    }

    private inner class syncVillagesFromCloud : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("villages")
                    val villageTable = VillageTable(context)
                    for (x in 0 until recs.length()) {
                        val village = recs.getJSONObject(x)
                        villageTable.fromJson(village)
                    }
                } catch (e: JSONException) {
                    Log.d("TREMAP", "Error creating Village:\n " + e.message)
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {

        } // onPostExecute() end
    }


    private inner class syncCountyLocations : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("locations")
                    val countyLocationTable = CountyLocationTable(context)
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
