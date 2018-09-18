package com.expansion.lg.kimaru.expansion.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable

/**
 * Created by kimaru on 3/30/17.
 */

class LinkFacilityViewFragment : Fragment(), View.OnClickListener {

    private val mRecyclerView: RecyclerView? = null
    internal lateinit var subCounty: SubCounty
    internal lateinit var mapping: Mapping
    internal lateinit var linkFacility: LinkFacility
    internal lateinit var sessionManagement: SessionManagement

    internal lateinit var facilityComment: TextView
    internal lateinit var stockLevels: TextView
    internal lateinit var actMrdtLevels: TextView
    internal lateinit var facilityName: TextView
    internal lateinit var subCountyName: TextView
    internal lateinit var relativeViewCommunityUnits: RelativeLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_link_facility_view, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_SUBCOUNTY_VIEW
        MainActivity.backFragment = SubCountiesFragment()

        sessionManagement = SessionManagement(context!!)
        mapping = sessionManagement.savedMapping
        subCounty = sessionManagement.savedSubCounty
        linkFacility = sessionManagement.savedLinkFacility

        facilityComment = v.findViewById<View>(R.id.mappingComment) as TextView
        facilityName = v.findViewById<View>(R.id.linkFacilityName) as TextView
        subCountyName = v.findViewById<View>(R.id.subCountyName) as TextView
        stockLevels = v.findViewById<View>(R.id.stockLevels) as TextView
        actMrdtLevels = v.findViewById<View>(R.id.mrdtActLevels) as TextView
        relativeViewCommunityUnits = v.findViewById<View>(R.id.relativeViewCommunityUnits) as RelativeLayout
        relativeViewCommunityUnits.setOnClickListener(this)


        facilityName.text = linkFacility.facilityName
        subCountyName.text = subCounty.subCountyName
        stockLevels.text = "Stock Levels"
        actMrdtLevels.text = "MRDT " + (linkFacility.getMrdtLevels().toString() +
                " ACT : " + linkFacility.getActLevels().toString())

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val county = mapping.county
        val subCountyName = subCounty.subCountyName
        (activity as AppCompatActivity).supportActionBar!!.title = linkFacility.facilityName
    }

    override fun onClick(view: View) {
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        val fragmentManager: FragmentManager
        when (view.id) {
            R.id.relativeViewCommunityUnits -> {
                val communityUnitsFragment = CommunityUnitsFragment()
                communityUnitsFragment.linkFacility = linkFacility
                fragment = communityUnitsFragment
                fragmentManager = activity!!.supportFragmentManager
                fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties")
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

}
