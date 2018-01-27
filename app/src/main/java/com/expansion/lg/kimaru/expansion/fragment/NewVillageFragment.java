package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.mzigos.Village;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.VillageTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewVillageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewVillageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewVillageFragment extends Fragment implements OnClickListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText editName, editAreaChiefName, editAreaChiefPhone, editComment, editDistanceToBranch,
            editTransportCost, editDistanceToMainRoad, editNumberOfHouseHolds, editPopulationDensity,
            editDistributors, editDistanceToHealthFacility;
    TextView textLat, textLon;
    Spinner editEconomicStatus, spinnerLinkFacility;
    RadioGroup editPresenceEstates, editPresenceOfFactories, editPresenceOfTraderMarket,
            editPresenceOfSuperMarket, editNgosGivingFreeDrugs, editBrac, editNgosIccms, mtn, airtel,
            orange, editNgosMhealth, editPresenceDistributors, actStock, editActCost;
    List<String> economicStatusList;
    List<LinkFacility> linkFacilityList = new ArrayList<LinkFacility>();
    List<String> linkFacilities = new ArrayList<String>();

    //location
    double latitude, longitude;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    // The minimum distance to change Updates in meters
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    // The minimum time between updates in milliseconds
    public static final long MIN_TIME_BETWEEN_UPDATE = 1000 * 60 / 2; // 30 seconds
    protected LocationManager locationManager;


    Button buttonSave, buttonList;
    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    SessionManagement session;
    Village editingVillage = null;
    HashMap<String, String> user;
    LinkFacility linkFacility = null;

    public NewVillageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewVillageFragment newInstance(String param1, String param2) {
        NewVillageFragment fragment = new NewVillageFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_village, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_VILLAGE;
        MainActivity.backFragment = new VillagesFragment();
        session = new SessionManagement(getContext());
        user = session.getUserDetails();

        //Ui Elements
        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        editName = (EditText) v.findViewById(R.id.editName);
        editAreaChiefName = (EditText) v.findViewById(R.id.editAreaChiefName);
        editAreaChiefPhone = (EditText) v.findViewById(R.id.editAreaChiefPhone);
        editComment = (EditText) v.findViewById(R.id.editComment);
        editDistanceToBranch = (EditText) v.findViewById(R.id.editDistanceToBranch);
        editTransportCost = (EditText) v.findViewById(R.id.editTransportCost);
        editDistanceToMainRoad = (EditText) v.findViewById(R.id.editDistanceToMainRoad);
        editNumberOfHouseHolds = (EditText) v.findViewById(R.id.editNumberOfHouseHolds);
        editPopulationDensity = (EditText) v.findViewById(R.id.editPopulationDensity);
        textLat = (TextView) v.findViewById(R.id.textLat);
        textLon = (TextView) v.findViewById(R.id.textLon);
        latitude=0D;
        longitude=0D;
        textLat.setText("Capturing LAT");
        textLon.setText("Capturing LON");


        /**
         * Hide population density
         * @requested by trainingTeam-Ug
         * @date Jan 23rd, 2018
         */
        if (session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("UG")){
            editPopulationDensity.setVisibility(View.GONE);
        }
        editDistributors = (EditText) v.findViewById(R.id.editDistributors);
        editDistanceToHealthFacility = (EditText) v.findViewById(R.id.editDistanceToHealthFacility);
//        editActLevels = (EditText) v.findViewById(R.id.editActLevels);
        editActCost = (RadioGroup) v.findViewById(R.id.editActCost);
        editPresenceEstates = (RadioGroup) v.findViewById(R.id.editPresenceEstates);
        editPresenceOfFactories = (RadioGroup) v.findViewById(R.id.editPresenceOfFactories);
        editPresenceOfTraderMarket = (RadioGroup) v.findViewById(R.id.editPresenceOfTraderMarket);
        editPresenceOfSuperMarket = (RadioGroup) v.findViewById(R.id.editPresenceOfSuperMarket);
        editNgosGivingFreeDrugs = (RadioGroup) v.findViewById(R.id.editNgosGivingFreeDrugs);
        editPresenceDistributors = (RadioGroup) v.findViewById(R.id.editPresenceDistributors);
        actStock = (RadioGroup) v.findViewById(R.id.editActStock);
        editBrac = (RadioGroup) v.findViewById(R.id.editBrac);
        editNgosIccms = (RadioGroup) v.findViewById(R.id.editNgosIccms);
        editNgosMhealth = (RadioGroup) v.findViewById(R.id.editNgosMhealth);
        mtn = (RadioGroup) v.findViewById(R.id.mtn);
        airtel = (RadioGroup) v.findViewById(R.id.airtel);
        orange = (RadioGroup) v.findViewById(R.id.orange);
        editEconomicStatus = (Spinner) v.findViewById(R.id.editEconomicStatus);
        spinnerLinkFacility = (Spinner) v.findViewById(R.id.editLinkFacilityId);
        economicStatusList = Arrays.asList(getResources().getStringArray(R.array.economic_status));

        // populate the link Facilities;
        linkFacilityList.clear();
        linkFacilities.clear();
        addLinkFacilities();
        ArrayAdapter<String> linkFacilityAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, linkFacilities);
        linkFacilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLinkFacility.setAdapter(linkFacilityAdapter);
        //lets set the selected
        if (linkFacility != null){
            int x = 0;
            for (LinkFacility e : linkFacilityList) {
                if (e.getId().equalsIgnoreCase(linkFacility.getId())){
                    spinnerLinkFacility.setSelection(x, true);
                    break;
                }
                x++;
            }
        }

        //in case we are editing
        setupEditingMode();

        checkPermissions();




        return v;
    }
    public void getLocation(){
        //get the location
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            //is GPS Enabled or not?
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // Is network enabled
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            //if both are off, we cannot get the GPS
            if (!isGPSEnabled && !isNetworkEnabled){
                //ask user to enable location
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                // Setting Dialog Title
                alertDialog.setTitle("GPS settings");

                // Setting Dialog Message
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

                // On pressing Settings button
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getContext().startActivity(intent);
                    }
                });

                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            }else{
                this.canGetLocation = true;
                // check for permissions
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    //show explanation for permissions
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Needs GPS Permission");
                    builder.setMessage("In order to work properly, this app needs GPS permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            }, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else{
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, PERMISSION_CALLBACK_CONSTANT);
                }

                //we have the permissions now
                // Get location from Network provider
                if (isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            textLat.setText("LAT: "+String.valueOf(latitude));
                            textLon.setText("LON: "+String.valueOf(longitude));
                        }
                    }
                }
                // we try getting the location form GPS
                if (isGPSEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            textLat.setText("LAT: "+String.valueOf(latitude));
                            textLon.setText("LON: "+String.valueOf(longitude));
                        }
                    }
                }

            }

        } catch (Exception e){}
    }
    public void checkPermissions(){
        try{
            if(ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[1])){
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Multiple Permissions Request");
                    builder.setMessage("This app needs Location permissions");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(),permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else if (permissionStatus.getBoolean(permissionsRequired[0], false)){
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Multiple Permissions Request");
                    builder.setMessage("This app needs Location permissions");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getContext(), "Go to Permissions to Grant Location Permissions",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else {
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0],true);
                editor.commit();
            }else{
                proceedAfterPermission();
            }
        }catch (Exception e){}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[1])){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(),permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    private void proceedAfterPermission() {
        getLocation();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.editDob:
                DialogFragment newFragment = new DatePickerFragment().newInstance(R.id.editDob);
                newFragment.show(getFragmentManager(), "DatePicker");
                break;

            case R.id.editRelocated:
                DialogFragment dateMovedFragment = new DatePickerFragment().newInstance(R.id.editRelocated);
                dateMovedFragment.show(getFragmentManager(), "Datepicker");
                break;
            case R.id.buttonSave:
                String id;
                if (editingVillage == null){
                    id = UUID.randomUUID().toString();
                }else{
                    id = editingVillage.getId();
                }

                if (editAreaChiefName.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Area chief name cannot be empty", Toast.LENGTH_LONG).show();
                    editAreaChiefName.requestFocus();
                    return;
                }
                if (editAreaChiefPhone.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Phone of the chief cannot be empty", Toast.LENGTH_LONG).show();
                    editAreaChiefPhone.requestFocus();
                    return;
                }
                String distanceToBranch = editDistanceToBranch.getText().toString();
                if (distanceToBranch.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Distance to branch is required", Toast.LENGTH_LONG).show();
                    editDistanceToBranch.requestFocus();
                    return;
                }
                String transportCost = editTransportCost.getText().toString();
                if (transportCost.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Transport cost to branch is required", Toast.LENGTH_LONG).show();
                    editTransportCost.requestFocus();
                    return;
                }
                String distanceToMainRoad = editDistanceToMainRoad.getText().toString();
                if (distanceToMainRoad.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Distance to the main road is required", Toast.LENGTH_LONG).show();
                    editDistanceToMainRoad.requestFocus();
                    return;
                }
                String numberOfhouseHolds = editNumberOfHouseHolds.getText().toString();
                if (numberOfhouseHolds.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Please enter the number of households", Toast.LENGTH_LONG).show();
                    editNumberOfHouseHolds.requestFocus();
                    return;
                }
                String populationDensity = "0";
//                String populationDensity = editPopulationDensity.getText().toString();
//                if (populationDensity.equalsIgnoreCase("")){
//                    Toast.makeText(getContext(), "Population density is required", Toast.LENGTH_LONG).show();
//                    editPopulationDensity.requestFocus();
//                    return;
//                }
                String distanceToHealthFacility = editDistanceToHealthFacility.getText().toString();
                if (distanceToHealthFacility.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Enter the distance to the nearest health facility",
                            Toast.LENGTH_LONG).show();
                    editDistanceToHealthFacility.requestFocus();
                    return;
                }
//                String actLevels = editActLevels.getText().toString();
//                if (editActLevels.getText().toString().equalsIgnoreCase("")){
//                    Toast.makeText(getContext(), "Enter the ACT levels of the nearest public" +
//                            " health facility", Toast.LENGTH_LONG).show();
//                    editActLevels.requestFocus();
//                    return;
//                }

                String villageName = editName.getText().toString();
                if (villageName.trim().equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Village Name is required", Toast.LENGTH_SHORT).show();
                    editName.requestFocus();
                    return;
                }
                String mappingId = session.getSavedMapping().getId();
                Double lat = latitude;
                Double lon = longitude;
                String country = session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY);
                String district = session.getSavedMapping().getDistrict();
                String county = session.getSavedMapping().getCounty();
                String subCountyId = session.getSavedMapping().getSubCounty();
                String communityUnit = ""; //UG does not have the community units, but provide for one.
                String linkFacilityId = ""; //UG does not have the link facilities, but provide for one.
                Integer lFacility = spinnerLinkFacility.getSelectedItemPosition();
                if (linkFacilityList.size() != 0 && lFacility != -1) {
                    linkFacilityId = linkFacilityList.get(spinnerLinkFacility.getSelectedItemPosition()).getId();
                }else{
                    linkFacilityId = "";
                }

                String areaChiefName = editAreaChiefName.getText().toString();
                String areaChiefPhone = editAreaChiefPhone.getText().toString();
                String distributorsInTheArea = editDistributors.getText().toString();
                String ward = ""; //No Wards in UG
                String parish = session.getSavedParish().getId();
                String economicStatus = economicStatusList.get(editEconomicStatus.getSelectedItemPosition());  //List<String> economicStatusList
                Boolean actStockPresent = getSelectedRadioItemValue(actStock).equalsIgnoreCase("Yes");
                String privateFacilityForAct = "";
                String privateFacilityForMrdt = "";
                String nameOfNgoDoingIccm = "";
                String nameOfNgoDoingMhealth = "";
                long dateAdded = new Date().getTime();
                boolean bracOperating = getSelectedRadioItemValue(editBrac).equalsIgnoreCase("Yes");
                Integer addedBy = Integer.valueOf(session.getUserDetails().get(SessionManagement.KEY_USERID));
                long numberOfChvs = 0L;
                long householdPerChv = 0L;
                long branch = Long.valueOf(distanceToBranch.equalsIgnoreCase("") ? "0" :
                        distanceToBranch);
                long transportCostToBranch = 0L;
                try {
                    transportCostToBranch = Long.valueOf(transportCost.equalsIgnoreCase("") ? "0" : transportCost);
                }catch(Exception e){}
                long mainRoad = 0L;
                try {
                    mainRoad = Long.valueOf(distanceToMainRoad
                            .equalsIgnoreCase("") ? "0" : distanceToMainRoad);
                }catch (Exception e){}
                long noOfHouseholds = 0L;
                try{
                    noOfHouseholds = Long.valueOf(editNumberOfHouseHolds.getText().toString()
                            .equalsIgnoreCase("") ? "0" : editNumberOfHouseHolds.getText().toString());
                }catch (Exception e){}
                long mohPoplationDensity = 0L;

                String comment = editComment.getText().toString();
                long estimatedPopulationDensity = 0L;
                try {
                    estimatedPopulationDensity = Long.valueOf(editPopulationDensity.getText().toString()
                            .equalsIgnoreCase("") ? "0" : editPopulationDensity.getText().toString());
                }catch (Exception e){}
                long distanceToNearestHealthFacility = 0L;
                try {
                    distanceToNearestHealthFacility = Long.valueOf(distanceToHealthFacility
                            .equalsIgnoreCase("") ? "0" : distanceToHealthFacility);
                }catch (Exception e){}

                long actLevel = 0L;

                long actPrice = 0L;
                try {
                    actPrice = getSelectedRadioItemValue(editActCost).equalsIgnoreCase("Less than 5000") ? 1000L : 6000L;
                }catch (Exception e){}


                Integer mtnSignal = Integer.valueOf(getSelectedRadioItemValue(mtn));
                Integer orangeSignal = Integer.valueOf(getSelectedRadioItemValue(orange));
                Integer airtelSignal = Integer.valueOf(getSelectedRadioItemValue(airtel));
                Integer safaricomSignal = 0;
                long mrdtLevels = 0L;
                long mrdtPrice = 0L;
                boolean synced = false;
                boolean presenceOfDistributors = getSelectedRadioItemValue(editPresenceDistributors).equalsIgnoreCase("Yes");
                boolean chvsTrained = false;
                boolean presenceOfHostels = false;
                boolean presenceOfEstates = getSelectedRadioItemValue(editPresenceEstates).equalsIgnoreCase("Yes");
                boolean ngoDoingIccm = getSelectedRadioItemValue(editNgosIccms).equalsIgnoreCase("Yes");
                boolean ngoDoingMhealth = getSelectedRadioItemValue(editNgosMhealth).equalsIgnoreCase("Yes");
                Integer numberOfFactories = Integer.valueOf(
                        getSelectedRadioItemValue(editPresenceOfFactories).equalsIgnoreCase("0") ? "0" :
                                getSelectedRadioItemValue(editPresenceOfFactories).equalsIgnoreCase("Less than 2") ? "2" : "3");
                boolean traderMarket = getSelectedRadioItemValue(editPresenceOfTraderMarket).equalsIgnoreCase("Yes");
                boolean largeSupermarket = getSelectedRadioItemValue(editPresenceOfSuperMarket).equalsIgnoreCase("Yes");
                boolean ngosGivingFreeDrugs = getSelectedRadioItemValue(editNgosGivingFreeDrugs).equalsIgnoreCase("Yes");




                Village village = new Village(id, villageName, mappingId, lat, lon, country,
                        district, county, subCountyId, parish, communityUnit, ward, linkFacilityId,
                        areaChiefName, areaChiefPhone, branch, transportCostToBranch, mainRoad,
                        noOfHouseholds, mohPoplationDensity, estimatedPopulationDensity,
                        economicStatus, distanceToNearestHealthFacility, actLevel, actPrice,
                        mrdtLevels, mrdtPrice, presenceOfHostels, presenceOfEstates,
                        numberOfFactories, presenceOfDistributors, distributorsInTheArea, traderMarket, largeSupermarket,
                        ngosGivingFreeDrugs, ngoDoingIccm, ngoDoingMhealth, nameOfNgoDoingIccm,
                        nameOfNgoDoingMhealth, privateFacilityForAct, privateFacilityForMrdt,
                        dateAdded, addedBy, comment, chvsTrained, synced, bracOperating, mtnSignal,
                        safaricomSignal, orangeSignal, airtelSignal, actStockPresent);
                VillageTable villageTable = new VillageTable(getContext());

                long saved = villageTable.addData(village);
                if (saved ==-1){
                    Toast.makeText(getContext(), "Error occured when saving the record", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Record saved successfully", Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editAreaChiefName.setText("");
                    editAreaChiefPhone.setText("");
                    editComment.setText("");
                    editDistanceToBranch.setText("");
                    editTransportCost.setText("");
                    editDistanceToMainRoad.setText("");
                    editNumberOfHouseHolds.setText("");
                    editPopulationDensity.setText("");
                    editDistributors.setText("");
                    editDistanceToHealthFacility.setText("");
                }

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(locationManager != null){
            try{
                locationManager.removeUpdates(this);
            }catch (SecurityException se){}

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

    public void setupEditingMode(){
        if (editingVillage != null){

            editName.setText(editingVillage.getVillageName().toString());
            editName.requestFocus();
            editAreaChiefName.setText(editingVillage.getAreaChiefName());
            editAreaChiefPhone.setText(editingVillage.getAreaChiefPhone());
            editComment.setText(editingVillage.getComment());
            editDistanceToBranch.setText(editingVillage.getDistanceToBranch().toString());
            editTransportCost.setText(editingVillage.getTransportCost().toString());
            editDistanceToMainRoad.setText(editingVillage.getDistanceToMainRoad().toString());
            editNumberOfHouseHolds.setText(editingVillage.getNoOfHouseholds().toString());
            editPopulationDensity.setText(editingVillage.getEstimatedPopulationDensity().toString());
            editDistributors.setText(editingVillage.getDistributorsInTheArea());
            editDistanceToHealthFacility.setText(editingVillage.getDistanceToNearestHealthFacility().toString());
//            editActLevels.setText(editingVillage.getActLevels().toString());
            editPresenceEstates.clearCheck();
            editPresenceEstates.check(editingVillage.getPresenceOfEstates() ? R.id.estate1 : R.id.estate2);
            editPresenceOfFactories.clearCheck();
            editPresenceOfFactories.check(editingVillage.getNumberOfFactories() == 0 ? R.id.cFactory1 :
                    editingVillage.getNumberOfFactories() == 2 ? R.id.cFactory2 : R.id.cFactory3);

            editPresenceOfTraderMarket.clearCheck();
            editPresenceOfTraderMarket.check(editingVillage.getTraderMarket() ? R.id.marketYes : R.id.marketNo);

            editPresenceOfSuperMarket.clearCheck();
            editPresenceOfSuperMarket.check(editingVillage.getLargeSupermarket() ? R.id.superMarketYes : R.id.SuperMarketNo);

            editNgosGivingFreeDrugs.clearCheck();
            editNgosGivingFreeDrugs.check(editingVillage.getNgosGivingFreeDrugs() ? R.id.drugsYes : R.id.drugsNo);

            editPresenceDistributors.clearCheck();
            editPresenceDistributors.check(editingVillage.getPresenceOfDistributors() ? R.id.distributorsYes : R.id.distributorsNo);
            editBrac.clearCheck();
            editBrac.check(editingVillage.isBracOperating() ? R.id.bracYes : R.id.bracNo);

            editNgosIccms.clearCheck();
            editNgosIccms.check(editingVillage.getNgoDoingIccm() ? R.id.iccmYes : R.id.iccmNo);

            editNgosMhealth.clearCheck();
            editNgosMhealth.check(editingVillage.getNgoDoingMhealth() ? R.id.mHealthYes : R.id.mHealthNo);

            actStock.clearCheck();
            if (editingVillage.getActStock()){
                actStock.check(R.id.actStockYes);
            }else{
                actStock.check(R.id.actStockNo);
            }

            editActCost.clearCheck();
            if (editingVillage.getActPrice().compareTo(5000L) > 0 ){
                editActCost.check(R.id.actPriceMore);
            }else{
                editActCost.check(R.id.actPriceLess);
            }

            mtn.clearCheck();
            switch (editingVillage.getMtnSignalStrength()){
                case 0:
                    mtn.check(R.id.mtn0);
                    break;
                case 1:
                    mtn.check(R.id.mtn1);
                    break;
                case 2:
                    mtn.check(R.id.mtn2);
                    break;
                case 3:
                    mtn.check(R.id.mtn3);
                    break;
                case 4:
                    mtn.check(R.id.mtn4);
                    break;
                default:
                    mtn.check(R.id.mtn0);
                    break;
            }

            airtel.clearCheck();
            switch (editingVillage.getAirtelSignalStrength()){
                case 0:
                    airtel.check(R.id.airtel0);
                    break;
                case 1:
                    airtel.check(R.id.airtel1);
                    break;
                case 2:
                    airtel.check(R.id.airtel2);
                    break;
                case 3:
                    airtel.check(R.id.airtel3);
                    break;
                case 4:
                    airtel.check(R.id.airtel4);
                    break;
                default:
                    airtel.check(R.id.airtel0);
                    break;
            }

            orange.clearCheck();
            switch (editingVillage.getOrangeSignalStrength()){
                case 0:
                    orange.check(R.id.orange0);
                    break;
                case 1:
                    orange.check(R.id.orange1);
                    break;
                case 2:
                    orange.check(R.id.orange2);
                    break;
                case 3:
                    orange.check(R.id.orange3);
                    break;
                case 4:
                    orange.check(R.id.orange4);
                    break;
                default:
                    orange.check(R.id.orange0);
                    break;
            }
            int x =0;
            for (String economic : economicStatusList){
                if (editingVillage.getEconomicStatus().equalsIgnoreCase(economic)){
                    editEconomicStatus.setSelection(x);
                    break;
                }
                x++;
            }

            //Health Facility
            x = 0;
            for (LinkFacility e : linkFacilityList) {
                if (e.getId().equalsIgnoreCase(editingVillage.getLinkFacilityId())){
                    spinnerLinkFacility.setSelection(x, true);
                    break;
                }
                x++;
            }
        }
    }
    //Location Methods
    @Override
    public void onLocationChanged(Location location){
        if (location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            textLat.setText("LAT: "+String.valueOf(latitude));
            textLon.setText("LON: "+String.valueOf(longitude));
        }
    }

    @Override
    public void onProviderEnabled(String provider){

    }

    @Override
    public void onProviderDisabled(String provider){

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    public String getSelectedRadioItemValue(RadioGroup radioGroup){
        Integer selectedButton = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton =(RadioButton) radioGroup.findViewById(selectedButton);
        String selectedValue = selectedRadioButton.getText().toString();
        return selectedValue;
    }
    public void addLinkFacilities() {
        LinkFacilityTable linkFacilityTable = new LinkFacilityTable(getContext());
        Log.d("TREMAP", "Testing");
        session.getSavedSubCounty();

        linkFacilityList = linkFacilityTable.getLinkFacilityBySubCounty(session.getSavedMapping().getSubCounty());
        for (LinkFacility lFacility: linkFacilityList){
            linkFacilities.add(lFacility.getFacilityName());
        }
    }
}
