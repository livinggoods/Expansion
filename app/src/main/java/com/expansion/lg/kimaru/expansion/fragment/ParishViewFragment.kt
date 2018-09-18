package com.expansion.lg.kimaru.expansion.fragment

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.Parish
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.sync.ApiClient
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable
import com.expansion.lg.kimaru.expansion.tables.MappingTable
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable
import com.expansion.lg.kimaru.expansion.tables.VillageTable

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.HashMap

/**
 * Created by kimaru on 3/30/17.
 */

class ParishViewFragment : Fragment(), View.OnClickListener {

    private val mRecyclerView: RecyclerView? = null
    internal lateinit  var parish: Parish
    internal lateinit var mapping: Mapping
    internal var subCounty: CountyLocation? = null
    internal lateinit var sessionManagement: SessionManagement

    internal lateinit var parishComment: TextView
    internal lateinit var contactPhone: TextView
    internal lateinit var contactPerson: TextView
    internal lateinit var parishName: TextView
    internal lateinit var parishSubCountyName: TextView
    internal lateinit var subCountyCounty: TextView
    internal lateinit var linkFacilitySummary: TextView
    internal lateinit var villagesSummary: TextView
    internal lateinit var partnersSummary: TextView
    internal lateinit var syncVillages: TextView
    internal lateinit var relativeViewVillages: RelativeLayout
    internal lateinit var relativeLinkFacilities: RelativeLayout
    internal lateinit var progressDialog: ProgressDialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_parish_view, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_SUBCOUNTY_VIEW
        MainActivity.backFragment = MapViewFragment()

        sessionManagement = SessionManagement(context!!)
        mapping = sessionManagement.savedMapping
        parish = sessionManagement.savedParish

        progressDialog = ProgressDialog(context)

        parishName = v.findViewById<View>(R.id.parishName) as TextView
        parishComment = v.findViewById<View>(R.id.parishComment) as TextView
        contactPhone = v.findViewById<View>(R.id.contactPhone) as TextView
        contactPerson = v.findViewById<View>(R.id.contactPerson) as TextView
        parishSubCountyName = v.findViewById<View>(R.id.parishSubCountyName) as TextView
        subCountyCounty = v.findViewById<View>(R.id.subCountyCounty) as TextView
        linkFacilitySummary = v.findViewById<View>(R.id.linkFacilitySummary) as TextView
        villagesSummary = v.findViewById<View>(R.id.villagesSummary) as TextView
        partnersSummary = v.findViewById<View>(R.id.partnersSummart) as TextView
        syncVillages = v.findViewById<View>(R.id.syncVillages) as TextView
        syncVillages.setOnClickListener(this)
        parishName.text = parish.name
        Log.d("Tremap", "Mapping Subcounty is " + mapping.subCounty)
        subCounty = CountyLocationTable(context!!).getLocationById(mapping.subCounty)

        parishSubCountyName.text = subCounty!!.name
        contactPhone.text = parish.contactPersonPhone
        contactPerson.text = parish.contactPerson
        subCountyCounty.text = CountyLocationTable(context!!)
                .getLocationById(subCounty!!.parent.toString())!!.name

        val linkFacilityTable = LinkFacilityTable(context!!)
        val linkFacilities = linkFacilityTable
                .getLinkFacilityByParish(parish.id).size
        if (linkFacilities == 0) {
            linkFacilitySummary.text = "NO LINK FACILITY"
        } else if (linkFacilities == 1) {
            linkFacilitySummary.text = "1 LINK FACILITY"
        } else {
            linkFacilitySummary.text = linkFacilities.toString() + " LINK FACILITIES"
        }

        val villageTable = VillageTable(context!!)
        val villages = villageTable
                .getVillageDataByParishId(parish.id).size
        if (villages == 0) {
            villagesSummary.text = "NO VILLAGES ADDED"
        } else if (villages == 1) {
            villagesSummary.text = "1 VILLAGE"
        } else {
            villagesSummary.text = villages.toString() + " VILLAGES"
        }


        relativeViewVillages = v.findViewById<View>(R.id.relativeViewVillages) as RelativeLayout
        relativeViewVillages.setOnClickListener(this)
        relativeLinkFacilities = v.findViewById<View>(R.id.relativeLinkFacilities) as RelativeLayout
        relativeLinkFacilities.setOnClickListener(this)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val parishName = parish.name
        (activity as AppCompatActivity).supportActionBar!!.title = "$parishName Parish"
    }

    override fun onClick(view: View) {
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        val fragmentManager: FragmentManager
        when (view.id) {
            R.id.relativeLinkFacilities -> {
                fragment = LinkFacilitiesFragment()
                fragmentManager = activity!!.supportFragmentManager
                fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties")
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.relativeViewVillages -> {
                fragment = VillagesFragment()
                fragmentManager = activity!!.supportFragmentManager
                fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties")
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.syncVillages -> {
                progressDialog.setTitle("Syncing Villages")
                progressDialog.setMessage("Getting villages for " + parish.name + " ...")
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                progressDialog.setCancelable(false)
                progressDialog.show()
                SyncVillages().execute()
            }
        }

    }

    private inner class SyncVillages : AsyncTask<Void, HashMap<*, *>, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg voids: Void): String? {
            var stream: String? = null
            var totalRecords: Int? = 0
            var processedRecords: Int? = 0
            var progress = HashMap<String, String>()
            val hh = ApiClient()
            stream = hh.GetHTTPData(Constants(context!!).cloudAddress + "/api/v1/sync/villages?parish=" + parish.id)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("villages")
                    val villageTable = VillageTable(context!!)
                    totalRecords = recs.length()
                    for (y in 0 until recs.length()) {
                        processedRecords = y + 1
                        val villageObj = recs.getJSONObject(y)
                        villageTable.fromJson(villageObj)
                        progress = HashMap()
                        progress["total"] = totalRecords.toString()
                        progress["processed"] = processedRecords.toString()
                        progress["message"] = "Creating village " + villageObj.get("village_name") + "\n Please wait ... "
                        publishProgress(progress)
                    }
                } catch (e: JSONException) {
                    Log.d("TREMAP", "ERROR Creating parish:\n " + e.message)
                }

            }
            return null

        }

        override fun onPostExecute(stream: String) {
            super.onPostExecute(null)
            progressDialog.dismiss()
        } // onPostExecute() end

        override fun onProgressUpdate(vararg updates: HashMap<*, *>) {
            super.onProgressUpdate(*updates)
            val progress = updates[0]
            progressDialog.progress = Integer.valueOf(progress["processed"].toString())
            progressDialog.max = Integer.valueOf(progress["total"].toString())
            progressDialog.setMessage(progress.get("message") as CharSequence?)
        }
    }

}
