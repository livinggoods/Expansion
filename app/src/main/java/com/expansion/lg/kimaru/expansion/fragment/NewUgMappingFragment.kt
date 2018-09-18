package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import com.expansion.lg.kimaru.expansion.tables.MappingTable

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewUgMappingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewUgMappingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewUgMappingFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit var mMappingName: EditText
    internal lateinit var mMappingContactPerson: EditText
    internal lateinit var mMappingContactPersonPhone: EditText
    internal lateinit var mComment: EditText
    internal lateinit var mRegion: Spinner
    internal lateinit var mDistrict: Spinner
    internal lateinit var mCounty: Spinner
    internal lateinit var mSubCounty: Spinner


    internal lateinit var buttonSave: Button
    internal lateinit var buttonList: Button


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    internal lateinit var session: SessionManagement
    internal lateinit var user: HashMap<String, String>

    internal var regions: List<CountyLocation> = ArrayList()
    internal var listRegions: MutableList<String> = ArrayList()

    internal var districts: MutableList<CountyLocation> = ArrayList()
    internal var listDistricts: MutableList<String> = ArrayList()

    internal var counties: MutableList<CountyLocation> = ArrayList()
    internal var listCounties: MutableList<String> = ArrayList()

    internal var subCounties: MutableList<CountyLocation> = ArrayList()
    internal var listSubCounties: MutableList<String> = ArrayList()
    internal lateinit var subCountyAdapter: ArrayAdapter<String>


    internal var editingMapping: Mapping? = null

    internal var onRegionSelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // with the ID selected, we will populate the district
            val countyLocationTable = CountyLocationTable(context!!)
            listDistricts.clear()
            districts.clear()
            districts = countyLocationTable.getChildrenLocations(regions[position]) as MutableList<CountyLocation>
            for (district in districts) {
                listDistricts.add(district.name)
            }
            val adapter = ArrayAdapter(context!!,
                    android.R.layout.simple_spinner_item, listDistricts)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mDistrict.adapter = adapter
            mDistrict.onItemSelectedListener = onDistrictSelectedListener
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }


    internal var onDistrictSelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // with the ID selected, we will populate the district
            val countyLocationTable = CountyLocationTable(context!!)
            listCounties.clear()
            counties.clear()
            counties = countyLocationTable.getChildrenLocations(districts[position]) as MutableList<CountyLocation>
            for (district in counties) {
                listCounties.add(district.name)
            }
            val adapter = ArrayAdapter(context!!,
                    android.R.layout.simple_spinner_item, listCounties)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mCounty.adapter = adapter
            mCounty.onItemSelectedListener = onCountySelectedListener
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }


    internal var onCountySelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // with the ID selected, we will populate the subcounty
            val countyLocationTable = CountyLocationTable(context!!)
            listSubCounties.clear()
            subCounties.clear()
            subCounties = countyLocationTable.getChildrenLocations(counties[position]) as MutableList<CountyLocation>
            for (subCounty in subCounties) {
                listSubCounties.add(subCounty.name)
            }
            val adapter = ArrayAdapter(context!!,
                    android.R.layout.simple_spinner_item, listSubCounties)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mSubCounty.adapter = adapter
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
        val v = inflater.inflate(R.layout.fragment_new_mapping, container, false)
        session = SessionManagement(context!!)
        //Initialize the UI Components
        // mMappingName, mMappingContactPerson, mMappingContactPersonPhone, mCounty, mComment;

        user = session.userDetails
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_MAPPING
        MainActivity.backFragment = MappingFragment()
        val countyLocationTable = CountyLocationTable(context!!)
        mRegion = v.findViewById<View>(R.id.editRegion) as Spinner
        regions = countyLocationTable.regions
        for (r in regions) {
            listRegions.add(r.name)
        }
        val regionAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, listRegions)
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mRegion.adapter = regionAdapter
        mRegion.onItemSelectedListener = onRegionSelectedListener


        mDistrict = v.findViewById<View>(R.id.editDistrict) as Spinner
        try {
            districts.clear()
            listDistricts.clear()
            districts = countyLocationTable.getChildrenLocations(regions[mRegion.selectedItemPosition]) as MutableList<CountyLocation>
        } catch (e: Exception) {
        }

        for (district in districts) {
            listDistricts.add(district.name)
        }
        mDistrict.onItemSelectedListener = onDistrictSelectedListener



        mCounty = v.findViewById<View>(R.id.editCounty) as Spinner
        //populate the Counties
        counties = countyLocationTable.countiesAndDistricts as MutableList<CountyLocation>
        for (location in counties) {
            listCounties.add(location.name)
        }
        val adapter0 = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, listCounties)
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCounty.adapter = adapter0
        mCounty.onItemSelectedListener = onCountySelectedListener
        //counties.get(position).getName();; get postion, then extract item at pos
        try {
            subCounties = countyLocationTable.getChildrenLocations(counties[mCounty.selectedItemPosition]) as MutableList<CountyLocation>
        } catch (e: Exception) {
        }

        for (subCounty in subCounties) {
            listSubCounties.add(subCounty.name)
        }
        mSubCounty = v.findViewById<View>(R.id.editSubCounty) as Spinner
        subCountyAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, listSubCounties)
        subCountyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSubCounty.adapter = subCountyAdapter


        //initialize the array adapter


        mMappingName = v.findViewById<View>(R.id.editMappingName) as EditText
        mMappingContactPerson = v.findViewById<View>(R.id.editContactPerson) as EditText
        mMappingContactPersonPhone = v.findViewById<View>(R.id.editContactPersonPhone) as EditText

        mComment = v.findViewById<View>(R.id.editComment) as EditText

        if (editingMapping != null) {
            mMappingName.setText(editingMapping!!.mappingName)
            mMappingContactPerson.setText(editingMapping!!.contactPerson)
            mMappingContactPersonPhone.setText(editingMapping!!.contactPersonPhone)
            mComment.setText(editingMapping!!.comment)
            var x = 0
            for (county in counties) {
                if (county.id == Integer.valueOf(editingMapping!!.county)) {
                    mCounty.setSelection(x, true)
                    break
                }
                x++
            }
            x = 0
            for (subCounty in subCounties) {
                if (subCounty.id == Integer.valueOf(editingMapping!!.subCounty)) {
                    mSubCounty.setSelection(x, true)
                    break
                }
                x++
            }
        }

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
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        when (view.id) {
            R.id.editDob -> {
                val newFragment = DatePickerFragment.newInstance(R.id.editDob)
                newFragment.show(fragmentManager!!, "DatePicker")
            }

            R.id.buttonSave -> {
                // set date as integers
                val dateFormat = SimpleDateFormat("yyyy/MM/dd")

                Toast.makeText(context, "Validating and saving", Toast.LENGTH_SHORT).show()
                val currentDate = Date().time

                val id: String
                if (editingMapping == null) {
                    id = UUID.randomUUID().toString()
                } else {
                    id = editingMapping!!.id
                }
                val contactPersonPhone = mMappingContactPersonPhone.text.toString()
                if (!isValidPhone(contactPersonPhone)) {
                    Toast.makeText(context, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
                    mMappingContactPersonPhone.requestFocus()
                    return
                }
                val mappingName = mMappingName.text.toString()
                val mappingRegion = regions[mRegion.selectedItemPosition].id.toString()
                val mappingDistrict = districts[mDistrict.selectedItemPosition].id.toString()
                val mappingCounty = counties[mCounty.selectedItemPosition].id.toString()
                val subCounty = subCounties[mSubCounty.selectedItemPosition].id.toString()
                val contactPerson = mMappingContactPerson.text.toString()

                val comment = mComment.text.toString()
                val applicantAddedBy = Integer.parseInt(user[SessionManagement.KEY_USERID])
                val sync = false


                // Do some validations
                if (mappingName.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Enter name of the mapping", Toast.LENGTH_SHORT).show()

                } else if (mappingCounty.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Enter the county or the area", Toast.LENGTH_SHORT).show()
                } else {
                    // Save Exam Details
                    val mapping = Mapping(id, mappingName, "UG", mappingCounty,
                            currentDate, applicantAddedBy, contactPerson, contactPersonPhone, sync,
                            comment, mappingDistrict, subCounty, mappingRegion)

                    val mappingTable = MappingTable(context!!)
                    val createdMap = mappingTable.addData(mapping)
                    // Clear boxes
                    mMappingContactPerson.setText("")
                    mMappingContactPersonPhone.setText("")
                    mMappingName.setText("")
                    mComment.setText("")
                    mMappingName.requestFocus()

                    session.saveMapping(mapping)
                    fragment = MapViewFragment()
                    fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, fragment, "mappings")
                    fragmentTransaction.commitAllowingStateLoss()

                }
            }
            R.id.buttonList -> {
                fragment = MappingFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "mappings")
                fragmentTransaction.commitAllowingStateLoss()
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
        fun newInstance(param1: String, param2: String): NewUgMappingFragment {
            val fragment = NewUgMappingFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
