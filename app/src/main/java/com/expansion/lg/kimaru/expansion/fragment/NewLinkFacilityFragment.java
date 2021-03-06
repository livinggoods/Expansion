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
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.GpsTracker;
import com.expansion.lg.kimaru.expansion.other.UtilFunctions;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.koushikdutta.async.http.body.JSONObjectBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.fabric.sdk.android.Fabric;


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

    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    Button buttonSave, buttonList;

    JSONArray jsonArray;
    JSONObject jsonResults;

    EditText editFacilityName, editActLevels, editMrdtLevels, editMflCode;
    TextView textLat, textLon;

    LinearLayout layoutQuestions;

    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 100;


    SessionManagement session;
    HashMap<String, String> user;
    double latitude;
    double longitude;
    //flag for GPS Status
    boolean isGPSEnabled = false;
    boolean hasLocation = false;

    LinkFacility editingLinkFacility = null;

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
        Fabric.with(getContext(), new Crashlytics());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_link_facility, container, false);
        MainActivity.CURRENT_TAG = MainActivity.TAG_NEW_LINK_FACILITY;
        MainActivity.backFragment = new LinkFacilitiesFragment();

        //get the current user
        session = new SessionManagement(getContext());
        user = session.getUserDetails();
        // check Permissions
        checkPermissions();

        editFacilityName = (EditText) v.findViewById(R.id.editFacilityName);
        editMrdtLevels = (EditText) v.findViewById(R.id.editMrdtLevels);
        editActLevels = (EditText) v.findViewById(R.id.editActLevels);
        editMflCode = (EditText) v.findViewById(R.id.editMflCode);
        textLat = (TextView) v.findViewById(R.id.textLat);
        textLon = (TextView) v.findViewById(R.id.textLon);

        if (user.get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("ug")) {
            editMrdtLevels.setVisibility(View.GONE);
            editActLevels.setVisibility(View.GONE);

            jsonArray = new JSONArray();
        } else {

            editMrdtLevels.setVisibility(View.GONE);
            editActLevels.setVisibility(View.GONE);

            String jsonStr = UtilFunctions.loadFromAsset(getContext(), "link_facility.json");
            try {
                jsonArray = new JSONArray(jsonStr);
            } catch (JSONException ex) {
                ex.printStackTrace();
                Crashlytics.log(ex.toString());
            }
        }

        layoutQuestions = (LinearLayout) v.findViewById(R.id.layout_additional_questions);

        textLat.setText("Capturing LAT");
        textLon.setText("Capturing LON");

        if (editingLinkFacility != null) {

            for (int i = 0; i < jsonArray.length(); i++) {
                // TODO implement some logic over here
            }

            setUpEditing();
        }


        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        setupAdditionalQuestions();

        return v;
    }

    /**
     * Adds additional questions
     */
    private void setupAdditionalQuestions() {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                View questionView = getQuestionView(json);

                if (questionView != null)
                    layoutQuestions.addView(questionView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Crashlytics.log(e.toString());
        }

    }

    private View getQuestionView(JSONObject json) throws JSONException {

        String type = json.getString("type");

        switch (type) {
            case "label":
                return createLabelField(json);
            case "input":
                return createInputField(json);
            case "radio":
                return createRadioButtons(json);
            default:
                return null;
        }
    }

    private LinearLayout getContainer() {

        LinearLayout container = new LinearLayout(getContext());
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.VERTICAL);

        float scale = getResources().getDisplayMetrics().density;

        int paddingLeft = (int) (scale * 10 + 0.5f);
        int paddingRight = (int) (scale * 0 + 0.5f);
        int paddingTop = (int) (scale * 0 + 0.5f);
        int paddingBottom = (int) (scale * 0 + 0.5f);

        container.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        return container;
    }

    private TextView getQuestionLabel(String title) {
        TextView label = new TextView(getContext());
        label.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        label.setText(title);

        float scale = getResources().getDisplayMetrics().density;

        int paddingLeft = (int) (scale * 0 + 0.5f);
        int paddingRight = (int) (scale * 0 + 0.5f);
        int paddingTop = (int) (scale * 16 + 0.5f);
        int paddingBottom = (int) (scale * 2 + 0.5f);

        label.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        label.setTextColor(getResources().getColor(android.R.color.primary_text_light));

        return label;
    }

    private View createRadioButtons(JSONObject json) throws JSONException {
        String title = json.getString("title");
        String value = json.getString("value");
        String name = json.getString("name");
        JSONArray options = json.getJSONArray("options");

        RadioGroup input = new RadioGroup(getContext());
        input.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        input.setOrientation(RadioGroup.HORIZONTAL);
        input.setTag(name);

        for (int i = 0; i < options.length(); i++) {
            JSONObject optionObj = options.getJSONObject(i);
            String option = optionObj.getString("option");

            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(option);
            radioButton.setTag(value);

            input.addView(radioButton);

            if (option.equals(value)) {
                input.check(radioButton.getId());
            }
        }


        LinearLayout container = getContainer();
        container.addView(getQuestionLabel(title));
        container.addView(input);

        return container;
    }

    private LinearLayout createInputField(JSONObject json) throws JSONException {
        String title = json.getString("title");
        String value = json.getString("value");
        String name = json.getString("name");
        String constraint = json.getString("constraint");

        EditText input = new EditText(getContext());
        input.setTag(name);
        input.setHint(title);
        input.setText(value);

        switch (constraint) {
            case "number":
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "tel":
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            default:
                input.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        LinearLayout container = getContainer();
        container.addView(getQuestionLabel(title));
        container.addView(input);
        return container;
    }

    private View createLabelField(JSONObject json) throws JSONException {
        float scale = getResources().getDisplayMetrics().density;

        int paddingLeft = (int) (scale * 0 + 0.5f);
        int paddingRight = (int) (scale * 0 + 0.5f);
        int paddingTop = (int) (scale * 32 + 0.5f);
        int paddingBottom = (int) (scale * 0 + 0.5f);

        TextView label = new TextView(getContext());
        label.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        label.setText(json.getString("title").toUpperCase());
        label.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        label.setTypeface(label.getTypeface(), Typeface.BOLD);
        return label;
    }

    public void getLocation() {
        //get the location
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            //is GPS Enabled or not?
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // Is network enabled
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            //if both are off, we cannot get the GPS
            if (!isGPSEnabled && !isNetworkEnabled) {
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

            } else {
                this.canGetLocation = true;
                // check for permissions
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, PERMISSION_CALLBACK_CONSTANT);
                }

                //we have the permissions now
                // Get location from Network provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            textLat.setText("LAT: " + String.valueOf(latitude));
                            textLon.setText("LON: " + String.valueOf(longitude));
                        }
                    }
                }
                // we try getting the location form GPS
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            textLat.setText("LAT: " + String.valueOf(latitude));
                            textLon.setText("LON: " + String.valueOf(longitude));
                        }
                    }
                }

            }

        } catch (Exception e) {
            Crashlytics.log(e.toString());
        }
    }

    public void checkPermissions() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[1])) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Multiple Permissions Request");
                    builder.setMessage("This app needs Location permissions");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
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
                } else {
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0], true);
                editor.commit();
            } else {
                proceedAfterPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.log(e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[1])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
                // Toast.makeText(getContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editDob:
                DialogFragment newFragment = new DatePickerFragment().newInstance(R.id.editDob);
                newFragment.show(getFragmentManager(), "DatePicker");
                break;


            case R.id.buttonList:
                //when user clicks this button, we will show the list

                LinkFacilitiesFragment linkFacilitiesFragment = new LinkFacilitiesFragment();
                Fragment fragment = linkFacilitiesFragment;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, fragment, "linkfacility");
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.buttonSave:
                // set date as integers

                Toast.makeText(getContext(), "Validating and saving", Toast.LENGTH_SHORT).show();
                Long currentDate = new Date().getTime();
                Mapping mapping = session.getSavedMapping();
                String subCountyId;
                String uuid;
                if (editingLinkFacility != null) {
                    uuid = editingLinkFacility.getId();
                } else {
                    uuid = UUID.randomUUID().toString();
                }

                String county = mapping.getCounty();
                String subCounty;
                if (session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("UG")) {
                    subCounty = mapping.getSubCounty();
                    subCountyId = mapping.getSubCounty();
                } else {
                    subCounty = session.getSavedSubCounty().getId();
                    subCountyId = session.getSavedSubCounty().getId();
                }

                String mappingId = mapping.getId();
                Integer addedBy = Integer.parseInt(user.get(SessionManagement.KEY_USERID));
                String country = user.get(SessionManagement.KEY_USER_COUNTRY);
                String facilityName = editFacilityName.getText().toString();
                String mflCode = editMflCode.getText().toString();
                Long mrdtLevels = 0L;
                if (!editMrdtLevels.getText().toString().trim().equals("")) {
                    mrdtLevels = Long.valueOf(editMrdtLevels.getText().toString());
                }

                Long actLevels = 0L;
                if (!editActLevels.getText().toString().trim().equals("")) {
                    actLevels = Long.valueOf(editActLevels.getText().toString());
                }

                // Do some validations
                if (facilityName.toString().trim().equals("")) {
                    Toast.makeText(getContext(), "Enter the name of the facility", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO Validate additional fields
                // TODO Get other field
                try {
                    if (!validateExtraFields()) {
                        Log.e("JSON", jsonResults.toString());
                        throw new Exception("Please check your input");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Crashlytics.log(e.toString());
                    return;
                }

                if (location == null) {
                    Toast.makeText(getContext(),
                            "The location has not been captured",
                            Toast.LENGTH_SHORT).show();
                }
                String parishId = null;
                if (user.get(SessionManagement.KEY_USER_COUNTRY).equals("UG"))
                    parishId = session.getSavedParish().getId();
                else
                    parishId = null;
                LinkFacility linkFacility = new LinkFacility(uuid, facilityName, country, mappingId,
                        latitude, longitude, subCounty, currentDate, addedBy, actLevels, mrdtLevels, mflCode, county, parishId, jsonResults.toString());

                LinkFacilityTable linkFacilityTable = new LinkFacilityTable(getContext());
                long id = linkFacilityTable.addData(linkFacility);

                if (id == -1) {
                    Toast.makeText(getContext(), "Could not save the link Facility", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    editFacilityName.setText("");
                    editFacilityName.requestFocus();
                    editMrdtLevels.setText("");
                    editActLevels.setText("");
                }
        }
    }

    private boolean validateExtraFields() throws JSONException {
        boolean isValid = true;
        jsonResults = new JSONObject();


        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject json = jsonArray.getJSONObject(i);
            String name = json.getString("name");
            String type = json.getString("type");
            boolean required = json.getBoolean("required");
            if (type.equals("label"))
                continue;

            String value = getValue(name, type);

            if (required && value.equals("")) {
                Log.e("Is Valid", "FALSe");
                Log.e("name", name +": " + value);
                isValid = false;
            }

            jsonResults.put(name, value);

        }

        return isValid;
    }

    private String getValue(String name, String type) {

        switch (type) {

            case "input":
                EditText editText = (EditText) layoutQuestions.findViewWithTag(name);
                return editText.getText().toString();

            case "radio":
                RadioGroup radioGroup = (RadioGroup) layoutQuestions.findViewWithTag(name);
                int selected = radioGroup.getCheckedRadioButtonId();
                if (selected != -1) {
                    RadioButton radioButton = (RadioButton) layoutQuestions.findViewById(selected);
                    return radioButton.getText().toString();
                }
                return "";

            default:
                return "";
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
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(this);
            } catch (SecurityException se) {
                Crashlytics.log(se.toString());
            }
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
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        textLat.setText("LAT: " + String.valueOf(latitude));
        textLon.setText("LON: " + String.valueOf(longitude));

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void setUpEditing() {
        if (editingLinkFacility != null) {
            editFacilityName.setText(editingLinkFacility.getFacilityName());
            editMrdtLevels.setText(String.valueOf(editingLinkFacility.getMrdtLevels()));
            editActLevels.setText(String.valueOf(editingLinkFacility.getActLevels()));
            editMflCode.setText(editingLinkFacility.getMflCode());
            textLat.setText(String.valueOf(editingLinkFacility.getLat()));
            textLon.setText(String.valueOf(editingLinkFacility.getLon()));

            String other = editingLinkFacility.getOther();
            Log.e("OTHER", other);
            try {
                jsonResults = new JSONObject(other);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if (obj.getString("type").equals("label")) {
                        continue;
                    }

                    obj.put("value", jsonResults.get(obj.getString("name")));
                    jsonArray.put(i, obj);
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
                Crashlytics.log(ex.toString());
            }
        }
    }
}
