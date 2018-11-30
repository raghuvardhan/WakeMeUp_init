package com.raghu.android.wakemeup.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
public class TimeTableTask implements Serializable{
    @PrimaryKey(autoGenerate = true)
    int timeTableTaskId;

    String timeTableTask;
    Date startTime;
    Date endTime;

    public TimeTableTask(int timeTableTaskId, String timeTableTask, Date startTime, Date endTime) {
        this.timeTableTaskId = timeTableTaskId;
        this.timeTableTask = timeTableTask;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Ignore
    public TimeTableTask(String timeTableTask, Date startTime, Date endTime) {
        this.timeTableTask = timeTableTask;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getTimeTableTaskId() {
        return timeTableTaskId;
    }

    public void setTimeTableTaskId(int timeTableTaskId) {
        this.timeTableTaskId = timeTableTaskId;
    }

    public String getTimeTableTask() {
        return timeTableTask;
    }

    public void setTimeTableTask(String timeTableTask) {
        this.timeTableTask = timeTableTask;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TimeTableTask{" +
                "timeTableTaskId=" + timeTableTaskId +
                ", timeTableTask='" + timeTableTask + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}