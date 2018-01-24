package com.expansion.lg.kimaru.expansion.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable;
import com.expansion.lg.kimaru.expansion.tables.ParishTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kimaru on 3/30/17.
 */

public class MapViewFragment extends Fragment implements View.OnClickListener {

    private  List<Parish> parishes = new ArrayList<>();
    private  List<Mobilization> mobilizationList = new ArrayList<>();
    private ListView mListView;
    private ListView mMobilizationListView;

    TextView mappingMainLocation, mappingSecondaryLocation, contactPerson, contactPhone;
    TextView mappingComment, ivReferrals, addParish, textViewAddMobilization;
    public TextView subject, message, iconText, timestamp;
    public ImageView iconImp, imgProfile;
    public LinearLayout registrationContainser;
    public RelativeLayout iconContainer, iconBack, iconFront, relativeViewMobilizations;

    AppCompatActivity a = new AppCompatActivity();
    SessionManagement sessionManagement;
    HashMap<String, String> user;
    Mapping mapping;

    public static MapViewFragment newInstance(String param1, String param2) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.mapping_content, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_MAP_VIEW;
        MainActivity.backFragment = new MappingFragment();
        // session
        sessionManagement = new SessionManagement(getContext());
        user = sessionManagement.getUserDetails();
        mapping = sessionManagement.getSavedMapping();
        CountyLocationTable countyLocationTable = new CountyLocationTable(getContext());
        CountyLocation county = countyLocationTable.getLocationById(mapping.getCounty());
        CountyLocation  subCounty = countyLocationTable.getLocationById(mapping.getSubCounty());
        mappingMainLocation = (TextView) v.findViewById(R.id.mappingMainLocation);
        mappingSecondaryLocation = (TextView) v.findViewById(R.id.mappingSecondaryLocation);
        contactPhone = (TextView) v.findViewById(R.id.contactPhone);
        contactPerson = (TextView) v.findViewById(R.id.contactPerson);
        mappingMainLocation.setText(subCounty.getName());
        mappingSecondaryLocation.setText( county.getName());
        contactPerson.setText(mapping.getContactPerson());
        mappingComment = (TextView) v.findViewById(R.id.mappingComment);
        mappingComment.setText(mapping.getComment());
        contactPhone.setText(mapping.getContactPersonPhone());
        addParish = (TextView) v.findViewById(R.id.addParish);
        addParish.setText(" + ADD PARISH");
        addParish.setOnClickListener(this);
        textViewAddMobilization = (TextView) v.findViewById(R.id.mobilizations);
        textViewAddMobilization.setOnClickListener(this);
        relativeViewMobilizations = (RelativeLayout) v.findViewById(R.id.relativeViewMobilizations);
        relativeViewMobilizations.setOnClickListener(this);

        // get the parishes
        getParishes();

        mListView = (ListView) v.findViewById(R.id.referral_list_view);
        String [] listItems = new String[parishes.size()];
        for (int i=0; i <parishes.size(); i++){
            Parish parish = parishes.get(i);
            listItems[i] = parish.getName();
        }
        MapAdapter adapter = new MapAdapter(getContext(), parishes);
        mListView.setAdapter(adapter);

        getMobilization();

        mMobilizationListView = (ListView) v.findViewById(R.id.mobilization_list_view);
        String [] mobilizationItems = new String[mobilizationList.size()];
        for (int i=0; i <mobilizationList.size(); i++){
            Mobilization mobilization = mobilizationList.get(i);
            mobilizationItems[i] = mobilization.getName();
        }
        MobilizationAdapter mobilizationAdapter = new MobilizationAdapter(getContext(), mobilizationList);
        mMobilizationListView.setAdapter(mobilizationAdapter);
        //.setLongClickable(true);
        mMobilizationListView.setLongClickable(true);



