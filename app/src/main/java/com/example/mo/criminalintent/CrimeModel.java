package com.example.mo.criminalintent;

import android.arch.persistence.room.TypeConverter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import com.example.mo.criminalintent.DatabasePackage.CrimeDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

/**
 * Created by MO on 08/06/2018.
 */

public class CrimeModel implements Serializable{

    Context androidContext ;


    public UUID getId() {
        return mId;
    }

    public String getCrimeName() {
        return mCrimeName;
    }

    public void setCrimeName(String crimeName) {
        mCrimeName = crimeName;
    }

    public Date getCrimeDate() {
        if(mCrimeDate == null)
            return mCrimeDate = new Date();
        else
            return mCrimeDate;
    }

    public void setCrimeDate(Date crimeDate) {
        mCrimeDate = crimeDate;
    }

    public Boolean getSolved() {
        return mIsSolved;
    }

    public void setSolved(Boolean solved) {
        mIsSolved = solved;
    }


    public void setCrimeDesc(String desc){mCrimeDesc = desc ;}
    public String getCrimeDesc(){return mCrimeDesc;}

    UUID mId ;
    String mCrimeName;
    String mCrimeDesc;
    String mCriminalName;
    String mCriminalImagePath ;

    public String getCrimeImagePath() {
        return  mCriminalImagePath ;
    }
    Date mCrimeDate ;
    Boolean mIsSolved ;




    public CrimeModel(String mCrimeName , boolean isSolved){
        mId = UUID.randomUUID();
        this.mCrimeDate = new Date() ;
        this.mCrimeName = mCrimeName ;
        this.mIsSolved = isSolved ;

    }





    public CrimeModel(UUID mCrimeId , String mCrimeName , boolean isSolved , Date mCrimeDate){
        this.mId = mCrimeId ; this.mCrimeName = mCrimeName ;
        this.mCrimeDate = mCrimeDate ; this.mIsSolved = isSolved ;

    }

    public void setCriminalName(String criminalName) {
        mCriminalName = criminalName;
    }

    public String getCriminalName(){
        return mCriminalName;
    }


    public CrimeModel(UUID mCrimeId , String mCrimeName , String mCrimeDesc , boolean isSolved , Date mCrimeDate ,String mCrinimalName , String imagePath){
        this.mId = mCrimeId ; this.mCrimeName = mCrimeName ;
        this.mCrimeDate = mCrimeDate ; this.mIsSolved = isSolved ;
        this.mCrimeDesc = mCrimeDesc ;
        this.mCriminalName = mCrinimalName ;
        this.mCriminalImagePath = imagePath ;

    }


    public CrimeModel(String mCrimeNameString ){
        mId = UUID.randomUUID();
        this.mCrimeDate = new Date();
        this.mCrimeName = mCrimeNameString;
        this.mIsSolved = false ;

    }

    public CrimeModel(String mCrimeName , String mCrimeDesc){
        mId = UUID.randomUUID();
        this.mCrimeDate = new Date();
        this.mCrimeDesc = mCrimeDesc ;
        this.mCrimeName = mCrimeName;
        this.mIsSolved = false ;

    }

    public CrimeDB getDBModel(){
        return new CrimeDB(mId , mCrimeName ,mCrimeDesc ,mIsSolved , mCrimeDate , mCriminalName , mCriminalImagePath);
    }


    public void setCriminalImagePath(String criminalImagePath) {
        mCriminalImagePath = criminalImagePath;
    }
}



