package com.expansion.lg.kimaru.expansion.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DateFormat;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;

import com.expansion.lg.kimaru.expansion.R;


/**
 * Created by kimaru on 3/9/17.
 */

public class AlertDialogManager {

    /**
     * Fn to display simple alert Dialog
     * @param context -- Application Context
     * @param title -- Alter Dialog Title
     * @param message - Alert Msg
     * @param status - success/failure (Used to set teh correct Icon)
     *               - pass null if no Icon is needed
     */

    public void showAlertDialog(Context context, String title, String message, Boolean status,
                                @Nullable String positiveButton, @Nullable String negativeButton){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        //set the title, I almost wrote 'Tithe', lakini  :D
        alertDialog.setTitle(title);

        //set the message
        alertDialog.setMessage(message);

        //set the icon only if status is not null
        if(status != null) {
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.ic_error_black_24dp : R.drawable.ic_done_black_24dp);
        }
        alertDialog.setCanceledOnTouchOutside(true);

        if(positiveButton != null){
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveButton, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        if(negativeButton == null){
            negativeButton = "Cancel";
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeButton, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }
}
