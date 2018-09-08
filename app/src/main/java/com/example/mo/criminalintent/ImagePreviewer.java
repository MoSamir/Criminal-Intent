package com.example.mo.criminalintent;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImagePreviewer extends DialogFragment implements View.OnClickListener {
    private static final String ARG_PARAM_IMAGE = "com.mo.criminalIntent.ImagePreviewer.IMAGE_URI";
    private static final String ARG_PARAM_NAME = "com.mo.criminalIntent.ImagePreviewer.CRIMINAL_NAME";
    private static final String ARG_PARAM_ACTIONS = "com.mo.criminalIntent.ImagePreviewer.ACTIONS_STATE";

    public static final int VIEW_DEMO_IMAGE_REQ = 300;

    File mImageFile ;
    String mCriminalName ;
    ImageView imageView ;
    TextView textView ;
    Boolean isActionBlocked = false ;
    Button retakeImage , acceptImage ;
    LinearLayout actionsLayout ;


    public ImagePreviewer() {}
    public static ImagePreviewer newInstance(File imagePath , String criminalName , Boolean isBlocked) {
        ImagePreviewer fragment = new ImagePreviewer();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_IMAGE , imagePath);
        args.putString(ARG_PARAM_NAME , criminalName);
        args.putBoolean(ARG_PARAM_ACTIONS , isBlocked);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(getArguments() != null){
            if(getArguments().containsKey(ARG_PARAM_IMAGE))
                mImageFile = (File)getArguments().getSerializable(ARG_PARAM_IMAGE);
            if(getArguments().containsKey(ARG_PARAM_NAME))
                mCriminalName = getArguments().getString(ARG_PARAM_NAME);
            if(getArguments().containsKey(ARG_PARAM_ACTIONS))
                isActionBlocked = getArguments().getBoolean(ARG_PARAM_ACTIONS);
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_image_priviewer , null , false);
        imageView = view.findViewById(R.id.dialog_criminal_image_IV);
        textView = view.findViewById(R.id.dialog_criminal_name_TV);
        acceptImage = view.findViewById(R.id.dialog_acceptImage_btn);
        retakeImage = view.findViewById(R.id.dialog_retakeImage_btn);
        actionsLayout = view.findViewById(R.id.imageControllerLinear);
        initUI();
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(true)
                .create();
    }

    private void initUI() {


        if(!isActionBlocked)
            actionsLayout.setVisibility(View.GONE);
        else
            actionsLayout.setVisibility(View.VISIBLE);


        if(mCriminalName!= null)
            textView.setText(mCriminalName);
        else
            textView.setText("");
        try{
            Picasso.get().load(mImageFile).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(R.drawable.ic_man_user).into(imageView);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_man_user).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(R.drawable.ic_man_user).into(imageView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getDialog().findViewById(R.id.viewImagealertDialog).setOnClickListener(this);
        this.getDialog().findViewById(R.id.dialog_acceptImage_btn).setOnClickListener(this);
        this.getDialog().findViewById(R.id.dialog_retakeImage_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.viewImagealertDialog){
            Log.e("TAG5", "Full Image Requested");
            if(getTargetFragment() != null){
                getTargetFragment().onActivityResult(VIEW_DEMO_IMAGE_REQ , STATIC_VALUES.ACTIVITYRESULT_FULLIMAGE , null);
            }
        }
        if(v.getId() == R.id.dialog_acceptImage_btn){
            if(getTargetFragment() != null){
                getTargetFragment().onActivityResult(VIEW_DEMO_IMAGE_REQ , Activity.RESULT_OK , null);
                dismiss();
            }
        }
        if(v.getId() == R.id.dialog_retakeImage_btn){
            Log.e("TAG5", "Retake Image Requested");
            if(getTargetFragment() != null){
                getTargetFragment().onActivityResult(VIEW_DEMO_IMAGE_REQ , STATIC_VALUES.ACTIVITYRESULT_RECAPTURE , new Intent());
                dismiss();
            }
        }
    }
}
