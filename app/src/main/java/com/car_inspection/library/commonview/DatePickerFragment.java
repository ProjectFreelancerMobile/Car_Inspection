package com.car_inspection.library.commonview;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private boolean isMinDate = false;

    public void setMinDate(boolean isMinDate) {
        this.isMinDate = isMinDate;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public static DatePickerFragment newInstance(long dateTimeStam) {
        DatePickerFragment f = new DatePickerFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("dateTimeStam", dateTimeStam);
        f.setArguments(args);

        return f;
    }

    private long dateTimeStam = 0;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments().containsKey("dateTimeStam"))
            dateTimeStam = getArguments().getLong("dateTimeStam");

        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(dateTimeStam));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        if (isMinDate)
            dialog.getDatePicker().setMinDate(dateTimeStam);

        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog.getDatePicker().setMinDate(dateTimeStam);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (onDateSetListener != null)
            onDateSetListener.onDateSet(view, year, month, day);
    }
}