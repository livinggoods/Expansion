package com.expansion.lg.kimaru.expansion.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.fragment.HomeFragment;
import com.expansion.lg.kimaru.expansion.fragment.InterviewsFragment;
import com.expansion.lg.kimaru.expansion.fragment.MappingFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewCommunityUnitFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewExamFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewInterviewFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewKeRecruitmentFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewLinkFacilityFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewKeMappingFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewRecruitmentFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewRegistrationFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewSubCountyFragment;
import com.expansion.lg.kimaru.expansion.fragment.NewUgMappingFragment;
import com.expansion.lg.kimaru.expansion.fragment.RegistrationsFragment;
import com.expansion.lg.kimaru.expansion.fragment.ExamsFragment;
import com.expansion.lg.kimaru.expansion.fragment.RecruitmentsFragment;
import com.expansion.lg.kimaru.expansion.other.SetUpApp;
import com.expansion.lg.kimaru.expansion.service.RecruitmentsSyncServiceAdapter;
import com.expansion.lg.kimaru.expansion.tables.MappingTable;


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
    public FloatingActionButton fab;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Start the Sync Service
        RecruitmentsSyncServiceAdapter.initializeSyncAdapter(getApplicationContext());

        //since we want login to be the first thing
        session = new SessionManagement(getBaseContext());
        //we cannow check login

        if (session.isInitialRun()){
            SetUpApp setUpApp = new SetUpApp();
            setUpApp.setUpEducation(getBaseContext());
        }

        session.checkLogin();

        //we can now extract User details
        HashMap<String, String> user = session.getUserDetails();

        //name
        name = user.get(SessionManagement.KEY_NAME);

        //Emails
        email = user.get(SessionManagement.KEY_EMAIL);
        country = user.get(SessionManagement.KEY_USER_COUNTRY);


        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clickedView = Integer.toString(view.getId());
                mView = view;
                loadFragment(view);
            }
        });

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
                Snackbar.make(view, "Please set fragment for " + CURRENT_TAG, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                        NewRegistrationFragment newRegistrationFragment = new NewRegistrationFragment();
                        Fragment fragment = newRegistrationFragment;
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
                if (country == "KE") {
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

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
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
        imgNavHeaderBg.setImageResource(R.drawable.nav_menu_header_bg);
        imgProfile.setImageResource(R.drawable.lg_bg);

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

            // show or hide the fab button
            toggleFab();
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

        // show or hide the fab button
        toggleFab();

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
            exportDB();


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

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 1 || navItemIndex == 2 || navItemIndex == 5)
            fab.show();
        else
            fab.hide();
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
}
