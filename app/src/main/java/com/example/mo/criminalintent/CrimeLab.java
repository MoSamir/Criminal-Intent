package com.example.mo.criminalintent;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.mo.criminalintent.DatabasePackage.CrimeDB;
import com.example.mo.criminalintent.DatabasePackage.Schema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by MO on 08/06/2018.
 */

class CrimeLab {

    private static CrimeLab ourInstance;
    private List<CrimeModel> mCrimeModelList;
    static private Context mContext;
    static Schema databaseInstance;



    static CrimeLab getInstance(Context context) {
        if (ourInstance != null && databaseInstance != null) {
            return ourInstance;
        } else
            ourInstance = new CrimeLab(context);
        return ourInstance;
    }

    public List<CrimeModel> getCache() {
        return mCrimeModelList;
    }

    public List<CrimeModel> getAllCrimes() {
        mCrimeModelList = Transform(databaseInstance.CrimeDao().getAllCrimes());
        return (mCrimeModelList == null) ? (new ArrayList<CrimeModel>()) : mCrimeModelList;
    }

    public void populateCrimes() {
        for (int i = 0; i < 5; i++)
            mCrimeModelList.add(new CrimeModel("Crime Number #" + (i + 1), (i % 2 == 0)));
    }

    public boolean appendNewCrime(CrimeModel model) {
        if (model == null) return false;
        databaseInstance.CrimeDao().insert(model.getDBModel());
        mCrimeModelList.add(model);
        return true;
    }

    private CrimeLab(Context ctx) {
        mContext = ctx;
        mCrimeModelList = new ArrayList<>();
        databaseInstance = Room.databaseBuilder(ctx,
                Schema.class, Schema.DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public CrimeModel getCrime(UUID crimeID) {
        CrimeDB dbModel = databaseInstance.CrimeDao().getCrimeById(crimeID.toString());
        if(dbModel == null) {
            return null;
        }else
             return  dbModel.getCrimeModel();
    }

    public List<CrimeModel> getFilteredList(boolean isSolved){
        List<CrimeModel> mCrimeModelList = Transform(databaseInstance.CrimeDao().getFilteredList(isSolved));
        return  mCrimeModelList == null ? (new ArrayList<CrimeModel>()) : mCrimeModelList;
    }

    public boolean deleteCrime(UUID crimeID) {

        if(crimeID == null) {
            return false;
        }
            CrimeModel tobeDeleted = getCrime(crimeID);
            if(tobeDeleted == null){
                return false ;
            }
            databaseInstance.CrimeDao().delete(tobeDeleted.getDBModel());
            return true;
    }
    private List<CrimeModel> Transform(List<CrimeDB> dbModels) {
        List<CrimeModel> data = new ArrayList<>();
        for (CrimeDB crime : dbModels)
            data.add(crime.getCrimeModel());
        return data;
    }

    public void UpdateCrime(CrimeModel crime) {
        for (int i = 0; i < mCrimeModelList.size(); i++) {
            if (crime.getId().equals(mCrimeModelList.get(i).getId())) {
                mCrimeModelList.set(i, crime);

                break;
            }
        }

        databaseInstance.CrimeDao().updateCrime(crime.getDBModel());
    }
}
