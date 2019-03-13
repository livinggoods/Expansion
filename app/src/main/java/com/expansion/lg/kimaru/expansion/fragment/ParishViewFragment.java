package com.expansion.lg.kimaru.expansion.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.sync.ApiClient;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.KeCountyTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.MappingTable;
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.expansion.lg.kimaru.expansion.tables.VillageTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

/**
 * Created by kimaru on 3/30/17.
 */

public class ParishViewFragment extends Fragment implements  View.OnClickListener{

    private RecyclerView mRecyclerView;
    Parish parish;
    Mapping mapping;
    CountyLocation subCounty;
    SessionManagement sessionManagement;

    TextView parishComment, contactPhone, contactPerson, parishName, parishSubCountyName, subCountyCounty;
    TextView linkFacilitySummary, villagesSummary, partnersSummary, syncVillages;
    RelativeLayout relativeViewVillages, relativeLinkFacilities;
    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_parish_view, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_SUBCOUNTY_VIEW;
        MainActivity.backFragment = new MapViewFragment();

        sessionManagement = new SessionManagement(getContext());
        mapping = sessionManagement.getSavedMapping();
        parish = sessionManagement.getSavedParish();

        progressDialog = new ProgressDialog(getContext());

        parishName = (TextView) v.findViewById(R.id.parishName);
        parishComment = (TextView) v.findViewById(R.id.parishComment);
        contactPhone = (TextView) v.findViewById(R.id.contactPhone);
        contactPerson = (TextView) v.findViewById(R.id.contactPerson);
        parishSubCountyName = (TextView) v.findViewById(R.id.parishSubCountyName);
        subCountyCounty = (TextView) v.findViewById(R.id.subCountyCounty);
        linkFacilitySummary = (TextView) v.findViewById(R.id.linkFacilitySummary);
        villagesSummary = (TextView) v.findViewById(R.id.villagesSummary);
        partnersSummary = (TextView) v.findViewById(R.id.partnersSummart);
        syncVillages = (TextView) v.findViewById(R.id.syncVillages);
        syncVillages.setOnClickListener(this);
        parishName.setText(parish.getName());
        Log.d("Tremap", "Mapping Subcounty is "+mapping.getSubCounty());
        subCounty = new CountyLocationTable(getContext()).getLocationById(mapping.getSubCounty());

        parishSubCountyName.setText(subCounty.getName());
        contactPhone.setText(parish.getContactPersonPhone());
        contactPerson.setText(parish.getContactPerson());
        subCountyCounty.setText(new CountyLocationTable(getContext())
                .getLocationById(String.valueOf(subCounty.getParent())).getName());

        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(getContext());
        Integer linkFacilities = linkFacilityTable
                .getLinkFacilityByParish(String.valueOf(parish.getId())).size();
        if (linkFacilities.equals(0)){
            linkFacilitySummary.setText("NO LINK FACILITY");
        }else if (linkFacilities.equals(1)){
            linkFacilitySummary.setText("1 LINK FACILITY");
        }else{
            linkFacilitySummary.setText(String.valueOf(linkFacilities)+" LINK FACILITIES");
        }

        VillageTable villageTable = new VillageTable(getContext());
        Integer villages = villageTable
                .getVillageDataByParishId(String.valueOf(parish.getId())).size();
        if (villages.equals(0)){
            villagesSummary.setText("NO VILLAGES ADDED");
        }else if (villages.equals(1)){
            villagesSummary.setText("1 VILLAGE");
        }else{
            villagesSummary.setText(String.valueOf(villages)+" VILLAGES");
        }


        relativeViewVillages = (RelativeLayout) v.findViewById(R.id.relativeViewVillages);
        relativeViewVillages.setOnClickListener(this);
        relativeLinkFacilities = (RelativeLayout) v.findViewById(R.id.relativeLinkFacilities);
        relativeLinkFacilities.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());
        String parishName = parish.getName();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(parishName + " Parish");
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
            case R.id.relativeViewVillages:
                fragment = new VillagesFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, fragment, "subcounties");
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.syncVillages:
                progressDialog.setTitle("Syncing Villages");
                progressDialog.setMessage("Getting villages for "+parish.getName()+" ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                new syncVillages().execute();
                break;
        }

    }

    private class syncVillages extends AsyncTask<Void, HashMap, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected String doInBackground(Void... voids){
            String stream = null;
            Integer totalRecords = 0, processedRecords = 0;
            HashMap<String, String> progress = new HashMap<String, String>();
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(new Constants(getContext()).getCloudAddress() + "/api/v1/sync/villages?parish="+parish.getId());
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("villages");
                    VillageTable villageTable = new VillageTable(getContext());
                    totalRecords = recs.length();
                    for (int y = 0; y < recs.length(); y++){
                        processedRecords = y+1;
                        JSONObject villageObj = recs.getJSONObject(y);
                        villageTable.fromJson(villageObj);
                        progress = new HashMap<String, String>();
                        progress.put("total", totalRecords.toString());
                        progress.put("processed", processedRecords.toString());
                        progress.put("message", "Creating village "+villageObj.get("village_name") +"\n Please wait ... ");
                        publishProgress(progress);
                    }
                }catch(JSONException e){
                    Log.d("TREMAP", "ERROR Creating parish:\n "+e.getMessage());
                }
            }
            return null;

        }
        protected void onPostExecute(String stream){
            super.onPostExecute(null);
            progressDialog.dismiss();
        } // onPostExecute() end

        @Override
        protected void onProgressUpdate(HashMap... updates){
            super.onProgressUpdate(updates);
            HashMap<String, String> progress = updates[0];
            progressDialog.setProgress(Integer.valueOf(progress.get("processed")));
            progressDialog.setMax(Integer.valueOf(progress.get("total")));
            progressDialog.setMessage(progress.get("message"));
        }
    }

}
