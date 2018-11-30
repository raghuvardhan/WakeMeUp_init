package com.raghu.android.wakemeup.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Date;
import java.sql.Time;

@Entity(foreignKeys = @ForeignKey(
        entity = Task.class,
        parentColumns = "taskId",
        childColumns = "ownerId"))
public class SubTask {
    @PrimaryKey(autoGenerate = true)
    int subTaskId;

    int ownerId;
    String message;
    boolean completed;

    public SubTask(int subTaskId, int ownerId, String message, boolean completed) {
        this.subTaskId = subTaskId;
        this.ownerId = ownerId;
        this.message = message;
        this.completed = completed;
    }

    @Ignore
    public SubTask(int ownerId, String message, boolean completed) {
        this.ownerId = ownerId;
        this.message = message;
        this.completed = completed;
    }

    public int getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(int subTaskId) {
        this.subTaskId = subTaskId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
