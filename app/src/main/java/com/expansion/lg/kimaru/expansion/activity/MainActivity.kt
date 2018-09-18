package com.expansion.lg.kimaru.expansion.activity


import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.fragment.HomeFragment
import com.expansion.lg.kimaru.expansion.fragment.InterviewsFragment
import com.expansion.lg.kimaru.expansion.fragment.MappingFragment
import com.expansion.lg.kimaru.expansion.fragment.NewCommunityUnitFragment
import com.expansion.lg.kimaru.expansion.fragment.NewExamFragment
import com.expansion.lg.kimaru.expansion.fragment.NewInterviewFragment
import com.expansion.lg.kimaru.expansion.fragment.NewKeRecruitmentFragment
import com.expansion.lg.kimaru.expansion.fragment.NewKeRegistrationFragment
import com.expansion.lg.kimaru.expansion.fragment.NewLinkFacilityFragment
import com.expansion.lg.kimaru.expansion.fragment.NewKeMappingFragment
import com.expansion.lg.kimaru.expansion.fragment.NewRecruitmentFragment
import com.expansion.lg.kimaru.expansion.fragment.NewRegistrationFragment
import com.expansion.lg.kimaru.expansion.fragment.NewSubCountyFragment
import com.expansion.lg.kimaru.expansion.fragment.NewUgMappingFragment
import com.expansion.lg.kimaru.expansion.fragment.RegistrationsFragment
import com.expansion.lg.kimaru.expansion.fragment.ExamsFragment
import com.expansion.lg.kimaru.expansion.fragment.RecruitmentsFragment
import com.expansion.lg.kimaru.expansion.fragment.SettingsFragment
import com.expansion.lg.kimaru.expansion.other.CircleTransform
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.other.SetUpApp
import com.expansion.lg.kimaru.expansion.service.MappingsSyncService
import com.expansion.lg.kimaru.expansion.service.MappingsSyncServiceAdapter
import com.expansion.lg.kimaru.expansion.service.RecruitmentsSyncServiceAdapter
import com.expansion.lg.kimaru.expansion.sync.ApiClient
import com.expansion.lg.kimaru.expansion.sync.IccmDataSync
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync
import com.expansion.lg.kimaru.expansion.tables.CountyLocationTable
import com.expansion.lg.kimaru.expansion.tables.EducationTable
import com.expansion.lg.kimaru.expansion.tables.MappingTable
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable
import com.expansion.lg.kimaru.expansion.tables.WardTable
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.HashMap


class MainActivity : AppCompatActivity() {

    lateinit var navigationView: NavigationView
    private var drawer: DrawerLayout? = null
    private var navHeader: View? = null
    private var imgNavHeaderBg: ImageView? = null
    private var imgProfile: ImageView? = null
    private var txtName: TextView? = null
    private var txtWebsite: TextView? = null
    lateinit var toolbar: Toolbar
    // toolbar titles respected to selected nav menu item
    private var activityTitles: Array<String>? = null

    // flag to load home fragment when user presses back key
    private val shouldLoadHomeFragOnBackPress = true
    private var mHandler: Handler? = null

    private val mView: View? = null


    // We add the dialog Manager
    internal var alert = AlertDialogManager()

    //We add the Session Manager
    internal lateinit var session: SessionManagement
    internal var name: String? = null
    internal var email: String? = null
    internal var country: String? = null
    internal lateinit var context: Context
    internal lateinit var pDialog: SweetAlertDialog
    internal var permissionsRequired = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val permissionStatus: SharedPreferences? = null
    private var sentToSettings = false
    private val i = -1
    private var locationDataSync: LocationDataSync? = null

