package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewInterviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewInterviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewInterviewFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RadioGroup mMotivation;
    RadioGroup mCommunity;
    RadioGroup mMentality;
    RadioGroup mSelling;
    RadioGroup mInvestment;
    RadioGroup mHealth;
    RadioGroup mInterpersonal;
    RadioGroup mCommitment;
    RadioGroup mConditionsPreventing;

    EditText mComment;
    Button buttonSave, buttonList;

    RadioGroup mReadAndInterpret;
    CheckBox mMotivationAssessment;
    CheckBox mAgeAssessment;
    CheckBox mResidencyAssessment;
    CheckBox mBracAssessment;
    CheckBox mQualifyAssessment;
    CheckBox mReadIntepreteEnglish;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    SessionManagement session;
    HashMap<String, String> user;

    Interview editingInterview = null;


    public NewInterviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewInterviewFragment newInstance(String param1, String param2) {
        NewInterviewFragment fragment = new NewInterviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
        if (user.get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("UG")) {
            v = inflater.inflate(R.layout.fragment_new_ug_interview, container, false);
            mReadAndInterpret = (RadioGroup) v.findViewById(R.id.editReadEnglish);
        } else {
            v = inflater.inflate(R.layout.fragment_new_interview, container, false);
            mHealth = (RadioGroup) v.findViewById(R.id.editHealth);
        }
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_INTERVIEW;
        MainActivity.backFragment = new InterviewsFragment();

        //Initialize the UI Components
        mMotivation = (RadioGroup) v.findViewById(R.id.editMotivation);
        mCommunity = (RadioGroup) v.findViewById(R.id.editCommunity);
        mMentality = (RadioGroup) v.findViewById(R.id.editMentality);
        mSelling = (RadioGroup) v.findViewById(R.id.editSelling);
        mInvestment = (RadioGroup) v.findViewById(R.id.editInvestment);
        mInterpersonal = (RadioGroup) v.findViewById(R.id.editInterpersonal);
        mCommitment = (RadioGroup) v.findViewById(R.id.editCommitment);
        mConditionsPreventing = (RadioGroup) v.findViewById(R.id.conditionsPreventing);

        mMotivationAssessment = (CheckBox) v.findViewById(R.id.cb_good_motivation_attitude);
        mAgeAssessment = (CheckBox) v.findViewById(R.id.cb_age_30_55);
        mResidencyAssessment = (CheckBox) v.findViewById(R.id.cb_residency_2_more);
        mBracAssessment = (CheckBox) v.findViewById(R.id.cb_not_brac_chp);
        mQualifyAssessment = (CheckBox) v.findViewById(R.id.cb_qualified_assessment);
        mReadIntepreteEnglish = (CheckBox) v.findViewById(R.id.cb_read_inteprete_english);

        mComment = (EditText) v.findViewById(R.id.editComment);
        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);
        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        setupEditingMode();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editDob:
                DialogFragment newFragment = new DatePickerFragment().newInstance(R.id.editDob);
                newFragment.show(getFragmentManager(), "DatePicker");
                break;

            case R.id.editRelocated:
                DialogFragment dateMovedFragment = new DatePickerFragment().newInstance(R.id.editRelocated);
                dateMovedFragment.show(getFragmentManager(), "Datepicker");
                break;
            case R.id.buttonSave:

                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                Long currentDate = new Date().getTime();
                String uuid;
                if (editingInterview == null) {
                    uuid = UUID.randomUUID().toString();
                } else {
                    uuid = editingInterview.getId();
                }

                String applicantId = session.getSavedRegistration().getId();
                String recruitment = session.getSavedRecruitment().getId();
                Integer applicantInterpersonal = Integer.parseInt(getSelectedRadioItemValue(mInterpersonal));
                Integer applicantCommitment = Integer.parseInt(getSelectedRadioItemValue(mCommitment));
                Integer applicantMotivation = Integer.parseInt(getSelectedRadioItemValue(mMotivation));
                Integer applicantCommunity = Integer.parseInt(getSelectedRadioItemValue(mCommunity));
                Integer applicantMentality = Integer.parseInt(getSelectedRadioItemValue(mMentality));
                Integer applicantSelling = Integer.parseInt(getSelectedRadioItemValue(mSelling));

                Integer applicantHealth;
                if (user.get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("KE")) {
                    applicantHealth = Integer.parseInt(getSelectedRadioItemValue(mHealth));
                }
                Integer applicantInvestment = Integer.parseInt(getSelectedRadioItemValue(mInvestment));
                boolean conditionsPreventingJoining = getSelectedRadioItemValue(mConditionsPreventing).equalsIgnoreCase("No");
                Integer applicantSelected = 0;//= getSelectedRadioItemValue(mSelected) == "Yes";
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);

                String applicantComment = mComment.getText().toString();
                Integer applicantAddedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
                Long applicantDateAdded = currentDate;
                Integer applicantSync = 0;
                // save the details
                Interview interview = new Interview();
                interview.setId(uuid);
                interview.setApplicant(applicantId);
                interview.setRecruitment(recruitment);
                interview.setMotivation(applicantMotivation);
                interview.setCommunity(applicantCommunity);
                interview.setMentality(applicantMentality);
                interview.setSelling(applicantSelling);
                if (user.get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("KE")) {
                    interview.setHealth(Integer.parseInt(getSelectedRadioItemValue(mHealth)));
                } else {
                    Integer readAndInterpret = Integer.parseInt(getSelectedRadioItemValue(mReadAndInterpret));
                    interview.setReadAndInterpret(readAndInterpret);
                    interview.setInterviewerMotivationAssessment(mMotivationAssessment.isChecked() ? 1 : 0);
                    interview.setInterviewerAgeAssessment(mAgeAssessment.isChecked() ? 1 : 0);
                    interview.setInterviewerResidenyAssessment(mResidencyAssessment.isChecked() ? 1 : 0);
                    interview.setInterviewerAbilityToReadAssessment(mReadIntepreteEnglish.isChecked() ? 1 : 0);
                    interview.setInterviewerBracAssessment(mBracAssessment.isChecked() ? 1 : 0);
                    interview.setInterviewerQualifyAssessment(mQualifyAssessment.isChecked() ? 1 : 0);
                }
                interview.setInvestment(applicantInvestment);
                interview.setInterpersonal(applicantInterpersonal);
                interview.setCommitment(applicantCommitment);
                interview.setSelected(applicantSelected);
                interview.setAddedBy(applicantAddedBy);
                interview.setDateAdded(applicantDateAdded);
                interview.setComment(applicantComment);
                interview.setCanJoin(conditionsPreventingJoining);
                interview.setCountry(country);
                interview.setSynced(0);


                InterviewTable interviewTable = new InterviewTable(getContext());
                long id = interviewTable.addData(interview);

                if (id == -1) {
                    Toast.makeText(getContext(), "Could not save the results", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new RegistrationViewFragment();
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW);
                    fragmentTransaction.commitAllowingStateLoss();
                }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setupEditingMode() {
        if (editingInterview != null) {
            mComment.setText(editingInterview.getComment());

            //check motivation
            int motivation = editingInterview.getMotivation();
            mMotivation.clearCheck();
            switch (motivation) {
                case 1:
                    mMotivation.check(R.id.motivation1);
                    break;
                case 2:
                    mMotivation.check(R.id.motivation2);
                    break;
                case 3:
                    mMotivation.check(R.id.motivation3);
                    break;
                case 4:
                    mMotivation.check(R.id.motivation4);
                    break;
                case 5:
                    mMotivation.check(R.id.motivation5);
                    break;
            }
            int community = editingInterview.getCommunity();
            mCommunity.clearCheck();
            switch (community) {
                case 1:
                    mCommunity.check(R.id.community1);
                    break;
                case 2:
                    mCommunity.check(R.id.community2);
                    break;
                case 3:
                    mCommunity.check(R.id.community3);
                    break;
                case 4:
                    mCommunity.check(R.id.community4);
                    break;
                case 5:
                    mCommunity.check(R.id.community5);
                    break;
            }
            int mentality = editingInterview.getMentality();
            mMentality.clearCheck();
            switch (mentality) {
                case 1:
                    mMentality.check(R.id.mentality1);
                    break;
                case 2:
                    mMentality.check(R.id.mentality2);
                    break;
                case 3:
                    mMentality.check(R.id.mentality3);
                    break;
                case 4:
                    mMentality.check(R.id.mentality4);
                    break;
                case 5:
                    mMentality.check(R.id.mentality5);
                    break;
            }
            int selling = editingInterview.getSelling();//
            mSelling.clearCheck();
            switch (selling) {
                case 1:
                    mSelling.check(R.id.selling1);
                    break;
                case 2:
                    mSelling.check(R.id.selling2);
                    break;
                case 3:
                    mSelling.check(R.id.selling3);
                    break;
                case 4:
                    mSelling.check(R.id.selling4);
                    break;
                case 5:
                    mSelling.check(R.id.selling5);
                    break;
            }
            int investment = editingInterview.getInvestment();
            mInvestment.clearCheck();
            switch (investment) {
                case 1:
                    mInvestment.check(R.id.investment1);
                    break;
                case 2:
                    mInvestment.check(R.id.investment2);
                    break;
                case 3:
                    mInvestment.check(R.id.investment3);
                    break;
                case 4:
                    mInvestment.check(R.id.investment4);
                    break;
                case 5:
                    mInvestment.check(R.id.investment5);
                    break;
            }
            int interpersonal = editingInterview.getInterpersonal();
            mInterpersonal.clearCheck();
            switch (interpersonal) {
                case 1:
                    mInterpersonal.check(R.id.interpersonal1);
                    break;
                case 2:
                    mInterpersonal.check(R.id.interpersonal2);
                    break;
                case 3:
                    mInterpersonal.check(R.id.interpersonal3);
                    break;
                case 4:
                    mInterpersonal.check(R.id.interpersonal4);
                    break;
                case 5:
                    mInterpersonal.check(R.id.interpersonal5);
                    break;
            }
            int commitment = editingInterview.getCommitment();
            mCommitment.clearCheck();
            switch (commitment) {
                case 1:
                    mCommitment.check(R.id.commitment1);
                    break;
                case 2:
                    mCommitment.check(R.id.commitment2);
                    break;
                case 3:
                    mCommitment.check(R.id.commitment3);
                    break;
                case 4:
                    mCommitment.check(R.id.commitment4);
                    break;
                case 5:
                    mCommitment.check(R.id.commitment5);
                    break;
            }
            if (editingInterview.getCountry().equalsIgnoreCase("UG")) {
                int readAndInterpret = editingInterview.getReadAndInterpret();
                mReadAndInterpret.clearCheck();
                switch (readAndInterpret) {
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
                }
            } else {
                int health = editingInterview.getHealth();
                mHealth.clearCheck();
                switch (health) {
                    case 1:
                        mHealth.check(R.id.health1);
                        break;
                    case 2:
                        mHealth.check(R.id.health2);
                        break;
                    case 3:
                        mHealth.check(R.id.health3);
                        break;
                    case 4:
                        mHealth.check(R.id.health4);
                        break;
                    case 5:
                        mHealth.check(R.id.health5);
                        break;
                }
            }

            mConditionsPreventing.clearCheck();
            mConditionsPreventing.check(editingInterview.isCanJoin() ? R.id.coditions1 : R.id.conditions2);

            if (editingInterview.getCountry().equalsIgnoreCase("UG")) {
                mMotivationAssessment.setChecked(editingInterview.getInterviewerMotivationAssessment() == 1);
                mAgeAssessment.setChecked(editingInterview.getInterviewerAgeAssessment() == 1);
                mResidencyAssessment.setChecked(editingInterview.getInterviewerResidenyAssessment() == 1);
                mBracAssessment.setChecked(editingInterview.getInterviewerBracAssessment() == 1);
                mQualifyAssessment.setChecked(editingInterview.getInterviewerQualifyAssessment() == 1);
                mReadIntepreteEnglish.setChecked(editingInterview.getInterviewerAbilityToReadAssessment() == 1);
            }

        }
    }

    public String getSelectedRadioItemValue(RadioGroup radioGroup) {
        Integer selectedButton = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) radioGroup.findViewById(selectedButton);
        String selectedValue = selectedRadioButton.getText().toString();
        return selectedValue;
    }

}
