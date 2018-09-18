package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

// to show list in Gmail Mode
import android.content.res.TypedArray
import android.graphics.Color
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.view.ActionMode
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.Education
import com.expansion.lg.kimaru.expansion.mzigos.Exam
import com.expansion.lg.kimaru.expansion.mzigos.Interview
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.other.DisplayDate
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.EducationTable
import com.expansion.lg.kimaru.expansion.tables.ExamTable
import com.expansion.lg.kimaru.expansion.tables.InterviewTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable
import com.expansion.lg.kimaru.expansion.dbhelpers.RegistrationListAdapter
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable
import com.expansion.lg.kimaru.expansion.tables.UserTable
import com.expansion.lg.kimaru.expansion.tables.WardTable

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.ArrayList
import java.util.HashMap
import java.util.UUID

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegistrationsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RegistrationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationsFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    internal lateinit var textshow: TextView

    // to show list in Gmail Mode
    private val registrations = ArrayList<Registration>()
    private var recyclerView: RecyclerView? = null
    private var rAdapter: RegistrationListAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionModeCallback? = null
    internal lateinit var fab: FloatingActionButton

    internal var communityUnit: CommunityUnit? = null

    internal lateinit var session: SessionManagement
    internal lateinit var recruitment: Recruitment
    internal lateinit var user: HashMap<String, String>
    internal lateinit var country: String
    internal var permissionsRequired = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val permissionStatus: SharedPreferences? = null
    private var sentToSettings = false


    // I cant seem to get the context working
    internal var mContext = context
    internal var activity: Activity? = getActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val v = inflater.inflate(R.layout.fragment_registrations, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_REGISTRATIONS
        MainActivity.backFragment = RecruitmentViewFragment()
        textshow = v.findViewById<View>(R.id.textShow) as TextView
        //session Management
        session = SessionManagement(context!!)
        recruitment = session.savedRecruitment
        user = session.userDetails
        country = user[SessionManagement.KEY_USER_COUNTRY]!!
        fab = v.findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val fragment: Fragment
            if (user[SessionManagement.KEY_USER_COUNTRY].equals("KE", ignoreCase = true)) {
                fragment = NewKeRegistrationFragment()
            } else {
                fragment = NewRegistrationFragment()
            }
            val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            fragmentTransaction.replace(R.id.frame, fragment, "villages")
            fragmentTransaction.commitAllowingStateLoss()
        }


        recyclerView = v.findViewById<View>(R.id.recycler_view) as RecyclerView
        swipeRefreshLayout = v.findViewById<View>(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            // onRefresh action here
            Toast.makeText(context, "Refreshing the list", Toast.LENGTH_SHORT).show()
            getRegistrations()
        }
        rAdapter = RegistrationListAdapter(context!!, registrations, object : RegistrationListAdapter.RegistrationListAdapterListener {
            override fun onIconClicked(position: Int) {
                val registration = registrations[position]
                session.saveRegistration(registration)
                registrations[position] = registration
                rAdapter!!.notifyDataSetChanged()
                val fragment = RegistrationViewFragment()
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()
            }

            override fun onIconImportantClicked(position: Int) {
                val registration = registrations[position]
                session.saveRegistration(registration)
                registrations[position] = registration
                rAdapter!!.notifyDataSetChanged()
                val fragment = RegistrationViewFragment()
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()
            }

            override fun onMessageRowClicked(position: Int) {
                // read the message which removes bold from the row
                val registration = registrations[position]
                session.saveRegistration(registration)
                registrations[position] = registration
                rAdapter!!.notifyDataSetChanged()
                val fragment = RegistrationViewFragment()
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()
            }

            override fun onRowLongClicked(position: Int) {
                // When one long presses a registration, we give them a chance to
                // edit the selected applicant

                //extract the clicked recruitment
                val registration = registrations[position]
                session.saveRegistration(registration)
                val fragment: Fragment
                if (country.equals("KE", ignoreCase = true)) {
                    val newKeRegistrationFragment = NewKeRegistrationFragment()
                    newKeRegistrationFragment.editingRegistration = registration
                    fragment = newKeRegistrationFragment
                } else {
                    val newRegistrationFragment = NewRegistrationFragment()
                    newRegistrationFragment.editingRegistration = registration
                    fragment = newRegistrationFragment
                }
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "interviews")

                fragmentTransaction.commitAllowingStateLoss()

            }

        })
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addItemDecoration(DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL))
        recyclerView!!.adapter = rAdapter
        actionModeCallback = ActionModeCallback()
        swipeRefreshLayout!!.post { getRegistrations() }

        //        actionModeCallback = new ActionMode().Callback;


        //===========Gmail View Ends here ============================
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (getActivity() as AppCompatActivity).supportActionBar!!.title = recruitment.name + " Registrations"
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


    // ===================================== Gmail View Methods ====================================
    private fun enableActionMode(position: Int) {
        if (actionMode == null) {
            Toast.makeText(context, "Values of Enabled", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "Values of Enabled", Toast.LENGTH_SHORT).show()
        toggleSelection(position)
    }

    private fun toggleSelection(position: Int) {
        rAdapter!!.toggleSelection(position)
        val count = rAdapter!!.selectedItemCount

        if (count == 0) {
            actionMode!!.finish()
        } else {
            actionMode!!.title = count.toString()
            actionMode!!.invalidate()
        }
    }

    /*
    *  Choose a random
    *  Color
     */
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


    private inner class ActionModeCallback : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.menu_action_mode, menu)

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout!!.isEnabled = false
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_delete -> {
                    // delete all the selected messages
                    deleteMessages()
                    mode.finish()
                    return true
                }

                else -> return false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            rAdapter!!.clearSelections()
            swipeRefreshLayout!!.isEnabled = true
            actionMode = null
            recyclerView!!.post {
                rAdapter!!.resetAnimationIndex()
                // mAdapter.notifyDataSetChanged();
            }
        }
    }

    // deleting the messages from recycler view
    private fun deleteMessages() {
        rAdapter!!.resetAnimationIndex()
        val selectedItemPositions = rAdapter!!.getSelectedItems()
        for (i in selectedItemPositions.indices.reversed()) {
            rAdapter!!.removeData(selectedItemPositions[i])
        }
        rAdapter!!.notifyDataSetChanged()
    }

    private fun getRegistrations() {
        swipeRefreshLayout!!.isRefreshing = true

        registrations.clear()

        // clear the registrations
        try {
            // get the registrations
            // Depending on the mode
            val registrationTable = RegistrationTable(context!!)
            var registrationList: List<Registration> = ArrayList()
            if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("KE", ignoreCase = true)) {
                if (communityUnit != null) {
                    registrationList = registrationTable.getRegistrationsByRecruitmentAndCommunityUnit(
                            session.savedRecruitment, communityUnit!!)
                } else {
                    registrationList = registrationTable.getRegistrationsByRecruitment(session.savedRecruitment)
                }
            } else {
                registrationList = registrationTable.getRegistrationsByRecruitment(session.savedRecruitment)
            }

            for (registration in registrationList) {
                registration.setColor(getRandomMaterialColor("400"))
                registrations.add(registration)
            }
            rAdapter!!.notifyDataSetChanged()
            swipeRefreshLayout!!.isRefreshing = false
        } catch (error: Exception) {
            Toast.makeText(context, "No Registrations", Toast.LENGTH_SHORT).show()

            textshow.text = " No registration recorded"
        }

        swipeRefreshLayout!!.isRefreshing = false
    }


    private fun getFilteredRegistrations(which: String) {
        swipeRefreshLayout!!.isRefreshing = true

        registrations.clear()

        // clear the registrations
        val registrationTable = RegistrationTable(context!!)
        var registrationList: List<Registration> = ArrayList()
        try {
            // get the registrations
            // Depending on the mode
            if (which === "passed") {
                registrationList = registrationTable.getPassedRegistrations(session.savedRecruitment, true)
            } else if (which === "failed") {
                registrationList = registrationTable.getPassedRegistrations(session.savedRecruitment, false)
            } else {
                registrationList = registrationTable.getRegistrationsByRecruitment(session.savedRecruitment)
            }

            for (registration in registrationList) {
                registration.setColor(getRandomMaterialColor("400"))
                registrations.add(registration)
            }
            rAdapter!!.notifyDataSetChanged()
            swipeRefreshLayout!!.isRefreshing = false
        } catch (error: Exception) {
            Toast.makeText(context, "No Registrations", Toast.LENGTH_SHORT).show()

            textshow.text = " No registration recorded"
        }

        swipeRefreshLayout!!.isRefreshing = false
    }

    private fun getSearchedRegistrations(query: String) {
        swipeRefreshLayout!!.isRefreshing = true

        // clear the registrations
        registrations.clear()
        val registrationTable = RegistrationTable(context!!)
        var registrationList: List<Registration> = ArrayList()
        try {
            // get the registrations
            registrationList = registrationTable.searchRegistrations(session.savedRecruitment, query)
            for (registration in registrationList) {
                registration.setColor(getRandomMaterialColor("400"))
                registrations.add(registration)
            }
            rAdapter!!.notifyDataSetChanged()
            swipeRefreshLayout!!.isRefreshing = false
        } catch (error: Exception) {
            Toast.makeText(context, "No Registrations", Toast.LENGTH_SHORT).show()
            textshow.text = " No registration recorded"
        }

        swipeRefreshLayout!!.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.registration_action_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        // searchView.setSuggestionsAdapter(new SearchSuggestionsAdapter(this));
        //        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener()
        //        {
        //            @Override
        //            public boolean onSuggestionClick(int position)
        //            {
        //                Toast.makeText(SearchActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
        //                searchView.clearFocus();
        //                return true;
        //            }
        //
        //            @Override
        //            public boolean onSuggestionSelect(int position)
        //            {
        //                return false;
        //            }
        //        });
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getSearchedRegistrations(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        when (item!!.itemId) {
            // action with ID action_refresh was selected
            R.id.action_export_scoring_tool -> {
                // export scoring tool
                Toast.makeText(context, "Exporting Scoring tool", Toast.LENGTH_SHORT).show()
                checkPermissions()
            }
            // action with ID action_settings was selected
            R.id.action_passed ->
                // refresh the registrations
                getFilteredRegistrations("passed")

            R.id.action_failed ->
                // refresh the registrations
                getFilteredRegistrations("failed")

            R.id.action_all ->
                // refresh the registrations
                getFilteredRegistrations("all")
            R.id.action_new -> {
                if (user[SessionManagement.KEY_USER_COUNTRY].equals("KE", ignoreCase = true)) {
                    fragment = NewKeRegistrationFragment()
                } else {
                    fragment = NewRegistrationFragment()
                }
                fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()
            }

            else -> {
            }
        }



        return true
    }

    private fun exportScoringTool() {
        // only export the scoring tool for the current recruitment
        // 1. Check if the Externa Storage is available
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED != state) {
            return
        } else {
            //We used Download Dir
            val exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }
            val file: File
            var printWriter: PrintWriter? = null
            file = File(exportDir, recruitment.name + " Scoring Tool.csv")
            try {
                file.createNewFile()
                printWriter = PrintWriter(FileWriter(file))

                //here we get the cursor that contains our records
                val registrationTable = RegistrationTable(context!!)
                val educationTable = EducationTable(context!!)


                if (country.equals("KE", ignoreCase = true)) {
                    val keHeader = "CHEW Name," +
                            "CHEW Contact," +
                            "Candidate Name," +
                            "Candidate Mobile," +
                            "Gender," +
                            "Marital Status," +
                            "Year of Birth," +
                            "Age," +
                            "Subcounty," +  // Shown as UUID, Requires a name

                            "Ward," + // Shown as UUID, Requires a name

                            "Village/zone/cell, " +
                            "Landmark, " +
                            "CU (Community Unit), " + // Shown as UUID, Requires a name

                            "Link Facility Name, " +
                            "Link Facility Code, " +
                            "No of Households," +
                            "Read/speak English," +
                            "Years at this CountyLocation," +
                            "Other Languages," +
                            "CHV," +
                            "GOK Training," +
                            "Other Trainings," +
                            "Highest education level," +
                            "Previous/Current health or business experience," +
                            "Community group membership," +
                            "Financial Accounts," +
                            "Recruitment Comments," +
                            "Math Score," +
                            "Reading Comprehension," +
                            "About you," +
                            "Total Score," +
                            "Eligible for Interview," +  // pass or not

                            "Interview: Overall Motivation," +
                            "Interview: Ability to work with communities," +
                            "Interview: Mentality," +
                            "Interview:Selling skills," +
                            "Interview: Interest in health," +
                            "Interview: Ability to invest," +
                            "Interview: Interpersonal skills," +
                            "Interview: Ability to commit," +
                            "Interview Score," +
                            "DO NOT ASK OUTLOUD: Any conditions to prevent joining?," + //canJoin

                            "Tranport as Per Recruitment," + //canJoin

                            "Comments," +
                            "Qualify for Training," +
                            "Completed By," +
                            "Invite for Training"  //selected

                    // add for interview and exam
                    printWriter.println(keHeader)
                    // Print the rows
                    for (registration in registrations) {
                        val exam = ExamTable(context!!).getExamByRegistration(registration.id)
                        val education = educationTable.getEducationById(Integer.valueOf(registration.education))
                        val interview = InterviewTable(context!!).getInterviewByRegistrationId(registration.id)
                        var motivation: Int? = 0
                        var community: Int? = 0
                        var mentality: Int? = 0
                        var selling: Int? = 0
                        var recruitmentTransport: Long? = 0L
                        var health: Int? = 0
                        var investment: Int? = 0
                        var interpersonal: Int? = 0
                        var commitment: Int? = 0
                        var totalI: Int? = 0
                        var invite = "N"
                        var canJoin = "N"
                        var userNames = ""
                        var comments = ""
                        var qualify = "N"
                        var hasPassedExam = "N"
                        recruitmentTransport = registration.recruitmentTransportCost
                        if (recruitmentTransport == null) {
                            recruitmentTransport = 0L
                        }
                        var math: Double? = 0.0
                        var english: Double? = 0.0
                        var personality: Double? = 0.0
                        var total: Double? = 0.0
                        if (exam != null) {
                            math = exam.math
                            english = exam.english
                            personality = exam.personality
                            total = math!! + english!! + personality!!
                            hasPassedExam = if (exam.hasPassed()) "Y" else "N"
                        }
                        if (interview != null) {
                            try {
                                motivation = interview.motivation
                                community = interview.community
                                mentality = interview.mentality
                                selling = interview.selling
                                health = interview.health
                                investment = interview.investment
                                interpersonal = interview.interpersonal
                                commitment = interview.commitment
                                totalI = interview.getTotal()
                                canJoin = if (interview.isCanJoin) "Y" else "N"
                                comments = interview.comment
                                qualify = if (interview.hasPassed()) "Y" else "N"
                                invite = if (interview.selected == 1) "Y" else if (interview.selected == 0) "N" else "Waiting"

                            } catch (e: Exception) {
                            }

                        }
                        try {
                            userNames = UserTable(context!!).getUserById(interview!!.addedBy!!)!!.name
                        } catch (e: Exception) {
                        }

                        val chewReferral = ChewReferralTable(context!!).getChewReferralById(registration.chewUuid)

                        val strRegistration = chewReferral!!.name + "," +
                                chewReferral.phone + "," +
                                registration.name + "," +
                                registration.phone.replace(",".toRegex(), ";") + "," +
                                registration.gender + "," +
                                registration.maritalStatus + "," +
                                DisplayDate(registration.dob).dateOnly() + "," +  //dd/mm/yyyy format

                                registration.age + "," +
                                //registration.getSubcounty() +","+
                                SubCountyTable(context!!).getSubCountyById(registration.subcounty)!!.subCountyName + "," +
                                //registration.getWard()+","+
                                WardTable(context!!).getWardById(registration.ward)!!.name + "," +
                                registration.village + "," +
                                registration.mark.replace(",".toRegex(), ";") + "," +
                                // registration.getCuName().replaceAll(",", ";") +","+
                                CommunityUnitTable(context!!).getCommunityUnitById(registration.cuName)!!.communityUnitName + "," +
                                LinkFacilityTable(context!!).getLinkFacilityById(registration.linkFacility)!!.facilityName + "," +
                                LinkFacilityTable(context!!).getLinkFacilityById(registration.linkFacility)!!.mflCode + "," +
                                // registration.getLinkFacility().replaceAll(",", ";") +","+
                                registration.noOfHouseholds + "," +
                                (if (registration.readEnglish == 1) "Y" else "N") + "," +
                                registration.dateMoved + "," +
                                registration.langs.replace(",".toRegex(), ";") + "," +
                                (if (registration.isChv) "Y" else "N") + "," +
                                (if (registration.isGokTrained) "Y" else "N") + "," +
                                registration.otherTrainings.replace(",".toRegex(), ";") + "," +
                                education!!.levelName + "," +
                                registration.occupation.replace(",".toRegex(), ";") + "," +
                                (if (registration.community == 1) "Y" else "N") + "," +
                                (if (registration.isAccounts) "Y" else "N") + "," +
                                registration.comment.replace(",".toRegex(), ";") + "," +
                                math!!.toString() + "," +
                                english!!.toString() + "," +
                                personality!!.toString() + "," +
                                total!!.toString() + "," +
                                hasPassedExam + "," + // has passed

                                motivation!!.toString() + "," +
                                community!!.toString() + "," +
                                mentality!!.toString() + "," +
                                selling!!.toString() + "," +
                                health!!.toString() + "," +
                                investment!!.toString() + "," +
                                interpersonal!!.toString() + "," +
                                commitment!!.toString() + "," +
                                totalI!!.toString() + "," +
                                canJoin + "," +
                                recruitmentTransport.toString() + "," +
                                comments.trim { it <= ' ' }.replace(",".toRegex(), ";").replace("\n".toRegex(), ":").replace("\r".toRegex(), ":") + "," +
                                qualify + "," +
                                userNames + "," +
                                invite
                        printWriter.println(strRegistration)
                    }

                } else {
                    val ugHeader = "Referral Name," +
                            "Referral Title," +
                            "Referral Mobile No," +
                            "VHT?," +
                            "Candidate Name," +
                            "Candidate Mobile," +
                            "Gender," +
                            "Age," +
                            "District," +
                            "Subcounty," +
                            "Parish," +
                            "Village/zone/cell, " +
                            "Landmark, " +
                            "Read/Speak English, " +
                            "Other Languages," +
                            "Years at this location," +
                            "Ever worked with BRAC?," +
                            "If yes as BRAC CHP?," +
                            "Highest Educational," +
                            "Community group memberships," +
                            "Maths Score," +
                            "Reading Comprehension," +
                            "About You," +
                            "Total Marks," +
                            "Eligible for Interview," +
                            "Interview Completed by," +
                            "Interview: Overall Motivation," +
                            "Interview: Ability to work with communities," +
                            "Interview: Mentality," +
                            "Interview:Selling skills," +
                            "Interview: Interest in health," +
                            "Interview: Ability to invest," +
                            "Interview: Interpersonal skills," +
                            "Interview: Ability to commit," +
                            "Interview Score," +
                            "DO NOT ASK OUTLOUD: Any conditions to prevent joining?," +
                            "Comments," +
                            "Qualify for Training," +
                            "Invite for Training"

                    // add for interview and exam
                    printWriter.println(ugHeader)
                    // Print the rows
                    for (registration in registrations) {
                        val exam = ExamTable(context!!).getExamByRegistration(registration.id)
                        val interview = InterviewTable(context!!).getInterviewByRegistrationId(registration.id)
                        var motivation: Int? = 0
                        var community: Int? = 0
                        var mentality: Int? = 0
                        var selling: Int? = 0
                        var health: Int? = 0
                        var investment: Int? = 0
                        var interpersonal: Int? = 0
                        var commitment: Int? = 0
                        var totalI: Int? = 0
                        var invite = "N"
                        var canJoin = "N"
                        var userNames = ""
                        var comments = ""
                        var qualify = "N"

                        var math: Double? = 0.0
                        var english: Double? = 0.0
                        var personality: Double? = 0.0
                        var total: Double? = 0.0
                        var passedRegistrationAndExam = false
                        if (exam != null) {
                            math = exam.math
                            english = exam.english
                            personality = exam.personality
                            total = math!! + english!! + personality!!
                            if (registration.hasPassed() && exam.hasPassed()) {
                                passedRegistrationAndExam = true
                            } else {
                                passedRegistrationAndExam = false
                            }

                        }
                        if (interview != null) {
                            motivation = interview.motivation
                            community = interview.community
                            mentality = interview.mentality
                            selling = interview.selling
                            health = interview.health
                            investment = interview.investment
                            interpersonal = interview.interpersonal
                            commitment = interview.commitment
                            totalI = interview.getTotal()
                            canJoin = if (interview.isCanJoin) "Y" else "N"
                            comments = interview.comment
                            qualify = if (interview.hasPassed()) "Y" else "N"
                            invite = if (interview.selected == 1) "Y" else if (interview.selected == 0) "N" else "Waiting"
                            userNames = UserTable(context!!).getUserById(interview.addedBy!!)!!.name
                        }
                        //chewReferral.getName() +","+
                        //chewReferral.getPhone()+","+
                        val chewReferral = ChewReferralTable(context!!).getChewReferralById(registration.chewUuid)
                        val record = chewReferral!!.name + "," +
                                chewReferral.title + "," +
                                chewReferral.phone + "," +
                                (if (registration.isVht) "Y" else "N") + "," +
                                registration.name + "," +
                                registration.phone + "," +
                                registration.gender + "," +
                                registration.age + "," +
                                recruitment.district + "," +
                                registration.subcounty + "," +
                                registration.parish + "," +
                                registration.village + "," +
                                registration.mark + "," +
                                (if (registration.readEnglish == 1) "Y" else "N") + "," +
                                registration.langs.replace(",".toRegex(), ";") + "," +
                                registration.dateMoved + "," +
                                (if (registration.brac == 1) "Y" else "N") + "," +
                                (if (registration.bracChp == 1) "Y" else "N") + "," +
                                registration.education + "," +
                                (if (registration.community == 1) "Y" else "N") + "," +
                                math!!.toString() + "," +
                                english!!.toString() + "," +
                                personality!!.toString() + "," +
                                total + "," +
                                (if (passedRegistrationAndExam) "Y" else "N") + "," +  // Has passed Interview and Exam

                                userNames + "," +
                                motivation + "," +
                                community + "," +
                                mentality + "," +
                                selling + "," +
                                health + "," +
                                investment + "," +
                                interpersonal + "," +
                                commitment + "," +
                                totalI + "," +
                                canJoin + "," +
                                comments.replace(",".toRegex(), ";") + "," +
                                qualify + "," +
                                invite
                        printWriter.println(record)
                    }
                }

            } catch (e: Exception) {
            } finally {
                printWriter?.close()
            }
            Toast.makeText(context, "Tool exported to " + file.absolutePath + " Folder", Toast.LENGTH_LONG).show()
        }

    }

    fun checkPermissions() {
        try {
            if (ActivityCompat.checkSelfPermission(context!!, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity()!!, permissionsRequired[0])) {
                    //Show Information about why you need the permission
                    val builder = AlertDialog.Builder(context!!)
                    builder.setTitle("Multiple Permissions Request")
                    builder.setMessage("This app needs Location permissions")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()
                        ActivityCompat.requestPermissions(getActivity()!!, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                    }
                    builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                    builder.show()
                } else if (permissionStatus!!.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    val builder = AlertDialog.Builder(context!!)
                    builder.setTitle("Multiple Permissions Request")
                    builder.setMessage("This app needs Location permissions")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()
                        sentToSettings = true
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", context!!.packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                        Toast.makeText(context, "Go to Permissions to Grant Location Permissions",
                                Toast.LENGTH_LONG).show()
                    }
                    builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(getActivity()!!, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                val editor = permissionStatus!!.edit()
                editor.putBoolean(permissionsRequired[0], true)
                editor.commit()
            } else {
                proceedAfterPermission()
            }
        } catch (e: Exception) {
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }

            if (allgranted) {
                proceedAfterPermission()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity()!!, permissionsRequired[0])) {
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Location permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(getActivity()!!, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                Toast.makeText(context, "Unable to get Permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(context!!, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(context!!, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission()
            }
        }
    }

    private fun proceedAfterPermission() {
        exportScoringTool()
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        private val PERMISSION_CALLBACK_CONSTANT = 101
        private val REQUEST_PERMISSION_SETTING = 102

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): RegistrationsFragment {
            val fragment = RegistrationsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}
