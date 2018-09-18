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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.Interview
import com.expansion.lg.kimaru.expansion.tables.InterviewTable

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.UUID


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewInterviewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewInterviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewInterviewFragment : Fragment(), OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit var mMotivation: RadioGroup
    internal lateinit  var mCommunity: RadioGroup
    internal lateinit  var mMentality: RadioGroup
    internal  lateinit var mSelling: RadioGroup
    internal  var mHealth: RadioGroup? = null
    internal  lateinit var mInvestment: RadioGroup
    internal  lateinit var mInterpersonal: RadioGroup
    internal  lateinit var mCommitment: RadioGroup
    internal lateinit  var mConditionsPreventing: RadioGroup
    internal var mReadAndInterpret: RadioGroup? = null
    internal var mMotivationAssessment: RadioGroup? = null
    internal var mAgeAssessment: RadioGroup? = null
    internal var mResidencyAssessment: RadioGroup? = null
    internal var mBracAssessment: RadioGroup? = null
    internal var mQualifyAssessment: RadioGroup? = null

    internal  lateinit var mComment: EditText
    internal  lateinit var buttonSave: Button
    internal  lateinit var buttonList: Button


    private val mYear: Int = 0
    private val mMonth: Int = 0
    private val mDay: Int = 0


    internal  lateinit var session: SessionManagement
    internal  lateinit var user: HashMap<String, String>

    internal var editingInterview: Interview? = null

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
        val v: View
        session = SessionManagement(context!!)
        user = session.userDetails
        if (user[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
            v = inflater.inflate(R.layout.fragment_new_interview, container, false)
            // mReadAndInterpret = (RadioGroup) v.findViewById(R.id.editReadEnglish);
        } else {
            v = inflater.inflate(R.layout.fragment_new_interview, container, false)
            // mHealth = (RadioGroup) v.findViewById(R.id.editHealth);
        }
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_INTERVIEW
        MainActivity.backFragment = InterviewsFragment()

        //Initialize the UI Components
        mMotivation = v.findViewById<View>(R.id.editMotivation) as RadioGroup
        mCommunity = v.findViewById<View>(R.id.editCommunity) as RadioGroup
        mMentality = v.findViewById<View>(R.id.editMentality) as RadioGroup
        mSelling = v.findViewById<View>(R.id.editSelling) as RadioGroup
        mInvestment = v.findViewById<View>(R.id.editInvestment) as RadioGroup
        mInterpersonal = v.findViewById<View>(R.id.editInterpersonal) as RadioGroup
        mCommitment = v.findViewById<View>(R.id.editCommitment) as RadioGroup
        mConditionsPreventing = v.findViewById<View>(R.id.conditionsPreventing) as RadioGroup

        /*mMotivationAssessment = (RadioGroup) v.findViewById(R.id.conditionsPreventing);
        mAgeAssessment = (RadioGroup) v.findViewById(R.id.conditionsPreventing);
        mResidencyAssessment = (RadioGroup) v.findViewById(R.id.conditionsPreventing);
        mBracAssessment = (RadioGroup) v.findViewById(R.id.conditionsPreventing);
        mQualifyAssessment = (RadioGroup) v.findViewById(R.id.conditionsPreventing); */

        mComment = v.findViewById<View>(R.id.editComment) as EditText
        buttonList = v.findViewById<View>(R.id.buttonList) as Button
        buttonList.setOnClickListener(this)
        buttonSave = v.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener(this)

        setupEditingMode()

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

                Toast.makeText(context, "Validating and saving", Toast.LENGTH_SHORT).show()
                val currentDate = Date().time
                val uuid: String
                if (editingInterview == null) {
                    uuid = UUID.randomUUID().toString()
                } else {
                    uuid = editingInterview!!.id
                }

                val applicantId = session.savedRegistration.id
                val recruitment = session.savedRecruitment.id
                val applicantInterpersonal = Integer.parseInt(getSelectedRadioItemValue(mInterpersonal))
                val applicantCommitment = Integer.parseInt(getSelectedRadioItemValue(mCommitment))
                val applicantMotivation = Integer.parseInt(getSelectedRadioItemValue(mMotivation))
                val applicantCommunity = Integer.parseInt(getSelectedRadioItemValue(mCommunity))
                val applicantMentality = Integer.parseInt(getSelectedRadioItemValue(mMentality))
                val applicantSelling = Integer.parseInt(getSelectedRadioItemValue(mSelling))

                val applicantHealth: Int?
                if (user[SessionManagement.KEY_USER_COUNTRY].equals("KE", ignoreCase = true)) {
                    applicantHealth = Integer.parseInt(getSelectedRadioItemValue(mHealth))
                }
                val applicantInvestment = Integer.parseInt(getSelectedRadioItemValue(mInvestment))
                val conditionsPreventingJoining = getSelectedRadioItemValue(mConditionsPreventing).equals("No", ignoreCase = true)
                val applicantSelected = 0//= getSelectedRadioItemValue(mSelected) == "Yes";
                val country = user[SessionManagement.KEY_USER_COUNTRY]

                val applicantComment = mComment.text.toString()
                val applicantAddedBy = Integer.parseInt(user[SessionManagement.KEY_USERID])
                val applicantSync = 0
                // save the details
                val interview = Interview()
                interview.id = uuid
                interview.applicant = applicantId
                interview.recruitment = recruitment
                interview.motivation = applicantMotivation
                interview.community = applicantCommunity
                interview.mentality = applicantMentality
                interview.selling = applicantSelling
                if (user[SessionManagement.KEY_USER_COUNTRY].equals("KE", ignoreCase = true)) {
                    interview.health = Integer.parseInt(getSelectedRadioItemValue(mHealth))
                } else {
                    // Integer readAndInterpret = Integer.parseInt(getSelectedRadioItemValue(mReadAndInterpret));
                    // interview.setReadAndInterpret(readAndInterpret);
                    //                    interview.setInterviewerMotivationAssessment(Integer.parseInt(getSelectedRadioItemValue(mMotivationAssessment)));
                    //                    interview.setInterviewerAgeAssessment(Integer.parseInt(getSelectedRadioItemValue(mAgeAssessment)));
                    //                    interview.setInterviewerResidenyAssessment(Integer.parseInt(getSelectedRadioItemValue(mResidencyAssessment)));
                    //                    interview.setInterviewerBracAssessment(Integer.parseInt(getSelectedRadioItemValue(mBracAssessment)));
                    //                    interview.setInterviewerQualifyAssessment(Integer.parseInt(getSelectedRadioItemValue(mQualifyAssessment)));
                }
                interview.investment = applicantInvestment
                interview.interpersonal = applicantInterpersonal
                interview.commitment = applicantCommitment
                interview.selected = applicantSelected
                interview.addedBy = applicantAddedBy
                interview.dateAdded = currentDate
                interview.comment = applicantComment
                interview.isCanJoin = conditionsPreventingJoining
                interview.country = country!!
                interview.synced = 0


                val interviewTable = InterviewTable(context!!)
                val id = interviewTable.addData(interview)

                if (id.equals(-1)) {
                    Toast.makeText(context, "Could not save the results", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
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

    fun setupEditingMode() {
        if (editingInterview != null) {
            mComment.setText(editingInterview!!.comment)

            //check motivation
            val motivation = editingInterview!!.motivation!!
            mMotivation.clearCheck()
            when (motivation) {
                1 -> mMotivation.check(R.id.motivation1)
                2 -> mMotivation.check(R.id.motivation2)
                3 -> mMotivation.check(R.id.motivation3)
                4 -> mMotivation.check(R.id.motivation4)
                5 -> mMotivation.check(R.id.motivation5)
            }
            val community = editingInterview!!.community!!
            mCommunity.clearCheck()
            when (community) {
                1 -> mCommunity.check(R.id.community1)
                2 -> mCommunity.check(R.id.community2)
                3 -> mCommunity.check(R.id.community3)
                4 -> mCommunity.check(R.id.community4)
                5 -> mCommunity.check(R.id.community5)
            }
            val mentality = editingInterview!!.mentality!!
            mMentality.clearCheck()
            when (mentality) {
                1 -> mMentality.check(R.id.mentality1)
                2 -> mMentality.check(R.id.mentality2)
                3 -> mMentality.check(R.id.mentality3)
                4 -> mMentality.check(R.id.mentality4)
                5 -> mMentality.check(R.id.mentality5)
            }
            val selling = editingInterview!!.selling!!//
            mSelling.clearCheck()
            when (selling) {
                1 -> mSelling.check(R.id.selling1)
                2 -> mSelling.check(R.id.selling2)
                3 -> mSelling.check(R.id.selling3)
                4 -> mSelling.check(R.id.selling4)
                5 -> mSelling.check(R.id.selling5)
            }
            val investment = editingInterview!!.investment!!
            mInvestment.clearCheck()
            when (investment) {
                1 -> mInvestment.check(R.id.investment1)
                2 -> mInvestment.check(R.id.investment2)
                3 -> mInvestment.check(R.id.investment3)
                4 -> mInvestment.check(R.id.investment4)
                5 -> mInvestment.check(R.id.investment5)
            }
            val interpersonal = editingInterview!!.interpersonal!!
            mInterpersonal.clearCheck()
            when (interpersonal) {
                1 -> mInterpersonal.check(R.id.interpersonal1)
                2 -> mInterpersonal.check(R.id.interpersonal2)
                3 -> mInterpersonal.check(R.id.interpersonal3)
                4 -> mInterpersonal.check(R.id.interpersonal4)
                5 -> mInterpersonal.check(R.id.interpersonal5)
            }
            val commitment = editingInterview!!.commitment!!
            mCommitment.clearCheck()
            when (commitment) {
                1 -> mCommitment.check(R.id.commitment1)
                2 -> mCommitment.check(R.id.commitment2)
                3 -> mCommitment.check(R.id.commitment3)
                4 -> mCommitment.check(R.id.commitment4)
                5 -> mCommitment.check(R.id.commitment5)
            }
            if (editingInterview!!.country.equals("UG", ignoreCase = true)) {
                val readAndInterpret = editingInterview!!.readAndInterpret!!
                // mReadAndInterpret.clearCheck();
                /*switch (readAndInterpret){
                    case 1:
                        mReadAndInterpret.check(R.id.read1);
                        break;
                    case 2:
                        mReadAndInterpret.check(R.id.read2);
                        break;
                    case 3:
                        mReadAndInterpret.check(R.id.read3);
                        break;
                    case 4:
                        mReadAndInterpret.check(R.id.read4);
                        break;
                    case 5:
                        mReadAndInterpret.check(R.id.read5);
                        break;
                }*/
            } else {
                val health = editingInterview!!.health!!
                mHealth!!.clearCheck()
                when (health) {
                    1 -> mHealth!!.check(R.id.health1)
                    2 -> mHealth!!.check(R.id.health2)
                    3 -> mHealth!!.check(R.id.health3)
                    4 -> mHealth!!.check(R.id.health4)
                    5 -> mHealth!!.check(R.id.health5)
                }
            }

            mConditionsPreventing.clearCheck()
            mConditionsPreventing.check(if (editingInterview!!.isCanJoin) R.id.coditions1 else R.id.conditions2)

        }
    }

    fun getSelectedRadioItemValue(radioGroup: RadioGroup?): String {
        val selectedButton = radioGroup!!.checkedRadioButtonId
        val selectedRadioButton = radioGroup.findViewById<View>(selectedButton) as RadioButton
        return selectedRadioButton.text.toString()
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
        fun newInstance(param1: String, param2: String): NewInterviewFragment {
            val fragment = NewInterviewFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
