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
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.KeCounty
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable
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
 * [NewKeMappingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewKeMappingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewKeMappingFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal  lateinit var mMappingName: EditText
    internal  lateinit var mMappingContactPerson: EditText
    internal lateinit  var mMappingContactPersonPhone: EditText
    internal  lateinit var mComment: EditText
    internal  lateinit var selectCounty: Spinner

    internal  lateinit var buttonSave: Button
    internal  lateinit var buttonList: Button


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    internal  lateinit var session: SessionManagement
    internal lateinit  var user: HashMap<String, String>

    internal var keCountyList: List<KeCounty> = ArrayList()
    internal var counties: MutableList<String> = ArrayList()
    internal var editingMapping: Mapping? = null

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
        val v = inflater.inflate(R.layout.fragment_new_ke_mapping, container, false)
        session = SessionManagement(context!!)
        //Initialize the UI Components
        // mMappingName, mMappingContactPerson, mMappingContactPersonPhone, mCounty, mComment;

        user = session.userDetails
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_MAPPING
        MainActivity.backFragment = MappingFragment()



        mMappingName = v.findViewById<View>(R.id.editMappingName) as EditText
        mMappingContactPerson = v.findViewById<View>(R.id.editContactPerson) as EditText
        mMappingContactPersonPhone = v.findViewById<View>(R.id.editContactPersonPhone) as EditText
        selectCounty = v.findViewById<View>(R.id.selectCounty) as Spinner
        mComment = v.findViewById<View>(R.id.editComment) as EditText

        val keCountyTable = KeCountyTable(context!!)
        keCountyList = keCountyTable.getCounties()
        for (k in keCountyList) {
            counties.add(k.countyName)
        }
        val adapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, counties)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectCounty.adapter = adapter

        if (editingMapping != null) {
            mMappingName.setText(editingMapping!!.mappingName)
            mMappingContactPerson.setText(editingMapping!!.contactPerson)
            mMappingContactPersonPhone.setText(editingMapping!!.contactPersonPhone)
            mComment.setText(editingMapping!!.comment)
            var x = 0
            for (c in keCountyList) {
                if (c.id == Integer.valueOf(editingMapping!!.county)) {
                    selectCounty.setSelection(x, true)
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
                if (editingMapping != null) {
                    id = editingMapping!!.id
                } else {
                    id = UUID.randomUUID().toString()
                }

                val mappingName = mMappingName.text.toString()
                val mappingCounty = keCountyList[selectCounty.selectedItemPosition].id.toString()
                val contactPerson = mMappingContactPerson.text.toString()
                val contactPersonPhone = mMappingContactPersonPhone.text.toString()
                val comment = mComment.text.toString()
                val country = user[SessionManagement.KEY_USER_COUNTRY]
                val applicantAddedBy = Integer.parseInt(user[SessionManagement.KEY_USERID])
                val sync = false


                // Do some validations
                if (mappingName.trim { it <= ' ' } == "") {
                    mMappingName.requestFocus()
                    Toast.makeText(context, "Enter name of the mapping", Toast.LENGTH_SHORT).show()
                    return
                }
                // Save Exam Details
                val mapping = Mapping(id, mappingName, country!!, mappingCounty, currentDate,
                        applicantAddedBy, contactPerson, contactPersonPhone, sync, comment, "", "", "")

                val mappingTable = MappingTable(context!!)
                val createdMap = mappingTable.addData(mapping)
                // Clear boxes
                mMappingContactPerson.setText("")
                mMappingContactPersonPhone.setText("")
                mMappingName.setText("")
                mComment.setText("")
                selectCounty.setSelection(0)
                mMappingName.requestFocus()

                session.saveMapping(mapping)
                fragment = MapKeViewFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, "mappings")
                fragmentTransaction.commitAllowingStateLoss()
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
        fun newInstance(param1: String, param2: String): NewKeMappingFragment {
            val fragment = NewKeMappingFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
