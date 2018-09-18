package com.expansion.lg.kimaru.expansion.service

import android.accounts.Account
import android.accounts.AccountManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SyncRequest
import android.content.SyncResult
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.sync.HttpClient

/**
 * Created by kimaru on 4/11/17.
 */

class RecruitmentsSyncServiceAdapter(context: Context, autoInitialize: Boolean) : AbstractThreadedSyncAdapter(context, autoInitialize) {

    override fun onPerformSync(account: Account, extras: Bundle, authority: String,
                               provider: ContentProviderClient, syncResult: SyncResult) {
        Log.i("MyServiceSyncAdapter", "onPerformSync")
        //TODO get some data from the internet, api calls, etc.
        //TODO save the data to database, sqlite, etc

        // syncRecruitments
        val client = HttpClient(context)
        client.syncRecruitments()
        client.syncRegistrations()
        client.syncExams()
        client.syncInterviews()
        client.syncCommunityUnits()
        client.syncReferrals()
        client.syncMapping()
        client.syncLinkFacilities()
        client.syncVillages()
        client.syncParishes()


        // Add parishes, partners. partnercu, mapping
        client.syncParishes()
        client.syncPartners()
        client.syncPartnersCommunityUnits()
        client.syncMapping()


    }

    /**
     * Send the notification message to the status bar
     */
    private fun notifyDataDownloaded() {
        val context = context
        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(android.support.v7.appcompat.R.drawable.notification_template_icon_bg)
                .setContentTitle("Downloaded Recruitments")
                .setContentText("Sync has happened")

        // Opening the app when the user clicks on the notification.
        val resultIntent = Intent(context, MainActivity::class.java)

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(resultPendingIntent)

        val mNotificationManager = getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build()) // MOVIE_NOTIFICATION_ID allows you to update the notification later on.
    }

    companion object {
        val SYNC_INTERVAL = 15
        val SYNC_FLEXTIME = SYNC_INTERVAL / 3
        val NOTIFICATION_ID = 3004

        /**
         * Helper method to schedule the sync adapter periodic execution
         */
        fun configurePeriodicSync(context: Context, syncInterval: Int, flexTime: Int) {
            val account = getSyncAccount(context)
            val authority = context.getString(R.string.content_authority)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // we can enable inexact timers in our periodic sync
                val request = SyncRequest.Builder()
                        .syncPeriodic(syncInterval.toLong(), flexTime.toLong())
                        .setSyncAdapter(account, authority)
                        .setExtras(Bundle()).build()
                ContentResolver.requestSync(request)
            } else {
                ContentResolver.addPeriodicSync(account, authority, Bundle(), syncInterval.toLong())
            }
        }


        /**
         * Helper method to have the sync adapter sync immediately
         * @param context The context used to access the account service
         */
        fun syncImmediately(context: Context) {
            Log.i("MyServiceSyncAdapter", "syncImmediately")
            val bundle = Bundle()
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
            ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle)
        }

        /**
         * Helper method to get the fake account to be used with SyncAdapter, or make a new one
         * if the fake account doesn't exist yet.  If we make a new account, we call the
         * onAccountCreated method so we can initialize things.
         *
         * @param context The context used to access the account service
         * @return a fake account.
         */
        fun getSyncAccount(context: Context): Account? {
            // get the loggedin user Account
            val sessionManagement = SessionManagement(context)

            val accountManager = context.getSystemService(
                    Context.ACCOUNT_SERVICE) as AccountManager // Get an instance of the Android account manager
            val newAccount = Account(sessionManagement.userDetails[SessionManagement.KEY_EMAIL], context.getString(R.string.sync_account_type)) // Create the account type and default account
            // If the password doesn't exist, the account doesn't exist
            if (accountManager.getPassword(newAccount) == null) {
                if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                    Log.e("MyServiceSyncAdapter", "getSyncAccount Failed to create new account.")
                    return null
                }
                onAccountCreated(newAccount, context)
            }
            return newAccount
        }

        private fun onAccountCreated(newAccount: Account, context: Context) {
            Log.i("MyServiceSyncAdapter", "onAccountCreated")
            RecruitmentsSyncServiceAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME)
            ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true)
            syncImmediately(context)
        }

        fun initializeSyncAdapter(context: Context) {
            Log.d("MyServiceSyncAdapter", "initializeSyncAdapter")
            getSyncAccount(context)
        }
    }

}
