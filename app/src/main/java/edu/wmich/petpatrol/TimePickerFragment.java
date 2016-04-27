package edu.wmich.petpatrol;

/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* This opens a TimePicker Dialog that
* allows the user to select a time.
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
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME =
            "edu.wmich.petpatrol.time";

    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Calendar cal) {
            /* Creates a new TimePicker Fragment with the current/last set time passed in as an argument */
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, cal);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            /* Stores the argument as time */
        Calendar time = (Calendar) getArguments().getSerializable(ARG_TIME);

        int hour = time.get(Calendar.HOUR_OF_DAY);  /* retrieves hours from time */
        int minute = time.get(Calendar.MINUTE); /* Retrieves minutes from time */

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);   /* Inflates the timepicker layout */

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setCurrentHour(hour);   /* Sets the timepicker hour hand */
        mTimePicker.setCurrentMinute(minute);   /* Sets the timepicker minute hand */
            /* The above deprecated functions were used in order to maintain backwards
            compatibility with the minimum API level */

        return new AlertDialog.Builder(getActivity())   /* Gets the time the user selected and sends it back to the calling fragment */
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hour = mTimePicker.getCurrentHour();
                                int minute = mTimePicker.getCurrentMinute();
                                Calendar cTime = Calendar.getInstance();
                                cTime.set(Calendar.HOUR_OF_DAY, hour);
                                cTime.set(Calendar.MINUTE, minute);
                                Date dTime = cTime.getTime();
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(dTime);
                                sendResult(Activity.RESULT_OK, cal);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Calendar cal) {
        /* Sends the time selected back to the function that requested the timepicker */
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, cal);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}