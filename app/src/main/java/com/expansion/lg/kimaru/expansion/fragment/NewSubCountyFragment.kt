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
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.GpsTracker
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewSubCountyFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewSubCountyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewSubCountyFragment : Fragment(), OnClickListener, LocationListener {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit var editName: EditText
    internal lateinit var editContactPerson: EditText
    internal lateinit var editPhone: EditText
    internal lateinit var editMainTown: EditText
    internal lateinit var editMainTownPopulation: EditText
    internal lateinit var editSubCountyPopulation: EditText
    internal lateinit var editNoOfVillages: EditText
    internal lateinit var editServicePopulation: EditText
    internal lateinit var editMainTownGPS: EditText
    internal lateinit var editTransportCost: EditText
    internal lateinit var editMajorRoads: EditText
    internal lateinit var editPrivateClinics: EditText
    internal lateinit var editPrivateClinicsInRadius: EditText
    internal var editCommunityUnits: EditText? = null
    internal lateinit var editMainSuperMarkets: EditText
    internal lateinit var editMainBanks: EditText
    internal lateinit var editMajorBusiness: EditText
    internal lateinit var editComment: EditText

    internal lateinit var editCountySupport: RadioGroup
    internal lateinit var editSubCountySupport: RadioGroup
    internal var editChvActivity: RadioGroup? = null
    internal lateinit var editRecommended: RadioGroup

    internal lateinit var editPopulationDensity: Spinner

    internal lateinit var buttonSave: Button
    internal lateinit var buttonList: Button
    internal lateinit var canUseCurrentPosition: CheckBox

    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    var subCountyEditing: SubCounty? = null


    internal lateinit var session: SessionManagement
    internal lateinit var user: HashMap<String, String?>
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

    internal lateinit var mapping: Mapping


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
        val v = inflater.inflate(R.layout.fragment_new_sub_county, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_SUB_COUNTY
        MainActivity.backFragment = SubCountiesFragment()
        //get the current user
        session = SessionManagement(context!!)
        user = session.userDetails
        mapping = session.savedMapping
        canUseCurrentPosition = v.findViewById<View>(R.id.chkGps) as CheckBox

        editMainTownGPS = v.findViewById<View>(R.id.editMainTownGPS) as EditText

        checkIfLocationIsEnabled()
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
                            editMainTownGPS.setText("Network Lat " + latitude.toString() + " Lon " + longitude.toString())
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
                            editMainTownGPS.setText("GPS Lat " + latitude.toString() + " Lon " + longitude.toString())
                        }
                    }
                }

            }

        } catch (e: Exception) {
        }


        //Initialize the UI Components
        editName = v.findViewById<View>(R.id.editName) as EditText
        editContactPerson = v.findViewById<View>(R.id.editContactPerson) as EditText
        editPhone = v.findViewById<View>(R.id.editPhone) as EditText
        editMainTown = v.findViewById<View>(R.id.editMainTown) as EditText
        editMainTownPopulation = v.findViewById<View>(R.id.editMainTownPopulation) as EditText
        // editCountyPopulation = (EditText) v.findViewById(R.id.editCountyPopulation);
        editSubCountyPopulation = v.findViewById<View>(R.id.editSubCountyPopulation) as EditText
        editNoOfVillages = v.findViewById<View>(R.id.editNoOfVillages) as EditText
        editServicePopulation = v.findViewById<View>(R.id.editServicePopulation) as EditText
        editTransportCost = v.findViewById<View>(R.id.editTransportCost) as EditText
        editMajorRoads = v.findViewById<View>(R.id.editMajorRoads) as EditText
        editPrivateClinics = v.findViewById<View>(R.id.editPrivateClinics) as EditText
        editPrivateClinicsInRadius = v.findViewById<View>(R.id.editPrivateClinicsInRadius) as EditText
        //editCommunityUnits = (EditText) v.findViewById(R.id.editCommunityUnits);
        editMainSuperMarkets = v.findViewById<View>(R.id.editMainSuperMarkets) as EditText
        editMainBanks = v.findViewById<View>(R.id.editMainBanks) as EditText
        editMajorBusiness = v.findViewById<View>(R.id.editMajorBusiness) as EditText
        editComment = v.findViewById<View>(R.id.editComment) as EditText

        //radioButtonGrps
        editCountySupport = v.findViewById<View>(R.id.editCountySupport) as RadioGroup
        editSubCountySupport = v.findViewById<View>(R.id.editSubCountySupport) as RadioGroup
        //editChvActivity = (RadioGroup) v.findViewById(R.id.editChvActivity);
        editRecommended = v.findViewById<View>(R.id.editRecommended) as RadioGroup

        //spinners
        editPopulationDensity = v.findViewById<View>(R.id.editPopulationDensity) as Spinner

        //at this point, let us setup Editing Mode
        setUpEditingMode()

        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)

        buttonSave = v.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener(this)

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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.editDob -> {
                val newFragment = DatePickerFragment.newInstance(R.id.editDob)
                newFragment.show(fragmentManager!!, "DatePicker")
            }

            R.id.editRelocated -> {
                val dateMovedFragment = DatePickerFragment.newInstance(R.id.editRelocated)
                dateMovedFragment.show(fragmentManager!!, "Datepicker")
            }
            R.id.buttonList -> {
                //when user clicks this button, we will show the list

                val subCountiesFragment = SubCountiesFragment()
                val fragmentManager = activity!!.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)

                fragmentTransaction.replace(R.id.frame, subCountiesFragment, "subcounties")
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.buttonSave -> {
                // set date as integers
                val dateFormat = SimpleDateFormat("yyyy/MM/dd")

                Toast.makeText(context, "Validating and saving", Toast.LENGTH_SHORT).show()
                val currentDate = Date().time

                val mapping = session.savedMapping

                val uuid: String
                if (subCountyEditing != null) {
                    uuid = subCountyEditing!!.id
                } else {
                    uuid = UUID.randomUUID().toString()
                }
                val county = mapping.county
                val mappingID = mapping.id
                val lat = latitude.toString()
                val lon = longitude.toString()
                val addedBy = Integer.parseInt(user[SessionManagement.KEY_USERID])

                val editDataName = editName.text.toString()
                val editDataContactPerson = editContactPerson.text.toString()
                val editDataPhone = editPhone.text.toString()
                val editDataMainTown = editMainTown.text.toString()
                val editDataMainTownPopulation = editMainTownPopulation.text.toString()
                val editDataCountyPopulation = "0" // editCountyPopulation.getText().toString();
                val editDataSubCountyPopulation = editSubCountyPopulation.text.toString()
                val editDataNoOfVillages = editNoOfVillages.text.toString()
                val editDataServicePopulation = editServicePopulation.text.toString()
                val editDataTransportCost = editTransportCost.text.toString()
                val editDataMajorRoads = editMajorRoads.text.toString()
                val editDataPrivateClinics = editPrivateClinics.text.toString()
                val editDataPrivateClinicsInRadius = editPrivateClinicsInRadius.text.toString()
                val editDataCommunityUnits = "" //editCommunityUnits.getText().toString();
                val editDataMainSuperMarkets = editMainSuperMarkets.text.toString()
                val editDataMainBanks = editMainBanks.text.toString()
                val editDataMajorBusiness = editMajorBusiness.text.toString()
                val editDataComment = editComment.text.toString()

                var countySupport: String
                try {
                    val countySupportRate = editCountySupport.checkedRadioButtonId
                    val cSupportRate = editCountySupport.findViewById<View>(countySupportRate) as RadioButton
                    countySupport = cSupportRate.text.toString()
                } catch (e: Exception) {
                    countySupport = "0"
                }


                //subcounty support
                var subCountySupport: String
                try {
                    val subCountySupportRate = editSubCountySupport.checkedRadioButtonId
                    val subCountySRate = editSubCountySupport.findViewById<View>(subCountySupportRate) as RadioButton
                    subCountySupport = subCountySRate.text.toString()
                } catch (e: Exception) {
                    subCountySupport = "0"
                }

                var isRecommended: Boolean
                try {
                    //Recommended
                    val subCountyRecommended = editCountySupport.checkedRadioButtonId
                    val recommend = editCountySupport.findViewById<View>(subCountyRecommended) as RadioButton
                    isRecommended = recommend.text.toString() === "yes"
                } catch (e: Exception) {
                    isRecommended = false
                }


                //chvActivity
                // Integer intChvActivity = editChvActivity.getCheckedRadioButtonId();
                // RadioButton selectedChvActivity =(RadioButton) editChvActivity.findViewById(intChvActivity);
                val chvActivity = "0" //selectedChvActivity.getText().toString();
                //editPopulationDensity
                val populationDensity = editPopulationDensity.selectedItemId.toString()
                val healtFacilities = ""

                // Do some validations
                if (editDataName.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Enter the name of Subcounty", Toast.LENGTH_SHORT).show()
                } else {
                    // Save Details

                    val subCounty = SubCounty()
                    val useCurrentGps = canUseCurrentPosition.isChecked
                    if (useCurrentGps) {
                        subCounty.lat = lat
                        subCounty.lon = lon
                    }

                    subCounty.subCountyName = editDataName
                    subCounty.id = uuid
                    subCounty.countyID = county
                    subCounty.country = mapping.country
                    subCounty.mappingId = mappingID
                    subCounty.contactPerson = editDataContactPerson
                    subCounty.contactPersonPhone = editDataPhone
                    subCounty.mainTown = editDataMainTown
                    subCounty.countySupport = countySupport
                    subCounty.subcountySupport = subCountySupport
                    subCounty.chvActivityLevel = chvActivity
                    subCounty.countyPopulation = editDataCountyPopulation
                    subCounty.subCountyPopulation = editDataSubCountyPopulation
                    subCounty.noOfVillages = editDataNoOfVillages
                    subCounty.mainTownPopulation = editDataMainTownPopulation
                    subCounty.servicePopulation = editDataServicePopulation
                    subCounty.populationDensity = populationDensity
                    subCounty.transportCost = editDataTransportCost
                    subCounty.majorRoads = editDataMajorRoads
                    subCounty.healtFacilities = healtFacilities
                    subCounty.privateClinicsInRadius = editDataPrivateClinicsInRadius
                    subCounty.privateClinicsInTown = editDataPrivateClinics
                    subCounty.communityUnits = editDataCommunityUnits
                    subCounty.mainSupermarkets = editDataMainSuperMarkets
                    subCounty.mainBanks = editDataMainBanks
                    subCounty.anyMajorBusiness = editDataMajorBusiness
                    subCounty.comments = editDataComment
                    subCounty.isRecommended = isRecommended
                    subCounty.dateAdded = currentDate
                    subCounty.addedBy = addedBy

                    val subCountyTable = SubCountyTable(context!!)
                    val id: Long
                    if (subCountyEditing == null) {
                        id = subCountyTable.addData(subCounty)
                    } else {
                        id = subCountyTable.editData(subCounty)
                    }

                    if (id.equals(-1)) {
                        Toast.makeText(context, "Could not save the results", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Operation successful", Toast.LENGTH_SHORT).show()
                        //clear the subcounty
                        subCountyEditing = null

                        // Clear boxes
                        editName.setText("")
                        editContactPerson.setText("")
                        editPhone.setText("")
                        editMainTown.setText("")
                        editMainTownPopulation.setText("")
                        //                        editCountyPopulation.setText("");
                        editSubCountyPopulation.setText("")
                        editNoOfVillages.setText("")
                        editServicePopulation.setText("")
                        editTransportCost.setText("")
                        editMajorRoads.setText("")
                        editPrivateClinics.setText("")
                        editPrivateClinicsInRadius.setText("")
                        //                        editCommunityUnits.setText("");
                        editMainSuperMarkets.setText("")
                        editMainBanks.setText("")
                        editMajorBusiness.setText("")
                        editComment.setText("")
                        editName.requestFocus()
                    }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.title = "New Sub County for " + mapping.county
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

    //Location Methods
    override fun onLocationChanged(location: Location) {
        longitude = location.longitude
        latitude = location.latitude
        editMainTownGPS.setText("Current Lat " + latitude.toString() + " Lon " + longitude.toString())

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    fun setUpEditingMode() {
        if (subCountyEditing != null) {
            editName.setText(subCountyEditing!!.subCountyName)
            editContactPerson.setText(subCountyEditing!!.contactPerson)
            editPhone.setText(subCountyEditing!!.contactPersonPhone)
            editMainTown.setText(subCountyEditing!!.mainTown)
            editMainTownPopulation.setText(subCountyEditing!!.mainTownPopulation)
            editSubCountyPopulation.setText(subCountyEditing!!.subCountyPopulation)
            editNoOfVillages.setText(subCountyEditing!!.noOfVillages)
            editServicePopulation.setText(subCountyEditing!!.servicePopulation)
            editTransportCost.setText(subCountyEditing!!.transportCost)
            editMajorRoads.setText(subCountyEditing!!.majorRoads)
            editPrivateClinics.setText(subCountyEditing!!.privateClinicsInTown)
            editPrivateClinicsInRadius.setText(subCountyEditing!!.privateClinicsInRadius)
            editMainSuperMarkets.setText(subCountyEditing!!.mainSupermarkets)
            editMainBanks.setText(subCountyEditing!!.mainBanks)
            editMajorBusiness.setText(subCountyEditing!!.anyMajorBusiness)
            editComment.setText(subCountyEditing!!.comments)
            //clear all radios

            if (!subCountyEditing!!.countySupport.equals("", ignoreCase = true)) {
                editSubCountySupport.clearCheck()
                editSubCountySupport.check(Integer.valueOf(subCountyEditing!!.countySupport))
            }

            editRecommended.clearCheck()
            editRecommended.check(Integer.valueOf(if (subCountyEditing!!.isRecommended) 1 else 0)!!)
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
        fun newInstance(param1: String, param2: String): NewSubCountyFragment {
            val fragment = NewSubCountyFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
