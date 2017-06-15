package com.expansion.lg.kimaru.expansion.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;

/**
 * Created by kimaru on 3/30/17.
 */

public class LinkFacilityViewFragment extends Fragment implements  View.OnClickListener{

    private RecyclerView mRecyclerView;
    SubCounty subCounty;
    Mapping mapping;
    LinkFacility linkFacility;
    SessionManagement sessionManagement;

    TextView facilityComment, stockLevels, actMrdtLevels, facilityName, subCountyName;
    RelativeLayout relativeViewCommunityUnits;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_link_facility_view, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_SUBCOUNTY_VIEW;
        MainActivity.backFragment = new SubCountiesFragment();

        sessionManagement = new SessionManagement(getContext());
        mapping = sessionManagement.getSavedMapping();
        subCounty = sessionManagement.getSavedSubCounty();
        linkFacility = sessionManagement.getSavedLinkFacility();

        facilityComment = (TextView) v.findViewById(R.id.mappingComment);
        facilityName = (TextView) v.findViewById(R.id.linkFacilityName);
        subCountyName = (TextView) v.findViewById(R.id.subCountyName);
        stockLevels = (TextView) v.findViewById(R.id.stockLevels);
        actMrdtLevels = (TextView) v.findViewById(R.id.mrdtActLevels);
        relativeViewCommunityUnits = (RelativeLayout) v.findViewById(R.id.relativeViewCommunityUnits);
        relativeViewCommunityUnits.setOnClickListener(this);


        facilityName.setText(String.valueOf(linkFacility.getFacilityName()));
        subCountyName.setText(subCounty.getSubCountyName());
        stockLevels.setText("Stock Levels");
        actMrdtLevels.setText("MRDT "+String.valueOf(linkFacility.getMrdtLevels()+
                " ACT : "+ String.valueOf(linkFacility.getActLevels())));

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String county = mapping.getCounty();
        String subCountyName = subCounty.getSubCountyName();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(linkFacility.getFacilityName());
    }

    @Override
    public void onClick (View view){
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        FragmentManager fragmentManager;
        switch (view.getId()){
            case R.id.relativeViewCommunityUnits:
                CommunityUnitsFragment communityUnitsFragment = new CommunityUnitsFragment();
                communityUnitsFragment.linkFacility = linkFacility;
                fragment = communityUnitsFragment;
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties");
                fragmentTransaction.commitAllowingStateLoss();
                break;
        }
    }

}
