package com.example.mo.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.*;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;

import java.util.Locale;

abstract public class SingleFragment_ContainerActivity extends AppCompatActivity {
    abstract Fragment createFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }

        setContentView(R.layout.activity_signle_fragment__container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentVisableFragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if(currentVisableFragment == null)
            currentVisableFragment = createFragment();
        if (!currentVisableFragment.isAdded()){
            fragmentManager.beginTransaction().add(R.id.fragmentContainer,currentVisableFragment).commit();
        }
    }



    public static void UpdateLanguage(Context ctx , int languageCode){

        Resources resource = ctx.getResources();
        DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        Configuration config = resource.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(languageCode == STATIC_VALUES.AR_CODE){
                config.setLocale(new Locale("ar"));
            } else if(languageCode == STATIC_VALUES.EN_CODE){
                config.setLocale(new Locale("en"));
            }
        }
        resource.updateConfiguration(config , displayMetrics);
        RestartAcitivy(ctx);

    }

    private static void RestartAcitivy(Context ctx) {

        Intent intent = new Intent(ctx , CrimeListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
    }


}
