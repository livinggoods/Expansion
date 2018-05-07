package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.HttpServerActivity;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.other.InternetCheck;
import com.expansion.lg.kimaru.expansion.sync.TrainingDataSync;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Handler mHandler;

    boolean internetAvailable;

    Recruitment recruitment;


    Button mapping, recruitments, sharing, graduation,
            cloud, settings;
    SessionManagement sessionManagement;
    HashMap <String, String> user;
    String country;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        MainActivity.CURRENT_TAG = MainActivity.TAG_HOME;
        MainActivity.backFragment = null;
        mHandler = new Handler();
        sessionManagement = new SessionManagement(getContext());
        user = sessionManagement.getUserDetails();
        country = sessionManagement.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY);

        //

        //mapping, recruitments, registrations, exams, interviews, sharing
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mapping = (Button) v.findViewById(R.id.btnMapping);
        recruitments = (Button) v.findViewById(R.id.btnRecruitments);
        sharing = (Button) v.findViewById(R.id.btnSharing);
        graduation = (Button) v.findViewById(R.id.btnGraduation);
        settings = (Button) v.findViewById(R.id.btnSettings);
        cloud = (Button) v.findViewById(R.id.btnCloud);


        mapping.setOnClickListener(this);
        recruitments.setOnClickListener(this);
        sharing.setOnClickListener(this);
        graduation.setOnClickListener(this);
        settings.setOnClickListener(this);
        cloud.setOnClickListener(this);


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
    public void onClick(View view){
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        Runnable mPendingRunnable;
        switch (view.getId()){
            case R.id.btnMapping:
                MainActivity.navItemIndex = 5; //navItemIndex
                MainActivity.CURRENT_TAG = MainActivity.TAG_MAPPING;
                fragment = new MappingFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case R.id.btnRecruitments:
                MainActivity.navItemIndex = 1;
                MainActivity.CURRENT_TAG = MainActivity.TAG_RECRUITMENTS;
                fragment = new RecruitmentsFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case R.id.btnSettings:
                MainActivity.navItemIndex = 1;
                MainActivity.CURRENT_TAG = MainActivity.TAG_HOME;
                fragment = new SettingsFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_HOME);
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case R.id.btnSharing:
                startActivity(new Intent(getActivity(), HttpServerActivity.class));
                break;
            case R.id.btnCloud:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(new Constants(getContext()).getCloudAddress()));
                startActivity(browserIntent);

        }
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tremap Home");
    }

    @Override
    public void onResume() {
        super.onResume();
        // register connection status listener
        // AppLaunch.getInstance().setConnectivityListener(this);
    }
}
