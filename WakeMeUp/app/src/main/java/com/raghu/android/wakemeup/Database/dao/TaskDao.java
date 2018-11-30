package com.raghu.android.wakemeup.Database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.raghu.android.wakemeup.Database.Entities.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public long insertTask(Task task);

    @Delete
    public void deleteTask(Task task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateTask(Task task);

    @Query("SELECT * FROM task")
    public LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM task WHERE taskId = :taskId")
    public Task getTaskById(int taskId);

    @Query("SELECT * FROM task WHERE date = :date")
    public List<Task> getTasksByDate(Date date);

    @Query("SELECT * FROM task WHERE completed = :completed")
    public List<Task> getTasksByStatus(boolean completed);

    @Query("SELECT * FROM task WHERE completed = :incomplete")
    public LiveData<List<Task>> getPendingTasks(boolean incomplete);

    @Query("SELECT COUNT(*) FROM task")
    public int getCountofTasks();


}