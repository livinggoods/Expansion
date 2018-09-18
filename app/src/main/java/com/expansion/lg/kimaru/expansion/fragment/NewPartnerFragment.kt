package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/11/17.
 */

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.Partners
import com.expansion.lg.kimaru.expansion.tables.PartnersTable

import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewPartnerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewPartnerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewPartnerFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal  var editPartnerID: EditText? = null
    internal  lateinit var editPartnerName: EditText
    internal  lateinit var editContactPerson: EditText
    internal  lateinit var editContactPersonPhone: EditText
    internal var editParent: EditText? = null
    internal var editMappingId: EditText? = null
    internal var editCountry: EditText? = null
    internal  lateinit var editComment: EditText
    internal var editSynced: EditText? = null
    internal var editArchived: EditText? = null
    internal var editDateAdded: EditText? = null
    internal var editAddedBy: EditText? = null


    internal  lateinit var buttonSave: Button
    internal  lateinit var buttonList: Button
    var editingPartners: Partners? = null

    var createdFromRecruitment: Boolean? = false


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0

    internal  lateinit var session: SessionManagement
    internal lateinit  var user: HashMap<String, String>
    internal  lateinit var country: String

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
        val v = inflater.inflate(R.layout.fragment_new_partner, container, false)
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_RECRUITMENT
        MainActivity.backFragment = PartnersFragment()


        session = SessionManagement(context!!)
        user = session.userDetails
        country = user[SessionManagement.KEY_USER_COUNTRY]!!

        editPartnerName = v.findViewById<View>(R.id.editPartnerName) as EditText
        editContactPerson = v.findViewById<View>(R.id.editContactPerson) as EditText
        editContactPersonPhone = v.findViewById<View>(R.id.editContactPersonPhone) as EditText
        // editParent = (EditText) v.findViewById(R.id.editParent);
        // editMappingId = (EditText) v.findViewById(R.id.editMappingId);
        // editCountry = (EditText) v.findViewById(R.id.editCountry);
        editComment = v.findViewById<View>(R.id.editComment) as EditText

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
                val fragment = PartnersFragment()
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
                if (editingPartners != null) {
                    id = editingPartners!!.partnerID
                } else {
                    id = UUID.randomUUID().toString()
                }
                val partnerName = editPartnerName.text.toString()
                val partnerPhone = editContactPerson.text.toString()
                val partnerContactPerson = editContactPersonPhone.text.toString()
                val comment = editComment.text.toString()


                if (partnerPhone.trim { it <= ' ' } != "") {
                    if (partnerPhone.trim { it <= ' ' }.startsWith("+")) {
                        if (partnerPhone.length != 13) {
                            Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                            editContactPersonPhone.requestFocus()
                            return
                        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(partnerPhone)) {
                            editContactPersonPhone.requestFocus()
                            Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                            return
                        }
                    } else if (partnerPhone.length != 10) {
                        editContactPersonPhone.requestFocus()
                        Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                        return
                    } else if (!PhoneNumberUtils.isGlobalPhoneNumber(partnerPhone)) {
                        Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                        return
                    }
                }

                // Do some validations
                if (partnerName.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Name cannot be blank", Toast.LENGTH_SHORT).show()
                    editPartnerName.requestFocus()
                    return
                }

                val partner = Partners()
                partner.partnerID = id
                partner.partnerName = partnerName
                partner.contactPerson = partnerContactPerson
                partner.contactPersonPhone = partnerPhone
                partner.parent = ""
                partner.mappingId = session.savedMapping.id
                partner.country = country
                partner.comment = comment
                partner.isSynced = false
                partner.isArchived = false
                partner.dateAdded = currentDate
                partner.addedBy = java.lang.Long.valueOf(user[SessionManagement.KEY_USERID])


                val partnersTable = PartnersTable(context!!)
                val statusId = partnersTable.addData(partner)

                if (statusId.toInt() == -1) {
                    Toast.makeText(context, "Could not save the partner", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                    // Clear boxes
                    editPartnerName.setText("")
                    editContactPerson.setText("")
                    editContactPersonPhone.setText("")
                    editComment.setText("")
                }
            }
        }
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
        if (editingPartners != null) {
            editPartnerName.setText(editingPartners!!.partnerName)
            editContactPerson.setText(editingPartners!!.contactPerson)
            editContactPersonPhone.setText(editingPartners!!.contactPersonPhone)
            editComment.setText(editingPartners!!.comment)
            editPartnerName.requestFocus()
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
        fun newInstance(param1: String, param2: String): NewPartnerFragment {
            val fragment = NewPartnerFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
