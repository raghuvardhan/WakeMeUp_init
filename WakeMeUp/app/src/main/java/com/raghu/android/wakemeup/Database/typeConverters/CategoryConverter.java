package com.raghu.android.wakemeup.Database.typeConverters;

import android.arch.persistence.room.TypeConverter;

import com.raghu.android.wakemeup.Database.Entities.Category;

public class CategoryConverter {
    
    @TypeConverter
    public static String CategorytoString(Category category){
        return category == null ? null : category.getName();
    }

    @TypeConverter
    public static Category StringtoCategory(String name){
        return name == null ? null : new Category(name);
    }
}
