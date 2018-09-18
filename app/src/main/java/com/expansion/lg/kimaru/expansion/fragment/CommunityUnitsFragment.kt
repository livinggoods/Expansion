package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
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

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.dbhelpers.CommunityUnitListAdapter
import com.expansion.lg.kimaru.expansion.dbhelpers.RegistrationListAdapter
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable

import java.util.ArrayList


// to show list in Gmail Mode

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CommunityUnitsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CommunityUnitsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityUnitsFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    internal lateinit var textshow: TextView
    internal lateinit var fab: FloatingActionButton

    // to show list in Gmail Mode
    private val communityUnits = ArrayList<CommunityUnit>()
    private var recyclerView: RecyclerView? = null
    private var rAdapter: CommunityUnitListAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionModeCallback? = null

    internal lateinit var session: SessionManagement
    internal lateinit var subCounty: SubCounty
    internal var backFragment: Fragment? = null
    internal var linkFacility: LinkFacility? = null


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
        val v = inflater.inflate(R.layout.fragment_communityunits, container, false)
        textshow = v.findViewById<View>(R.id.textShow) as TextView
        fab = v.findViewById<View>(R.id.fab) as FloatingActionButton
        //session Management
        session = SessionManagement(context!!)
        MainActivity.CURRENT_TAG = MainActivity.TAG_COMMUNITY_UNITS
        if (backFragment == null) {
            MainActivity.backFragment = SubCountyViewFragment()
        } else {
            MainActivity.backFragment = backFragment
        }
        subCounty = session.savedSubCounty

        recyclerView = v.findViewById<View>(R.id.recycler_view) as RecyclerView
        swipeRefreshLayout = v.findViewById<View>(R.id.swipe_refresh_layout) as SwipeRefreshLayout


        rAdapter = CommunityUnitListAdapter(this.context!!, communityUnits, object : CommunityUnitListAdapter.CommunityUnitListAdapterListener {
            override fun onIconClicked(position: Int) {
                val communityUnit = communityUnits[position]
                session.saveCommunityUnit(communityUnit)
                val communityUnitViewFragment = CommunityUnitViewFragment()
                communityUnitViewFragment.communityUnit = communityUnit
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, communityUnitViewFragment, "mappings")
                fragmentTransaction.commitAllowingStateLoss()
            }

            override fun onIconImportantClicked(position: Int) {
                this.onIconClicked(position)
            }

            override fun onMessageRowClicked(position: Int) {
                this.onIconClicked(position)
            }

            override fun onRowLongClicked(position: Int) {
                //extract the clicked community Unit
                val communityUnit = communityUnits[position]
                session.saveCommunityUnit(communityUnit)
                val newCommunityUnitFragment = NewCommunityUnitFragment()
                newCommunityUnitFragment.editingCommunityUnit = communityUnit
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, newCommunityUnitFragment, "mappings")
                fragmentTransaction.commitAllowingStateLoss()


            }

        })
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addItemDecoration(DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL))
        recyclerView!!.adapter = rAdapter
        actionModeCallback = ActionModeCallback()
        swipeRefreshLayout!!.post { getCommunityUnits() }
        fab.setOnClickListener {
            val newCommunityUnitFragment = NewCommunityUnitFragment()
            newCommunityUnitFragment.linkFacility = linkFacility
            val fragmentManager = getActivity()!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right)
            fragmentTransaction.replace(R.id.frame, newCommunityUnitFragment, "subcounties")
            fragmentTransaction.commitAllowingStateLoss()
        }
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
        val subCountyName = subCounty.subCountyName
        (getActivity() as AppCompatActivity).supportActionBar!!.title = "$subCountyName - Community Units"
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
        for (i in selectedItemPositions.size-1..0) {
            rAdapter!!.removeData(selectedItemPositions.get(i))
        }
        rAdapter!!.notifyDataSetChanged()
    }

    private fun getCommunityUnits() {
        swipeRefreshLayout!!.isRefreshing = true

        communityUnits.clear()

        try {
            // get CUs
            val communityUnitTable = CommunityUnitTable(context!!)
            var communityUnitList: List<CommunityUnit> = ArrayList()
            if (linkFacility != null) {
                communityUnitList = communityUnitTable.getCommunityUnitByLinkFacility(linkFacility!!.id)
            } else {
                communityUnitList = communityUnitTable.getCommunityUnitBySubCounty(subCounty.id)
            }

            for (communityUnit in communityUnitList) {
                communityUnit.setColor(getRandomMaterialColor("400"))
                communityUnits.add(communityUnit)
            }
            rAdapter!!.notifyDataSetChanged()
            swipeRefreshLayout!!.isRefreshing = false
        } catch (error: Exception) {
            Toast.makeText(context, "No Community unit  found", Toast.LENGTH_SHORT).show()

            textshow.text = " No Community unit recorded "
        }

        swipeRefreshLayout!!.isRefreshing = false
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
        fun newInstance(param1: String, param2: String): CommunityUnitsFragment {
            val fragment = CommunityUnitsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
