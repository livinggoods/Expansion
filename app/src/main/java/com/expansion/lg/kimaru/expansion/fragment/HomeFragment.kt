package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.HttpServerActivity
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.other.InternetCheck
import com.expansion.lg.kimaru.expansion.sync.TrainingDataSync

import java.util.HashMap
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mHandler: Handler? = null

    internal var internetAvailable: Boolean = false

    internal var recruitment: Recruitment? = null


    internal lateinit var mapping: Button
    internal lateinit var recruitments: Button
    internal lateinit var sharing: Button
    internal lateinit var graduation: Button
    internal lateinit var cloud: Button
    internal lateinit var settings: Button
    internal lateinit var sessionManagement: SessionManagement
    internal lateinit var user: HashMap<String, String>
    internal lateinit var country: String

    private var mListener: OnFragmentInteractionListener? = null

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
        MainActivity.CURRENT_TAG = MainActivity.TAG_HOME
        MainActivity.backFragment = null
        mHandler = Handler()
        sessionManagement = SessionManagement(context!!)
        user = sessionManagement.userDetails
        country = sessionManagement.userDetails[SessionManagement.KEY_USER_COUNTRY]!!

        //

        //mapping, recruitments, registrations, exams, interviews, sharing
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        mapping = v.findViewById<View>(R.id.btnMapping) as Button
        recruitments = v.findViewById<View>(R.id.btnRecruitments) as Button
        sharing = v.findViewById<View>(R.id.btnSharing) as Button
        graduation = v.findViewById<View>(R.id.btnGraduation) as Button
        settings = v.findViewById<View>(R.id.btnSettings) as Button
        cloud = v.findViewById<View>(R.id.btnCloud) as Button


        mapping.setOnClickListener(this)
        recruitments.setOnClickListener(this)
        sharing.setOnClickListener(this)
        graduation.setOnClickListener(this)
        settings.setOnClickListener(this)
        cloud.setOnClickListener(this)


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

    override fun onClick(view: View) {
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        val mPendingRunnable: Runnable
        when (view.id) {
            R.id.btnMapping -> {
                MainActivity.navItemIndex = 5 //navItemIndex
                MainActivity.CURRENT_TAG = MainActivity.TAG_MAPPING
                fragment = MappingFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
            }

            R.id.btnRecruitments -> {
                MainActivity.navItemIndex = 1
                MainActivity.CURRENT_TAG = MainActivity.TAG_RECRUITMENTS
                fragment = RecruitmentsFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
            }

            R.id.btnSettings -> {
                MainActivity.navItemIndex = 1
                MainActivity.CURRENT_TAG = MainActivity.TAG_HOME
                fragment = SettingsFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_HOME)
                fragmentTransaction.commitAllowingStateLoss()
            }

            R.id.btnSharing -> startActivity(Intent(activity, HttpServerActivity::class.java))
            R.id.btnCloud -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants(context!!).cloudAddress))
                startActivity(browserIntent)
            }
        }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.title = "Tremap Home"
    }

    override fun onResume() {
        super.onResume()
        // register connection status listener
        // AppLaunch.getInstance().setConnectivityListener(this);
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
