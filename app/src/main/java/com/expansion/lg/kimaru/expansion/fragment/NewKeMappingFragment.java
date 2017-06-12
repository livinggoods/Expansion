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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.KeCounty;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable;
import com.expansion.lg.kimaru.expansion.tables.MappingTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewKeMappingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewKeMappingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewKeMappingFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText mMappingName, mMappingContactPerson, mMappingContactPersonPhone, mComment;
    Spinner selectCounty;

    Button buttonSave, buttonList;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    HashMap<String, String> user;

    List<KeCounty> keCountyList = new ArrayList<>();
    List<String> counties = new ArrayList<>();
    Mapping editingMapping = null;


    public NewKeMappingFragment() {
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
    public static NewKeMappingFragment newInstance(String param1, String param2) {
        NewKeMappingFragment fragment = new NewKeMappingFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_ke_mapping, container, false);
        session = new SessionManagement(getContext());
        //Initialize the UI Components
        // mMappingName, mMappingContactPerson, mMappingContactPersonPhone, mCounty, mComment;

        user = session.getUserDetails();
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_MAPPING;
        MainActivity.backFragment = new MappingFragment();



        mMappingName = (EditText) v.findViewById(R.id.editMappingName);
        mMappingContactPerson = (EditText) v.findViewById(R.id.editContactPerson);
        mMappingContactPersonPhone = (EditText) v.findViewById(R.id.editContactPersonPhone);
        selectCounty = (Spinner) v.findViewById(R.id.selectCounty);
        mComment = (EditText) v.findViewById(R.id.editComment);

        KeCountyTable keCountyTable = new KeCountyTable(getContext());
        keCountyList = keCountyTable.getCounties();
        for (KeCounty k : keCountyList){
            counties.add(k.getCountyName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, counties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCounty.setAdapter(adapter);

        if (editingMapping != null){
            mMappingName.setText(editingMapping.getMappingName());
            mMappingContactPerson.setText(editingMapping.getContactPerson());
            mMappingContactPersonPhone.setText(editingMapping.getContactPersonPhone());
            mComment.setText(editingMapping.getComment());
            int x = 0;
            for (KeCounty c : keCountyList){
                if (c.getId().equals(Integer.valueOf(editingMapping.getCounty()))){
                    selectCounty.setSelection(x, true);
                    break;
                }
                x++;
            }
        }

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

            case R.id.buttonSave:
                // set date as integers
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                Long currentDate =  new Date().getTime();
                String id;
                if (editingMapping != null){
                    id = editingMapping.getId();
                }else{
                    id = UUID.randomUUID().toString();
                }

                String mappingName = mMappingName.getText().toString();
                String mappingCounty = String.valueOf(keCountyList.get(selectCounty.getSelectedItemPosition()).getId());
                String contactPerson = mMappingContactPerson.getText().toString();
                String contactPersonPhone = mMappingContactPersonPhone.getText().toString();
                String comment = mComment.getText().toString();
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);
                Integer applicantAddedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
                boolean sync = false;


                // Do some validations
                if (mappingName.toString().trim().equals("")){
                    mMappingName.requestFocus();
                    Toast.makeText(getContext(), "Enter name of the mapping", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Save Exam Details
                Mapping mapping = new Mapping(id, mappingName, country, mappingCounty, currentDate,
                        applicantAddedBy, contactPerson,contactPersonPhone, sync, comment, "", "");

                MappingTable mappingTable = new MappingTable(getContext());
                String createdMap = mappingTable.addData(mapping);
                // Clear boxes
                mMappingContactPerson.setText("");
                mMappingContactPersonPhone.setText("");
                mMappingName.setText("");
                mComment.setText("");
                selectCounty.setSelection(0);
                mMappingName.requestFocus();
                break;
            case R.id.buttonList:
                Fragment fragment = new MappingFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "mappings");
                fragmentTransaction.commitAllowingStateLoss();
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