        return v;
    }

    private class MapAdapter extends ArrayAdapter<Parish> {
        private final Context context;
        private final List <Parish> parishList;

        public MapAdapter( Context context, List<Parish> values){
            super(context, -1, values);
            this.parishList = values;
            this.context = context;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            final Parish p = parishList.get(position);
            // name.setText(chew.getTitle());
            subject.setText(p.getName());
            //view_instance.getLayoutParams().width = LayoutParams.MATCH_PARENT
            subject.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
            if (user.get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("UG")){
                CountyLocationTable countyLocationTable = new CountyLocationTable(context);
                message.setText(countyLocationTable.getLocationById(p.getParent()).getName());
            }
            //message.setText(p.getParent());
            message.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
            // timestamp.setText(chew.getRecruitmentId());
            // timestamp.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
            iconText.setText(String.valueOf(p.getName().substring(0,1)));
            imgProfile.setImageResource(R.drawable.bg_circle);
            imgProfile.setColorFilter(p.getColor());
            iconText.setVisibility(View.VISIBLE);
            iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_black_24dp));
            iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_tint_selected));
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to the village listing
                    sessionManagement.saveParish(p);
                    ParishViewFragment parishViewFragment =  new ParishViewFragment();
                    parishViewFragment.parish = p;
                    Fragment fragment = parishViewFragment;
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "villages");
                    fragmentTransaction.commitAllowingStateLoss();
                }
            });

            return rowView;
        }
    }

    private class MobilizationAdapter extends ArrayAdapter<Mobilization> {
        private final Context context;
        private final List <Mobilization> mobilizationsList;

        public MobilizationAdapter( Context context, List<Mobilization> values){
            super(context, -1, values);
            this.mobilizationsList = values;
            this.context = context;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            final Mobilization m = mobilizationsList.get(position);
            // name.setText(chew.getTitle());
            subject.setText(m.getName());
            //view_instance.getLayoutParams().width = LayoutParams.MATCH_PARENT
            subject.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
            if (sessionManagement.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("KE")){
                SubCountyTable subCountyTable = new SubCountyTable(context);
                message.setText(subCountyTable.getSubCountyById(m.getSubCounty()).getSubCountyName());
            }else{
                CountyLocationTable countyLocationTable = new CountyLocationTable(context);
                message.setText(countyLocationTable.getLocationById(m.getSubCounty()).getName());
            }

            message.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
            // timestamp.setText(chew.getRecruitmentId());
            // timestamp.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
            iconText.setText(String.valueOf(m.getName().substring(0,1)));
            imgProfile.setImageResource(R.drawable.bg_circle);
            imgProfile.setColorFilter(getRandomMaterialColor("400"));
            iconText.setVisibility(View.VISIBLE);
            iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_black_24dp));
            iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_tint_selected));
            registrationContainser.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NewMobilizationFragment newMobilizationFragment = new NewMobilizationFragment();
                    newMobilizationFragment.editingMobilization = m;

                    Fragment fragment = newMobilizationFragment;
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "villages");
                    fragmentTransaction.commitAllowingStateLoss();
                    return true;
                }
            });
            registrationContainser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to the village listing
                    sessionManagement.saveMobilization(m);
                    Fragment fragment = new ReferralsFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "villages");
                    fragmentTransaction.commitAllowingStateLoss();
                }
            });

            return rowView;
        }
    }

    @Override
    public void onClick (View view){
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        switch (view.getId()){
            case R.id.addParish:
                // creating new Parish
                fragment = new NewParishFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "registrations");
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.mobilizations:
                fragment = new NewMobilizationFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "mobilization");
                fragmentTransaction.commitAllowingStateLoss();
            case R.id.relativeViewMobilizations:
                fragment = new NewMobilizationFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "mobilization");
                fragmentTransaction.commitAllowingStateLoss();
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SessionManagement s = new SessionManagement(getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(s.getSavedMapping()
                .getMappingName() +" Mapping ");
    }

    //getParishByParent

    private void getParishes(){
        parishes.clear();
        try{
            List<Parish> parishList = new ParishTable(getContext())
                    .getParishByMapping(sessionManagement.getSavedMapping().getId());
            for (Parish p : parishList){
                p.setColor(getRandomMaterialColor("400"));
                parishes.add(p);
            }
        }catch (Exception e){
            ivReferrals.setText("No recruitments added. Please create one");
        }
    }



    private void getMobilization(){
        mobilizationList.clear();
        try{
            List<Mobilization> mobList = new MobilizationTable(getContext())
                    .getMobilizationByMappingId(sessionManagement.getSavedMapping().getId());
            for (Mobilization mob : mobList){
                mobilizationList.add(mob);
            }
        }catch (Exception e){
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

}