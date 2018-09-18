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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.Parish
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.tables.ParishTable
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable

import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewParishFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewParishFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewParishFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal  lateinit var mName: EditText
    internal  lateinit var mContactPerson: EditText
    internal  lateinit var mContactPersonPhone: EditText
    internal  lateinit var mComment: EditText


    internal  lateinit var buttonSave: Button
    internal  lateinit var buttonList: Button
    var editingParish: Parish? = null


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    internal  lateinit var session: SessionManagement
    internal  lateinit var user: HashMap<String, String>

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
        val v = inflater.inflate(R.layout.fragment_new_parish, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_RECRUITMENT
        MainActivity.backFragment = MapViewFragment()
        session = SessionManagement(context!!)
        user = session.userDetails

        //Initialize the UI Components
        mName = v.findViewById<View>(R.id.editParishName) as EditText
        mContactPerson = v.findViewById<View>(R.id.editContactPerson) as EditText
        mContactPersonPhone = v.findViewById<View>(R.id.editContactPersonPhone) as EditText
        mComment = v.findViewById<View>(R.id.editComment) as EditText

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

                val fragment = MapViewFragment()
                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT)
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.buttonSave -> {

                // set date as integers
                Toast.makeText(context, "Validating and saving", Toast.LENGTH_SHORT).show()


                // Generate the uuid
                val id: String
                if (editingParish != null) {
                    id = editingParish!!.id
                } else {
                    id = UUID.randomUUID().toString()
                }
                val parishName = mName.text.toString()
                val contactPerson = mContactPerson.text.toString()
                val contactPersonPhone = mContactPersonPhone.text.toString()
                val comment = mComment.text.toString()


                val mapping = session.savedMapping.id
                val parent = session.savedMapping.subCounty
                val country = user[SessionManagement.KEY_USER_COUNTRY]
                val addedBy = Integer.parseInt(user[SessionManagement.KEY_USERID])
                val dateAdded = Date().time
                val synced = 0


                // Do some validations
                if (parishName.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Name cannot be blank", Toast.LENGTH_SHORT).show()
                    mName.requestFocus()
                    return
                }

                // Save Parish
                val parish = Parish(id, parishName, contactPerson, contactPersonPhone,
                        parent, mapping, dateAdded, addedBy, synced, country!!, comment)

                val parishTable = ParishTable(context!!)
                val statusId = parishTable.addData(parish)

                if (statusId.toInt() == -1) {
                    Toast.makeText(context, "Could not save recruitment", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                    // Clear boxes
                    mName.setText("")
                    mContactPerson.setText("")
                    mContactPersonPhone.setText("")
                    mComment.setText("")
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
        if (editingParish != null) {
            mName.setText(editingParish!!.name)
            mContactPerson.setText(editingParish!!.contactPerson)
            mContactPersonPhone.setText(editingParish!!.contactPersonPhone)
            mComment.setText(editingParish!!.comment)
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
        fun newInstance(param1: String, param2: String): NewParishFragment {
            val fragment = NewParishFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
