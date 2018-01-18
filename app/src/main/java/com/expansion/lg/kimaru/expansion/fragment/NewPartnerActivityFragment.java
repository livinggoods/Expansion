package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.IccmComponent;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.mzigos.PartnerActivity;
import com.expansion.lg.kimaru.expansion.mzigos.Partners;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.mzigos.Village;
import com.expansion.lg.kimaru.expansion.tables.IccmComponentTable;
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable;
import com.expansion.lg.kimaru.expansion.tables.PartnersTable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewPartnerActivityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewPartnerActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPartnerActivityFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Spinner selectPartner;
    EditText partnerActivityComment;
    RadioGroup editIsDoingIccm, editIsGivingFreeMedicine, editIsGivingStipend, editIsDoingMhealth;
    LinearLayout parentLayout;
    List<Partners> partnersList = new ArrayList<Partners>();
    List<String> partnerNames = new ArrayList<String>();
    List<IccmComponent> iccmComponentList = new ArrayList<IccmComponent>();


    Button buttonSave, buttonList;
    public PartnerActivity editingPartnerActivity = null;
    public Village village = null;
    public Parish parish = null;
    public SubCounty subCounty = null;
    public CommunityUnit communityUnit = null;



   static final int DATE_DIALOG_ID = 100;

    SessionManagement session;
    HashMap<String, String> user;
    String country;



    public NewPartnerActivityFragment() {
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
    public static NewPartnerActivityFragment newInstance(String param1, String param2) {
        NewPartnerActivityFragment fragment = new NewPartnerActivityFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_partner_activity, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_RECRUITMENT;
        if (subCounty != null){
            MainActivity.backFragment = new SubCountyViewFragment();
        }
        if (communityUnit != null){
            MainActivity.backFragment = new SubCountyViewFragment();
        }


        session = new SessionManagement(getContext());
        user = session.getUserDetails();
        country = user.get(SessionManagement.KEY_USER_COUNTRY);

        selectPartner = (Spinner) v.findViewById(R.id.selectPartner);
        partnerActivityComment = (EditText) v.findViewById(R.id.editComment);
        editIsDoingIccm = (RadioGroup) v.findViewById(R.id.editIsDoingIccm);
        editIsGivingFreeMedicine = (RadioGroup) v.findViewById(R.id.editIsGivingFreeMedicine);
        editIsGivingStipend = (RadioGroup) v.findViewById(R.id.editIsGivingStipend);
        editIsDoingMhealth = (RadioGroup) v.findViewById(R.id.editIsDoingMhealth);
        parentLayout = (LinearLayout) v.findViewById(R.id.check_add_layout);

        addPartners();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, partnerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectPartner.setAdapter(adapter);
        selectPartner.setOnItemSelectedListener(onSelectedPartnerListener);

        IccmComponentTable iccmComponentTable = new IccmComponentTable(getContext());
        iccmComponentList = iccmComponentTable.getIccmComponentData();
        for (IccmComponent component: iccmComponentList){
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setId(component.getId());
            checkBox.setText(component.getComponentName());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER_VERTICAL;


            LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            checkParams.gravity = Gravity.CENTER_VERTICAL;
            parentLayout.addView(checkBox, params);
        }

        setUpEditingMode();

        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);


        return v;
    }
    AdapterView.OnItemSelectedListener onSelectedPartnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, final long id) {
            String selectedItem = partnerNames.get(position);
            if (selectedItem.equalsIgnoreCase("Add New")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add a New Partner");
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText editTextPartnerName = new EditText(getContext());
                editTextPartnerName.setHint("Partner Name");
                editTextPartnerName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                layout.addView(editTextPartnerName);

                final EditText editTextPartnerContactPerson = new EditText(getContext());
                editTextPartnerContactPerson.setHint("Contact person");
                editTextPartnerContactPerson.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                layout.addView(editTextPartnerContactPerson);

                final EditText editTextPartnerContactPersonPhone = new EditText(getContext());
                editTextPartnerContactPersonPhone.setHint("Contact person phone");
                editTextPartnerContactPersonPhone.setInputType(InputType.TYPE_CLASS_PHONE);
                layout.addView(editTextPartnerContactPersonPhone);

                final EditText editTextComment = new EditText(getContext());
                editTextComment.setHint("Comment");
                editTextComment.setInputType(InputType.TYPE_CLASS_TEXT);
                layout.addView(editTextComment);


                builder.setView(layout);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String partnerName = editTextPartnerName.getText().toString();
                        if (partnerName.equalsIgnoreCase("")){
                            Toast.makeText(getContext(), "Facility Name is required", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // save the Link Facility
                        String uuid = UUID.randomUUID().toString();
                        Partners partner = new Partners();
                        partner.setPartnerID(uuid);
                        partner.setPartnerName(partnerName);

                        partner.setContactPerson(editTextPartnerContactPerson.getText().toString());
                        partner.setContactPersonPhone(editTextPartnerContactPersonPhone.getText().toString());
                        //partner.setParent("");
                        partner.setMappingId(session.getSavedMapping().getId());
                        partner.setCountry(session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY));
                        partner.setComment(editTextComment.getText().toString());
                        partner.setSynced(false);
                        partner.setArchived(false);
                        partner.setDateAdded(new Date().getTime());
                        partner.setAddedBy(Long.valueOf(session.getUserDetails().get(SessionManagement.KEY_USERID)));
                        new PartnersTable(getContext()).addData(partner);

                        // clear select
                        partnersList.clear();
                        partnerNames.clear();
                        addPartners();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, partnerNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectPartner.setAdapter(adapter);

                        //lets set the selected
                        int x = 0;
                        for (Partners e : partnersList) {
                            if (e.getPartnerID().equalsIgnoreCase(uuid)){
                                selectPartner.setSelection(x, true);
                                break;
                            }
                            x++;
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void addPartners(){
        PartnersTable partnersTable = new PartnersTable(getContext());
        partnersList = partnersTable.getPartnersData();
        for (Partners partner: partnersList){
            partnerNames.add(partner.getPartnerName());
        }
        if (partnerNames.size() == 0){
            partnerNames.add("  ");
        }
        partnerNames.add("Add New");
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
                if (editingPartnerActivity != null){
                    id = editingPartnerActivity.getId();
                }else {
                    id = UUID.randomUUID().toString();
                }

                PartnerActivity partnerActivity = new PartnerActivity();

                partnerActivity.setId(id);

                if (partnersList.size() !=0){
                    partnerActivity.setPartnerId(partnersList.get(selectPartner.getSelectedItemPosition()).getPartnerID());
                }

                partnerActivity.setCountry(session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY));
                partnerActivity.setCounty(session.getSavedMapping().getCounty());

                if (subCounty != null){
                    Log.d("TREMEAP", subCounty.getId());
                    Log.d("TREMEAP", ">>>>>>>>>>>>>>>>>>>>>>>");
                    partnerActivity.setSubcounty(subCounty.getId());
                    partnerActivity.setCounty(subCounty.getCountyID());
                }

                if (parish != null){
                    partnerActivity.setParish(parish.getId());
                }
                if (village != null){
                    partnerActivity.setVillage(village.getId());
                }

                if (communityUnit != null){
                    partnerActivity.setCommunityUnit(communityUnit.getId());
                    partnerActivity.setSubcounty(communityUnit.getSubCountyId());
                }

                partnerActivity.setMappingId(session.getSavedMapping().getId());
                partnerActivity.setComment(partnerActivityComment.getText().toString());


                partnerActivity.setDoingIccm(getSelectedRadioItemValue(editIsDoingIccm).equalsIgnoreCase("Yes"));
                partnerActivity.setGivingFreeDrugs(getSelectedRadioItemValue(editIsGivingFreeMedicine).equalsIgnoreCase("Yes"));
                partnerActivity.setGivingStipend(getSelectedRadioItemValue(editIsGivingStipend).equalsIgnoreCase("Yes"));
                partnerActivity.setDoingMhealth(getSelectedRadioItemValue(editIsDoingMhealth).equalsIgnoreCase("Yes"));
                partnerActivity.setDateAdded(new Date().getTime());
                partnerActivity.setAddedBy(Long.valueOf(session.getUserDetails().get(SessionManagement.KEY_USERID)));



                JSONObject activities = new JSONObject();
                String partnerActivities="";

                partnerActivity.setSynced(false);

                ArrayList<Integer> listOfSelectedCheckBoxId = new ArrayList<>();
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    CheckBox checkbox = (CheckBox) parentLayout.getChildAt(i);
                    try{
                        activities.put(String.valueOf(checkbox.getId()), checkbox.isChecked());
                        }catch (Exception e){}
                    if (checkbox.isChecked())
                    {
                        listOfSelectedCheckBoxId.add(checkbox.getId());
                    }
                }
                partnerActivities = activities.toString();
                partnerActivity.setActivities(partnerActivities);

                PartnerActivityTable partnerActivityTable = new PartnerActivityTable(getContext());
                long statusId = partnerActivityTable.addData(partnerActivity);
                if (statusId ==-1){
                    Toast.makeText(getContext(), "Could not save the partner", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    // Clear boxes
                    // Go back to Subcounty View...
                }

        }
    }

    public String getSelectedRadioItemValue(RadioGroup radioGroup){
        Integer selectedButton = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton =(RadioButton) radioGroup.findViewById(selectedButton);
        String selectedValue = selectedRadioButton.getText().toString();
        return selectedValue;
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
        if (editingPartnerActivity != null){

        }
    }
}
