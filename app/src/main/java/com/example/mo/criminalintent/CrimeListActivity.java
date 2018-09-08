package com.example.mo.criminalintent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

public class CrimeListActivity extends SingleFragment_ContainerActivity {

    BottomNavigationView mBottomNavigationItemView ;
    @Override
    Fragment createFragment() {
        return CrimesListFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBottomNavigationItemView = findViewById(R.id.bottomNavBar);
        mBottomNavigationItemView.setSelectedItemId(R.id.allCrimes);

        mBottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.solvedCrime) {
                    BottomBarNavigationController(CrimesFilterdFragment.newInstance(STATIC_VALUES.CLOSED_CRIMES_KEY));
                    return true ;
                } else if(item.getItemId() == R.id.openCrimes) {
                    BottomBarNavigationController(CrimesFilterdFragment.newInstance(STATIC_VALUES.ONGOING_CRIMES_KEY));
                    return true ;
                } else if(item.getItemId() == R.id.allCrimes){
                    BottomBarNavigationController(CrimesListFragment.newInstance());
                    return true ;
                }
                return false;
            }
        });
    }

    void BottomBarNavigationController(Fragment newFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!newFragment.isAdded()){
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,newFragment).commit();
        }
    }


}
