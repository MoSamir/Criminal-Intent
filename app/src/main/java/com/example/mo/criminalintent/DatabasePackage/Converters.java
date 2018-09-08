package com.example.mo.criminalintent.DatabasePackage;
import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.UUID;

public class Converters implements Serializable{

    @TypeConverter
    public Long fromDateToString(Date date) {
        if (date == null)
            return null;
        return date.getTime();
    }

    @TypeConverter
    public Date fromStringToDate(Long dateLong) {
        return new Date(dateLong);
    }

    @TypeConverter
    public String fromUUIDtoString(UUID id) {
        if (id == null) return null;
        return id.toString();
    }

    @TypeConverter
    public UUID fromStringtoUUID(String id) {
        return UUID.fromString(id);
    }
}

