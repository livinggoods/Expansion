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

    RadioGroup mMotivation, mCommunity, mMentality, mSelling, mHealth, mInvestment;
    RadioGroup mInterpersonal, mCommitment, mConditionsPreventing, mSelected;

    EditText mComment;
    Button buttonSave, buttonList;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    SessionManagement session;
    HashMap <String, String> user;

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
        View v =  inflater.inflate(R.layout.fragment_new_interview, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_INTERVIEW;
        MainActivity.backFragment = new InterviewsFragment();
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
                //Initialize the UI Components
        mMotivation = (RadioGroup) v.findViewById(R.id.editMotivation);
        mCommunity = (RadioGroup) v.findViewById(R.id.editCommunity);
        mMentality = (RadioGroup) v.findViewById(R.id.editMentality);
        mSelling = (RadioGroup) v.findViewById(R.id.editSelling);
        mHealth = (RadioGroup) v.findViewById(R.id.editHealth);
        mInvestment = (RadioGroup) v.findViewById(R.id.editInvestment);
        mInterpersonal = (RadioGroup) v.findViewById(R.id.editInterpersonal);
        mCommitment = (RadioGroup) v.findViewById(R.id.editCommitment);
        mConditionsPreventing = (RadioGroup) v.findViewById(R.id.conditionsPreventing);
        mSelected = (RadioGroup) v.findViewById(R.id.editSelected);
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
    public void onClick(View view){
        switch (view.getId()){
            case R.id.editDob:
                DialogFragment newFragment = new DatePickerFragment().newInstance(R.id.editDob);
                newFragment.show(getFragmentManager(), "DatePicker");
                break;

            case R.id.editRelocated:
                DialogFragment dateMovedFragment = new DatePickerFragment().newInstance(R.id.editRelocated);
                dateMovedFragment.show(getFragmentManager(), "Datepicker");
                break;
            case R.id.buttonSave:
                // set date as integers
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                Long currentDate =  new Date().getTime();
                String uuid;
                if (editingInterview == null){
                    uuid = UUID.randomUUID().toString();
                }else{
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
                Integer applicantHealth = Integer.parseInt(getSelectedRadioItemValue(mHealth));
                Integer applicantInvestment = Integer.parseInt(getSelectedRadioItemValue(mInvestment));
                boolean conditionsPreventingJoining = getSelectedRadioItemValue(mConditionsPreventing) == "Yes";
                boolean applicantSelected = false;//= getSelectedRadioItemValue(mSelected) == "Yes";
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);

                String applicantComment = mComment.getText().toString();
                Integer applicantAddedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
                Long applicantDateAdded = currentDate;
                Integer applicantSync = 0;
                // save the details
                Interview interview = new Interview(uuid, applicantId, recruitment, applicantMotivation,
                            applicantCommunity, applicantMentality, applicantSelling, applicantHealth,
                            applicantInvestment, applicantInterpersonal, applicantCommitment,
                            applicantSelected, applicantAddedBy, applicantDateAdded, 0,
                            applicantComment, conditionsPreventingJoining, country);
                    InterviewTable interviewTable = new InterviewTable(getContext());
                long id = interviewTable.addData(interview);

                    if (id ==-1){
                        Toast.makeText(getContext(), "Could not save the results", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                        Fragment fragment = new RegistrationViewFragment();
                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW);
                        fragmentTransaction.commitAllowingStateLoss();
                    }


                // Do some validations
//                if (applicantMotivation.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "Enter the Score for Motivation", Toast.LENGTH_SHORT).show();
//                }
//
//                else if (applicantCommunity.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "Enter the Score for community involvement", Toast.LENGTH_SHORT).show();
//                }
//
//                else if(applicantMentality.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "Enter the Score for the applicant's mentality", Toast.LENGTH_SHORT).show();
//                }
//                else if(applicantSelling.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "Enter the Score for the applicant's selling skills", Toast.LENGTH_SHORT).show();
//                }
//                else if(applicantHealth.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "Enter the Score for the applicant's rating for interest in health", Toast.LENGTH_SHORT).show();
//                }
//                else if(applicantInvestment.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "Enter the Score for the applicant's ability to invest", Toast.LENGTH_SHORT).show();
//                }
//                else if(applicantInterpersonal.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "Enter the Score for the applicant's interpersonal skills", Toast.LENGTH_SHORT).show();
//                }
//                else if(applicantCommitment.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "Enter the Score for the applicant's commitment ability", Toast.LENGTH_SHORT).show();
//                } else{
//                    // Save Exam Details
//                    Interview interview = new Interview(uuid, applicantId, recruitment, applicantMotivation,
//                            applicantCommunity, applicantMentality, applicantSelling, applicantHealth,
//                            applicantInvestment, applicantInterpersonal, applicantCommitment, 0,
//                            applicantAddedBy, applicantDateAdded, 0, applicantComment);
//                    InterviewTable interviewTable = new InterviewTable(getContext());
//                    long id = interviewTable.addData(interview);
//
//                    if (id ==-1){
//                        Toast.makeText(getContext(), "Could not save the results", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
//
//                        // Clear boxes
//                        mMotivation.clearCheck();
//                        mCommunity.clearCheck();
//                        mMentality.clearCheck();
//                        mSelling.clearCheck();
//                        mHealth.clearCheck();
//                        mInvestment.clearCheck();
//                        mInterpersonal.clearCheck();
//                        mCommitment.clearCheck();
//                    }
//
//                }

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
    public void setupEditingMode(){
        if (editingInterview != null){
//            mMotivation.setText(editingInterview.getMotivation());
//            mCommunity.setText(editingInterview.getCommunity());
//            mMentality.setText(editingInterview.getMentality());
//            mSelling.setText(editingInterview.getSelling());
//            mHealth.setText(editingInterview.getHealth());
//            mInvestment.setText(editingInterview.getInvestment());
//            mInterpersonal.setText(editingInterview.getInterpersonal());
//            mCommitment.setText(editingInterview.getCommitment());
        }
    }

    public String getSelectedRadioItemValue(RadioGroup radioGroup){
        Integer selectedButton = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton =(RadioButton) radioGroup.findViewById(selectedButton);
        String selectedValue = selectedRadioButton.getText().toString();
        return selectedValue;
    }

}
