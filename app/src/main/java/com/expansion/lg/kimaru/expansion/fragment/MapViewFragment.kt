package com.expansion.lg.kimaru.expansion.fragment

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization
import com.expansion.lg.kimaru.expansion.mzigos.Parish
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable
import com.expansion.lg.kimaru.expansion.tables.ParishTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by kimaru on 3/30/17.
 */

class MapViewFragment : Fragment(), View.OnClickListener {

    private val parishes = ArrayList<Parish>()
    private val mobilizationList = ArrayList<Mobilization>()
    private var mListView: ListView? = null
    private var mMobilizationListView: ListView? = null

    internal lateinit var mappingMainLocation: TextView
    internal lateinit var mappingSecondaryLocation: TextView
    internal lateinit var contactPerson: TextView
    internal lateinit var contactPhone: TextView
    internal lateinit var mappingComment: TextView
    internal var ivReferrals: TextView? = null
    internal lateinit var addParish: TextView
    internal lateinit var textViewAddMobilization: TextView
    lateinit var subject: TextView
    lateinit var message: TextView
    lateinit var iconText: TextView
    lateinit var timestamp: TextView
    lateinit  var iconImp: ImageView
    lateinit  var imgProfile: ImageView
    lateinit  var registrationContainser: LinearLayout
    lateinit  var iconContainer: RelativeLayout
    lateinit   var iconBack: RelativeLayout
    lateinit    var iconFront: RelativeLayout
    lateinit   var relativeViewMobilizations: RelativeLayout

    internal var a = AppCompatActivity()
    lateinit   internal var sessionManagement: SessionManagement
    lateinit   internal var user: HashMap<String, String>
    lateinit  internal var mapping: Mapping
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.mapping_content, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_MAP_VIEW
        MainActivity.backFragment = MappingFragment()
        // session
        sessionManagement = SessionManagement(context!!)
        user = sessionManagement.userDetails
        mapping = sessionManagement.savedMapping
        val countyLocationTable = CountyLocationTable(context!!)
        val county = countyLocationTable.getLocationById(mapping.county)
        val subCounty = countyLocationTable.getLocationById(mapping.subCounty)
        mappingMainLocation = v.findViewById<View>(R.id.mappingMainLocation) as TextView
        mappingSecondaryLocation = v.findViewById<View>(R.id.mappingSecondaryLocation) as TextView
        contactPhone = v.findViewById<View>(R.id.contactPhone) as TextView
        contactPerson = v.findViewById<View>(R.id.contactPerson) as TextView
        mappingMainLocation.text = subCounty!!.name
        mappingSecondaryLocation.text = county!!.name
        contactPerson.text = mapping.contactPerson
        mappingComment = v.findViewById<View>(R.id.mappingComment) as TextView
        mappingComment.text = mapping.comment
        contactPhone.text = mapping.contactPersonPhone
        addParish = v.findViewById<View>(R.id.addParish) as TextView
        addParish.text = " + ADD PARISH"
        addParish.setOnClickListener(this)
        textViewAddMobilization = v.findViewById<View>(R.id.mobilizations) as TextView
        textViewAddMobilization.setOnClickListener(this)
        relativeViewMobilizations = v.findViewById<View>(R.id.relativeViewMobilizations) as RelativeLayout
        relativeViewMobilizations.setOnClickListener(this)

        // get the parishes
        getParishes()

        mListView = v.findViewById<View>(R.id.referral_list_view) as ListView
        val listItems = arrayOfNulls<String>(parishes.size)
        for (i in parishes.indices) {
            val parish = parishes[i]
            listItems[i] = parish.name
        }
        val adapter = MapAdapter(context!!, parishes)
        mListView!!.adapter = adapter

        getMobilization()

        mMobilizationListView = v.findViewById<View>(R.id.mobilization_list_view) as ListView
        val mobilizationItems = arrayOfNulls<String>(mobilizationList.size)
        for (i in mobilizationList.indices) {
            val mobilization = mobilizationList[i]
            mobilizationItems[i] = mobilization.name
        }
        val mobilizationAdapter = MobilizationAdapter(context!!, mobilizationList)
        mMobilizationListView!!.adapter = mobilizationAdapter
        //.setLongClickable(true);
        mMobilizationListView!!.isLongClickable = true



