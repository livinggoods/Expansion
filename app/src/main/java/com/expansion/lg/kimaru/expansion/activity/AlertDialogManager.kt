package com.expansion.lg.kimaru.expansion.activity

import android.content.Context
import android.support.v7.app.AlertDialog
import com.expansion.lg.kimaru.expansion.R


/**
 * Created by kimaru on 3/9/17.
 */

class AlertDialogManager {

    /**
     * Fn to display simple alert Dialog
     * @param context -- Application Context
     * @param title -- Alter Dialog Title
     * @param message - Alert Msg
     * @param status - success/failure (Used to set teh correct Icon)
     * - pass null if no Icon is needed
     */

    fun showAlertDialog(context: Context, title: String, message: String, status: Boolean?,
                        positiveButton: String?, negativeButton: String?) {
        var negativeButton = negativeButton
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(title)

        //set the message
        alertDialog.setMessage(message)

        //set the icon only if status is not null
        if (status != null) {
            // Setting alert dialog icon
            alertDialog.setIcon(if (status) R.drawable.ic_error_black_24dp else R.drawable.ic_done_black_24dp)
        }
        alertDialog.setCanceledOnTouchOutside(true)

        if (positiveButton != null) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveButton) { dialog, which -> }
        }
        if (negativeButton == null) {
            negativeButton = "Cancel"
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeButton) { dialog, which -> }


        alertDialog.show()
    }
}
