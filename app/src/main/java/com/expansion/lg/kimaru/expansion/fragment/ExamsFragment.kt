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
import com.expansion.lg.kimaru.expansion.mzigos.Exam
import com.expansion.lg.kimaru.expansion.dbhelpers.ExamListAdapter
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.tables.ExamTable
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable

import java.util.ArrayList


// to show list in Gmail Mode

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ExamsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ExamsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExamsFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    internal lateinit var textshow: TextView
    internal lateinit var fab: FloatingActionButton

    // to show list in Gmail Mode
    private val exams = ArrayList<Exam>()
    private var recyclerView: RecyclerView? = null
    private var rAdapter: ExamListAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionModeCallback? = null

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
        val v: View
        //check if Recruitment is set
        MainActivity.CURRENT_TAG = MainActivity.TAG_EXAMS
        MainActivity.backFragment = RegistrationsFragment()
        session = SessionManagement(context!!)

        if (!session.isRegistrationSet) {
            val newRegistrationsFragment = RegistrationsFragment()
            val fragmentManager = getActivity()!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out)

            fragmentTransaction.replace(R.id.frame, newRegistrationsFragment, "registrations")
            fragmentTransaction.commitAllowingStateLoss()
        }
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_exams, container, false)
        textshow = v.findViewById<View>(R.id.textShow) as TextView

        fab = v.findViewById<View>(R.id.fab) as FloatingActionButton
        fab.hide()
        //        final Fragment fragment;
        //        if (session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("KE")){
        //            fragment = new NewExamFragment();
        //        }else {
        //            fragment = new NewExamFragment();
        //        }
        //        fab.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        //                fragmentTransaction.replace(R.id.frame, fragment, "villages");
        //                fragmentTransaction.commitAllowingStateLoss();
        //            }
        //        });

        // ============Gmail View starts here =======================
        // Gmail View.

        recyclerView = v.findViewById<View>(R.id.recycler_view) as RecyclerView
        swipeRefreshLayout = v.findViewById<View>(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener { getExams() }
        rAdapter = ExamListAdapter(this.context!!, exams, object : ExamListAdapter.ExamListAdapterListener {
            override fun onIconClicked(position: Int) {

                // get the registration
                val exam = exams[position]
                //extract registration from the exam
                val regId = exam.applicant
                val registrationTable = RegistrationTable(context!!)
                val registration = registrationTable.getRegistrationById(regId)
                session.saveRegistration(registration!!)
                val fragment = RegistrationViewFragment()
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()

            }

            override fun onIconImportantClicked(position: Int) {
                // get the registration
                val exam = exams[position]
                //extract registration from the exam
                val regId = exam.applicant
                val registrationTable = RegistrationTable(context!!)
                val registration = registrationTable.getRegistrationById(regId)
                session.saveRegistration(registration!!)
                val fragment = RegistrationViewFragment()
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()
            }

            override fun onMessageRowClicked(position: Int) {
                // read the message which removes bold from the row
                val exam = exams[position]

                exam.isRead = true
                exams[position] = exam
                rAdapter!!.notifyDataSetChanged()

                //extract registration from the exam
                val regId = exam.applicant
                val registrationTable = RegistrationTable(context!!)
                val registration = registrationTable.getRegistrationById(regId)
                session.saveRegistration(registration!!)
                val fragment = RegistrationViewFragment()
                val fragmentTransaction = getActivity()!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "registrations")
                fragmentTransaction.commitAllowingStateLoss()

            }

            override fun onRowLongClicked(position: Int) {
                val exam = exams[position]
                val newExamFragment = NewExamFragment()
                newExamFragment.editingExam = exam
                val fragmentManager = getActivity()!!.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)

                fragmentTransaction.replace(R.id.frame, newExamFragment, "exams")
                fragmentTransaction.commitAllowingStateLoss()
            }

        })
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addItemDecoration(DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL))
        recyclerView!!.adapter = rAdapter
        actionModeCallback = ActionModeCallback()
        swipeRefreshLayout!!.post { getExams() }

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

    private fun getExams() {
        swipeRefreshLayout!!.isRefreshing = true

        exams.clear()

        // clear the registrations
        try {
            // get the registrations
            val examTable = ExamTable(context!!)
            var examList: List<Exam> = ArrayList()

            examList = examTable.examData
            examList = examTable.getExamsByRecruitment(session.savedRecruitment)
            for (exam in examList) {
                exam.setColor(getRandomMaterialColor("400"))
                exams.add(exam)
            }
            rAdapter!!.notifyDataSetChanged()
            swipeRefreshLayout!!.isRefreshing = false
        } catch (error: Exception) {
            Toast.makeText(context, "No Exams", Toast.LENGTH_SHORT).show()
            textshow.text = "No Exams added. Please create one"
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
        fun newInstance(param1: String, param2: String): ExamsFragment {
            val fragment = ExamsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    //====================================== End Gmail Methods======================================

}// Required empty public constructor
