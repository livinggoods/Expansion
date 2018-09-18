package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.KeCounty
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.sync.IccmDataSync
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable

import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewKeRecruitmentFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewKeRecruitmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewKeRecruitmentFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit  var mName: EditText
    internal  lateinit var mCounty: Spinner
    internal  lateinit var mSubCounty: Spinner

    internal var keCountyList: List<KeCounty> = ArrayList()
    internal var keCounties: MutableList<String> = ArrayList()

    internal var subCountyList: MutableList<SubCounty> = ArrayList()
    internal var subCounties: MutableList<String> = ArrayList()


    internal lateinit  var buttonSave: Button
    internal  lateinit var buttonList: Button
    var editingRecruitment: Recruitment? = null


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    internal lateinit  var session: SessionManagement
    internal  lateinit var user: HashMap<String, String>

    internal  lateinit var subCountyName: String
    internal  lateinit var subCountyContactPerson: String
    internal lateinit  var subCountyContactPhone: String

    internal var onSelectedCountyListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // get subcounties
            subCountyList.clear()
            subCounties.clear()
            val scTable = SubCountyTable(context!!)
            subCountyList = scTable.getSubCountiesByCounty(keCountyList[position].id) as MutableList<SubCounty>
            for (s in subCountyList) {
                subCounties.add(s.subCountyName)
            }
            subCounties.add("Add New")

            val adapter = ArrayAdapter(context!!,
                    android.R.layout.simple_spinner_item, subCounties)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mSubCounty.adapter = adapter
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }

    internal var onSelectedSubCountyListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            if (subCounties[position].equals("Add New", ignoreCase = true)) {
                // Show Dialog to add the Referral
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Add New Sub County")

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                // Context context = mapView.getContext();
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL

                val subCName = EditText(context)
                subCName.hint = "Sub County Name"
                subCName.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                layout.addView(subCName)

                val refName = EditText(context)
                refName.hint = "Contact Person Name"
                refName.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                layout.addView(refName)

                val refPhone = EditText(context)
                refPhone.hint = "Contact Person Phone"
                refPhone.inputType = InputType.TYPE_CLASS_PHONE
                layout.addView(refPhone)

                builder.setView(layout)

                // Set up the buttons
                builder.setPositiveButton("OK") { dialog, which ->
                    subCountyName = subCName.text.toString()
                    subCountyContactPerson = refName.text.toString()
                    subCountyContactPhone = refPhone.text.toString()
                    // we save the referral, refresh the list and rebind the Spinner, and set selected
                    val uuid = UUID.randomUUID().toString()
                    val subCounty = SubCounty()
                    subCounty.id = uuid
                    subCounty.contactPersonPhone = subCountyContactPerson
                    subCounty.subCountyName = subCountyName
                    subCounty.contactPerson = subCountyContactPerson
                    subCounty.countyID = keCountyList[mCounty.selectedItemPosition].id.toString()

                    val scTbl = SubCountyTable(context!!)
                    scTbl.addData(subCounty)

                    // clear subcounties

                    subCountyList.clear()
                    subCounties.clear()

                    subCountyList = scTbl.getSubCountiesByCounty(keCountyList[mCounty.selectedItemPosition].id) as MutableList<SubCounty>
                    for (s in subCountyList) {
                        subCounties.add(s.subCountyName)
                    }
                    subCounties.add("Add New")

                    val adapter = ArrayAdapter(context!!,
                            android.R.layout.simple_spinner_item, subCounties)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    mSubCounty.adapter = adapter

                    //lets set the selected
                    var x = 0
                    for (s in subCountyList) {
                        if (s.id.equals(uuid, ignoreCase = true)) {
                            mSubCounty.setSelection(x, true)
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
        val v = inflater.inflate(R.layout.fragment_new_ke_recruitment, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_RECRUITMENT
        MainActivity.backFragment = RecruitmentsFragment()
        session = SessionManagement(context!!)
        user = session.userDetails
        //Initialize the UI Components
        mName = v.findViewById<View>(R.id.editRecruitmentName) as EditText
        // mCounty = (EditText) v.findViewById(R.id.editRecruitmentCounty);
        // mSubCounty = (EditText) v.findViewById(R.id.editRecruitmentSubCounty);

        mCounty = v.findViewById<View>(R.id.selectCounty) as Spinner
        mSubCounty = v.findViewById<View>(R.id.selectSubCounty) as Spinner

        keCountyList = KeCountyTable(context!!).getCounties()
        for (k in keCountyList) {
            keCounties.add(k.countyName)
        }

        // check if we have subCounties and Wards
        val adapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, keCounties)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCounty.adapter = adapter
        mCounty.onItemSelectedListener = onSelectedCountyListener
        mSubCounty.onItemSelectedListener = onSelectedSubCountyListener

        // Subcounties

        //in case we are editing
        setUpEditingMode()

        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)

        buttonSave = v.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener(this)

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
            R.id.buttonSave -> {

                // set date as integers
                val currentDate = Date().time

                // Generate the uuid
                val id: String
                if (editingRecruitment != null) {
                    id = editingRecruitment!!.id
                } else {
                    id = UUID.randomUUID().toString()
                }
                if (mSubCounty.selectedItemPosition > subCountyList.size - 1) {
                    Toast.makeText(context, "Please select a valid Sub County", Toast.LENGTH_LONG).show()
                    return
                }
                val recruitmentName = mName.text.toString()
                val recruitmentDistrict = ""
                val recruitmentDivision = ""
                val recruitmentSubCounty = subCountyList[mSubCounty.selectedItemPosition].id
                val recruitmentCounty = keCountyList[mCounty.selectedItemPosition].id
                val recruitmentComment = ""
                val recruitmentLat = ""
                val recruitmentLon = ""
                val country = user[SessionManagement.KEY_USER_COUNTRY]

                val recruitmentAddedBy = Integer.parseInt(user[SessionManagement.KEY_USERID])
                val recruitmentSync = 0


                // Do some validations
                if (recruitmentName.trim { it <= ' ' } == "") {
                    mName.requestFocus()
                    Toast.makeText(context, "Name cannot be blank", Toast.LENGTH_SHORT).show()
                    return
                }

                // Save Recruitment
                val recruitment = Recruitment()
                recruitment.id = id
                recruitment.name = recruitmentName
                recruitment.district = recruitmentDistrict
                recruitment.subcounty = recruitmentSubCounty
                recruitment.division = recruitmentDivision
                recruitment.lat = recruitmentLat
                recruitment.lon = recruitmentLon
                recruitment.comment = recruitmentComment
                recruitment.addedBy = recruitmentAddedBy
                recruitment.dateAdded = currentDate
                recruitment.synced = recruitmentSync
                recruitment.country = country!!
                recruitment.county = recruitmentCounty.toString()
                recruitment.countyId = recruitmentCounty
                recruitment.subCountyId = recruitmentSubCounty
                val recruitmentTable = RecruitmentTable(context!!)
                val statusId = recruitmentTable.addData(recruitment)

                if (statusId.equals(-1)) {
                    Toast.makeText(context, "Could not save recruitment", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                    // Clear boxes
                    mName.setText("")
                    mCounty.setSelection(0)
                    mSubCounty.setSelection(0)

                    //set Focus
                    mName.requestFocus()

                    session.saveRecruitment(recruitment)
                    val fragment = RecruitmentViewFragment()
                    val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, fragment, "villages")
                    fragmentTransaction.commitAllowingStateLoss()
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
        if (editingRecruitment != null) {
            mName.setText(editingRecruitment!!.name)
            var x = 0
            for (c in keCountyList) {
                if (c.id == Integer.valueOf(editingRecruitment!!.county)) {
                    mCounty.setSelection(x, true)
                    break
                }
                x++
            }
            x = 0
            for (sc in subCountyList) {
                if (sc.id.equals(editingRecruitment!!.subcounty, ignoreCase = true)) {
                    mSubCounty.setSelection(x, true)
                    break
                }
                x++
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
        fun newInstance(param1: String, param2: String): NewKeRecruitmentFragment {
            val fragment = NewKeRecruitmentFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
