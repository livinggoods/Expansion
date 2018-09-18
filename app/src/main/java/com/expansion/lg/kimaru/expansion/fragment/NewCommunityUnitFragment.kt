package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewCommunityUnitFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewCommunityUnitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewCommunityUnitFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    internal var backFragment: Fragment? = null
    internal var editingCommunityUnit: CommunityUnit? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit  var editName: EditText
    internal  lateinit var editAreaChiefName: EditText
    internal lateinit  var editAreaChiefPhone: EditText
    internal lateinit  var editWard: EditText
    internal  lateinit var editComment: EditText
    internal  lateinit var editPrivateFacilityForAct: EditText
    internal  lateinit var editPrivateFacilityForMrdt: EditText
    internal  lateinit var editNumberOfChvs: EditText
    internal lateinit  var editChvHouseHold: EditText
    internal lateinit  var editNumberOfHouseHolds: EditText
    internal lateinit  var editMohPopulation: EditText
    internal  lateinit var editPopulationDensity: EditText
    internal lateinit  var editNumberOfVillages: EditText
    internal  lateinit var editDistanceToBranch: EditText
    internal  lateinit var editTransportCost: EditText
    internal lateinit  var editDistanceToMainRoad: EditText
    internal  lateinit var editDistanceToHealthFacility: EditText
    internal lateinit  var editDistributors: EditText
    internal var editCHVsTrained: EditText? = null
    internal  lateinit var editPriceofAct: EditText
    internal  lateinit var editPriceOfMrdt: EditText
    internal  lateinit var editChiefPopulation: EditText
    internal lateinit  var editChiefChvHouseHold: EditText

    internal lateinit  var editEconomicStatus: Spinner
    internal lateinit  var spinnerLinkFacility: Spinner

    internal lateinit  var editPresenceOfFactories: RadioGroup
    internal  lateinit var editPresenceEstates: RadioGroup
    internal  lateinit var editPresenceOfTraderMarket: RadioGroup
    internal lateinit  var editPresenceOfSuperMarket: RadioGroup
    internal  lateinit var editNgosGivingFreeDrugs: RadioGroup
    internal lateinit  var editCHVsTrainedGroup: RadioGroup
    internal var linkFacility: LinkFacility? = null

    internal  lateinit var buttonSave: Button
    internal  lateinit var buttonList: Button


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    internal lateinit  var session: SessionManagement
    internal var mapping: Mapping? = null
    internal  lateinit var subCounty: SubCounty
    internal var name: String? = null
    internal var email: String? = null
    internal lateinit  var user: HashMap<String, String>


    internal var latitude: Double? = 0.0
    internal var longitude: Double? = 0.0

    internal var linkFacilityList: MutableList<LinkFacility> = ArrayList()
    internal var linkFacilities: MutableList<String> = ArrayList()

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
        val v = inflater.inflate(R.layout.fragment_new_community_unit, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_COMMUNITY_UNIT
        if (backFragment == null) {
            MainActivity.backFragment = CommunityUnitsFragment()
        } else {
            MainActivity.backFragment = backFragment
        }

        session = SessionManagement(context!!)
        mapping = session.savedMapping
        subCounty = session.savedSubCounty
        user = session.userDetails

        //Initialize the UI Components

        editName = v.findViewById<View>(R.id.editName) as EditText
        editAreaChiefName = v.findViewById<View>(R.id.editAreaChiefName) as EditText
        editAreaChiefPhone = v.findViewById<View>(R.id.editAreaChiefPhone) as EditText
        editWard = v.findViewById<View>(R.id.editWard) as EditText
        editEconomicStatus = v.findViewById<View>(R.id.editEconomicStatus) as Spinner

        editPrivateFacilityForAct = v.findViewById<View>(R.id.editPrivateFacilityForAct) as EditText
        editPrivateFacilityForMrdt = v.findViewById<View>(R.id.editPrivateFacilityForMrdt) as EditText
        editNumberOfChvs = v.findViewById<View>(R.id.editNumberOfChvs) as EditText
        editChvHouseHold = v.findViewById<View>(R.id.editChvHouseHold) as EditText
        editPriceofAct = v.findViewById<View>(R.id.editPriceOfMrdt) as EditText
        editPriceOfMrdt = v.findViewById<View>(R.id.editPriceofAct) as EditText

        editChiefPopulation = v.findViewById<View>(R.id.editChiefPopulation) as EditText
        editChiefChvHouseHold = v.findViewById<View>(R.id.editChiefChvHouseHold) as EditText


        editNumberOfHouseHolds = v.findViewById<View>(R.id.editNumberOfHouseHolds) as EditText
        editMohPopulation = v.findViewById<View>(R.id.editMohPopulation) as EditText
        editPopulationDensity = v.findViewById<View>(R.id.editPopulationDensity) as EditText
        editNumberOfVillages = v.findViewById<View>(R.id.editNumberOfVillages) as EditText

        editDistanceToBranch = v.findViewById<View>(R.id.editDistanceToBranch) as EditText
        editTransportCost = v.findViewById<View>(R.id.editTransportCost) as EditText
        editDistanceToMainRoad = v.findViewById<View>(R.id.editDistanceToMainRoad) as EditText
        editDistanceToHealthFacility = v.findViewById<View>(R.id.editDistanceToHealthFacility) as EditText
        editComment = v.findViewById<View>(R.id.editComment) as EditText

        spinnerLinkFacility = v.findViewById<View>(R.id.spinnerLinkFacility) as Spinner
        editDistributors = v.findViewById<View>(R.id.editDistributors) as EditText
        // editCHVsTrained = (EditText) v.findViewById(R.id.editCHVsTrained);
        editCHVsTrainedGroup = v.findViewById<View>(R.id.editCHVsTrainedGroup) as RadioGroup


        editPresenceOfFactories = v.findViewById<View>(R.id.editPresenceOfFactories) as RadioGroup
        editPresenceEstates = v.findViewById<View>(R.id.editPresenceEstates) as RadioGroup
        editPresenceOfTraderMarket = v.findViewById<View>(R.id.editPresenceOfTraderMarket) as RadioGroup
        editPresenceOfSuperMarket = v.findViewById<View>(R.id.editPresenceOfSuperMarket) as RadioGroup
        editNgosGivingFreeDrugs = v.findViewById<View>(R.id.editNgosGivingFreeDrugs) as RadioGroup

        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)

        buttonSave = v.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener(this)

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
            // hide the spinner
            val linkFacilityView = v.findViewById<View>(R.id.linkFacility) as TextInputLayout
            linkFacilityView.visibility = View.INVISIBLE
            // Toast.makeText(getContext(), "ID "+linkFacility.getId(), Toast.LENGTH_SHORT).show();
        }
        setUpEditingMode()

        return v
    }

    fun addLinkFacilities() {
        val linkFacilityTable = LinkFacilityTable(context!!)

        linkFacilityList = linkFacilityTable.getLinkFacilityBySubCounty(subCounty.id) as MutableList<LinkFacility>
        for (lFacility in linkFacilityList) {
            linkFacilities.add(lFacility.facilityName)
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

            R.id.buttonList -> {
                val communityUnitsFragment = CommunityUnitsFragment()
                communityUnitsFragment.linkFacility = linkFacility
                var fragment: Fragment = communityUnitsFragment
                val fragmentManager = activity!!.supportFragmentManager
                var fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties")
                fragmentTransaction.commitAllowingStateLoss()
            }

            R.id.editRelocated -> {
                val dateMovedFragment = DatePickerFragment.newInstance(R.id.editRelocated)
                dateMovedFragment.show(fragmentManager!!, "Datepicker")
            }
            R.id.buttonSave -> {
                val currentDate = Date().time
                val communityUnitTable = CommunityUnitTable(context!!)
                // check if the field for Chiefs population exists or not.
                if (!communityUnitTable.isFieldExist(CommunityUnitTable.CHVS_HOUSEHOLDS_AS_PER_CHIEF)) {
                    communityUnitTable.addChiefsFields()
                }
                if (!communityUnitTable.isFieldExist(CommunityUnitTable.COMMENT)) {
                    communityUnitTable.addChiefsFields()
                }


                //                "Private facility \n" +
                //                        " • (Capture)- Name of Private  Facility\n" +
                //                        "                  - Price of ACT at private Facility\n" +
                //                        " • MRDT- same as above\n" +
                //                        "\n" +
                //                        " • No of Households per CHVs as per- Ast Chief\n" +
                //                        " •  Population as per Ast Chief\n" +
                //                        "In the Chief's \n" +
                //                        " \n" +
                //                        "Link Partners with the CU-\n" +
                //                        "Make it possible to capture partners ina specific CU\n" +
                //                        "Comments box- alpha -numeric\n" +
                //                        "Partner save function is not working\n" +
                //                        "List section does not capture partners instead it lists all CUs"
                val name = editName.text.toString()
                val areaChiefName = editAreaChiefName.text.toString()
                val areaChiefPhone = editAreaChiefPhone.text.toString()
                val ward = editWard.text.toString()
                val economicStatus = editEconomicStatus.selectedItemPosition.toString()
                val comments = editComment.text.toString()

                val privateFacilityForAct = editPrivateFacilityForAct.text.toString()
                val privateFacilityForMrdt = editPrivateFacilityForMrdt.text.toString()

                val numberOfChvs = java.lang.Long.valueOf(if (editNumberOfChvs.text.toString().trim { it <= ' ' }
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editNumberOfChvs.text.toString())

                val chvHouseHold = java.lang.Long.valueOf(if (editChvHouseHold.text.toString().trim { it <= ' ' }
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editChvHouseHold.text.toString())

                val numberOfHouseHolds = java.lang.Long.valueOf(if (editNumberOfHouseHolds.text.toString().trim { it <= ' ' }
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editNumberOfHouseHolds.text.toString())

                val mohPopulation = java.lang.Long.valueOf(if (editMohPopulation.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editMohPopulation.text.toString())

                val populationDensity = java.lang.Long.valueOf(if (editPopulationDensity.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editPopulationDensity.text.toString())
                val numberOfVillages = java.lang.Long.valueOf(if (editNumberOfVillages.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editNumberOfVillages.text.toString())

                val distanceToBranch = java.lang.Long.valueOf(if (editDistanceToBranch.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editDistanceToBranch.text.toString())

                val transportCost = java.lang.Long.valueOf(if (editTransportCost.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editTransportCost.text.toString())

                val actPrice = java.lang.Long.valueOf(if (editPriceofAct.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editPriceofAct.text.toString())

                val mrdtPrice = java.lang.Long.valueOf(if (editPriceOfMrdt.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editPriceOfMrdt.text.toString())

                val distanceToMainRoad = java.lang.Long.valueOf(if (editDistanceToMainRoad.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editDistanceToMainRoad.text.toString())
                val distanceToHealthFacility = java.lang.Long.valueOf(if (editDistanceToHealthFacility.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editDistanceToHealthFacility.text.toString())
                val linkFacilityId: String
                if (linkFacility != null) {
                    linkFacilityId = linkFacility!!.id
                } else {
                    val lFacility = spinnerLinkFacility.selectedItemPosition
                    if (linkFacilityList.size != 0 && lFacility != -1) {
                        linkFacilityId = linkFacilityList[spinnerLinkFacility.selectedItemPosition].id
                    } else {
                        linkFacilityId = ""
                    }
                }

                val chiefPopulation = java.lang.Long.valueOf(if (editChiefPopulation.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editChiefPopulation.text.toString())
                val chiefChvsHousehold = java.lang.Long.valueOf(if (editChiefChvHouseHold.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editChiefChvHouseHold.text.toString())


                val distributors = java.lang.Long.valueOf(if (editDistributors.text.toString()
                                .equals("", ignoreCase = true))
                    "0"
                else
                    editDistributors.text.toString())

                // boolean cHVsTrained = (editCHVsTrained.getText().toString() == "Yes");

                val chvsTrainedInICCm = editCHVsTrainedGroup.checkedRadioButtonId
                val selectedIccmOption = editCHVsTrainedGroup.findViewById<View>(chvsTrainedInICCm) as RadioButton
                val cHVsTrained = selectedIccmOption.text.toString().equals("Yes", ignoreCase = true)


                val factoriesPresent = editPresenceOfFactories.checkedRadioButtonId
                val selectedFactoryOption = editPresenceOfFactories.findViewById<View>(factoriesPresent) as RadioButton
                var presenceOfFactories: Long? = 0L
                if (selectedFactoryOption.text.toString().equals("More than 2", ignoreCase = true)) {
                    presenceOfFactories = 3L
                } else if (selectedFactoryOption.text.toString().equals("More than 2", ignoreCase = true)) {
                    presenceOfFactories = 2L
                } else {
                    presenceOfFactories = 0L
                }
                //boolean presenceOfFactories = (selectedFactoryOption.getText().toString().equalsIgnoreCase("Yes"));

                val estatesPresent = editPresenceEstates.checkedRadioButtonId
                val selectedEstateOption = editPresenceEstates.findViewById<View>(estatesPresent) as RadioButton
                val presenceEstates = selectedEstateOption.text.toString().equals("Yes", ignoreCase = true)

                val tradeMarketsPresent = editPresenceOfTraderMarket.checkedRadioButtonId
                val selectedTradeMarketOption = editPresenceOfTraderMarket.findViewById<View>(tradeMarketsPresent) as RadioButton
                val presenceOfTraderMarket = selectedTradeMarketOption.text.toString().equals("Yes", ignoreCase = true)


                val superMarketPresent = editPresenceOfSuperMarket.checkedRadioButtonId
                val selectedSuperMarketOption = editPresenceOfSuperMarket.findViewById<View>(superMarketPresent) as RadioButton
                val presenceOfSuperMarket = selectedSuperMarketOption.text.toString().equals("Yes", ignoreCase = true)


                val ngosDrugs = editNgosGivingFreeDrugs.checkedRadioButtonId
                val ngosGivingFreeDrugsSelected = editNgosGivingFreeDrugs.findViewById<View>(ngosDrugs) as RadioButton
                val ngosGivingFreeDrugs = ngosGivingFreeDrugsSelected.text.toString().equals("Yes", ignoreCase = true)

                // Do some validations

                if (name.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Enter name of the Community Unit", Toast.LENGTH_SHORT).show()
                    editName.requestFocus()
                } else if (areaChiefName.trim { it <= ' ' } == "" && mapping != null && backFragment == null) {
                    Toast.makeText(context, "Enter the name of the Chief", Toast.LENGTH_SHORT).show()
                    editAreaChiefName.requestFocus()
                } else if (areaChiefPhone.trim { it <= ' ' } == "" && mapping != null && backFragment == null) {
                    Toast.makeText(context, "Enter the phone contact details of the chief", Toast.LENGTH_SHORT).show()
                    editAreaChiefPhone.requestFocus()
                } else {
                    Toast.makeText(context, "Validating and saving", Toast.LENGTH_SHORT).show()
                    var id = UUID.randomUUID().toString()
                    if (editingCommunityUnit != null) {
                        id = editingCommunityUnit!!.id
                    }
                    val userId = java.lang.Long.valueOf(user[SessionManagement.KEY_USERID])
                    val country = user[SessionManagement.KEY_USER_COUNTRY]
                    var mappingId = ""
                    if (mapping != null) {
                        mappingId = mapping!!.id
                    }

                    val communityUnit = CommunityUnit()

                    communityUnit.id = id
                    communityUnit.communityUnitName = name
                    communityUnit.mappingId = mappingId
                    communityUnit.lat = latitude
                    communityUnit.lon = longitude
                    communityUnit.country = country!!
                    communityUnit.subCountyId = subCounty.id
                    communityUnit.linkFacilityId = linkFacilityId
                    communityUnit.areaChiefName = areaChiefName
                    communityUnit.areaChiefPhone = areaChiefPhone
                    communityUnit.ward = ward
                    communityUnit.economicStatus = economicStatus
                    communityUnit.privateFacilityForAct = privateFacilityForAct
                    communityUnit.privateFacilityForMrdt = privateFacilityForMrdt
                    communityUnit.nameOfNgoDoingIccm = ""
                    communityUnit.nameOfNgoDoingMhealth = ""
                    communityUnit.dateAdded = currentDate
                    communityUnit.addedBy = userId
                    communityUnit.numberOfChvs = numberOfChvs
                    communityUnit.householdPerChv = chvHouseHold
                    communityUnit.numberOfVillages = numberOfVillages
                    communityUnit.distanceToBranch = distanceToBranch
                    communityUnit.transportCost = transportCost
                    communityUnit.distanceTOMainRoad = distanceToMainRoad
                    communityUnit.noOfHouseholds = numberOfHouseHolds
                    communityUnit.mohPoplationDensity = mohPopulation
                    communityUnit.estimatedPopulationDensity = populationDensity
                    communityUnit.distanceTONearestHealthFacility = distanceToHealthFacility
                    communityUnit.actLevels = 0
                    communityUnit.actPrice = actPrice
                    communityUnit.mrdtLevels = 0
                    communityUnit.mrdtPrice = mrdtPrice
                    communityUnit.noOfDistibutors = distributors
                    communityUnit.isChvsTrained = cHVsTrained
                    communityUnit.isPresenceOfEstates = presenceEstates
                    communityUnit.setPresenceOfFactories(presenceOfFactories)
                    communityUnit.isPresenceOfHostels = presenceEstates
                    communityUnit.isTraderMarket = presenceOfTraderMarket
                    communityUnit.isLargeSupermarket = presenceOfSuperMarket
                    communityUnit.isNgosGivingFreeDrugs = ngosGivingFreeDrugs
                    communityUnit.isNgoDoingIccm = false
                    communityUnit.isNgoDoingMhealth = false
                    communityUnit.populationAsPerChief = chiefPopulation
                    communityUnit.chvsHouseholdsAsPerChief = chiefChvsHousehold
                    communityUnit.comment = comments


                    val cid = communityUnitTable.addCommunityUnitData(communityUnit)
                    if (!cid.equals(-1)) {
                        Toast.makeText(context, "Community Unit saved successfully", Toast.LENGTH_SHORT).show()
                        editName.setText("")
                        editName.requestFocus()
                        editAreaChiefName.setText("")
                        editAreaChiefPhone.setText("")
                        editWard.setText("")

                        editPrivateFacilityForAct.setText("")
                        editPrivateFacilityForMrdt.setText("")
                        editNumberOfChvs.setText("")
                        editChvHouseHold.setText("")

                        editNumberOfHouseHolds.setText("")
                        editMohPopulation.setText("")
                        editPopulationDensity.setText("")
                        editNumberOfVillages.setText("")

                        editDistanceToBranch.setText("")
                        editTransportCost.setText("")
                        editDistanceToMainRoad.setText("")
                        editDistanceToHealthFacility.setText("")

                        editDistributors.setText("")
                        editPriceOfMrdt.setText("")
                        editPriceofAct.setText("")

                        editChiefPopulation.setText("")
                        editChiefChvHouseHold.setText("")
                        editComment.setText("")

                    }
                    if (backFragment != null) {
                        val recruitmentViewFragment = RecruitmentViewFragment()
                        val fragment = recruitmentViewFragment
                        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                        fragmentTransaction.commitAllowingStateLoss()
                    }
                }
            }
        }
    }

    fun setUpEditingMode() {
        if (editingCommunityUnit != null) {
            editName.setText(editingCommunityUnit!!.communityUnitName)
            editName.requestFocus()
            editAreaChiefName.setText(editingCommunityUnit!!.areaChiefName)
            editAreaChiefPhone.setText(editingCommunityUnit!!.areaChiefPhone)
            editWard.setText(editingCommunityUnit!!.ward)
            editPrivateFacilityForAct.setText(editingCommunityUnit!!.privateFacilityForAct)
            editPrivateFacilityForMrdt.setText(editingCommunityUnit!!.privateFacilityForMrdt)
            editNumberOfChvs.setText(editingCommunityUnit!!.numberOfChvs.toString())
            editChvHouseHold.setText(editingCommunityUnit!!.householdPerChv.toString())
            editNumberOfHouseHolds.setText(editingCommunityUnit!!.noOfHouseholds.toString())
            editMohPopulation.setText(editingCommunityUnit!!.mohPoplationDensity.toString())
            editPopulationDensity.setText(editingCommunityUnit!!.estimatedPopulationDensity.toString())
            editNumberOfVillages.setText(editingCommunityUnit!!.numberOfVillages.toString())
            editDistanceToBranch.setText(editingCommunityUnit!!.distanceToBranch.toString())
            editTransportCost.setText(editingCommunityUnit!!.transportCost.toString())
            editDistanceToMainRoad.setText(editingCommunityUnit!!.distanceTOMainRoad.toString())
            editDistanceToHealthFacility.setText(editingCommunityUnit!!.distanceTONearestHealthFacility.toString())
            editDistributors.setText(editingCommunityUnit!!.noOfDistibutors.toString())
            editPriceofAct.setText(editingCommunityUnit!!.actPrice.toString())
            editPriceOfMrdt.setText(editingCommunityUnit!!.mrdtPrice.toString())
            editChiefPopulation.setText(editingCommunityUnit!!.populationAsPerChief.toString())
            editChiefChvHouseHold.setText(editingCommunityUnit!!.chvsHouseholdsAsPerChief.toString())
            editComment.setText(editingCommunityUnit!!.comment)


            // set radio Buttons
            //mReadEnglish.check(editingRegistration.getReadEnglish().equals(1) ? R.id.radioCanReadEnglish : R.id.radioCannotReadEnglish);
            editCHVsTrainedGroup.clearCheck()
            editCHVsTrainedGroup.check(if (editingCommunityUnit!!.isChvsTrained) R.id.trainedYes else R.id.trainedNo)

            editPresenceOfFactories.clearCheck()
            if (editingCommunityUnit!!.presenceOfFactories() == 0L) {
                editPresenceOfFactories.check(R.id.cFactory1)
            } else if (editingCommunityUnit!!.presenceOfFactories() == 2L) {
                editPresenceOfFactories.check(R.id.cFactory2)
            } else {
                editPresenceOfFactories.check(R.id.cFactory3)
            }

            editPresenceEstates.clearCheck()
            editPresenceEstates.check(if (editingCommunityUnit!!.isPresenceOfEstates) R.id.estate1 else R.id.estate2)

            editPresenceOfTraderMarket.clearCheck()
            editPresenceOfTraderMarket.check(if (editingCommunityUnit!!.isTraderMarket) R.id.marketYes else R.id.marketNo)


            editPresenceOfSuperMarket.clearCheck()
            editPresenceOfSuperMarket.check(if (editingCommunityUnit!!.isLargeSupermarket) R.id.superMarketYes else R.id.SuperMarketNo)

            editNgosGivingFreeDrugs.clearCheck()
            editNgosGivingFreeDrugs.check(if (editingCommunityUnit!!.isNgosGivingFreeDrugs) R.id.drugsYes else R.id.drugsNo)
            try {
                editEconomicStatus.setSelection(Integer.valueOf(editingCommunityUnit!!.economicStatus))
            } catch (e: Exception) {
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
        fun newInstance(param1: String, param2: String): NewCommunityUnitFragment {
            val fragment = NewCommunityUnitFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
