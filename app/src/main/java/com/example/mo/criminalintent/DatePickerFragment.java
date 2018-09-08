package com.example.mo.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private Date passedDate ;
    private DatePicker mDatePicker ;
    static final String DATE_ARG_KEY = "date";
    public static final int datePickerFragmentToSingleCrimeFragment_REQ = 101;
    public static final String EXTRA_DATE = "com.mo.android.criminalintent.date";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


         View calenderView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.util_date_picker , null);

        mDatePicker = calenderView.findViewById(R.id.dialog_date_picker);


        if(getArguments()!= null){
            if(getArguments().containsKey(DATE_ARG_KEY)){
                passedDate = (Date) getArguments().getSerializable(DATE_ARG_KEY);
            }
        }
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.crime_solved)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date selectedDate = new GregorianCalendar(mDatePicker.getYear(), mDatePicker.getMonth() , mDatePicker.getDayOfMonth()).getTime();
                        sendDataBack(Activity.RESULT_OK , selectedDate);
                    }
                })
                .setView(calenderView)
                .create();
    }


    public static DatePickerFragment newInstance(Date date){
        DatePickerFragment newInstance = new DatePickerFragment();
        Bundle fragmentArguments = new Bundle();
        fragmentArguments.putSerializable(DATE_ARG_KEY , date);
        newInstance.setArguments(fragmentArguments);
        return  newInstance ;
    }



    private void sendDataBack(int resultCode , Date selectedDate){
        if(getTargetFragment() == null){
            return;
        }
        Intent dateIntent = new Intent();
        dateIntent.putExtra(EXTRA_DATE , selectedDate);
        getTargetFragment().onActivityResult(getTargetRequestCode() , resultCode , dateIntent);
    }
}
