package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.telephony.PhoneNumberUtils
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.Education
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.mzigos.Ward
import com.expansion.lg.kimaru.expansion.other.DisplayDate
import com.expansion.lg.kimaru.expansion.other.SpinnersCursorAdapter
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.EducationTable
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable
import com.expansion.lg.kimaru.expansion.tables.WardTable

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewKeRegistrationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewKeRegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewKeRegistrationFragment : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal var textshow: TextView? = null
    internal  lateinit var mName: EditText
    internal  lateinit var mPhone: EditText
    internal  lateinit var mGender: RadioGroup
    internal  var gender: RadioButton? = null
    internal  lateinit var editWard: Spinner
    internal  lateinit var mVillage: EditText
    internal  lateinit var mMark: EditText
    internal  lateinit var mLangs: EditText
    internal  lateinit var mOccupation: EditText
    internal lateinit  var mComment: EditText
    internal lateinit  var mDob: EditText
    internal lateinit  var mAge: EditText
    internal  lateinit var mReadEnglish: RadioGroup
    internal var mRecruitment: EditText? = null
    internal  lateinit var mDateMoved: EditText
    internal lateinit  var editBranchTransportCost: EditText
    internal  lateinit var editRecruitmentTransportCost: EditText
    internal  lateinit var editIsChv: RadioGroup
    internal lateinit  var editIsGokTrained: RadioGroup
    internal  lateinit var mCommunity: RadioGroup
    internal  lateinit var mAccounts: RadioGroup
    internal  lateinit var chooseAgeFormat: RadioGroup
    internal lateinit  var editCuName: Spinner
    internal lateinit  var selectMaritalStatus: Spinner
    internal  lateinit var selectLinkFacility: Spinner
    internal var editLinkFacility: EditText? = null
    internal  lateinit var editNoOfHouseholds: EditText
    internal  lateinit var editOtherTrainings: EditText
    internal  lateinit var educationLevel: Spinner
    internal  lateinit var selectChew: Spinner

    internal  lateinit var buttonSave: Button
    internal lateinit  var buttonList: Button

    private var referralTitle = ""
    private var referralName = ""
    private var referralPhone = ""

    private var wardName = ""


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0


    internal lateinit  var session: SessionManagement
    internal lateinit  var userName: String
    internal lateinit  var userEmail: String
    internal  lateinit var recruitmentId: String
    internal lateinit  var linkName: String
    internal lateinit  var user: HashMap<String, String>
    internal var userId: Int? = null
    internal var editingRegistration: Registration? = null
    internal var cu: CommunityUnit? = null

    internal var chewReferralList: MutableList<ChewReferral> = ArrayList()
    internal var chewReferrals: MutableList<String> = ArrayList()

    internal var communityUnitList: MutableList<CommunityUnit> = ArrayList()
    internal var communityUnits: MutableList<String> = ArrayList()

    internal var linkFacilityList: MutableList<LinkFacility> = ArrayList()
    internal var linkFacilities: MutableList<String> = ArrayList()

    internal var wardsList: MutableList<Ward> = ArrayList()
    internal var wards: MutableList<String> = ArrayList()

    internal var educationList: MutableList<Education> = ArrayList()


    internal var onSelectedLinkFacilityListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val selectedItem = linkFacilities[position]
            if (selectedItem.equals("Add New", ignoreCase = true)) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("New Link Facility")
                // add Facility Name, the rest can be edited later
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL
                val facilityName = EditText(context)
                facilityName.hint = "Link Facility Name"
                facilityName.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                layout.addView(facilityName)

                val facilityMflCode = EditText(context)
                facilityMflCode.hint = "MFL Code"
                facilityMflCode.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                layout.addView(facilityMflCode)
                builder.setView(layout)

                builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    linkName = facilityName.text.toString()
                    if (linkName.equals("", ignoreCase = true)) {
                        Toast.makeText(context, "Facility Name is required", Toast.LENGTH_LONG).show()
                        facilityName.requestFocus()
                        return@OnClickListener
                    }
                    // save the Link Facility
                    val uuid = UUID.randomUUID().toString()
                    val lknFacility = LinkFacility()
                    lknFacility.id = uuid
                    lknFacility.facilityName = linkName
                    lknFacility.subCountyId = session.savedRecruitment.subcounty
                    lknFacility.mflCode = facilityMflCode.text.toString()
                    lknFacility.mappingId = ""
                    lknFacility.dateAdded = Date().time
                    lknFacility.addedBy = Integer.valueOf(session.userDetails[SessionManagement.KEY_USERID])
                    lknFacility.setMrdtLevels(0L)
                    lknFacility.setActLevels(0L)
                    lknFacility.country = session.userDetails[SessionManagement.KEY_USER_COUNTRY]!!

                    val linkFacilityTable = LinkFacilityTable(context!!)
                    linkFacilityTable.addData(lknFacility)

                    // clear chews
                    linkFacilityList.clear()
                    linkFacilities.clear()
                    addLinkFacilities()
                    val linkFacilityAdapter = ArrayAdapter(context!!,
                            android.R.layout.simple_spinner_item, linkFacilities)
                    linkFacilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    selectLinkFacility.adapter = linkFacilityAdapter

                    //lets set the selected
                    var x = 0
                    for (e in linkFacilityList) {
                        if (e.id.equals(uuid, ignoreCase = true)) {
                            selectLinkFacility.setSelection(x, true)
                            break
                        }
                        x++
                    }
                })
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }

    internal var onSelectedCuListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val selectedItem = communityUnits[position]
            if (selectedItem.equals("Add New", ignoreCase = true)) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("New Community Unit")
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL
                val cuName = EditText(context)
                cuName.hint = "Cu Name"
                cuName.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                layout.addView(cuName)
                builder.setView(layout)
                builder.setPositiveButton("OK") { dialog, which ->
                    val communityUnitName = cuName.text.toString()
                    // save the CU
                    val uuid = UUID.randomUUID().toString()
                    val communityUnit = CommunityUnit()
                    communityUnit.id = uuid
                    communityUnit.communityUnitName = communityUnitName
                    communityUnit.country = session.savedRecruitment.country
                    communityUnit.subCountyId = session.savedRecruitment.subcounty
                    communityUnit.dateAdded = Date().time
                    communityUnit.addedBy = Integer.valueOf(session.userDetails[SessionManagement.KEY_USERID]).toLong()



                    communityUnit.mappingId = ""
                    communityUnit.lat = 0.0
                    communityUnit.lon = 0.0
                    communityUnit.linkFacilityId = ""
                    communityUnit.areaChiefName = ""
                    communityUnit.areaChiefPhone = ""
                    communityUnit.ward = ""
                    communityUnit.economicStatus = ""
                    communityUnit.privateFacilityForAct = ""
                    communityUnit.privateFacilityForMrdt = ""
                    communityUnit.nameOfNgoDoingIccm = ""
                    communityUnit.nameOfNgoDoingMhealth = ""
                    communityUnit.numberOfChvs = 0
                    communityUnit.householdPerChv = 0
                    communityUnit.numberOfVillages = 0
                    communityUnit.distanceToBranch = 0
                    communityUnit.transportCost = 0
                    communityUnit.distanceTOMainRoad = 0
                    communityUnit.noOfHouseholds = 0
                    communityUnit.mohPoplationDensity = 0
                    communityUnit.estimatedPopulationDensity = 0
                    communityUnit.distanceTONearestHealthFacility = 0
                    communityUnit.actLevels = 0
                    communityUnit.actPrice = 0
                    communityUnit.mrdtLevels = 0
                    communityUnit.mrdtPrice = 0
                    communityUnit.noOfDistibutors = 0
                    communityUnit.isChvsTrained = false
                    communityUnit.isPresenceOfEstates = false
                    communityUnit.setPresenceOfFactories(0L)
                    communityUnit.isPresenceOfHostels = false
                    communityUnit.isTraderMarket = false
                    communityUnit.isLargeSupermarket = false
                    communityUnit.isNgosGivingFreeDrugs = false
                    communityUnit.isNgoDoingIccm = false
                    communityUnit.isNgoDoingMhealth = false

                    val communityUnitTable = CommunityUnitTable(context!!)
                    communityUnitTable.addCommunityUnitData(communityUnit)

                    // clear data
                    communityUnitList.clear()
                    communityUnits.clear()
                    addCommunityUnits()
                    val cuAdapter = ArrayAdapter(context!!,
                            android.R.layout.simple_spinner_item, communityUnits)
                    cuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    editCuName.adapter = cuAdapter
                    editCuName.onItemSelectedListener = this
                    // set the selected CU
                    var x = 0
                    for (cu in communityUnitList) {
                        if (cu.id.equals(uuid, ignoreCase = true)) {
                            editCuName.setSelection(x, true)
                            break
                        }
                        x++
                    }
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }


    internal var onSelectedChewListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val selectedItem = chewReferrals[position]
            if (selectedItem.equals("Add New", ignoreCase = true)) {
                // Show Dialog to add the Referral
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Add new Referral")

                // Set up the input
                val title = EditText(context)
                val name = EditText(context)
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                // Context context = mapView.getContext();
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL

                val titleBox = EditText(context)
                titleBox.hint = "Title"

                //name for both KE and UG
                val refName = EditText(context)
                if (session.savedRecruitment.country.equals("UG", ignoreCase = true)) {
                    layout.addView(titleBox)
                    refName.hint = "Referral Name"
                } else {
                    refName.hint = "CHEW Name"
                }
                layout.addView(refName)


                val refPhone = EditText(context)
                refPhone.hint = "Phone"
                refPhone.inputType = InputType.TYPE_CLASS_PHONE
                layout.addView(refPhone)

                builder.setView(layout)

                // Set up the buttons
                builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    referralTitle = titleBox.text.toString()
                    referralName = refName.text.toString()
                    referralPhone = refPhone.text.toString()
                    if (referralName.equals("", ignoreCase = true)) {
                        Toast.makeText(context, "Name is required", Toast.LENGTH_LONG).show()
                        refName.requestFocus()
                        return@OnClickListener
                    }
                    // we save the referral, refresh the list and rebind the Spinner, and set selected
                    val uuid = UUID.randomUUID().toString()
                    val chew = ChewReferral(uuid, referralName, referralPhone, "CHEW",
                            session.savedRecruitment.country,
                            session.savedRecruitment.id, 0, "", "", "", "", "", "", "", "", "")
                    val chewTb = ChewReferralTable(context!!)
                    chewTb.addChewReferral(chew)

                    // clear chews
                    chewReferralList.clear()
                    chewReferrals.clear()
                    addChewReferrals()
                    val adapter = ArrayAdapter(context!!,
                            android.R.layout.simple_spinner_item, chewReferrals)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    selectChew.adapter = adapter

                    //lets set the selected
                    var x = 0
                    for (e in chewReferralList) {
                        if (e.id.equals(uuid, ignoreCase = true)) {
                            selectChew.setSelection(x, true)
                            break
                        }
                        x++
                    }
                })
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }

    internal var onSelectedWardListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val selectedItem = wards[position]
            if (selectedItem.equals("Add New", ignoreCase = true)) {
                // Show Dialog to add the Referral
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Add new Ward")

                // Set up the input
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL

                val titleBox = EditText(context)
                titleBox.hint = "Ward Name"
                titleBox.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                layout.addView(titleBox)

                builder.setView(layout)

                // Set up the buttons
                builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    wardName = titleBox.text.toString()
                    if (wardName.equals("", ignoreCase = true)) {
                        Toast.makeText(context, "Name is required", Toast.LENGTH_LONG).show()
                        titleBox.requestFocus()
                        return@OnClickListener
                    }
                    // we save the ward, refresh the list and rebind the Spinner, and set selected
                    val uuid = UUID.randomUUID().toString()
                    val ward = Ward()
                    ward.id = uuid
                    ward.name = wardName
                    ward.archived = 0
                    ward.subCounty = session.savedRecruitment.subcounty
                    ward.county = Integer.valueOf(session.savedRecruitment.county)
                    val wardTable = WardTable(context!!)
                    wardTable.addData(ward)

                    // clear chews
                    wardsList.clear()
                    wards.clear()
                    addWards()
                    val adapter = ArrayAdapter(context!!,
                            android.R.layout.simple_spinner_item, wards)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    editWard.adapter = adapter

                    //lets set the selected
                    var x = 0
                    for (e in wardsList) {
                        if (e.id.equals(uuid, ignoreCase = true)) {
                            editWard.setSelection(x, true)
                            break
                        }
                        x++
                    }
                })
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }

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
        val v = inflater.inflate(R.layout.fragment_new_ke_registration, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_REGISTRATION

        //check if Recruitment is set
        session = SessionManagement(context!!)
        session.checkRecruitment()

        //we can now extract User details
        user = session.userDetails
        //name
        userName = user[SessionManagement.KEY_NAME]!!
        //Emails
        userEmail = user[SessionManagement.KEY_EMAIL]!!
        userId = Integer.parseInt(user[SessionManagement.KEY_USERID])
        val recruitment = session.savedRecruitment
        recruitmentId = recruitment.id
        if (cu == null) {
            MainActivity.backFragment = RegistrationsFragment()
        } else {
            val registrationsFragment = RegistrationsFragment()
            registrationsFragment.communityUnit = cu
            MainActivity.backFragment = registrationsFragment
        }

        //Initialize the UI Components
        mName = v.findViewById<View>(R.id.editName) as EditText
        mPhone = v.findViewById<View>(R.id.editPhone) as EditText
        mGender = v.findViewById<View>(R.id.editGender) as RadioGroup
        mVillage = v.findViewById<View>(R.id.editVillage) as EditText
        mMark = v.findViewById<View>(R.id.editLandmark) as EditText
        mLangs = v.findViewById<View>(R.id.editOtherlanguages) as EditText
        mOccupation = v.findViewById<View>(R.id.editOccupation) as EditText
        mDob = v.findViewById<View>(R.id.editDob) as EditText
        mAge = v.findViewById<View>(R.id.editAge) as EditText
        mReadEnglish = v.findViewById<View>(R.id.editReadEnglish) as RadioGroup
        selectChew = v.findViewById<View>(R.id.selectChewReferral) as Spinner
        mComment = v.findViewById<View>(R.id.editComment) as EditText
        editWard = v.findViewById<View>(R.id.editWard) as Spinner
        editCuName = v.findViewById<View>(R.id.editCuName) as Spinner
        selectLinkFacility = v.findViewById<View>(R.id.selectLinkFacility) as Spinner
        selectMaritalStatus = v.findViewById<View>(R.id.selectMaritalStatus) as Spinner
        editBranchTransportCost = v.findViewById<View>(R.id.editBranchTransportCost) as EditText
        editRecruitmentTransportCost = v.findViewById<View>(R.id.editRecruitmentTransportCost) as EditText
        editNoOfHouseholds = v.findViewById<View>(R.id.editNoOfHouseholds) as EditText
        editOtherTrainings = v.findViewById<View>(R.id.editOtherTrainings) as EditText
        mDateMoved = v.findViewById<View>(R.id.editRelocated) as EditText
        mAccounts = v.findViewById<View>(R.id.editAccounts) as RadioGroup
        editIsChv = v.findViewById<View>(R.id.editIsChv) as RadioGroup
        editIsGokTrained = v.findViewById<View>(R.id.editIsGokTrained) as RadioGroup
        mCommunity = v.findViewById<View>(R.id.editCommunityMembership) as RadioGroup
        educationLevel = v.findViewById<View>(R.id.selectEdducation) as Spinner

        mAge.visibility = View.GONE
        mDob.visibility = View.GONE
        chooseAgeFormat = v.findViewById<View>(R.id.chooseAgeFormat) as RadioGroup
        chooseAgeFormat.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioAge -> {
                    mAge.visibility = View.VISIBLE
                    mDob.visibility = View.GONE
                }
                R.id.radioYear -> {
                    mDob.visibility = View.VISIBLE
                    mAge.visibility = View.GONE
                }
            }
        }


        addChewReferrals()

        val adapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, chewReferrals)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectChew.adapter = adapter
        selectChew.onItemSelectedListener = onSelectedChewListener

        addEducationSelectList()

        addCommunityUnits()
        val cuAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, communityUnits)
        cuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editCuName.adapter = cuAdapter
        editCuName.onItemSelectedListener = onSelectedCuListener
        // set the selected CU
        // In case there is no order followed
        if (cu != null) {
            var x = 0
            for (c in communityUnitList) {
                if (c.id.equals(cu!!.id, ignoreCase = true)) {
                    editCuName.setSelection(x, true)
                    break
                }
                x++
            }
        }
        addWards()
        addLinkFacilities()
        val linkFacilityAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, linkFacilities)
        linkFacilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectLinkFacility.adapter = linkFacilityAdapter
        selectLinkFacility.onItemSelectedListener = onSelectedLinkFacilityListener


        val wardAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, wards)
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editWard.adapter = wardAdapter
        // editWard.setOnItemSelectedListener(onSelectedWardListener);

        setUpEditingMode()

        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)

        buttonSave = v.findViewById<View>(R.id.buttonSaveRegistration) as Button
        buttonSave.setOnClickListener(this)

        mDob.setOnClickListener(this)
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
            R.id.buttonList -> {
                //
                if (cu != null) {
                    val registrationsFragment = RegistrationsFragment()
                    registrationsFragment.communityUnit = cu
                    val fragmentTransaction = activity!!
                            .supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, registrationsFragment, MainActivity.TAG_REGISTRATIONS)
                    fragmentTransaction.commitAllowingStateLoss()
                } else {
                    val registrationsFragment = RegistrationsFragment()
                    registrationsFragment.communityUnit = null
                    val fragmentTransaction = activity!!
                            .supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, registrationsFragment, MainActivity.TAG_REGISTRATIONS)
                    fragmentTransaction.commitAllowingStateLoss()
                }
                val newFragment = DatePickerFragment.newInstance(R.id.editDob)
                newFragment.show(fragmentManager!!, "DatePicker")
            }
            R.id.editDob -> {
                val newFragment = DatePickerFragment.newInstance(R.id.editDob)
                newFragment.show(fragmentManager!!, "DatePicker")
            }

            R.id.buttonSaveRegistration -> {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                val currentDate = Date().time

                val registrationId: String
                if (editingRegistration != null) {
                    registrationId = editingRegistration!!.id
                } else {
                    registrationId = UUID.randomUUID().toString()
                }
                val applicantName = mName.text.toString()
                val applicantPhone = mPhone.text.toString()


                val selectedGender = mGender.checkedRadioButtonId
                val genderRadioButton = mGender.findViewById<View>(selectedGender) as RadioButton
                val applicantGender = genderRadioButton.text.toString()

                val applicantDistrict = ""
                val applicantSubcounty = session.savedRecruitment.subcounty
                val applicantDivision = ""
                val applicantVillage = mVillage.text.toString()
                val applicantMark = mMark.text.toString()
                val applicantLangs = mLangs.text.toString()
                val applicantEducation = educationLevel.selectedItemId.toString()
                val applicantOccupation = mOccupation.text.toString()
                val applicantComment = mComment.text.toString()
                val aDob = mDob.text.toString()
                val applicantChewName = ""
                val applicantChewNumber = ""
                if (wards[editWard.selectedItemPosition].equals("Add New", ignoreCase = true) || wards[editWard.selectedItemPosition].trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Please select a valid Ward", Toast.LENGTH_SHORT)
                            .show()
                    return
                }
                val applicantWard = wardsList[editWard.selectedItemPosition].id
                var applicantCuName = ""
                if (communityUnits[editCuName.selectedItemPosition].equals("Add New", ignoreCase = true) || communityUnits[editCuName.selectedItemPosition].trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Please select a valid Community Unit", Toast.LENGTH_SHORT)
                            .show()
                    return
                }
                if (communityUnitList.size != 0) {
                    applicantCuName = communityUnitList[editCuName.selectedItemPosition].id
                }
                var applicantLinkFacility = ""
                if (linkFacilities[selectLinkFacility.selectedItemPosition].equals("Add New", ignoreCase = true) || linkFacilities[selectLinkFacility.selectedItemPosition].trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Please select a valid Link Facility", Toast.LENGTH_SHORT)
                            .show()
                    return
                }
                if (linkFacilityList.size != 0) {
                    applicantLinkFacility = linkFacilityList[selectLinkFacility.selectedItemPosition].id
                }
                val noOfHouseholds = editNoOfHouseholds.text.toString()
                val applicantOtherTrainings = editOtherTrainings.text.toString()
                //recruitmentTransportCost,transportCostToBranch

                // Transport From branch
                val transportCostBranch = editBranchTransportCost.text.toString()
                val transportCostToBranch: Long?
                if (transportCostBranch.trim { it <= ' ' } == "") {
                    transportCostToBranch = 0L
                } else {
                    transportCostToBranch = java.lang.Long.valueOf(transportCostBranch)
                }

                // recruitment Transport
                val recruitmentTransportCostI = editRecruitmentTransportCost.text.toString()
                val recruitmentTransportCost: Long?
                if (recruitmentTransportCostI.trim { it <= ' ' } == "") {
                    recruitmentTransportCost = 0L
                } else {
                    recruitmentTransportCost = java.lang.Long.valueOf(recruitmentTransportCostI)
                }

                //////////////
                val applicantNoOfHouseholds: Long?
                if (noOfHouseholds.trim { it <= ' ' } == "") {
                    applicantNoOfHouseholds = 0L
                } else {
                    applicantNoOfHouseholds = java.lang.Long.valueOf(noOfHouseholds)
                }
                val chv = editIsChv.checkedRadioButtonId
                val isChv = editIsChv.findViewById<View>(chv) as RadioButton
                val isApplicantAChv = isChv.text.toString()
                val applicantIsChv = isApplicantAChv.equals("Yes", ignoreCase = true)


                val acc = mAccounts.checkedRadioButtonId
                val accounts = mAccounts.findViewById<View>(acc) as RadioButton
                val hasAccount = accounts.text.toString()
                val applicantAccounts = hasAccount.equals("Yes", ignoreCase = true)

                val gok = editIsGokTrained.checkedRadioButtonId
                val isTrained = editIsGokTrained.findViewById<View>(gok) as RadioButton
                val isApplicantTrained = isTrained.text.toString()
                val isGokTrained = isApplicantTrained.equals("Yes", ignoreCase = true)

                var applicantDob: Long?
                if (chooseAgeFormat.checkedRadioButtonId == R.id.radioAge) {
                    // convert the age to years
                    val year = Calendar.getInstance().get(Calendar.YEAR)
                    val age = Integer.valueOf(mAge.text.toString())
                    val birthYear = year - age
                    val birthYyear = birthYear.toString() + "/1/1"
                    try {
                        val date = dateFormat.parse(birthYyear)
                        applicantDob = date.time
                    } catch (e: Exception) {
                        applicantDob = currentDate
                    }

                } else {
                    try {
                        val date = dateFormat.parse(aDob)
                        applicantDob = date.time
                    } catch (e: Exception) {
                        applicantDob = currentDate
                    }

                }


                val readEnglish = mReadEnglish.checkedRadioButtonId
                val readEnglishRadioButton = mReadEnglish.findViewById<View>(readEnglish) as RadioButton
                val canApplicantReadEnglish = readEnglishRadioButton.text.toString()
                val applicantReadEnglish = if (canApplicantReadEnglish.equals("yes", ignoreCase = true)) 1 else 0

                val dateMoved = mDateMoved.text.toString()
                val applicantDateMoved: Long?
                if (dateMoved.trim { it <= ' ' } == "") {
                    applicantDateMoved = 0L
                } else {
                    applicantDateMoved = java.lang.Long.valueOf(dateMoved)
                }

                val applicantBrac = 0


                val applicantBracChp = 0 // = hasApplicantWorkedAsBracChp == "Yes" ? 1 : 0;

                if (chewReferrals[selectChew.selectedItemPosition].equals("Add New", ignoreCase = true) || chewReferrals[selectChew.selectedItemPosition].trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(context, "Please select a valid CHEW", Toast.LENGTH_SHORT)
                            .show()
                    return
                }

                val chewUuid = chewReferralList[selectChew.selectedItemPosition].id
                val maritalStatus = selectMaritalStatus.selectedItem.toString()

                val communityParticipation = mCommunity.checkedRadioButtonId
                val hasCommunityParticipation = mCommunity.findViewById<View>(communityParticipation) as RadioButton
                val hasApplicantCommunityParticipation = hasCommunityParticipation.text.toString()
                val applicantCommunity = if (hasApplicantCommunityParticipation.equals("yes", ignoreCase = true)) 1 else 0

                val applicantAddedBy = userId
                val applicantProceed = 0
                val applicantSync = 0
                val applicantRecruitment = recruitmentId
                val country = user[SessionManagement.KEY_USER_COUNTRY]

                // Do some validations
                if (applicantName.trim { it <= ' ' } == "") {
                    mName.requestFocus()
                    Toast.makeText(context, "Name cannot be blank", Toast.LENGTH_SHORT).show()
                    return
                }
                if (applicantVillage.trim { it <= ' ' } == "") {
                    mVillage.requestFocus()
                    Toast.makeText(context, "Village is required", Toast.LENGTH_SHORT).show()
                    return
                }
                if (applicantPhone.trim { it <= ' ' } != "") {
                    if (applicantPhone.trim { it <= ' ' }.startsWith("+")) {
                        if (applicantPhone.length != 13) {
                            Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                            mPhone.requestFocus()
                            return
                        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(applicantPhone)) {
                            mPhone.requestFocus()
                            Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                            return
                        }
                    } else if (applicantPhone.length != 10) {
                        mPhone.requestFocus()
                        Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                        return
                    } else if (!PhoneNumberUtils.isGlobalPhoneNumber(applicantPhone)) {
                        Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                if (applicantEducation.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Education is required", Toast.LENGTH_SHORT).show()
                    return
                }

                // Save Registration
                //                    Registration registration = new Registration(mName, mNumber, mEmail);
                val registration: Registration
                registration = Registration(registrationId, applicantName, applicantPhone, applicantGender,
                        applicantDistrict, applicantSubcounty, applicantDivision, applicantVillage,
                        applicantMark, applicantLangs, applicantEducation, applicantOccupation,
                        applicantComment, applicantDob, applicantReadEnglish, applicantRecruitment,
                        country!!, applicantDateMoved, applicantBrac, applicantBracChp, applicantCommunity,
                        applicantAddedBy, applicantProceed, currentDate, applicantSync, applicantChewName,
                        applicantChewNumber, applicantWard, applicantCuName, applicantLinkFacility,
                        applicantNoOfHouseholds!!, applicantIsChv, isGokTrained, applicantOtherTrainings,
                        "", "", "", false, applicantAccounts, "", recruitmentTransportCost,
                        transportCostToBranch, chewUuid, maritalStatus, "", "", "")

                // Before saving, do some validations
                // Years in location should always be less than age
                if (registration.age!!.compareTo(applicantDateMoved!!) < 0) {
                    mDateMoved.requestFocus()
                    Toast.makeText(context, "The years at the location is greater than the age ", Toast.LENGTH_LONG).show()
                    return
                }
                val registrationTable = RegistrationTable(context!!)
                val id = registrationTable.addData(registration)
                if (id.equals(-1)) {
                    Toast.makeText(context, "Could not save registration", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                    // Clear boxes
                    mName.setText("")
                    mPhone.setText("")
                    mGender.clearCheck()
                    mVillage.setText("")
                    mMark.setText("")
                    mLangs.setText("")
                    educationLevel.setSelection(0)
                    selectChew.setSelection(0)
                    mOccupation.setText("")
                    mDob.setText("")
                    editWard.setSelection(0)
                    //editCuName.setSelection(0);
                    editNoOfHouseholds.setText("")
                    editOtherTrainings.setText("")
                    editIsChv.clearCheck()
                    mAccounts.clearCheck()
                    editIsGokTrained.clearCheck()
                    mReadEnglish.clearCheck()
                    mDateMoved.setText("")
                    mCommunity.clearCheck()
                    editingRegistration = null
                    editBranchTransportCost.setText("")
                    mComment.setText("")
                    editRecruitmentTransportCost.setText("")
                    mName.requestFocus()


                }
            }
            R.id.buttonSave -> {
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

    fun addEducationSelectList() {
        val educationTable = EducationTable(context!!)
        val cursorAdapter = SpinnersCursorAdapter(context!!,
                educationTable.getEducationDataCursor(user[SessionManagement.KEY_USER_COUNTRY]!!))
        educationLevel.adapter = cursorAdapter
        // add education details
        val cursor = educationTable.getEducationDataCursor(user[SessionManagement.KEY_USER_COUNTRY]!!)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val education = Education()
            education.id = cursor.getInt(0)
            education.levelName = cursor.getString(1)
            education.levelType = cursor.getString(2)
            education.hierachy = cursor.getInt(3)
            education.country = cursor.getString(4)
            educationList.add(education)
            cursor.moveToNext()
        }


    }

    fun addChewReferrals() {
        val chewReferralTable = ChewReferralTable(context!!)
        chewReferralList = chewReferralTable.getChewReferralByRecruitmentId(session.savedRecruitment.id) as MutableList<ChewReferral>
        for (chewReferral in chewReferralList) {
            chewReferrals.add(chewReferral.name)
        }
        if (chewReferrals.size == 0) {
            chewReferrals.add("  ")
        }
        chewReferrals.add("Add New")
    }

    fun addCommunityUnits() {
        val cuTable = CommunityUnitTable(context!!)
        //communityUnits communityUnitList
        communityUnitList = cuTable.getCommunityUnitBySubCounty(session.savedRecruitment.subcounty) as MutableList<CommunityUnit>
        for (cUnit in communityUnitList) {
            communityUnits.add(cUnit.communityUnitName)
        }
        if (communityUnits.size == 0) {
            communityUnits.add("  ")
        }
        communityUnits.add("Add New")
    }

    fun addLinkFacilities() {
        val linkFacilityTable = LinkFacilityTable(context!!)
        //communityUnits communityUnitList
        linkFacilityList = linkFacilityTable.getLinkFacilityBySubCounty(session
                .savedRecruitment.subcounty) as MutableList<LinkFacility>
        for (lFacility in linkFacilityList) {
            linkFacilities.add(lFacility.facilityName)
        }
        if (linkFacilities.size == 0) {
            linkFacilities.add("  ")
        }
        linkFacilities.add("Add New")
    }

    fun addWards() {
        val wardTable = WardTable(context!!)
        wardsList = wardTable.getWardsbySubCounty(SubCountyTable(context!!)
                .getSubCountyById(session.savedRecruitment.subcounty)!!) as MutableList<Ward>
        for (w in wardsList) {
            wards.add(w.name)
        }
        if (wards.size == 0) {
            wards.add(" ")
        }
        wards.add("Add New")
    }

    fun setUpEditingMode() {
        if (editingRegistration != null) {
            mName.setText(editingRegistration!!.name)
            mPhone.setText(editingRegistration!!.phone)
            editRecruitmentTransportCost.setText(
                    editingRegistration!!.recruitmentTransportCost.toString())
            editBranchTransportCost.setText(
                    editingRegistration!!.transportCostToBranch.toString())
            mGender.clearCheck()
            if (editingRegistration!!.gender.equals("Male", ignoreCase = true)) {
                mGender.check(R.id.radioM)
            } else {
                mGender.check(R.id.radioF)
            }
            mVillage.setText(editingRegistration!!.village)
            mMark.setText(editingRegistration!!.mark)
            mLangs.setText(editingRegistration!!.langs)

            //            // A dirty Hack
            var x = 0
            for (e in educationList) {
                if (e.id == Integer.valueOf(editingRegistration!!.education)) {
                    educationLevel.setSelection(x, true)
                    break
                }
                x++
            }
            x = 0
            for (c in chewReferralList) {
                if (c.id.equals(editingRegistration!!.chewUuid, ignoreCase = true)) {
                    selectChew.setSelection(x, true)
                    break
                }
                x++
            }

            x = 0
            for (w in wardsList) {
                if (w.id.equals(editingRegistration!!.ward, ignoreCase = true)) {
                    editWard.setSelection(x, true)
                    break
                }
                x++
            }

            mOccupation.setText(editingRegistration!!.occupation)
            mDob.setText(DisplayDate(java.lang.Long.valueOf(editingRegistration!!.dob!!)).widgetDateOnly())
            x = 0
            for (cu in communityUnitList) {
                if (cu.id.equals(editingRegistration!!.cuName, ignoreCase = true)) {
                    editCuName.setSelection(x, true)
                    break
                }
                x++
            }
            x = 0
            for (lf in linkFacilityList) {
                if (lf.id.equals(editingRegistration!!.linkFacility, ignoreCase = true)) {
                    selectLinkFacility.setSelection(x, true)
                    break
                }
                x++
            }
            editNoOfHouseholds.setText(editingRegistration!!.noOfHouseholds.toString())
            editOtherTrainings.setText(editingRegistration!!.otherTrainings)
            if (editingRegistration!!.isChv) {
                editIsChv.check(R.id.editIsChvYes)
            } else {
                editIsChv.check(R.id.editIsChvNo)
            }
            mAccounts.check(if (editingRegistration!!.isAccounts) R.id.editAccountsYes else R.id.editAccountsNo)
            editIsGokTrained.check(if (editingRegistration!!.isGokTrained) R.id.editIsGokTrainedYes else R.id.editIsGokTrainedNo)
            mReadEnglish.check(if (editingRegistration!!.readEnglish == 1) R.id.radioCanReadEnglish else R.id.radioCannotReadEnglish)
            mDateMoved.setText(editingRegistration!!.dateMoved!!.toString())
            mCommunity.check(if (editingRegistration!!.community == 1) R.id.radioCommMbrYes else R.id.radioCommMbrNo)
            mName.requestFocus()
            editBranchTransportCost.setText(editingRegistration!!.transportCostToBranch.toString())
            editRecruitmentTransportCost.setText(editingRegistration!!.recruitmentTransportCost.toString())
        }
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
        fun newInstance(param1: String, param2: String): NewKeRegistrationFragment {
            val fragment = NewKeRegistrationFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
