package com.example.mo.criminalintent;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Full_ImageFragment extends Fragment {
    public Full_ImageFragment(){}
    public static final String IMAGE_KEY = "FullImageKey";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.full_image_fragment,container,false);

      ImageView full_Image = v.findViewById(R.id.fullImageView);
        if(getArguments()!= null){
            if(getArguments().containsKey(IMAGE_KEY)){
                File ImageFile = (File)getArguments().getSerializable(IMAGE_KEY);
                Picasso.get().load(ImageFile).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(full_Image);
            }
        }
        return v ;
    }

    public static Full_ImageFragment newInstance(File imagePath) {
        Bundle args = new Bundle();
        Full_ImageFragment fragment = new Full_ImageFragment();
        args.putSerializable(IMAGE_KEY,imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
