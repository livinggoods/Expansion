package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewExamFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewExamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewExamFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText mMaths, mEnglish, mSelfAssessment;
    TextView editApplicantDetails;

    Button buttonSave, buttonList;

    Exam editingExam = null;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    SessionManagement sessionManagement;
    HashMap<String, String> user;



    public NewExamFragment() {
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
    public static NewExamFragment newInstance(String param1, String param2) {
        NewExamFragment fragment = new NewExamFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_exam, container, false);
        //Initialize the UI Components
        mMaths = (EditText) v.findViewById(R.id.editMathScore);
        mEnglish = (EditText) v.findViewById(R.id.editEnglishScore);
        mSelfAssessment = (EditText) v.findViewById(R.id.editSelfAssessmentScore);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_EXAM;
        MainActivity.backFragment = new RegistrationViewFragment();
        sessionManagement = new SessionManagement(getContext());
        user = sessionManagement.getUserDetails();
        editApplicantDetails = (TextView) v.findViewById(R.id.editApplicantDetails);
        editApplicantDetails.setText(sessionManagement.getSavedRegistration().getName());
        setUpEditingMode();
        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

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
                if (editingExam == null){
                    uuid = UUID.randomUUID().toString();
                }else{
                    uuid = UUID.randomUUID().toString();
                }

                String applicantId = sessionManagement.getSavedRegistration().getId();
                String recruitment = sessionManagement.getSavedRecruitment().getId();

                Double applicantMathsScore = Double.parseDouble(mMaths.getText().toString());
                Double applicantEnglishScore = Double.parseDouble(mEnglish.getText().toString());
                Double applicantSelfAssessmentScore = Double.parseDouble(mSelfAssessment.getText().toString());

                String applicantComment = "";
                Integer applicantAddedBy = Integer.valueOf(user.get(SessionManagement.KEY_USERID));
                Integer applicantProceed = 0;
                Long applicantDateAdded = currentDate;
                Integer applicantSync = 0;
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);


                // Do some validations
                if (applicantMathsScore.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Enter the Score for Maths", Toast.LENGTH_SHORT).show();
                }

                else if (applicantEnglishScore.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Enter the Score for English", Toast.LENGTH_SHORT).show();
                }

                else if(applicantSelfAssessmentScore.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Enter the Score for Self Assessment", Toast.LENGTH_SHORT).show();
                } else{
                    // Save Exam Details
                    Exam exam;
                    exam = new Exam(uuid, applicantId, applicantMathsScore, recruitment,
                            applicantSelfAssessmentScore, applicantEnglishScore, applicantAddedBy,
                            applicantDateAdded, applicantSync, applicantComment, country);

                    ExamTable examTable = new ExamTable(getContext());
                    long id = examTable.addData(exam);

                    if (id ==-1){
                        Toast.makeText(getContext(), "Could not save the results", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                        // Clear boxes
                        mMaths.setText("");
                        mEnglish.setText("");
                        mSelfAssessment.setText("");
                        mMaths.requestFocus();
                    }

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
    public void setUpEditingMode(){

        if (editingExam != null){

            mMaths.setText(String.valueOf(editingExam.getMath()));
            mEnglish.setText(String.valueOf(editingExam.getEnglish()));
            mSelfAssessment.setText(String.valueOf(editingExam.getPersonality()));
            mMaths.requestFocus();
        }
    }
}
