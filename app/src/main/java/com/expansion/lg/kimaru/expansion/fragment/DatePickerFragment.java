package com.expansion.lg.kimaru.expansion.fragment;

/**
 * Created by kimaru on 3/14/17.
 */


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import com.expansion.lg.kimaru.expansion.R;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    int DateEditText;

    public DatePickerFragment(){
        //default empty constructor as per the docs..
    }

    public static DatePickerFragment newInstance(int DateEditText) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("DateEditText", DateEditText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (getArguments() != null) {
            DateEditText = getArguments().getInt("DateEditText");
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        TextView tv1= (TextView) getActivity().findViewById(DateEditText);
        tv1.setText(year+"/"+ (month + 1) +"/"+day);
    }
    public void populateSetDate(int year, int month, int day) {
        TextView tv1= (TextView) getActivity().findViewById(DateEditText);
        tv1.setText(month+"/"+day+"/"+year);
    }
}
