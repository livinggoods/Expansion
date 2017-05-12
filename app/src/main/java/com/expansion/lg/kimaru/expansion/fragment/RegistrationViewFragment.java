package com.expansion.lg.kimaru.expansion.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Education;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu;
import com.expansion.lg.kimaru.expansion.other.DisplayDate;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 3/30/17.
 */

public class RegistrationViewFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    SessionManagement sessionManagement;
    Registration registration;
    TextView interviewResults, examResults, selectedStatus;
    Spinner selectionStatus;
    Integer selected = 0;
    Boolean examPass = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_view_registration, container, false);
        MainActivity.CURRENT_TAG = MainActivity.REGISTRATION_VIEW;
        MainActivity.backFragment = new RegistrationsFragment();
        // Retrieve saved details
        sessionManagement = new SessionManagement(getContext());
        //get the details of the registration
        registration = sessionManagement.getSavedRegistration();

        TextView profileName = (TextView) v.findViewById(R.id.user_profile_name);
        selectionStatus = (Spinner) v.findViewById(R.id.editSelectedStatus);


        profileName.setText(registration.getName() + "( " + registration.getGender() + ")");
        //user_profile_short_bio
        TextView bio = (TextView) v.findViewById(R.id.user_profile_short_bio);
        bio.setText("Aged "+ registration.getAge()
                + " years.  Phone " + registration.getPhone()
            );
        //location
        TextView viewlocation = (TextView) v.findViewById(R.id.viewlocation);
        viewlocation.setText("Lived at " + registration.getVillage() +
                " for  " + registration.getDateMoved() + " years");

        //education
        TextView educationLevel = (TextView) v.findViewById(R.id.educationLevel);
        EducationTable educationTable = new EducationTable(getContext());
        Education education = educationTable.getEducationById(Integer.valueOf(registration.getEducation()));
        if (education != null){

            if (registration.getCountry().equalsIgnoreCase("KE")){
                educationLevel.setText("Education Level: "+ education.getLevelName() + "\n\n" +
                        "Other Trainings\n" +registration.getOtherTrainings());

            }else{
                educationLevel.setText("Education Level: "+education.getLevelName());
            }
        }else{
            educationLevel.setText("");
        }


        //Occupation
        TextView occupation = (TextView) v.findViewById(R.id.occupation);
        occupation.setText("Current occupation: "+ registration.getOccupation());

        //brac
        TextView bracChp = (TextView) v.findViewById(R.id.bracChp);
        if (registration.getCountry().equalsIgnoreCase("UG")){
            if (registration.getBrac() == 1 ){
                if (registration.getBracChp() == 1){
                    bracChp.setText("Has worked as BRAC CHP");
                }else{
                    bracChp.setText("Has worked at BRAC");
                }
            }else {
                bracChp.setText("Hasn't worked at BRAC");
            }
        }else{
            String txt="";
            txt += (registration.isGokTrained() ? "GoK Trained\n" : "Not a GoK Trained\n");
            txt += (registration.isChv() ? "Currently a CHV\n" : "Not a CHV\n");
            txt += ("Households " +registration.getNoOfHouseholds()+"\n");
            bracChp.setText(txt);

        }
        Exam exam = new Exam();
        ExamTable examTable = new ExamTable(getContext());
        exam = examTable.getExamByRegistration(sessionManagement.getSavedRegistration().getId());

        examResults = (TextView) v.findViewById(R.id.examResults);
        examResults.setText("");
        if (exam != null){
            Double total = exam.getEnglish() + exam.getMath() + exam.getPersonality();
            String ex = "Exam Results " +"\n\n" +
                        "English: " +exam.getEnglish() + "\n" +
                        "Maths: "+ exam.getMath() + "\n" +
                        "About: " + exam.getPersonality() + "\n" +
                        "Total " + total +"\n\n"+
                        "Status " + (exam.hasPassed() ? "Passed" : "Failed");

            String regComment = registration.getComment();
            examResults.setText(regComment + "\n\n\n" +ex);
            examPass = exam.hasPassed();

        }else{
            //Allow adding new Exam
            examResults.setText("No Exam taken");
            examResults.setOnClickListener(this);
        }

        //Interviews
        Interview interview = new Interview();
        InterviewTable interviewTable = new InterviewTable(getContext());
        interview = interviewTable.getInterviewByRegistrationId(sessionManagement.getSavedRegistration().getId());
        interviewResults = (TextView) v.findViewById(R.id.interviewResults);
        selectedStatus = (TextView) v.findViewById(R.id.selectedStatus);
        interviewResults.setText("");
        selectedStatus.setText("");
        selectedStatus.setOnClickListener(this);
        if (interview != null){
            String interviewScores = "Interview Results \n\n" +
                        "Comments\n\n: " +interview.getComment() + "\n\n" +
                        "Motivation: " +interview.getMotivation() + "\n" +
                        "Community engagement: "+ interview.getCommunity() + "\n" +
                        "Mentality: " + interview.getMentality() + "\n" +
                        "Selling Skills: " + interview.getSelling() + "\n" +
                        "Interest in health: " + interview.getHealth() + "\n" +
                        "Investment: " + interview.getInvestment() + "\n" +
                        "Interpersonal Skills: " + interview.getInterpersonal()+ "\n" +
                        "Commitment: " + interview.getCommitment() + "\n" +
                        "Selling Skills "+ interview.getSelling() + "\n\n" +
                        "Interview Status "+ (interview.hasPassed() ? "Passed":"Failed")+"\n\n\n"+
                        "Overall Status "+ (interview.hasPassed() ? "Passed":"Failed");
            interviewResults.setText(interviewScores);
            if (interview.getSelected().equals(1)){
                selected = 1;
                selectedStatus.setText("Selected for training");
            }else if (interview.getSelected().equals(0)){
                selected = 0;
                selectedStatus.setText("Not selected for training");
            }else{
                selected = 2;
                selectedStatus.setText("Waiting List");
            }
            selectionStatus.setSelection(interview.getSelected());
        }else{
            //Allow adding new Interview
            interviewResults.setText("No Interview. Click to interview");
            interviewResults.setOnClickListener(this);
        }
        selectionStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toggleCandidateSelectionStatus(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }
    public String formatEducation(int educationLevel){
        if (educationLevel > 12 ){
            return "Year " + (educationLevel -12);
        }else if (educationLevel > 8 ){
            return "Form " + (educationLevel - 8);
        }else{
            return "Class " + educationLevel;
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(registration.getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        // getMenuInflater().inflate(R.menu.registration_menu, menu);
        inflater.inflate(R.menu.registration_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_interview:
                // get teh interview so that users dont reenter new data
                InterviewTable interviewTable = new InterviewTable(getContext());
                Interview interview = interviewTable.getInterviewByRegistrationId(
                        sessionManagement.getSavedRegistration().getId());
                NewInterviewFragment newInterviewFragment = new NewInterviewFragment();
                newInterviewFragment.editingInterview = interview;
                fragment = newInterviewFragment;

                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            // action with ID action_settings was selected
            case R.id.action_exam:
                ExamTable examTable = new ExamTable(getContext());
                Exam exam = examTable.getExamByRegistration(sessionManagement.getSavedRegistration().getId());
                NewExamFragment newExamFragment = new NewExamFragment();
                newExamFragment.editingExam = exam;
                fragment = newExamFragment;
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }



        return true;
    }

    @Override
    public void onClick(View view){
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        switch (view.getId()){
            case R.id.examResults:
                fragment = new NewExamFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.interviewResults:
                fragment = new NewInterviewFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.selectedStatus:
                //get the interview
                Interview interview;
                InterviewTable interviewTable = new InterviewTable(getContext());
                interview = interviewTable.getInterviewByRegistrationId(sessionManagement.getSavedRegistration().getId());
                if (interview != null){
                    if (selected.equals(1)){
                        selected = 0;
                        interview.setSelected(0);
                        interviewTable.addData(interview);
                        selectedStatus.setText("Not selected for training \n Click to select");
                    }else{
                        selected = 1;
                        interview.setSelected(1);
                        interviewTable.addData(interview);
                        selectedStatus.setText("Selected for training \n Click to deselect");
                    }
                }

        }
    }
    public void toggleCandidateSelectionStatus(Integer status){
        Interview interview;
        InterviewTable interviewTable = new InterviewTable(getContext());
        interview = interviewTable.getInterviewByRegistrationId(sessionManagement.getSavedRegistration().getId());
        if (interview != null){
            if (status.equals(1)){ // the person has been selected
                selected = 1;
                interview.setSelected(1);
                interviewTable.addData(interview);
                selectedStatus.setText("Selected for Training");
            }else if (status.equals(0)){
                selected = 0;
                interview.setSelected(0);
                interviewTable.addData(interview);
                selectedStatus.setText("Not selected for training");
            } else {
                selected = 2;
                interview.setSelected(2);
                interviewTable.addData(interview);
                selectedStatus.setText("Waiting List");
            }
        }
    }



}
