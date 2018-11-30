package com.raghu.android.wakemeup.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.raghu.android.wakemeup.Database.Entities.Alarm;
import com.raghu.android.wakemeup.Database.Entities.Category;
import com.raghu.android.wakemeup.Database.Entities.SubTask;
import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.Database.Entities.TimeTableTask;
import com.raghu.android.wakemeup.Database.dao.AlarmDao;
import com.raghu.android.wakemeup.Database.dao.CategoryDao;
import com.raghu.android.wakemeup.Database.dao.SubTaskDao;
import com.raghu.android.wakemeup.Database.dao.TaskDao;
import com.raghu.android.wakemeup.Database.dao.TimeTableTaskDao;
import com.raghu.android.wakemeup.Database.typeConverters.CategoryConverter;
import com.raghu.android.wakemeup.Database.typeConverters.DateConverter;
import com.raghu.android.wakemeup.Database.typeConverters.TimeConverter;

@Database(entities = {Alarm.class, Task.class, Category.class, SubTask.class, TimeTableTask.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, TimeConverter.class, CategoryConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase ourInstance;
    private static final Object lock = new Object();
    private static final String DatabaseName = "Tasks";

    public static AppDatabase getInstance(Context context) {
        if(ourInstance == null){
            synchronized (lock){
                ourInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DatabaseName)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return ourInstance;
    }

    public abstract AlarmDao alarmDao();
    public abstract TaskDao taskDao();
    public abstract CategoryDao categoryDao();
    public abstract SubTaskDao subTaskDao();
    public abstract TimeTableTaskDao timeTableTaskDao();
}
