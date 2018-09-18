package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
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
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewRecruitmentFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewRecruitmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewRecruitmentFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit var mName: EditText
    internal lateinit  var mComment: EditText
    internal lateinit  var mRegion: Spinner
    internal lateinit  var mDistrict: Spinner
    internal  lateinit var mCounty: Spinner
    internal lateinit  var mSubCounty: Spinner


    internal  lateinit var buttonSave: Button
    internal  lateinit var buttonList: Button
    var editingRecruitment: Recruitment? = null


    internal var regions: List<CountyLocation> = ArrayList()
    internal var listRegions: MutableList<String> = ArrayList()

    internal var districts: MutableList<CountyLocation> = ArrayList()
    internal var listDistricts: MutableList<String> = ArrayList()

    internal var counties: MutableList<CountyLocation> = ArrayList()
    internal var listCounties: MutableList<String> = ArrayList()

    internal var subCounties: MutableList<CountyLocation> = ArrayList()
    internal var listSubCounties: MutableList<String> = ArrayList()
    internal  lateinit var subCountyAdapter: ArrayAdapter<String>

    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    internal  lateinit var session: SessionManagement
    internal lateinit  var user: HashMap<String, String>

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
        val v = inflater.inflate(R.layout.fragment_new_recruitment, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_RECRUITMENT
        MainActivity.backFragment = RecruitmentsFragment()
        val countyLocationTable = CountyLocationTable(context!!)
        session = SessionManagement(context!!)
        user = session.userDetails
        //Initialize the UI Components
        mName = v.findViewById<View>(R.id.editRecruitmentName) as EditText
        mComment = v.findViewById<View>(R.id.editComment) as EditText
        //region
        mRegion = v.findViewById<View>(R.id.selectRegion) as Spinner
        regions = countyLocationTable.regions
        for (r in regions) {
            listRegions.add(r.name)
        }
        val regionAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, listRegions)
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mRegion.adapter = regionAdapter
        mRegion.onItemSelectedListener = onRegionSelectedListener


        mDistrict = v.findViewById<View>(R.id.selectDistrict) as Spinner
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



        mCounty = v.findViewById<View>(R.id.selectCounty) as Spinner
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
        mSubCounty = v.findViewById<View>(R.id.selectSubCounty) as Spinner
        subCountyAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, listSubCounties)
        subCountyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSubCounty.adapter = subCountyAdapter


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
            R.id.buttonList -> {

                val fragment = RecruitmentsFragment()
                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.buttonSave -> {

                // set date as integers
                Toast.makeText(context, "Validating and saving", Toast.LENGTH_SHORT).show()
                val currentDate = Date().time

                // Generate the uuid
                val id: String
                if (editingRecruitment != null) {
                    id = editingRecruitment!!.id
                } else {
                    id = UUID.randomUUID().toString()
                }
                val recruitmentName = mName.text.toString()
                val recruitmentRegion = regions[mRegion.selectedItemPosition].id.toString()
                val recruitmentDistrict = districts[mDistrict
                        .selectedItemPosition].id.toString()
                val recruitmentCounty = counties[mCounty.selectedItemPosition].id.toString()
                val recruitmentSubCounty = subCounties[mSubCounty.selectedItemPosition].id.toString()
                val recruitmentComment = mComment.text.toString()
                val recruitmentLat = ""
                val recruitmentLon = ""
                val country = user[SessionManagement.KEY_USER_COUNTRY]

                val recruitmentAddedBy = Integer.parseInt(user[SessionManagement.KEY_USERID])
                val recruitmentSync = 0


                // Do some validations
                if (recruitmentName.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Name cannot be blank", Toast.LENGTH_SHORT).show()
                    mName.requestFocus()
                    return
                }

                if (recruitmentDistrict.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "District is required", Toast.LENGTH_SHORT).show()
                    mDistrict.requestFocus()
                    return
                }
                // Save Recruitment
                val recruitment = Recruitment()
                recruitment.id = id
                recruitment.name = recruitmentName
                recruitment.regionId = recruitmentRegion
                recruitment.district = recruitmentDistrict
                recruitment.county = recruitmentCounty
                try {
                    recruitment.countyId = Integer.parseInt(recruitmentCounty)
                } catch (e: Exception) {
                }

                recruitment.subcounty = recruitmentSubCounty
                recruitment.lat = recruitmentLat
                recruitment.lon = recruitmentLon
                recruitment.comment = recruitmentComment
                recruitment.addedBy = recruitmentAddedBy
                recruitment.dateAdded = currentDate
                recruitment.synced = recruitmentSync
                recruitment.country = country!!
                try {
                    recruitment.locationId = Integer.parseInt(recruitmentRegion)
                } catch (e: Exception) {
                }

                recruitment.subCountyId = recruitmentSubCounty


                val recruitmentTable = RecruitmentTable(context!!)
                val statusId = recruitmentTable.addData(recruitment)

                if (statusId.equals(-1)) {
                    Toast.makeText(context, "Could not save recruitment", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                    // Clear boxes
                    mName.setText("")
                    mDistrict.setSelection(0)
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
        if (editingRecruitment != null) {
            mName.setText(editingRecruitment!!.name)
            mComment.setText(editingRecruitment!!.comment)
            var x = 0
            for (c in regions) {
                if (!editingRecruitment!!.regionId.equals("", ignoreCase = true)) {
                    if (c.id == Integer.valueOf(editingRecruitment!!.regionId)) {
                        mRegion.setSelection(x, true)
                        break
                    }
                }
                x++
            }

            x = 0
            for (d in districts) {
                if (!editingRecruitment!!.district.equals("", ignoreCase = true)) {
                    if (d.id == Integer.valueOf(editingRecruitment!!.district)) {
                        mDistrict.setSelection(x, true)
                        break
                    }
                }
                x++
            }

            x = 0
            for (c in counties) {
                if (c.id == Integer.valueOf(editingRecruitment!!.countyId!!)) {
                    mCounty.setSelection(x, true)
                    break
                }
                x++
            }

            x = 0
            for (c in subCounties) {
                if (c.id == Integer.valueOf(editingRecruitment!!.subCountyId)) {
                    mSubCounty.setSelection(x, true)
                    break
                }
                x++
            }

            mName.requestFocus()
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
        fun newInstance(param1: String, param2: String): NewRecruitmentFragment {
            val fragment = NewRecruitmentFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
