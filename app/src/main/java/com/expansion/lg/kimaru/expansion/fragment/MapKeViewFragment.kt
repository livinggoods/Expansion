package com.expansion.lg.kimaru.expansion.fragment

import android.content.Context
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
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.KeCounty
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter

import java.util.ArrayList
import java.util.HashMap

import com.expansion.lg.kimaru.expansion.R.id.parishes

/**
 * Created by kimaru on 3/30/17.
 */

class MapKeViewFragment : Fragment(), View.OnClickListener {

    private val mRecyclerView: RecyclerView? = null

    internal lateinit var sessionManagement: SessionManagement
    internal lateinit var user: HashMap<String, String>
    internal lateinit var mapping: Mapping
    internal lateinit var mappingMainLocation: TextView
    internal lateinit var mappingSecondaryLocation: TextView
    internal lateinit var contactPerson: TextView
    internal lateinit var contactPhone: TextView
    internal lateinit var mappingComment: TextView
    internal lateinit var txtSubCounty: TextView
    private val mListView: ListView? = null
    private val subCountyList = ArrayList<SubCounty>()
    var subject: TextView? = null
    var message: TextView? = null
    var iconText: TextView? = null
    var timestamp: TextView? = null
    var iconImp: ImageView? = null
    var imgProfile: ImageView? = null
    var registrationContainser: LinearLayout? = null
    var iconContainer: RelativeLayout? = null
    var iconBack: RelativeLayout? = null
    var iconFront: RelativeLayout? = null
    lateinit var relativeSubCountySummary: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_mapping_view, container, false)
        MainActivity.backFragment = MappingFragment()
        sessionManagement = SessionManagement(context!!)
        user = sessionManagement.userDetails
        mapping = sessionManagement.savedMapping
        val county = KeCountyTable(context!!).getCountyById(Integer.valueOf(mapping.county))
        mappingSecondaryLocation = v.findViewById<View>(R.id.mappingSecondaryLocation) as TextView
        contactPhone = v.findViewById<View>(R.id.contactPhone) as TextView
        contactPerson = v.findViewById<View>(R.id.contactPerson) as TextView
        mappingMainLocation = v.findViewById<View>(R.id.mappingMainLocation) as TextView
        mappingComment = v.findViewById<View>(R.id.mappingComment) as TextView

        mappingMainLocation.text = mapping.mappingName
        mappingSecondaryLocation.text = county!!.countyName
        contactPerson.text = mapping.contactPerson
        contactPhone.text = mapping.contactPersonPhone
        mappingComment.text = mapping.comment
        // get the parishes
        getSubcounties()

        txtSubCounty = v.findViewById<View>(R.id.addSubCounty) as TextView
        txtSubCounty.text = subCountyList.size.toString() + " SUB COUNTIES"
        txtSubCounty.setOnClickListener(this)

        relativeSubCountySummary = v.findViewById<View>(R.id.subCountySummary) as RelativeLayout
        relativeSubCountySummary.setOnClickListener(this)




        return v
    }


    private fun getSubcounties() {
        subCountyList.clear()
        try {
            val subCounties = SubCountyTable(context!!)
                    .getSubCountiesByCounty(Integer.valueOf(mapping.county))
            for (sb in subCounties) {
                sb.setColor(getRandomMaterialColor("400"))
                subCountyList.add(sb)
            }
        } catch (e: Exception) {
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val s = SessionManagement(context!!)
        (activity as AppCompatActivity).supportActionBar!!.title = s.savedMapping
                .mappingName + " Mapping "
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

    override fun onClick(view: View) {
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        when (view.id) {
            R.id.addSubCounty -> {
                fragment = SubCountiesFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()
            }

            R.id.subCountySummary -> {
                fragment = SubCountiesFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

}
