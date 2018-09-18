package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context
import android.net.Uri
import android.os.Bundle
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
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization
import com.expansion.lg.kimaru.expansion.mzigos.Parish
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable
import com.expansion.lg.kimaru.expansion.tables.ParishTable

import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewMobilizationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewMobilizationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewMobilizationFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit  var editName: EditText
    internal lateinit  var editComment: EditText
    internal lateinit  var selectParish: Spinner


    internal lateinit  var buttonSave: Button
    internal  lateinit var buttonList: Button
    var editingMobilization: Mobilization? = null

    internal var parishList: List<Parish> = ArrayList()
    internal var parishes: MutableList<String> = ArrayList()


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    internal lateinit  var session: SessionManagement
    internal lateinit  var user: HashMap<String, String>

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
        val v = inflater.inflate(R.layout.new_mobilization, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_RECRUITMENT
        MainActivity.backFragment = MapViewFragment()
        session = SessionManagement(context!!)
        user = session.userDetails
        editName = v.findViewById<View>(R.id.editName) as EditText
        editComment = v.findViewById<View>(R.id.editComment) as EditText
        selectParish = v.findViewById<View>(R.id.selectParish) as Spinner

        addParishes()
        val parishesAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, parishes)
        parishesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectParish.adapter = parishesAdapter

        setUpEditingMode()
        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)

        buttonSave = v.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener(this)

        return v
    }

    fun addParishes() {
        val parishTable = ParishTable(context!!)
        //communityUnits communityUnitList
        parishList = parishTable.getParishByMapping(session
                .savedMapping.id)
        for (p in parishList) {
            parishes.add(p.name)
        }
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
            R.id.buttonList -> {
                fragment = MapViewFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
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
                if (editingMobilization != null) {
                    id = editingMobilization!!.id
                } else {
                    id = UUID.randomUUID().toString()
                }
                val name = editName.text.toString()
                val comment = editComment.text.toString()

                val mappingId = session.savedMapping.id
                // String county = session.getSavedMapping().getCounty(); // only for Kenya
                val country = session.userDetails[SessionManagement.KEY_USER_COUNTRY]
                val district = session.savedMapping.district
                val subCounty = session.savedMapping.subCounty


                val mobilization = Mobilization()
                mobilization.id = id
                mobilization.name = name
                mobilization.mappingId = mappingId
                mobilization.country = country!!
                mobilization.addedBy = Integer.valueOf(user[SessionManagement.KEY_USERID])
                mobilization.comment = comment
                mobilization.dateAdded = currentDate
                mobilization.synced = false
                mobilization.district = district
                mobilization.subCounty = subCounty
                mobilization.parish = parishList[selectParish.selectedItemPosition].id

                // Do some validations
                if (editName.toString().trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Name cannot be blank", Toast.LENGTH_SHORT).show()
                    editComment.requestFocus()
                    return
                }

                // Save the Mobilization
                val mobilizationTable = MobilizationTable(context!!)
                val statusId = mobilizationTable.addData(mobilization)

                if (statusId.toInt() == -1) {
                    Toast.makeText(context, "Could not save the mobilization", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
                    fragment = MapViewFragment()
                    fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
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
        if (editingMobilization != null) {
            editName.setText(editingMobilization!!.name)
            editComment.setText(editingMobilization!!.comment)
            var x = 0
            for (p in parishList) {
                if (p.id.equals(editingMobilization!!.parish, ignoreCase = true)) {
                    selectParish.setSelection(x, true)
                    break
                }
                x++
            }
            editName.requestFocus()
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
        fun newInstance(param1: String, param2: String): NewMobilizationFragment {
            val fragment = NewMobilizationFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
