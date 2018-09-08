package com.example.mo.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.UUID;

import static com.example.mo.criminalintent.SingleCrimeActivity.EXTRA_CRIME_ID;

public class CrimePagerActivity extends AppCompatActivity {


    ViewPager crimesViewPager ;
    private List<CrimeModel> mCrimesList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        crimesViewPager = findViewById(R.id.crimes_pager);
        mCrimesList = CrimeLab.getInstance(getApplicationContext()).getAllCrimes();
        crimesViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return  SingleCrimeFragment.newInstance(mCrimesList.get(position).getId());
            }
            @Override
            public int getCount() {
                return mCrimesList.size();
            }
        });

        InitViewPager();
    }

    private void InitViewPager() {
        UUID selectedCrimeId = null ;
        if((UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID) != null)
            selectedCrimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        for(int i = 0 ; i < mCrimesList.size() ; i++)
            if(mCrimesList.get(i).getId().equals(selectedCrimeId)){
            Log.e("TAG" , "" + mCrimesList.get(i).getId() + " Match  "  + getIntent().getSerializableExtra(EXTRA_CRIME_ID) + "   " + i);
            crimesViewPager.setCurrentItem(i);
            break;
            }
    }
}
