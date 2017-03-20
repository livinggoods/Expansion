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
import com.expansion.lg.kimaru.expansion.dbhelpers.Recruitment;
import com.expansion.lg.kimaru.expansion.dbhelpers.RecruitmentTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewRecruitmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewRecruitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRecruitmentFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText mName;
    EditText mDivision;
    EditText mSubCounty;
    EditText mDistrict;


    Button buttonSave, buttonList;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    Integer loggedInUser = 1;



    public NewRecruitmentFragment() {
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
    public static NewRecruitmentFragment newInstance(String param1, String param2) {
        NewRecruitmentFragment fragment = new NewRecruitmentFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_recruitment, container, false);
        //Initialize the UI Components
        mName = (EditText) v.findViewById(R.id.editRecruitmentName);
        mDistrict = (EditText) v.findViewById(R.id.editRecruitmentDistrict);
        mDivision = (EditText) v.findViewById(R.id.editRecruitmentDivision);
        mSubCounty = (EditText) v.findViewById(R.id.editRecruitmentSubCounty);


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
            case R.id.buttonSave:
                // set date as integers
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                Integer currentDate =  (int) (new Date().getTime()/1000);

                String recruitmentName = mName.getText().toString();
                String recruitmentDistrict = mDistrict.getText().toString();
                String recruitmentDivision = mDivision.getText().toString();
                String recruitmentSubCounty = mSubCounty.getText().toString();
                String recruitmentComment = "";
                String recruitmentLat = "";
                String recruitmentLon = "";

                Integer recruitmentAddedBy = loggedInUser;
                Integer recruitmentDateAdded = currentDate;
                Integer recruitmentSync = 0;


                // Do some validations
                if (recruitmentName.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                }

                else if (recruitmentDistrict.toString().trim().equals("")){
                    Toast.makeText(getContext(), "District is required", Toast.LENGTH_SHORT).show();
                }

                else if(recruitmentDivision.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Division is required", Toast.LENGTH_SHORT).show();
                }
                else if(recruitmentSubCounty.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Sub County is required", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Save Recruitment
                    Recruitment recruitment;
                    recruitment = new Recruitment(recruitmentName, recruitmentDistrict, recruitmentSubCounty, recruitmentDivision, recruitmentLat, recruitmentLon, recruitmentComment, recruitmentAddedBy, recruitmentDateAdded, recruitmentSync);
                    RecruitmentTable recruitmentTable = new RecruitmentTable(getContext());
                    long id = recruitmentTable.addData(recruitment);

                    if (id ==-1){
                        Toast.makeText(getContext(), "Could not save registration", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                        // Clear boxes
                        mName.setText("");
                        mDistrict.setText("");
                        mDivision.setText("");
                        mSubCounty.setText("");

                        //set Focus
                        mName.requestFocus();
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
