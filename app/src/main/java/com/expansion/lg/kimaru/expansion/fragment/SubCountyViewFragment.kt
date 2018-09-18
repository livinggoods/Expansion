package com.expansion.lg.kimaru.expansion.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable
import com.expansion.lg.kimaru.expansion.tables.PartnersTable
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter

import java.util.ArrayList

/**
 * Created by kimaru on 3/30/17.
 */

class SubCountyViewFragment : Fragment(), View.OnClickListener {

    private val mRecyclerView: RecyclerView? = null
    internal lateinit var subCounty: SubCounty
    internal lateinit var mapping: Mapping
    internal lateinit var sessionManagement: SessionManagement

    internal lateinit var mappingComment: TextView
    internal lateinit var contactPhone: TextView
    internal lateinit var contactPerson: TextView
    internal lateinit var subCountyName: TextView
    internal lateinit var subCountyCounty: TextView
    internal lateinit var linkFacilitySummary: TextView
    internal lateinit var communityUnitSummary: TextView
    internal lateinit var partnersSummary: TextView
    internal lateinit var relativeViewPartners: RelativeLayout
    internal lateinit var relativeViewCommunityUnits: RelativeLayout
    internal lateinit var relativeLinkFacilities: RelativeLayout
    internal lateinit var createNewPartner: RelativeLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_subcounty_view, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_SUBCOUNTY_VIEW
        MainActivity.backFragment = SubCountiesFragment()

        sessionManagement = SessionManagement(context!!)
        mapping = sessionManagement.savedMapping
        subCounty = sessionManagement.savedSubCounty
        mappingComment = v.findViewById<View>(R.id.mappingComment) as TextView
        contactPhone = v.findViewById<View>(R.id.contactPhone) as TextView
        contactPerson = v.findViewById<View>(R.id.contactPerson) as TextView
        subCountyName = v.findViewById<View>(R.id.subCountyName) as TextView
        subCountyCounty = v.findViewById<View>(R.id.subCountyCounty) as TextView
        linkFacilitySummary = v.findViewById<View>(R.id.linkFacilitySummary) as TextView
        communityUnitSummary = v.findViewById<View>(R.id.communityUnitSummary) as TextView
        partnersSummary = v.findViewById<View>(R.id.partnersSummart) as TextView

        subCountyName.text = subCounty.subCountyName
        contactPhone.text = subCounty.contactPersonPhone
        contactPerson.text = subCounty.contactPerson
        subCountyCounty.text = KeCountyTable(context!!)
                .getCountyById(Integer.valueOf(mapping.county))!!.countyName

        val linkFacilityTable = LinkFacilityTable(context!!)
        val linkFacilities = linkFacilityTable.getLinkFacilityBySubCounty(subCounty.id).size
        if (linkFacilities == 0) {
            linkFacilitySummary.text = "NO LINK FACILITY"
        } else if (linkFacilities == 1) {
            linkFacilitySummary.text = "1 LINK FACILITY"
        } else {
            linkFacilitySummary.text = linkFacilities.toString() + " LINK FACILITIES"
        }

        val communityUnitTable = CommunityUnitTable(context!!)
        val communityUnits = communityUnitTable
                .getCommunityUnitBySubCounty(subCounty.id).size
        if (communityUnits == 0) {
            communityUnitSummary.text = "NO COMMUNITY UNITS"
        } else if (communityUnits == 1) {
            communityUnitSummary.text = "1 COMMUNITY UNIT"
        } else {
            communityUnitSummary.text = communityUnits.toString() + " COMMUNITY UNITS"
        }

        val partnerActivityTable = PartnerActivityTable(context!!)
        //        Integer partners = partnerActivityTable
        //                .getPartnersBySubCounty(subCounty.getId()).size();
        val partners = partnerActivityTable
                .getPartnerActivityByField(PartnerActivityTable.SUBCOUNTY, subCounty.id)
                .size
        if (partners == 0) {
            partnersSummary.text = "NO PARTNERS"
        } else if (partners == 1) {
            partnersSummary.text = "1 PARTNER"
        } else {
            partnersSummary.text = partners.toString() + " PARTNERS"
        }

        relativeViewPartners = v.findViewById<View>(R.id.relativeViewPartners) as RelativeLayout
        relativeViewCommunityUnits = v.findViewById<View>(R.id.relativeViewCommunityUnits) as RelativeLayout
        relativeLinkFacilities = v.findViewById<View>(R.id.relativeLinkFacilities) as RelativeLayout
        createNewPartner = v.findViewById<View>(R.id.createNewPartner) as RelativeLayout

        relativeLinkFacilities.setOnClickListener(this)
        relativeViewCommunityUnits.setOnClickListener(this)
        relativeViewPartners.setOnClickListener(this)
        createNewPartner.setOnClickListener(this)


        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val county = mapping.county
        val subCountyName = subCounty.subCountyName
        (activity as AppCompatActivity).supportActionBar!!.title = county + " - " + subCountyName + "Sub County"
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
            R.id.relativeViewCommunityUnits -> {
                fragment = CommunityUnitsFragment()
                fragmentManager = activity!!.supportFragmentManager
                fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties")
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.relativeViewPartners -> {
                val partnerActivityFragment = PartnerActivityFragment()
                partnerActivityFragment.subCounty = subCounty
                fragment = partnerActivityFragment
                fragmentManager = activity!!.supportFragmentManager
                fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties")
                fragmentTransaction.commitAllowingStateLoss()
            }

            R.id.createNewPartner -> {
                val newPartnerActivityFragment = NewPartnerActivityFragment()
                newPartnerActivityFragment.subCounty = subCounty
                sessionManagement.saveSubCounty(subCounty)
                newPartnerActivityFragment.communityUnit = null
                fragment = newPartnerActivityFragment
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
