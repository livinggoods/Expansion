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
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu;
import com.expansion.lg.kimaru.expansion.other.DisplayDate;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
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
        educationLevel.setText("Education: "+formatEducation(Integer.valueOf(registration.getEducation())));

        //Occupation
        TextView occupation = (TextView) v.findViewById(R.id.occupation);
        occupation.setText("Current occupation: "+ registration.getOccupation());

        //brac
        TextView bracChp = (TextView) v.findViewById(R.id.bracChp);
        if (registration.getBrac() == 1 ){
            if (registration.getBracChp() == 1){
                bracChp.setText("Has worked as BRAC CHP");
            }else{
                bracChp.setText("Has worked at BRAC");
            }
        }else {
            bracChp.setText("Hasn't worked at BRAC");
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
            examResults.setText(ex);

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
        if (interview != null){
            String interviewScores = "Interview Results \n\n" +
                        "Motivation: " +interview.getMotivation() + "\t" +
                        "Community engagement: "+ interview.getCommunity() + "\n" +
                        "Mentality: " + interview.getMentality() + "\n" +
                        "Selling Skills: " + interview.getSelling() + "\n" +
                        "Interest in health: " + interview.getHealth() + "\n" +
                        "Investment: " + interview.getInvestment() + "\n" +
                        "Interpersonal Skills: " + interview.getInterpersonal()+ "\n" +
                        "Commitment: " + interview.getCommitment() + "\n" +
                        "Selling Skills "+ interview.getSelling() + "\n" +
                        "Status "+ interview.getSelling();
            interviewResults.setText(interviewScores);
            if (interview.getSelected()){
                selectedStatus.setText("Selected for training");
            }else{
                selectedStatus.setText("Not selected for training");
                selectedStatus.setOnClickListener(this);
            }
        }else{
            //Allow adding new Interview
            interviewResults.setText("No Interview. Click to interview");
            interviewResults.setOnClickListener(this);
        }
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
                fragment = new NewInterviewFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            // action with ID action_settings was selected
            case R.id.action_exam:
                fragment = new NewExamFragment();
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
                Interview interview = new Interview();
                InterviewTable interviewTable = new InterviewTable(getContext());
                interview = interviewTable.getInterviewByRegistrationId(sessionManagement.getSavedRegistration().getId());
                interview.setSelected(true);
                interviewTable.addData(interview);
                selectedStatus.setText("Selected for training");
                //Toast.makeText(getContext(), "Selected "+sessionManagement.getSavedRegistration().getName(), Toast.LENGTH_SHORT).show();
        }
    }


}
