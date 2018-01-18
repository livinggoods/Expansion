package com.expansion.lg.kimaru.expansion.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.PartnersTable;
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 3/30/17.
 */

public class SubCountyViewFragment extends Fragment implements  View.OnClickListener{

    private RecyclerView mRecyclerView;
    SubCounty subCounty;
    Mapping mapping;
    SessionManagement sessionManagement;

    TextView mappingComment, contactPhone, contactPerson, subCountyName, subCountyCounty;
    TextView linkFacilitySummary, communityUnitSummary, partnersSummary;
    RelativeLayout relativeViewPartners, relativeViewCommunityUnits, relativeLinkFacilities;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_subcounty_view, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_SUBCOUNTY_VIEW;
        MainActivity.backFragment = new SubCountiesFragment();

        sessionManagement = new SessionManagement(getContext());
        mapping = sessionManagement.getSavedMapping();
        subCounty = sessionManagement.getSavedSubCounty();

        mappingComment = (TextView) v.findViewById(R.id.mappingComment);
        contactPhone = (TextView) v.findViewById(R.id.contactPhone);
        contactPerson = (TextView) v.findViewById(R.id.contactPerson);
        subCountyName = (TextView) v.findViewById(R.id.subCountyName);
        subCountyCounty = (TextView) v.findViewById(R.id.subCountyCounty);
        linkFacilitySummary = (TextView) v.findViewById(R.id.linkFacilitySummary);
        communityUnitSummary = (TextView) v.findViewById(R.id.communityUnitSummary);
        partnersSummary = (TextView) v.findViewById(R.id.partnersSummart);

        subCountyName.setText(subCounty.getSubCountyName());
        contactPhone.setText(subCounty.getContactPersonPhone());
        contactPerson.setText(subCounty.getContactPerson());
        subCountyCounty.setText(new KeCountyTable(getContext())
                .getCountyById(Integer.valueOf(mapping.getCounty())).getCountyName());

        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(getContext());
        Integer linkFacilities = linkFacilityTable.getLinkFacilityBySubCounty(subCounty.getId()).size();
        if (linkFacilities.equals(0)){
            linkFacilitySummary.setText("NO LINK FACILITY");
        }else if (linkFacilities.equals(1)){
            linkFacilitySummary.setText("1 LINK FACILITY");
        }else{
            linkFacilitySummary.setText(String.valueOf(linkFacilities)+" LINK FACILITIES");
        }

        CommunityUnitTable communityUnitTable = new CommunityUnitTable(getContext());
        Integer communityUnits = communityUnitTable
                .getCommunityUnitBySubCounty(subCounty.getId()).size();
        if (communityUnits.equals(0)){
            communityUnitSummary.setText("NO COMMUNITY UNITS");
        }else if (communityUnits.equals(1)){
            communityUnitSummary.setText("1 COMMUNITY UNIT");
        }else{
            communityUnitSummary.setText(String.valueOf(communityUnits) +" COMMUNITY UNITS");
        }

        Integer partners = new PartnersTable(getContext())
                .getPartnersBySubCounty(subCounty.getId()).size();
        if (partners.equals(0)){
            partnersSummary.setText("NO PARTNERS");
        }else if (partners.equals(1)){
            partnersSummary.setText("1 PARTNER");
        }else{
            partnersSummary.setText(String.valueOf(partners) +" PARTNERS");
        }

        relativeViewPartners = (RelativeLayout) v.findViewById(R.id.relativeViewPartners);
        relativeViewCommunityUnits = (RelativeLayout) v.findViewById(R.id.relativeViewCommunityUnits);
        relativeLinkFacilities = (RelativeLayout) v.findViewById(R.id.relativeLinkFacilities);

        relativeLinkFacilities.setOnClickListener(this);
        relativeViewCommunityUnits.setOnClickListener(this);
        relativeViewPartners.setOnClickListener(this);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String county = mapping.getCounty();
        String subCountyName = subCounty.getSubCountyName();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(county + " - "+ subCountyName + "Sub County");
    }

    @Override
    public void onClick (View view){
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        FragmentManager fragmentManager;
        switch (view.getId()){
            case R.id.relativeLinkFacilities:
                fragment = new LinkFacilitiesFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties");
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.relativeViewCommunityUnits:
                fragment = new CommunityUnitsFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties");
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.relativeViewPartners:
                NewPartnerActivityFragment partnerActivityFragment = new NewPartnerActivityFragment();
                Log.d("TREMAP", "--------------------------------------------------");
                Log.d("TREMAP", subCounty.getId());
                Log.d("TREMAP", "--------------------------------------------------");
                partnerActivityFragment.subCounty = subCounty;
                sessionManagement.saveSubCounty(subCounty);
                partnerActivityFragment.communityUnit = null;
                fragment = partnerActivityFragment;
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
