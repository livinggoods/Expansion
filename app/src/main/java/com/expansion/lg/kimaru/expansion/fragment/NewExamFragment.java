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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.dbhelpers.Registration;
import com.expansion.lg.kimaru.expansion.dbhelpers.RegistrationTable;
import com.expansion.lg.kimaru.expansion.dbhelpers.Exam;
import com.expansion.lg.kimaru.expansion.dbhelpers.ExamTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


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


    Button buttonSave, buttonList;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    Integer loggedInUser = 1;



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
                Integer currentDate =  (int) (new Date().getTime()/1000);

                Integer applicantId = 1;
                Integer recruitment = 1; //mMaths, mEnglish, mSelfAssessment

                Integer applicantMathsScore = Integer.parseInt(mMaths.getText().toString());
                Integer applicantEnglishScore = Integer.parseInt(mEnglish.getText().toString());
                Integer applicantSelfAssessmentScore = Integer.parseInt(mSelfAssessment.getText().toString());

                String applicantComment = "";
                Integer applicantAddedBy = loggedInUser;
                Integer applicantProceed = 0;
                Integer applicantDateAdded = currentDate;
                Integer applicantSync = 0;
                Integer applicantRecruitment = 1;


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
                    exam = new Exam(applicantId, applicantMathsScore, applicantRecruitment,
                            applicantSelfAssessmentScore, applicantEnglishScore, applicantAddedBy,
                            applicantDateAdded, applicantSync, applicantComment);

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
}
