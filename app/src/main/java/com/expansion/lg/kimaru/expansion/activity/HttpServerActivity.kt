package com.expansion.lg.kimaru.expansion.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.expansion.lg.kimaru.expansion.BuildConfig
import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.sync.*
import com.expansion.lg.kimaru.expansion.tables.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.*


class HttpServerActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var enableServer: Button
    internal lateinit var buttonGetSubCounty: Button
    internal lateinit var recreateLocationTable: Button
    internal lateinit var resyncLocations: Button
    internal lateinit var shareRecords: Button
    internal lateinit var syncIccmComponents: Button
    internal lateinit var buttonExtractJson: Button
    internal lateinit var pDialog: SweetAlertDialog

    internal lateinit var context: Context
    internal lateinit var session: SessionManagement
    private var locationDataSync: LocationDataSync? = null
    internal lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http_server)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        context = this
        session = SessionManagement(context)
        locationDataSync = LocationDataSync(baseContext)

        enableServer = findViewById<View>(R.id.buttonStartServer) as Button
        resyncLocations = findViewById<View>(R.id.buttonLocationSync) as Button
        shareRecords = findViewById<View>(R.id.buttonShareRecords) as Button
        buttonGetSubCounty = findViewById<View>(R.id.buttonGetSubCounty) as Button
        syncIccmComponents = findViewById<View>(R.id.iccmComponents) as Button
        recreateLocationTable = findViewById<View>(R.id.recreateLocationTable) as Button
        buttonExtractJson = findViewById<View>(R.id.buttonExtractJson) as Button
        progressDialog = ProgressDialog(context)

        enableServer.setOnClickListener(this)
        shareRecords.setOnClickListener(this)
        resyncLocations.setOnClickListener(this)
        buttonGetSubCounty.setOnClickListener(this)
        syncIccmComponents.setOnClickListener(this)
        recreateLocationTable.setOnClickListener(this)
        buttonExtractJson.setOnClickListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        val mainActivity = MainActivity()
        when (view.id) {
            R.id.buttonStartServer -> {
                val server = HttpServer(baseContext)
                Log.d("Tremap Sync", "Starting server")
                server.startServer()

                Toast.makeText(baseContext, "Server started successfully",
                        Toast.LENGTH_SHORT).show()
            }
            R.id.buttonShareRecords -> {
                Log.d("Tremap Sync", "Starting Client")
                val httpClient = HttpClient(baseContext)
                httpClient.startClient()

                Toast.makeText(baseContext, "Sharing of records enabled.",
                        Toast.LENGTH_SHORT).show()
            }
            R.id.buttonLocationSync -> syncLocationsInForeGround()

            R.id.buttonGetSubCounty -> syncKeWardsInForeground()

            R.id.iccmComponents -> {
                val iccmThread = Thread(Runnable {
                    val iccmDataSync = IccmDataSync(baseContext)
                    iccmDataSync.pollNewComponents()
                })
                iccmThread.start()
                Toast.makeText(baseContext, "Getting ICCM components ", Toast.LENGTH_SHORT).show()
            }
            R.id.recreateLocationTable -> {
                val countyLocationTable = CountyLocationTable(baseContext)
                countyLocationTable.reCreateDb()
                syncLocationsInForeGround()
            }

            R.id.buttonExtractJson -> Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                exportJsonData()
                            }
                            if (report.isAnyPermissionPermanentlyDenied) {
                                val i = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID))
                                startActivity(i)
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                            token.continuePermissionRequest()
                        }
                    })
                    .withErrorListener { error -> Toast.makeText(applicationContext, "Error Occured: " + error.toString(), Toast.LENGTH_SHORT).show() }
                    .onSameThread()
                    .check()
        }
    }


    public override fun onResume() {
        super.onResume()
    }


    fun syncKeWardsInForeground() {
        SyncKeWardsInForeground().execute(Constants(baseContext).cloudAddress + "/api/v1/sync/ke-counties")
    }

    private inner class SyncKeWardsInForeground : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Loading KE Locations. Please wait")
            pDialog.show()
            pDialog.setCancelable(false)
        }

        override fun doInBackground(vararg strings: String): String {
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
            session.flagSynced(false) // just to force the app to resync the data.
            pDialog.dismiss()
        } // onPostExecute() end
    }


    fun syncLocationsInForeGround() {
        SyncLocationsInForeGround().execute(Constants(baseContext).cloudAddress + "/api/v1/sync/locations")
    }

    private inner class SyncLocationsInForeGround : AsyncTask<String, HashMap<*, *>, String>() {
        internal var totalRecords: Int? = 0
        internal var processedRecords: Int? = 0
        internal var recordType = ""
        override fun onPreExecute() {
            super.onPreExecute()
            recordType = "Location"
            progressDialog.setTitle("Syncing $recordType")
            progressDialog.setMessage("Please wait ...")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

        override fun doInBackground(vararg strings: String): String {
            var stream: String? = null
            var progress = HashMap<String, String>()
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("locations")
                    val countyLocationTable = CountyLocationTable(baseContext)
                    totalRecords = recs.length()
                    for (x in 0 until recs.length()) {
                        countyLocationTable.fromJson(recs.getJSONObject(x))
                        processedRecords = x
                        progress = HashMap()
                        progress["total"] = totalRecords!!.toString()
                        progress["processed"] = processedRecords!!.toString()
                        progress["type"] = "Location"
                        publishProgress(progress)
                    }
                } catch (e: JSONException) {
                }

            }
            recordType = "Parish"
            processedRecords = 0
            progress = HashMap()
            progress["total"] = "100"
            progress["processed"] = "0"
            progress["type"] = "Parish"
            progress["message"] = "Downloading parishes from the Cloud ... "
            progress["title"] = "Getting Parishes"
            progress["action"] = "Downloading"
            publishProgress(progress)

            val parishStream = hh.GetHTTPData(Constants(context).cloudAddress + "/api/v1/sync/parish")
            if (parishStream != null) {
                try {
                    val reader = JSONObject(parishStream)
                    val recs = reader.getJSONArray("parish")
                    val parishTable = ParishTable(context)
                    totalRecords = recs.length()
                    for (y in 0 until recs.length()) {
                        val parish = recs.getJSONObject(y)
                        progress = HashMap()
                        progress["total"] = totalRecords!!.toString()
                        progress["processed"] = y.toString()
                        progress["type"] = "Parish"
                        progress["message"] = "Processing parishes ... "
                        progress["title"] = "Processing Parish"
                        publishProgress(progress)
                        parishTable.fromJson(parish)
                    }
                } catch (e: JSONException) {
                    Log.d("TREMAP", "ERROR Creating parish:\n " + e.message)
                }

            }
            return stream

        }

        override fun onPostExecute(stream: String) {
            session.flagSynced(false) // just to force the app to resync the data.
            progressDialog.dismiss()
        } // onPostExecute() end

        override fun onProgressUpdate(vararg updates: HashMap<*, *>) {
            super.onProgressUpdate(*updates)
            val progress = updates[0]
            if (progress.containsKey("action")) {
                progressDialog.progress = 0
                progressDialog.max = 100
                progressDialog.setMessage(progress.get("message").toString())
                progressDialog.setTitle(progress["title"].toString())
            } else {
                progressDialog.progress = Integer.valueOf(progress["processed"].toString())
                progressDialog.max = Integer.valueOf(progress["total"].toString())
                progressDialog.setMessage("Syncing " + progress["type"] + "\n Please wait ... ")
                progressDialog.setTitle("Syncing " + progress["type"])
            }

        }
    }


    private fun exportJsonData() {
        // only export the scoring tool for the current recruitment
        // 1. Check if the Externa Storage is available
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED != state) {
            return
        } else {
            //We used Download Dir
            //File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            val exportDir = File(Environment.getExternalStorageDirectory().toString() + "/Download/tremap/mapping/")
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }
            val file: File
            var printWriter: PrintWriter? = null
            file = File(exportDir, "mapping_data.json")
            try {
                file.createNewFile()
                printWriter = PrintWriter(FileWriter(file))

                //we get villages
                val villageTable = VillageTable(applicationContext)
                val villageJson = villageTable.json
                printWriter.println(villageJson.toString())


                //we get mapping
                val mappingTable = MappingTable(applicationContext)
                val mappingTableJson = mappingTable.json
                printWriter.println(mappingTableJson.toString())


                // We get interviews
                val interviewTable = InterviewTable(applicationContext)
                val interviewJson = interviewTable.interviewsToSyncAsJson
                printWriter.println(interviewJson.toString())


                //we get parishes
                val parishTable = ParishTable(applicationContext)
                val parishJson = parishTable.json
                Log.d("Tremap", "}{{}{}{{}{}{}{}{}{}{}{}{}{}{}{}{}{}}{}{}{}{}{}{}{{}{{}")
                Log.d("Tremap", parishJson.toString())
                Log.d("Tremap", "}{{}{}{{}{}{}{}{}{}{}{}{}{}{}{}{}{}}{}{}{}{}{}{}{{}{{}")
                printWriter.println(parishJson.toString())


                //we get mobilization
                val mobilizationTable = MobilizationTable(applicationContext)
                val mobilizationJson = mobilizationTable.mobilizationJson
                printWriter.println(mobilizationJson.toString())

                //we get link Facilities
                val linkFacilityTable = LinkFacilityTable(applicationContext)
                val linkFacilityJson = linkFacilityTable.json
                printWriter.println(linkFacilityJson.toString())


            } catch (e: Exception) {
            } finally {
                printWriter?.close()
            }
            Toast.makeText(applicationContext, "Data exported to " + file.absolutePath + " Folder", Toast.LENGTH_LONG).show()
        }

    }

}
