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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Date;
import java.util.GregorianCalendar;

public class AddCrimeFragment extends DialogFragment implements DialogInterface.OnClickListener{


    public static final String EXTRA_CNAME = "com.example.mo.criminalintent.crimeNameStr";
    public static final String EXTRA_CDESC = "com.example.mo.criminalintent.crimeDescStr";
    public static int AddCrimeFragmentToCrimeList_REQ = 102;
    TextView crimeName , crimeDesc;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.util_add_crime , null );
          crimeName = v.findViewById(R.id.crime_name_ET);
          crimeDesc = v.findViewById(R.id.crimeDesc_ET);
        final Dialog addCrimeDialog =  new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setPositiveButton(getString(R.string.save), this)
                .setNegativeButton(android.R.string.cancel, this)
                .setView(v)
                .create();
        return  addCrimeDialog;
    }

    private void sendDataBack(int resultCode , String title , String Desc){
        if(getTargetFragment() == null){
            return;
        }

        Intent dateIntent = new Intent();
        dateIntent.putExtra(EXTRA_CNAME , title);
        dateIntent.putExtra(EXTRA_CDESC , Desc);

        getTargetFragment().onActivityResult(getTargetRequestCode() , resultCode , dateIntent);
    }

    public static AddCrimeFragment newInstance(){
        AddCrimeFragment newInstance = new AddCrimeFragment();
        return  newInstance ;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
         if(which == DialogInterface.BUTTON_NEGATIVE){
            sendDataBack(Activity.RESULT_CANCELED , null , null);
            dialog.dismiss();
        } else {
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        final AlertDialog dialog = (AlertDialog) getDialog();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(crimeName.getText().toString().isEmpty()){
                        crimeName.setError(getString(R.string.EmptyName));
                    } else{
                        sendDataBack(Activity.RESULT_OK , crimeName.getText().toString() , crimeDesc.getText().toString());
                        dismiss();
                    }
            }
        });



    }
}
