package com.expansion.lg.kimaru.expansion.fragment;

/**
 * Created by kimaru on 3/11/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.TestMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;


import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.dbhelpers.ChewReferralListAdapter;
import com.expansion.lg.kimaru.expansion.dbhelpers.CommunityUnitListAdapter;
import com.expansion.lg.kimaru.expansion.dbhelpers.RecruitmentListAdapter;
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// to show list in Gmail Mode
public class RecruitmentViewFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private ChewReferralListAdapter rAdapter;
    public SwipeRefreshLayout swipeRefreshLayout;

    private List<ChewReferral> chewReferrals = new ArrayList<>();
    private List<CommunityUnit> communityUnits = new ArrayList<>();
    private ListView mListView, cuListView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecruitmentsFragment.OnFragmentInteractionListener mListener;
    TextView recruitmentMainLocation, recruitmentSecondaryLocation, txtRegistrations;
    TextView recruitmentRegSummary, ivReferrals, addReferrals;

    public TextView subject, message, iconText, timestamp;
    public ImageView iconImp, imgProfile;
    public LinearLayout registrationContainser;
    public RelativeLayout iconContainer, iconBack, iconFront;


    AppCompatActivity a = new AppCompatActivity();
    CuAdapter cuAdapter;
    ChewAdapter adapter;

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
        getReferrals();

        // initialize the UI

        mListView = (ListView) v.findViewById(R.id.referral_list_view);
        String[] listItems = new String[chewReferrals.size()];
        for (int i = 0; i < chewReferrals.size(); i++){
            ChewReferral chewReferral = chewReferrals.get(i);
            listItems[i] = chewReferral.getName();
        }
        adapter = new ChewAdapter(getContext(), chewReferrals);

        mListView.setAdapter(adapter);

        recruitmentMainLocation = (TextView) v.findViewById(R.id.recruitmentMainLocation);
        addReferrals = (TextView) v.findViewById(R.id.addReferrals);
        recruitmentSecondaryLocation = (TextView) v.findViewById(R.id.recruitmentSecondaryLocation);
        registrations = new RegistrationTable(getContext())
                .getRegistrationsByRecruitment(session.getSavedRecruitment());
        txtRegistrations = (TextView) v.findViewById(R.id.registrations);
        txtRegistrations.setText(registrations.size()+" Registrations");
        txtRegistrations.setOnClickListener(this);



        int passed =0, failed = 0, waiting = 0;
        for (Registration registration:registrations){
            //get the interview for the registration
            Interview i = new InterviewTable(getContext()).getInterviewByRegistrationId(registration.getId());
            Integer selected = 0;
            if (i != null){
                selected = i.getSelected();
            }
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
        recruitmentRegSummary.setOnClickListener(this);

        RelativeLayout keCuList = (RelativeLayout) v.findViewById(R.id.ke_cu_list);
        RelativeLayout keCuView = (RelativeLayout) v.findViewById(R.id.ke_cu_view);
        if (session.getSavedRecruitment().getCountry().equalsIgnoreCase("UG")){
            recruitmentMainLocation.setText(session.getSavedRecruitment().getName());
            CountyLocation district = new CountyLocationTable(getContext())
                    .getLocationById(session.getSavedRecruitment().getDistrict());
            //recruitmentSecondaryLocation.setText(session.getSavedRecruitment().getDistrict());
            recruitmentSecondaryLocation.setText(district.getName());
            addReferrals.setText("+ ADD REFERRAL");
            //Hide KE
            keCuList.setVisibility(View.INVISIBLE);
            keCuView.setVisibility(View.INVISIBLE);
        }else{
            getCommunityUnits();
            cuListView = (ListView) v.findViewById(R.id.cu_list_view);
            String[] cuItems = new String[communityUnits.size()];
            for (int i = 0; i < communityUnits.size(); i++){
                CommunityUnit communityUnit = communityUnits.get(i);
                cuItems[i] = communityUnit.getCommunityUnitName();
            }
            cuAdapter = new CuAdapter(getContext(), communityUnits);

            cuListView.setAdapter(cuAdapter);

            recruitmentMainLocation.setText(session.getSavedRecruitment().getName());

//            recruitmentSecondaryLocation.setText(new SubCountyTable(getContext())
//                    .getSubCountyById(session.getSavedRecruitment().getSubcounty())
//                    .getSubCountyName());


            recruitmentSecondaryLocation.setText(new SubCountyTable(getContext())
                    .getSubCountyById(session.getSavedRecruitment().getSubcounty())
                    .getSubCountyName());

            session.saveSubCounty(new SubCountyTable(getContext())
                    .getSubCountyById(session.getSavedRecruitment().getSubcounty()));
            addReferrals.setText("+ ADD A CHEW");
            keCuView.setOnClickListener(this);

        }
        addReferrals.setOnClickListener(this);

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

    private void getReferrals(){
        chewReferrals.clear();
        try{
            List<ChewReferral> chewReferralList = new ChewReferralTable(getContext())
                    .getChewReferralByRecruitmentId(session.getSavedRecruitment().getId());
            for (ChewReferral chewReferral : chewReferralList){
                chewReferral.setColor(getRandomMaterialColor("400"));
                chewReferrals.add(chewReferral);
            }
        }catch (Exception e){
           // ivReferrals.setText("No recruitments added. Please create one");
        }

    }
    private void getCommunityUnits(){
        communityUnits.clear();
        try{
            List<CommunityUnit> communityUnitList = new CommunityUnitTable(getContext())
                    .getCommunityUnitBySubCounty(session.getSavedRecruitment().getSubcounty());
            for (CommunityUnit cu : communityUnitList){
                cu.setColor(getRandomMaterialColor("400"));
                communityUnits.add(cu);
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "Error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

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

    @Override
    public void onClick(View view){
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        switch (view.getId()){
            case R.id.addReferrals:
                NewChewReferralFragment newChewReferralFragment = new NewChewReferralFragment();
                newChewReferralFragment.createdFromRecruitment = true;
                fragment = newChewReferralFragment;
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case R.id.recruitmentRegSummary:
                fragment = new RegistrationsFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case  R.id.registrations:
                fragment = new RegistrationsFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.ke_cu_view:
                NewCommunityUnitFragment newCommunityUnitFragment = new NewCommunityUnitFragment();
                newCommunityUnitFragment.backFragment = new RecruitmentViewFragment();
                fragment = newCommunityUnitFragment;
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();
                break;

        }
    }

    private class ChewAdapter extends ArrayAdapter<ChewReferral>{
        private final Context context;
        private final List <ChewReferral> chewReferralss;

        public ChewAdapter( Context context, List<ChewReferral> values){
            super(context, -1, values);
            this.chewReferralss = values;
            this.context = context;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
            View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
            // name = (TextView) rowView.findViewById(R.id.from);
            subject = (TextView) rowView.findViewById(R.id.txt_primary);
            message = (TextView) rowView.findViewById(R.id.txt_secondary);
            iconText = (TextView) rowView.findViewById(R.id.icon_text);
            timestamp = (TextView) rowView.findViewById(R.id.timestamp);
            iconBack = (RelativeLayout) rowView.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) rowView.findViewById(R.id.icon_front);
            iconImp = (ImageView) rowView.findViewById(R.id.icon_star);
            imgProfile = (ImageView) rowView.findViewById(R.id.icon_profile);
            registrationContainser = (LinearLayout) rowView.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) rowView.findViewById(R.id.icon_container);

            final ChewReferral chew = chewReferralss.get(position);
            // name.setText(chew.getTitle());
            subject.setText(chew.getTitle() + " " +chew.getName());
            //view_instance.getLayoutParams().width = LayoutParams.MATCH_PARENT
            subject.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;

            message.setText(chew.getPhone());
            message.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
            // timestamp.setText(chew.getRecruitmentId());
            // timestamp.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
            if (!chew.getName().equalsIgnoreCase("")){
                iconText.setText(String.valueOf(chew.getName().substring(0,1)));
            }else{
                iconText.setText("");
            }

            imgProfile.setImageResource(R.drawable.bg_circle);
            imgProfile.setColorFilter(chew.getColor());
            iconText.setVisibility(View.VISIBLE);
            iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_white_24dp));
            iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_warning));
            iconImp.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // really delete the Item?
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Confirm Delete");
                    alertDialog.setMessage("Are you sure you want to delete "+ chew.getName()+"?");
                    alertDialog.setIcon(R.drawable.ic_delete_white_24dp);
                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Check if there are records dependent on this item
                            List <Registration> registrations = new RegistrationTable(getContext())
                                    .getRegistrationsByChewReferral(chew);
                            if (registrations.size() <= 0){
                                chewReferrals.remove(position);
                                deleteChewReferral(chew);
                            }
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Check if there are records dependent on this item
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                    return false;
                }
            });

            View.OnLongClickListener chewLongClick = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NewChewReferralFragment newChewReferralFragment = new NewChewReferralFragment();
                    newChewReferralFragment.createdFromRecruitment = true;
                    newChewReferralFragment.editingChewReferral = chew;
                    Fragment fragment = newChewReferralFragment;
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                    fragmentTransaction.commitAllowingStateLoss();
                    return false;
                }
            };
            registrationContainser.setOnLongClickListener(chewLongClick);

            return rowView;
        }
    }

    private class CuAdapter extends ArrayAdapter<CommunityUnit>{
        private final Context context;
        private final List <CommunityUnit> communityUnitss;

        public CuAdapter( Context context, List<CommunityUnit> values){
            super(context, -1, values);
            this.communityUnitss = values;
            this.context = context;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
            subject = (TextView) rowView.findViewById(R.id.txt_primary);
            message = (TextView) rowView.findViewById(R.id.txt_secondary);
            iconText = (TextView) rowView.findViewById(R.id.icon_text);
            timestamp = (TextView) rowView.findViewById(R.id.timestamp);
            iconBack = (RelativeLayout) rowView.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) rowView.findViewById(R.id.icon_front);
            iconImp = (ImageView) rowView.findViewById(R.id.icon_star);
            imgProfile = (ImageView) rowView.findViewById(R.id.icon_profile);
            registrationContainser = (LinearLayout) rowView.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) rowView.findViewById(R.id.icon_container);

            final CommunityUnit community = communityUnitss.get(position);
            subject.setText(community.getCommunityUnitName());
            subject.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;

            message.setText(community.getLinkFacilityId());
            message.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;

            iconText.setText(String.valueOf(community.getCommunityUnitName().substring(0,1)));
            imgProfile.setImageResource(R.drawable.bg_circle);
            imgProfile.setColorFilter(community.getColor());
            iconText.setVisibility(View.VISIBLE);
            iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_white_24dp));
            iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_warning));
            iconImp.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // really delete the Item?
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Confirm Delete");
                    alertDialog.setMessage("Are you sure you want to delete "+ community.getCommunityUnitName()+"?");
                    alertDialog.setIcon(R.drawable.ic_delete_white_24dp);
                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Check if there are records dependent on this item
                            List <Registration> registrations = new RegistrationTable(getContext())
                                    .getRegistrationsByRecruitmentAndCommunityUnit(session
                                            .getSavedRecruitment(), community);
                            if (registrations.size() <= 0){
                                communityUnits.remove(position);
                                //arrayAdapter.notifyDataSetChanged();
                                deleteCommunityUnit(community);
                            }
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Check if there are records dependent on this item
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                    return false;
                }
            });

            View.OnClickListener itemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set the CU, and filter the registrations by CU
                    SessionManagement sessionManagement = new SessionManagement(getContext());
                    sessionManagement.saveCommunityUnit(community);
                    RegistrationsFragment registrationsFragment = new RegistrationsFragment();
                    registrationsFragment.communityUnit = community;
                    Fragment fragment = registrationsFragment;
                    FragmentTransaction fragmentTransaction  = getActivity()
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            };
            registrationContainser.setOnClickListener(itemClickListener);

            View.OnLongClickListener longClick = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NewCommunityUnitFragment newCommunityUnitFragment = new NewCommunityUnitFragment();
                    newCommunityUnitFragment.editingCommunityUnit = community;
                    newCommunityUnitFragment.backFragment =  new RecruitmentViewFragment();
                    Fragment fragment = newCommunityUnitFragment;
                    FragmentTransaction fragmentTransaction  = getActivity()
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS);
                    fragmentTransaction.commitAllowingStateLoss();
                    return false;
                }
            };
            registrationContainser.setOnLongClickListener(longClick);

            return rowView;
        }
    }

    public void deleteCommunityUnit(CommunityUnit communityUnit){
        new CommunityUnitTable(getContext()).deleteCommunityUnit(communityUnit);
        cuAdapter.notifyDataSetChanged();
    }

    public void deleteChewReferral(ChewReferral chewReferral){
        new ChewReferralTable(getContext()).deleteChewReferral(chewReferral);
        adapter.notifyDataSetChanged();
    }
}
