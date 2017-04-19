package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


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
    public Recruitment editingRecruitment = null;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    HashMap<String, String> user;



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
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_RECRUITMENT;
        MainActivity.backFragment = new RecruitmentsFragment();
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
                //Initialize the UI Components
        mName = (EditText) v.findViewById(R.id.editRecruitmentName);
        mDistrict = (EditText) v.findViewById(R.id.editRecruitmentDistrict);
        //mDivision = (EditText) v.findViewById(R.id.editRecruitmentDivision);
        //mSubCounty = (EditText) v.findViewById(R.id.editRecruitmentSubCounty);

        //in case we are editing
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
            case R.id.buttonList:

                Fragment fragment = new RecruitmentsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.buttonSave:

                // set date as integers
                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                Long currentDate =  new Date().getTime();

                // Generate the uuid
                String id;
                if (editingRecruitment != null){
                    id = editingRecruitment.getId();
                }else {
                    id = UUID.randomUUID().toString();
                }
                String recruitmentName = mName.getText().toString();
                String recruitmentDistrict = mDistrict.getText().toString();
                String recruitmentDivision = "";
                String recruitmentSubCounty = "";
                String recruitmentComment = "";
                String recruitmentLat = "";
                String recruitmentLon = "";
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);

                Integer recruitmentAddedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
                Long recruitmentDateAdded = currentDate;
                Integer recruitmentSync = 0;


                // Do some validations
                if (recruitmentName.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                    mName.requestFocus();
                    return;
                }

                if (recruitmentDistrict.toString().trim().equals("")){
                    Toast.makeText(getContext(), "District is required", Toast.LENGTH_SHORT).show();
                    mDistrict.requestFocus();
                    return;
                }
                // Save Recruitment
                Recruitment recruitment;
                recruitment = new Recruitment(id, recruitmentName, recruitmentDistrict,
                        recruitmentSubCounty, recruitmentDivision, recruitmentLat,
                        recruitmentLon, recruitmentComment, recruitmentAddedBy,
                        recruitmentDateAdded, recruitmentSync, country, "");
                RecruitmentTable recruitmentTable = new RecruitmentTable(getContext());
                long statusId = recruitmentTable.addData(recruitment);

                if (statusId ==-1){
                    Toast.makeText(getContext(), "Could not save recruitment", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    // Clear boxes
                    mName.setText("");
                    mDistrict.setText("");
                    //set Focus
                    mName.requestFocus();
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
        if (editingRecruitment != null){
            mName.setText(editingRecruitment.getName());
            mDistrict.setText(editingRecruitment.getDistrict());
            mName.requestFocus();
        }
    }
}