        return v
    }

    private inner class MapAdapter(private val _context: Context, private val parishList: List<Parish>) : ArrayAdapter<Parish>(context, -1, parishList) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflater.inflate(R.layout.rowlayout, parent, false)
            // name = (TextView) rowView.findViewById(R.id.from);
            subject = rowView.findViewById<View>(R.id.txt_primary) as TextView
            message = rowView.findViewById<View>(R.id.txt_secondary) as TextView
            iconText = rowView.findViewById<View>(R.id.icon_text) as TextView
            timestamp = rowView.findViewById<View>(R.id.timestamp) as TextView
            iconBack = rowView.findViewById<View>(R.id.icon_back) as RelativeLayout
            iconFront = rowView.findViewById<View>(R.id.icon_front) as RelativeLayout
            iconImp = rowView.findViewById<View>(R.id.icon_star) as ImageView
            imgProfile = rowView.findViewById<View>(R.id.icon_profile) as ImageView
            registrationContainser = rowView.findViewById<View>(R.id.message_container) as LinearLayout
            iconContainer = rowView.findViewById<View>(R.id.icon_container) as RelativeLayout

            val p = parishList[position]
            // name.setText(chew.getTitle());
            subject.text = p.name
            //view_instance.getLayoutParams().width = LayoutParams.MATCH_PARENT
            subject.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT
            if (user[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
                val countyLocationTable = CountyLocationTable(context)
                message.text = countyLocationTable.getLocationById(p.parent)!!.name
            }
            //message.setText(p.getParent());
            message.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT
            // timestamp.setText(chew.getRecruitmentId());
            // timestamp.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
            iconText.text = p.name.substring(0, 1)
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(p.color)
            iconText.visibility = View.VISIBLE
            iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_black_24dp))
            iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_tint_selected))
            rowView.setOnClickListener {
                // go to the village listing
                sessionManagement.saveParish(p)
                val parishViewFragment = ParishViewFragment()
                parishViewFragment.parish = p
                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, parishViewFragment, "villages")
                fragmentTransaction.commitAllowingStateLoss()
            }

            return rowView
        }
    }

    private inner class MobilizationAdapter(private val _context: Context, private val mobilizationsList: List<Mobilization>) : ArrayAdapter<Mobilization>(context, -1, mobilizationsList) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflater.inflate(R.layout.rowlayout, parent, false)
            // name = (TextView) rowView.findViewById(R.id.from);
            subject = rowView.findViewById<View>(R.id.txt_primary) as TextView
            message = rowView.findViewById<View>(R.id.txt_secondary) as TextView
            iconText = rowView.findViewById<View>(R.id.icon_text) as TextView
            timestamp = rowView.findViewById<View>(R.id.timestamp) as TextView
            iconBack = rowView.findViewById<View>(R.id.icon_back) as RelativeLayout
            iconFront = rowView.findViewById<View>(R.id.icon_front) as RelativeLayout
            iconImp = rowView.findViewById<View>(R.id.icon_star) as ImageView
            imgProfile = rowView.findViewById<View>(R.id.icon_profile) as ImageView
            registrationContainser = rowView.findViewById<View>(R.id.message_container) as LinearLayout
            iconContainer = rowView.findViewById<View>(R.id.icon_container) as RelativeLayout

            val m = mobilizationsList[position]
            // name.setText(chew.getTitle());
            subject.text = m.name
            //view_instance.getLayoutParams().width = LayoutParams.MATCH_PARENT
            subject.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT
            if (sessionManagement.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("KE", ignoreCase = true)) {
                val subCountyTable = SubCountyTable(context)
                message.text = subCountyTable.getSubCountyById(m.subCounty)!!.subCountyName
            } else {
                val countyLocationTable = CountyLocationTable(context)
                message.text = countyLocationTable.getLocationById(m.subCounty)!!.name
            }

            message.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT
            // timestamp.setText(chew.getRecruitmentId());
            // timestamp.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
            iconText.text = m.name.substring(0, 1)
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400"))
            iconText.visibility = View.VISIBLE
            iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_black_24dp))
            iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_tint_selected))
            registrationContainser.setOnLongClickListener {
                val newMobilizationFragment = NewMobilizationFragment()
                newMobilizationFragment.editingMobilization = m

                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, newMobilizationFragment, "villages")
                fragmentTransaction.commitAllowingStateLoss()
                true
            }
            registrationContainser.setOnClickListener {
                // go to the village listing
                sessionManagement.saveMobilization(m)
                val fragment = ReferralsFragment()
                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "villages")
                fragmentTransaction.commitAllowingStateLoss()
            }

            return rowView
        }
    }

    override fun onClick(view: View) {
        var fragment: Fragment
        var fragmentTransaction: FragmentTransaction
        when (view.id) {
            R.id.addParish -> {
                // creating new Parish
                fragment = NewParishFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.mobilizations -> {
                fragment = NewMobilizationFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "mobilization")
                fragmentTransaction.commitAllowingStateLoss()
                fragment = NewMobilizationFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "mobilization")
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.relativeViewMobilizations -> {
                fragment = NewMobilizationFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "mobilization")
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val s = SessionManagement(context!!)
        (activity as AppCompatActivity).supportActionBar!!.title = s.savedMapping
                .mappingName + " Mapping "
    }

    //getParishByParent

    private fun getParishes() {
        parishes.clear()
        try {
            val parishList = ParishTable(context!!)
                    .getParishByParent(sessionManagement.savedMapping.subCounty)
            for (p in parishList) {
                p.color = getRandomMaterialColor("400")
                parishes.add(p)
            }
        } catch (e: Exception) {
            ivReferrals!!.text = "No recruitments added. Please create one"
        }

    }


    private fun getMobilization() {
        mobilizationList.clear()
        try {
            val mobList = MobilizationTable(context!!)
                    .getMobilizationByMappingId(sessionManagement.savedMapping.id)
            for (mob in mobList) {
                mobilizationList.add(mob)
            }
        } catch (e: Exception) {
        }

    }

    private fun getRandomMaterialColor(typeColor: String): Int {
        var returnColor = Color.GRAY
        val arrayId = resources.getIdentifier("mdcolor_$typeColor", "array", context!!.packageName)

        if (arrayId != 0) {
            val colors = resources.obtainTypedArray(arrayId)
            val index = (Math.random() * colors.length()).toInt()
            returnColor = colors.getColor(index, Color.GRAY)
            colors.recycle()
        }
        return returnColor
    }

    companion object {

        fun newInstance(param1: String, param2: String): MapViewFragment {
            val fragment = MapViewFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}