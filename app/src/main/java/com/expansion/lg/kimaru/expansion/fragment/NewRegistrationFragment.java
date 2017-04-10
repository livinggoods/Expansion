package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.DisplayDate;
import com.expansion.lg.kimaru.expansion.other.SpinnersCursorAdapter;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    RadioButton gender;
    EditText mDistrict;
    EditText mSubcounty;
    EditText mDivision;
    EditText mVillage;
    EditText mMark;
    EditText mLangs;
    EditText mOccupation;
    EditText mComment;
    EditText mDob;
    RadioGroup mReadEnglish;
    EditText mRecruitment;
    EditText mDateMoved;
    RadioGroup mBrac;
    RadioGroup mBracChp;
    RadioGroup mCommunity;
    EditText mAddedBy;
    EditText mProceed;
    EditText mDateAdded;
    EditText mSync;
    Spinner educationLevel;

    Button buttonSave, buttonList;


    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    Integer loggedInUser = 1;

    SessionManagement session;
    String userName, userEmail, recruitmentId;
    HashMap<String, String> user;
    Integer userId;
    Registration editingRegistration = null;


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
        mVillage = (EditText) v.findViewById(R.id.editVillage);
        mMark = (EditText) v.findViewById(R.id.editLandmark);
        mLangs = (EditText) v.findViewById(R.id.editOtherlanguages);
        mOccupation = (EditText) v.findViewById(R.id.editOccupation);
        mDob = (EditText) v.findViewById(R.id.editDob);
        mReadEnglish = (RadioGroup) v.findViewById(R.id.editReadEnglish);
        mDateMoved = (EditText) v.findViewById(R.id.editRelocated);
        mBrac = (RadioGroup) v.findViewById(R.id.editWorkedWithBrac);
        mBracChp = (RadioGroup) v.findViewById(R.id.editBracChp);
        mCommunity = (RadioGroup) v.findViewById(R.id.editCommunityMembership);
        educationLevel = (Spinner) v.findViewById(R.id.selectEdducation);



        addEducationSelectList();
        setUpEditingMode();

        /*
        Th
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
    }
    public void onNothingSelected(AdapterView<?> parent) {
    }
});
         */


        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        mDob.setOnClickListener(this);
        mDateMoved.setOnClickListener(this);
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

