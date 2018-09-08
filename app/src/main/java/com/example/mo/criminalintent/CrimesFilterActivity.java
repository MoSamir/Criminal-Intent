package com.example.mo.criminalintent;

import android.support.v4.app.Fragment;

import static com.example.mo.criminalintent.CrimesFilterdFragment.PAGE_TYPE_KEY;

public class CrimesFilterActivity extends SingleFragment_ContainerActivity {
    @Override
    Fragment createFragment() {

        int PageType = STATIC_VALUES.CLOSED_CRIMES_KEY;

        if(getIntent().getExtras().containsKey(PAGE_TYPE_KEY))
            PageType = getIntent().getExtras().getInt(PAGE_TYPE_KEY);

        return CrimesFilterdFragment.newInstance(PageType);
    }
}
