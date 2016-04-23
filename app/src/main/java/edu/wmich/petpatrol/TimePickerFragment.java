package edu.wmich.petpatrol;

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

/**
 * Created by Andrew on 4/17/2016.
 */

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME =
            "edu.wmich.petpatrol.myapplication.time";

    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Calendar cal) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, cal);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar time = (Calendar) getArguments().getSerializable(ARG_TIME);

        int hour = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
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
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, cal);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}