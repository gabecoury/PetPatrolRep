package edu.wmich.petpatrol;

/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* This opens a DatePicker Dialog that
* allows the user to select a date.
*************************************
*/


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE =
            "edu.wmich.petpatrol.myapplication.date";

    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Calendar cal) { /* Creates DatePicker Fragment */
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, cal);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {   /* Creates DatePicker Dialog */
        Calendar date = (Calendar) getArguments().getSerializable(ARG_DATE);

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, month, day, null);
        mDatePicker.setMinDate(Calendar.getInstance().getTimeInMillis());

        return new AlertDialog.Builder(getActivity())
                /* When the Date is picked and submitted, return the date to the AddReport fragment */
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                Calendar cal = Calendar.getInstance();
                                cal.set(year, month, day);
                                sendResult(Activity.RESULT_OK, cal);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Calendar cal) { /* Returns date to AddReport Fragment */
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, cal);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}