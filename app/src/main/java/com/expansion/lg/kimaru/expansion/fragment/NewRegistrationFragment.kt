package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.telephony.PhoneNumberUtils
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.support.v4.app.DialogFragment

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation
import com.expansion.lg.kimaru.expansion.mzigos.Parish
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.mzigos.Village
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.other.DisplayDate
import com.expansion.lg.kimaru.expansion.other.SpinnersCursorAdapter
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import com.expansion.lg.kimaru.expansion.tables.EducationTable
import com.expansion.lg.kimaru.expansion.tables.ParishTable
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable
import com.expansion.lg.kimaru.expansion.tables.VillageTable

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
 * [NewRegistrationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewRegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewRegistrationFragment : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal var textshow: TextView? = null
    internal lateinit var mName: EditText
    //EditText mPhone;
    internal lateinit var mGender: RadioGroup
    //EditText editReferralName;
    //EditText editReferralNumber;
    //EditText editReferralTitle;
    internal lateinit var editVht: RadioGroup
    //EditText editSubCounty;
    //EditText editParish;

    internal var mDistrict: EditText? = null
    internal var mDivision: EditText? = null
    //EditText mVillage;
    internal lateinit var mMark: EditText
    internal lateinit var mLangs: EditText
    internal lateinit var mOccupation: EditText
    internal lateinit var mComment: EditText
    internal lateinit var mDob: EditText
    internal lateinit var mAge: EditText
    internal lateinit var mReadEnglish: RadioGroup
    internal var mRecruitment: EditText? = null
    internal lateinit var mDateMoved: EditText
    internal lateinit var mBrac: RadioGroup
    internal lateinit var mAccounts: RadioGroup
    internal lateinit var mBracChp: RadioGroup
    internal lateinit var mCommunity: RadioGroup
    internal lateinit var chooseAgeFormat: RadioGroup
    internal var mAddedBy: EditText? = null
    internal var mProceed: EditText? = null
    internal var mDateAdded: EditText? = null
    internal var mSync: EditText? = null
    internal lateinit var educationLevel: Spinner
    internal lateinit var selectChew: Spinner
    internal lateinit var selectSubCounty: Spinner
    internal lateinit var selectParish: Spinner
    internal lateinit var selectVillage: Spinner
    internal lateinit var addAnotherPhone: Button

    internal lateinit var buttonSave: Button
    internal lateinit var buttonList: Button


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0


    internal var loggedInUser: Int? = 1

    internal lateinit var session: SessionManagement
    internal lateinit var userName: String
    internal lateinit var userEmail: String
    internal lateinit var recruitmentId: String
    internal lateinit var user: HashMap<String, String>
    internal var userId: Int? = null
    internal var editingRegistration: Registration? = null

    internal var chewReferralList: MutableList<ChewReferral> = ArrayList()
    internal var chewReferrals: MutableList<String> = ArrayList()

    internal var countyLocationList: List<CountyLocation> = ArrayList()
    internal var locations: MutableList<String> = ArrayList()

    internal var parishList: MutableList<Parish> = ArrayList()
    internal var parishes: MutableList<String> = ArrayList()

    internal var villageList: MutableList<Village> = ArrayList()
    internal var villages: MutableList<String> = ArrayList()

    private var referralTitle = ""
    private var referralName = ""
    private var referralPhone = ""
    internal lateinit var linearLayout: LinearLayout

    internal var onParishSelected: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            //populate the villages
            villageList.clear()
            villages.clear()
            villageList = VillageTable(context!!).getVillageDataByParishId(parishList[selectParish.selectedItemPosition].id) as MutableList<Village>
            for (v in villageList) {
                villages.add(v.villageName)
            }
            bindVillageAdapter()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }

    internal var onSelectedSubCounty: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            //here we populate the Parishes.
            //rebind the adapter
            bindParishAdapter()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }

    internal var onSelectedChewListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            if (position > chewReferralList.size - 1) {
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
                layout.addView(titleBox)

                val refName = EditText(context)
                refName.hint = "Referral Name"
                layout.addView(refName)

                val refPhone = EditText(context)
                refPhone.hint = "Phone"
                refPhone.inputType = InputType.TYPE_CLASS_PHONE
                layout.addView(refPhone)

                builder.setView(layout)

                // Set up the buttons
                builder.setPositiveButton("OK") { dialog, which ->
                    referralTitle = titleBox.text.toString()
                    referralName = refName.text.toString()
                    referralPhone = refPhone.text.toString()
                    // we save the referral, refresh the list and rebind the Spinner, and set selected
                    val uuid = UUID.randomUUID().toString()
                    val chew = ChewReferral(uuid, referralName, referralPhone, referralTitle,
                            session.savedRecruitment.country,
                            session.savedRecruitment.id, 0,
                            session.savedRecruitment.county,
                            session.savedRecruitment.district,
                            session.savedRecruitment.subcounty, "", "", "", "", "", "")

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
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            if (selectChew.selectedItemPosition > chewReferralList.size - 1) {
                showDialog()
            }
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
        val v = inflater.inflate(R.layout.fragment_new_registration, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_REGISTRATION
        MainActivity.backFragment = RegistrationsFragment()
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
        addAnotherPhone = v.findViewById<View>(R.id.addAnotherPhone) as Button
        addAnotherPhone.setOnClickListener(this)
        linearLayout = v.findViewById<View>(R.id.linearPhoneLayout) as LinearLayout

        //phone number editText
        val phoneNumber = EditText(context)
        phoneNumber.id = View.generateViewId()
        phoneNumber.setHint(R.string.add_phone_number)
        val phoneNumberParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        //android:inputType="phone"
        phoneNumber.inputType = InputType.TYPE_CLASS_PHONE
        phoneNumber.layoutParams = phoneNumberParams

        //Since the editText exists in TextInpoutLayout, we create one and put in it the editText
        val textInputLayout = TextInputLayout(context!!)
        textInputLayout.id = View.generateViewId()
        val phoneLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        textInputLayout.layoutParams = phoneLayoutParams
        textInputLayout.addView(phoneNumber, phoneNumberParams)
        linearLayout.addView(textInputLayout, phoneLayoutParams)


        //Initialize the UI Components
        mName = v.findViewById<View>(R.id.editName) as EditText
        mGender = v.findViewById<View>(R.id.editGender) as RadioGroup
        //mVillage = (EditText) v.findViewById(R.id.editVillage);
        mMark = v.findViewById<View>(R.id.editLandmark) as EditText
        mLangs = v.findViewById<View>(R.id.editOtherlanguages) as EditText
        mOccupation = v.findViewById<View>(R.id.editOccupation) as EditText
        mDob = v.findViewById<View>(R.id.editDob) as EditText
        mAge = v.findViewById<View>(R.id.editAge) as EditText
        selectChew = v.findViewById<View>(R.id.selectChewReferral) as Spinner
        selectSubCounty = v.findViewById<View>(R.id.selectSubCounty) as Spinner
        mComment = v.findViewById<View>(R.id.editComment) as EditText
        mReadEnglish = v.findViewById<View>(R.id.editReadEnglish) as RadioGroup
        mDateMoved = v.findViewById<View>(R.id.editRelocated) as EditText
        mBrac = v.findViewById<View>(R.id.editWorkedWithBrac) as RadioGroup
        mAccounts = v.findViewById<View>(R.id.editAccounts) as RadioGroup
        mBracChp = v.findViewById<View>(R.id.editBracChp) as RadioGroup
        mCommunity = v.findViewById<View>(R.id.editCommunityMembership) as RadioGroup
        educationLevel = v.findViewById<View>(R.id.selectEdducation) as Spinner
        //editReferralName = (EditText) v.findViewById(R.id.editReferralName);
        //editReferralNumber = (EditText) v.findViewById(R.id.editReferralNumber);
        //editReferralTitle = (EditText) v.findViewById(R.id.editReferralTitle);
        // editSubCounty = (EditText) v.findViewById(R.id.editSubCounty);
        //editParish = (EditText) v.findViewById(R.id.editParish);
        editVht = v.findViewById<View>(R.id.editVht) as RadioGroup
        // mAge.setVisibility(View.GONE);
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
        addSubCounties()

        val adapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, chewReferrals)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectChew.adapter = adapter
        selectChew.onItemSelectedListener = onSelectedChewListener

        bindSubCountyAdapter()
        selectSubCounty.onItemSelectedListener = onSelectedSubCounty

        //Parish must come after the SubCounty
        selectParish = v.findViewById<View>(R.id.selectParish) as Spinner
        bindParishAdapter()
        selectParish.onItemSelectedListener = onParishSelected

        selectVillage = v.findViewById<View>(R.id.selectVillage) as Spinner

        addEducationSelectList()
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

    fun showDialog() {
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
        layout.addView(titleBox)

        val refName = EditText(context)
        refName.hint = "Referral Name"
        layout.addView(refName)

        val refPhone = EditText(context)
        refPhone.hint = "Phone"
        refPhone.inputType = InputType.TYPE_CLASS_PHONE
        layout.addView(refPhone)

        builder.setView(layout)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->
            referralTitle = titleBox.text.toString()
            referralName = refName.text.toString()
            referralPhone = refPhone.text.toString()
            // we save the referral, refresh the list and rebind the Spinner, and set selected
            val uuid = UUID.randomUUID().toString()
            val chew = ChewReferral(uuid, referralName, referralPhone, referralTitle,
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
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonList -> {
                val fragment = RegistrationsFragment()
                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_REGISTRATION)
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.editDob -> {
                val newFragment = DatePickerFragment.newInstance(R.id.editDob)
                newFragment.show(fragmentManager!!, "DatePicker")
            }
            R.id.selectChewReferral -> if (selectChew.selectedItemPosition > chewReferralList.size - 1) {
                showDialog()
            }

            R.id.addAnotherPhone -> {
                //phone number editText
                val phoneNumber = EditText(context)
                phoneNumber.id = View.generateViewId()
                phoneNumber.setHint(R.string.add_phone_number)
                val phoneNumberParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                //android:inputType="phone"
                phoneNumber.inputType = InputType.TYPE_CLASS_PHONE
                phoneNumber.layoutParams = phoneNumberParams

                //Since the editText exists in TextInpoutLayout, we create one and put in it the editText
                val textInputLayout = TextInputLayout(context!!)
                textInputLayout.id = View.generateViewId()
                val phoneLayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                textInputLayout.layoutParams = phoneLayoutParams
                textInputLayout.addView(phoneNumber, phoneNumberParams)
                linearLayout.addView(textInputLayout, phoneLayoutParams)
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
                var applicantPhone = ""

                val selectedGender = mGender.checkedRadioButtonId
                val genderRadioButton = mGender.findViewById<View>(selectedGender) as RadioButton
                val applicantGender = genderRadioButton.text.toString()


                val selectedVht = editVht.checkedRadioButtonId
                val vht = editVht.findViewById<View>(selectedVht) as RadioButton
                val isVht = vht.text.toString().equals("Yes", ignoreCase = true)


                val referralName = "" //editReferralName.getText().toString();
                val referralNumber = "" //editReferralNumber.getText().toString();
                val referralTitle = "" //editReferralTitle.getText().toString();
                // String applicantParish = editParish.getText().toString();


                val applicantDistrict = ""
                // String applicantSubcounty = editSubCounty.getText().toString();
                val applicantDivision = ""
                // String applicantVillage = mVillage.getText().toString();
                val applicantMark = mMark.text.toString()
                val applicantLangs = mLangs.text.toString()
                val applicantEducation = educationLevel.selectedItemId.toString()
                val applicantOccupation = mOccupation.text.toString()
                val applicantComment = mComment.text.toString()
                val aDob = mDob.text.toString()
                val strApplicantAge = ""
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
                val applicantReadEnglish = if (canApplicantReadEnglish.equals("Yes", ignoreCase = true)) 1 else 0

                val dateMoved = mDateMoved.text.toString()
                val applicantDateMoved: Long?
                if (dateMoved.trim { it <= ' ' } == "") {
                    applicantDateMoved = 0L
                } else {
                    applicantDateMoved = java.lang.Long.valueOf(dateMoved)
                }

                val workedBrac = mBrac.checkedRadioButtonId
                val hasWorkAtBrac = mBrac.findViewById<View>(workedBrac) as RadioButton
                val hasApplicantWorkedAtBrac = hasWorkAtBrac.text.toString()
                val applicantBrac: Int?
                if (hasApplicantWorkedAtBrac.equals("Yes", ignoreCase = true)) {
                    applicantBrac = 1
                } else {
                    applicantBrac = 0
                }

                val workedBracAsChp = mBracChp.checkedRadioButtonId
                val hasWorkAsBracChp = mBracChp.findViewById<View>(workedBracAsChp) as RadioButton
                val hasApplicantWorkedAsBracChp = hasWorkAsBracChp.text.toString()
                val applicantBracChp: Int? // = hasApplicantWorkedAsBracChp == "Yes" ? 1 : 0;
                if (hasApplicantWorkedAsBracChp.equals("Yes", ignoreCase = true)) {
                    applicantBracChp = 1
                } else {
                    applicantBracChp = 0
                }

                val communityParticipation = mCommunity.checkedRadioButtonId
                val hasCommunityParticipation = mCommunity.findViewById<View>(communityParticipation) as RadioButton
                val hasApplicantCommunityParticipation = hasCommunityParticipation.text.toString()
                val applicantCommunity = if (hasApplicantCommunityParticipation.equals("Yes", ignoreCase = true)) 1 else 0

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
                // also validate other added phone numbers, and push them to a list
                val phoneNumbers = ArrayList<String>()

                if (linearLayout.childCount == 0) {
                    Toast.makeText(context, "At least one phone number is required",
                            Toast.LENGTH_SHORT).show()
                    return
                }
                for (i in 0 until linearLayout.childCount) {
                    if (linearLayout.getChildAt(i) is TextInputLayout) {
                        val t = linearLayout.getChildAt(i) as TextInputLayout
                        val p = t.editText
                        val phone = p!!.text.toString()
                        if (isValidPhone(phone)) {
                            //add this to arrayList
                            phoneNumbers.add(phone)
                        } else {
                            Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                            p.requestFocus()
                            return
                        }
                    }
                }
                // at this point, we create the phones and we separate them with ';'
                val sb = StringBuilder()
                for (x in phoneNumbers) {
                    sb.append(x).append(";")
                }
                applicantPhone = sb.toString()

                //                if (!applicantPhone.trim().equals("")){
                //                    if (applicantPhone.trim().startsWith("+")){
                //                        if (applicantPhone.length() != 13){
                //                            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                //                            mPhone.requestFocus();
                //                            return;
                //                        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(applicantPhone)) {
                //                            mPhone.requestFocus();
                //                            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                //                            return;
                //                        }
                //                    }else if (applicantPhone.length() != 10){
                //                        mPhone.requestFocus();
                //                        Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                //                        return;
                //                    }else if(!PhoneNumberUtils.isGlobalPhoneNumber(applicantPhone)){
                //                        Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                //                        return;
                //                    }
                //                }
                if (applicantEducation.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Education is required", Toast.LENGTH_SHORT).show()
                    return
                }
                val chewUuid = chewReferralList[selectChew.selectedItemPosition].id
                val parishId = parishList[selectParish.selectedItemPosition].id
                var villageId = ""
                if (villageList.size > 0) {
                    villageId = villageList[selectVillage.selectedItemPosition].id
                } else {
                    Toast.makeText(context, "No Village selected", Toast.LENGTH_SHORT).show()
                    return
                }
                var subCountyId = ""
                if (villageList.size > 0) {
                    subCountyId = countyLocationList[selectSubCounty.selectedItemPosition].id!!.toString()
                } else {
                    Toast.makeText(context, "No County selected", Toast.LENGTH_SHORT).show()
                    return
                }
                // Save Registration //String subCountyId, String parishId, String villageId
                //                    Registration registration = new Registration(mName, mNumber, mEmail);
                val registration: Registration
                registration = Registration(registrationId, applicantName, applicantPhone, applicantGender,
                        applicantDistrict, "", applicantDivision, "",
                        applicantMark, applicantLangs, applicantEducation, applicantOccupation,
                        applicantComment, applicantDob, applicantReadEnglish, applicantRecruitment,
                        country!!, applicantDateMoved, applicantBrac, applicantBracChp, applicantCommunity,
                        applicantAddedBy, applicantProceed, currentDate, applicantSync, "",
                        "", "", "", "", 0L,
                        false, false, "", referralName, referralTitle,
                        referralNumber, isVht, false, "",
                        0L, 0L, chewUuid, "",
                        subCountyId, parishId, villageId)

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
                    //mVillage.setText("");
                    mMark.setText("")
                    mLangs.setText("")
                    mOccupation.setText("")
                    mComment.setText("")
                    mDob.setText("")
                    mDateMoved.setText("")
                    mName.requestFocus()
                    // editReferralName.setText("");
                    // editReferralTitle.setText("");
                    // editReferralNumber.setText("");
                    //editParish.setText("");
                    editVht.clearCheck()
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

    }

    fun setUpEditingMode() {
        if (editingRegistration != null) {
            mName.setText(editingRegistration!!.name)
            //mVillage.setText(editingRegistration.getVillage());
            mMark.setText(editingRegistration!!.mark)
            mLangs.setText(editingRegistration!!.langs)
            mOccupation.setText(editingRegistration!!.occupation)
            mDob.setText(DisplayDate(java.lang.Long.valueOf(editingRegistration!!.dob!!)).widgetDateOnly())
            mDateMoved.setText(editingRegistration!!.dateMoved!!.toString())
            //editReferralName.setText(editingRegistration.getReferralName());
            //editReferralTitle.setText(editingRegistration.getReferralTitle());
            //editReferralNumber.setText(editingRegistration.getReferralPhone());
            //editParish.setText(editingRegistration.getParish());
            //editSubCounty.setText(editingRegistration.getSubcounty());


            // Phone number
            val phones = editingRegistration!!.phone.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            //clear the unused view
            //parentLinearLayout.removeView((View) v.getParent());
            linearLayout.removeAllViews()

            for (phone in phones) {
                val phoneNumber = EditText(context)
                phoneNumber.id = View.generateViewId()
                phoneNumber.setHint(R.string.add_phone_number)
                val phoneNumberParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                phoneNumber.inputType = InputType.TYPE_CLASS_PHONE
                phoneNumber.layoutParams = phoneNumberParams
                phoneNumber.setText(phone)

                //Since the editText exists in TextInpoutLayout, we create one and put in it the editText
                val textInputLayout = TextInputLayout(context!!)
                textInputLayout.id = View.generateViewId()
                val phoneLayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                textInputLayout.layoutParams = phoneLayoutParams
                textInputLayout.addView(phoneNumber, phoneNumberParams)

                linearLayout.addView(textInputLayout, phoneLayoutParams)
            }





            mComment.setText(editingRegistration!!.comment)
            editVht.clearCheck()
            editVht.check(if (editingRegistration!!.isVht) R.id.editVhtYes else R.id.editVhtNo)

            mGender.clearCheck()
            mGender.check(if (editingRegistration!!.gender.equals("Male", ignoreCase = true)) R.id.radioM else R.id.radioF)



            mReadEnglish.clearCheck()
            mReadEnglish.check(if (editingRegistration!!.readEnglish == 1)
                R.id.radioCanReadEnglish
            else
                R.id.radioCannotReadEnglish)

            mBrac.clearCheck()
            mBrac.check(if (editingRegistration!!.brac == 1) R.id.radiobracYes else R.id.radiobracNo)

            mBracChp.clearCheck()
            mBracChp.check(if (editingRegistration!!.bracChp == 1)
                R.id.radiobracChpYes
            else
                R.id.radiobracChpNo)


            mCommunity.clearCheck()
            mCommunity.check(if (editingRegistration!!.community == 1)
                R.id.radioCommMbrYes
            else
                R.id.radioCommMbrNo)

            educationLevel.setSelection(Integer.valueOf(editingRegistration!!.education) - 1, true)
            var x = 0
            for (c in chewReferralList) {
                if (c.id.equals(editingRegistration!!.chewUuid, ignoreCase = true)) {
                    selectChew.setSelection(x, true)
                    break
                }
                x++
            }
            //SubCounty
            x = 0
            for (cv in countyLocationList) {
                if (cv.id.toString().equals(editingRegistration!!.subCountyId)) {
                    selectSubCounty.setSelection(x, true)
                    break
                }
            }
            //Parish
            x = 0
            for (p in parishList) {
                if (p.id.equals(editingRegistration!!.parishId, ignoreCase = true)) {
                    selectParish.setSelection(x, true)
                    break
                }
            }

            //village
            x = 0
            for (v in villageList) {
                if (v.id.equals(editingRegistration!!.villageId, ignoreCase = true)) {
                    selectVillage.setSelection(x, true)
                    break
                }
            }


        }
    }

    fun addChewReferrals() {
        val chewReferralTable = ChewReferralTable(context!!)
        chewReferralList = chewReferralTable.getChewReferralByRecruitmentId(session.savedRecruitment.id) as MutableList<ChewReferral>
        for (chewReferral in chewReferralList) {
            chewReferrals.add(chewReferral.name)
        }
        chewReferrals.add("Add New")
    }

    fun addSubCounties() {
        val countyLocationTable = CountyLocationTable(context!!)
        countyLocationList = countyLocationTable.getChildrenLocations(session.savedRecruitment.countyId!!.toString())
        for (loc in countyLocationList) {
            locations.add(loc.name)
        }
    }

    fun bindVillageAdapter() {
        villages.clear()
        villageList.clear()
        villageList = VillageTable(context!!).getVillageDataByParishId(parishList[selectParish.selectedItemPosition].id) as MutableList<Village>
        for (v in villageList) {
            villages.add(v.villageName)
        }
        val villageAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, villages)
        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectVillage.adapter = villageAdapter

    }

    fun bindSubCountyAdapter() {
        val countyLocationAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, locations)
        countyLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectSubCounty.adapter = countyLocationAdapter
    }

    fun bindParishAdapter() {
        parishes.clear()
        parishList.clear()
        parishList = ParishTable(context!!).getParishByParent(countyLocationList[selectSubCounty.selectedItemPosition].id!!.toString()) as MutableList<Parish>
        for (p in parishList) {
            parishes.add(p.name)
        }
        val parishAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, parishes)
        parishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectParish.adapter = parishAdapter
    }

    private fun isValidPhone(phoneNumber: String): Boolean {
        return Constants(context!!).isValidPhone(phoneNumber)
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
        fun newInstance(param1: String, param2: String): NewRegistrationFragment {
            val fragment = NewRegistrationFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
