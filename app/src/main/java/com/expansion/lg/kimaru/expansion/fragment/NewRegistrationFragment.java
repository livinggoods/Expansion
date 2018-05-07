package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.util.Log;
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
import android.support.v4.app.DialogFragment;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.mzigos.Village;
import com.expansion.lg.kimaru.expansion.other.DisplayDate;
import com.expansion.lg.kimaru.expansion.other.SpinnersCursorAdapter;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.ParishTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.expansion.lg.kimaru.expansion.tables.VillageTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewRegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRegistrationFragment extends Fragment implements View.OnClickListener {
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
    //EditText editReferralName;
    //EditText editReferralNumber;
    //EditText editReferralTitle;
    RadioGroup editVht;
    //EditText editSubCounty;
    //EditText editParish;

    EditText mDistrict;
    EditText mDivision;
    //EditText mVillage;
    EditText mMark;
    EditText mLangs;
    EditText mOccupation;
    EditText mComment;
    EditText mDob, mAge;
    RadioGroup mReadEnglish;
    EditText mRecruitment;
    EditText mDateMoved;
    RadioGroup mBrac, mAccounts;
    RadioGroup mBracChp;
    RadioGroup mCommunity, chooseAgeFormat;
    EditText mAddedBy;
    EditText mProceed;
    EditText mDateAdded;
    EditText mSync;
    Spinner educationLevel, selectChew, selectSubCounty, selectParish, selectVillage;

    Button buttonSave, buttonList;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    Integer loggedInUser = 1;

    SessionManagement session;
    String userName, userEmail, recruitmentId;
    HashMap<String, String> user;
    Integer userId;
    Registration editingRegistration = null;

    List<ChewReferral> chewReferralList = new ArrayList<ChewReferral>();
    List<String> chewReferrals = new ArrayList<String>();

    List<CountyLocation> countyLocationList = new ArrayList<CountyLocation>();
    List<String> locations = new ArrayList<String>();

    List<Parish> parishList = new ArrayList<Parish>();
    List<String> parishes = new ArrayList<String>();

    List<Village> villageList = new ArrayList<Village>();
    List<String> villages = new ArrayList<String>();

    private String referralTitle = "";
    private String referralName = "";
    private String referralPhone = "";


    public NewRegistrationFragment() {
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
    public static NewRegistrationFragment newInstance(String param1, String param2) {
        NewRegistrationFragment fragment = new NewRegistrationFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_registration, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_REGISTRATION;
        MainActivity.backFragment = new RegistrationsFragment();
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

        //Initialize the UI Components
        mName = (EditText) v.findViewById(R.id.editName);
        mPhone = (EditText) v.findViewById(R.id.editPhone);
        mGender = (RadioGroup) v.findViewById(R.id.editGender);
        //mVillage = (EditText) v.findViewById(R.id.editVillage);
        mMark = (EditText) v.findViewById(R.id.editLandmark);
        mLangs = (EditText) v.findViewById(R.id.editOtherlanguages);
        mOccupation = (EditText) v.findViewById(R.id.editOccupation);
        mDob = (EditText) v.findViewById(R.id.editDob);
        mAge = (EditText) v.findViewById(R.id.editAge);
        selectChew = (Spinner) v.findViewById(R.id.selectChewReferral);
        selectSubCounty = (Spinner) v.findViewById(R.id.selectSubCounty);
        mComment = (EditText) v.findViewById(R.id.editComment);
        mReadEnglish = (RadioGroup) v.findViewById(R.id.editReadEnglish);
        mDateMoved = (EditText) v.findViewById(R.id.editRelocated);
        mBrac = (RadioGroup) v.findViewById(R.id.editWorkedWithBrac);
        mAccounts = (RadioGroup) v.findViewById(R.id.editAccounts);
        mBracChp = (RadioGroup) v.findViewById(R.id.editBracChp);
        mCommunity = (RadioGroup) v.findViewById(R.id.editCommunityMembership);
        educationLevel = (Spinner) v.findViewById(R.id.selectEdducation);
        //editReferralName = (EditText) v.findViewById(R.id.editReferralName);
        //editReferralNumber = (EditText) v.findViewById(R.id.editReferralNumber);
        //editReferralTitle = (EditText) v.findViewById(R.id.editReferralTitle);
        // editSubCounty = (EditText) v.findViewById(R.id.editSubCounty);
        //editParish = (EditText) v.findViewById(R.id.editParish);
        editVht = (RadioGroup) v.findViewById(R.id.editVht);
        // mAge.setVisibility(View.GONE);
        mDob.setVisibility(View.GONE);
        chooseAgeFormat = (RadioGroup) v.findViewById(R.id.chooseAgeFormat);
        chooseAgeFormat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioAge:
                        mAge.setVisibility(View.VISIBLE);
                        mDob.setVisibility(View.GONE);
                        break;
                    case R.id.radioYear:
                        mDob.setVisibility(View.VISIBLE);
                        mAge.setVisibility(View.GONE);
                        break;
                }
            }
        });


        addChewReferrals();
        addSubCounties();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, chewReferrals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectChew.setAdapter(adapter);
        selectChew.setOnItemSelectedListener(onSelectedChewListener);

        bindSubCountyAdapter();
        selectSubCounty.setOnItemSelectedListener(onSelectedSubCounty);

        //Parish must come after the SubCounty
        selectParish =   (Spinner) v.findViewById(R.id.selectParish);
        bindParishAdapter();
        selectParish.setOnItemSelectedListener(onParishSelected);

        selectVillage =   (Spinner) v.findViewById(R.id.selectVillage);

        addEducationSelectList();
        setUpEditingMode();

        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSaveRegistration);
        buttonSave.setOnClickListener(this);

        mDob.setOnClickListener(this);
        return v;
    }

    AdapterView.OnItemSelectedListener onParishSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //populate the villages
            villageList.clear();
            villages.clear();
            villageList = new VillageTable(getContext()).getVillageDataByParishId(parishList.get(selectParish.getSelectedItemPosition()).getId());
            for (Village v:villageList){
                villages.add(v.getVillageName());
            }
            bindVillageAdapter();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener onSelectedSubCounty = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //here we populate the Parishes.
            //rebind the adapter
            bindParishAdapter();
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
                layout.addView(titleBox);

                final EditText refName = new EditText(getContext());
                refName.setHint("Referral Name");
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
                        ChewReferral chew = new ChewReferral(uuid, referralName, referralPhone, referralTitle,
                                session.getSavedRecruitment().getCountry(),
                                session.getSavedRecruitment().getId(), 0,
                                session.getSavedRecruitment().getCounty(),
                                session.getSavedRecruitment().getDistrict(),
                                session.getSavedRecruitment().getSubcounty(), "", "", "", "", "", "");

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
            if (selectChew.getSelectedItemPosition() > chewReferralList.size() -1){
                showDialog();
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void showDialog(){
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
        layout.addView(titleBox);

        final EditText refName = new EditText(getContext());
        refName.setHint("Referral Name");
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
                ChewReferral chew = new ChewReferral(uuid, referralName, referralPhone, referralTitle,
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

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.buttonList:
                Fragment fragment = new RegistrationsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_REGISTRATION);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.editDob:
                DialogFragment newFragment = new DatePickerFragment().newInstance(R.id.editDob);
                newFragment.show(getFragmentManager(), "DatePicker");
                break;
            case R.id.selectChewReferral:
                if (selectChew.getSelectedItemPosition() > chewReferralList.size() -1){
                    showDialog();
                }


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


                Integer selectedVht = editVht.getCheckedRadioButtonId();
                RadioButton vht =(RadioButton) editVht.findViewById(selectedVht);
                boolean isVht = vht.getText().toString().equalsIgnoreCase("Yes");


                String referralName = ""; //editReferralName.getText().toString();
                String referralNumber = ""; //editReferralNumber.getText().toString();
                String referralTitle = ""; //editReferralTitle.getText().toString();
                // String applicantParish = editParish.getText().toString();


                String applicantDistrict = "";
                // String applicantSubcounty = editSubCounty.getText().toString();
                String applicantDivision = "";
                // String applicantVillage = mVillage.getText().toString();
                String applicantMark = mMark.getText().toString();
                String applicantLangs = mLangs.getText().toString();
                String applicantEducation = String.valueOf(educationLevel.getSelectedItemId());
                String applicantOccupation = mOccupation.getText().toString();
                String applicantComment = mComment.getText().toString();
                String aDob = mDob.getText().toString();
                String strApplicantAge = "";
                Long applicantDob;
                if (chooseAgeFormat.getCheckedRadioButtonId() == R.id.radioAge){
                    // convert the age to years
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    int age = Integer.valueOf(mAge.getText().toString());
                    int birthYear = year-age;
                    String birthYyear = String.valueOf(birthYear)+"/1/1";
                    try{
                        Date date = dateFormat.parse(birthYyear);
                        applicantDob = date.getTime();
                    }catch (Exception e){
                        applicantDob = currentDate;
                    }
                }else{
                    try{
                        Date date = dateFormat.parse(aDob);
                        applicantDob = date.getTime();
                    }catch (Exception e){
                        applicantDob = currentDate;
                    }
                }
                Integer readEnglish = mReadEnglish.getCheckedRadioButtonId();
                RadioButton readEnglishRadioButton =(RadioButton) mReadEnglish.findViewById(readEnglish);
                String canApplicantReadEnglish = readEnglishRadioButton.getText().toString();
                Integer applicantReadEnglish = canApplicantReadEnglish.equalsIgnoreCase("Yes") ? 1 : 0;

                String dateMoved = mDateMoved.getText().toString();
                Long applicantDateMoved;
                if (dateMoved.toString().trim().equals("")){
                    applicantDateMoved = 0L;
                }else {
                    applicantDateMoved = Long.valueOf(dateMoved);
                }

                Integer workedBrac = mBrac.getCheckedRadioButtonId();
                RadioButton hasWorkAtBrac =(RadioButton) mBrac.findViewById(workedBrac);
                String hasApplicantWorkedAtBrac = hasWorkAtBrac.getText().toString();
                Integer applicantBrac;
                if (hasApplicantWorkedAtBrac.equalsIgnoreCase("Yes")){
                    applicantBrac = 1;
                }else{
                    applicantBrac = 0;
                }

                Integer workedBracAsChp = mBracChp.getCheckedRadioButtonId();
                RadioButton hasWorkAsBracChp =(RadioButton) mBracChp.findViewById(workedBracAsChp);
                String hasApplicantWorkedAsBracChp = hasWorkAsBracChp.getText().toString();
                Integer applicantBracChp; // = hasApplicantWorkedAsBracChp == "Yes" ? 1 : 0;
                if (hasApplicantWorkedAsBracChp.equalsIgnoreCase("Yes")){
                    applicantBracChp =1;
                }else{
                    applicantBracChp = 0;
                }

                Integer communityParticipation = mCommunity.getCheckedRadioButtonId();
                RadioButton hasCommunityParticipation =(RadioButton) mCommunity.findViewById(communityParticipation);
                String hasApplicantCommunityParticipation = hasCommunityParticipation.getText().toString();
                Integer applicantCommunity = hasApplicantCommunityParticipation.equalsIgnoreCase("Yes") ? 1 : 0;

                Integer applicantAddedBy = userId;
                Integer applicantProceed = 0;
                Long applicantDateAdded = currentDate;
                Integer applicantSync = 0;
                String applicantRecruitment = recruitmentId;
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);

                // Do some validations
                if (applicantName.trim().equals("")){
                    mName.requestFocus();
                    Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                //if (applicantVillage.trim().equals("")){
                //    mVillage.requestFocus();
                //    Toast.makeText(getContext(), "Village is required", Toast.LENGTH_SHORT).show();
                //    return;
                //}
                if (!applicantPhone.trim().equals("")){
                    if (applicantPhone.trim().startsWith("+")){
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
                if(applicantEducation.trim().equals("")){
                    Toast.makeText(getContext(), "Education is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                String chewUuid = chewReferralList.get(selectChew.getSelectedItemPosition()).getId();
                String parishId = parishList.get(selectParish.getSelectedItemPosition()).getId();
                String villageId = villageList.get(selectVillage.getSelectedItemPosition()).getId();
                String subCountyId = countyLocationList.get(selectSubCounty.getSelectedItemPosition()).getId().toString();
                // Save Registration //String subCountyId, String parishId, String villageId
//                    Registration registration = new Registration(mName, mNumber, mEmail);
                Registration registration;
                registration = new Registration(registrationId, applicantName, applicantPhone, applicantGender,
                        applicantDistrict, "", applicantDivision, "",
                        applicantMark, applicantLangs, applicantEducation, applicantOccupation,
                        applicantComment, applicantDob, applicantReadEnglish, applicantRecruitment,
                        country, applicantDateMoved, applicantBrac, applicantBracChp, applicantCommunity,
                        applicantAddedBy, applicantProceed, applicantDateAdded, applicantSync, "",
                        "", "", "", "", 0L,
                        false, false, "", referralName, referralTitle,
                        referralNumber, isVht, false, "",
                        0L, 0L, chewUuid, "",
                        subCountyId, parishId, villageId);

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
                    //mVillage.setText("");
                    mMark.setText("");
                    mLangs.setText("");
                    mOccupation.setText("");
                    mComment.setText("");
                    mDob.setText("");
                    mDateMoved.setText("");
                    mName.requestFocus();
                    // editReferralName.setText("");
                    // editReferralTitle.setText("");
                    // editReferralNumber.setText("");
                    //editParish.setText("");
                    editVht.clearCheck();
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

    }
    public void setUpEditingMode(){
        if (editingRegistration != null){
            mName.setText(editingRegistration.getName());
            mPhone.setText(editingRegistration.getPhone());
            //mVillage.setText(editingRegistration.getVillage());
            mMark.setText(editingRegistration.getMark());
            mLangs.setText(editingRegistration.getLangs());
            mOccupation.setText(editingRegistration.getOccupation());
            mDob.setText(new DisplayDate(Long.valueOf(editingRegistration.getDob())).widgetDateOnly());
            mDateMoved.setText(editingRegistration.getDateMoved().toString());
            //editReferralName.setText(editingRegistration.getReferralName());
            //editReferralTitle.setText(editingRegistration.getReferralTitle());
            //editReferralNumber.setText(editingRegistration.getReferralPhone());
            //editParish.setText(editingRegistration.getParish());
            //editSubCounty.setText(editingRegistration.getSubcounty());
            mComment.setText(editingRegistration.getComment());
            editVht.clearCheck();
            editVht.check(editingRegistration.isVht() ? R.id.editVhtYes : R.id.editVhtNo);

            mGender.clearCheck();
            mGender.check(editingRegistration.getGender().equalsIgnoreCase("Male") ? R.id.radioM : R.id.radioF);



            mReadEnglish.clearCheck();
            mReadEnglish.check(editingRegistration.getReadEnglish().equals(1) ?
                    R.id.radioCanReadEnglish: R.id.radioCannotReadEnglish);

            mBrac.clearCheck();
            mBrac.check(editingRegistration.getBrac().equals(1) ? R.id.radiobracYes : R.id.radiobracNo);

            mBracChp.clearCheck();
            mBracChp.check(editingRegistration.getBracChp() == 1 ?
                    R.id.radiobracChpYes : R.id.radiobracChpNo);


            mCommunity.clearCheck();
            mCommunity.check(editingRegistration.getCommunity().equals(1) ?
                    R.id.radioCommMbrYes : R.id.radioCommMbrNo);

            educationLevel.setSelection(Integer.valueOf(editingRegistration.getEducation()) - 1, true);
            int x = 0;
            for (ChewReferral c : chewReferralList){
                if (c.getId().equalsIgnoreCase(editingRegistration.getChewUuid())){
                    selectChew.setSelection(x, true);
                    break;
                }
                x++;
            }
            //SubCounty
            x=0;
            for (CountyLocation cv: countyLocationList){
                if (cv.getId().equals(editingRegistration.getSubCountyId())){
                    selectSubCounty.setSelection(x, true);
                    break;
                }
            }
            //Parish
            x=0;
            for (Parish p: parishList){
                if (p.getId().equalsIgnoreCase(editingRegistration.getParishId())){
                    selectParish.setSelection(x, true);
                    break;
                }
            }

            //village
            x=0;
            for (Village v: villageList){
                if (v.getId().equalsIgnoreCase(editingRegistration.getVillageId())){
                    selectVillage.setSelection(x, true);
                    break;
                }
            }


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

    public void addSubCounties() {
        CountyLocationTable countyLocationTable = new CountyLocationTable(getContext());
        countyLocationList = countyLocationTable.getChildrenLocations(session.getSavedRecruitment().getCountyId().toString());
        for (CountyLocation loc: countyLocationList){
            locations.add(loc.getName());
        }
    }

    public void bindVillageAdapter(){
        villages.clear();
        villageList.clear();
        villageList = new VillageTable(getContext()).getVillageDataByParishId(parishList.get(
                selectParish.getSelectedItemPosition()).getId());
        for (Village v : villageList){
            villages.add(v.getVillageName());
        }
        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, villages);
        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectVillage.setAdapter(villageAdapter);

    }

    public void bindSubCountyAdapter(){
        ArrayAdapter<String> countyLocationAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, locations);
        countyLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSubCounty.setAdapter(countyLocationAdapter);
    }

    public void bindParishAdapter(){
        parishes.clear();
        parishList.clear();
        parishList = new ParishTable(getContext()).getParishByParent(countyLocationList.get(selectSubCounty.getSelectedItemPosition()).getId().toString());
        for (Parish p : parishList){
            parishes.add(p.getName());
        }
        ArrayAdapter<String> parishAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, parishes);
        parishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectParish.setAdapter(parishAdapter);
    }
}
