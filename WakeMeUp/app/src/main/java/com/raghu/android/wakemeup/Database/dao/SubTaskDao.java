package com.raghu.android.wakemeup.Database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.raghu.android.wakemeup.Database.Entities.SubTask;

import java.util.List;

@Dao
public interface SubTaskDao {
    @Insert
    public long insertSubTask(SubTask subTask);

    @Delete
    public void deleteSubTask(SubTask subTask);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateSubTask(SubTask subTask);

    @Query("SELECT * FROM SubTask")
    public LiveData<List<SubTask>> getSubTasks();

    @Query("SELECT * FROM SubTask WHERE subTaskId = :subTaskId")
    public SubTask getSubTaskById(int subTaskId);

    @Query("SELECT * FROM SubTask WHERE ownerId IS :ownerId")
    public List<SubTask> getSubTaskByOwnerId(int ownerId);

    @Query("DELETE FROM SubTask WHERE ownerId IS :ownerId")
    public void deleteSubTaskByOwnerId(int ownerId);

}