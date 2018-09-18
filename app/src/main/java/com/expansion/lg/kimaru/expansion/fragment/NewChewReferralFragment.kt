package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable

import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewChewReferralFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewChewReferralFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewChewReferralFragment : Fragment(), OnClickListener, LocationListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit var mName: EditText
    internal lateinit var mPhone: EditText
    internal lateinit var mTitle: EditText


    internal lateinit var buttonSave: Button
    internal lateinit var buttonList: Button
    var editingChewReferral: ChewReferral? = null

    var createdFromRecruitment: Boolean? = false


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    internal lateinit var session: SessionManagement
    internal lateinit var user: HashMap<String, String>
    internal lateinit var country: String

    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()
    internal var isGPSEnabled = false
    //flag for net status
    internal var isNetworkEnabled = false

    internal var canGetLocation = false
    //private val id = 0

    internal var location: Location? = null //location
    internal var permissionsRequired = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var sentToSettings = false
    private val permissionStatus: SharedPreferences? = null

    protected var locationManager: LocationManager? = null

    internal var alert = AlertDialogManager()

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
        val v = inflater.inflate(R.layout.fragment_new_chewreferral, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_RECRUITMENT
        if (createdFromRecruitment!!) {
            MainActivity.backFragment = RecruitmentViewFragment()
        } else {
            MainActivity.backFragment = ReferralsFragment()
        }

        session = SessionManagement(context!!)
        user = session.userDetails
        country = user[SessionManagement.KEY_USER_COUNTRY]!!
        mName = v.findViewById<View>(R.id.editName) as EditText
        mTitle = v.findViewById<View>(R.id.editReferralTitle) as EditText
        mPhone = v.findViewById<View>(R.id.editReferralPhoneNumber) as EditText

        if (country.equals("ke", ignoreCase = true)) {
            mTitle.visibility = View.GONE
        }

        setUpEditingMode()

        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)

        buttonSave = v.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener(this)

        // Location

        // check if we have permissions.
        // if no, request permisions
        // if yes or granted, check if GPS enabled.
        // if No, request to enable
        // when enabled,
        // get location

        // PERMISSIONS
        checkIfLocationIsEnabled()
        try {
            if (ActivityCompat.checkSelfPermission(context!!, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionsRequired[0])) {
                    //Show Information about why you need the permission
                    val builder = AlertDialog.Builder(context!!)
                    builder.setTitle("Multiple Permissions Request")
                    builder.setMessage("This app needs Location permissions")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()
                        ActivityCompat.requestPermissions(activity!!, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
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
                    ActivityCompat.requestPermissions(activity!!, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                val editor = permissionStatus!!.edit()
                editor.putBoolean(permissionsRequired[0], true)
                editor.commit()
            } else {
                proceedAfterPermission()
            }
        } catch (e: Exception) {
        }

        checkIfLocationIsEnabled()

        return v
    }

    fun checkIfLocationIsEnabled() {
        try {
            locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            //is GPS Enabled or not?
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            // Is network enabled
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            //if both are off, we cannot get the GPS
            if (!isGPSEnabled && !isNetworkEnabled) {
                //ask user to enable location
                val alertDialog = AlertDialog.Builder(context!!)
                // Setting Dialog Title
                alertDialog.setTitle("GPS settings")

                // Setting Dialog Message
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")

                // On pressing Settings button
                alertDialog.setPositiveButton("Settings") { dialog, which ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context!!.startActivity(intent)
                }

                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

                // Showing Alert Message
                alertDialog.show()

            }
        } catch (e: Exception) {
        }

    }

    fun getLocation() {
        this.canGetLocation = true
        // check for permissions
        if (ActivityCompat.checkSelfPermission(context!!, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionsRequired[0])) {
                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Multiple Permissions Request")
                builder.setMessage("This app needs Location permissions")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(activity!!, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
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
                ActivityCompat.requestPermissions(activity!!, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }
            val editor = permissionStatus!!.edit()
            editor.putBoolean(permissionsRequired[0], true)
            editor.commit()
        } else {
            //we have the permissions now
            // Get location from Network provider
            if (isNetworkEnabled) {
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATE,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                if (locationManager != null) {
                    location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null) {
                        latitude = location!!.latitude
                        longitude = location!!.longitude
                    }
                }
            }
            // we try getting the location form GPS
            if (isGPSEnabled) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATE,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                if (locationManager != null) {
                    location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location != null) {
                        latitude = location!!.latitude
                        longitude = location!!.longitude
                    }
                }
            }
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionsRequired[0])) {
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Location permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(activity!!, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                Toast.makeText(context, "Unable to get necessary Permissions. The app may be unreliable.", Toast.LENGTH_LONG).show()
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
        getLocation()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonList -> {
                val fragment = RecruitmentViewFragment()
                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.buttonSave -> {
                // set date as integers
                val currentDate = Date().time

                // Generate the uuid
                val id: String
                if (editingChewReferral != null) {
                    id = editingChewReferral!!.id
                } else {
                    id = UUID.randomUUID().toString()
                }
                val referralName = mName.text.toString()
                val referralPhone = mPhone.text.toString()
                val referralTitle = mTitle.text.toString()
                var recruitment = ""
                var county = ""
                var parish = ""
                var subcounty = ""
                var district = ""
                var mapping = ""

                if (referralPhone.trim { it <= ' ' } != "") {
                    if (referralPhone.trim { it <= ' ' }.startsWith("+")) {
                        if (referralPhone.length != 13) {
                            Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                            mPhone.requestFocus()
                            return
                        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(referralPhone)) {
                            mPhone.requestFocus()
                            Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                            return
                        }
                    } else if (referralPhone.length != 10) {
                        mPhone.requestFocus()
                        Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                        return
                    } else if (!PhoneNumberUtils.isGlobalPhoneNumber(referralPhone)) {
                        Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                        return
                    }
                }

                var mobilization = ""
                if (createdFromRecruitment!!) {
                    recruitment = session.savedRecruitment.id
                    county = session.savedRecruitment.county
                    subcounty = session.savedRecruitment.subcounty
                } else {
                    mobilization = session.savedMobilization.id
                    county = session.savedMapping.county
                    subcounty = session.savedMapping.subCounty
                    district = session.savedMapping.district
                    mapping = session.savedMapping.id
                    parish = session.savedParish.id
                }

                // Do some validations
                if (referralName.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Name cannot be blank", Toast.LENGTH_SHORT).show()
                    mName.requestFocus()
                    return
                }
                Toast.makeText(context, "Validating and saving", Toast.LENGTH_SHORT).show()

                /**
                 * String id, String name, String phone, String title, String country,
                 * String recruitmentId, Integer synced, String county, String district,
                 * String subCounty, String communityUnit, String village, String mapping,
                 * String lat, String lon, String mobilization
                 */

                // Save Recruitment
                val chewReferral = ChewReferral(id, referralName, referralPhone,
                        referralTitle, country, recruitment, 0, county, district, subcounty, "", "",
                        mapping, latitude.toString(), longitude.toString(), mobilization)
                val chewReferralTable = ChewReferralTable(context!!)
                val statusId = chewReferralTable.addChewReferral(chewReferral)

                if (statusId.equals(-1)) {
                    Toast.makeText(context, "Could not save recruitment", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                    // Clear boxes
                    mName.setText("")
                    mPhone.setText("")
                    mTitle.setText("")
                    //set Focus
                    mName.requestFocus()
                }
            }
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        //        if (context instanceof OnFragmentInteractionListener) {
        //            mListener = (OnFragmentInteractionListener) context;
        //        } else {
        //            throw new RuntimeException(context.toString()
        //                    + " must implement OnFragmentInteractionListener");
        //        }
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

    fun setUpEditingMode() {
        if (editingChewReferral != null) {
            mName.setText(editingChewReferral!!.name)
            mTitle.setText(editingChewReferral!!.title)
            mPhone.setText(editingChewReferral!!.phone)
            mName.requestFocus()
        }
    }

    override fun onLocationChanged(location: Location) {
        longitude = location.longitude
        latitude = location.latitude
    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        internal val DATE_DIALOG_ID = 100
        // Due to Runtime Perms, let us declare some constants for the permissions
        private val PERMISSION_CALLBACK_CONSTANT = 101
        private val REQUEST_PERMISSION_SETTING = 102


        // The minimum distance to change Updates in meters
        val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 1 // 10 meters
        // The minimum time between updates in milliseconds
        val MIN_TIME_BETWEEN_UPDATE = (1000 * 60 / 2).toLong() // 30 seconds

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): NewChewReferralFragment {
            val fragment = NewChewReferralFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
