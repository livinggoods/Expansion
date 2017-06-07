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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.tables.ParishTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewParishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewParishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewParishFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText mName;
    EditText mContactPerson;
    EditText mContactPersonPhone;
    EditText mComment;


    Button buttonSave, buttonList;
    public Parish editingParish = null;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    HashMap<String, String> user;



    public NewParishFragment() {
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
    public static NewParishFragment newInstance(String param1, String param2) {
        NewParishFragment fragment = new NewParishFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_parish, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_RECRUITMENT;
        MainActivity.backFragment = new MapViewFragment();
        session = new SessionManagement(getContext());
        user = session.getUserDetails();

                //Initialize the UI Components
        mName = (EditText) v.findViewById(R.id.editParishName);
        mContactPerson = (EditText) v.findViewById(R.id.editContactPerson);
        mContactPersonPhone = (EditText) v.findViewById(R.id.editContactPersonPhone);
        mComment = (EditText) v.findViewById(R.id.editComment);

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

                Fragment fragment = new MapViewFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.buttonSave:

                // set date as integers
                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();


                // Generate the uuid
                String id;
                if (editingParish != null){
                    id = editingParish.getId();
                }else {
                    id = UUID.randomUUID().toString();
                }
                String parishName = mName.getText().toString();
                String contactPerson = mContactPerson.getText().toString();
                String contactPersonPhone = mContactPersonPhone.getText().toString();
                String comment = mComment.getText().toString();


                String mapping = session.getSavedMapping().getId();
                String parent = session.getSavedMapping().getSubCounty();
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);
                Integer addedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
                Long dateAdded =  new Date().getTime();
                Integer synced = 0;


                // Do some validations
                if (parishName.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                    mName.requestFocus();
                    return;
                }

                // Save Parish
                Parish parish = new Parish(id, parishName, contactPerson, contactPersonPhone,
                        parent, mapping, dateAdded, addedBy, synced, country, comment);

                ParishTable parishTable = new ParishTable(getContext());
                String statusId = parishTable.addData(parish);

                if (statusId.equals(-1)){
                    Toast.makeText(getContext(), "Could not save recruitment", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    // Clear boxes
                    mName.setText("");
                    mContactPerson.setText("");
                    mContactPersonPhone.setText("");
                    mComment.setText("");
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
        if (editingParish != null){
            mName.setText(editingParish.getName());
            mContactPerson.setText(editingParish.getContactPerson());
            mContactPersonPhone.setText(editingParish.getContactPersonPhone());
            mComment.setText(editingParish.getComment());
            mName.requestFocus();
        }
    }
}
