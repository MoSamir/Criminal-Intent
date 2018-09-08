package com.example.mo.criminalintent.DatabasePackage;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {CrimeDB.class} , version = 4 , exportSchema = false )
@TypeConverters(Converters.class)
abstract public class Schema extends RoomDatabase {
    static public String DATABASE_NAME = "CrimesDB";
    abstract public CrimeDao CrimeDao();
}
