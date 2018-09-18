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
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.GpsTracker
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewLinkFacilityFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewLinkFacilityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewLinkFacilityFragment : Fragment(), OnClickListener, LocationListener {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private val parentLayout: LinearLayout? = null
    private var mListener: OnFragmentInteractionListener? = null
    internal var communityUnitsCreated: List<EditText> = ArrayList()
    internal var cuPartner: List<EditText> = ArrayList()

    internal var permissionsRequired = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val permissionStatus: SharedPreferences? = null
    private var sentToSettings = false

    internal  lateinit var buttonSave: Button
    internal  lateinit var buttonList: Button

    internal  lateinit var editFacilityName: EditText
    internal  lateinit var editActLevels: EditText
    internal  lateinit var editMrdtLevels: EditText
    internal  lateinit var editMflCode: EditText
    internal lateinit  var textLat: TextView
    internal lateinit  var textLon: TextView

    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0


    internal lateinit  var session: SessionManagement
    internal  lateinit var user: HashMap<String, String>
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()
    //flag for GPS Status
    internal var isGPSEnabled = false
    internal var hasLocation = false

    internal var editingLinkFacility: LinkFacility? = null

    //flag for net status
    internal var isNetworkEnabled = false

    internal var canGetLocation = false
    //private val id = 0

    internal var location: Location? = null //location

    protected var locationManager: LocationManager? = null


    //show alerts
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
        val v = inflater.inflate(R.layout.fragment_new_link_facility, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_LINK_FACILITY
        MainActivity.backFragment = LinkFacilitiesFragment()

        //get the current user
        session = SessionManagement(context!!)
        user = session.userDetails
        // check Permissions
        checkPermissions()

        editFacilityName = v.findViewById<View>(R.id.editFacilityName) as EditText
        editMrdtLevels = v.findViewById<View>(R.id.editMrdtLevels) as EditText
        editActLevels = v.findViewById<View>(R.id.editActLevels) as EditText
        editMflCode = v.findViewById<View>(R.id.editMflCode) as EditText
        textLat = v.findViewById<View>(R.id.textLat) as TextView
        textLon = v.findViewById<View>(R.id.textLon) as TextView

        textLat.text = "Capturing LAT"
        textLon.text = "Capturing LON"

        if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
            editActLevels.visibility = View.GONE
            editMrdtLevels.visibility = View.GONE
        }
        if (editingLinkFacility != null) {
            setUpEditing()
        }



        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)

        buttonSave = v.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener(this)

        return v
    }

    fun getLocation() {
        //get the location
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

            } else {
                this.canGetLocation = true
                // check for permissions
                if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //show explanation for permissions
                    val builder = AlertDialog.Builder(context!!)
                    builder.setTitle("Needs GPS Permission")
                    builder.setMessage("In order to work properly, this app needs GPS permission")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()
                        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_CALLBACK_CONSTANT)
                    }
                    builder.setNegativeButton("Dismiss") { dialog, which -> dialog.cancel() }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_CALLBACK_CONSTANT)
                }

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
                            textLat.text = "LAT: " + latitude.toString()
                            textLon.text = "LON: " + longitude.toString()
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
                            textLat.text = "LAT: " + latitude.toString()
                            textLon.text = "LON: " + longitude.toString()
                        }
                    }
                }

            }

        } catch (e: Exception) {
        }

    }

    fun checkPermissions() {
        try {
            if (ActivityCompat.checkSelfPermission(activity!!, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity!!, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionsRequired[1])) {
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionsRequired[1])) {
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
        getLocation()
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.editDob -> {
                val newFragment = DatePickerFragment.newInstance(R.id.editDob)
                newFragment.show(fragmentManager!!, "DatePicker")
            }


            R.id.buttonList -> {
                //when user clicks this button, we will show the list

                val linkFacilitiesFragment = LinkFacilitiesFragment()
                val fragmentManager = activity!!.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                fragmentTransaction.replace(R.id.frame, linkFacilitiesFragment, "linkfacility")
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.buttonSave -> {
                // set date as integers

                Toast.makeText(context, "Validating and saving", Toast.LENGTH_SHORT).show()
                val currentDate = Date().time
                val mapping = session.savedMapping
                val subCountyId: String
                val uuid: String
                if (editingLinkFacility != null) {
                    uuid = editingLinkFacility!!.id
                } else {
                    uuid = UUID.randomUUID().toString()
                }

                val county = mapping.county
                val subCounty: String
                if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
                    subCounty = mapping.subCounty
                    subCountyId = mapping.subCounty
                } else {
                    subCounty = session.savedSubCounty.id
                    subCountyId = session.savedSubCounty.id
                }

                val mappingId = mapping.id
                val addedBy = Integer.parseInt(user[SessionManagement.KEY_USERID])
                val country = user[SessionManagement.KEY_USER_COUNTRY]
                val facilityName = editFacilityName.text.toString()
                val mflCode = editMflCode.text.toString()
                var mrdtLevels: Long? = 0L
                if (editMrdtLevels.text.toString().trim { it <= ' ' } != "") {
                    mrdtLevels = java.lang.Long.valueOf(editMrdtLevels.text.toString())
                }

                var actLevels: Long? = 0L
                if (editActLevels.text.toString().trim { it <= ' ' } != "") {
                    actLevels = java.lang.Long.valueOf(editActLevels.text.toString())
                }

                // Do some validations
                if (facilityName.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Enter the name of the facility", Toast.LENGTH_SHORT).show()
                    return
                }

                if (location == null) {
                    Toast.makeText(context,
                            "The location has not been captured",
                            Toast.LENGTH_SHORT).show()
                }
                val parishId = session.savedParish.id
                val linkFacility = LinkFacility(uuid, facilityName, country!!, mappingId,
                        latitude, longitude, subCounty, currentDate, addedBy, actLevels!!, mrdtLevels!!, mflCode, county, parishId)

                val linkFacilityTable = LinkFacilityTable(context!!)
                val id = linkFacilityTable.addData(linkFacility)

                if (id.equals(-1)) {
                    Toast.makeText(context, "Could not save the link Facility", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                    editFacilityName.setText("")
                    editFacilityName.requestFocus()
                    editMrdtLevels.setText("")
                    editActLevels.setText("")
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

        //When we detach, lets stop GPS
        if (locationManager != null) {
            try {
                locationManager!!.removeUpdates(this)
            } catch (se: SecurityException) {
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

    //CountyLocation Methods
    override fun onLocationChanged(location: Location) {
        longitude = location.longitude
        latitude = location.latitude
        textLat.text = "LAT: " + latitude.toString()
        textLon.text = "LON: " + longitude.toString()

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    fun setUpEditing() {
        if (editingLinkFacility != null) {
            editFacilityName.setText(editingLinkFacility!!.facilityName)
            editMrdtLevels.setText(editingLinkFacility!!.getMrdtLevels().toString())
            editActLevels.setText(editingLinkFacility!!.getActLevels().toString())
            editMflCode.setText(editingLinkFacility!!.mflCode)
            textLat.text = editingLinkFacility!!.lat.toString()
            textLon.text = editingLinkFacility!!.lon.toString()
        }
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
        fun newInstance(param1: String, param2: String): NewLinkFacilityFragment {
            val fragment = NewLinkFacilityFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
