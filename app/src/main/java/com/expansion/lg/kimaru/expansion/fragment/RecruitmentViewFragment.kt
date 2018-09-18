package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.view.ActionMode
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.test.suitebuilder.TestMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
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

import android.support.v7.app.AppCompatActivity


import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.dbhelpers.ChewReferralListAdapter
import com.expansion.lg.kimaru.expansion.dbhelpers.CommunityUnitListAdapter
import com.expansion.lg.kimaru.expansion.dbhelpers.RecruitmentListAdapter
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation
import com.expansion.lg.kimaru.expansion.mzigos.Interview
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import com.expansion.lg.kimaru.expansion.tables.InterviewTable
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter

import java.util.ArrayList
import java.util.HashMap

// to show list in Gmail Mode
class RecruitmentViewFragment : Fragment(), View.OnClickListener {

    private val recyclerView: RecyclerView? = null
    private val rAdapter: ChewReferralListAdapter? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    lateinit var registrationDetails: RelativeLayout

    private val chewReferrals = ArrayList<ChewReferral>()
    private val communityUnits = ArrayList<CommunityUnit>()
    private var mListView: ListView? = null
    private var cuListView: ListView? = null


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: RecruitmentsFragment.OnFragmentInteractionListener? = null
    internal lateinit var recruitmentMainLocation: TextView
    internal lateinit var recruitmentSecondaryLocation: TextView
    internal lateinit var txtRegistrations: TextView
    internal lateinit var recruitmentRegSummary: TextView
    internal var ivReferrals: TextView? = null
    internal lateinit var addReferrals: TextView

    lateinit var subject: TextView
    lateinit var message: TextView
    lateinit var iconText: TextView
    lateinit var timestamp: TextView
    lateinit var iconImp: ImageView
    lateinit var imgProfile: ImageView
    lateinit var registrationContainser: LinearLayout
    lateinit var iconContainer: RelativeLayout
    lateinit var iconBack: RelativeLayout
    lateinit var iconFront: RelativeLayout
    lateinit var relativePitch: RelativeLayout


    internal var a = AppCompatActivity()
    internal lateinit var cuAdapter: CuAdapter
    internal lateinit var adapter: ChewAdapter

    internal lateinit var session: SessionManagement
    internal lateinit var user: HashMap<String, String>
    internal var registrations: List<Registration> = ArrayList()

