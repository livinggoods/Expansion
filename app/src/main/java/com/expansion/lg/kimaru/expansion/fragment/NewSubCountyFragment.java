package com.expansion.lg.kimaru.expansion.fragment;
/**
 * Created by kimaru on 3/11/17.
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager;
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
    EditText editCountyPopulation, editSubCountyPopulation, editNoOfVillages, editServicePopulation;
    EditText editTransportCost, editMajorRoads, editPrivateClinics, editPrivateClinicsInRadius;
    EditText editCommunityUnits, editMainSuperMarkets, editMainBanks, editMajorBusiness, editComment;

    RadioGroup editCountySupport, editSubCountySupport, editChvActivity, editRecommended;

    Spinner editPopulationDensity;

    Button buttonSave, buttonList;

    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    SessionManagement session;
    HashMap<String, String> user;
    GpsTracker gps;
    double latitude;
    double longitude;
    //flag for GPS Status
    boolean isGPSEnabled = false;

    //flag for net status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; //location
    // Due to Runtime Perms, let us declare some constants for the permissions
    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;


    // The minimum distance to change Updates in meters
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    // The minimum time between updates in milliseconds
    public static final long MIN_TIME_BETWEEN_UPDATE = 1000 * 60 / 2; // 30 seconds

    protected LocationManager locationManager;


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

        //get the current user
        session = new SessionManagement(getContext());
        user = session.getUserDetails();

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

        } catch (Exception e){}



        //Initialize the UI Components
        editName = (EditText) v.findViewById(R.id.editName);
        editContactPerson = (EditText) v.findViewById(R.id.editContactPerson);
        editPhone = (EditText) v.findViewById(R.id.editPhone);
        editMainTown = (EditText) v.findViewById(R.id.editMainTown);
        editMainTownPopulation = (EditText) v.findViewById(R.id.editMainTownPopulation);
        editCountyPopulation = (EditText) v.findViewById(R.id.editCountyPopulation);
        editSubCountyPopulation = (EditText) v.findViewById(R.id.editSubCountyPopulation);
        editNoOfVillages = (EditText) v.findViewById(R.id.editNoOfVillages);
        editServicePopulation = (EditText) v.findViewById(R.id.editServicePopulation);
        editTransportCost = (EditText) v.findViewById(R.id.editTransportCost);
        editMajorRoads = (EditText) v.findViewById(R.id.editMajorRoads);
        editPrivateClinics = (EditText) v.findViewById(R.id.editPrivateClinics);
        editPrivateClinicsInRadius = (EditText) v.findViewById(R.id.editPrivateClinicsInRadius);
        editCommunityUnits = (EditText) v.findViewById(R.id.editCommunityUnits);
        editMainSuperMarkets = (EditText) v.findViewById(R.id.editMainSuperMarkets);
        editMainBanks = (EditText) v.findViewById(R.id.editMainBanks);
        editMajorBusiness = (EditText) v.findViewById(R.id.editMajorBusiness);
        editComment = (EditText) v.findViewById(R.id.editComment);

        //radioButtonGrps
        editCountySupport = (RadioGroup) v.findViewById(R.id.editCountySupport);
        editSubCountySupport = (RadioGroup) v.findViewById(R.id.editSubCountySupport);
        editChvActivity = (RadioGroup) v.findViewById(R.id.editChvActivity);
        editRecommended = (RadioGroup) v.findViewById(R.id.editRecommended);

        //spinners
        editPopulationDensity = (Spinner) v.findViewById(R.id.editPopulationDensity);

        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        Toast.makeText(getContext(), "LA: "+ latitude + " LO: "+longitude, Toast.LENGTH_SHORT).show();

        return v;
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
                Integer currentDate =  (int) (new Date().getTime()/1000);

                Mapping mapping = session.getSavedMapping();

                String uuid = UUID.randomUUID().toString();
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
                String editDataCountyPopulation = editCountyPopulation.getText().toString();
                String editDataSubCountyPopulation = editSubCountyPopulation.getText().toString();
                String editDataNoOfVillages = editNoOfVillages.getText().toString();
                String editDataServicePopulation = editServicePopulation.getText().toString();
                String editDataTransportCost = editTransportCost.getText().toString();
                String editDataMajorRoads = editMajorRoads.getText().toString();
                String editDataPrivateClinics = editPrivateClinics.getText().toString();
                String editDataPrivateClinicsInRadius = editPrivateClinicsInRadius.getText().toString();
                String editDataCommunityUnits = editCommunityUnits.getText().toString();
                String editDataMainSuperMarkets = editMainSuperMarkets.getText().toString();
                String editDataMainBanks = editMainBanks.getText().toString();
                String editDataMajorBusiness = editMajorBusiness.getText().toString();
                String editDataComment = editComment.getText().toString();

                Integer countySupportRate = editCountySupport.getCheckedRadioButtonId();
                RadioButton cSupportRate =(RadioButton) editCountySupport.findViewById(countySupportRate);
                String countySupport = cSupportRate.getText().toString();

                //subcounty support
                Integer subCountySupportRate = editSubCountySupport.getCheckedRadioButtonId();
                RadioButton subCountySRate =(RadioButton) editSubCountySupport.findViewById(subCountySupportRate);
                String subCountySupport = subCountySRate.getText().toString();

                //Recommended
                Integer subCountyRecommended = editCountySupport.getCheckedRadioButtonId();
                RadioButton recommend =(RadioButton) editCountySupport.findViewById(subCountyRecommended);
                boolean isRecommended = recommend.getText().toString() == "yes";

                //chvActivity
                Integer intChvActivity = editChvActivity.getCheckedRadioButtonId();
                RadioButton selectedChvActivity =(RadioButton) editChvActivity.findViewById(intChvActivity);
                String chvActivity = selectedChvActivity.getText().toString();
                //editPopulationDensity
                String populationDensity = String.valueOf(editPopulationDensity.getSelectedItemId());
                String healtFacilities = "";

                // Do some validations
                if (editDataName.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Enter the Score for Motivation", Toast.LENGTH_SHORT).show();
                } else{
                    // Save Details

                    /**
                     *
                     * String servicePopulation, String populationDensity,
                     String transportCost, String majorRoads, String healtFacilities,
                     String privateClinicsInTown, String privateClinicsInRadius, String communityUnits,
                     String mainSupermarkets, String mainBanks, String anyMajorBusiness,
                     String comments, boolean recommended, Integer dateAdded, Integer addedBy)
                     */
                    SubCounty subCounty;
                    subCounty = new SubCounty(uuid, editDataName, mappingID, "", mappingID, lat, lon,
                            editDataContactPerson, editDataPhone, editDataMainTown, countySupport,
                            subCountySupport, chvActivity, editDataCountyPopulation,
                            editDataSubCountyPopulation, editDataNoOfVillages, editDataMainTownPopulation,
                            editDataServicePopulation, populationDensity, editDataTransportCost, editDataMajorRoads,
                            healtFacilities, editDataPrivateClinics, editDataPrivateClinicsInRadius,
                            editDataCommunityUnits,editDataMainSuperMarkets, editDataMainBanks, editDataMajorBusiness,
                            editDataComment, isRecommended, currentDate, addedBy);
                    SubCountyTable subCountyTable = new SubCountyTable(getContext());
                    long id  = subCountyTable.addData(subCounty);

                    if (id ==-1){
                        Toast.makeText(getContext(), "Could not save the results", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                        // Clear boxes
                        editName.setText("");
                        editContactPerson.setText("");
                        editPhone.setText("");
                        editMainTown.setText("");
                        editMainTownPopulation.setText("");
                        editCountyPopulation.setText("");
                        editSubCountyPopulation.setText("");
                        editNoOfVillages.setText("");
                        editServicePopulation.setText("");
                        editTransportCost.setText("");
                        editMajorRoads.setText("");
                        editPrivateClinics.setText("");
                        editPrivateClinicsInRadius.setText("");
                        editCommunityUnits.setText("");
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
}
