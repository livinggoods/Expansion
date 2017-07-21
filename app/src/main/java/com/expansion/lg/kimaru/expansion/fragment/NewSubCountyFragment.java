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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.GpsTracker;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewSubCountyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewSubCountyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewSubCountyFragment extends Fragment implements OnClickListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    EditText editName, editContactPerson, editPhone, editMainTown, editMainTownPopulation;
    EditText editSubCountyPopulation, editNoOfVillages, editServicePopulation, editMainTownGPS;
    EditText editTransportCost, editMajorRoads, editPrivateClinics, editPrivateClinicsInRadius;
    EditText editCommunityUnits, editMainSuperMarkets, editMainBanks, editMajorBusiness, editComment;

    RadioGroup editCountySupport, editSubCountySupport, editChvActivity, editRecommended;

    Spinner editPopulationDensity;

    Button buttonSave, buttonList;
    CheckBox canUseCurrentPosition;

    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;

    public SubCounty subCountyEditing = null;


    SessionManagement session;
    HashMap<String, String> user;
    double latitude, longitude;
    boolean isGPSEnabled = false;
    //flag for net status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;
    private int id = 0;

    Location location; //location
    // Due to Runtime Perms, let us declare some constants for the permissions
    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;


    // The minimum distance to change Updates in meters
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    // The minimum time between updates in milliseconds
    public static final long MIN_TIME_BETWEEN_UPDATE = 1000 * 60 / 2; // 30 seconds

    protected LocationManager locationManager;

    Mapping mapping;


    //show alerts
    AlertDialogManager alert = new AlertDialogManager();






    public NewSubCountyFragment() {
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
    public static NewSubCountyFragment newInstance(String param1, String param2) {
        NewSubCountyFragment fragment = new NewSubCountyFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_sub_county, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_SUB_COUNTY;
        MainActivity.backFragment = new SubCountiesFragment();
                //get the current user
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
        mapping = session.getSavedMapping();
        canUseCurrentPosition = (CheckBox) v.findViewById(R.id.chkGps);

        editMainTownGPS = (EditText) v.findViewById(R.id.editMainTownGPS);

        checkIfLocationIsEnabled();
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
                            editMainTownGPS.setText("Network Lat "+ String.valueOf(latitude)+" Lon " + String.valueOf(longitude));
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
                            editMainTownGPS.setText("GPS Lat "+ String.valueOf(latitude)+" Lon " + String.valueOf(longitude));
                        }
                    }
                }

            }

        } catch (Exception e){}



        //Initialize the UI Components
        editName = (EditText) v.findViewById(R.id.editName);
        editContactPerson = (EditText) v.findViewById(R.id.editContactPerson);
        editPhone = (EditText) v.findViewById(R.id.editPhone);
        editMainTown = (EditText) v.findViewById(R.id.editMainTown);
        editMainTownPopulation = (EditText) v.findViewById(R.id.editMainTownPopulation);
        // editCountyPopulation = (EditText) v.findViewById(R.id.editCountyPopulation);
        editSubCountyPopulation = (EditText) v.findViewById(R.id.editSubCountyPopulation);
        editNoOfVillages = (EditText) v.findViewById(R.id.editNoOfVillages);
        editServicePopulation = (EditText) v.findViewById(R.id.editServicePopulation);
        editTransportCost = (EditText) v.findViewById(R.id.editTransportCost);
        editMajorRoads = (EditText) v.findViewById(R.id.editMajorRoads);
        editPrivateClinics = (EditText) v.findViewById(R.id.editPrivateClinics);
        editPrivateClinicsInRadius = (EditText) v.findViewById(R.id.editPrivateClinicsInRadius);
        //editCommunityUnits = (EditText) v.findViewById(R.id.editCommunityUnits);
        editMainSuperMarkets = (EditText) v.findViewById(R.id.editMainSuperMarkets);
        editMainBanks = (EditText) v.findViewById(R.id.editMainBanks);
        editMajorBusiness = (EditText) v.findViewById(R.id.editMajorBusiness);
        editComment = (EditText) v.findViewById(R.id.editComment);

        //radioButtonGrps
        editCountySupport = (RadioGroup) v.findViewById(R.id.editCountySupport);
        editSubCountySupport = (RadioGroup) v.findViewById(R.id.editSubCountySupport);
        //editChvActivity = (RadioGroup) v.findViewById(R.id.editChvActivity);
        editRecommended = (RadioGroup) v.findViewById(R.id.editRecommended);

        //spinners
        editPopulationDensity = (Spinner) v.findViewById(R.id.editPopulationDensity);

        //at this point, let us setup Editing Mode
        setUpEditingMode();

        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        try{
            if(ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[0])){
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

        return v;
    }

    public void checkIfLocationIsEnabled(){
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

            }
        } catch (Exception e){}
    }

    public void getLocation(){
        this.canGetLocation = true;
        // check for permissions
        if(ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[0])){
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
                    }
                }
            }
        }


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
            case R.id.buttonList:
                //when user clicks this button, we will show the list

                SubCountiesFragment subCountiesFragment = new SubCountiesFragment();
                Fragment fragment = subCountiesFragment;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

                fragmentTransaction.replace(R.id.frame, fragment, "subcounties");
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.buttonSave:
                // set date as integers
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                Long currentDate =  new Date().getTime();

                Mapping mapping = session.getSavedMapping();

                String uuid;
                if (subCountyEditing != null){
                    uuid = subCountyEditing.getId();
                }else{
                    uuid = UUID.randomUUID().toString();
                }
                String county = mapping.getCounty();
                String mappingID = mapping.getId();
                String lat = String.valueOf(latitude);
                String lon= String.valueOf(longitude);
                Integer addedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));

                String editDataName = editName.getText().toString();
                String editDataContactPerson = editContactPerson.getText().toString();
                String editDataPhone = editPhone.getText().toString();
                String editDataMainTown = editMainTown.getText().toString();
                String editDataMainTownPopulation = editMainTownPopulation.getText().toString();
                String editDataCountyPopulation = "0"; // editCountyPopulation.getText().toString();
                String editDataSubCountyPopulation = editSubCountyPopulation.getText().toString();
                String editDataNoOfVillages = editNoOfVillages.getText().toString();
                String editDataServicePopulation = editServicePopulation.getText().toString();
                String editDataTransportCost = editTransportCost.getText().toString();
                String editDataMajorRoads = editMajorRoads.getText().toString();
                String editDataPrivateClinics = editPrivateClinics.getText().toString();
                String editDataPrivateClinicsInRadius = editPrivateClinicsInRadius.getText().toString();
                String editDataCommunityUnits = ""; //editCommunityUnits.getText().toString();
                String editDataMainSuperMarkets = editMainSuperMarkets.getText().toString();
                String editDataMainBanks = editMainBanks.getText().toString();
                String editDataMajorBusiness = editMajorBusiness.getText().toString();
                String editDataComment = editComment.getText().toString();

                String countySupport;
                try{
                    Integer countySupportRate = editCountySupport.getCheckedRadioButtonId();
                    RadioButton cSupportRate =(RadioButton) editCountySupport.findViewById(countySupportRate);
                    countySupport = cSupportRate.getText().toString();
                }catch (Exception e){
                    countySupport = "0";
                }


                //subcounty support
                String subCountySupport;
                try{
                    Integer subCountySupportRate = editSubCountySupport.getCheckedRadioButtonId();
                    RadioButton subCountySRate =(RadioButton) editSubCountySupport.findViewById(subCountySupportRate);
                    subCountySupport = subCountySRate.getText().toString();
                }catch ( Exception e){
                    subCountySupport = "0";
                }

                boolean isRecommended;
                try{
                    //Recommended
                    Integer subCountyRecommended = editCountySupport.getCheckedRadioButtonId();
                    RadioButton recommend =(RadioButton) editCountySupport.findViewById(subCountyRecommended);
                    isRecommended = recommend.getText().toString() == "yes";
                } catch (Exception e){
                    isRecommended = false;
                }


                //chvActivity
                // Integer intChvActivity = editChvActivity.getCheckedRadioButtonId();
                // RadioButton selectedChvActivity =(RadioButton) editChvActivity.findViewById(intChvActivity);
                String chvActivity = "0"; //selectedChvActivity.getText().toString();
                //editPopulationDensity
                String populationDensity = String.valueOf(editPopulationDensity.getSelectedItemId());
                String healtFacilities = "";

                // Do some validations
                if (editDataName.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Enter the name of Subcounty", Toast.LENGTH_SHORT).show();
                } else{
                    // Save Details

                    SubCounty subCounty = new SubCounty();
                    boolean useCurrentGps = canUseCurrentPosition.isChecked();
                    if (useCurrentGps){
                        subCounty.setLat(lat);
                        subCounty.setLon(lon);
                    }

                    subCounty.setSubCountyName(editDataName);
                    subCounty.setId(uuid);
                    subCounty.setCountyID(county);
                    subCounty.setCountry(mapping.getCountry());
                    subCounty.setMappingId(mappingID);
                    subCounty.setContactPerson(editDataContactPerson);
                    subCounty.setContactPersonPhone(editDataPhone);
                    subCounty.setMainTown(editDataMainTown);
                    subCounty.setCountySupport(countySupport);
                    subCounty.setSubcountySupport(subCountySupport);
                    subCounty.setChvActivityLevel(chvActivity);
                    subCounty.setCountyPopulation(editDataCountyPopulation);
                    subCounty.setSubCountyPopulation(editDataSubCountyPopulation);
                    subCounty.setNoOfVillages(editDataNoOfVillages);
                    subCounty.setMainTownPopulation(editDataMainTownPopulation);
                    subCounty.setServicePopulation(editDataServicePopulation);
                    subCounty.setPopulationDensity(populationDensity);
                    subCounty.setTransportCost(editDataTransportCost);
                    subCounty.setMajorRoads(editDataMajorRoads);
                    subCounty.setHealtFacilities(healtFacilities);
                    subCounty.setPrivateClinicsInRadius(editDataPrivateClinicsInRadius);
                    subCounty.setPrivateClinicsInTown(editDataPrivateClinics);
                    subCounty.setCommunityUnits(editDataCommunityUnits);
                    subCounty.setMainSupermarkets(editDataMainSuperMarkets);
                    subCounty.setMainBanks(editDataMainBanks);
                    subCounty.setAnyMajorBusiness(editDataMajorBusiness);
                    subCounty.setComments(editDataComment);
                    subCounty.setRecommended(isRecommended);
                    subCounty.setDateAdded(currentDate);
                    subCounty.setAddedBy(addedBy);

                    SubCountyTable subCountyTable = new SubCountyTable(getContext());
                    long id;
                    if (subCountyEditing == null){
                        id  = subCountyTable.addData(subCounty);
                    }else{
                        id  = subCountyTable.editData(subCounty);
                    }

                    if (id ==-1){
                        Toast.makeText(getContext(), "Could not save the results", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Operation successful", Toast.LENGTH_SHORT).show();
                        //clear the subcounty
                        subCountyEditing = null;

                        // Clear boxes
                        editName.setText("");
                        editContactPerson.setText("");
                        editPhone.setText("");
                        editMainTown.setText("");
                        editMainTownPopulation.setText("");
//                        editCountyPopulation.setText("");
                        editSubCountyPopulation.setText("");
                        editNoOfVillages.setText("");
                        editServicePopulation.setText("");
                        editTransportCost.setText("");
                        editMajorRoads.setText("");
                        editPrivateClinics.setText("");
                        editPrivateClinicsInRadius.setText("");
//                        editCommunityUnits.setText("");
                        editMainSuperMarkets.setText("");
                        editMainBanks.setText("");
                        editMajorBusiness.setText("");
                        editComment.setText("");
                        editName.requestFocus();
                    }

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New Sub County for " + mapping.getCounty());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        //When we detach, lets stop GPS
        if(locationManager != null){
            try {
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

    //Location Methods
    @Override
    public void onLocationChanged(Location location){
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        editMainTownGPS.setText("Current Lat "+ String.valueOf(latitude)+" Lon " + String.valueOf(longitude));

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

    public void setUpEditingMode(){
        if (subCountyEditing != null){
            editName.setText(subCountyEditing.getSubCountyName());
            editContactPerson.setText(subCountyEditing.getContactPerson());
            editPhone.setText(subCountyEditing.getContactPersonPhone());
            editMainTown.setText(subCountyEditing.getMainTown());
            editMainTownPopulation.setText(subCountyEditing.getMainTownPopulation());
            editSubCountyPopulation.setText(subCountyEditing.getSubCountyPopulation());
            editNoOfVillages.setText(subCountyEditing.getNoOfVillages());
            editServicePopulation.setText(subCountyEditing.getServicePopulation());
            editTransportCost.setText(subCountyEditing.getTransportCost());
            editMajorRoads.setText(subCountyEditing.getMajorRoads());
            editPrivateClinics.setText(subCountyEditing.getPrivateClinicsInTown());
            editPrivateClinicsInRadius.setText(subCountyEditing.getPrivateClinicsInRadius());
            editMainSuperMarkets.setText(subCountyEditing.getMainSupermarkets());
            editMainBanks.setText(subCountyEditing.getMainBanks());
            editMajorBusiness.setText(subCountyEditing.getAnyMajorBusiness());
            editComment.setText(subCountyEditing.getComments());
            //clear all radios

            if (!subCountyEditing.getCountySupport().equalsIgnoreCase("")){
                editSubCountySupport.clearCheck();
                editSubCountySupport.check(Integer.valueOf(subCountyEditing.getCountySupport()));
            }

            editRecommended.clearCheck();
            editRecommended.check(Integer.valueOf(subCountyEditing.isRecommended() ? 1 : 0));
        }
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
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[0])){
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
                Toast.makeText(getContext(),"Unable to get necessary Permissions. The app may be unreliable.",Toast.LENGTH_LONG).show();
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
}
