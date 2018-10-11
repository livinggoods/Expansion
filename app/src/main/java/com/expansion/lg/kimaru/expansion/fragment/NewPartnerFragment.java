package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Partners;
import com.expansion.lg.kimaru.expansion.tables.PartnersTable;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewPartnerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewPartnerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPartnerFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText editPartnerID,  editPartnerName, editContactPerson,  editContactPersonPhone,
            editParent,  editMappingId, editCountry,  editComment, editSynced,  editArchived,
            editDateAdded,  editAddedBy;


    Button buttonSave, buttonList;
    public Partners editingPartners = null;

    public Boolean createdFromRecruitment = false;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    HashMap<String, String> user;
    String country;


    public NewPartnerFragment() {
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
    public static NewPartnerFragment newInstance(String param1, String param2) {
        NewPartnerFragment fragment = new NewPartnerFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_partner, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_RECRUITMENT;
        MainActivity.backFragment = new PartnersFragment();


        session = new SessionManagement(getContext());
        user = session.getUserDetails();
        country = user.get(SessionManagement.KEY_USER_COUNTRY);

        editPartnerName = (EditText) v.findViewById(R.id.editPartnerName);
        editContactPerson = (EditText) v.findViewById(R.id.editContactPerson);
        editContactPersonPhone = (EditText) v.findViewById(R.id.editContactPersonPhone);
        // editParent = (EditText) v.findViewById(R.id.editParent);
        // editMappingId = (EditText) v.findViewById(R.id.editMappingId);
        // editCountry = (EditText) v.findViewById(R.id.editCountry);
        editComment = (EditText) v.findViewById(R.id.editComment);

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
                Fragment fragment = new PartnersFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.buttonSave:
                // set date as integers
                Long currentDate =  new Date().getTime();

                // Generate the uuid
                String id;
                if (editingPartners != null){
                    id = editingPartners.getPartnerID();
                }else {
                    id = UUID.randomUUID().toString();
                }
                String partnerName = editPartnerName.getText().toString();
                String partnerPhone = editContactPerson.getText().toString();
                String partnerContactPerson = editContactPersonPhone.getText().toString();
                String comment = editComment.getText().toString();


                if (!partnerPhone.trim().equals("")){
                    if (partnerPhone.trim().startsWith("+")){
                        if (partnerPhone.length() != 13){
                            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                            editContactPersonPhone.requestFocus();
                            return;
                        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(partnerPhone)) {
                            editContactPersonPhone.requestFocus();
                            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else if (partnerPhone.length() != 10){
                        editContactPersonPhone.requestFocus();
                        Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!PhoneNumberUtils.isGlobalPhoneNumber(partnerPhone)){
                        Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Do some validations
                if (partnerName.trim().equals("")){
                    Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                    editPartnerName.requestFocus();
                    return;
                }

                Partners partner = new Partners();
                partner.setPartnerID(id);
                partner.setPartnerName(partnerName);
                partner.setContactPerson(partnerContactPerson);
                partner.setContactPersonPhone(partnerPhone);
                partner.setParent("");
                partner.setMappingId(session.getSavedMapping().getId());
                partner.setCountry(country);
                partner.setComment(comment);
                partner.setSynced(0);
                partner.setArchived(false);
                partner.setDateAdded(currentDate);
                partner.setAddedBy(Long.valueOf(user.get(SessionManagement.KEY_USERID)));


                PartnersTable partnersTable = new PartnersTable(getContext());
                long statusId = partnersTable.addData(partner);

                if (statusId ==-1){
                    Toast.makeText(getContext(), "Could not save the partner", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    // Clear boxes
                    editPartnerName.setText("");
                    editContactPerson.setText("");
                    editContactPersonPhone.setText("");
                    editComment.setText("");
                }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        if (editingPartners != null){
            editPartnerName.setText(editingPartners.getPartnerName());
            editContactPerson.setText(editingPartners.getContactPerson());
            editContactPersonPhone.setText(editingPartners.getContactPersonPhone());
            editComment.setText(editingPartners.getComment());
            editPartnerName.requestFocus();
        }
    }
}