//            case R.id.editRelocated:
//                DialogFragment dateMovedFragment = new DatePickerFragment().newInstance(R.id.editRelocated);
//                dateMovedFragment.show(getFragmentManager(), "Datepicker");
//                break;
//            mName, mPhone, mGender, mDistrict,
//                    mSubcounty, mDivision, mVillage, mMark, mLangs, mEducation, mOccupation,
//                    mComment, mDob, mReadEnglish, mRecruitment, mDateMoved,
//                    mBrac, mBracChp, mCommunity, mAddedBy, mProceed, mDateAdded, mSynced
            case R.id.buttonSave:
                // set date as integers
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
                String applicantSubcounty = "";
                String applicantDivision = "";
                String applicantVillage = mVillage.getText().toString();
                String applicantMark = mMark.getText().toString();
                String applicantLangs = mLangs.getText().toString();
                String applicantEducation = String.valueOf(educationLevel.getSelectedItemId());
                String applicantOccupation = mOccupation.getText().toString();
                String applicantComment = "";
                String aDob = mDob.getText().toString();
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
                Integer applicantReadEnglish = canApplicantReadEnglish == "yes" ? 1 : 0;

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
                Integer applicantBrac = hasApplicantWorkedAtBrac == "yes" ? 1 : 0;

                Integer workedBracAsChp = mBracChp.getCheckedRadioButtonId();
                RadioButton hasWorkAsBracChp =(RadioButton) mBracChp.findViewById(workedBracAsChp);
                String hasApplicantWorkedAsBracChp = hasWorkAsBracChp.getText().toString();
                Integer applicantBracChp = hasApplicantWorkedAsBracChp == "yes" ? 1 : 0;

                Integer communityParticipation = mCommunity.getCheckedRadioButtonId();
                RadioButton hasCommunityParticipation =(RadioButton) mCommunity.findViewById(communityParticipation);
                String hasApplicantCommunityParticipation = hasCommunityParticipation.getText().toString();
                Integer applicantCommunity = hasApplicantCommunityParticipation == "yes" ? 1 : 0;

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
                }

                else if (applicantVillage.toString().trim().equals("")){
                    mVillage.requestFocus();
                    Toast.makeText(getContext(), "Village is required", Toast.LENGTH_SHORT).show();
                }
                else if (!applicantPhone.toString().trim().equals("")){
                    if (applicantPhone.toString().trim().startsWith("+")){
                        if (applicantPhone.length() != 13){
                            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                            mPhone.requestFocus();
                        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(applicantPhone)) {
                            mPhone.requestFocus();
                            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                        }
                    }else if (applicantPhone.length() != 10){
                        mPhone.requestFocus();
                        Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    }else if(!PhoneNumberUtils.isGlobalPhoneNumber(applicantPhone)){
                        Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    }
                }

                else if(applicantEducation.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Education is required", Toast.LENGTH_SHORT).show();
                } else{
                    // Save Registration
//                    Registration registration = new Registration(mName, mNumber, mEmail);
                    Registration registration;
                    registration = new Registration(registrationId, applicantName, applicantPhone, applicantGender,
                            applicantDistrict, applicantSubcounty, applicantDivision, applicantVillage,
                            applicantMark, applicantLangs, applicantEducation, applicantOccupation,
                            applicantComment, applicantDob, applicantReadEnglish, applicantRecruitment,
                            country, applicantDateMoved, applicantBrac, applicantBracChp, applicantCommunity,
                            applicantAddedBy, applicantProceed, applicantDateAdded, applicantSync);

                    // Before saving, do some validations
                    // Years in location should always be less than age
                    if (registration.getAge() < applicantDateMoved){
                        mDateMoved.requestFocus();
                        Toast.makeText(getContext(), "The years at the location is greater than the age ", Toast.LENGTH_LONG).show();
                    } else {
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
                            mVillage.setText("");
                            mMark.setText("");
                            mLangs.setText("");
                            mOccupation.setText("");
                            //mComment.setText("");
                            mDob.setText("");
                            mDateMoved.setText("");
                        }
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

    public void addEducationSelectList() {
        EducationTable educationTable = new EducationTable(getContext());
        SpinnersCursorAdapter cursorAdapter = new SpinnersCursorAdapter(getContext(), educationTable.getEducationDataCursor());
        educationLevel.setAdapter(cursorAdapter);

    }
    public void setUpEditingMode(){
        if (editingRegistration != null){
            mName.setText(editingRegistration.getName());
            mPhone.setText(editingRegistration.getPhone());
            mVillage.setText(editingRegistration.getVillage());
            mMark.setText(editingRegistration.getMark());
            mLangs.setText(editingRegistration.getLangs());
            mOccupation.setText(editingRegistration.getOccupation());
            mDob.setText(new DisplayDate(Long.valueOf(editingRegistration.getDob())).dateOnly());
            mDateMoved.setText(editingRegistration.getDateMoved().toString());

            mGender.clearCheck();
            mGender.check(editingRegistration.getGender() == "Male" ? R.id.radioM : R.id.radioF);

            mReadEnglish.clearCheck();
            mReadEnglish.check(editingRegistration.getReadEnglish() == 1 ?
                    R.id.radioCanReadEnglish: R.id.radioCannotReadEnglish);

            mBrac.clearCheck();
            mBrac.check(editingRegistration.getBrac() == 1 ? R.id.radiobracYes : R.id.radiobracNo);

            mBracChp.clearCheck();
            mBracChp.check(editingRegistration.getBracChp() == 1 ?
                    R.id.radiobracChpYes : R.id.radiobracChpNo);


            mCommunity.clearCheck();
            mCommunity.check(editingRegistration.getCommunity() == 1 ?
                    R.id.radioCommMbrYes : R.id.radioCommMbrNo);

            educationLevel.setSelection(Integer.valueOf(editingRegistration.getEducation()), true);
        }
    }
}
