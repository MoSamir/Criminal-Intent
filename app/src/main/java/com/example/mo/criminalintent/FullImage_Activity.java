package com.example.mo.criminalintent;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;

import java.io.File;

public class FullImage_Activity extends SingleFragment_ContainerActivity {


    @Override
    Fragment createFragment() {

        Full_ImageFragment fullImagePreview = null ;

        if(getIntent().getExtras().containsKey(Full_ImageFragment.IMAGE_KEY)){
            fullImagePreview = Full_ImageFragment.newInstance((File)getIntent().getExtras().getSerializable(Full_ImageFragment.IMAGE_KEY));
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fullImagePreview.setEnterTransition(new Fade(Fade.IN));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fullImagePreview.setExitTransition(new Slide(Gravity.RIGHT));
        }


        return  fullImagePreview ;
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.slide_out_right);
    }
}
