package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.Education;
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.DisplayDate;
import com.expansion.lg.kimaru.expansion.other.SpinnersCursorAdapter;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;

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
 * {@link NewKeRegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewKeRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewKeRegistrationFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView textshow;
    EditText mName;
    EditText mPhone;
    RadioGroup mGender;
    RadioButton gender;
    EditText editWard;
    EditText mVillage;
    EditText mMark;
    EditText mLangs;
    EditText mOccupation;
    EditText mComment;
    EditText mDob;
    RadioGroup mReadEnglish;
    EditText mRecruitment;
    EditText mDateMoved, editBranchTransportCost, editRecruitmentTransportCost;
    RadioGroup editIsChv, editIsGokTrained;
    RadioGroup mCommunity, mAccounts;
    Spinner editCuName;
    Spinner selectLinkFacility;
    EditText editLinkFacility;
    EditText editNoOfHouseholds;
    EditText editOtherTrainings;
    Spinner educationLevel;
    Spinner selectChew;

    Button buttonSave, buttonList;

    private String referralTitle = "";
    private String referralName = "";
    private String referralPhone = "";


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    SessionManagement session;
    String userName, userEmail, recruitmentId, linkName;
    HashMap<String, String> user;
    Integer userId;
    Registration editingRegistration = null;
    CommunityUnit cu = null;

    List<ChewReferral> chewReferralList = new ArrayList<ChewReferral>();
    List<String> chewReferrals = new ArrayList<String>();

    List<CommunityUnit> communityUnitList = new ArrayList<CommunityUnit>();
    List<String> communityUnits = new ArrayList<String>();

    List<LinkFacility> linkFacilityList = new ArrayList<LinkFacility>();
    List<String> linkFacilities = new ArrayList<String>();

    List<Education> educationList = new ArrayList<Education>();

    public NewKeRegistrationFragment() {
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
    public static NewKeRegistrationFragment newInstance(String param1, String param2) {
        NewKeRegistrationFragment fragment = new NewKeRegistrationFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_ke_registration, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_REGISTRATION;

                //check if Recruitment is set
        session = new SessionManagement(getContext());
        session.checkRecruitment();

        //we can now extract User details
        user = session.getUserDetails();
        //name
        userName = user.get(SessionManagement.KEY_NAME);
        //Emails
        userEmail = user.get(SessionManagement.KEY_EMAIL);
        userId = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
        Recruitment recruitment = session.getSavedRecruitment();
        recruitmentId = recruitment.getId();
        cu = session.getSavedCommunityUnit();
        if (cu == null){
            MainActivity.backFragment = new RegistrationsFragment();
        }else{
            RegistrationsFragment registrationsFragment = new RegistrationsFragment();
            registrationsFragment.communityUnit = cu;
            MainActivity.backFragment = registrationsFragment;
        }

        //Initialize the UI Components
        mName = (EditText) v.findViewById(R.id.editName);
        mPhone = (EditText) v.findViewById(R.id.editPhone);
        mGender = (RadioGroup) v.findViewById(R.id.editGender);
        mVillage = (EditText) v.findViewById(R.id.editVillage);
        mMark = (EditText) v.findViewById(R.id.editLandmark);
        mLangs = (EditText) v.findViewById(R.id.editOtherlanguages);
        mOccupation = (EditText) v.findViewById(R.id.editOccupation);
        mDob = (EditText) v.findViewById(R.id.editDob);
        mReadEnglish = (RadioGroup) v.findViewById(R.id.editReadEnglish);
        selectChew = (Spinner) v.findViewById(R.id.selectChewReferral);
        mComment = (EditText) v.findViewById(R.id.editComment);
        editWard = (EditText) v.findViewById(R.id.editWard);
        editCuName = (Spinner) v.findViewById(R.id.editCuName);
        selectLinkFacility = (Spinner) v.findViewById(R.id.selectLinkFacility);
        editBranchTransportCost = (EditText) v.findViewById(R.id.editBranchTransportCost);
        editRecruitmentTransportCost = (EditText) v.findViewById(R.id.editRecruitmentTransportCost);
        editLinkFacility = (EditText) v.findViewById(R.id.editLinkFacility);
        editNoOfHouseholds = (EditText) v.findViewById(R.id.editNoOfHouseholds);
        editOtherTrainings = (EditText) v.findViewById(R.id.editOtherTrainings);
        mDateMoved = (EditText) v.findViewById(R.id.editRelocated);
        mAccounts = (RadioGroup) v.findViewById(R.id.editAccounts);
        editIsChv = (RadioGroup) v.findViewById(R.id.editIsChv);
        editIsGokTrained = (RadioGroup) v.findViewById(R.id.editIsGokTrained);
        mCommunity = (RadioGroup) v.findViewById(R.id.editCommunityMembership);
        educationLevel = (Spinner) v.findViewById(R.id.selectEdducation);

        //

        addChewReferrals();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, chewReferrals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectChew.setAdapter(adapter);
        selectChew.setOnItemSelectedListener(onSelectedChewListener);

        addEducationSelectList();

        addCommunityUnits();
        ArrayAdapter<String> cuAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, communityUnits);
        cuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCuName.setAdapter(cuAdapter);
        // set the selected CU
        // In case there is no order followed
        if (cu != null){
            int x = 0;
            for (CommunityUnit c : communityUnitList){
                if (c.getId().equalsIgnoreCase(cu.getId())){
                    editCuName.setSelection(x, true);
                    break;
                }
                x++;
            }
        }

        addLinkFacilities();
        ArrayAdapter<String> linkFacilityAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, linkFacilities);
        linkFacilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectLinkFacility.setAdapter(linkFacilityAdapter);
        selectLinkFacility.setOnItemSelectedListener(onSelectedLinkFacilityListener);

        setUpEditingMode();

        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSaveRegistration);
        buttonSave.setOnClickListener(this);

        mDob.setOnClickListener(this);
        return v;
    }


    AdapterView.OnItemSelectedListener onSelectedLinkFacilityListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > chewReferralList.size() -1){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("New Link Facility");
                // add Facility Name, the rest can be edited later
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText facilityName = new EditText(getContext());
                facilityName.setHint("Link Facility Name");
                layout.addView(facilityName);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linkName = facilityName.getText().toString();
                        // save the Link Facility
                        String uuid = UUID.randomUUID().toString();
                        LinkFacility lknFacility = new LinkFacility();
                        lknFacility.setId(uuid);
                        lknFacility.setFacilityName(linkName);
                        lknFacility.setSubCountyId(session.getSavedRecruitment().getSubcounty());
                        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(getContext());
                        linkFacilityTable.addData(lknFacility);

                        // clear chews
                        linkFacilityList.clear();
                        linkFacilities.clear();
                        addLinkFacilities();
                        ArrayAdapter<String> linkFacilityAdapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, linkFacilities);
                        linkFacilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectLinkFacility.setAdapter(linkFacilityAdapter);

                        //lets set the selected
                        int x = 0;
                        for (LinkFacility e : linkFacilityList) {
                            if (e.getId().equalsIgnoreCase(uuid)){
                                selectLinkFacility.setSelection(x, true);
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


    AdapterView.OnItemSelectedListener onSelectedChewListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > chewReferralList.size() -1){
                // Show Dialog to add the Referral
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add new Referral");

                // Set up the input
                final EditText title = new EditText(getContext());
                final EditText name = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                // Context context = mapView.getContext();
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText titleBox = new EditText(getContext());
                titleBox.setHint("Title");

                //name for both KE and UG
                final EditText refName = new EditText(getContext());
                if (session.getSavedRecruitment().getCountry().equalsIgnoreCase("UG")){
                    layout.addView(titleBox);
                    refName.setHint("Referral Name");
                }else{
                    refName.setHint("CHEW Name");
                }
                layout.addView(refName);


                final EditText refPhone = new EditText(getContext());
                refPhone.setHint("Phone");
                refPhone.setInputType(InputType.TYPE_CLASS_PHONE);
                layout.addView(refPhone);

                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        referralTitle = titleBox.getText().toString();
                        referralName = refName.getText().toString();
                        referralPhone = refPhone.getText().toString();
                        // we save the referral, refresh the list and rebind the Spinner, and set selected
                        String uuid = UUID.randomUUID().toString();
                        ChewReferral chew = new ChewReferral(uuid, referralName, referralPhone, "CHEW",
                                session.getSavedRecruitment().getCountry(),
                                session.getSavedRecruitment().getId(), 0, "", "", "", "", "", "", "", "", "");
                        ChewReferralTable chewTb = new ChewReferralTable(getContext());
                        chewTb.addChewReferral(chew);

                        // clear chews
                        chewReferralList.clear();
                        chewReferrals.clear();
                        addChewReferrals();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, chewReferrals);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectChew.setAdapter(adapter);

                        //lets set the selected
                        int x = 0;
                        for (ChewReferral e : chewReferralList) {
                            if (e.getId().equalsIgnoreCase(uuid)){
                                selectChew.setSelection(x, true);
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
                //
                if (cu != null){
                    RegistrationsFragment registrationsFragment = new RegistrationsFragment();
                    registrationsFragment.communityUnit = cu;
                    Fragment fragment = registrationsFragment;
                    FragmentTransaction fragmentTransaction  = getActivity()
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS);
                    fragmentTransaction.commitAllowingStateLoss();
                }else{
                    RegistrationsFragment registrationsFragment = new RegistrationsFragment();
                    registrationsFragment.communityUnit = null;
                    Fragment fragment = registrationsFragment;
                    FragmentTransaction fragmentTransaction  = getActivity()
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            case R.id.editDob:
                DialogFragment newFragment = new DatePickerFragment().newInstance(R.id.editDob);
                newFragment.show(getFragmentManager(), "DatePicker");
                break;

            case R.id.buttonSaveRegistration:
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Long currentDate =  new Date().getTime();

                String registrationId;
                if (editingRegistration != null){
                    registrationId = editingRegistration.getId();
                }else{
                    registrationId = UUID.randomUUID().toString();
                }
                String applicantName = mName.getText().toString();
                String applicantPhone = mPhone.getText().toString();

                Integer selectedGender = mGender.getCheckedRadioButtonId();
                RadioButton genderRadioButton =(RadioButton) mGender.findViewById(selectedGender);
                String applicantGender = genderRadioButton.getText().toString();

                String applicantDistrict = "";
                String applicantSubcounty = session.getSavedRecruitment().getSubcounty();
                String applicantDivision = "";
                String applicantVillage = mVillage.getText().toString();
                String applicantMark = mMark.getText().toString();
                String applicantLangs = mLangs.getText().toString();
                String applicantEducation = String.valueOf(educationLevel.getSelectedItemId());
                String applicantOccupation = mOccupation.getText().toString();
                String applicantComment = mComment.getText().toString();
                String aDob = mDob.getText().toString();
                String applicantChewName = "";
                String applicantChewNumber = "";
                String applicantWard = editWard.getText().toString();
                String applicantCuName = communityUnitList.get(editCuName.getSelectedItemPosition()).getId(); //editCuName.getText().toString();
                String applicantLinkFacility = editLinkFacility.getText().toString();
                String noOfHouseholds = editNoOfHouseholds.getText().toString();
                String applicantOtherTrainings = editOtherTrainings.getText().toString();
                //recruitmentTransportCost,transportCostToBranch

                // Transport From branch
                String transportCostBranch = editBranchTransportCost.getText().toString();
                Long transportCostToBranch;
                if (transportCostBranch.trim().equals("")){
                    transportCostToBranch = 0L;
                }else{
                    transportCostToBranch = Long.valueOf(transportCostBranch);
                }

                // recruitment Transport
                String recruitmentTransportCostI = editRecruitmentTransportCost.getText().toString();
                Long recruitmentTransportCost;
                if (recruitmentTransportCostI.trim().equals("")){
                    recruitmentTransportCost = 0L;
                }else{
                    recruitmentTransportCost = Long.valueOf(recruitmentTransportCostI);
                }

                //////////////
                Long applicantNoOfHouseholds;
                if (noOfHouseholds.trim().equals("")){
                    applicantNoOfHouseholds = 0L;
                }else {
                    applicantNoOfHouseholds = Long.valueOf(noOfHouseholds);
                }
                Integer chv = editIsChv.getCheckedRadioButtonId();
                RadioButton isChv =(RadioButton) editIsChv.findViewById(chv);
                String isApplicantAChv = isChv.getText().toString();
                Boolean applicantIsChv = isApplicantAChv.equalsIgnoreCase("Yes");


                Integer acc = mAccounts.getCheckedRadioButtonId();
                RadioButton accounts = (RadioButton) mAccounts.findViewById(acc);
                String hasAccount = accounts.getText().toString();
                Boolean applicantAccounts = hasAccount.equalsIgnoreCase("Yes");

                Integer gok = editIsGokTrained.getCheckedRadioButtonId();
                RadioButton isTrained =(RadioButton) editIsGokTrained.findViewById(gok);
                String isApplicantTrained = isTrained.getText().toString();
                Boolean isGokTrained = isApplicantTrained.equalsIgnoreCase("Yes");

                Long applicantDob;
                try{
                    Date date = dateFormat.parse(aDob);
                    applicantDob = date.getTime();
                }catch (Exception e){
                    applicantDob = currentDate;
                }
                Integer readEnglish = mReadEnglish.getCheckedRadioButtonId();
                RadioButton readEnglishRadioButton =(RadioButton) mReadEnglish.findViewById(readEnglish);
                String canApplicantReadEnglish = readEnglishRadioButton.getText().toString();
                Integer applicantReadEnglish = canApplicantReadEnglish.equalsIgnoreCase("yes") ? 1 : 0;

                String dateMoved = mDateMoved.getText().toString();
                Long applicantDateMoved;
                if (dateMoved.trim().equals("")){
                    applicantDateMoved = 0L;
                }else {
                    applicantDateMoved = Long.valueOf(dateMoved);
                }

                Integer applicantBrac = 0;


                Integer applicantBracChp = 0; // = hasApplicantWorkedAsBracChp == "Yes" ? 1 : 0;

                String chewUuid = chewReferralList.get(selectChew.getSelectedItemPosition()).getId();

                Integer communityParticipation = mCommunity.getCheckedRadioButtonId();
                RadioButton hasCommunityParticipation =(RadioButton) mCommunity.findViewById(communityParticipation);
                String hasApplicantCommunityParticipation = hasCommunityParticipation.getText().toString();
                Integer applicantCommunity = hasApplicantCommunityParticipation.equalsIgnoreCase("yes") ? 1 : 0;

                Integer applicantAddedBy = userId;
                Integer applicantProceed = 0;
                Long applicantDateAdded = currentDate;
                Integer applicantSync = 0;
                String applicantRecruitment = recruitmentId;
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);

                // Do some validations
                if (applicantName.toString().trim().equals("")){
                    mName.requestFocus();
                    Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (applicantVillage.toString().trim().equals("")){
                    mVillage.requestFocus();
                    Toast.makeText(getContext(), "Village is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!applicantPhone.toString().trim().equals("")){
                    if (applicantPhone.toString().trim().startsWith("+")){
                        if (applicantPhone.length() != 13){
                            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                            mPhone.requestFocus();
                            return;
                        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(applicantPhone)) {
                            mPhone.requestFocus();
                            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else if (applicantPhone.length() != 10){
                        mPhone.requestFocus();
                        Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!PhoneNumberUtils.isGlobalPhoneNumber(applicantPhone)){
                        Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(applicantEducation.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Education is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save Registration
//                    Registration registration = new Registration(mName, mNumber, mEmail);
                Registration registration;
                registration = new Registration(registrationId, applicantName, applicantPhone, applicantGender,
                        applicantDistrict, applicantSubcounty, applicantDivision, applicantVillage,
                        applicantMark, applicantLangs, applicantEducation, applicantOccupation,
                        applicantComment, applicantDob, applicantReadEnglish, applicantRecruitment,
                        country, applicantDateMoved, applicantBrac, applicantBracChp, applicantCommunity,
                        applicantAddedBy, applicantProceed, applicantDateAdded, applicantSync, applicantChewName,
                        applicantChewNumber, applicantWard, applicantCuName, applicantLinkFacility,
                        applicantNoOfHouseholds, applicantIsChv, isGokTrained, applicantOtherTrainings,
                        "", "","",false, applicantAccounts, "", recruitmentTransportCost,transportCostToBranch, chewUuid);

                // Before saving, do some validations
                // Years in location should always be less than age
                if (registration.getAge() < applicantDateMoved){
                    mDateMoved.requestFocus();
                    Toast.makeText(getContext(), "The years at the location is greater than the age ", Toast.LENGTH_LONG).show();
                    return;
                }
                RegistrationTable registrationTable = new RegistrationTable(getContext());
                long id = registrationTable.addData(registration);
                if (id ==-1){
                    Toast.makeText(getContext(), "Could not save registration", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    // Clear boxes
                    mName.setText("");
                    mPhone.setText("");
                    mGender.clearCheck();
                    mVillage.setText("");
                    mMark.setText("");
                    mLangs.setText("");
                    educationLevel.setSelection(0);
                    selectChew.setSelection(0);
                    mOccupation.setText("");
                    mDob.setText("");
                    editWard.setText("");
                    //editCuName.setSelection(0);
                    editLinkFacility.setText("");
                    editNoOfHouseholds.setText("");
                    editOtherTrainings.setText("");
                    editIsChv.clearCheck();
                    mAccounts.clearCheck();
                    editIsGokTrained.clearCheck();
                    mReadEnglish.clearCheck();
                    mDateMoved.setText("");
                    mCommunity.clearCheck();
                    editingRegistration = null;
                    editBranchTransportCost.setText("");
                    mComment.setText("");
                    editRecruitmentTransportCost.setText("");
                    mName.requestFocus();


                }
                break;
            case R.id.buttonSave:
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

    public void addEducationSelectList() {
        EducationTable educationTable = new EducationTable(getContext());
        SpinnersCursorAdapter cursorAdapter = new SpinnersCursorAdapter(getContext(),
                educationTable.getEducationDataCursor(user.get(SessionManagement.KEY_USER_COUNTRY)));
        educationLevel.setAdapter(cursorAdapter);
        // add education details
        Cursor cursor = educationTable.getEducationDataCursor(user.get(SessionManagement.KEY_USER_COUNTRY));
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Education education=new Education();
            education.setId(cursor.getInt(0));
            education.setLevelName(cursor.getString(1));
            education.setLevelType(cursor.getString(2));
            education.setHierachy(cursor.getInt(3));
            education.setCountry(cursor.getString(4));
            educationList.add(education);
        }



    }
    public void addChewReferrals() {
        ChewReferralTable chewReferralTable = new ChewReferralTable(getContext());
        chewReferralList = chewReferralTable.getChewReferralByRecruitmentId(session.getSavedRecruitment().getId());
        for (ChewReferral chewReferral: chewReferralList){
            chewReferrals.add(chewReferral.getName());
        }
        chewReferrals.add("Add New");
    }

    public void addCommunityUnits() {
        CommunityUnitTable cuTable = new CommunityUnitTable(getContext());
        //communityUnits communityUnitList
        communityUnitList = cuTable.getCommunityUnitBySubCounty(session.getSavedRecruitment().getSubcounty());
        for (CommunityUnit cUnit: communityUnitList){
            communityUnits.add(cUnit.getCommunityUnitName());
        }
    }

    public void addLinkFacilities() {
        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(getContext());
        //communityUnits communityUnitList
        linkFacilityList = linkFacilityTable.getLinkFacilityBySubCounty(session
                .getSavedRecruitment().getSubcounty());
        for (LinkFacility lFacility: linkFacilityList){
            linkFacilities.add(lFacility.getFacilityName());
        }
        linkFacilities.add("Add New");
    }

    public void setUpEditingMode(){
        if (editingRegistration != null){
            mName.setText(editingRegistration.getName());
            mPhone.setText(editingRegistration.getPhone());
            editRecruitmentTransportCost.setText(
                    String.valueOf(editingRegistration.getRecruitmentTransportCost()));
            editBranchTransportCost.setText(
                    String.valueOf(editingRegistration.getTransportCostToBranch()));
            mGender.clearCheck();
            if (editingRegistration.getGender().equalsIgnoreCase("Male")){
                mGender.check(R.id.radioM);
            }else{
                mGender.check(R.id.radioF);
            }
            mVillage.setText(editingRegistration.getVillage());
            mMark.setText(editingRegistration.getMark());
            mLangs.setText(editingRegistration.getLangs());

//            // A dirty Hack
            int x = 0;
            for (Education e : educationList) {
                if (e.getId().equals(Integer.valueOf(editingRegistration.getEducation()))) {
                    educationLevel.setSelection(x, true);
                    break;
                }
                x++;
            }
            x = 0;
            for (ChewReferral c : chewReferralList){
                if (c.getId().equalsIgnoreCase(editingRegistration.getChewUuid())){
                    selectChew.setSelection(x, true);
                    break;
                }
                x++;
            }
            mOccupation.setText(editingRegistration.getOccupation());
            mDob.setText(new DisplayDate(Long.valueOf(editingRegistration.getDob())).widgetDateOnly());
            editWard.setText(editingRegistration.getWard());
            x = 0;
            for (CommunityUnit cu : communityUnitList){
                if (cu.getId().equalsIgnoreCase(editingRegistration.getCuName())){
                    editCuName.setSelection(x, true);
                    break;
                }
                x++;
            }
            editLinkFacility.setText(editingRegistration.getLinkFacility());
            editNoOfHouseholds.setText(String.valueOf(editingRegistration.getNoOfHouseholds()));
            editOtherTrainings.setText(editingRegistration.getOtherTrainings());
            if (editingRegistration.isChv()){
                editIsChv.check(R.id.editIsChvYes);
            }else{
                editIsChv.check(R.id.editIsChvNo);
            }
            mAccounts.check(editingRegistration.isAccounts() ? R.id.editAccountsYes : R.id.editAccountsNo);
            editIsGokTrained.check(editingRegistration.isGokTrained() ? R.id.editIsGokTrainedYes : R.id.editIsGokTrainedNo);
            mReadEnglish.check(editingRegistration.getReadEnglish().equals(1) ? R.id.radioCanReadEnglish : R.id.radioCannotReadEnglish);
            mDateMoved.setText(editingRegistration.getDateMoved().toString());
            mCommunity.check(editingRegistration.getCommunity().equals(1) ? R.id.radioCommMbrYes : R.id.radioCommMbrNo);
            mName.requestFocus();
            editBranchTransportCost.setText(String.valueOf(editingRegistration.getTransportCostToBranch()));
            editRecruitmentTransportCost.setText(String.valueOf(editingRegistration.getRecruitmentTransportCost()));
        }
    }
}
