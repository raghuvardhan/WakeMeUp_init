package com.raghu.android.wakemeup.Database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.Database.Entities.TimeTableTask;

import java.util.List;

@Dao
public interface TimeTableTaskDao {
    @Insert
    public long insertTimeTableTask(TimeTableTask timeTableTask);

    @Delete
    public void deleteTimeTableTask(TimeTableTask timeTableTask);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateTimeTableTask(TimeTableTask timeTableTask);

    @Query("SELECT * FROM TimeTableTask")
    public LiveData<List<TimeTableTask>> getTimeTableTasks();

    @Query("SELECT * FROM TimeTableTask WHERE timeTableTaskId = :timeTableTaskId")
    public TimeTableTask getTimeTableTaskById(int timeTableTaskId);

}
