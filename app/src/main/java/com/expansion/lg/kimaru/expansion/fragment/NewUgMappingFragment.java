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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
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
 * {@link NewUgMappingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewUgMappingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUgMappingFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText mMappingName, mMappingContactPerson, mMappingContactPersonPhone, mComment;
    Spinner mRegion, mDistrict, mCounty, mSubCounty;


    Button buttonSave, buttonList;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    HashMap<String, String> user;

    List<CountyLocation> regions = new ArrayList<>();
    List<String> listRegions = new ArrayList<String>();

    List<CountyLocation> districts = new ArrayList<>();
    List<String> listDistricts = new ArrayList<String>();

    List<CountyLocation> counties = new ArrayList<>();
    List<String> listCounties = new ArrayList<String>();

    List<CountyLocation> subCounties = new ArrayList<>();
    List<String> listSubCounties = new ArrayList<String>();
    ArrayAdapter<String> subCountyAdapter;


    Mapping editingMapping = null;

    public NewUgMappingFragment() {
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
    public static NewUgMappingFragment newInstance(String param1, String param2) {
        NewUgMappingFragment fragment = new NewUgMappingFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_mapping, container, false);
        session = new SessionManagement(getContext());
        //Initialize the UI Components
        // mMappingName, mMappingContactPerson, mMappingContactPersonPhone, mCounty, mComment;

        user = session.getUserDetails();
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_MAPPING;
        MainActivity.backFragment = new MappingFragment();
        CountyLocationTable countyLocationTable = new CountyLocationTable(getContext());
        mRegion = (Spinner) v.findViewById(R.id.editRegion);
        regions = countyLocationTable.getRegions();
        for (CountyLocation r: regions){
            listRegions.add(r.getName());
        }
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listRegions);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRegion.setAdapter(regionAdapter);
        mRegion.setOnItemSelectedListener(onRegionSelectedListener);


        mDistrict = (Spinner) v.findViewById(R.id.editDistrict);
        try{
            districts.clear();
            listDistricts.clear();
            districts = countyLocationTable.getChildrenLocations(regions.get(mRegion.getSelectedItemPosition()));
        }catch (Exception e){}
        for (CountyLocation district: districts){
            listDistricts.add(district.getName());
        }
        mDistrict.setOnItemSelectedListener(onDistrictSelectedListener);



        mCounty = (Spinner) v.findViewById(R.id.editCounty);
        //populate the Counties
        counties = countyLocationTable.getCountiesAndDistricts();
        for (CountyLocation location: counties){
            listCounties.add(location.getName());
        }
        ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listCounties);
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCounty.setAdapter(adapter0);
        mCounty.setOnItemSelectedListener(onCountySelectedListener);
        //counties.get(position).getName();; get postion, then extract item at pos
        try{
            subCounties = countyLocationTable.getChildrenLocations(counties.get(mCounty.getSelectedItemPosition()));
        }catch(Exception e){}

        for (CountyLocation subCounty: subCounties){
            listSubCounties.add(subCounty.getName());
        }
        mSubCounty = (Spinner) v.findViewById(R.id.editSubCounty);
        subCountyAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listSubCounties);
        subCountyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSubCounty.setAdapter(subCountyAdapter);


        //initialize the array adapter


        mMappingName = (EditText) v.findViewById(R.id.editMappingName);
        mMappingContactPerson = (EditText) v.findViewById(R.id.editContactPerson);
        mMappingContactPersonPhone = (EditText) v.findViewById(R.id.editContactPersonPhone);

        mComment = (EditText) v.findViewById(R.id.editComment);

        if (editingMapping != null){
            mMappingName.setText(editingMapping.getMappingName());
            mMappingContactPerson.setText(editingMapping.getContactPerson());
            mMappingContactPersonPhone.setText(editingMapping.getContactPersonPhone());
            mComment.setText(editingMapping.getComment());
            int x = 0;
            for (CountyLocation county : counties){
                if (county.getId().equals(Integer.valueOf(editingMapping.getCounty()))){
                    mCounty.setSelection(x, true);
                    break;
                }
                x++;
            }
            x = 0;
            for (CountyLocation subCounty : subCounties){
                if (subCounty.getId().equals(Integer.valueOf(editingMapping.getSubCounty()))){
                    mSubCounty.setSelection(x, true);
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

    AdapterView.OnItemSelectedListener onRegionSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // with the ID selected, we will populate the district
            CountyLocationTable countyLocationTable = new CountyLocationTable(getContext());
            listDistricts.clear();
            districts.clear();
            districts = countyLocationTable.getChildrenLocations(regions.get(position));
            for (CountyLocation district: districts){
                listDistricts.add(district.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, listDistricts);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDistrict.setAdapter(adapter);
            mDistrict.setOnItemSelectedListener(onDistrictSelectedListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    AdapterView.OnItemSelectedListener onDistrictSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // with the ID selected, we will populate the district
            CountyLocationTable countyLocationTable = new CountyLocationTable(getContext());
            listCounties.clear();
            counties.clear();
            counties = countyLocationTable.getChildrenLocations(districts.get(position));
            for (CountyLocation district: counties){
                listCounties.add(district.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, listCounties);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCounty.setAdapter(adapter);
            mCounty.setOnItemSelectedListener(onCountySelectedListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    AdapterView.OnItemSelectedListener onCountySelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // with the ID selected, we will populate the subcounty
            CountyLocationTable countyLocationTable = new CountyLocationTable(getContext());
            listSubCounties.clear();
            subCounties.clear();
            subCounties = countyLocationTable.getChildrenLocations(counties.get(position));
            for (CountyLocation subCounty: subCounties){
                listSubCounties.add(subCounty.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, listSubCounties);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSubCounty.setAdapter(adapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onClick(View view){
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
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
                if (editingMapping == null){
                    id = UUID.randomUUID().toString();
                }else{
                    id = editingMapping.getId();
                }
                String mappingName = mMappingName.getText().toString();
                String mappingRegion = String.valueOf(regions.get(mRegion.getSelectedItemPosition()).getId());
                String mappingDistrict = String.valueOf(districts.get(mDistrict.getSelectedItemPosition()).getId());
                String mappingCounty = String.valueOf(counties.get(mCounty.getSelectedItemPosition()).getId());
                String subCounty = String.valueOf(subCounties.get(mSubCounty.getSelectedItemPosition()).getId());
                String contactPerson = mMappingContactPerson.getText().toString();
                String contactPersonPhone = mMappingContactPersonPhone.getText().toString();
                String comment = mComment.getText().toString();
                Integer applicantAddedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
                boolean sync = false;


                // Do some validations
                if (mappingName.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Enter name of the mapping", Toast.LENGTH_SHORT).show();

                }

                else if (mappingCounty.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Enter the county or the area", Toast.LENGTH_SHORT).show();
                } else{
                    // Save Exam Details
                    Mapping mapping = new Mapping(id, mappingName, "UG", mappingCounty,
                            currentDate, applicantAddedBy, contactPerson,contactPersonPhone, sync,
                            comment, mappingDistrict, subCounty, mappingRegion);

                    MappingTable mappingTable = new MappingTable(getContext());
                    String createdMap = mappingTable.addData(mapping);
                    // Clear boxes
                    mMappingContactPerson.setText("");
                    mMappingContactPersonPhone.setText("");
                    mMappingName.setText("");
                    mComment.setText("");
                    mMappingName.requestFocus();

                    session.saveMapping(mapping);
                    fragment = new MapViewFragment();
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "mappings");
                    fragmentTransaction.commitAllowingStateLoss();

                }
                break;
            case R.id.buttonList:
                fragment = new MappingFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "mappings");
                fragmentTransaction.commitAllowingStateLoss();
                break;

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
