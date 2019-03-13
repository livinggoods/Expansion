package com.expansion.lg.kimaru.expansion.fragment;

/**
 * Created by kimaru on 3/11/17.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

// to show list in Gmail Mode
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.Education;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.DisplayDate;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.expansion.lg.kimaru.expansion.dbhelpers.RegistrationListAdapter;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.expansion.lg.kimaru.expansion.tables.UserTable;
import com.expansion.lg.kimaru.expansion.tables.WardTable;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.fabric.sdk.android.Fabric;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationsFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TextView textshow;

    // to show list in Gmail Mode
    private List<Registration> registrations = new ArrayList<>();
    private RecyclerView recyclerView;
    private RegistrationListAdapter rAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    FloatingActionButton fab;

    CommunityUnit communityUnit = null;

    SessionManagement session;
    Recruitment recruitment;
    HashMap <String, String> user;
    String country;

    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;


    // I cant seem to get the context working
    Context mContext = getContext();
    Activity activity = getActivity();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationsFragment newInstance(String param1, String param2) {
        RegistrationsFragment fragment = new RegistrationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Fabric.with(getContext(), new Crashlytics());

        setHasOptionsMenu(true);
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
        setHasOptionsMenu(true);
        View v =  inflater.inflate(R.layout.fragment_registrations, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_REGISTRATIONS;
        MainActivity.backFragment = new RecruitmentViewFragment();
        textshow = (TextView) v.findViewById(R.id.textShow);
        //session Management
        session = new SessionManagement(getContext());
        recruitment = session.getSavedRecruitment();
        user = session.getUserDetails();
        country = user.get(SessionManagement.KEY_USER_COUNTRY);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                if (user.get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("KE")){
                    fragment = new NewKeRegistrationFragment();
                }else{
                    fragment = new NewRegistrationFragment();
                }
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "villages");
                fragmentTransaction.commitAllowingStateLoss();
            }
        });


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // onRefresh action here
                Toast.makeText(getContext(), "Refreshing the list", Toast.LENGTH_SHORT).show();
                getRegistrations();
            }
        });
        rAdapter = new RegistrationListAdapter(getContext(), registrations, new RegistrationListAdapter.RegistrationListAdapterListener() {
            @Override
            public void onIconClicked(int position) {
                Registration registration = registrations.get(position);
                session.saveRegistration(registration);
                registrations.set(position, registration);
                rAdapter.notifyDataSetChanged();
                Fragment fragment = new RegistrationViewFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "registrations");
                fragmentTransaction.commitAllowingStateLoss();
            }

            @Override
            public void onIconImportantClicked(int position) {
                Registration registration = registrations.get(position);
                session.saveRegistration(registration);
                registrations.set(position, registration);
                rAdapter.notifyDataSetChanged();
                Fragment fragment = new RegistrationViewFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "registrations");
                fragmentTransaction.commitAllowingStateLoss();
            }

            @Override
            public void onMessageRowClicked(int position) {
                // read the message which removes bold from the row
                Registration registration = registrations.get(position);
                session.saveRegistration(registration);
                registrations.set(position, registration);
                rAdapter.notifyDataSetChanged();
                Fragment fragment = new RegistrationViewFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "registrations");
                fragmentTransaction.commitAllowingStateLoss();
            }

            @Override
            public void onRowLongClicked(int position) {
                // When one long presses a registration, we give them a chance to
                // edit the selected applicant

                //extract the clicked recruitment
                Registration registration = registrations.get(position);
                session.saveRegistration(registration);
                Fragment fragment;
                if (country.equalsIgnoreCase("KE")){
                    NewKeRegistrationFragment newKeRegistrationFragment = new NewKeRegistrationFragment();
                    newKeRegistrationFragment.editingRegistration  = registration;
                    fragment = newKeRegistrationFragment;
                }else{
                    NewRegistrationFragment newRegistrationFragment = new NewRegistrationFragment();
                    newRegistrationFragment.editingRegistration  = registration;
                    fragment = newRegistrationFragment;
                }
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "interviews");

                fragmentTransaction.commitAllowingStateLoss();

            }

        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(rAdapter);
        actionModeCallback = new ActionModeCallback();
        swipeRefreshLayout.post(
                new Runnable(){
                    @Override
                    public void run(){
                        getRegistrations();
                    }
                }
        );

//        actionModeCallback = new ActionMode().Callback;


        //===========Gmail View Ends here ============================
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recruitment.getName() +" Registrations");
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


    // ===================================== Gmail View Methods ====================================
    private void enableActionMode(int position) {
        if (actionMode == null) {
            Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        rAdapter.toggleSelection(position);
        int count = rAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }
    /*
    *  Choose a random
    *  Color
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            rAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    rAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        rAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                rAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            rAdapter.removeData(selectedItemPositions.get(i));
        }
        rAdapter.notifyDataSetChanged();
    }
    private void getRegistrations() {
        swipeRefreshLayout.setRefreshing(true);

        registrations.clear();

        // clear the registrations
        try {
            // get the registrations
            // Depending on the mode
            RegistrationTable registrationTable = new RegistrationTable(getContext());
            List<Registration> registrationList = new ArrayList<>();
            if (session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("KE")){
                if (communityUnit != null){
                    registrationList = registrationTable.getRegistrationsByRecruitmentAndCommunityUnit(
                            session.getSavedRecruitment(), communityUnit);
                }else{
                    registrationList = registrationTable.getRegistrationsByRecruitment(session.getSavedRecruitment());
                }
            }else{
                registrationList = registrationTable.getRegistrationsByRecruitment(session.getSavedRecruitment());
            }

            for (Registration registration:registrationList){
                registration.setColor(getRandomMaterialColor("400"));
                registrations.add(registration);
            }
            rAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception error){
            Toast.makeText(getContext(), "No Registrations", Toast.LENGTH_SHORT).show();
            Crashlytics.log(error.toString());

            textshow.setText(" No registration recorded");
        }
        swipeRefreshLayout.setRefreshing(false);
    }



    private void getFilteredRegistrations(String which) {
        swipeRefreshLayout.setRefreshing(true);

        registrations.clear();

        // clear the registrations
        RegistrationTable registrationTable = new RegistrationTable(getContext());
        List<Registration> registrationList = new ArrayList<>();
        try {
            // get the registrations
            // Depending on the mode
            if (which == "passed"){
                registrationList = registrationTable.getPassedRegistrations(session.getSavedRecruitment(), true);
            }else if (which == "failed"){
                registrationList = registrationTable.getPassedRegistrations(session.getSavedRecruitment(), false);
            }else{
                registrationList = registrationTable.getRegistrationsByRecruitment(session.getSavedRecruitment());
            }

            for (Registration registration:registrationList){
                registration.setColor(getRandomMaterialColor("400"));
                registrations.add(registration);
            }
            rAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception error){
            Toast.makeText(getContext(), "No Registrations", Toast.LENGTH_SHORT).show();
            Crashlytics.log(error.toString());
            textshow.setText(" No registration recorded");
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getSearchedRegistrations(String query) {
        swipeRefreshLayout.setRefreshing(true);

        // clear the registrations
        registrations.clear();
        RegistrationTable registrationTable = new RegistrationTable(getContext());
        List<Registration> registrationList = new ArrayList<>();
        try {
            // get the registrations
            registrationList = registrationTable.searchRegistrations(session.getSavedRecruitment(), query);
            for (Registration registration:registrationList){
                registration.setColor(getRandomMaterialColor("400"));
                registrations.add(registration);
            }
            rAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception error){
            Toast.makeText(getContext(), "No Registrations", Toast.LENGTH_SHORT).show();
            textshow.setText(" No registration recorded");
            Crashlytics.log(error.toString());
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.registration_action_menu, menu);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        // searchView.setSuggestionsAdapter(new SearchSuggestionsAdapter(this));
//        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener()
//        {
//            @Override
//            public boolean onSuggestionClick(int position)
//            {
//                Toast.makeText(SearchActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
//                searchView.clearFocus();
//                return true;
//            }
//
//            @Override
//            public boolean onSuggestionSelect(int position)
//            {
//                return false;
//            }
//        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                getSearchedRegistrations(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_export_scoring_tool:
                // export scoring tool
                Toast.makeText(getContext(), "Exporting Scoring tool", Toast.LENGTH_SHORT).show();
                checkPermissions();
                break;
            // action with ID action_settings was selected
            case R.id.action_passed:
                // refresh the registrations
                getFilteredRegistrations("passed");
                break;

            case R.id.action_failed:
                // refresh the registrations
                getFilteredRegistrations("failed");
                break;

            case R.id.action_all:
                // refresh the registrations
                getFilteredRegistrations("all");
                break;
            case R.id.action_new:
                if (user.get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("KE")){
                    fragment = new NewKeRegistrationFragment();
                }else{
                    fragment = new NewRegistrationFragment();
                }fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "registrations");
                fragmentTransaction.commitAllowingStateLoss();
                break;

            default:
                break;
        }



        return true;
    }

    private void exportScoringTool(){
        // only export the scoring tool for the current recruitment
        // 1. Check if the Externa Storage is available
        String state = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(state)){
            return;
        }else{
            //We used Download Dir
            File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if(!exportDir.exists()){
                exportDir.mkdirs();
            }
            File file;
            PrintWriter printWriter = null;
            file = new File(exportDir, recruitment.getName()+" Scoring Tool.csv");
            try {
                file.createNewFile();
                printWriter = new PrintWriter(new FileWriter(file));

                //here we get the cursor that contains our records
                RegistrationTable registrationTable = new RegistrationTable(getContext());
                EducationTable educationTable = new EducationTable(getContext());


                if (country.equalsIgnoreCase("KE")){
                        String keHeader = "CHEW Name," +
                            "CHEW Contact," +
                            "Candidate Name," +
                            "Candidate Mobile," +
                            "Gender," +
                            "Marital Status," +
                            "Year of Birth," +
                            "Age," +
                            "Subcounty," +  // Shown as UUID, Requires a name
                            "Ward," + // Shown as UUID, Requires a name
                            "Village/zone/cell, " +
                            "Landmark, " +
                            "CU (Community Unit), " + // Shown as UUID, Requires a name
                            "Link Facility Name, " +
                            "Link Facility Code, " +
                            "No of Households," +
                            "Read/speak English," +
                            "Years at this CountyLocation," +
                            "Other Languages," +
                            "CHV," +
                            "GOK Training," +
                            "Other Trainings,"+
                            "Highest education level,"+
                            "Previous/Current health or business experience," +
                            "Community group membership," +
                            "Financial Accounts," +
                            "Recruitment Comments," +
                            "Math Score," +
                            "Reading Comprehension," +
                            "About you," +
                            "Total Score," +
                            "Eligible for Interview," +  // pass or not
                            "Interview: Overall Motivation," +
                            "Interview: Ability to work with communities," +
                            "Interview: Mentality," +
                            "Interview:Selling skills," +
                            "Interview: Interest in health," +
                            "Interview: Ability to invest," +
                            "Interview: Interpersonal skills," +
                            "Interview: Ability to commit," +
                            "Interview Score," +
                            "DO NOT ASK OUTLOUD: Any conditions to prevent joining?," + //canJoin
                            "Tranport as Per Recruitment," + //canJoin
                            "Comments," +
                            "Qualify for Training," +
                            "Completed By," +
                            "Invite for Training";  //selected

                    // add for interview and exam
                    printWriter.println(keHeader);
                    // Print the rows
                    for (Registration registration:registrations){
                        Exam exam = new ExamTable(getContext()).getExamByRegistration(registration.getId());
                        Education education = educationTable.getEducationById(Integer.valueOf(registration.getEducation()));
                        Interview interview = new InterviewTable(getContext()).getInterviewByRegistrationId(registration.getId());
                        Integer motivation = 0, community = 0, mentality = 0, selling=0;
                        Long recruitmentTransport = 0L;
                        Integer health = 0, investment = 0, interpersonal = 0, commitment = 0, totalI = 0;
                        String invite = "N", canJoin="N", userNames ="", comments = "";
                        String qualify = "N", hasPassedExam="N";
                        recruitmentTransport = registration.getRecruitmentTransportCost();
                        if (recruitmentTransport == null){
                            recruitmentTransport = 0L;
                        }
                        Double math = 0D, english = 0D, personality = 0D, total = 0D;
                        if (exam != null){
                            math = exam.getMath();
                            english = exam.getEnglish();
                            personality = exam.getPersonality();
                            total = math + english + personality;
                            hasPassedExam = exam.hasPassed() ? "Y" : "N";
                        }
                        if (interview != null){
                            try {
                                motivation = interview.getMotivation();
                                community = interview.getCommunity();
                                mentality = interview.getMentality();
                                selling = interview.getSelling();
                                health = interview.getHealth();
                                investment = interview.getInvestment();
                                interpersonal = interview.getInterpersonal();
                                commitment = interview.getCommitment();
                                totalI = interview.getTotal();
                                canJoin = interview.isCanJoin() ? "Y" : "N";
                                comments = interview.getComment();
                                qualify = interview.hasPassed() ? "Y" : "N";
                                invite = interview.getSelected().equals(1) ? "Y" : interview.getSelected().equals(0) ? "N" : "Waiting";

                            }catch (Exception e){}
                        }
                        try {
                            userNames = new UserTable(getContext()).getUserById(interview.getAddedBy()).getName();
                        }catch (Exception e){
                            Crashlytics.log(e.toString());
                        }

                        ChewReferral chewReferral = new ChewReferralTable(getContext()).getChewReferralById(registration.getChewUuid());

                        String strRegistration = chewReferral.getName() +","+
                                chewReferral.getPhone()+","+
                                registration.getName() +","+
                                registration.getPhone().replaceAll(",", ";") +","+
                                registration.getGender() +","+
                                registration.getMaritalStatus() +","+
                                new DisplayDate(registration.getDob()).dateOnly() +","+  //dd/mm/yyyy format
                                registration.getAge() +","+
                                //registration.getSubcounty() +","+
                                new SubCountyTable(getContext()).getSubCountyById(registration.getSubcounty()).getSubCountyName()+","+
                                //registration.getWard()+","+
                                new WardTable(getContext()).getWardById(registration.getWard()).getName()+","+
                                registration.getVillage()+","+
                                registration.getMark().replaceAll(",", ";") +","+
                                // registration.getCuName().replaceAll(",", ";") +","+
                                new CommunityUnitTable(getContext()).getCommunityUnitById(registration.getCuName()).getCommunityUnitName()+","+
                                new LinkFacilityTable(getContext()).getLinkFacilityById(registration.getLinkFacility()).getFacilityName() +","+
                                new LinkFacilityTable(getContext()).getLinkFacilityById(registration.getLinkFacility()).getMflCode() +","+
                                // registration.getLinkFacility().replaceAll(",", ";") +","+
                                registration.getNoOfHouseholds() +","+
                                (registration.getReadEnglish().equals(1) ? "Y" : "N") +","+
                                registration.getDateMoved() +","+
                                registration.getLangs().replaceAll(",",";") +"," +
                                (registration.isChv() ? "Y" : "N") +","+
                                (registration.isGokTrained() ? "Y": "N") +","+
                                registration.getOtherTrainings().replaceAll(",", ";") +","+
                                education.getLevelName()+ "," +
                                registration.getOccupation().replaceAll(",", ";")+ "," +
                                (registration.getCommunity().equals(1) ? "Y" : "N")+ "," +
                                (registration.isAccounts() ? "Y" : "N")+ "," +
                                registration.getComment().replaceAll(",", ";")+ "," +
                                math.toString() + ","+
                                english.toString() + ","+
                                personality.toString() + ","+
                                total.toString()+","+
                                hasPassedExam+","+ // has passed
                                motivation.toString()+","+
                                community.toString()+","+
                                mentality.toString()+","+
                                selling.toString()+","+
                                health.toString()+","+
                                investment.toString()+","+
                                interpersonal.toString()+","+
                                commitment.toString()+","+
                                totalI.toString()+","+
                                canJoin.toString()+","+
                                recruitmentTransport.toString() +","+
                                comments.trim().replaceAll(",",";").replaceAll("\n",":").replaceAll("\r",":")+","+
                                qualify.toString()+","+
                                userNames.toString()+","+
                                invite.toString();
                        printWriter.println(strRegistration);
                    }

                }else{
                    String ugHeader = "Referral Name," +
                            "Referral Title," +
                            "Referral Mobile No," +
                            "VHT?," +
                            "Candidate Name," +
                            "Candidate Mobile," +
                            "Gender," +
                            "Age," +
                            "District," +
                            "Subcounty," +
                            "Parish," +
                            "Village/zone/cell, " +
                            "Landmark, " +
                            "Read/Speak English, " +
                            "Other Languages," +
                            "Years at this location," +
                            "Ever worked with BRAC?," +
                            "If yes as BRAC CHP?," +
                            "Highest Educational," +
                            "Community group memberships," +
                            "Maths Score," +
                            "Reading Comprehension,"+
                            "About You," +
                            "Total Marks," +
                            "Eligible for Interview," +
                            "Interview Completed by," +
                            "Interview: Overall Motivation," +
                            "Interview: Ability to work with communities," +
                            "Interview: Mentality," +
                            "Interview:Selling skills," +
                            "Interview: Interest in health," +
                            "Interview: Ability to invest," +
                            "Interview: Interpersonal skills," +
                            "Interview: Ability to commit," +
                            "Interview Score," +
                            "DO NOT ASK OUTLOUD: Any conditions to prevent joining?," +
                            "Comments," +
                            "Qualify for Training," +
                            "Invite for Training";

                    // add for interview and exam
                    printWriter.println(ugHeader);
                    // Print the rows
                    for (Registration registration:registrations){
                        Exam exam = new ExamTable(getContext()).getExamByRegistration(registration.getId());
                        Interview interview = new InterviewTable(getContext()).getInterviewByRegistrationId(registration.getId());
                        Integer motivation = 0, community = 0, mentality = 0, selling=0;
                        Integer health = 0, investment = 0, interpersonal = 0, commitment = 0, totalI = 0;
                        String invite = "N", canJoin="N", userNames ="", comments = "";
                        String qualify = "N";

                        Double math = 0D, english = 0D, personality = 0D, total = 0D;
                        boolean passedRegistrationAndExam = false;
                        if (exam != null){
                            math = exam.getMath();
                            english = exam.getEnglish();
                            personality = exam.getPersonality();
                            total = math + english + personality;
                            if (registration.hasPassed() && exam.hasPassed()){
                                passedRegistrationAndExam = true;
                            }else{
                                passedRegistrationAndExam = false;
                            }

                        }
                        if (interview != null){
                            motivation = interview.getMotivation();
                            community = interview.getCommunity();
                            mentality = interview.getMentality();
                            selling = interview.getSelling();
                            health = interview.getHealth();
                            investment = interview.getInvestment();
                            interpersonal = interview.getInterpersonal();
                            commitment = interview.getCommitment();
                            totalI = interview.getTotal();
                            canJoin = interview.isCanJoin() ? "Y" : "N";
                            comments = interview.getComment();
                            qualify = interview.hasPassed() ? "Y" : "N";
                            invite = interview.getSelected().equals(1) ? "Y" : interview.getSelected().equals(0) ? "N" : "Waiting";
                            userNames = new UserTable(getContext()).getUserById(interview.getAddedBy()).getName();
                        }
                        //chewReferral.getName() +","+
                        //chewReferral.getPhone()+","+
                        ChewReferral chewReferral = new ChewReferralTable(getContext()).getChewReferralById(registration.getChewUuid());
                        String record = chewReferral.getName() +","+
                                chewReferral.getTitle() +","+
                                chewReferral.getPhone()+","+
                                (registration.isVht() ? "Y" : "N") +","+
                                registration.getName() +","+
                                registration.getPhone() +","+
                                registration.getGender() +","+
                                registration.getAge() +","+
                                recruitment.getDistrict() +","+
                                registration.getSubcounty() +","+
                                registration.getParish()+","+
                                registration.getVillage()+","+
                                registration.getMark() +","+
                                (registration.getReadEnglish().equals(1) ? "Y" : "N") +","+
                                registration.getLangs().replaceAll(",",";") +"," +
                                registration.getDateMoved() +"," +
                                (registration.getBrac().equals(1) ? "Y" : "N") +","+
                                (registration.getBracChp().equals(1) ? "Y": "N") +","+
                                registration.getEducation() +","+
                                (registration.getCommunity().equals(1) ? "Y" : "N")+ "," +
                                math.toString() + ","+
                                english.toString() + ","+
                                personality.toString() + ","+
                                total+","+
                                (passedRegistrationAndExam ? "Y" : "N")+ "," +  // Has passed Interview and Exam
                                userNames+","+
                                motivation+","+
                                community+","+
                                mentality+","+
                                selling+","+
                                health+","+
                                investment+","+
                                interpersonal+","+
                                commitment+","+
                                totalI+","+
                                canJoin+","+
                                comments.replaceAll(",", ";")+","+
                                qualify+","+
                                invite;
                        printWriter.println(record);
                    }
                }

            } catch (Exception e){
                Crashlytics.log(e.toString());
            }
            finally {
                if(printWriter != null) printWriter.close();
            }
            Toast.makeText(getContext(), "Tool exported to "+ file.getAbsolutePath() +" Folder", Toast.LENGTH_LONG).show();
        }

    }
    public void checkPermissions(){
        try{
            if(ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[0])){
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Multiple Permissions Request");
                    builder.setMessage("This app needs Location permissions");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(),permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else if (permissionStatus.getBoolean(permissionsRequired[0], false)){
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Multiple Permissions Request");
                    builder.setMessage("This app needs Location permissions");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getContext(), "Go to Permissions to Grant Location Permissions",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else {
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0],true);
                editor.commit();
            }else{
                proceedAfterPermission();
            }
        }catch (Exception e){
            Crashlytics.log(e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[0])){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(),permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                // Toast.makeText(getContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    private void proceedAfterPermission() {
        exportScoringTool();
    }

}
