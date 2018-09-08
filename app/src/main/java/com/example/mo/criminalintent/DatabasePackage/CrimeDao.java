package com.example.mo.criminalintent.DatabasePackage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import static android.arch.persistence.room.OnConflictStrategy.ABORT;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
import static android.arch.persistence.room.OnConflictStrategy.ROLLBACK;

@Dao
public interface CrimeDao {

@Query("SELECT * FROM  CrimeDB")
   public List<CrimeDB> getAllCrimes();

@Insert(onConflict = REPLACE)
    public void insert(CrimeDB crimeDB);

@Delete
    public void delete(CrimeDB crimeDB);

@Query("SELECT * FROM CrimeDB WHERE uid  LIKE :param1 ")
    public CrimeDB getCrimeById(String param1);

@Update(onConflict = ABORT)
    public void updateCrime(CrimeDB updateCrime);


@Query("SELECT * FROM CrimeDB WHERE isSolved = :param1")
    public List<CrimeDB> getFilteredList(boolean param1);

}