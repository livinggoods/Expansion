package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement

import java.util.HashMap


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    internal lateinit var editCloudUrl: EditText
    internal lateinit var editApiVersion: EditText
    internal lateinit var editApiSuffix: EditText
    internal lateinit var editApiPrefix: EditText
    internal lateinit var editPeerServer: EditText
    internal lateinit var editPeerServerPort: EditText

    internal lateinit var saveCloudUrl: ToggleButton
    internal lateinit var saveApiVersion: ToggleButton
    internal lateinit var saveApiPrefix: ToggleButton
    internal lateinit var saveApiSuffix: ToggleButton
    internal lateinit var savePeerServer: ToggleButton
    internal lateinit var savePeerServerPort: ToggleButton
    internal lateinit var session: SessionManagement
    internal var user: HashMap<String, String>? = null

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
        val v = inflater.inflate(R.layout.fragment_settings, container, false)
        session = SessionManagement(context!!)
        // Edittexts
        editCloudUrl = v.findViewById<View>(R.id.editCloudUrl) as EditText
        editApiVersion = v.findViewById<View>(R.id.editApiVersion) as EditText
        editApiSuffix = v.findViewById<View>(R.id.editApiSuffix) as EditText
        editApiPrefix = v.findViewById<View>(R.id.editApiPrefix) as EditText
        editPeerServer = v.findViewById<View>(R.id.editPeerServer) as EditText
        editPeerServerPort = v.findViewById<View>(R.id.editPeerServerPort) as EditText

        //buttons
        saveCloudUrl = v.findViewById<View>(R.id.saveCloudUrl) as ToggleButton
        saveApiVersion = v.findViewById<View>(R.id.saveApiVersion) as ToggleButton
        saveApiPrefix = v.findViewById<View>(R.id.saveApiPrefix) as ToggleButton
        saveApiSuffix = v.findViewById<View>(R.id.saveApiSuffix) as ToggleButton
        savePeerServer = v.findViewById<View>(R.id.savePeerServer) as ToggleButton
        savePeerServerPort = v.findViewById<View>(R.id.savePeerServerPort) as ToggleButton

        //listeners
        saveCloudUrl.setOnClickListener(this)
        saveApiVersion.setOnClickListener(this)
        saveApiPrefix.setOnClickListener(this)
        saveApiSuffix.setOnClickListener(this)
        savePeerServer.setOnClickListener(this)
        savePeerServerPort.setOnClickListener(this)
        setUpViewMode()
        return v
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.saveCloudUrl -> {
                val cloudUrl = editCloudUrl.text.toString()
                if (cloudUrl.trim { it <= ' ' }.length > 0) {
                    if (cloudUrl.substring(0, 4).equals("http", ignoreCase = true) || cloudUrl.substring(0, 4).equals("https", ignoreCase = true)) {
                        session.saveCloudUrl(cloudUrl)
                        showMessage("Cloud URL Saved")
                    } else {
                        showMessage("Enter the correct URL starting with the scheme (http[s])")
                        return
                    }
                } else {
                    showMessage("Cloud URL is empty")
                    return
                }
            }
            R.id.saveApiVersion -> {
                val apiVersion = editApiVersion.text.toString()
                if (apiVersion.trim { it <= ' ' }.length > 0) {
                    session.saveApiVersion(apiVersion)
                    showMessage("Version saved successfully")
                } else {
                    showMessage("Unable to save empty Version")
                }
            }

            R.id.saveApiPrefix -> {
                val apiPrefix = editApiPrefix.text.toString()
                if (apiPrefix.trim { it <= ' ' }.length > 0) {
                    session.saveApiPrefix(apiPrefix)
                    showMessage("Prefix saved successfully")
                } else {
                    showMessage("Unable to save empty prefix")
                }
            }
            R.id.saveApiSuffix -> {
                val apiSuffix = editApiSuffix.text.toString()
                if (apiSuffix.trim { it <= ' ' }.length > 0) {
                    session.saveApiSuffix(apiSuffix)
                    showMessage("Suffix saved successfully")
                } else {
                    showMessage("Unable to save empty suffix")
                }
            }

            R.id.savePeerServer -> {
                val peerServer = editPeerServer.text.toString()
                if (peerServer.trim { it <= ' ' }.length > 0) {
                    if (peerServer.substring(0, 4).equals("http", ignoreCase = true) || peerServer.substring(0, 4).equals("https", ignoreCase = true)) {
                        session.savePeerServer(peerServer)
                        showMessage("Peer Server saved successfully")
                    } else {
                        showMessage("Enter the correct URL starting with the scheme (http[s])")
                        return
                    }
                } else {
                    showMessage("Unable to save empty peer server")
                }
            }

            R.id.savePeerServerPort -> {
                val peerServerPort = editPeerServerPort.text.toString()
                if (peerServerPort.trim { it <= ' ' }.length > 0) {
                    try {
                        val port = Integer.parseInt(peerServerPort)
                        session.savePeerServerPort(peerServerPort)
                        showMessage("Peer Server port saved successfully")
                    } catch (e: Exception) {
                        showMessage("Peer Server port must be an integer")
                    }

                } else {
                    showMessage("Unable to save empty peer server port")
                }
            }
        }
        //reload this fragment

        val fragment = SettingsFragment()
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out)
        fragmentTransaction.replace(R.id.frame, fragment, MainActivity.CURRENT_TAG)

        fragmentTransaction.commitAllowingStateLoss()
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

    fun setUpViewMode() {
        editCloudUrl.setText(session.cloudUrl)
        editApiVersion.setText(session.apiVersion)
        editApiPrefix.setText(session.apiPrefix)
        editApiSuffix.setText(session.apiSuffix)
        editPeerServer.setText(session.peerServer)
        editPeerServerPort.setText(session.peerServerPort)
    }

    fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        internal val DATE_DIALOG_ID = 100

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
