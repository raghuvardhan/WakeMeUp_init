package com.raghu.android.wakemeup.Database.typeConverters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static long DatetoLong(Date date){
        if (date == null) return Long.parseLong(null);
        else return date.getTime();
    }

    @TypeConverter
    public static Date LongtoDate(Long milliseconds){
        return milliseconds == null ? null : new Date(milliseconds);
    }
}
