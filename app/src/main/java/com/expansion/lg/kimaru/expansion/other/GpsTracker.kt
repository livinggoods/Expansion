package com.expansion.lg.kimaru.expansion.other

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog

import com.expansion.lg.kimaru.expansion.activity.AlertDialogManager
import com.expansion.lg.kimaru.expansion.activity.MainActivity

/**
 * Created by kimaru on 3/30/17.
 */

class GpsTracker(val mContext: Context, val mActivity: Activity) : Service(), LocationListener {

    //flag for GPS Status
    internal var isGPSEnabled = false

    //flag for net status
    internal var isNetworkEnabled = false

    internal var canGetLocation = false

    internal var location: Location? = null //location
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()

    protected var locationManager: LocationManager? = null


    //show alerts
    internal var alert = AlertDialogManager()

    init {
        getLocation()
    }

    fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            //get GPS Status
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            //get network status
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                //no network provider enabled
            } else {
                this.canGetLocation = true

                //check for permissions before getting the location
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //some users need to know what is happening, so let us show the rationale
                    // Should we show an explanation?

                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.


                        val builder = AlertDialog.Builder(mContext)
                        builder.setTitle("Needs GPS Permission")
                        builder.setMessage("In order to work properly, this app needs GPS permission")
                        builder.setPositiveButton("Grant") { dialog, which ->
                            dialog.cancel()
                            ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_CALLBACK_CONSTANT)
                        }
                        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                        builder.show()

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(mActivity,
                                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission_group.LOCATION),
                                PERMISSION_CALLBACK_CONSTANT)
                    }

                    //We have the permissions now
                    //get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BETWEEN_UPDATE,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)

                        if (locationManager != null) {
                            location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            if (location != null) {
                                latitude = location!!.latitude
                                latitude = location!!.latitude
                            }
                        }
                    }
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager!!.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BETWEEN_UPDATE,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                            if (locationManager != null) {
                                location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                if (location != null) {
                                    latitude = location!!.latitude
                                    longitude = location!!.longitude
                                }
                            }
                        }
                    }


                }

            }

        } catch (e: Exception) {
        }

        return location
    }

    /**
     * Stop using the GPS listener
     * When you call this, you wil stop using the GPS in the app
     */
    fun stopUsingGPS() {
        if (locationManager != null) {
            try {
                locationManager!!.removeUpdates(this@GpsTracker)
            } catch (se: SecurityException) {

            }

        }
    }

    /**
     * Function to get latitude
     */
    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.latitude
        }

        // return latitude
        return latitude
    }

    /**
     * Function to get longitude
     */
    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.longitude
        }

        // return longitude
        return longitude
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    fun canGetLocation(): Boolean {
        return this.canGetLocation
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings")

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        // Showing Alert Message
        alertDialog.show()
    }

    //OVERRIDES
    override fun onLocationChanged(location: Location) {
        this.longitude = location.longitude
        this.latitude = location.latitude
    }

    override fun onProviderDisabled(provider: String) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    companion object {

        // Due to Runtime Perms, let us declare some constants for the permissions
        private val PERMISSION_CALLBACK_CONSTANT = 101
        private val REQUEST_PERMISSION_SETTING = 102


        // The minimum distance to change Updates in meters
        val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 1 // 10 meters
        // The minimum time between updates in milliseconds
        val MIN_TIME_BETWEEN_UPDATE = (1000 * 60 / 2).toLong() // 30 minutes
    }

}
