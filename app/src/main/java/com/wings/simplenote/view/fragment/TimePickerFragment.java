package com.wings.simplenote.view.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.wings.simplenote.listener.OnTimePickListener;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePickerFragment";
    private OnTimePickListener mTimePickListener;

    public void setOnTimePickListener(OnTimePickListener mTimePickListener) {
        this.mTimePickListener = mTimePickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
//        this.setCancelable(false);
        return timePickerDialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mTimePickListener != null) {
            mTimePickListener.onTimePickCancel();
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Log.i(TAG, "onTimeSet: " + hourOfDay + "::" + minute);
        if (mTimePickListener != null) {
            mTimePickListener.onTimePick(hourOfDay, minute);
        }
    }


}
