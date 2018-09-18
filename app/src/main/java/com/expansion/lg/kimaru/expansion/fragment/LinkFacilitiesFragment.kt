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
import com.expansion.lg.kimaru.expansion.dbhelpers.LinkFacilityListAdapter
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable

import java.util.ArrayList


// to show list in Gmail Mode

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LinkFacilitiesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LinkFacilitiesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LinkFacilitiesFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    internal lateinit var textshow: TextView

    // to show list in Gmail Mode
    private val linkFacilities = ArrayList<LinkFacility>()
    private var recyclerView: RecyclerView? = null
    private var rAdapter: LinkFacilityListAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionModeCallback? = null
    internal lateinit var fab: FloatingActionButton

    internal lateinit var session: SessionManagement


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
        //session Management
        session = SessionManagement(context!!)
        MainActivity.CURRENT_TAG = MainActivity.TAG_LINK_FACILITIES


        if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
            MainActivity.backFragment = ParishViewFragment()
        } else {
            MainActivity.backFragment = SubCountyViewFragment()
        }


        fab = v.findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val fragment = NewLinkFacilityFragment()
            val fragmentManager = getActivity()!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right)
            fragmentTransaction.replace(R.id.frame, fragment, "subcounties")
            fragmentTransaction.commitAllowingStateLoss()
        }

        recyclerView = v.findViewById<View>(R.id.recycler_view) as RecyclerView
        swipeRefreshLayout = v.findViewById<View>(R.id.swipe_refresh_layout) as SwipeRefreshLayout

        rAdapter = LinkFacilityListAdapter(this.context!!, linkFacilities, object : LinkFacilityListAdapter.LinkFacilityListAdapterListener {
            override fun onIconClicked(position: Int) {
                //
                val linkFacility = linkFacilities[position]
                session.saveLinkFacility(linkFacility)
            }

            override fun onIconImportantClicked(position: Int) {
                session.saveLinkFacility(linkFacilities[position])
            }

            override fun onMessageRowClicked(position: Int) {
                if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("KE", ignoreCase = true)) {
                    val linkFacility = linkFacilities[position]
                    session.saveLinkFacility(linkFacilities[position])

                    val fragment = LinkFacilityViewFragment()
                    val fragmentManager = getActivity()!!.supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right)
                    fragmentTransaction.replace(R.id.frame, fragment, "subcounties")
                    fragmentTransaction.commitAllowingStateLoss()
                }

            }

            override fun onRowLongClicked(position: Int) {
                val linkFacility = linkFacilities[position]
                val linkFacilityFragment = NewLinkFacilityFragment()
                linkFacilityFragment.editingLinkFacility = linkFacility
                val fragmentManager = getActivity()!!.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                fragmentTransaction.replace(R.id.frame, linkFacilityFragment, "")
                fragmentTransaction.commitAllowingStateLoss()

            }

        })
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addItemDecoration(DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL))
        recyclerView!!.adapter = rAdapter
        actionModeCallback = ActionModeCallback()
        swipeRefreshLayout!!.post { getLinkFacilities() }
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

    private fun enableActionMode(position: Int) {
        if (actionMode == null) {
            //Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val parishName: String
        if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
            parishName = session.savedParish.name
        } else {
            parishName = session.savedSubCounty.subCountyName
        }
        (getActivity() as AppCompatActivity).supportActionBar!!.title = "$parishName Health Facilities"
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

    private fun getLinkFacilities() {
        swipeRefreshLayout!!.isRefreshing = true

        linkFacilities.clear()

        try {
            // get CUs
            val linkFacilityTable = LinkFacilityTable(context!!)
            var linkFacilityList: List<LinkFacility> = ArrayList()

            //linkFacilityList = linkFacilityTable.getLinkFacilityBySubCounty(session.getSavedSubCounty().getId());

            //            Integer linkFacilities = linkFacilityTable
            //                    .getLinkFacilityBySubCounty(String.valueOf(subCounty.getId())).size();
            //
            if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
                linkFacilityList = linkFacilityTable.getLinkFacilityByParish(session.savedParish.id)
            } else {
                linkFacilityList = linkFacilityTable.getLinkFacilityBySubCounty(session.savedSubCounty.id)
            }

            for (linkFacility in linkFacilityList) {
                linkFacility.color = getRandomMaterialColor("400")
                linkFacilities.add(linkFacility)
            }
            rAdapter!!.notifyDataSetChanged()
            swipeRefreshLayout!!.isRefreshing = false
        } catch (error: Exception) {
            Toast.makeText(context, "No Link facility found ", Toast.LENGTH_SHORT).show()
            textshow.text = "Error " + "\n\n\n" + error.message
            swipeRefreshLayout!!.isRefreshing = false
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
        fun newInstance(param1: String, param2: String): LinkFacilitiesFragment {
            val fragment = LinkFacilitiesFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
