package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.Manifest
import android.content.Context
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
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.mzigos.Village
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable
import com.expansion.lg.kimaru.expansion.tables.VillageTable
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewVillageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewVillageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewVillageFragment : Fragment(), OnClickListener, LocationListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit var editName: EditText
    internal lateinit var editAreaChiefName: EditText
    internal lateinit var editAreaChiefPhone: EditText
    internal lateinit var editComment: EditText
    internal lateinit var editDistanceToBranch: EditText
    internal lateinit var editTransportCost: EditText
    internal lateinit var editDistanceToMainRoad: EditText
    internal lateinit var editNumberOfHouseHolds: EditText
    internal lateinit var editPopulationDensity: EditText
    internal lateinit var editDistributors: EditText
    internal lateinit var editDistanceToHealthFacility: EditText
    internal lateinit var textLat: TextView
    internal lateinit var textLon: TextView
    internal lateinit var editEconomicStatus: Spinner
    internal lateinit var spinnerLinkFacility: Spinner
    internal lateinit var editPresenceEstates: RadioGroup
    internal lateinit var editPresenceOfFactories: RadioGroup
    internal lateinit var editPresenceOfTraderMarket: RadioGroup
    internal lateinit var editPresenceOfSuperMarket: RadioGroup
    internal lateinit var editNgosGivingFreeDrugs: RadioGroup
    internal lateinit var editBrac: RadioGroup
    internal lateinit var editNgosIccms: RadioGroup
    internal lateinit var mtn: RadioGroup
    internal lateinit var airtel: RadioGroup
    internal lateinit var orange: RadioGroup
    internal lateinit var editNgosMhealth: RadioGroup
    internal lateinit var editPresenceDistributors: RadioGroup
    internal lateinit var actStock: RadioGroup
    internal lateinit var editActCost: RadioGroup
    internal lateinit var economicStatusList: List<String>
    internal var linkFacilityList: MutableList<LinkFacility> = ArrayList()
    internal var linkFacilities: MutableList<String> = ArrayList()

    //location
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()
    internal var isGPSEnabled = false
    internal var isNetworkEnabled = false
    internal var canGetLocation = false
    internal var location: Location? = null
    protected var locationManager: LocationManager? = null


    internal lateinit var buttonSave: Button
    internal lateinit var buttonList: Button
    internal var permissionsRequired = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val permissionStatus: SharedPreferences? = null
    private var sentToSettings = false

    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0


    internal lateinit  var session: SessionManagement
    internal var editingVillage: Village? = null
    internal lateinit  var user: HashMap<String, String>
    internal var linkFacility: LinkFacility? = null

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
        val v = inflater.inflate(R.layout.fragment_new_village, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_VILLAGE
        MainActivity.backFragment = VillagesFragment()
        session = SessionManagement(context!!)
        user = session.userDetails

        //Ui Elements
        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)

        buttonSave = v.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener(this)

        editName = v.findViewById<View>(R.id.editName) as EditText
        editAreaChiefName = v.findViewById<View>(R.id.editAreaChiefName) as EditText
        editAreaChiefPhone = v.findViewById<View>(R.id.editAreaChiefPhone) as EditText
        editComment = v.findViewById<View>(R.id.editComment) as EditText
        editDistanceToBranch = v.findViewById<View>(R.id.editDistanceToBranch) as EditText
        editTransportCost = v.findViewById<View>(R.id.editTransportCost) as EditText
        editDistanceToMainRoad = v.findViewById<View>(R.id.editDistanceToMainRoad) as EditText
        editNumberOfHouseHolds = v.findViewById<View>(R.id.editNumberOfHouseHolds) as EditText
        editPopulationDensity = v.findViewById<View>(R.id.editPopulationDensity) as EditText
        textLat = v.findViewById<View>(R.id.textLat) as TextView
        textLon = v.findViewById<View>(R.id.textLon) as TextView
        latitude = 0.0
        longitude = 0.0
        textLat.text = "Capturing LAT"
        textLon.text = "Capturing LON"


        /**
         * Hide population density
         * @requested by trainingTeam-Ug
         * @date Jan 23rd, 2018
         */
        if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
            editPopulationDensity.visibility = View.GONE
        }
        editDistributors = v.findViewById<View>(R.id.editDistributors) as EditText
        editDistanceToHealthFacility = v.findViewById<View>(R.id.editDistanceToHealthFacility) as EditText
        //        editActLevels = (EditText) v.findViewById(R.id.editActLevels);
        editActCost = v.findViewById<View>(R.id.editActCost) as RadioGroup
        editPresenceEstates = v.findViewById<View>(R.id.editPresenceEstates) as RadioGroup
        editPresenceOfFactories = v.findViewById<View>(R.id.editPresenceOfFactories) as RadioGroup
        editPresenceOfTraderMarket = v.findViewById<View>(R.id.editPresenceOfTraderMarket) as RadioGroup
        editPresenceOfSuperMarket = v.findViewById<View>(R.id.editPresenceOfSuperMarket) as RadioGroup
        editNgosGivingFreeDrugs = v.findViewById<View>(R.id.editNgosGivingFreeDrugs) as RadioGroup
        editPresenceDistributors = v.findViewById<View>(R.id.editPresenceDistributors) as RadioGroup
        actStock = v.findViewById<View>(R.id.editActStock) as RadioGroup
        editBrac = v.findViewById<View>(R.id.editBrac) as RadioGroup
        editNgosIccms = v.findViewById<View>(R.id.editNgosIccms) as RadioGroup
        editNgosMhealth = v.findViewById<View>(R.id.editNgosMhealth) as RadioGroup
        mtn = v.findViewById<View>(R.id.mtn) as RadioGroup
        airtel = v.findViewById<View>(R.id.airtel) as RadioGroup
        orange = v.findViewById<View>(R.id.orange) as RadioGroup
        editEconomicStatus = v.findViewById<View>(R.id.editEconomicStatus) as Spinner
        spinnerLinkFacility = v.findViewById<View>(R.id.editLinkFacilityId) as Spinner
        economicStatusList = Arrays.asList(*resources.getStringArray(R.array.economic_status))

        // populate the link Facilities;
        linkFacilityList.clear()
        linkFacilities.clear()
        addLinkFacilities()
        val linkFacilityAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, linkFacilities)
        linkFacilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLinkFacility.adapter = linkFacilityAdapter
        //lets set the selected
        if (linkFacility != null) {
            var x = 0
            for (e in linkFacilityList) {
                if (e.id.equals(linkFacility!!.id, ignoreCase = true)) {
                    spinnerLinkFacility.setSelection(x, true)
                    break
                }
                x++
            }
        }

        //in case we are editing
        setupEditingMode()

        checkPermissions()




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

            R.id.editRelocated -> {
                val dateMovedFragment = DatePickerFragment.newInstance(R.id.editRelocated)
                dateMovedFragment.show(fragmentManager!!, "Datepicker")
            }
            R.id.buttonSave -> {
                val id: String
                if (editingVillage == null) {
                    id = UUID.randomUUID().toString()
                } else {
                    id = editingVillage!!.id
                }

                if (editAreaChiefName.text.toString().equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Area chief name cannot be empty", Toast.LENGTH_LONG).show()
                    editAreaChiefName.requestFocus()
                    return
                }
                if (editAreaChiefPhone.text.toString().equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Phone of the chief cannot be empty", Toast.LENGTH_LONG).show()
                    editAreaChiefPhone.requestFocus()
                    return
                }
                val distanceToBranch = editDistanceToBranch.text.toString()
                if (distanceToBranch.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Distance to branch is required", Toast.LENGTH_LONG).show()
                    editDistanceToBranch.requestFocus()
                    return
                }
                val transportCost = editTransportCost.text.toString()
                if (transportCost.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Transport cost to branch is required", Toast.LENGTH_LONG).show()
                    editTransportCost.requestFocus()
                    return
                }
                val distanceToMainRoad = editDistanceToMainRoad.text.toString()
                if (distanceToMainRoad.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Distance to the main road is required", Toast.LENGTH_LONG).show()
                    editDistanceToMainRoad.requestFocus()
                    return
                }
                val numberOfhouseHolds = editNumberOfHouseHolds.text.toString()
                if (numberOfhouseHolds.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Please enter the number of households", Toast.LENGTH_LONG).show()
                    editNumberOfHouseHolds.requestFocus()
                    return
                }
                val populationDensity = "0"
                //                String populationDensity = editPopulationDensity.getText().toString();
                //                if (populationDensity.equalsIgnoreCase("")){
                //                    Toast.makeText(getContext(), "Population density is required", Toast.LENGTH_LONG).show();
                //                    editPopulationDensity.requestFocus();
                //                    return;
                //                }
                val distanceToHealthFacility = editDistanceToHealthFacility.text.toString()
                if (distanceToHealthFacility.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Enter the distance to the nearest health facility",
                            Toast.LENGTH_LONG).show()
                    editDistanceToHealthFacility.requestFocus()
                    return
                }
                //                String actLevels = editActLevels.getText().toString();
                //                if (editActLevels.getText().toString().equalsIgnoreCase("")){
                //                    Toast.makeText(getContext(), "Enter the ACT levels of the nearest public" +
                //                            " health facility", Toast.LENGTH_LONG).show();
                //                    editActLevels.requestFocus();
                //                    return;
                //                }

                val villageName = editName.text.toString()
                if (villageName.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Village Name is required", Toast.LENGTH_SHORT).show()
                    editName.requestFocus()
                    return
                }
                val mappingId = session.savedMapping.id
                val lat = latitude
                val lon = longitude
                val country = session.userDetails[SessionManagement.KEY_USER_COUNTRY]
                val district = session.savedMapping.district
                val county = session.savedMapping.county
                val subCountyId = session.savedMapping.subCounty
                val communityUnit = "" //UG does not have the community units, but provide for one.
                var linkFacilityId = "" //UG does not have the link facilities, but provide for one.
                val lFacility = spinnerLinkFacility.selectedItemPosition
                if (linkFacilityList.size != 0 && lFacility != -1) {
                    linkFacilityId = linkFacilityList[spinnerLinkFacility.selectedItemPosition].id
                } else {
                    linkFacilityId = ""
                }

                val areaChiefName = editAreaChiefName.text.toString()
                val areaChiefPhone = editAreaChiefPhone.text.toString()
                val distributorsInTheArea = editDistributors.text.toString()
                val ward = "" //No Wards in UG
                val parish = session.savedParish.id
                val economicStatus = economicStatusList[editEconomicStatus.selectedItemPosition]  //List<String> economicStatusList
                val actStockPresent = getSelectedRadioItemValue(actStock).equals("Yes", ignoreCase = true)
                val privateFacilityForAct = ""
                val privateFacilityForMrdt = ""
                val nameOfNgoDoingIccm = ""
                val nameOfNgoDoingMhealth = ""
                val dateAdded = Date().time
                val bracOperating = getSelectedRadioItemValue(editBrac).equals("Yes", ignoreCase = true)
                val addedBy = Integer.valueOf(session.userDetails[SessionManagement.KEY_USERID])
                val numberOfChvs = 0L
                val householdPerChv = 0L
                val branch = java.lang.Long.valueOf(if (distanceToBranch.equals("", ignoreCase = true))
                    "0"
                else
                    distanceToBranch)
                var transportCostToBranch = 0L
                try {
                    transportCostToBranch = java.lang.Long.valueOf(if (transportCost.equals("", ignoreCase = true)) "0" else transportCost)
                } catch (e: Exception) {
                }

                var mainRoad = 0L
                try {
                    mainRoad = java.lang.Long.valueOf(if (distanceToMainRoad
                                    .equals("", ignoreCase = true))
                        "0"
                    else
                        distanceToMainRoad)
                } catch (e: Exception) {
                }

                var noOfHouseholds = 0L
                try {
                    noOfHouseholds = java.lang.Long.valueOf(if (editNumberOfHouseHolds.text.toString()
                                    .equals("", ignoreCase = true))
                        "0"
                    else
                        editNumberOfHouseHolds.text.toString())
                } catch (e: Exception) {
                }

                val mohPoplationDensity = 0L

                val comment = editComment.text.toString()
                var estimatedPopulationDensity = 0L
                try {
                    estimatedPopulationDensity = java.lang.Long.valueOf(if (editPopulationDensity.text.toString()
                                    .equals("", ignoreCase = true))
                        "0"
                    else
                        editPopulationDensity.text.toString())
                } catch (e: Exception) {
                }

                var distanceToNearestHealthFacility = 0L
                try {
                    distanceToNearestHealthFacility = java.lang.Long.valueOf(if (distanceToHealthFacility
                                    .equals("", ignoreCase = true))
                        "0"
                    else
                        distanceToHealthFacility)
                } catch (e: Exception) {
                }

                val actLevel = 0L

                var actPrice = 0L
                try {
                    actPrice = if (getSelectedRadioItemValue(editActCost).equals("Less than 5000", ignoreCase = true)) 1000L else 6000L
                } catch (e: Exception) {
                }


                val mtnSignal = Integer.valueOf(getSelectedRadioItemValue(mtn))
                val orangeSignal = Integer.valueOf(getSelectedRadioItemValue(orange))
                val airtelSignal = Integer.valueOf(getSelectedRadioItemValue(airtel))
                val safaricomSignal = 0
                val mrdtLevels = 0L
                val mrdtPrice = 0L
                val synced = false
                val presenceOfDistributors = getSelectedRadioItemValue(editPresenceDistributors).equals("Yes", ignoreCase = true)
                val chvsTrained = false
                val presenceOfHostels = false
                val presenceOfEstates = getSelectedRadioItemValue(editPresenceEstates).equals("Yes", ignoreCase = true)
                val ngoDoingIccm = getSelectedRadioItemValue(editNgosIccms).equals("Yes", ignoreCase = true)
                val ngoDoingMhealth = getSelectedRadioItemValue(editNgosMhealth).equals("Yes", ignoreCase = true)
                val numberOfFactories = Integer.valueOf(
                        if (getSelectedRadioItemValue(editPresenceOfFactories).equals("0", ignoreCase = true))
                            "0"
                        else if (getSelectedRadioItemValue(editPresenceOfFactories).equals("Less than 2", ignoreCase = true)) "2" else "3")
                val traderMarket = getSelectedRadioItemValue(editPresenceOfTraderMarket).equals("Yes", ignoreCase = true)
                val largeSupermarket = getSelectedRadioItemValue(editPresenceOfSuperMarket).equals("Yes", ignoreCase = true)
                val ngosGivingFreeDrugs = getSelectedRadioItemValue(editNgosGivingFreeDrugs).equals("Yes", ignoreCase = true)


                val village = Village(id, villageName, mappingId, lat, lon, country!!,
                        district, county, subCountyId, parish, communityUnit, ward, linkFacilityId,
                        areaChiefName, areaChiefPhone, branch, transportCostToBranch, mainRoad,
                        noOfHouseholds, mohPoplationDensity, estimatedPopulationDensity,
                        economicStatus, distanceToNearestHealthFacility, actLevel, actPrice,
                        mrdtLevels, mrdtPrice, presenceOfHostels, presenceOfEstates,
                        numberOfFactories, presenceOfDistributors, distributorsInTheArea, traderMarket, largeSupermarket,
                        ngosGivingFreeDrugs, ngoDoingIccm, ngoDoingMhealth, nameOfNgoDoingIccm,
                        nameOfNgoDoingMhealth, privateFacilityForAct, privateFacilityForMrdt,
                        dateAdded, addedBy, comment, chvsTrained, synced, bracOperating, mtnSignal,
                        safaricomSignal, orangeSignal, airtelSignal, actStockPresent)
                val villageTable = VillageTable(context!!)

                val saved = villageTable.addData(village)
                if (saved.equals(-1)) {
                    Toast.makeText(context, "Error occured when saving the record", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Record saved successfully", Toast.LENGTH_SHORT).show()
                    editName.setText("")
                    editAreaChiefName.setText("")
                    editAreaChiefPhone.setText("")
                    editComment.setText("")
                    editDistanceToBranch.setText("")
                    editTransportCost.setText("")
                    editDistanceToMainRoad.setText("")
                    editNumberOfHouseHolds.setText("")
                    editPopulationDensity.setText("")
                    editDistributors.setText("")
                    editDistanceToHealthFacility.setText("")
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

    fun setupEditingMode() {
        if (editingVillage != null) {

            editName.setText(editingVillage!!.villageName)
            editName.requestFocus()
            editAreaChiefName.setText(editingVillage!!.areaChiefName)
            editAreaChiefPhone.setText(editingVillage!!.areaChiefPhone)
            editComment.setText(editingVillage!!.comment)
            editDistanceToBranch.setText(editingVillage!!.distanceToBranch!!.toString())
            editTransportCost.setText(editingVillage!!.transportCost!!.toString())
            editDistanceToMainRoad.setText(editingVillage!!.distanceToMainRoad!!.toString())
            editNumberOfHouseHolds.setText(editingVillage!!.noOfHouseholds!!.toString())
            editPopulationDensity.setText(editingVillage!!.estimatedPopulationDensity!!.toString())
            editDistributors.setText(editingVillage!!.distributorsInTheArea)
            editDistanceToHealthFacility.setText(editingVillage!!.distanceToNearestHealthFacility!!.toString())
            //            editActLevels.setText(editingVillage.getActLevels().toString());
            editPresenceEstates.clearCheck()
            editPresenceEstates.check(if (editingVillage!!.presenceOfEstates!!) R.id.estate1 else R.id.estate2)
            editPresenceOfFactories.clearCheck()
            editPresenceOfFactories.check(if (editingVillage!!.numberOfFactories == 0)
                R.id.cFactory1
            else if (editingVillage!!.numberOfFactories == 2) R.id.cFactory2 else R.id.cFactory3)

            editPresenceOfTraderMarket.clearCheck()
            editPresenceOfTraderMarket.check(if (editingVillage!!.traderMarket!!) R.id.marketYes else R.id.marketNo)

            editPresenceOfSuperMarket.clearCheck()
            editPresenceOfSuperMarket.check(if (editingVillage!!.largeSupermarket!!) R.id.superMarketYes else R.id.SuperMarketNo)

            editNgosGivingFreeDrugs.clearCheck()
            editNgosGivingFreeDrugs.check(if (editingVillage!!.ngosGivingFreeDrugs!!) R.id.drugsYes else R.id.drugsNo)

            editPresenceDistributors.clearCheck()
            editPresenceDistributors.check(if (editingVillage!!.presenceOfDistributors!!) R.id.distributorsYes else R.id.distributorsNo)
            editBrac.clearCheck()
            editBrac.check(if (editingVillage!!.isBracOperating) R.id.bracYes else R.id.bracNo)

            editNgosIccms.clearCheck()
            editNgosIccms.check(if (editingVillage!!.ngoDoingIccm!!) R.id.iccmYes else R.id.iccmNo)

            editNgosMhealth.clearCheck()
            editNgosMhealth.check(if (editingVillage!!.ngoDoingMhealth!!) R.id.mHealthYes else R.id.mHealthNo)

            actStock.clearCheck()
            if (editingVillage!!.actStock!!) {
                actStock.check(R.id.actStockYes)
            } else {
                actStock.check(R.id.actStockNo)
            }

            editActCost.clearCheck()
            if (editingVillage!!.actPrice!!.compareTo(5000L) > 0) {
                editActCost.check(R.id.actPriceMore)
            } else {
                editActCost.check(R.id.actPriceLess)
            }

            mtn.clearCheck()
            when (editingVillage!!.mtnSignalStrength) {
                0 -> mtn.check(R.id.mtn0)
                1 -> mtn.check(R.id.mtn1)
                2 -> mtn.check(R.id.mtn2)
                3 -> mtn.check(R.id.mtn3)
                4 -> mtn.check(R.id.mtn4)
                else -> mtn.check(R.id.mtn0)
            }

            airtel.clearCheck()
            when (editingVillage!!.airtelSignalStrength) {
                0 -> airtel.check(R.id.airtel0)
                1 -> airtel.check(R.id.airtel1)
                2 -> airtel.check(R.id.airtel2)
                3 -> airtel.check(R.id.airtel3)
                4 -> airtel.check(R.id.airtel4)
                else -> airtel.check(R.id.airtel0)
            }

            orange.clearCheck()
            when (editingVillage!!.orangeSignalStrength) {
                0 -> orange.check(R.id.orange0)
                1 -> orange.check(R.id.orange1)
                2 -> orange.check(R.id.orange2)
                3 -> orange.check(R.id.orange3)
                4 -> orange.check(R.id.orange4)
                else -> orange.check(R.id.orange0)
            }
            var x = 0
            if (editingVillage!!.economicStatus != null) {
                for (economic in economicStatusList) {
                    if (editingVillage!!.economicStatus.equals(economic, ignoreCase = true)) {
                        editEconomicStatus.setSelection(x)
                        break
                    }
                    x++
                }
            }

            //Health Facility
            x = 0
            for (e in linkFacilityList) {
                if (e.id.equals(editingVillage!!.linkFacilityId, ignoreCase = true)) {
                    spinnerLinkFacility.setSelection(x, true)
                    break
                }
                x++
            }
        }
    }

    //Location Methods
    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            latitude = location.latitude
            longitude = location.longitude
            textLat.text = "LAT: " + latitude.toString()
            textLon.text = "LON: " + longitude.toString()
        }
    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    fun getSelectedRadioItemValue(radioGroup: RadioGroup): String {
        val selectedButton = radioGroup.checkedRadioButtonId
        val selectedRadioButton = radioGroup.findViewById<View>(selectedButton) as RadioButton
        return selectedRadioButton.text.toString()
    }

    fun addLinkFacilities() {
        val linkFacilityTable = LinkFacilityTable(context!!)
        Log.d("TREMAP", "Testing")
        session.savedSubCounty

        linkFacilityList = linkFacilityTable.getLinkFacilityBySubCounty(session.savedMapping.subCounty) as MutableList<LinkFacility>
        for (lFacility in linkFacilityList) {
            linkFacilities.add(lFacility.facilityName)
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        // The minimum distance to change Updates in meters
        val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 1 // 10 meters
        // The minimum time between updates in milliseconds
        val MIN_TIME_BETWEEN_UPDATE = (1000 * 60 / 2).toLong() // 30 seconds
        private val PERMISSION_CALLBACK_CONSTANT = 101
        private val REQUEST_PERMISSION_SETTING = 102
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
        fun newInstance(param1: String, param2: String): NewVillageFragment {
            val fragment = NewVillageFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
