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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.Exam
import com.expansion.lg.kimaru.expansion.tables.ExamTable

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewExamFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewExamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewExamFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit var mMaths: EditText
    internal  lateinit var mEnglish: EditText
    internal  lateinit var mSelfAssessment: EditText
    internal  lateinit var editApplicantDetails: TextView

    internal  lateinit var buttonSave: Button
    internal  lateinit var buttonList: Button

    internal var editingExam: Exam? = null


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0


    internal lateinit  var sessionManagement: SessionManagement
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
        val v = inflater.inflate(R.layout.fragment_new_exam, container, false)
        //Initialize the UI Components
        mMaths = v.findViewById<View>(R.id.editMathScore) as EditText
        mEnglish = v.findViewById<View>(R.id.editEnglishScore) as EditText
        mSelfAssessment = v.findViewById<View>(R.id.editSelfAssessmentScore) as EditText
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_EXAM
        MainActivity.backFragment = RegistrationViewFragment()
        sessionManagement = SessionManagement(context!!)
        user = sessionManagement.userDetails
        editApplicantDetails = v.findViewById<View>(R.id.editApplicantDetails) as TextView
        editApplicantDetails.text = sessionManagement.savedRegistration.name
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
            R.id.editDob -> {
                val newFragment = DatePickerFragment.newInstance(R.id.editDob)
                newFragment.show(fragmentManager!!, "DatePicker")
            }

            R.id.editRelocated -> {
                val dateMovedFragment = DatePickerFragment.newInstance(R.id.editRelocated)
                dateMovedFragment.show(fragmentManager!!, "Datepicker")
            }
            R.id.buttonSave -> {
                // set date as integers
                val dateFormat = SimpleDateFormat("yyyy/MM/dd")

                Toast.makeText(context, "Validating and saving", Toast.LENGTH_SHORT).show()
                val currentDate = Date().time
                val uuid: String
                if (editingExam == null) {
                    uuid = UUID.randomUUID().toString()
                } else {
                    uuid = editingExam!!.id
                }

                val applicantId = sessionManagement.savedRegistration.id
                val recruitment = sessionManagement.savedRecruitment.id

                val applicantComment = ""
                val applicantAddedBy = Integer.valueOf(user[SessionManagement.KEY_USERID])
                val applicantProceed = 0
                val applicantSync = 0
                val country = user[SessionManagement.KEY_USER_COUNTRY]

                val mathsScore = mMaths.text.toString()
                val englishScore = mEnglish.text.toString()
                val selfAssessmentScore = mSelfAssessment.text.toString()
                // Do some validations
                if (mathsScore.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Enter the Score for Maths", Toast.LENGTH_SHORT).show()
                    mMaths.requestFocus()
                    return
                }
                if (java.lang.Double.valueOf(mathsScore) > 10 && country.equals("KE", ignoreCase = true)) {
                    Toast.makeText(context, "Marks cannot be more than 10", Toast.LENGTH_SHORT).show()
                    mMaths.requestFocus()
                    return
                }

                if (englishScore.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Enter the Score for English", Toast.LENGTH_SHORT).show()
                    mEnglish.requestFocus()
                    return
                }
                if (java.lang.Double.valueOf(englishScore) > 10 && country.equals("KE", ignoreCase = true)) {
                    Toast.makeText(context, "Marks cannot be more than 10", Toast.LENGTH_SHORT).show()
                    mEnglish.requestFocus()
                    return
                }

                if (selfAssessmentScore.trim { it <= ' ' } == "") {
                    Toast.makeText(context, "Enter the Score for Self Assessment", Toast.LENGTH_SHORT).show()
                    mSelfAssessment.requestFocus()
                    return
                }
                if (java.lang.Double.valueOf(selfAssessmentScore) > 10 && country.equals("KE", ignoreCase = true)) {
                    Toast.makeText(context, "Marks cannot be more than 10", Toast.LENGTH_SHORT).show()
                    mSelfAssessment.requestFocus()
                    return
                }
                val applicantMathsScore = java.lang.Double.parseDouble(mMaths.text.toString())
                val applicantEnglishScore = java.lang.Double.parseDouble(mEnglish.text.toString())
                val applicantSelfAssessmentScore = java.lang.Double.parseDouble(mSelfAssessment.text.toString())

                // Save Exam Details
                val exam: Exam
                exam = Exam(uuid, applicantId, applicantMathsScore, recruitment,
                        applicantSelfAssessmentScore, applicantEnglishScore, applicantAddedBy,
                        currentDate, applicantSync, applicantComment, country!!)

                val examTable = ExamTable(context!!)
                val id = examTable.addData(exam)

                if (id.equals(-1)) {
                    Toast.makeText(context, "Could not save the results", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                    // Clear boxes
                    mMaths.setText("")
                    mEnglish.setText("")
                    mSelfAssessment.setText("")
                    mMaths.requestFocus()

                    val fragment = RegistrationViewFragment()
                    val fragmentTransaction: FragmentTransaction
                    fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW)
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

        if (editingExam != null) {

            mMaths.setText(editingExam!!.math.toString())
            mEnglish.setText(editingExam!!.english.toString())
            mSelfAssessment.setText(editingExam!!.personality.toString())
            mMaths.requestFocus()
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
        fun newInstance(param1: String, param2: String): NewExamFragment {
            val fragment = NewExamFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
