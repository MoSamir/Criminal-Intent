package com.example.mo.criminalintent;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

/**
 * Created by MO on 08/06/2018.
 */

public class SingleCrimeActivity extends SingleFragment_ContainerActivity {
    public static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";

    @Override
    Fragment createFragment() {
        UUID selectedCrimeId = null ;
        if((UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID) != null)
        selectedCrimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return SingleCrimeFragment.newInstance(selectedCrimeId);
    }


}
