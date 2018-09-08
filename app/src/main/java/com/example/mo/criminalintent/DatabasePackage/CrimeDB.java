package com.example.mo.criminalintent.DatabasePackage;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.mo.criminalintent.CrimeModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.UUID;

@Entity
public class CrimeDB implements Serializable{


    public CrimeDB(UUID uid   , String mCrimeName , String mCrimeDesc ,boolean isSolved , Date mCrimeDate , String mCriminalName , String mCriminalImage){
        this.uid = uid ; this.mCrimeName = mCrimeName ; this.mCrimeDesc = mCrimeName ;
        this.mCrimeDate = mCrimeDate ; this.isSolved = isSolved ;
        this.mCriminalName = mCriminalName ;
        this.mCriminalImage = mCriminalImage ;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getCrimeName() {
        return mCrimeName;
    }

    public void setmCrimeDesc(String crimeDesc) {
        mCrimeDesc = crimeDesc;
    }


    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public Date getCrimeDate() {
        return mCrimeDate;
    }

    public void setCrimeDate(Date crimeDate) {
        mCrimeDate = crimeDate;
    }

    @TypeConverters(Converters.class)
    @PrimaryKey @NonNull
    private UUID uid;

    @ColumnInfo(name = "crime_name")
    private String mCrimeName ;

    public String getCriminalImage() {
        return mCriminalImage;
    }

    public void setCriminalImage(String criminalImage) {
        mCriminalImage = criminalImage;
    }

    @ColumnInfo(name = "criminal_image")
    private String mCriminalImage ;



    public String getCriminalName() {
        return mCriminalName;
    }

    public void setCriminalName(String criminalName) {
        mCriminalName = criminalName;
    }

    @ColumnInfo(name = "criminal_name")
    private String mCriminalName ;


    public String getCrimeDesc() {
        return mCrimeDesc;
    }

    @ColumnInfo(name = "crime_description")
    private String mCrimeDesc ;

    @ColumnInfo(name = "isSolved")
    private boolean isSolved ;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "crime_date")
    private Date mCrimeDate;

    public CrimeModel getCrimeModel(){
        return new CrimeModel(uid , mCrimeName, mCrimeDesc , isSolved , mCrimeDate , mCriminalName , mCriminalImage);
    }
}



