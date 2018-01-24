package com.expansion.lg.kimaru.expansion.fragment;

/**
 * Created by kimaru on 3/11/17.
 */

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.expansion.lg.kimaru.expansion.dbhelpers.ChewReferralListAdapter;
import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.PartnerActivity;
import com.expansion.lg.kimaru.expansion.mzigos.Partners;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable;
import com.expansion.lg.kimaru.expansion.tables.PartnersTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// to show list in Gmail Mode
public class CommunityUnitViewFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private ChewReferralListAdapter rAdapter;
    public SwipeRefreshLayout swipeRefreshLayout;
    public RelativeLayout registrationDetails;

    public CommunityUnit communityUnit = null;

    private List<PartnerActivity> partnerActivities = new ArrayList<>();
    private List<CommunityUnit> communityUnits = new ArrayList<>();
    private ListView mListView, cuListView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecruitmentsFragment.OnFragmentInteractionListener mListener;
    TextView textContactPerson, textContactPersonPhone, textCommunityUnitName,
            textLinkFacility;

    TextView textCounty, textSubCounty, textWard, textEconomicStatus, textMrdtDetails, textActDetails;

    public TextView subject, message, iconText, timestamp;
    public ImageView iconImp, imgProfile;
    public LinearLayout registrationContainser;
    public RelativeLayout iconContainer, iconBack, iconFront;


    // AppCompatActivity a = new AppCompatActivity();
    PartnerActivityAdapter adapter;

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
    public static CommunityUnitViewFragment newInstance(String param1, String param2) {
        CommunityUnitViewFragment fragment = new CommunityUnitViewFragment();
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
        View v =  inflater.inflate(R.layout.communityunit_content, container, false);

        MainActivity.CURRENT_TAG = MainActivity.TAG_RECRUITMENTS;
        MainActivity.backFragment = new CommunityUnitsFragment();
        //session Management
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
        if (communityUnit == null){
            communityUnit = session.getSavedCommunityUnit();
        }

        getPartnerActivities();

        mListView = (ListView) v.findViewById(R.id.partner_list_view);
//        String[] listItems = new String[partnerActivities.size()];
//        for (int i = 0; i < partnerActivities.size(); i++){
//            PartnerActivity partnerActivity = partnerActivities.get(i);
//            Partners partners = new PartnersTable(getContext())
//                    .getPartnerById(partnerActivity.getPartnerId());
//            listItems[i] = partners.getPartnerName();
//        }
        adapter = new PartnerActivityAdapter(getContext(), partnerActivities);
        mListView.setAdapter(adapter);

        textContactPerson = (TextView) v.findViewById(R.id.textContactPerson);
        textContactPersonPhone = (TextView) v.findViewById(R.id.textContactPersonPhone);
        textCommunityUnitName = (TextView) v.findViewById(R.id.textCommunityUnitName);
        textLinkFacility = (TextView) v.findViewById(R.id.textLinkFacility);

        SubCounty subCounty =  new SubCountyTable(getContext())
                .getSubCountyById(communityUnit.getSubCountyId());

        textCounty = (TextView) v.findViewById(R.id.textCounty);
        try{
            textCounty.setText(new KeCountyTable(getContext())
                    .getCountyById(Integer.valueOf(subCounty.getCountyID()))
                    .getCountyName());
        }catch (Exception e){
            textCounty.setText("");
        }

        textSubCounty = (TextView) v.findViewById(R.id.textSubCounty);
        textSubCounty.setText(subCounty.getSubCountyName());

        textWard = (TextView) v.findViewById(R.id.textWard);
        textWard.setText(communityUnit.getWard());

        textEconomicStatus = (TextView) v.findViewById(R.id.textEconomicStatus);
        textEconomicStatus.setText(communityUnit.getEconomicStatus());

        textMrdtDetails = (TextView) v.findViewById(R.id.textMrdtDetails);
        textMrdtDetails.setText(String.valueOf(communityUnit.getPrivateFacilityForMrdt())+" " + communityUnit.getMrdtPrice());

        textActDetails = (TextView) v.findViewById(R.id.textActDetails);
        textActDetails.setText(String.valueOf(communityUnit.getPrivateFacilityForAct())+" " + communityUnit.getActPrice());

        textCommunityUnitName.setText(communityUnit.getCommunityUnitName());
        textContactPerson.setText(communityUnit.getAreaChiefName());
        textContactPersonPhone.setText(communityUnit.getAreaChiefPhone());
        try {
            textLinkFacility.setText(new LinkFacilityTable(getContext())
                    .getLinkFacilityById(communityUnit.getLinkFacilityId()).getFacilityName());
        }catch (Exception e){
            textLinkFacility.setText("Link Facility not found");
        }


        RelativeLayout partnerView = (RelativeLayout) v.findViewById(R.id.cuPartnerActivitiesView);
        partnerView.setOnClickListener(this);

        return v;
    }

    View.OnClickListener onRecruitmentRegistrationSummaryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment fragment = new RegistrationsFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_REGISTRATIONS);
            fragmentTransaction.commitAllowingStateLoss();
        }
    };

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

    private void getPartnerActivities(){
        partnerActivities.clear();
        try{
            List<PartnerActivity> activities = new PartnerActivityTable(getContext())
                    .getPartnerActivityByField(PartnerActivityTable.COMMUNITYUNIT, communityUnit.getId());
            for (PartnerActivity pa : activities){
                pa.setColor(getRandomMaterialColor("400"));
                partnerActivities.add(pa);

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
            case R.id.cuPartnerActivitiesView:
                NewPartnerActivityFragment newPartnerActivityFragment = new NewPartnerActivityFragment();
                newPartnerActivityFragment.backFragment = new CommunityUnitViewFragment();
                newPartnerActivityFragment.communityUnit = communityUnit;
                fragment = newPartnerActivityFragment;
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();
                break;
        }
    }

    private class PartnerActivityAdapter extends ArrayAdapter<PartnerActivity>{
        private final Context context;
        private final List <PartnerActivity> values;

        public PartnerActivityAdapter( Context context, List<PartnerActivity> values){
            super(context, -1, values);
            this.values = values;
            this.context = context;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
            final PartnerActivity ps = values.get(position);
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
            try {
                final PartnerActivity partnerActivity = values.get(position);
                Partners partner = new PartnersTable(context).getPartnerById(partnerActivity.getPartnerId());

                subject.setText(partner.getPartnerName());
                subject.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;

                message.setText(partner.getContactPerson());
                message.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
                // timestamp.setText(chew.getRecruitmentId());
                // timestamp.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
                if (!partner.getPartnerName().equalsIgnoreCase("")) {
                    iconText.setText(String.valueOf(partner.getPartnerName().substring(0, 1)));
                } else {
                    iconText.setText("");
                }

                imgProfile.setImageResource(R.drawable.bg_circle);
                imgProfile.setColorFilter(partner.getColor());
                iconText.setVisibility(View.VISIBLE);
                iconImp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_white_24dp));
                iconImp.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_warning));
            } catch (Exception e){
                Log.d("TREMAP", "==><====>==><====>==><====>==><====>==><====>==><====>==><====>==><====>");
                Log.d("TREMAP", e.getMessage());

            }
            /**
             * Enable the logic to delete
             *
             */
//            iconImp.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    // really delete the Item?
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
//                    alertDialog.setTitle("Confirm Delete");
//                    alertDialog.setMessage("Are you sure you want to delete "+ chew.getName()+"?");
//                    alertDialog.setIcon(R.drawable.ic_delete_white_24dp);
//                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Check if there are records dependent on this item
//
//                        }
//                    });
//                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Check if there are records dependent on this item
//                            dialog.cancel();
//                        }
//                    });
//                    alertDialog.show();
//                    return false;
//                }
//            });

            return rowView;
        }
    }


}
