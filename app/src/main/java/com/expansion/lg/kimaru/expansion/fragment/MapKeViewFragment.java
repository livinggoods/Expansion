package com.expansion.lg.kimaru.expansion.fragment;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.KeCounty;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.expansion.lg.kimaru.expansion.R.id.parishes;

/**
 * Created by kimaru on 3/30/17.
 */

public class MapKeViewFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;

    SessionManagement sessionManagement;
    HashMap<String, String> user;
    Mapping mapping;
    TextView mappingMainLocation, mappingSecondaryLocation, contactPerson, contactPhone, mappingComment;
    TextView txtSubCounty;
    private ListView mListView;
    private List<SubCounty> subCountyList = new ArrayList<>();
    public TextView subject, message, iconText, timestamp;
    public ImageView iconImp, imgProfile;
    public LinearLayout registrationContainser;
    public RelativeLayout iconContainer, iconBack, iconFront, relativeSubCountySummary;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_mapping_view, container, false);
        MainActivity.backFragment = new MappingFragment();
        sessionManagement = new SessionManagement(getContext());
        user = sessionManagement.getUserDetails();
        mapping = sessionManagement.getSavedMapping();
        KeCounty county = new KeCountyTable(getContext()).getCountyById(Integer.valueOf(mapping.getCounty()));
        mappingSecondaryLocation = (TextView) v.findViewById(R.id.mappingSecondaryLocation);
        contactPhone = (TextView) v.findViewById(R.id.contactPhone);
        contactPerson = (TextView) v.findViewById(R.id.contactPerson);
        mappingMainLocation = (TextView) v.findViewById(R.id.mappingMainLocation);
        mappingComment = (TextView) v.findViewById(R.id.mappingComment);

        mappingMainLocation.setText(mapping.getMappingName());
        mappingSecondaryLocation.setText(county.getCountyName());
        contactPerson.setText(mapping.getContactPerson());
        contactPhone.setText(mapping.getContactPersonPhone());
        mappingComment.setText(mapping.getComment());
        // get the parishes
        getSubcounties();

        txtSubCounty = (TextView) v.findViewById(R.id.addSubCounty);
        txtSubCounty.setText(String.valueOf(subCountyList.size())+" SUB COUNTIES");
        txtSubCounty.setOnClickListener(this);

        relativeSubCountySummary = (RelativeLayout) v.findViewById(R.id.subCountySummary);
        relativeSubCountySummary.setOnClickListener(this);




        return v;
    }


    private void getSubcounties(){
        subCountyList.clear();
        try{
            List<SubCounty> subCounties = new SubCountyTable(getContext())
                    .getSubCountiesByCounty(Integer.valueOf(mapping.getCounty()));
            for (SubCounty sb : subCounties){
                sb.setColor(getRandomMaterialColor("400"));
                subCountyList.add(sb);
            }
        }catch (Exception e){}
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SessionManagement s = new SessionManagement(getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(s.getSavedMapping()
                .getMappingName() +" Mapping ");
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
        switch (view.getId()) {
            case R.id.addSubCounty:
                fragment = new SubCountiesFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "registrations");
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case R.id.subCountySummary:
                fragment = new SubCountiesFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "registrations");
                fragmentTransaction.commitAllowingStateLoss();
                break;
        }
    }

}
