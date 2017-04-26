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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.GpsTracker;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewLinkFacilityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewLinkFacilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewLinkFacilityFragment extends Fragment implements OnClickListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout parentLayout;
    private OnFragmentInteractionListener mListener;
    List<EditText> communityUnitsCreated = new ArrayList<EditText>();
    List<EditText> cuPartner = new ArrayList<EditText>();

    Button buttonSave, buttonList;

    ImageView addNewIcon;
    EditText editFacilityName, editActLevels, editMrdtLevels;

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
    private int id = 0;

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

    public NewLinkFacilityFragment() {
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
    public static NewLinkFacilityFragment newInstance(String param1, String param2) {
        NewLinkFacilityFragment fragment = new NewLinkFacilityFragment();
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
        View v =  inflater.inflate(R.layout.fragment_new_link_facility, container, false);
        MainActivity.CURRENT_TAG =MainActivity.TAG_NEW_LINK_FACILITY;
        MainActivity.backFragment = new LinkFacilitiesFragment();

                //get the current user
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
//        parentLayout = (LinearLayout)findViewById(R.id.parentLayout);
        parentLayout = (LinearLayout) v.findViewById(R.id.editCuLayout);

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
        editFacilityName = (EditText) v.findViewById(R.id.editFacilityName);
        //editMrdtLevels = (EditText) v.findViewById(R.id.editMrdtLevels);
        //editActLevels = (EditText) v.findViewById(R.id.editActLevels);

        addNewIcon = (ImageView) v.findViewById(R.id.add_new_icon);
        addNewIcon.setImageResource(R.drawable.ic_add_box_black_24dp);
        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        addNewIcon.setOnClickListener(this);

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

            case R.id.add_new_icon:
                //editCuLayout
//                LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.editCuLayout);
//                EditText editText = new EditText(getContext());
//                editText.setId(id+1);
//                id+=1;
//                editText.setLayoutParams(new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT));
//                editText.setText("Niko " + id);
//                linearLayout.addView(editText);
//                Toast.makeText(getContext(), "Adding "+id, Toast.LENGTH_SHORT).show();

//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (
//                        RelativeLayout.LayoutParams.WRAP_CONTENT,
//                        RelativeLayout.LayoutParams.WRAP_CONTENT);
//                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                // params.setMargins(0,10,0,10);
                TextInputLayout textInputLayout = new TextInputLayout(getContext());
                textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                EditText edittTxt = new EditText(getContext());
                communityUnitsCreated.add(edittTxt);
                id++;
                if (id > 1) {
                    edittTxt.setHint("Add another Community Unit");
                }else{
                    edittTxt.setHint("Add Community Unit");
                }
                edittTxt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
//                edittTxt.setLayoutParams(params);
                // edtTxt.setBackgroundColor(Color.WHITE);
//                edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
//                edittTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                edittTxt.setId(id);
//                InputFilter[] fArray = new InputFilter[1];
//                fArray[0] = new InputFilter.LengthFilter(maxLength);
//                edittTxt.setFilters(fArray);
                textInputLayout.addView(edittTxt);
                parentLayout.addView(textInputLayout);

                break;
            case R.id.buttonList:
                //when user clicks this button, we will show the list

                LinkFacilitiesFragment linkFacilitiesFragment  = new LinkFacilitiesFragment();
                Fragment fragment = linkFacilitiesFragment;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_LINK_FACILITY;

                fragmentTransaction.replace(R.id.frame, fragment, "linkfacility");
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.buttonSave:
                // set date as integers
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                Integer currentDate =  (int) (new Date().getTime()/1000);

                Mapping mapping = session.getSavedMapping();
                String subCountyId = session.getSavedSubCounty().getId();

                String uuid = UUID.randomUUID().toString();
                String county = mapping.getCounty();
                String mappingId = mapping.getId();
                String lat = String.valueOf(latitude);
                String lon= String.valueOf(longitude);
                Integer addedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);
                String facilityName = editFacilityName.getText().toString();
                Long mrdtLevels = 0L; // Long.valueOf(editMrdtLevels.getText().toString());
                Long actLevels = 0L; //Long.valueOf(editActLevels.getText().toString());


                // retrieve all Cus Created
                int cuSize = communityUnitsCreated.size();
                String[] strings = new String[cuSize];


                for(int i=0; i < communityUnitsCreated.size(); i++){
                    //strings[i] = communityUnitsCreated.get(i).getText().toString();
                    String cuName = communityUnitsCreated.get(i).getText().toString();
                    Toast.makeText(getContext(), cuName, Toast.LENGTH_SHORT).show();

                    // Save the community unit
                    CommunityUnit communityUnit = new CommunityUnit("id", cuName, mappingId, lat, lon, country, subCountyId, uuid, "", "", "", "","","","", 0L, 0L, 0L, 0L, currentDate, addedBy, 0L,
                            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, false, false, false, false, false, false, false, false, false);
                    CommunityUnitTable communityUnitTable = new CommunityUnitTable(getContext());
                    communityUnitTable.addCommunityUnitData(communityUnit);
                }


                // Do some validations
                if (facilityName.toString().trim().equals("")){
                    Toast.makeText(getContext(), "Enter the name of the facility", Toast.LENGTH_SHORT).show();
                } else{
                    // Save Details
                    LinkFacility linkFacility = new LinkFacility(uuid, facilityName, country, mappingId, lat, lon, county, currentDate, addedBy,
                            actLevels, mrdtLevels);
                    LinkFacilityTable linkFacilityTable = new LinkFacilityTable(getContext());
                    long id  = linkFacilityTable.addData(linkFacility);

                    if (id ==-1){
                        Toast.makeText(getContext(), "Could not save the link Facility", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                        // Clear boxes
                        editFacilityName.setText("");
                        editFacilityName.requestFocus();
                        //editMrdtLevels.setText("");
                        //editActLevels.setText("");
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

    //CountyLocation Methods
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
