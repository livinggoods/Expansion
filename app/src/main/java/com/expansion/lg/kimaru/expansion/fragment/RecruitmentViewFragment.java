package com.expansion.lg.kimaru.expansion.fragment;

/**
 * Created by kimaru on 3/11/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.TestMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;


import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.dbhelpers.CommunityUnitListAdapter;
import com.expansion.lg.kimaru.expansion.dbhelpers.RecruitmentListAdapter;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// to show list in Gmail Mode
public class RecruitmentViewFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecruitmentsFragment.OnFragmentInteractionListener mListener;
    TextView recruitmentMainLocation, recruitmentSecondaryLocation, txtRegistrations;
    TextView recruitmentRegSummary;

    AppCompatActivity a = new AppCompatActivity();

    SessionManagement session;
    HashMap<String, String> user;
    List<Registration> registrations = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecruitmentViewFragment newInstance(String param1, String param2) {
        RecruitmentViewFragment fragment = new RecruitmentViewFragment();
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
        View v =  inflater.inflate(R.layout.recruitment_content, container, false);

        MainActivity.CURRENT_TAG = MainActivity.TAG_RECRUITMENTS;
        MainActivity.backFragment = new HomeFragment();
        //session Management
        session = new SessionManagement(getContext());
        user = session.getUserDetails();

        // initialize the UI
        recruitmentMainLocation = (TextView) v.findViewById(R.id.recruitmentMainLocation);
        recruitmentSecondaryLocation = (TextView) v.findViewById(R.id.recruitmentSecondaryLocation);
        registrations = new RegistrationTable(getContext())
                .getRegistrationsByRecruitment(session.getSavedRecruitment());
        txtRegistrations = (TextView) v.findViewById(R.id.registrations);
        txtRegistrations.setText(registrations.size()+" Registrations");
        int passed =0, failed = 0, waiting = 0;
        for (Registration registration:registrations){
            //get the interview for the registration
            Integer selected = new InterviewTable(getContext())
                    .getInterviewByRegistrationId(registration.getId()).getSelected();
            switch (selected){
                case 1:
                    passed +=1;
                    break;
                case 2:
                    waiting +=1;
                    break;
                case 0:
                    failed +=1;
                    break;
            }

        }
        String summary = String.valueOf(passed) + " Selected\n"+ String.valueOf(waiting) + " Waiting" +
                " list\n" + String.valueOf(failed) + " Not selected";
        recruitmentRegSummary = (TextView) v.findViewById(R.id.recruitmentRegSummary);
        recruitmentRegSummary.setText(summary);


        if (session.getSavedRecruitment().getCountry().equalsIgnoreCase("UG")){
            recruitmentMainLocation.setText(session.getSavedRecruitment().getName());
            recruitmentSecondaryLocation.setText(session.getSavedRecruitment().getDistrict());
        }else{
            recruitmentMainLocation.setText(session.getSavedRecruitment().getSubcounty());
            recruitmentSecondaryLocation.setText(session.getSavedRecruitment().getCounty());
        }




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
}
