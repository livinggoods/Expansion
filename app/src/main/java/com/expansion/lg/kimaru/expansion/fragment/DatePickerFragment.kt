package com.expansion.lg.kimaru.expansion.fragment

/**
 * Created by kimaru on 3/14/17.
 */


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import java.util.Calendar
import com.expansion.lg.kimaru.expansion.R

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    internal var DateEditText: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        if (arguments != null) {
            DateEditText = arguments!!.getInt("DateEditText")
        }

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity!!, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val tv1 = activity!!.findViewById<View>(DateEditText) as TextView
        // override the default set date to Jan 01, We are only interested in the year
        tv1.text = year.toString() + "/" + month + "/" + day
    }

    fun populateSetDate(year: Int, month: Int, day: Int) {
        val tv1 = activity!!.findViewById<View>(DateEditText) as TextView
        tv1.text = month.toString() + "/" + day + "/" + year
    }

    companion object {

        fun newInstance(DateEditText: Int): DatePickerFragment {
            val fragment = DatePickerFragment()
            val args = Bundle()
            args.putInt("DateEditText", DateEditText)
            fragment.arguments = args
            return fragment
        }
    }
}//default empty constructor as per the docs..
