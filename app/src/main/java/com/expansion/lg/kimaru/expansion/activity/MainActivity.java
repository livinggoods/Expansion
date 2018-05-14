package com.expansion.lg.kimaru.expansion.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.fragment.HomeFragment;
import com.expansion.lg.kimaru.expansion.fragment.InterviewsFragment;
import com.expansion.lg.kimaru.expansion.fragment.MappingFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewCommunityUnitFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewExamFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewInterviewFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewKeRecruitmentFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewKeRegistrationFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewLinkFacilityFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewKeMappingFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewRecruitmentFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewRegistrationFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewSubCountyFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewUgMappingFragment;
import com.expansion.lg.kimaru.expansion.fragment.RegistrationsFragment;
import com.expansion.lg.kimaru.expansion.fragment.ExamsFragment;
import com.expansion.lg.kimaru.expansion.fragment.RecruitmentsFragment;
import com.expansion.lg.kimaru.expansion.fragment.SettingsFragment;
import com.expansion.lg.kimaru.expansion.other.CircleTransform;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.other.SetUpApp;
import com.expansion.lg.kimaru.expansion.service.MappingsSyncService;
import com.expansion.lg.kimaru.expansion.service.MappingsSyncServiceAdapter;
import com.expansion.lg.kimaru.expansion.service.RecruitmentsSyncServiceAdapter;
import com.expansion.lg.kimaru.expansion.sync.ApiClient;
import com.expansion.lg.kimaru.expansion.sync.IccmDataSync;
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync;
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable;
import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.MappingTable;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;
import com.expansion.lg.kimaru.expansion.tables.WardTable;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    public NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    public Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    public static final String TAG_HOME = "home";
    public static final String TAG_RECRUITMENTS = "recruitments";
    public static final String TAG_REGISTRATIONS = "registrations";
    public static final String TAG_EXAMS = "exams";
    public static final String TAG_INTERVIEWS = "interviews";
    public static final String TAG_MAPPINGS = "mappings";
    public static final String TAG_MAPPING = "mapping";
    public static final String TAG_COUNTY = "county";
    public static final String TAG_COUNTIES = "counties";
    public static final String TAG_SUBCOUNTIES = "subcounties";
    public static final String TAG_SUBCOUNTY = "subcounty";
    public static final String TAG_COMMUNITY_UNIT = "communityunit";
    public static final String TAG_COMMUNITY_UNITS = "communityunits";
    public static final String TAG_VILLAGE = "village";
    public static final String TAG_VILLAGES = "villages";
    public static final String TAG_LINK_FACILITY = "linkFacility";
    public static final String TAG_NEW_LINK_FACILITY = "newLinkFacilities";
    public static final String TAG_LINK_FACILITIES = "linkFacilities";
    public static final String TAG_MAPPING_VIEW = "mappingView";
    public static final String TAG_MAP_VIEW = "mapView";
    public static final String TAG_NEW_COMMUNITY_UNIT = "newCommunityUnit";
    public static final String TAG_NEW_EXAM = "newExam";
    public static final String TAG_NEW_INTERVIEW = "newInterview";
    public static final String TAG_NEW_MAPPING = "newMapping";
    public static final String TAG_NEW_RECRUITMENT = "newRecruitment";
    public static final String TAG_NEW_REGISTRATION = "newRegistration";
    public static final String TAG_NEW_SUB_COUNTY = "newSubCounty";
    public static final String TAG_NEW_VILLAGE = "newVillage";
    public static final String TAG_PARTNERS = "partners";
    public static final String TAG_SUBCOUNTY_VIEW = "subCountyView";
    public static final String REGISTRATION_VIEW = "registrationView";

    public static String CURRENT_TAG = TAG_HOME;

    public static Fragment backFragment = null;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private View mView;


    // We add the dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    //We add the Session Manager
    SessionManagement session;
    String name, email;
    String country;
    Context context;
    SweetAlertDialog pDialog;

    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    String[] permissionsRequired = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private int i =-1;
    private LocationDataSync locationDataSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManagement(getBaseContext());
        session.checkLogin();

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //we cannow check login
        context = this;

        new EducationTable(getBaseContext()).createEducationLevels();
        try {
            SetUpApp setUpApp = new SetUpApp(getBaseContext());
            setUpApp.setUpEducation();
        }catch (Exception e){
            new EducationTable(getBaseContext()).createEducationLevels();
        }

        locationDataSync = new LocationDataSync(getBaseContext());

        if (session.isLoggedIn()){
            RecruitmentsSyncServiceAdapter.initializeSyncAdapter(getApplicationContext());
            MappingsSyncServiceAdapter.initializeSyncAdapter(getApplicationContext());
            if (session.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("UG")){
                //Force the app to sync all the location details on initial Load
                if(!session.isInitialDataSynced()){
                    new syncLocations().execute(new Constants(getApplicationContext()).getCloudAddress()+"/api/v1/sync/locations");
                }
                /**
                 * sync Counties, subcounties, parish, villages
                 * the county, subcounty and villages are in the same DB (one class is enough to pull the data)
                 */
            }else{
                IccmDataSync iccmDataSync = new IccmDataSync(getBaseContext());
                iccmDataSync.pollNewComponents();
                if(!session.isInitialDataSynced()){
                    new syncKeWards().execute(new Constants(context).getCloudAddress()+"/api/v1/sync/ke-counties");
                }
            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//
//                    } catch (Exception e) {
//
//                    }
//                }
//            }).start();
        }



        //we can now extract User details
        HashMap<String, String> user = session.getUserDetails();

        //name
        name = user.get(SessionManagement.KEY_NAME);

        //Emails
        email = user.get(SessionManagement.KEY_EMAIL);
        country = user.get(SessionManagement.KEY_USER_COUNTRY);

        if (country.equalsIgnoreCase("UG")){
            CountyLocationTable countyLocationTable = new CountyLocationTable(getBaseContext());
            countyLocationTable.createLocations();
        }


        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /**
     * Nimeona that the Frgaments are not getting loaded properly
     * THis is a dirty trick to ensure that they get loaded
     */

    public void loadFragment(View view){
        Runnable mPendingRunnable;
        switch (CURRENT_TAG){
            case TAG_HOME:
                break;
            case TAG_RECRUITMENTS:
                mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        Fragment fragment = null;
                        switch (country){
                            case "KE":
                                NewKeRecruitmentFragment newKeRecruitmentFragment = new NewKeRecruitmentFragment();
                                fragment = newKeRecruitmentFragment;
                                break;
                            case "UG":
                                NewRecruitmentFragment newRecruitmentFragment = new NewRecruitmentFragment();
                                fragment = newRecruitmentFragment;
                                break;
                        }
                        if (fragment != null){
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            fragmentTransaction.replace( R.id.frame, fragment).addToBackStack( CURRENT_TAG).commit();
                        }

                    }
                };
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
                break;
            case TAG_REGISTRATIONS:
                mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        Fragment fragment;
                        if (country.equalsIgnoreCase("UG")) {
                            NewRegistrationFragment newRegistrationFragment = new NewRegistrationFragment();
                            fragment = newRegistrationFragment;
                        }else{
                            NewKeRegistrationFragment newKeRegistrationFragment = new NewKeRegistrationFragment();
                            fragment = newKeRegistrationFragment;
                        }
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);

                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
                break;
            case TAG_EXAMS:
                mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        NewExamFragment newExamFragment = new NewExamFragment();
                        Fragment fragment = newExamFragment;
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);

                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
                break;
            case TAG_INTERVIEWS:
                mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        NewInterviewFragment newInterviewFragment = new NewInterviewFragment();
                        Fragment fragment = newInterviewFragment;
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);

                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
                break;

            case TAG_MAPPINGS:
                Toast.makeText(getBaseContext(), "Countyr is "+ country, Toast.LENGTH_SHORT).show();
                if (country.equalsIgnoreCase("KE")) {
                    mPendingRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // update the main content by replacing fragments
                            NewKeMappingFragment newKeMappingFragment = new NewKeMappingFragment();
                            Fragment fragment = newKeMappingFragment;
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);

                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    };
                    // If mPendingRunnable is not null, then add to the message queue
                    if (mPendingRunnable != null) {
                        mHandler.post(mPendingRunnable);
                    }
                }else{
                    mPendingRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // update the main content by replacing fragments
                            NewUgMappingFragment newUgMappingFragment = new NewUgMappingFragment();
                            Fragment fragment = newUgMappingFragment;
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);

                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    };
                    // If mPendingRunnable is not null, then add to the message queue
                    if (mPendingRunnable != null) {
                        mHandler.post(mPendingRunnable);
                    }
                }
                break;

            case TAG_SUBCOUNTIES:
                mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        NewSubCountyFragment newSubCountyFragment = new NewSubCountyFragment();
                        Fragment fragment = newSubCountyFragment;
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);

                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
                break;
            case TAG_COMMUNITY_UNITS:
                mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        NewCommunityUnitFragment newCommunityUnitFragment = new NewCommunityUnitFragment();
                        Fragment fragment = newCommunityUnitFragment;
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);

                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
                break;

            case TAG_LINK_FACILITIES:
                mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        NewLinkFacilityFragment newLinkFacilityFragment = new NewLinkFacilityFragment();
                        Fragment fragment = newLinkFacilityFragment;
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);

                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
                break;

            case TAG_VILLAGES:
                break;
            case TAG_COUNTIES:
                break;
            case TAG_PARTNERS:
                break;

        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText(name);
        txtWebsite.setText(email);
        Glide.with(this).load(getImage("nav_menu_header_bg"))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);


        //imgNavHeaderBg.setImageResource(R.drawable.nav_menu_header_bg);

        Glide.with(this).load(getImage("lg_bg"))
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
        //imgProfile.setImageResource(R.drawable.lg_bg);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);

                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // recruitments fragment
                RecruitmentsFragment recruitmentsFragment = new RecruitmentsFragment();
                return recruitmentsFragment;
            case 2:
                // registrations fragment
                // Before loading the Registrations, ensure that the user has already set the recruitment\
                if (session.isRecruitmentSet()){
                    RegistrationsFragment registrationsFragment = new RegistrationsFragment();
                    return registrationsFragment;
                } else {
                    Toast.makeText(this, "Please set the recruitment by long pressing the recruitment", Toast.LENGTH_SHORT).show();
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_RECRUITMENTS;
                    return new RecruitmentsFragment();
                }

            case 3:
                // exams fragment
                // Before loading the Exams, ensure that the user has already set the recruitment
                if (session.isRecruitmentSet()){
                    ExamsFragment examsFragment = new ExamsFragment();
                    return examsFragment;
                } else {
                    Toast.makeText(this, "Long press on Recruitment before administering an exam", Toast.LENGTH_SHORT).show();
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_RECRUITMENTS;
                    return new RecruitmentsFragment();
                }

            case 4:
                // interviews fragment
                // Before loading the Interviews, ensure that the user has already set the recruitment
                if (session.isRecruitmentSet()){
                    InterviewsFragment interviewsFragment = new InterviewsFragment();
                    return interviewsFragment;
                } else {
                    Toast.makeText(this, "Long press on Recruitment before conducting an interview", Toast.LENGTH_SHORT).show();
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_RECRUITMENTS;
                    return new RecruitmentsFragment();
                }
            case 5:
                navItemIndex = 5;
                CURRENT_TAG = TAG_MAPPING;
                MappingFragment mappingFragment = new MappingFragment();
                return mappingFragment;

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void setSelectedNavMenu(int navItemIndex){
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    public void setUpMenus(int itemIndex, String tag){
        navItemIndex = itemIndex;
        CURRENT_TAG = tag;
        setUpNavigationView();
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_recruitments:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_RECRUITMENTS;
                        break;
                    case R.id.nav_registrations:
                        if (session.isRecruitmentSet()){
                            navItemIndex = 2;
                            CURRENT_TAG = TAG_REGISTRATIONS;
                        }else {
                            Toast.makeText(getBaseContext(), "Long press on Recruitment before creating a registration", Toast.LENGTH_LONG).show();
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_RECRUITMENTS;
                        }
                        break;
                    case R.id.nav_notifications:
                        if (session.isRecruitmentSet()){
                            navItemIndex = 3;
                            CURRENT_TAG = TAG_EXAMS;
                        }else {
                            Toast.makeText(getBaseContext(), "Long press on Recruitment before conducting an exam", Toast.LENGTH_SHORT).show();
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_RECRUITMENTS;
                        }
                        break;
                    case R.id.nav_settings:
                        if (session.isRecruitmentSet()){
                            navItemIndex = 4;
                            CURRENT_TAG = TAG_INTERVIEWS;
                        }else {
                            Toast.makeText(getBaseContext(), "Long press on Recruitment before conducting an interview", Toast.LENGTH_SHORT).show();
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_RECRUITMENTS;
                        }
                        break;

                    case R.id.nav_mapping:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_MAPPING;
                        break;

                    case R.id.nav_app_setting:
                        drawer.closeDrawers();
                        Fragment fragment = new SettingsFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                        fragmentTransaction.commitAllowingStateLoss();
                        return true;

                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_http_server:
                        startActivity(new Intent(MainActivity.this, HttpServerActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_logout:
                        //logout User
                        session.logoutUser();
                        drawer.closeDrawers();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        return true;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        Runnable mPendingRunnable;
        if (backFragment != null){
            mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    // update the main content by replacing fragments
                    Fragment fragment = backFragment;
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            };

            // If mPendingRunnable is not null, then add to the message queue
            if (mPendingRunnable != null) {
                mHandler.post(mPendingRunnable);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            // Logout our user here
            session.logoutUser();
            return true;
        }
        if (id == R.id.backup_db) {
            Toast.makeText(getApplicationContext(), "Backing up the DB!", Toast.LENGTH_LONG).show();
            checkPermissions();


//            try {
//                File sd = Environment.getExternalStorageDirectory();
//                File data = Environment.getDataDirectory();
//
//                if (sd.canWrite()) {
//                    String currentDBPath = "//data//data//" + getPackageName() + "//databases//expansion.db";
//                    String backupDBPath = "expansiondb.db";
////                    File currentDB = new File(data, currentDBPath);
//                    File currentDB = getDatabasePath(MappingTable.DATABASE_NAME);
//                    File backupDB = new File(sd, backupDBPath);
//                    Toast.makeText(this, getPackageName(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(this, currentDB.getAbsolutePath(), Toast.LENGTH_LONG).show();
//                    FileChannel src = new FileInputStream(currentDB).getChannel();
//                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
//                    dst.transferFrom(src, 0, src.size());
//                    src.close();
//                    dst.close();
//
//                    if (currentDB.exists()) {
//                        Toast.makeText(this, "DB file is found...", Toast.LENGTH_LONG).show();
//                        FileChannel srcs = new FileInputStream(currentDB).getChannel();
//                        FileChannel dsts = new FileOutputStream(backupDB).getChannel();
//                        dst.transferFrom(srcs, 0, srcs.size());
//                        srcs.close();
//                        dsts.close();
//                        Toast.makeText(this, "BCK Completed successfully", Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(this, "Unable to backup. File not found", Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                }
//            } catch (Exception e) {
//                Toast.makeText(this, "Error \n"+e.getMessage(), Toast.LENGTH_LONG).show();
//            }
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkPermissions(){
        try{
            if(ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[0])){
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Multiple Permissions Request");
                    builder.setMessage("This app needs Location permissions");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(MainActivity.this,permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Multiple Permissions Request");
                    builder.setMessage("This app needs Location permissions");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant Location Permissions",
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
                    ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[0])){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
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
                Toast.makeText(this,"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    private void proceedAfterPermission() {
        exportDB();
    }

    private void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ getPackageName() +"/databases/"+MappingTable.DATABASE_NAME;
        String backupDBPath = MappingTable.DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        //File backupDB = new File(sd, backupDBPath);
        File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "expansion_backup.db");
        Toast.makeText(this, backupDB.getAbsolutePath(), Toast.LENGTH_LONG).show();
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error in backing up the db!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class syncLocations extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Loading. Please wait");
            pDialog.show();
            pDialog.setCancelable(false);
        }

        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            // Start Village
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        locationDataSync.getVillageDatafromCloud();
                        Log.d("Tremap", "GETTING Village Data in a thread");
                    } catch (Exception e) {
                        Log.d("Tremap", "ERROR GETTING Village Data");
                        Log.d("Tremap", e.getMessage());
                    }
                }
            }).start();

            // Start Parish
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        locationDataSync.getParishDatafromCloud();
                        Log.d("Tremap", "GETTING Parish Data in a thread");
                    } catch (Exception e) {
                        Log.d("Tremap", "ERROR GETTING Parish Data");
                        Log.d("Tremap", e.getMessage());
                    }
                }
            }).start();
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("locations");
                    CountyLocationTable countyLocationTable = new CountyLocationTable(getBaseContext());
                    for (int x = 0; x < recs.length(); x++){
                        countyLocationTable.fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                }

            }
            return stream;
        }
        protected void onPostExecute(String stream){
            session.flagSynced(true);
            pDialog.dismiss();
        } // onPostExecute() end
    }


    private class syncKeWards extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Loading Wards. Please wait");
            pDialog.show();
            pDialog.setCancelable(false);
        }

        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("counties");
                    SubCountyTable subCountyTable = new SubCountyTable(context);
                    for (int x = 0; x < recs.length(); x++){
                        // I have the county Details, lets extract all subcounties
                        JSONObject county = recs.getJSONObject(x);
                        JSONArray subCounties = county.getJSONArray("subcounties");
                        for (int a = 0; a<subCounties.length(); a++){
                            // I have the subcounty, create the subcounty
                            JSONObject subCounty = subCounties.getJSONObject(a);
                            subCountyTable.fromJson(subCounty);
                            //also get the wards
                            JSONArray wards = subCounty.getJSONArray("wards");
                            for (int b=0; b<wards.length(); b++){
                                JSONObject ward = wards.getJSONObject(b);
                                //create a ward
                                new WardTable(context).fromJson(ward);
                            }
                        }
                    }
                }catch(JSONException e){
                    Log.d("TREMAP", "KE County Sync ERROR "+e.getMessage());
                }

            }
            return stream;
        }
        protected void onPostExecute(String stream){
            session.flagSynced(true);
            pDialog.dismiss();
        } // onPostExecute() end
    }
}
