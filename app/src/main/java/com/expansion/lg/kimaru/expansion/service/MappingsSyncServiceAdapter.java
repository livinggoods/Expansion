package com.expansion.lg.kimaru.expansion.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.sync.HttpClient;

/**
 * Created by kimaru on 4/11/17.
 */

public class MappingsSyncServiceAdapter extends AbstractThreadedSyncAdapter {
    public  static final int SYNC_INTERVAL = 15;
    public  static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    public static final int NOTIFICATION_ID = 3004;

    public MappingsSyncServiceAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync (Account account, Bundle extras, String authority,
                               ContentProviderClient provider, SyncResult syncResult){
        Log.i("MyServiceSyncAdapter", "onPerformSync");
        //TODO get some data from the internet, api calls, etc.
        //TODO save the data to database, sqlite, etc

        // syncRecruitments
        HttpClient client = new HttpClient(getContext());
//        client.syncRecruitments();
//        client.syncRegistrations();
//        client.syncExams();
//        client.syncInterviews();
//        client.syncCommunityUnits();
//        client.syncReferrals();
//        client.syncLinkFacilities();

    }

    /**
     * Send the notification message to the status bar
     */
    private void notifyDataDownloaded() {
        Context context = getContext();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.support.v7.appcompat.R.drawable.notification_template_icon_bg)
                        .setContentTitle("Downloaded Recruitments")
                        .setContentText("Sync has happened");

        // Opening the app when the user clicks on the notification.
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build()); // MOVIE_NOTIFICATION_ID allows you to update the notification later on.
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }


    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Log.i("MyServiceSyncAdapter", "syncImmediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_mapping), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // get the loggedin user Account
        SessionManagement sessionManagement = new SessionManagement(context);

        AccountManager accountManager = (AccountManager) context.getSystemService(
                Context.ACCOUNT_SERVICE); // Get an instance of the Android account manager
        Account newAccount = new Account(sessionManagement.getUserDetails()
                .get(SessionManagement.KEY_EMAIL), context.getString(R.string.sync_account_type)); // Create the account type and default account
        // If the password doesn't exist, the account doesn't exist
        if (accountManager.getPassword(newAccount) == null) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                Log.e("MyServiceSyncAdapter", "getSyncAccount Failed to create new account.");
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        Log.i("MyServiceSyncAdapter", "onAccountCreated");
        MappingsSyncServiceAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        Log.d("MyServiceSyncAdapter", "initializeSyncAdapter");
        getSyncAccount(context);
    }

}
