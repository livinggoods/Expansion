package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable;
import com.expansion.lg.kimaru.expansion.tables.ParishTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewMobilizationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewMobilizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewMobilizationFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText editName;
    EditText editComment;
    Spinner selectParish;


    Button buttonSave, buttonList;
    public Mobilization editingMobilization = null;

    List<Parish> parishList = new ArrayList<>();
    List<String> parishes = new ArrayList<>();


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    HashMap<String, String> user;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewMobilizationFragment newInstance(String param1, String param2) {
        NewMobilizationFragment fragment = new NewMobilizationFragment();
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
        View v =  inflater.inflate(R.layout.new_mobilization, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_RECRUITMENT;
        MainActivity.backFragment = new MapViewFragment();
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
        editName = (EditText) v.findViewById(R.id.editName);
        editComment = (EditText) v.findViewById(R.id.editComment);
        selectParish = (Spinner) v.findViewById(R.id.selectParish);

        addParishes();
        ArrayAdapter<String> parishesAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, parishes);
        parishesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectParish.setAdapter(parishesAdapter);

        setUpEditingMode();
        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        return v;
    }

    public void addParishes(){
        ParishTable parishTable = new ParishTable(getContext());
        //communityUnits communityUnitList
        parishList = parishTable.getParishByMapping(session
                .getSavedMapping().getId());
        for (Parish p: parishList){
            parishes.add(p.getName());
        }
    }

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
            case R.id.buttonList:
                fragment = new MapViewFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
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
                if (editingMobilization != null){
                    id = editingMobilization.getId();
                }else {
                    id = UUID.randomUUID().toString();
                }
                String name = editName.getText().toString();
                String comment = editComment.getText().toString();

                String mappingId = session.getSavedMapping().getId();
                // String county = session.getSavedMapping().getCounty(); // only for Kenya
                String country = session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY);
                String district = session.getSavedMapping().getDistrict();
                String subCounty = session.getSavedMapping().getSubCounty();


                Mobilization mobilization = new Mobilization();
                mobilization.setId(id);
                mobilization.setName(name);
                mobilization.setMappingId(mappingId);
                mobilization.setCountry(country);
                mobilization.setAddedBy(Integer.valueOf(user.get(SessionManagement.KEY_USERID)));
                mobilization.setComment(comment);
                mobilization.setDateAdded(currentDate);
                mobilization.setSynced(false);
                mobilization.setDistrict(district);
                mobilization.setSubCounty(subCounty);
                mobilization.setParish(parishList.get(selectParish.getSelectedItemPosition()).getId());

                // Do some validations
                if (editName.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                    editComment.requestFocus();
                    return;
                }

                // Save the Mobilization
                MobilizationTable mobilizationTable = new MobilizationTable(getContext());
                long statusId = mobilizationTable.addData(mobilization);

                if (statusId ==-1){
                    Toast.makeText(getContext(), "Could not save the mobilization", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                    fragment = new MapViewFragment();
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
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

    public void setUpEditingMode(){
        if (editingMobilization != null){
            editName.setText(editingMobilization.getName());
            editComment.setText(editingMobilization.getComment());
            int x = 0;
            for (Parish p : parishList){
                if (p.getId().equalsIgnoreCase(editingMobilization.getParish())){
                    selectParish.setSelection(x, true);
                    break;
                }
                x++;
            }
            editName.requestFocus();
        }
    }
}