    private// home
    // recruitments fragment
    // registrations fragment
    // Before loading the Registrations, ensure that the user has already set the recruitment\
    // exams fragment
    // Before loading the Exams, ensure that the user has already set the recruitment
    // interviews fragment
    // Before loading the Interviews, ensure that the user has already set the recruitment
    val homeFragment: Fragment
        get() {
            when (navItemIndex) {
                0 -> {
                    return HomeFragment()
                }
                1 -> {
                    return RecruitmentsFragment()
                }
                2 -> if (session.isRecruitmentSet) {
                    return RegistrationsFragment()
                } else {
                    Toast.makeText(this, "Please set the recruitment by long pressing the recruitment", Toast.LENGTH_SHORT).show()
                    navItemIndex = 1
                    CURRENT_TAG = TAG_RECRUITMENTS
                    return RecruitmentsFragment()
                }

                3 -> if (session.isRecruitmentSet) {
                    return ExamsFragment()
                } else {
                    Toast.makeText(this, "Long press on Recruitment before administering an exam", Toast.LENGTH_SHORT).show()
                    navItemIndex = 1
                    CURRENT_TAG = TAG_RECRUITMENTS
                    return RecruitmentsFragment()
                }

                4 -> if (session.isRecruitmentSet) {
                    return InterviewsFragment()
                } else {
                    Toast.makeText(this, "Long press on Recruitment before conducting an interview", Toast.LENGTH_SHORT).show()
                    navItemIndex = 1
                    CURRENT_TAG = TAG_RECRUITMENTS
                    return RecruitmentsFragment()
                }
                5 -> {
                    navItemIndex = 5
                    CURRENT_TAG = TAG_MAPPING
                    return MappingFragment()
                }

                else -> return HomeFragment()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManagement(baseContext)
        session.checkLogin()

        setContentView(R.layout.activity_main)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        //we cannow check login
        context = this

        EducationTable(baseContext).createEducationLevels()
        try {
            val setUpApp = SetUpApp(baseContext)
            setUpApp.setUpEducation()
        } catch (e: Exception) {
            EducationTable(baseContext).createEducationLevels()
        }

        locationDataSync = LocationDataSync(baseContext)

        if (session.isLoggedIn) {
            RecruitmentsSyncServiceAdapter.initializeSyncAdapter(applicationContext)
            MappingsSyncServiceAdapter.initializeSyncAdapter(applicationContext)
            if (session.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
                //Force the app to sync all the location details on initial Load
                if (!session.isInitialDataSynced) {
                    syncLocations().execute(Constants(applicationContext).cloudAddress + "/api/v1/sync/locations")
                }
                /**
                 * sync Counties, subcounties, parish, villages
                 * the county, subcounty and villages are in the same DB (one class is enough to pull the data)
                 */
            } else {
                val iccmDataSync = IccmDataSync(baseContext)
                iccmDataSync.pollNewComponents()
                if (!session.isInitialDataSynced) {
                    syncKeWards().execute(Constants(context).cloudAddress + "/api/v1/sync/ke-counties")
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
        val user = session.userDetails

        //name
        name = user[SessionManagement.KEY_NAME]!!

        //Emails
        email = user[SessionManagement.KEY_EMAIL]!!
        country = user[SessionManagement.KEY_USER_COUNTRY]!!

        if (country.equals("UG", ignoreCase = true)) {
            val countyLocationTable = CountyLocationTable(baseContext)
            countyLocationTable.createLocations()
        }


        setSupportActionBar(toolbar)

        mHandler = Handler()

        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView


        // Navigation view header
        navHeader = navigationView.getHeaderView(0)
        txtName = navHeader!!.findViewById<View>(R.id.name) as TextView
        txtWebsite = navHeader!!.findViewById<View>(R.id.website) as TextView
        imgNavHeaderBg = navHeader!!.findViewById<View>(R.id.img_header_bg) as ImageView
        imgProfile = navHeader!!.findViewById<View>(R.id.img_profile) as ImageView

        // load toolbar titles from string resources
        activityTitles = resources.getStringArray(R.array.nav_item_activity_titles)

        // load nav menu header data
        loadNavHeader()

        // initializing navigation menu
        setUpNavigationView()

        if (savedInstanceState == null) {
            navItemIndex = 0
            CURRENT_TAG = TAG_HOME
            loadHomeFragment()
        }
    }

    /**
     * Nimeona that the Frgaments are not getting loaded properly
     * THis is a dirty trick to ensure that they get loaded
     */

    fun loadFragment(view: View) {
        val mPendingRunnable: Runnable?
        when (CURRENT_TAG) {
            TAG_HOME -> {
            }
            TAG_RECRUITMENTS -> {
                mPendingRunnable = Runnable {
                    // update the main content by replacing fragments
                    var fragment: Fragment? = null
                    when (country) {
                        "KE" -> {
                            val newKeRecruitmentFragment = NewKeRecruitmentFragment()
                            fragment = newKeRecruitmentFragment
                        }
                        "UG" -> {
                            val newRecruitmentFragment = NewRecruitmentFragment()
                            fragment = newRecruitmentFragment
                        }
                    }
                    if (fragment != null) {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        fragmentTransaction.replace(R.id.frame, fragment).addToBackStack(CURRENT_TAG).commit()
                    }
                }
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler!!.post(mPendingRunnable)
                }
            }
            TAG_REGISTRATIONS -> {
                mPendingRunnable = Runnable {
                    // update the main content by replacing fragments
                    val fragment: Fragment
                    if (country.equals("UG", ignoreCase = true)) {
                        val newRegistrationFragment = NewRegistrationFragment()
                        fragment = newRegistrationFragment
                    } else {
                        val newKeRegistrationFragment = NewKeRegistrationFragment()
                        fragment = newKeRegistrationFragment
                    }
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)

                    fragmentTransaction.commitAllowingStateLoss()
                }
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler!!.post(mPendingRunnable)
                }
            }
            TAG_EXAMS -> {
                mPendingRunnable = Runnable {
                    // update the main content by replacing fragments
                    val newExamFragment = NewExamFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, newExamFragment, CURRENT_TAG)

                    fragmentTransaction.commitAllowingStateLoss()
                }
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler!!.post(mPendingRunnable)
                }
            }
            TAG_INTERVIEWS -> {
                mPendingRunnable = Runnable {
                    // update the main content by replacing fragments
                    val newInterviewFragment = NewInterviewFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, newInterviewFragment, CURRENT_TAG)

                    fragmentTransaction.commitAllowingStateLoss()
                }
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler!!.post(mPendingRunnable)
                }
            }

            TAG_MAPPINGS -> {
                Toast.makeText(baseContext, "Countyr is $country", Toast.LENGTH_SHORT).show()
                if (country.equals("KE", ignoreCase = true)) {
                    mPendingRunnable = Runnable {
                        // update the main content by replacing fragments
                        val newKeMappingFragment = NewKeMappingFragment()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        fragmentTransaction.replace(R.id.frame, newKeMappingFragment, CURRENT_TAG)

                        fragmentTransaction.commitAllowingStateLoss()
                    }
                    // If mPendingRunnable is not null, then add to the message queue
                    if (mPendingRunnable != null) {
                        mHandler!!.post(mPendingRunnable)
                    }
                } else {
                    mPendingRunnable = Runnable {
                        // update the main content by replacing fragments
                        val newUgMappingFragment = NewUgMappingFragment()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        fragmentTransaction.replace(R.id.frame, newUgMappingFragment, CURRENT_TAG)

                        fragmentTransaction.commitAllowingStateLoss()
                    }
                    // If mPendingRunnable is not null, then add to the message queue
                    if (mPendingRunnable != null) {
                        mHandler!!.post(mPendingRunnable)
                    }
                }
            }

            TAG_SUBCOUNTIES -> {
                mPendingRunnable = Runnable {
                    // update the main content by replacing fragments
                    val newSubCountyFragment = NewSubCountyFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, newSubCountyFragment, CURRENT_TAG)

                    fragmentTransaction.commitAllowingStateLoss()
                }
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler!!.post(mPendingRunnable)
                }
            }
            TAG_COMMUNITY_UNITS -> {
                mPendingRunnable = Runnable {
                    // update the main content by replacing fragments
                    val newCommunityUnitFragment = NewCommunityUnitFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, newCommunityUnitFragment, CURRENT_TAG)

                    fragmentTransaction.commitAllowingStateLoss()
                }
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler!!.post(mPendingRunnable)
                }
            }

            TAG_LINK_FACILITIES -> {
                mPendingRunnable = Runnable {
                    // update the main content by replacing fragments
                    val newLinkFacilityFragment = NewLinkFacilityFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, newLinkFacilityFragment, CURRENT_TAG)

                    fragmentTransaction.commitAllowingStateLoss()
                }
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler!!.post(mPendingRunnable)
                }
            }

            TAG_VILLAGES -> {
            }
            TAG_COUNTIES -> {
            }
            TAG_PARTNERS -> {
            }
        }

        //Closing drawer on item click
        drawer!!.closeDrawers()

        // refresh toolbar menu
        invalidateOptionsMenu()
    }

    fun getImage(imageName: String): Int {

        return resources.getIdentifier(imageName, "drawable", packageName)
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private fun loadNavHeader() {
        // name, website
        txtName!!.text = name
        txtWebsite!!.text = email
        Glide.with(this).load(getImage("nav_menu_header_bg"))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg!!)


        //imgNavHeaderBg.setImageResource(R.drawable.nav_menu_header_bg);

        Glide.with(this).load(getImage("lg_bg"))
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile!!)
        //imgProfile.setImageResource(R.drawable.lg_bg);

        // showing dot next to notifications label
        navigationView.menu.getItem(3).setActionView(R.layout.menu_dot)
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private fun loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu()

        // set toolbar title
        setToolbarTitle()

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (supportFragmentManager.findFragmentByTag(CURRENT_TAG) != null) {
            drawer!!.closeDrawers()
            return
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        val mPendingRunnable = Runnable {
            // update the main content by replacing fragments
            val fragment = homeFragment
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out)
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)

            fragmentTransaction.commitAllowingStateLoss()
        }

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler!!.post(mPendingRunnable)
        }
        //Closing drawer on item click
        drawer!!.closeDrawers()

        // refresh toolbar menu
        invalidateOptionsMenu()
    }

    private fun setToolbarTitle() {
        supportActionBar!!.title = activityTitles!![navItemIndex]
    }

    fun setSelectedNavMenu(navItemIndex: Int) {
        navigationView.menu.getItem(navItemIndex).isChecked = true
    }

    private fun selectNavMenu() {
        navigationView.menu.getItem(navItemIndex).isChecked = true
    }

    fun setUpMenus(itemIndex: Int, tag: String) {
        navItemIndex = itemIndex
        CURRENT_TAG = tag
        setUpNavigationView()
    }

    private fun setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            // This method will trigger on item Click of navigation menu
            //Check to see which item was being clicked and perform appropriate action
            when (menuItem.itemId) {
                //Replacing the main content with ContentFragment Which is our Inbox View;
                R.id.home -> {
                    navItemIndex = 0
                    CURRENT_TAG = TAG_HOME
                }
                R.id.nav_recruitments -> {
                    navItemIndex = 1
                    CURRENT_TAG = TAG_RECRUITMENTS
                }
                R.id.nav_registrations -> if (session.isRecruitmentSet) {
                    navItemIndex = 2
                    CURRENT_TAG = TAG_REGISTRATIONS
                } else {
                    Toast.makeText(baseContext, "Long press on Recruitment before creating a registration", Toast.LENGTH_LONG).show()
                    navItemIndex = 1
                    CURRENT_TAG = TAG_RECRUITMENTS
                }
                R.id.nav_notifications -> if (session.isRecruitmentSet) {
                    navItemIndex = 3
                    CURRENT_TAG = TAG_EXAMS
                } else {
                    Toast.makeText(baseContext, "Long press on Recruitment before conducting an exam", Toast.LENGTH_SHORT).show()
                    navItemIndex = 1
                    CURRENT_TAG = TAG_RECRUITMENTS
                }
                R.id.nav_settings -> if (session.isRecruitmentSet) {
                    navItemIndex = 4
                    CURRENT_TAG = TAG_INTERVIEWS
                } else {
                    Toast.makeText(baseContext, "Long press on Recruitment before conducting an interview", Toast.LENGTH_SHORT).show()
                    navItemIndex = 1
                    CURRENT_TAG = TAG_RECRUITMENTS
                }

                R.id.nav_mapping -> {
                    navItemIndex = 5
                    CURRENT_TAG = TAG_MAPPING
                }

                R.id.nav_app_setting -> {
                    drawer!!.closeDrawers()
                    val fragment = SettingsFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)
                    fragmentTransaction.commitAllowingStateLoss()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_about_us -> {
                    // launch new intent instead of loading fragment
                    startActivity(Intent(this@MainActivity, AboutUsActivity::class.java))
                    drawer!!.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_privacy_policy -> {
                    // launch new intent instead of loading fragment
                    startActivity(Intent(this@MainActivity, PrivacyPolicyActivity::class.java))
                    drawer!!.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_http_server -> {
                    startActivity(Intent(this@MainActivity, HttpServerActivity::class.java))
                    drawer!!.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_logout -> {
                    //logout User
                    session.logoutUser()
                    drawer!!.closeDrawers()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }

                else -> navItemIndex = 0
            }

            //Checking if the item is in checked state or not, if not make it in checked state
            if (menuItem.isChecked) {
                menuItem.isChecked = false
            } else {
                menuItem.isChecked = true
            }
            menuItem.isChecked = true

            loadHomeFragment()

            true
        })


        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            override fun onDrawerClosed(drawerView: View) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView)
            }
        }

        //Setting the actionbarToggle to drawer layout
        drawer!!.setDrawerListener(actionBarDrawerToggle)

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawers()
            return
        }
        val mPendingRunnable: Runnable?
        if (backFragment != null) {
            mPendingRunnable = Runnable {
                // update the main content by replacing fragments
                val fragment = backFragment
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)
                fragmentTransaction.commitAllowingStateLoss()
            }

            // If mPendingRunnable is not null, then add to the message queue
            if (mPendingRunnable != null) {
                mHandler!!.post(mPendingRunnable)
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            menuInflater.inflate(R.menu.main, menu)
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            menuInflater.inflate(R.menu.notifications, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_logout) {
            Toast.makeText(applicationContext, "Logout user!", Toast.LENGTH_LONG).show()
            // Logout our user here
            session.logoutUser()
            return true
        }
        if (id == R.id.backup_db) {
            Toast.makeText(applicationContext, "Backing up the DB!", Toast.LENGTH_LONG).show()
            checkPermissions()


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
            return true
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(applicationContext, "All notifications marked as read!", Toast.LENGTH_LONG).show()
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(applicationContext, "Clear all notifications!", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

    fun checkPermissions() {
        try {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {
                    //Show Information about why you need the permission
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Multiple Permissions Request")
                    builder.setMessage("This app needs Location permissions")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()
                        ActivityCompat.requestPermissions(this@MainActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                    }
                    builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                    builder.show()
                } else if (permissionStatus!!.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Multiple Permissions Request")
                    builder.setMessage("This app needs Location permissions")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()
                        sentToSettings = true
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                        Toast.makeText(baseContext, "Go to Permissions to Grant Location Permissions",
                                Toast.LENGTH_LONG).show()
                    }
                    builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                val editor = permissionStatus!!.edit()
                editor.putBoolean(permissionsRequired[0], true)
                editor.commit()
            } else {
                proceedAfterPermission()
            }
        } catch (e: Exception) {
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }

            if (allgranted) {
                proceedAfterPermission()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Location permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(this@MainActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                Toast.makeText(this, "Unable to get Permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission()
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission()
            }
        }
    }

    private fun proceedAfterPermission() {
        exportDB()
    }

    private fun exportDB() {
        val sd = Environment.getExternalStorageDirectory()
        val data = Environment.getDataDirectory()
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val currentDBPath = "/data/" + packageName + "/databases/" + MappingTable.DATABASE_NAME
        val backupDBPath = MappingTable.DATABASE_NAME
        val currentDB = File(data, currentDBPath)
        //File backupDB = new File(sd, backupDBPath);
        val backupDB = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "expansion_backup.db")
        Toast.makeText(this, backupDB.absolutePath, Toast.LENGTH_LONG).show()
        try {
            source = FileInputStream(currentDB).channel
            destination = FileOutputStream(backupDB).channel
            destination!!.transferFrom(source, 0, source!!.size())
            source.close()
            destination.close()
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error in backing up the db!\n" + e.message, Toast.LENGTH_LONG).show()
        }

    }

    private inner class syncLocations : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Loading. Please wait")
            pDialog.show()
            pDialog.setCancelable(false)
        }

        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            // Start Village
            Thread(Runnable {
                try {
                    locationDataSync!!.getVillageDatafromCloud()
                    Log.d("Tremap", "GETTING Village Data in a thread")
                } catch (e: Exception) {
                    Log.d("Tremap", "ERROR GETTING Village Data")
                    Log.d("Tremap", e.message)
                }
            }).start()

            // Start Parish
            Thread(Runnable {
                try {
                    locationDataSync!!.getParishDatafromCloud()
                    Log.d("Tremap", "GETTING Parish Data in a thread")
                } catch (e: Exception) {
                    Log.d("Tremap", "ERROR GETTING Parish Data")
                    Log.d("Tremap", e.message)
                }
            }).start()
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("locations")
                    val countyLocationTable = CountyLocationTable(baseContext)
                    for (x in 0 until recs.length()) {
                        countyLocationTable.fromJson(recs.getJSONObject(x))
                    }
                } catch (e: JSONException) {
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {
            session.flagSynced(true)
            pDialog.dismiss()
        } // onPostExecute() end
    }


    private inner class syncKeWards : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Loading Wards. Please wait")
            pDialog.show()
            pDialog.setCancelable(false)
        }

        override fun doInBackground(vararg strings: String): String? {
            var stream: String? = null
            val urlString = strings[0]
            val hh = ApiClient()
            stream = hh.GetHTTPData(urlString)
            if (stream != null) {
                try {
                    val reader = JSONObject(stream)
                    val recs = reader.getJSONArray("counties")
                    val subCountyTable = SubCountyTable(context)
                    for (x in 0 until recs.length()) {
                        // I have the county Details, lets extract all subcounties
                        val county = recs.getJSONObject(x)
                        val subCounties = county.getJSONArray("subcounties")
                        for (a in 0 until subCounties.length()) {
                            // I have the subcounty, create the subcounty
                            val subCounty = subCounties.getJSONObject(a)
                            subCountyTable.fromJson(subCounty)
                            //also get the wards
                            val wards = subCounty.getJSONArray("wards")
                            for (b in 0 until wards.length()) {
                                val ward = wards.getJSONObject(b)
                                //create a ward
                                WardTable(context).fromJson(ward)
                            }
                        }
                    }
                } catch (e: JSONException) {
                    Log.d("TREMAP", "KE County Sync ERROR " + e.message)
                }

            }
            return stream
        }

        override fun onPostExecute(stream: String) {
            session.flagSynced(true)
            pDialog.dismiss()
        } // onPostExecute() end
    }

    companion object {

        // index to identify current nav menu item
        var navItemIndex = 0

        // tags used to attach the fragments
        val TAG_HOME = "home"
        val TAG_RECRUITMENTS = "recruitments"
        val TAG_REGISTRATIONS = "registrations"
        val TAG_EXAMS = "exams"
        val TAG_INTERVIEWS = "interviews"
        val TAG_MAPPINGS = "mappings"
        val TAG_MAPPING = "mapping"
        val TAG_COUNTY = "county"
        val TAG_COUNTIES = "counties"
        val TAG_SUBCOUNTIES = "subcounties"
        val TAG_SUBCOUNTY = "subcounty"
        val TAG_COMMUNITY_UNIT = "communityunit"
        val TAG_COMMUNITY_UNITS = "communityunits"
        val TAG_VILLAGE = "village"
        val TAG_VILLAGES = "villages"
        val TAG_LINK_FACILITY = "linkFacility"
        val TAG_NEW_LINK_FACILITY = "newLinkFacilities"
        val TAG_LINK_FACILITIES = "linkFacilities"
        val TAG_MAPPING_VIEW = "mappingView"
        val TAG_MAP_VIEW = "mapView"
        val TAG_NEW_COMMUNITY_UNIT = "newCommunityUnit"
        val TAG_NEW_EXAM = "newExam"
        val TAG_NEW_INTERVIEW = "newInterview"
        val TAG_NEW_MAPPING = "newMapping"
        val TAG_NEW_RECRUITMENT = "newRecruitment"
        val TAG_NEW_REGISTRATION = "newRegistration"
        val TAG_NEW_SUB_COUNTY = "newSubCounty"
        val TAG_NEW_VILLAGE = "newVillage"
        val TAG_PARTNERS = "partners"
        val TAG_SUBCOUNTY_VIEW = "subCountyView"
        val REGISTRATION_VIEW = "registrationView"

        var CURRENT_TAG = TAG_HOME

        var backFragment: Fragment? = null

        private val PERMISSION_CALLBACK_CONSTANT = 101
        private val REQUEST_PERMISSION_SETTING = 102
    }
}