    internal var onRecruitmentRegistrationSummaryClickListener: View.OnClickListener = View.OnClickListener {
        val fragment = RegistrationsFragment()
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out)
        fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.recruitment_content, container, false)

        MainActivity.CURRENT_TAG = MainActivity.TAG_RECRUITMENTS
        MainActivity.backFragment = HomeFragment()
        //session Management
        session = SessionManagement(context!!)
        user = session.userDetails
        getReferrals()

        // initialize the UI

        mListView = v.findViewById<View>(R.id.referral_list_view) as ListView
        val listItems = arrayOfNulls<String>(chewReferrals.size)
        for (i in chewReferrals.indices) {
            val chewReferral = chewReferrals[i]
            listItems[i] = chewReferral.name
        }
        adapter = ChewAdapter(context!!, chewReferrals)

        mListView!!.adapter = adapter
        registrationDetails = v.findViewById<View>(R.id.registrationDetails) as RelativeLayout
        registrationDetails.setOnClickListener(onRecruitmentRegistrationSummaryClickListener)

        recruitmentMainLocation = v.findViewById<View>(R.id.recruitmentMainLocation) as TextView
        addReferrals = v.findViewById<View>(R.id.addReferrals) as TextView
        recruitmentSecondaryLocation = v.findViewById<View>(R.id.recruitmentSecondaryLocation) as TextView
        registrations = RegistrationTable(context!!)
                .getRegistrationsByRecruitment(session.savedRecruitment)
        txtRegistrations = v.findViewById<View>(R.id.registrations) as TextView
        txtRegistrations.text = registrations.size.toString() + " Registrations"
        txtRegistrations.setOnClickListener(onRecruitmentRegistrationSummaryClickListener)

        relativePitch = v.findViewById<View>(R.id.relativePitch) as RelativeLayout
        relativePitch.setOnClickListener {
            val fragmentTransaction: FragmentTransaction
            val fragment = FragmentPitch()
            fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out)
            fragmentTransaction.replace(R.id.frame, fragment, "")
            fragmentTransaction.commitAllowingStateLoss()
        }

        var passed = 0
        var failed = 0
        var waiting = 0
        for (registration in registrations) {
            //get the interview for the registration
            val i = InterviewTable(context!!).getInterviewByRegistrationId(registration.id)
            var selected: Int? = 0
            if (i != null) {
                selected = i.selected
            }
            when (selected) {
                1 -> passed += 1
                2 -> waiting += 1
                0 -> failed += 1
            }

        }
        val summary = passed.toString() + " Selected\n" + waiting.toString() + " Waiting" +
                " list\n" + failed.toString() + " Not selected"
        recruitmentRegSummary = v.findViewById<View>(R.id.recruitmentRegSummary) as TextView
        recruitmentRegSummary.text = summary
        recruitmentRegSummary.setOnClickListener(onRecruitmentRegistrationSummaryClickListener)

        val keCuList = v.findViewById<View>(R.id.ke_cu_list) as RelativeLayout
        val keCuView = v.findViewById<View>(R.id.ke_cu_view) as RelativeLayout
        if (session.savedRecruitment.country.equals("UG", ignoreCase = true)) {
            recruitmentMainLocation.text = session.savedRecruitment.name
            val district = CountyLocationTable(context!!)
                    .getLocationById(session.savedRecruitment.district)
            //recruitmentSecondaryLocation.setText(session.getSavedRecruitment().getDistrict());
            recruitmentSecondaryLocation.text = district!!.name
            addReferrals.text = "+ ADD REFERRAL"
            //Hide KE
            keCuList.visibility = View.INVISIBLE
            keCuView.visibility = View.INVISIBLE
        } else {
            getCommunityUnits()
            cuListView = v.findViewById<View>(R.id.cu_list_view) as ListView
            val cuItems = arrayOfNulls<String>(communityUnits.size)
            for (i in communityUnits.indices) {
                val communityUnit = communityUnits[i]
                cuItems[i] = communityUnit.communityUnitName
            }
            cuAdapter = CuAdapter(context!!, communityUnits)

            cuListView!!.adapter = cuAdapter

            recruitmentMainLocation.text = session.savedRecruitment.name

            //            recruitmentSecondaryLocation.setText(new SubCountyTable(getContext())
            //                    .getSubCountyById(session.getSavedRecruitment().getSubcounty())
            //                    .getSubCountyName());


            recruitmentSecondaryLocation.text = SubCountyTable(context!!)
                    .getSubCountyById(session.savedRecruitment.subcounty)!!
                    .subCountyName

            session.saveSubCounty(SubCountyTable(context!!)
                    .getSubCountyById(session.savedRecruitment.subcounty)!!)
            addReferrals.text = "+ ADD A CHEW"
            keCuView.setOnClickListener(this)

        }
        addReferrals.setOnClickListener(this)

        return v
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private fun getReferrals() {
        chewReferrals.clear()
        try {
            val chewReferralList = ChewReferralTable(context!!)
                    .getChewReferralByRecruitmentId(session.savedRecruitment.id)
            for (chewReferral in chewReferralList) {
                chewReferral.color = getRandomMaterialColor("400")
                chewReferrals.add(chewReferral)
            }
        } catch (e: Exception) {
            // ivReferrals.setText("No recruitments added. Please create one");
        }

    }

    private fun getCommunityUnits() {
        communityUnits.clear()
        try {
            val communityUnitList = CommunityUnitTable(context!!)
                    .getCommunityUnitBySubCounty(session.savedRecruitment.subcounty)
            for (cu in communityUnitList) {
                cu.setColor(getRandomMaterialColor("400"))
                communityUnits.add(cu)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error " + e.message, Toast.LENGTH_SHORT).show()
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

    override fun onClick(view: View) {
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        when (view.id) {
            R.id.addReferrals -> {
                val newChewReferralFragment = NewChewReferralFragment()
                newChewReferralFragment.createdFromRecruitment = true
                fragment = newChewReferralFragment
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
            }

            //            case R.id.recruitmentRegSummary:
            //                fragment = new RegistrationsFragment();
            //                Toast.makeText(getContext(), "Opening Registrations", Toast.LENGTH_SHORT).show();
            //                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            //                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
            //                        android.R.anim.fade_out);
            //                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS);
            //                fragmentTransaction.commitAllowingStateLoss();
            //                break;
            //            case  R.id.registrations:
            //                fragment = new RegistrationsFragment();
            //                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            //                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
            //                        android.R.anim.fade_out);
            //                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS);
            //                fragmentTransaction.commitAllowingStateLoss();
            //                break;
            R.id.ke_cu_view -> {
                val newCommunityUnitFragment = NewCommunityUnitFragment()
                newCommunityUnitFragment.backFragment = RecruitmentViewFragment()
                fragment = newCommunityUnitFragment
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    inner class ChewAdapter(private val _context: Context, private val chewReferralss: List<ChewReferral>) : ArrayAdapter<ChewReferral>(context, -1, chewReferralss) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
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

            val chew = chewReferralss[position]
            // name.setText(chew.getTitle());
            subject.text = chew.title + " " + chew.name
            //view_instance.getLayoutParams().width = LayoutParams.MATCH_PARENT
            subject.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT

            message.text = chew.phone
            message.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT
            // timestamp.setText(chew.getRecruitmentId());
            // timestamp.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
            if (!chew.name.equals("", ignoreCase = true)) {
                iconText.text = chew.name.substring(0, 1)
            } else {
                iconText.text = ""
            }

            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(chew.color)
            iconText.visibility = View.VISIBLE
            iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_white_24dp))
            iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_warning))
            iconImp.setOnLongClickListener {
                // really delete the Item?
                val alertDialog = AlertDialog.Builder(getContext())
                alertDialog.setTitle("Confirm Delete")
                alertDialog.setMessage("Are you sure you want to delete " + chew.name + "?")
                alertDialog.setIcon(R.drawable.ic_delete_white_24dp)
                alertDialog.setPositiveButton("Delete") { dialog, which ->
                    // Check if there are records dependent on this item
                    val registrations = RegistrationTable(getContext())
                            .getRegistrationsByChewReferral(chew)
                    if (registrations.size <= 0) {
                        chewReferrals.removeAt(position)
                        deleteChewReferral(chew)
                    }
                }
                alertDialog.setNegativeButton("Cancel") { dialog, which ->
                    // Check if there are records dependent on this item
                    dialog.cancel()
                }
                alertDialog.show()
                false
            }

            val chewLongClick = View.OnLongClickListener {
                val newChewReferralFragment = NewChewReferralFragment()
                newChewReferralFragment.createdFromRecruitment = true
                newChewReferralFragment.editingChewReferral = chew
                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, newChewReferralFragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
                false
            }
            registrationContainser.setOnLongClickListener(chewLongClick)

            return rowView
        }
    }

    inner class CuAdapter(private val _context: Context, private val communityUnitss: List<CommunityUnit>) : ArrayAdapter<CommunityUnit>(context, -1, communityUnitss) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflater.inflate(R.layout.rowlayout, parent, false)
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

            val community = communityUnitss[position]
            subject.text = community.communityUnitName
            subject.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT

            message.text = community.linkFacilityId
            message.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT

            iconText.text = community.communityUnitName.substring(0, 1)
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(community.getColor())
            iconText.visibility = View.VISIBLE
            iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_white_24dp))
            iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_warning))
            iconImp.setOnLongClickListener {
                // really delete the Item?
                val alertDialog = AlertDialog.Builder(getContext())
                alertDialog.setTitle("Confirm Delete")
                alertDialog.setMessage("Are you sure you want to delete " + community.communityUnitName + "?")
                alertDialog.setIcon(R.drawable.ic_delete_white_24dp)
                alertDialog.setPositiveButton("Delete") { dialog, which ->
                    // Check if there are records dependent on this item
                    val registrations = RegistrationTable(getContext())
                            .getRegistrationsByRecruitmentAndCommunityUnit(session
                                    .savedRecruitment, community)
                    if (registrations.size <= 0) {
                        communityUnits.removeAt(position)
                        //arrayAdapter.notifyDataSetChanged();
                        deleteCommunityUnit(community)
                    }
                }
                alertDialog.setNegativeButton("Cancel") { dialog, which ->
                    // Check if there are records dependent on this item
                    dialog.cancel()
                }
                alertDialog.show()
                false
            }

            val itemClickListener = View.OnClickListener {
                // Set the CU, and filter the registrations by CU
                val sessionManagement = SessionManagement(getContext())
                sessionManagement.saveCommunityUnit(community)
                val registrationsFragment = RegistrationsFragment()
                registrationsFragment.communityUnit = community
                val fragmentTransaction = activity!!
                        .supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, registrationsFragment, MainActivity.TAG_REGISTRATIONS)
                fragmentTransaction.commitAllowingStateLoss()
            }
            registrationContainser.setOnClickListener(itemClickListener)

            val longClick = View.OnLongClickListener {
                val newCommunityUnitFragment = NewCommunityUnitFragment()
                newCommunityUnitFragment.editingCommunityUnit = community
                newCommunityUnitFragment.backFragment = RecruitmentViewFragment()
                val fragmentTransaction = activity!!
                        .supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, newCommunityUnitFragment, MainActivity.TAG_REGISTRATIONS)
                fragmentTransaction.commitAllowingStateLoss()
                false
            }
            registrationContainser.setOnLongClickListener(longClick)

            return rowView
        }
    }

    fun deleteCommunityUnit(communityUnit: CommunityUnit) {
        CommunityUnitTable(context!!).deleteCommunityUnit(communityUnit)
        cuAdapter.notifyDataSetChanged()
    }

    fun deleteChewReferral(chewReferral: ChewReferral) {
        ChewReferralTable(context!!).deleteChewReferral(chewReferral)
        adapter.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.title = session.savedRecruitment.name + " Recruitment"
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): RecruitmentViewFragment {
            val fragment = RecruitmentViewFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
