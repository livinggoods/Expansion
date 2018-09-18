package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.view.ActionMode
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import android.support.v7.app.AppCompatActivity


import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.dbhelpers.RecruitmentListAdapter
import com.expansion.lg.kimaru.expansion.sync.IccmDataSync
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable

import java.util.ArrayList
import java.util.HashMap


// to show list in Gmail Mode

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RecruitmentsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RecruitmentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecruitmentsFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    internal lateinit var textshow: TextView
    internal lateinit var fab: FloatingActionButton

    // to show list in Gmail Mode
    private val recruitments = ArrayList<Recruitment>()
    private var recyclerView: RecyclerView? = null
    private var rAdapter: RecruitmentListAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var actionModeCallback: ActionModeCallback? = null
    private var actionMode: ActionMode? = null

    internal var a = AppCompatActivity()

    internal lateinit var session: SessionManagement
    internal lateinit var user: HashMap<String, String>


    // I cant seem to get the context working
    internal var mContext = context
    internal var activity: Activity? = getActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_exams, container, false)
        textshow = v.findViewById<View>(R.id.textShow) as TextView
        MainActivity.CURRENT_TAG = MainActivity.TAG_RECRUITMENTS
        MainActivity.backFragment = HomeFragment()
        //session Management
        session = SessionManagement(context!!)
        user = session.userDetails
        fab = v.findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val fragment: Fragment
            if (user[SessionManagement.KEY_USER_COUNTRY].equals("KE", ignoreCase = true)) {
                fragment = NewKeRecruitmentFragment()
                val s = SubCountyTable(context!!).subCountyData.size
                if (s == 0) {
                    // wait till syncing is done..
                    val progressDialog = ProgressDialog(context)
                    progressDialog.setTitle("Syncing Location Data")
                    progressDialog.setMessage("Please wait ...")
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    val iccmDataSync = IccmDataSync(context!!)
                    iccmDataSync.pollNewComponents()
                    val locationDataSync = LocationDataSync(context!!)
                    locationDataSync.getKeSubcounties()
                    progressDialog.dismiss()
                }
            } else {
                fragment = NewRecruitmentFragment()
            }
            val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            fragmentTransaction.replace(R.id.frame, fragment, "villages")
            fragmentTransaction.commitAllowingStateLoss()
        }

        // ============Gmail View starts here =======================
        // Gmail View.

        recyclerView = v.findViewById<View>(R.id.recycler_view) as RecyclerView
        swipeRefreshLayout = v.findViewById<View>(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            // onRefresh action here
            getRecruitments()
        }
        rAdapter = RecruitmentListAdapter(this.context!!, recruitments, object : RecruitmentListAdapter.RecruitmentListAdapterListener {
            override fun onIconClicked(position: Int) {
                val recruitment = recruitments[position]
                session.saveRecruitment(recruitment)
            }

            override fun onIconImportantClicked(position: Int) {
                //when you click ka-macho, it sould open for you the details of the recruitment
                val recruitment = recruitments[position]
                session.saveRecruitment(recruitment)

            }

            override fun onMessageRowClicked(position: Int) {
                // go to view the recruitment
                val recruitment = recruitments[position]
                session.saveRecruitment(recruitment)

                //Fragment fragment = new RegistrationsFragment();
                val fragment = RecruitmentViewFragment()
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()

            }

            override fun onRowLongClicked(position: Int) {

                //extract the clicked recruitment
                val recruitment = recruitments[position]
                session.saveRecruitment(recruitment)
                val fragment: Fragment
                val country = user[SessionManagement.KEY_USER_COUNTRY]
                if (country.equals("KE", ignoreCase = true)) {
                    val newKeRecruitmentFragment = NewKeRecruitmentFragment()
                    newKeRecruitmentFragment.editingRecruitment = recruitment
                    fragment = newKeRecruitmentFragment
                } else {
                    val newRecruitmentFragment = NewRecruitmentFragment()
                    newRecruitmentFragment.editingRecruitment = recruitment
                    fragment = newRecruitmentFragment
                }
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()
            }
        })
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addItemDecoration(DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL))
        recyclerView!!.adapter = rAdapter
        actionModeCallback = ActionModeCallback()
        swipeRefreshLayout!!.post { getRecruitments() }
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

    private fun getRecruitments() {
        swipeRefreshLayout!!.isRefreshing = true

        recruitments.clear()

        val recruitmentTable = RecruitmentTable(context!!)
        var recruitmentList: List<Recruitment> = ArrayList()

        recruitmentList = recruitmentTable.getRecruitmentDataByCountryCode(user[SessionManagement.KEY_USER_COUNTRY]!!)
        for (recruitment in recruitmentList) {
            recruitment.setColor(getRandomMaterialColor("400"))
            recruitments.add(recruitment)
        }
        rAdapter!!.notifyDataSetChanged()
        swipeRefreshLayout!!.isRefreshing = false


        //        try {
        //            // get the registrations
        //            RecruitmentTable recruitmentTable = new RecruitmentTable(getContext());
        //            List<Recruitment> recruitmentList = new ArrayList<>();
        //
        //            recruitmentList = recruitmentTable.getRecruitmentDataByCountryCode(user.get(SessionManagement.KEY_USER_COUNTRY));
        //            for (Recruitment recruitment : recruitmentList){
        //                recruitment.setColor(getRandomMaterialColor("400"));
        //                recruitments.add(recruitment);
        //            }
        //            rAdapter.notifyDataSetChanged();
        //            swipeRefreshLayout.setRefreshing(false);
        //        } catch (Exception error){
        //            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        //            textshow.setText(error.getMessage());
        //        }
        swipeRefreshLayout!!.isRefreshing = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (getActivity() as AppCompatActivity).supportActionBar!!.title = " Recruitments "
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
        fun newInstance(param1: String, param2: String): RecruitmentsFragment {
            val fragment = RecruitmentsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    //====================================== End Gmail Methods======================================

}// Required empty public constructor
