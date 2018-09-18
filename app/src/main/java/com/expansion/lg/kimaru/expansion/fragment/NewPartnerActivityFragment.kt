package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.telephony.PhoneNumberUtils
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.IccmComponent
import com.expansion.lg.kimaru.expansion.mzigos.Parish
import com.expansion.lg.kimaru.expansion.mzigos.PartnerActivity
import com.expansion.lg.kimaru.expansion.mzigos.Partners
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.mzigos.Village
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable
import com.expansion.lg.kimaru.expansion.tables.PartnersTable

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewPartnerActivityFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewPartnerActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewPartnerActivityFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    internal var backFragment: Fragment? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal  lateinit var selectPartner: Spinner
    internal  lateinit var partnerActivityComment: EditText
    internal  lateinit var editIsDoingIccm: RadioGroup
    internal  lateinit var editIsGivingFreeMedicine: RadioGroup
    internal lateinit  var editIsGivingStipend: RadioGroup
    internal  lateinit var editIsDoingMhealth: RadioGroup
    internal lateinit  var parentLayout: LinearLayout
    internal var partnersList: MutableList<Partners> = ArrayList()
    internal var partnerNames: MutableList<String> = ArrayList()
    internal var iccmComponentList: List<IccmComponent> = ArrayList()


    internal  lateinit var buttonSave: Button
    internal  lateinit var buttonList: Button
    var editingPartnerActivity: PartnerActivity? = null
    var village: Village? = null
    var parish: Parish? = null
    var subCounty: SubCounty? = null
    var communityUnit: CommunityUnit? = null

    internal lateinit  var session: SessionManagement
    internal lateinit  var user: HashMap<String, String>
    internal lateinit  var country: String
    internal var onSelectedPartnerListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val selectedItem = partnerNames[position]
            if (selectedItem.equals("Add New", ignoreCase = true)) {
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Add a New Partner")
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL
                val editTextPartnerName = EditText(context)
                editTextPartnerName.hint = "Partner Name"
                editTextPartnerName.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                layout.addView(editTextPartnerName)

                val editTextPartnerContactPerson = EditText(context)
                editTextPartnerContactPerson.hint = "Contact person"
                editTextPartnerContactPerson.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                layout.addView(editTextPartnerContactPerson)

                val editTextPartnerContactPersonPhone = EditText(context)
                editTextPartnerContactPersonPhone.hint = "Contact person phone"
                editTextPartnerContactPersonPhone.inputType = InputType.TYPE_CLASS_PHONE
                layout.addView(editTextPartnerContactPersonPhone)

                val editTextComment = EditText(context)
                editTextComment.hint = "Comment"
                editTextComment.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                layout.addView(editTextComment)


                builder.setView(layout)

                builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    val partnerName = editTextPartnerName.text.toString()
                    if (partnerName.equals("", ignoreCase = true)) {
                        Toast.makeText(context, "Facility Name is required", Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                    // save the Link Facility
                    val uuid = UUID.randomUUID().toString()
                    val partner = Partners()
                    partner.partnerID = uuid
                    partner.partnerName = partnerName

                    partner.contactPerson = editTextPartnerContactPerson.text.toString()
                    partner.contactPersonPhone = editTextPartnerContactPersonPhone.text.toString()
                    //partner.setParent("");
                    partner.mappingId = session.savedMapping.id
                    partner.country = session.userDetails[SessionManagement.KEY_USER_COUNTRY]!!
                    partner.comment = editTextComment.text.toString()
                    partner.isSynced = false
                    partner.isArchived = false
                    partner.dateAdded = Date().time
                    partner.addedBy = java.lang.Long.valueOf(session.userDetails[SessionManagement.KEY_USERID])
                    PartnersTable(context!!).addData(partner)

                    // clear select
                    partnersList.clear()
                    partnerNames.clear()
                    addPartners()
                    val adapter = ArrayAdapter(context!!,
                            android.R.layout.simple_spinner_item, partnerNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    selectPartner.adapter = adapter

                    //lets set the selected
                    var x = 0
                    for (e in partnersList) {
                        if (e.partnerID.equals(uuid, ignoreCase = true)) {
                            selectPartner.setSelection(x, true)
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
        val v = inflater.inflate(R.layout.fragment_new_partner_activity, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_RECRUITMENT
        if (backFragment == null) {
            MainActivity.backFragment = PartnerActivityFragment()
        } else {
            MainActivity.backFragment = backFragment
        }
        session = SessionManagement(context!!)
        user = session.userDetails
        country = user[SessionManagement.KEY_USER_COUNTRY]!!

        selectPartner = v.findViewById<View>(R.id.selectPartner) as Spinner
        partnerActivityComment = v.findViewById<View>(R.id.editComment) as EditText
        editIsDoingIccm = v.findViewById<View>(R.id.editIsDoingIccm) as RadioGroup
        editIsGivingFreeMedicine = v.findViewById<View>(R.id.editIsGivingFreeMedicine) as RadioGroup
        editIsGivingStipend = v.findViewById<View>(R.id.editIsGivingStipend) as RadioGroup
        editIsDoingMhealth = v.findViewById<View>(R.id.editIsDoingMhealth) as RadioGroup
        parentLayout = v.findViewById<View>(R.id.check_add_layout) as LinearLayout

        addPartners()

        val adapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, partnerNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectPartner.adapter = adapter
        selectPartner.onItemSelectedListener = onSelectedPartnerListener

        val iccmComponentTable = IccmComponentTable(context!!)
        iccmComponentList = iccmComponentTable.iccmComponentData
        for (component in iccmComponentList) {
            val checkBox = CheckBox(context)
            checkBox.id = component.id!!
            checkBox.text = component.componentName

            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.CENTER_VERTICAL


            val checkParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            checkParams.gravity = Gravity.CENTER_VERTICAL
            parentLayout.addView(checkBox, params)
        }

        setUpEditingMode()

        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)

        buttonSave = v.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener(this)


        return v
    }

    fun addPartners() {
        val partnersTable = PartnersTable(context!!)
        partnersList = partnersTable.partnersData as MutableList<Partners>
        for (partner in partnersList) {
            partnerNames.add(partner.partnerName)
        }
        if (partnerNames.size == 0) {
            partnerNames.add("  ")
        }
        partnerNames.add("Add New")
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
                val fragment = PartnerActivityFragment()
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
                if (editingPartnerActivity != null) {
                    id = editingPartnerActivity!!.id
                } else {
                    id = UUID.randomUUID().toString()
                }

                val partnerActivity = PartnerActivity()

                partnerActivity.id = id

                if (partnersList.size != 0) {
                    partnerActivity.partnerId = partnersList[selectPartner.selectedItemPosition].partnerID
                }

                partnerActivity.country = session.userDetails[SessionManagement.KEY_USER_COUNTRY]!!
                partnerActivity.county = session.savedMapping.county

                if (subCounty != null) {
                    partnerActivity.subcounty = subCounty!!.id
                    partnerActivity.county = subCounty!!.countyID
                }

                if (parish != null) {
                    partnerActivity.parish = parish!!.id
                }
                if (village != null) {
                    partnerActivity.village = village!!.id
                }

                if (communityUnit != null) {
                    partnerActivity.communityUnit = communityUnit!!.id
                    partnerActivity.subcounty = communityUnit!!.subCountyId
                }

                partnerActivity.mappingId = session.savedMapping.id
                partnerActivity.comment = partnerActivityComment.text.toString()


                partnerActivity.isDoingIccm = getSelectedRadioItemValue(editIsDoingIccm).equals("Yes", ignoreCase = true)
                partnerActivity.isGivingFreeDrugs = getSelectedRadioItemValue(editIsGivingFreeMedicine).equals("Yes", ignoreCase = true)
                partnerActivity.isGivingStipend = getSelectedRadioItemValue(editIsGivingStipend).equals("Yes", ignoreCase = true)
                partnerActivity.isDoingMhealth = getSelectedRadioItemValue(editIsDoingMhealth).equals("Yes", ignoreCase = true)
                partnerActivity.dateAdded = Date().time
                partnerActivity.addedBy = java.lang.Long.valueOf(session.userDetails[SessionManagement.KEY_USERID])


                val activities = JSONObject()
                var partnerActivities = ""

                partnerActivity.isSynced = false

                val listOfSelectedCheckBoxId = ArrayList<Int>()
                for (i in 0 until parentLayout.childCount) {
                    val checkbox = parentLayout.getChildAt(i) as CheckBox
                    try {
                        activities.put(checkbox.id.toString(), checkbox.isChecked)
                    } catch (e: Exception) {
                    }

                    if (checkbox.isChecked) {
                        listOfSelectedCheckBoxId.add(checkbox.id)
                    }
                }
                partnerActivities = activities.toString()
                partnerActivity.activities = partnerActivities

                val partnerActivityTable = PartnerActivityTable(context!!)
                val statusId = partnerActivityTable.addData(partnerActivity)
                if (statusId.equals(-1)) {
                    Toast.makeText(context, "Could not save the partner", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                    // Clear boxes
                    // Go back to Subcounty View...
                }
            }
        }
    }

    fun getSelectedRadioItemValue(radioGroup: RadioGroup): String {
        val selectedButton = radioGroup.checkedRadioButtonId
        val selectedRadioButton = radioGroup.findViewById<View>(selectedButton) as RadioButton
        return selectedRadioButton.text.toString()
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

    fun setUpEditingMode() {
        if (editingPartnerActivity != null) {
            var x = 0
            for (e in partnersList) {
                if (e.partnerID.equals(editingPartnerActivity!!.partnerId, ignoreCase = true)) {
                    selectPartner.setSelection(x, true)
                    break
                }
                x++
            }

            editIsDoingIccm.clearCheck()
            editIsDoingIccm.check(if (editingPartnerActivity!!.isDoingIccm)
                R.id.editIsDoingIccmYes
            else
                R.id.editIsDoingIccmNo)

            editIsGivingFreeMedicine.clearCheck()
            editIsGivingFreeMedicine.check(if (editingPartnerActivity!!.isGivingFreeDrugs)
                R.id.editIsGivingFreeMedicineYes
            else
                R.id.editIsGivingFreeMedicineNo)

            editIsGivingStipend.clearCheck()
            editIsGivingStipend.check(if (editingPartnerActivity!!.isGivingStipend)
                R.id.editIsGivingStipendYes
            else
                R.id.editIsGivingStipendNo)

            editIsDoingMhealth.clearCheck()
            editIsDoingMhealth.check(if (editingPartnerActivity!!.isDoingMhealth)
                R.id.editIsDoingMhealthYes
            else
                R.id.editIsDoingMhealthNo)

            try {
                val activities = JSONObject(editingPartnerActivity!!.activities)
                val keys = activities.keys()
                while (keys.hasNext()) {
                    val key = keys.next() as String
                    val iccm = Integer.valueOf(key)

                    Log.d("Tremap", iccm.toString() + " -- " + activities.getString(key))
                    val checkbox = parentLayout.findViewById<View>(iccm) as CheckBox
                    checkbox.isChecked = activities.getBoolean(key)
                }
            } catch (je: JSONException) {
            }

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
        fun newInstance(param1: String, param2: String): NewPartnerActivityFragment {
            val fragment = NewPartnerActivityFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
